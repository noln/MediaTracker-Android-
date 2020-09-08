package com.example.mediatracker20.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mediatracker20.R;
import com.example.mediatracker20.listselectors.ListItemDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.model.ItemManager;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;

//Adapter for MediaItems Recyclerview
public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.MediaItemViewHolder> {

    private static List<MediaItem> allItems;
    private static List<MediaItem> displayItems;
    private static ListManager listManager;
    private MediaList selectedList;
    private SelectionTracker selectionTracker;
    private static ItemManager itemManager;
    private int actionId;

    public class MediaItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle;
        public TextView itemRating;
        public TextView itemStatus;
        public ImageView itemImage;
        public TextView itemEpisodes;
        public RelativeLayout layout;


        public MediaItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.media_card_title);
            itemRating = itemView.findViewById(R.id.media_card_rating);
            itemStatus = itemView.findViewById(R.id.media_card_status);
            itemImage = itemView.findViewById(R.id.media_card_image);
            itemEpisodes = itemView.findViewById(R.id.media_card_episodes);
            layout = itemView.findViewById(R.id.media_card_layout);
        }

        public ItemDetailsLookup.ItemDetails getItemDetails() {
            return new ListItemDetail(getAdapterPosition(), displayItems.get(getAdapterPosition()));
        }

        public final void bind(MediaItem list, boolean isActive) {
            layout.setActivated(isActive);
        }
    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void syncLists() {
        allItems.clear();
        allItems.addAll(displayItems);
    }

    @NonNull
    @Override
    public MediaItemAdapter.MediaItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item_card, parent, false);
        MediaItemAdapter.MediaItemViewHolder evh = new MediaItemAdapter.MediaItemViewHolder(v);
        return evh; //evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MediaItemAdapter.MediaItemViewHolder holder, int position) {
        MediaItem item = displayItems.get(position);
        holder.itemRating.setText("Rating: " + item.getItemInfo("Rating"));
        holder.itemStatus.setText("Status: " + item.getItemInfo("Status"));
        holder.itemTitle.setText(item.getItemInfo("Title"));
        holder.itemEpisodes.setText("Episodes: " + item.getItemInfo("Episodes"));
        Picasso.get().load(item.getItemInfo("ImageLink")).into(holder.itemImage);
        holder.bind(item, selectionTracker.isSelected(item));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectionTracker.hasSelection()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("MEDIA_ITEM", item);
                    bundle.putString("LIST_NAME", selectedList.getListName());
                    Navigation.findNavController(holder.itemView).navigate(actionId, bundle);
                } else {
                    if(selectionTracker.isSelected(item)) {
                        selectionTracker.deselect(item);
                    } else {
                        selectionTracker.select(item);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return displayItems.size();
    }

    public MediaItemAdapter(List<MediaItem> itemsList, MediaList mediaList, int navActionId) {
        displayItems = itemsList;
        allItems = new ArrayList<>();
        allItems.addAll(displayItems);
        listManager = ListManager.getInstance();
        itemManager = ItemManager.getInstance();
        this.selectedList = mediaList;
        actionId = navActionId;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        displayItems.clear();
        System.out.println(charText.length());
        if (charText.length() == 0) {
            displayItems.addAll(allItems);
        } else {
            for (MediaItem wp : allItems) {
                if (wp.getItemInfo("Title").toLowerCase().contains(charText)) {
                    displayItems.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void addList(List<MediaItem> list) {
        if (list == null) {
            Log.e("list is null", "list is null");
        } else {
            displayItems.addAll(list);
            this.allItems.addAll(list);
        }
    }

}
