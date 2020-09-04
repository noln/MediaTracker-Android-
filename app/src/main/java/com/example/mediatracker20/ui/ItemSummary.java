package com.example.mediatracker20.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediatracker20.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

import model.exceptions.DataExistAlreadyException;
import model.exceptions.ItemNotFoundException;
import model.exceptions.KeyAlreadyExistsException;
import model.model.ItemManager;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;
import model.model.MetaData;
import model.model.Tag;
import model.model.TagManager;
import model.persistence.Saver;

import static android.text.InputType.TYPE_NULL;


public class ItemSummary extends Fragment {

    private Dialog addListDialog;
    private MediaItem mediaItem;
    private View view;
    private ListManager listManager;
    private ItemManager itemManager;
    private TagManager tagManager;
    private Spinner userStatusSpinner;
    private Spinner userRatingSpinner;
    private String listName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_media_summary, container, false);
        mediaItem = (MediaItem) getArguments().getSerializable("MEDIA_ITEM");
        listName = getArguments().getString("LIST_NAME");
        listManager = ListManager.getInstance();
        itemManager = ItemManager.getInstance();
        tagManager = TagManager.getInstance();
        initializeInformation();
        initializeUserReview();
        initializeTag();
        initializeSpinners();
        return view;
    }

    private void initializeInformation() {
        ImageView mediaImage = view.findViewById(R.id.media_sum_image);
        TextView mediaEpisodes = view.findViewById(R.id.media_sum_episodes);
        TextView mediaTitle = view.findViewById(R.id.media_sum_title);
        TextView mediaDescription = view.findViewById(R.id.media_sum_descript);
        TextView siteUrl = view.findViewById(R.id.media_sum_site_url);
        TextView imageUrl = view.findViewById(R.id.media_sum_img_url);
        TextView mediaFormat = view.findViewById(R.id.media_sum_format);
        TextView mediaType = view.findViewById(R.id.media_sum_type);
        TextView mediaDate = view.findViewById(R.id.media_sum_date);
        TextView mediaId = view.findViewById(R.id.media_sum_id);
        TextView mediaRating = view.findViewById(R.id.media_sum_rating);
        Picasso.get().load(mediaItem.getItemInfo("ImageLink")).into(mediaImage);
        mediaEpisodes.setText("Episodes: " + mediaItem.getItemInfo("Episodes"));
        mediaTitle.setText(mediaItem.getItemInfo("Title"));
        mediaDescription.setText(mediaItem.getItemInfo("Plot"));
        siteUrl.setText(mediaItem.getItemInfo("WebsiteLink"));
        imageUrl.setText(mediaItem.getItemInfo("ImageLink"));
        mediaFormat.setText("Format: " + mediaItem.getItemInfo("Format"));
        mediaType.setText("Type: " + mediaItem.getItemInfo("Type"));
        mediaDate.setText("Date: " + mediaItem.getItemInfo("Date"));
        mediaId.setText("Id: " + mediaItem.getItemInfo("Id"));
        mediaRating.setText("Rating: " + mediaItem.getItemInfo("Rating"));
    }

    private void initializeUserRating() {
        userRatingSpinner = view.findViewById(R.id.media_sum_user_rating_spin);
        String[] ratings = new String[]{"N/A", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> ratingAdapter = new ArrayAdapter<String>(getContext(), R.layout.card_spinner_item, ratings);
        userRatingSpinner.setAdapter(ratingAdapter);
        String userRating = mediaItem.getItemInfo("UserRating");
        if(userRating.length() != 0) {
            userRatingSpinner.setSelection(Integer.parseInt(userRating));
        }
        userRatingSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(itemInAList()) {
                    userRatingSpinner.setEnabled(true);
                } else {
                    userRatingSpinner.setEnabled(false);
                }
                return false;
            }
        });
        userRatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userRatingSpinner.setSelection(position);
                mediaItem.setItemInfo("UserRating", userRatingSpinner.getSelectedItem().toString());
                Saver.getInstance().appChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    private void initializeUserStatus() {
        userStatusSpinner = view.findViewById(R.id.media_sum_user_status_spin);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.status_spinner_array, R.layout.card_spinner_item);
        userStatusSpinner.setAdapter(statusAdapter);
        String[] statusArray = getResources().getStringArray(R.array.status_spinner_array);
        String userStatus = mediaItem.getItemInfo("Status");
        if(userStatus.length() != 0) {
            int c = 0;
            for(String s: statusArray) {
                if(s.equals(userStatus)) {
                    break;
                }
                c++;
            }
            userStatusSpinner.setSelection(c);
        }
        userStatusSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!itemInAList()) {
                    userStatusSpinner.setEnabled(false);
                }
                return false;
            }
        });
        userStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userStatusSpinner.setSelection(position);
                mediaItem.setItemInfo("Status", userStatusSpinner.getSelectedItem().toString());
                Saver.getInstance().appChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    private void initializeSpinners() {
        initializeUserRating();
        initializeUserStatus();
    }

    private void initializeUserReview() {
        TextView userReview = view.findViewById(R.id.media_sum_user_rev);
        userReview.setText(mediaItem.getItemInfo("UserReview"));
        userReview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(ItemManager.getInstance().contains(mediaItem)) {
                    if(userReview.getInputType() == TYPE_NULL) {
                        userReview.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                    }
                } else {
                    if(userReview.getInputType() != TYPE_NULL) {
                        userReview.setInputType(TYPE_NULL);
                    }
                }
                return false;
            }
        });
        initializeReviewButtons(userReview);
    }

    private void initializeReviewButtons(TextView userReview) {
        TextView saveButton = view.findViewById(R.id.media_sum_user_rev_save);
        TextView cancelButton = view.findViewById(R.id.media_sum_user_rev_canc);
        TextView deleteButton = view.findViewById(R.id.media_sum_user_rev_clear);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemInAList()) {
                    mediaItem.setItemInfo("UserReview", userReview.getText().toString());
                }   else {
                    createToast(R.string.add_to_list_first, Toast.LENGTH_SHORT);
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemInAList()) {
                    mediaItem.setItemInfo("UserReview", "");
                    userReview.setText("");
                }   else {
                    createToast(R.string.add_to_list_first, Toast.LENGTH_SHORT);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemInAList()) {
                    userReview.setText("");
                }   else {
                    createToast(R.string.add_to_list_first, Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean itemInAList() {
        return itemManager.contains(mediaItem);
    }

    private void initializeTag() {
        LinearLayout mediaTagLayout = view.findViewById(R.id.media_sum_tag_layout);
        generateTagsInLayout(mediaTagLayout);
        TextView addTag = view.findViewById(R.id.media_sum_addtag_btn);
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemInAList()) {
                    Dialog manageTagDialog = new Dialog(getContext());
                    manageTagDialog.setContentView(R.layout.manage_tag_popup);
                    LinearLayout linearLayout = manageTagDialog.findViewById(R.id.manage_tag_item_layout);
                    List<Tag> allTags = TagManager.getInstance().getAllActiveTags().stream().collect(Collectors.toList());
                    for (Tag t : allTags) {
                        linearLayout.addView(createCheckBox(t.getTagName(), tagManager.mediaIsTaggedBy(t, mediaItem)));
                    }
                    ImageView addBtn = manageTagDialog.findViewById(R.id.new_tag_btn);
                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createNewTagDialog(linearLayout, manageTagDialog);
                            manageTagDialog.hide();
                        }
                    });
                    manageTagDialog.show();
                    manageTagDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            saveTags(linearLayout, mediaTagLayout);
                        }
                    });
                } else {
                    createToast(R.string.add_to_list_first, Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void saveTags(LinearLayout dialogLayout, LinearLayout itemLayout) {
        for(int i = 0; i < dialogLayout.getChildCount(); i++) {
            CheckBox c = (CheckBox) dialogLayout.getChildAt(i);
            try {
                if(c.isChecked()) {
                    tagManager.tagItem(tagManager.getTagByName(c.getText().toString()), mediaItem);
                } else {
                    tagManager.removeTag(tagManager.getTagByName(c.getText().toString()), mediaItem);
                }
            } catch (ItemNotFoundException | DataExistAlreadyException e) {
                System.out.println(c.getText().toString());
            }
        }
        Saver.getInstance().appChanged();
        updateMainLayout(itemLayout);
    }

    private void createNewTagDialog(LinearLayout dialogLayout, Dialog addTagDialog) {
        Dialog newTagDialog = new Dialog(getContext());
        newTagDialog.setContentView(R.layout.new_tag_popup);
        Button confirm = newTagDialog.findViewById(R.id.new_tag_confirm_button);
        TextView name = newTagDialog.findViewById(R.id.new_tag_title);
        TextView errorText = newTagDialog.findViewById(R.id.new_tag_error);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() != 0) {
                    try {
                        tagManager.addNewTag(new Tag(name.getText().toString()));
                        newTagDialog.hide();
                        CheckBox c = new CheckBox(getContext());
                        c.setTextSize(24);
                        c.setText(name.getText().toString());
                        dialogLayout.addView(c);
                        addTagDialog.show();
                        Saver.getInstance().appChanged();
                    } catch (KeyAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                } else {
                    errorText.setText(R.string.new_tag_name_empty_err);
                }
            }
        });
        newTagDialog.show();
    }

    private void generateTagsInLayout(LinearLayout linearLayout) {
        for(MetaData m: mediaItem.getMetaDataOfType("Tag")) {
            linearLayout.addView(createTag(m.getNameOfObject()));
        }
    }

    private TextView createTag(String name) {
        TextView textView = new TextView(getContext());
        textView.setText("#" + name);
        textView.setLines(1);
        textView.setTextSize(18);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 0, 0, 0);
        textView.setLayoutParams(params);
        return textView;
    }

    private void updateMainLayout(LinearLayout itemLayout) {
        itemLayout.removeAllViews();
        generateTagsInLayout(itemLayout);
    }

    private void enableSpinners() {
        userRatingSpinner.setEnabled(true);
        userStatusSpinner.setEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.media_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.media_add_list) {
           createListDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createListDialog() {
        addListDialog = new Dialog(getContext());
        addListDialog.setContentView(R.layout.media_add_to_list);
        LinearLayout linearLayout = addListDialog.findViewById(R.id.list_all_active);
        if(listManager.allActiveLists().size() == 0) {
            createToast(R.string.no_list_to_add, Toast.LENGTH_SHORT);
        } else {
            for (MediaList list : listManager.allActiveLists()) {
                linearLayout.addView(createCheckBox(list.getName(), listManager.getListOfMedia(list).contains(mediaItem)));
            }
            Button confirm = addListDialog.findViewById(R.id.media_add_list_btn);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        addDeleteToLists(linearLayout, i);
                    }
                    Saver.getInstance().appChanged();
                    addListDialog.hide();
                }
            });
            addListDialog.show();
        }
    }

    private CheckBox createCheckBox(String name, boolean checked) {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setTextSize(24);
        checkBox.setChecked(checked);
        checkBox.setText(name);
        return checkBox;
    }

    private void createToast(int stringId, int length) {
        Toast toast = Toast.makeText(getContext(), stringId, length);
        toast.show();
    }

    private void addDeleteToLists(LinearLayout linearLayout, int i) {
        CheckBox c = (CheckBox) linearLayout.getChildAt(i);
        try {
            MediaList list = listManager.getMediaListByName(c.getText().toString());
            if (c.isChecked()) {
                listManager.addMediaItemToList(list, mediaItem);
                enableSpinners();
            } else {
                listManager.deleteMediaItemFromList(list, mediaItem);
            }
        } catch (ItemNotFoundException | DataExistAlreadyException | KeyAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

}
