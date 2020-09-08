package model.model;

import model.exceptions.EmptyStringException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

//List of MediaItem with a name
public class MediaList {


    private String listName; //Name of the list
    private String dateCreated;
    private int frequency;
    private String latestAccessDate;

    public MediaList() {
    }

    public MediaList(String name) throws EmptyStringException {
        if (name.trim().isEmpty()) {
            throw new EmptyStringException();
        }
        frequency = 0;
        this.listName = name;
        Calendar calendar = Calendar.getInstance();
        this.dateCreated = calendar.getTime().toString();
        latestAccessDate = this.dateCreated;
    }

    public String getListName() {
        return this.listName;
    }

    public void setName(String newName) throws EmptyStringException {
        if (newName.isEmpty()) {
            throw new EmptyStringException();
        }
        this.listName = newName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int freq) {
        frequency = freq;
    }

    public void incFrequency() {
        frequency += 1;
    }

    public String getLatestAccessDate() {
        return latestAccessDate;
    }

    public void setLatestAccessDate(String date) {
        this.latestAccessDate = date;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MediaList mediaList = (MediaList) o;
        return listName.equals(mediaList.listName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listName);
    }

    // EFFECTS: save the information of the object into json
    public JSONObject save() throws JSONException {
        JSONObject mediaListObject = new JSONObject();
        mediaListObject.put("listName", this.listName);
        mediaListObject.put("dateCreated", this.dateCreated);
        mediaListObject.put("frequency", this.frequency);
        mediaListObject.put("latestAccessDate", this.latestAccessDate);
        return mediaListObject;
    }

    @Override
    public String toString() {
        return getListName();
    }
}
