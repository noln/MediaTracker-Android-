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
import java.util.*;


// class which manages tags and tagged items
public class TagManager implements SaveAble {
    private Map<Tag, ArrayList<MediaItem>> tagAndItem;
    private static TagManager instance;

    // MODIFIES: this
    // EFFECTS: initialize tagAndItem (empty)
    private TagManager() {
        tagAndItem = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: Get instance of TagManager
    public static TagManager getInstance() {
        if (instance == null) {
            instance = new TagManager();
        }
        return instance;
    }

    // MODIFIES: this
    // EFFECTS: remove instance of TagManager
    public static void removeInstance() {
        instance = null;
    }

    // MODIFIES: this
    // EFFECTS: add a new tag
    public void addNewTag(Tag tag) throws KeyAlreadyExistsException {
        if (tagAndItem.containsKey(tag)) {
            throw new KeyAlreadyExistsException();
        }
        tagAndItem.put(tag, new ArrayList<>());
    }


    // EFFECTS: return number of tags that exist
    public int numOfTags() {
        return tagAndItem.keySet().size();
    }

    // EFFECTS: check if tag is in the list, if not throw ItemNotFoundException
    public void tagInList(Tag tag) throws ItemNotFoundException {
        if (!tagAndItem.containsKey(tag)) {
            throw new ItemNotFoundException();
        }
    }

    // EFFECTS: return all active tags
    public Set<Tag> getAllActiveTags() {
        return this.tagAndItem.keySet();
    }

    public ArrayList<Tag> getAllTags() {
        return new ArrayList<>(this.tagAndItem.keySet());
    }

    // MODIFIES: this
    // EFFECTS: delete a tag
    public void deleteTag(Tag tag) throws ItemNotFoundException {
        tagInList(tag);
        ArrayList<MediaItem> itemsInList = tagAndItem.get(tag);
        for (MediaItem item: itemsInList) {
            item.removeData("Tag", tag.getTagName());
            ItemManager.getInstance().removeInactiveItem(item);
        }
        tagAndItem.remove(tag);
    }

    // MODIFIES: this
    // EFFECTS: tag a mediaItem
    public void tagItem(Tag tag, MediaItem mediaItem) throws ItemNotFoundException, DataExistAlreadyException {
        tagInList(tag);
        ArrayList<MediaItem> tagList = tagAndItem.get(tag);
        if(tagList.contains(mediaItem)) {
            throw new DataExistAlreadyException();
        }
        tagList.add(mediaItem);
        mediaItem.updateData("Tag", tag.getTagName());
    }

    // MODIFIES: this
    // EFFECTS: remove tag a mediaItem
    public void removeTag(Tag tag, MediaItem mediaItem) throws ItemNotFoundException {
        tagInList(tag);
        ArrayList<MediaItem> tagList = tagAndItem.get(tag);
        if (tagList.contains(mediaItem)) {
            tagList.remove(mediaItem);
            mediaItem.removeData("Tag", tag.getTagName());
            return;
        }
        throw new ItemNotFoundException();
    }

    // EFFECTS: return the list of MediaItems associated with tag, throw ItemNotFoundException if tag not found.
    public ArrayList<MediaItem> getListOfMediaWithTag(Tag tag) throws ItemNotFoundException {
        tagInList(tag);
        return tagAndItem.get(tag);
    }

    public boolean mediaIsTaggedBy(Tag tag, MediaItem mediaItem) {
        return tagAndItem.get(tag).contains(mediaItem);
    }

    public void removeInactiveTags() {
        Iterator it = tagAndItem.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(((List<MediaItem>) pair.getValue()).isEmpty()) {
                it.remove();
            }
        }
    }

    public Tag getTagByName(String name) throws ItemNotFoundException {
        for(Tag t: tagAndItem.keySet()) {
            if(t.getTagName().equals(name)) {
                return t;
            }
        }
        throw new ItemNotFoundException();
    }

    @Override
    public void save(FileWriter listFile, FileWriter tagFile, FileWriter itemFile) throws JSONException, IOException {
        JSONArray arrayOfTags = new JSONArray();
        for (Tag tag: tagAndItem.keySet()) {
            JSONObject tagObject = tag.save();
            arrayOfTags.put(tagObject);
        }
        tagFile.write(arrayOfTags.toString());
    }
}
