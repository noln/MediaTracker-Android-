package model.persistence;

import android.content.Context;
import android.content.Intent;

import com.example.mediatracker20.MainActivity;
import model.exceptions.ItemNotFoundException;
import model.exceptions.KeyAlreadyExistsException;
import model.model.ItemManager;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;
import model.model.Tag;
import model.model.TagManager;
import java.util.List;

public class ReaderLoader {


    public static void loadInfo(Context context, List<MediaList> listRead, List<Tag> tagRead, List<MediaItem> mediaRead) {
        try {
            ReaderLoader.processListData(listRead, ListManager.getInstance());
            ReaderLoader.processTagData(tagRead, TagManager.getInstance());
            ReaderLoader.processUserItemData(mediaRead, ListManager.getInstance(), TagManager.getInstance(), ItemManager.getInstance());
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS Processes Tag data from files
    public static void processTagData(List<Tag> tagRead, TagManager tagManager) throws KeyAlreadyExistsException {
        if (tagRead != null) {
            for (Tag tag: tagRead) {
                tagManager.addNewTag(tag);
            }
        } else {
            System.out.println("There are no tags!");
        }

    }

    // MODIFIES: this
    // EFFECTS Processes List data from files
    public static void processListData(List<MediaList> listRead, ListManager listColl) throws KeyAlreadyExistsException {
        if (listRead != null) {
            for (MediaList list: listRead) {
                listColl.addNewList(list);
            }
        } else {
            System.out.println("There are no lists");
        }
    }

    // MODIFIES: this
    // EFFECTS Processes User Item data from files
    public static void processUserItemData(List<MediaItem> userItemRead,
                                            ListManager listColl, TagManager tagColl, ItemManager itemColl) {
        if (userItemRead != null) {
            itemColl.getAllMediaItems().addAll(userItemRead);
            loadToLists(userItemRead, listColl);
            loadToTags(userItemRead, tagColl);

        } else {
            System.out.println("There are no items!");
        }
    }


    // MODIFIES: this
    // EFFECTS load MediaItems to lists
    private static void loadToLists(List<MediaItem> mediaItems, ListManager listColl) {
        System.out.println(listColl.allActiveLists().size());
        for (MediaList list: listColl.allActiveLists()) {
            for (MediaItem item: mediaItems) {
                if (item.containMetaDataOf("List", list.getListName())) {
                    listColl.getListOfMedia(list).add(item);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS load MediaItems to tags
    private static void loadToTags(List<MediaItem> mediaItem, TagManager tagManager) {
        for (Tag tag: tagManager.getAllActiveTags()) {
            for (MediaItem item: mediaItem) {
                if (item.containMetaDataOf("Tag", tag.getTagName())) {
                    try {
                        tagManager.getListOfMediaWithTag(tag).add(item);
                    } catch (ItemNotFoundException e) {
                        System.out.println("Internal Error");
                    }
                }
            }
        }
    }

}
