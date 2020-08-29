package com.example.mediatracker20.ui.search;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediatracker20.R;
import com.example.mediatracker20.adapters.SearchSourcesAdapter;
import com.example.mediatracker20.adapters.sources.MediaSources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.requests.ApiKey;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private RecyclerView recyclerView;
    private List<MediaSources> allSources;
    private Dialog setKeyDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        recyclerView = root.findViewById(R.id.search_frag_recyc);
        createSources();
        initializeRecyclerView(recyclerView);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_setting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.set_key) {
            setKeyDialog = new Dialog(getContext());
            setKeyDialog.setContentView(R.layout.key_enter_popup);
            EditText key = setKeyDialog.findViewById(R.id.key_enter_api);
            Button button = setKeyDialog.findViewById(R.id.key_enter_confirm);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userKey = key.getText().toString();
                    System.out.println(userKey);
                    ApiKey.setKey(userKey);
                    File keyFile = new File(getActivity().getFilesDir(), "key.txt");
                    try {
                        FileOutputStream stream = new FileOutputStream(keyFile);
                        stream.write(userKey.getBytes());
                        stream.close();
                        setKeyDialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            setKeyDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createSources() {
        allSources = new ArrayList<>();
        allSources.add(new MediaSources(R.mipmap.mal_foreground,getString(R.string.my_anime_list_anime), "anime"));
        allSources.add(new MediaSources(R.mipmap.ani_list_foreground, getString(R.string.ani_list_anime), "anime"));
        allSources.add(new MediaSources(R.mipmap.mal_foreground, getString(R.string.my_anime_list_manga), "manga"));
        allSources.add(new MediaSources(R.mipmap.ani_list_foreground, getString(R.string.ani_list_manga), "manga"));
        allSources.add(new MediaSources(R.mipmap.imdb_foreground, "IMDB", "general"));

    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        SearchSourcesAdapter searchSourcesAdapter = new SearchSourcesAdapter(allSources);
        recyclerView.setAdapter(searchSourcesAdapter);
    }
}
