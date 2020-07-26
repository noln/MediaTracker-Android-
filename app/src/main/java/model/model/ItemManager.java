package model.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.persistence.SaveAble;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// class that holds all items
public class ItemManager implements SaveAble {
    private ArrayList<MediaItem> allMediaItem; //represent all ACTIVE items
    private static ItemManager instance;

    // MODIFIES: this
    // EFFECTS: initialize allMediaItem (empty)
    private ItemManager() {
        allMediaItem = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: Get instance of ItemManager
    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    // MODIFIES: this
    // EFFECTS: remove instance of ItemManager
    public static void removeInstance() {
        instance = null;
    }

    // EFFECTS: return number of active mediaItems
    public int totalNumOfUserItems() {
        return allMediaItem.size();
    }

    // EFFECTS: return list of User defined item
    public ArrayList<MediaItem> getAllMediaItems() {
        return this.allMediaItem;
    }

    // MODIFIES: this
    // EFFECTS: if mediaItem is inactive, remove item from allMediaItems list and return true, else return false.
    public Boolean removeInactiveItem(MediaItem mediaItem) {
        if (!mediaItem.isActive()) {
            allMediaItem.remove(mediaItem);
            return true;
        }
        return false;
    }

    public void addItem(MediaItem item) {
        if (!allMediaItem.contains(item)) {
            allMediaItem.add(item);
        }
    }

    public void removeItem(MediaItem item) {
        if (allMediaItem.contains(item)) {
            allMediaItem.remove(item);
        }
    }

    // EFFECTS: returns true if allMediaItem contains Item
    public Boolean contains(MediaItem item) {
        return allMediaItem.contains(item);
    }

    @Override
    public void save(FileWriter listFile, FileWriter tagFile, FileWriter itemFile) throws JSONException, IOException {
        JSONArray arrayOfItems = new JSONArray();
        for (MediaItem item: allMediaItem) {
            JSONObject mediaListObject = item.save();
            arrayOfItems.put(mediaListObject);
        }
        listFile.write(arrayOfItems.toString());
    }
}
