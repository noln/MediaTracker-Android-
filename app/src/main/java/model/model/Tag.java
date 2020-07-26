package model.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

// Represents a simple string to categorize MediaItems
public class Tag {
    private String tagName;

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return this.tagName;
    }

    public void setTagName(String newName) {
        this.tagName = newName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        return tagName.toLowerCase().equals(tag.tagName.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName.toLowerCase());
    }

    // EFFECTS: save the tag into a json object
    public JSONObject save() throws JSONException {
        JSONObject tagObject = new JSONObject();
        tagObject.put("tagName", this.tagName);
        return tagObject;
    }
}
