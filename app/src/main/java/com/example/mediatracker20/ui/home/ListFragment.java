package com.example.mediatracker20.ui.home;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ActionMode;
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
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.OnDragInitiatedListener;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatracker20.R;

import model.jsonreaders.ItemManagerDocument;
import model.jsonreaders.ListManagerDocument;
import model.jsonreaders.TagManagerDocument;
import model.model.ItemManager;
import model.model.MediaItem;
import model.model.Tag;
import model.model.TagManager;
import model.persistence.ReaderLoader;
import model.persistence.Saver;
import com.example.mediatracker20.listselectors.ActionModeController;
import com.example.mediatracker20.listselectors.KeyProviderLists;
import com.example.mediatracker20.listselectors.ListItemLookup;
import com.example.mediatracker20.adapters.MediaListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Organize.Sorter;
import model.exceptions.EmptyStringException;
import model.exceptions.KeyAlreadyExistsException;
import model.model.ListManager;
import model.model.MediaList;
import model.persistence.Reader;

//handles the main menu -> where all lists are displayed and interacted
public class ListFragment extends Fragment {

    private ListViewModel homeViewModel;

    private View rootView; //item in the views
    private List<MediaList> allLists;
    private ListManager listManager;
    private ItemManager itemManager;
    private TagManager tagManager;
    private MediaListAdapter mediaListAdapter;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private FloatingActionButton fab;
    private Dialog newListDialog;
    private ActionMode actionMode;
    private Menu menu;
    private  SearchView searchView;
    private SelectionTracker selectionTracker;

    //Handle view creation and ui actions
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        homeViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_list, container, false);
        listManager = ListManager.getInstance();
        initializeSearchView();
        initializeSpinner();
        initializeRecyclerView();
        initializeSelectionTracker();
        initializeFab();
        sortList(allLists);
        return rootView;
    }

    //Initialize the search interface
    private void initializeSearchView() {
        searchView = rootView.findViewById(R.id.list_Frag_search);
        searchView.setQueryHint("Search");
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mediaListAdapter.filter(newText);
                return false;
            }
        });
    }


    //initialize list of cardViews to show all lists
    private void initializeRecyclerView() {
        recyclerView = rootView.findViewById(R.id.list_frag_recyc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allLists = listManager.allActiveLists().stream().collect(Collectors.toList());
        mediaListAdapter = new MediaListAdapter(allLists, actionMode);
        recyclerView.setAdapter(mediaListAdapter);
    }

    //initialize spinner for different sorting methods
    private void initializeSpinner() {
        spinner = rootView.findViewById(R.id.list_frag_sort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.list_spinner_array, R.layout.spinner_item_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortList(allLists);
                searchView.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Sorter.listSortByName(allLists);
            }
        });
    }

    //initialize a selection tracker to allow multiple item selections
    private void initializeSelectionTracker() {
        selectionTracker = new SelectionTracker.Builder<>(
                "my-selection-id",
                recyclerView,
                new KeyProviderLists(1, allLists),
                new ListItemLookup(recyclerView),
                StorageStrategy.createLongStorage()
        )
                .withOnDragInitiatedListener(new OnDragInitiatedListener() {
                    @Override
                    public boolean onDragInitiated(@NonNull MotionEvent e) {
                        Log.d("drag", "onDragInitiated");
                        return true;
                    }
                }).build();
        mediaListAdapter.setSelectionTracker(selectionTracker);
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (selectionTracker.hasSelection() && actionMode == null) {
                    actionMode = getActivity().startActionMode(new ActionModeController(getActivity(),
                            selectionTracker, allLists, mediaListAdapter));
                    Integer size = selectionTracker.getSelection().size();
                    actionMode.setTitle(size.toString());
                    fab.setClickable(false);
                    fab.hide();
                    searchView.clearFocus();
                    searchView.setInputType(InputType.TYPE_NULL);
                    spinner.setEnabled(false);
                } else if (!selectionTracker.hasSelection() && actionMode != null) {
                    actionMode.finish();
                    actionMode = null;
                    fab.setClickable(true);
                    fab.show();
                    searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                    spinner.setEnabled(true);
                } else if (selectionTracker.hasSelection()){
                    Integer size = selectionTracker.getSelection().size();
                    actionMode.setTitle(size.toString());
                }
            }
        });

    }

    //initialize fab to for new item creation.
    private void initializeFab() {
        newListDialog = new Dialog(getContext());
        fab = rootView.findViewById(R.id.list_frag_new_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.clearFocus();
                newListDialog.setContentView(R.layout.new_list_popup);
                newListDialog.show();
                Button confirm = newListDialog.findViewById(R.id.new_list_confirm_btn);
                TextView errorText = newListDialog.findViewById(R.id.new_list_error);
                errorText.setVisibility(View.GONE);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchView.setQuery("", false);
                        EditText name = newListDialog.findViewById(R.id.new_list_title);
                        try {
                            MediaList newList = new MediaList(name.getText().toString());
                            listManager.addNewList(newList);
                            allLists.add(newList);
                            newListDialog.dismiss();
                            mediaListAdapter.syncLists();
                            sortList(allLists);
                            Saver.getInstance().appChanged();
                        } catch (KeyAlreadyExistsException e) {
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText(getString(R.string.list_already_exist));
                        } catch (EmptyStringException e) {
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText(getString(R.string.list_empty_name));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //sort list given based on the current spinner value
    private void sortList(List<MediaList> allLists) {
        switch (spinner.getSelectedItem().toString()) { //would use id but can't accept long
            case "Recent":
                Sorter.listSortByLatestAccess(allLists);
                break;
            case "Date Created":
                Sorter.listSortByDateCreated(allLists);
                break;
            case "Most Used":
                Sorter.listSortByFrequency(allLists);
                break;
            default:
                Sorter.listSortByName(allLists);
                break;
        }
        mediaListAdapter.notifyDataSetChanged();
    }


}
