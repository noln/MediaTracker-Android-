package model.Organize;

import android.os.Build;

import androidx.annotation.RequiresApi;


import model.model.MediaItem;
import model.model.MediaList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchManager {

    private static SearchManager searchManager;



    private SearchManager() {

    }

    public static SearchManager getInstance() {
        if (searchManager == null) {
            searchManager = new SearchManager();
        }
        return searchManager;
    }

    public List<MediaItem> searchByName(List<MediaItem> list, String name) {
        Predicate<MediaItem> predicate = item -> item.getItemInfo("Title").contains(name);
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public List<MediaItem> searchByTag(List<MediaItem> list, String tagName) {
        Predicate<MediaItem> predicate = item -> item.containMetaDataOf("Tag", tagName);
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public List<MediaList> searchByNameL(List<MediaList> list, String name) {
        Predicate<MediaList> predicate = item -> item.getName().contains(name);
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public List<MediaItem> searchByType(List<MediaItem> list, String type) {
        Predicate<MediaItem> predicate = item -> item.getItemInfo("Type").equals(type);
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public List<MediaItem> searchByStatus(List<MediaItem> list, String status) {
        Predicate<MediaItem> predicate = item -> item.getItemInfo("Status").equals(status);
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
