package com.example.mediatracker20.listselectors;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatracker20.adapters.MediaItemAdapter;
import com.example.mediatracker20.adapters.MediaListAdapter;

//MediaList look up for selection tracker
public class ListItemLookup extends ItemDetailsLookup {

    private final RecyclerView recyclerView;

    public ListItemLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
            if (holder instanceof MediaListAdapter.MediaListViewHolder) {
                return ((MediaListAdapter.MediaListViewHolder) holder).getItemDetails();
            } else if (holder instanceof MediaItemAdapter.MediaItemViewHolder) {
                return ((MediaItemAdapter.MediaItemViewHolder) holder).getItemDetails();
            }
        }
        return null;
    }
}
