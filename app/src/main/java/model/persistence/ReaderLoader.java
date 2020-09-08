package model.persistence;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;


import model.converters.Converter;
import model.exceptions.ItemNotFoundException;
import model.exceptions.KeyAlreadyExistsException;
import model.jsonreaders.ItemManagerDocument;
import model.jsonreaders.ListManagerDocument;
import model.jsonreaders.ReadUserItem;
import model.jsonreaders.TagManagerDocument;
import model.model.ItemManager;
import model.model.ListManager;
import model.model.MediaItem;
import model.model.MediaList;
import model.model.Tag;
import model.model.TagManager;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReaderLoader {


    // EFFECTS: save state of listColl to LISTS_FILE. Modeled after TellerApp
    public static void saveProgram(ListManager listColl, TagManager tagManager, ItemManager itemManager, Context context) throws JSONException {
        try {
            //TagManager.getInstance().removeInactiveTags();
            Writer writer = new Writer(new File(context.getFilesDir(), "listFile.txt"),
                    new File(context.getFilesDir(), "tagFile.txt"), new File(context.getFilesDir(), "itemFile.txt"));
            writer.write(listColl);
            writer.write(tagManager);
            writer.write(itemManager);
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to save");
            e.printStackTrace();
        }
    }


//    // MODIFIES: this
//    // EFFECTS: load info from LIST_FILE. Catch NullPointerException when there are no info in LIST_FILE
//    public static void loadInfo(ListManager listColl, TagManager tagColl, ItemManager itemColl, Context context) throws IOException, KeyAlreadyExistsException {
//        ArrayList<ReadUserItem> userItemRead = Reader.readItemFile(context.getFilesDir().getPath() + "/itemFile.txt");
//        ArrayList<Tag> tagRead = Reader.readTagFile(context.getFilesDir().getPath() + "/tagFile.txt");
//        ArrayList<MediaList> listRead = Reader.readListFile(context.getFilesDir().getPath() + "/listFile.txt");
//        processListData(listRead, listColl);
//        processTagData(tagRead, tagColl);
//        processUserItemData(userItemRead, listColl, tagColl, itemColl);
//    }


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
    // EFFECTS Processes all user item
    private static ArrayList<MediaItem> processAllItems(List<ReadUserItem> itemRead) {
        ArrayList<MediaItem> mediaItemArrayList = new ArrayList<>();
        for (ReadUserItem item: itemRead) {
            mediaItemArrayList.add(Converter.readItemToMediaItem(item));
        }
        return mediaItemArrayList;
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
