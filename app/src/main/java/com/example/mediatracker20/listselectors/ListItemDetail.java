package com.example.mediatracker20.listselectors;

import android.content.ClipData;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

import model.model.MediaItem;
import model.model.MediaList;

//provide selection tracker item details
public class ListItemDetail extends ItemDetailsLookup.ItemDetails {

    private final int adapterPosition;
    private final MediaList listSelectionKey;
    private final MediaItem itemSelectionKey;

    public ListItemDetail(int adapterPosition, MediaList selectionKey) {
        this.adapterPosition = adapterPosition;
        this.listSelectionKey = selectionKey;
        this.itemSelectionKey = null;
    }

    public ListItemDetail(int adapterPosition, MediaItem selectionKey) {
        this.adapterPosition = adapterPosition;
        this.itemSelectionKey = selectionKey;
        this.listSelectionKey = null;
    }


    @Override
    public int getPosition() {
        return adapterPosition;
    }

    @Nullable
    @Override
    public Object getSelectionKey() {
        return listSelectionKey == null ? itemSelectionKey : listSelectionKey;
    }
}
