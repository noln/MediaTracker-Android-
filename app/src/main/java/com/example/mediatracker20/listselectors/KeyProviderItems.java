package com.example.mediatracker20.listselectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import java.util.List;

import model.model.MediaItem;

//Key provider for mediaItems
public class KeyProviderItems extends ItemKeyProvider {

    private final List<MediaItem> mediaItems;


    public KeyProviderItems(int scope, List<MediaItem> mediaItems) {
        super(scope);
        this.mediaItems = mediaItems;
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return null;
    }

    @Override
    public int getPosition(@NonNull Object key) {
        return 0;
    }
}
