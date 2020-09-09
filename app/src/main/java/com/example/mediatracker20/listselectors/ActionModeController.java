package com.example.mediatracker20.listselectors;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.recyclerview.selection.SelectionTracker;

import com.example.mediatracker20.R;
import com.example.mediatracker20.adapters.MediaItemAdapter;
import com.example.mediatracker20.adapters.MediaListAdapter;

import java.util.Iterator;
import java.util.List;


import model.exceptions.ItemNotFoundException;
import model.google.Database;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;

//Controls action mode for selection trackers
public class ActionModeController implements ActionMode.Callback {

    private Context context;
    private SelectionTracker selectionTracker;
    private List<MediaList> currentList = null;
    private List<MediaItem> currentItems = null;
    private ListManager listManager;
    private String listName;
    private boolean isList;
    private ActionMode actionMode;
    private MediaListAdapter mediaListAdapter;
    private MediaItemAdapter mediaItemAdapter;


    public ActionModeController(Context context, SelectionTracker selectionTracker, List<MediaList> list, MediaListAdapter adapter) {
        construct(context, selectionTracker, adapter);
        currentList = list;
        isList = true;
    }

    public ActionModeController(Context context, SelectionTracker selectionTracker, List<MediaItem> mediaItemList, String listName, MediaItemAdapter adapter) {
        construct(context, selectionTracker, adapter);
        currentItems = mediaItemList;
        this.listName = listName;
        isList = false;
    }

    private void construct(Context context, SelectionTracker selectionTracker, MediaListAdapter adapter) {
        this.context = context;
        this.selectionTracker = selectionTracker;
        this.listManager = ListManager.getInstance();
        this.mediaListAdapter = adapter;
    }

    private void construct(Context context, SelectionTracker selectionTracker, MediaItemAdapter adapter) {
        this.context = context;
        this.selectionTracker = selectionTracker;
        this.listManager = ListManager.getInstance();
        this.mediaItemAdapter = adapter;
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.selection_action_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(isList) {
            handleListActions(item, mode);
        } else {
            handleItemActions(item, mode);
        }
        return true;
    }

    private void handleListActions(MenuItem item, ActionMode mode) {
        Iterator<MediaList> listIterator = selectionTracker.getSelection().iterator();
        switch (item.getTitle().toString()) {
            case "Delete":
                while (listIterator.hasNext()) {
                    MediaList next = listIterator.next();
                    currentList.remove(next);
                    try {
                        listManager.removeList(next);
                        Database.deleteInfo("Lists", next.hashCode());
                    } catch (ItemNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                onDestroyActionMode(mode);
                mediaListAdapter.notifyDataSetChanged();
                //snackbar
                break;
            case "Move Item":
                break;
            default:
                break;
        }
    }

    private void handleItemActions(MenuItem item, ActionMode mode) {
        Iterator<MediaItem> itemIterator = selectionTracker.getSelection().iterator();

    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mode.finish();
        selectionTracker.clearSelection();
    }

}
