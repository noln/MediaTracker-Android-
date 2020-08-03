package com.example.mediatracker20.listselectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import java.util.List;

import model.model.MediaList;

//Key provider for MediaLists
public class KeyProviderLists extends ItemKeyProvider {

    private final List<MediaList> mediaLists;


    public KeyProviderLists(int scope, List<MediaList> mediaLists) {
        super(scope);
        this.mediaLists = mediaLists;
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return mediaLists.get(position);
    }

    @Override
    public int getPosition(@NonNull Object key) {
        return mediaLists.indexOf(key);
    }
}
