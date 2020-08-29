package com.example.mediatracker20.ui.search.data.searchDisplay;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediatracker20.R;
import com.example.mediatracker20.adapters.MediaItemDisplayAdapter;
import com.example.mediatracker20.adapters.MediaListAdapter;
import com.example.mediatracker20.ui.search.data.SourceChooser;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.exceptions.EmptyStringException;
import model.jsonreaders.AnimeReader;
import model.model.MediaItem;
import model.requests.AnimeRequest;

public class AnimeSearchDisplay extends Fragment {

    private AnimeRequest animeRequest;
    private AnimeReader animeReader;
    private String sourceTitle;
    private String query;

    private RecyclerView recyclerView;
    private View view;
    private MediaItemDisplayAdapter mediaListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_anime_search_display, container, false);
        sourceTitle = getArguments().getString("SOURCE");
        query = getArguments().getString("QUERY");
        animeReader = SourceChooser.getAnimeRead(sourceTitle);
        animeRequest = SourceChooser.getAnimeReq(sourceTitle);
        initializeRecyclerView();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new SearchAnime().execute(query);
    }

    //initialize list of cardViews to show all lists
    private void initializeRecyclerView() {
        recyclerView = view.findViewById(R.id.card_display);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mediaListAdapter = new MediaItemDisplayAdapter(new ArrayList<>(), R.id.action_animeSearchDisplay_to_itemSumamry);
        recyclerView.setAdapter(mediaListAdapter);
    }


    private class SearchAnime extends AsyncTask<String, Void, Void> {

        List<MediaItem> result;
        @Override
        protected Void doInBackground(String... strings) {
            try {
                result = animeReader.readSearch(animeRequest.search(strings[0], getContext()), 50);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mediaListAdapter.addList(result);
            mediaListAdapter.notifyDataSetChanged();
        }
    }
}
