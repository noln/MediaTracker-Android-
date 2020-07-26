package model.Organize;


import model.model.MediaItem;
import model.model.MediaList;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sorter {

    private static Comparator<MediaItem> sortByName = Comparator.comparing((MediaItem o) -> o.getItemInfo("Title"));
    private static Comparator<MediaItem> sortByDateAdded = null;
    private static Comparator<MediaItem> sortByRating = Comparator.comparing((MediaItem o) -> o.getItemInfo("Rating"));
    private static Comparator<MediaList> sortByDateAddedL = Comparator.comparing(MediaList::getDateCreated);
    private static Comparator<MediaList> sortByFrequencyL = Comparator.comparing(MediaList::getFrequency);
    private static Comparator<MediaList> sortByNameL = Comparator.comparing((MediaList o) -> o.getName().toString().toLowerCase());
    private static Comparator<MediaList>  sortByLatestAccessedL = Comparator.comparing(MediaList::getLatestAccessDate);


    public static void mediaSortByName(List<MediaItem> list) {
        list.sort(sortByName);
    }

    public static void mediaSortByDateAdded(List<MediaItem> list, String activeList) {
        sortByDateAdded = Comparator.comparing(o -> o.getMetaDataWithName("List", activeList).getDate());
        list.sort(sortByDateAdded);
        Collections.reverse(list);
    }

    public static void mediaSortByRating(List<MediaItem> list) {
        list.sort(sortByRating);
        Collections.reverse(list);
    }

    public static void listSortByDateCreated(List<MediaList> list) {
        list.sort(sortByDateAddedL);
        Collections.reverse(list);
    }

    public static void listSortByFrequency(List<MediaList> list) {
        list.sort(sortByFrequencyL);
        Collections.reverse(list);
    }

    public static void listSortByLatestAccess(List<MediaList> list) {
        list.sort(sortByLatestAccessedL);
        Collections.reverse(list);
    }

    public static void listSortByName(List<MediaList> list) {
        list.sort(sortByNameL);
    }



}
