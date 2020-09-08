package model.jsonreaders;

import java.util.List;

import model.model.MediaList;

public class ListManagerDocument {

    public void setListManager(List<MediaList> listManager) {
        this.listManager = listManager;
    }

    public List<MediaList> listManager;

    public List<MediaList> getListManager() {
        return listManager;
    }
}
