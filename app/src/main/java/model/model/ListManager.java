package model.model;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.exceptions.DataExistAlreadyException;
import model.exceptions.ItemNotFoundException;
import model.exceptions.KeyAlreadyExistsException;
import model.persistence.SaveAble;


import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Represent a manager that control and stores all the lists
public class ListManager implements SaveAble {
    private Map<MediaList, ArrayList<MediaItem>> listAndItems;
    private static ListManager instance = null;

     // MODIFIES: this
     // EFFECTS: initializes ListManager with an empty data lists
    private ListManager() {
        listAndItems = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: Get instance of ListManager, create if null
    public static ListManager getInstance() {
        if (instance == null) {
            instance = new ListManager();
        }
        return instance;
    }

    // MODIFIES: this
    // EFFECTS: remove instance of ListManager
    public static void removeInstance() {
        instance = null;
    }

    // EFFECTS: return MediaList with name. Throw ItemNotFoundException if not found.
    public MediaList getMediaListByName(String name) throws ItemNotFoundException {
        Set<MediaList> keySet = listAndItems.keySet();
        for (MediaList key: keySet) {
            if (key.getName().equals(name)) {
                return key;
            }
        }
        throw new ItemNotFoundException();
    }

    // EFFECTS: return the List of MediaItem associated with the mediaList, throw ItemNotFoundException if not found.
    public ArrayList<MediaItem> getListOfMedia(MediaList mediaList) {
        return listAndItems.get(mediaList);
    }

    // EFFECTS: return the MediaItem in List with name
    public MediaItem getMediaItemInListByName(String itemName, ArrayList<MediaItem> listOfItems)
            throws ItemNotFoundException {
        for (MediaItem item: listOfItems) {
            if (item.getItemInfo("Title").equals(itemName)) {
                return item;
            }
        }
        throw new ItemNotFoundException();
    }

    // MODIFIES: this
    // EFFECTS: add mediaItem to the list specified
    public void addMediaItemToList(MediaList mediaList, MediaItem mediaItem)
            throws ItemNotFoundException, DataExistAlreadyException, KeyAlreadyExistsException {
        listAlreadyExists(mediaList);
        ArrayList<MediaItem> list = listAndItems.get(mediaList);
        try {
            getMediaItemInListByName(mediaItem.getItemInfo("Title"), list);
        } catch (ItemNotFoundException e) {
            list.add(mediaItem);
            mediaItem.updateData("List", mediaList.getName());
            ItemManager.getInstance().addItem(mediaItem);
            return;
        }
        throw new KeyAlreadyExistsException();
    }

    // MODIFIES: this
    // EFFECTS: add list of mediaItem to list
    public void addListOfItemToList(MediaList mediaList, ArrayList<MediaItem> mediaItems)
            throws DataExistAlreadyException, ItemNotFoundException, KeyAlreadyExistsException {
        for (MediaItem item: mediaItems) {
            addMediaItemToList(mediaList, item);
        }
    }

    // MODIFIES: this
    // EFFECTS: delete mediaItem from the list specified
    public void deleteMediaItemFromList(MediaList mediaList, MediaItem mediaItem) throws ItemNotFoundException {
        listAlreadyExists(mediaList);
        ArrayList<MediaItem> list = listAndItems.get(mediaList);
        list.remove(mediaItem);
        mediaItem.removeData("List", mediaList.getName());
        ItemManager.getInstance().removeInactiveItem(mediaItem);
    }

    // MODIFIES: this
    // EFFECTS: add a new key to listAndItem and associate it with a new empty list
    public void addNewList(MediaList mediaList) throws KeyAlreadyExistsException {
        if (listAndItems.containsKey(mediaList)) {
            throw new KeyAlreadyExistsException();
        }
        listAndItems.put(mediaList, new ArrayList<>());
    }


    // EFFECTS: check if mediaList exists, throw ItemNotFoundException if not
    public void listAlreadyExists(MediaList mediaList) throws ItemNotFoundException {
        if (!listAndItems.containsKey(mediaList)) {
            throw new ItemNotFoundException();
        }
    }

    // MODIFIES: this
    // EFFECTS: remove a key(list) from listAndItem and its associating list
    public void removeList(MediaList mediaList) throws ItemNotFoundException {
        listAlreadyExists(mediaList);
        ArrayList<MediaItem> itemsInList = listAndItems.get(mediaList);
        for (MediaItem item: itemsInList) {
            item.removeData("List", mediaList.getName());
            ItemManager.getInstance().removeInactiveItem(item);
        }
        listAndItems.remove(mediaList);
    }

    // MODIFIES: this
    // EFFECTS: Return the number of lists
    public int numOfLists() {
        return listAndItems.keySet().size();
    }

    // EFFECTS: return set of all keys to lists
    public Set<MediaList> allActiveLists() {
        return listAndItems.keySet();
    }

    @Override
    public void save(FileWriter listFile, FileWriter tagFile, FileWriter itemFile) throws JSONException, IOException {
        JSONArray arrayOfLists = new JSONArray();
        for (MediaList list: listAndItems.keySet()) {
            JSONObject listObject = list.save();
            arrayOfLists.put(listObject);
        }
        listFile.write(arrayOfLists.toString());
    }


}
