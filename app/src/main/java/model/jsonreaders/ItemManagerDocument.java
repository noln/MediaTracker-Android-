package model.jsonreaders;

import java.util.List;

import model.model.MediaItem;

public class ItemManagerDocument {
    public void setItemManager(List<MediaItem> itemManager) {
        this.itemManager = itemManager;
    }

    public List<MediaItem> getItemManager() {
        return itemManager;
    }

    public List<MediaItem> itemManager;

    public ItemManagerDocument() {}
}
