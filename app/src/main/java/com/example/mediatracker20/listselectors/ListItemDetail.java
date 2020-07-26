package com.example.mediatracker20.listselectors;

import android.content.ClipData;

import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

import model.model.MediaList;

public class ListItemDetail extends ItemDetailsLookup.ItemDetails {

    private final int adapterPosition;
    private final MediaList selectionKey;

    public ListItemDetail(int adapterPosition, MediaList selectionKey) {
        this.adapterPosition = adapterPosition;
        this.selectionKey = selectionKey;
    }

    @Override
    public int getPosition() {
        return adapterPosition;
    }

    @Nullable
    @Override
    public Object getSelectionKey() {
        return selectionKey;
    }
}
