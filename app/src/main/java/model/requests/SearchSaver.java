package model.requests;

import android.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.model.MediaItem;

public class SearchSaver {
    private static SearchSaver searchSaver;

    private Map<String, Pair<List<MediaItem>, List<MediaItem>>> animeSearch;

    private SearchSaver() {
        animeSearch = new HashMap<>();
    }

    public static SearchSaver getInstance() {
        if(searchSaver == null) {
            searchSaver = new SearchSaver();
        }
        return searchSaver;
    }

    public Pair<List<MediaItem>, List<MediaItem> > getAnimeSearch(String type) {
        return animeSearch.get(type);
    }

    public void setAnimeSearch(String type, Pair<List<MediaItem>, List<MediaItem>> items) {
        animeSearch.put(type, items);
    }

}
