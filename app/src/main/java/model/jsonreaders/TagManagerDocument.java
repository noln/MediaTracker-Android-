package model.jsonreaders;

import java.util.List;

import model.model.Tag;

public class TagManagerDocument {

    public void setTagManager(List<Tag> tagManager) {
        this.tagManager = tagManager;
    }

    public List<Tag> tagManager;

    public List<Tag> getTagManager() {
        return tagManager;
    }
}
