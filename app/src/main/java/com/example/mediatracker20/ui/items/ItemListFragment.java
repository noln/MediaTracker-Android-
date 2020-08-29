package com.example.mediatracker20.ui.items;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.OnDragInitiatedListener;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.mediatracker20.R;
import com.example.mediatracker20.adapters.MediaItemAdapter;
import com.example.mediatracker20.listselectors.ActionModeController;
import com.example.mediatracker20.listselectors.KeyProviderItems;
import com.example.mediatracker20.listselectors.ListItemLookup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import model.Organize.Sorter;
import model.exceptions.ItemNotFoundException;
import model.model.ItemManager;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;


public class ItemListFragment extends Fragment {

    private ItemListViewModel mViewModel;

    //items in the list
    private List<MediaItem> allItems;

    //Info Managers
    private ListManager listManager;
    private ItemManager itemManager;

    //Views
    private View rootView;
    private MediaItemAdapter mediaItemAdapter;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private ActionMode actionMode;
    private Menu menu;
    private SearchView searchView;

    //current list
    private String listName;
    private MediaList chosenList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_list_fragment, container, false);
        listName = getArguments().getString("LIST_NAME");
        listManager = ListManager.getInstance();
        itemManager = ItemManager.getInstance();
        initializeRecyclerView();
        initializeSearchView();
        initializeSpinner();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ItemListViewModel.class);
    }

    private void initializeSearchView() {
        searchView = rootView.findViewById(R.id.item_search);
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
                mediaItemAdapter.filter(newText);
                return false;
            }
        });
    }

//    //initialize list of cardViews to show all lists
    private void initializeRecyclerView() {
        recyclerView = rootView.findViewById(R.id.card_display);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        try {
            chosenList = listManager.getMediaListByName(listName);
        } catch (ItemNotFoundException e) {
            e.printStackTrace();
        }
        allItems = listManager.getListOfMedia(chosenList);
        mediaItemAdapter = new MediaItemAdapter(allItems, chosenList, R.id.action_itemListFragment_to_itemSumamry);
        recyclerView.setAdapter(mediaItemAdapter);
    }
//
    //initialize spinner for different sorting methods
    private void initializeSpinner() {
        spinner = rootView.findViewById(R.id.sort_choice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.item_spinner_array, R.layout.spinner_item_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortItemList(allItems);
                searchView.clearFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Sorter.mediaSortByName(allItems);
            }
        });
    }

    //initialize a selection tracker to allow multiple item selections
    private void initializeSelectionTracker() {
        SelectionTracker selectionTracker = new SelectionTracker.Builder<>(
                "my-selection-id",
                recyclerView,
                new KeyProviderItems(1, allItems),
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
        mediaItemAdapter.setSelectionTracker(selectionTracker);
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (selectionTracker.hasSelection() && actionMode == null) {
                    actionMode = getActivity().startActionMode(new ActionModeController(getActivity(),
                            selectionTracker, allItems, listName, mediaItemAdapter));
                    Integer size = selectionTracker.getSelection().size();
                    actionMode.setTitle(size.toString());
                    searchView.clearFocus();
                    searchView.setInputType(InputType.TYPE_NULL);
                } else if (!selectionTracker.hasSelection() && actionMode != null) {
                    actionMode.finish();
                    actionMode = null;
                    searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (selectionTracker.hasSelection()){
                    Integer size = selectionTracker.getSelection().size();
                    actionMode.setTitle(size.toString());
                }
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
    private void sortItemList(List<MediaItem> allItems) {
        switch (spinner.getSelectedItem().toString()) {
            case "Name":
                Sorter.mediaSortByName(allItems);
                break;
            case "Date Added":
                Sorter.mediaSortByDateAdded(allItems, listName);
                break;
            case "Rating":
                Sorter.mediaSortByRating(allItems);
                break;
            default:
                Sorter.mediaSortByName(allItems);
                break;
        }
        mediaItemAdapter.notifyDataSetChanged();
    }

}
