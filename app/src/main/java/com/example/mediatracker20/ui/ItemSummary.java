package com.example.mediatracker20.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediatracker20.R;

import model.model.MediaItem;


public class ItemSummary extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MediaItem item = (MediaItem) getArguments().getSerializable("MEDIA_ITEM");
        System.out.println(item.getItemInfo("Title"));
        return inflater.inflate(R.layout.fragment_item_summary, container, false);
    }
}
