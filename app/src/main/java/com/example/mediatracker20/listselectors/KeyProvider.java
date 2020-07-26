package com.example.mediatracker20.listselectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.model.MediaList;

public class KeyProvider extends ItemKeyProvider {

    private final List<MediaList> itemList;

    public KeyProvider(int scope, List<MediaList> itemList) {
        super(scope);
        this.itemList = itemList;
    }


    @Nullable
    @Override
    public Object getKey(int position) {
        return itemList.get(position);
    }

    @Override
    public int getPosition(@NonNull Object key) {
        return itemList.indexOf(key);
    }
}
