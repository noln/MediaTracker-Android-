package model.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

import model.exceptions.DataExistAlreadyException;
import model.exceptions.EmptyStringException;
import model.exceptions.ItemNotFoundException;

// Represents a abstract media(Movies, tv show...) with different information
public class MediaItem implements Serializable {
    protected Map<String, String> itemDetails;
    protected ArrayList<MetaData> listData;
    protected ArrayList<MetaData> tagData;


    // MODIFIES: this
    // EFFECTS: initialize different types of metaData;
    public MediaItem(String title) throws EmptyStringException {
        if (title.trim().isEmpty()) {
            throw new EmptyStringException();
        }
        itemDetails = new HashMap<>();
        listData = new ArrayList<>();
        tagData = new ArrayList<>();
        itemDetails.put("Title", title);
        itemDetails.put("Rating", "");
        itemDetails.put("Status", "");
        itemDetails.put("Plot", "");
        itemDetails.put("WebsiteLink", "");
        itemDetails.put("Date", "");
        itemDetails.put("ImageLink", "");
        itemDetails.put("Type", ""); //mal, custom, or imdb
        itemDetails.put("Episodes", "");
        itemDetails.put("Id", "");
        itemDetails.put("UserRating", "");
    }

    // MODIFIES: this
    // EFFECTS: update the metaData of item with type and nameOfObject specified.
    public void updateData(String type, String nameOfObject) throws DataExistAlreadyException {
        MetaData newData = new MetaData(nameOfObject);
        addMetaDataOfType(type, newData);
    }

    // MODIFIES: this
    // EFFECTS: remove metaData with type and nameOfObject specified.
    public void removeData(String type, String nameOfObject) throws ItemNotFoundException {
        MetaData data = new MetaData(nameOfObject);
        removeMetaDataOfType(type, data);
    }

    // EFFECTS: return true if the MediaItem is in any lists at all.
    public Boolean isActive() {
        return listData.size() != 0;
    }

    // EFFECTS: return the list of metaData of the specified type, throw ItemNotFoundException if type not found
    public ArrayList<MetaData> getMetaDataOfType(String type) {
        if (type.equals("List")) {
            return  listData;
        } else {
            return tagData;
        }
    }

    // EFFECTS: return info about the item of the specified type, throw ItemNotFoundException if type not found
    public String getItemInfo(String typeOfInfo) {
        return itemDetails.get(typeOfInfo);
    }

    // EFFECTS: add info about the item of the specified type, throw ItemNotFoundException if type not found
    public void setItemInfo(String typeOfInfo, String info)  {
        itemDetails.put(typeOfInfo, info);
    }

    // EFFECTS: return the number of metaData with type lists
    public int numOfListMetaDataTypes() {
        return listData.size();
    }

    // EFFECTS: return the number of metaData with type tags
    public int numOfTagMetaDataTypes() {
        return tagData.size();
    }

    // EFFECTS: return the number of keys for item details
    public int numOfItemDetails() {
        return this.itemDetails.keySet().size();
    }

    // MODIFIES: this
    // EFFECTS: remove data from the list of metaData of the specified type, throw ItemNotFoundException if not found
    private void removeMetaDataOfType(String type, MetaData newData) {
        ArrayList<MetaData> metaDataList = new ArrayList<>();
        if (type.equals("List")) {
            metaDataList = listData;
        } else if (type.equals("Tag")) {
            metaDataList = tagData;
        }
        metaDataList.remove(newData);

    }

    // MODIFIES: this
    // EFFECTS: add newData to the list of metaData of the specified type, throw ItemNotFoundException if type not found
    private void addMetaDataOfType(String type, MetaData newData)
            throws DataExistAlreadyException {
        ArrayList<MetaData> metaDataList = new ArrayList<>();
        if (type.equals("List")) {
            metaDataList = listData;
        } else if (type.equals("Tag")) {
            metaDataList = tagData;
        }
        if (metaDataList.contains(newData)) {
            throw new DataExistAlreadyException();
        }
        metaDataList.add(newData);
    }

    // EFFECTS: return true if item contains metaData with type and nameOfObject
    public Boolean containMetaDataOf(String type, String nameOfObject) {
        ArrayList<MetaData> metaDataList;
        if (type.equals("List")) {
            metaDataList = listData;
        } else {
            metaDataList = tagData;
        }
        for (MetaData data: metaDataList) {
            if (data.getNameOfObject().equals(nameOfObject)) {
                return true;
            }
        }
        return false;
    }

    public MetaData getMetaDataWithName(String type, String nameOfObject) {
        ArrayList<MetaData> metaDataList;
        if (type.equals("List")) {
            metaDataList = listData;
        } else {
            metaDataList = tagData;
        }
        for (MetaData data: metaDataList) {
            if (data.getNameOfObject().equals(nameOfObject)) {
                return data;
            }
        }
        //this should never happen
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaItem mediaItem = (MediaItem) o;
        return itemDetails.equals(mediaItem.itemDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemDetails);
    }

    // EFFECTS: save the information of the object into json
    public JSONObject save() throws JSONException {
        JSONObject mediaObject = new JSONObject();
        mediaObject.put("title", itemDetails.get("Title"));
        mediaObject.put("status", itemDetails.get("Status"));
        mediaObject.put("rating", itemDetails.get("Rating"));
        mediaObject.put("plot", itemDetails.get("Plot"));
        mediaObject.put("imageLink", itemDetails.get("ImageLink"));
        mediaObject.put("date", itemDetails.get("Date"));
        mediaObject.put("websiteLink", itemDetails.get("WebsiteLink"));
        mediaObject.put("type", itemDetails.get("Type"));
        mediaObject.put("episode", itemDetails.get("Episode"));
        mediaObject.put("id", itemDetails.get("Id"));
        mediaObject.put("userRating", itemDetails.get("UserRating"));
        JSONArray listMetaData = new JSONArray();
        JSONArray tagMetaData = new JSONArray();
        for (MetaData data: listData) {
            JSONObject dataObject = data.save();
            listMetaData.put(dataObject);
        }
        for (MetaData data: tagData) {
            JSONObject dataObject = data.save();
            tagMetaData.put(dataObject);
        }

        mediaObject.put("metaDataList", listMetaData);
        mediaObject.put("metaDataTag", tagMetaData);
        return mediaObject;
    }

    // EFFECTS: make it so that toString on the object returns its title
    @Override
    public String toString() {
        return itemDetails.get("Title");
    }
}
