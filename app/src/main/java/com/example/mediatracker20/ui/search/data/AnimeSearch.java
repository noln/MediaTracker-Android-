package com.example.mediatracker20.ui.search.data;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediatracker20.R;
import com.example.mediatracker20.adapters.MediaItemDisplayAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.jsonreaders.AnimeReader;
import model.model.MediaItem;
import model.requests.AnimeRequest;
import model.requests.SearchSaver;

public class AnimeSearch extends Fragment {

    private MediaItemDisplayAdapter pagerCardAdapter;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private View view;
    private LinearLayout trendingLayout;
    private LinearLayout recommendedLayout;
    private ProgressBar trendingProgress;
    private ProgressBar recommendProgress;
    private AnimeReader animeReader;
    private AnimeRequest animeRequest;

    private String sourceTitle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view =  inflater.inflate(R.layout.anime_search_fragment, container, false);
        tabLayout = view.findViewById(R.id.media_tab);
        viewPager2 = view.findViewById(R.id.media_pager);
        trendingProgress = view.findViewById(R.id.anime_search_trend_prog);
        recommendProgress = view.findViewById(R.id.media_rec_prog);
        tabLayout.setVisibility(View.GONE);
        viewPager2.setVisibility(View.GONE);
        recommendedLayout = view.findViewById(R.id.media_recommend);
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        pagerCardAdapter = new MediaItemDisplayAdapter(mediaItems, R.id.action_animeSearch_to_itemSumamry);
        pagerCardAdapter.isPager(true);
        viewPager2.setAdapter(pagerCardAdapter);
        trendingLayout = view.findViewById(R.id.anime_search_trend_layout);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText("")
        ).attach();
        sourceTitle = getArguments().getString("Source_Title");
        animeReader = SourceChooser.getAnimeRead(sourceTitle);
        animeRequest = SourceChooser.getAnimeReq(sourceTitle);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(sourceTitle);
        if (SearchSaver.getInstance().getAnimeSearch(sourceTitle) != null) {
            generateView(SearchSaver.getInstance().getAnimeSearch(sourceTitle));
        } else if(isConnected()) {
            new MyTask().execute();
        } else {
            Toast toast = Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void generateView(Pair<List<MediaItem>, List<MediaItem>> items) {
        pagerCardAdapter.addList(items.first);
        pagerCardAdapter.notifyDataSetChanged();
        createTopAiring(items.second);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager2.setVisibility(View.VISIBLE);
        recommendProgress.setVisibility(View.GONE);
        trendingProgress.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("QUERY", query);
                    bundle.putString("SOURCE", sourceTitle);
                    Navigation.findNavController(view).navigate(R.id.action_animeSearch_to_animeSearchDisplay, bundle);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private class MyTask extends AsyncTask<Void, Void, Void> {
        List<MediaItem> randomItems;
        List<MediaItem> trendingItems;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                randomItems = animeReader.readTop(animeRequest.getRecommended(), 5);
                trendingItems = animeReader.readTop(animeRequest.getTrending(), 20);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (randomItems == null || randomItems.isEmpty()) {
                    TextView textView = new TextView(getContext());
                    textView.setGravity(Gravity.CENTER);
                    textView.setText("Failed to get information. Try later or another source");
                    recommendedLayout.addView(textView);
                } else {
                    pagerCardAdapter.addList(randomItems);
                    pagerCardAdapter.notifyDataSetChanged();
                }
                if (trendingItems == null || trendingItems.isEmpty()) {
                    TextView textView = new TextView(getContext());
                    textView.setGravity(Gravity.CENTER);
                    textView.setText("Failed to get information. Try later or another source");
                    trendingLayout.addView(textView);
                } else {
                    createTopAiring(trendingItems);
                }
                tabLayout.setVisibility(View.VISIBLE);
                viewPager2.setVisibility(View.VISIBLE);
                recommendProgress.setVisibility(View.GONE);
                trendingProgress.setVisibility(View.GONE);
                if(trendingItems != null && !trendingItems.isEmpty() && !randomItems.isEmpty() && randomItems != null) {
                    SearchSaver.getInstance().setAnimeSearch(sourceTitle, new Pair<>(randomItems, trendingItems));
                }
                super.onPostExecute(aVoid);
            } catch (NullPointerException e){
                return;
            }
        }
    }

    private void createTopAiring(List<MediaItem> trendingItems) {
        for(MediaItem i: trendingItems) {
            View c = LayoutInflater.from(getContext()).inflate(R.layout.media_item_display_card, null);
            RelativeLayout layout = c.findViewById(R.id.media_card_dis_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("MEDIA_ITEM", i);
                    Navigation.findNavController(view).navigate(R.id.action_animeSearch_to_itemSumamry, bundle);
                }
            });
            TextView itemRating = c.findViewById(R.id.media_card_dis_rating);
            TextView itemTitle = c.findViewById(R.id.media_card_dis_title);
            TextView itemEpisodes = c.findViewById(R.id.media_card_dis_episodes);
            ImageView itemImage = c.findViewById(R.id.media_card_dis_image);
            itemRating.setText("Rating: " + i.getItemInfo("Rating"));
            itemTitle.setText(i.getItemInfo("Title"));
            itemEpisodes.setText("Episodes: " + i.getItemInfo("Episodes"));
            Picasso.get().load(i.getItemInfo("ImageLink")).into(itemImage);
            trendingLayout.addView(c);
        }
    }

}
