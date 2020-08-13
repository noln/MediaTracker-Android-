package com.example.mediatracker20.adapters.sources;

//represent a source of media - a site or database to get information from
public class MediaSources {

    private String title; //name of the source
    private int imageId; //icon of source
    private final String type; //type of source (general, anime, manga, drama)

    public MediaSources(int imageId, String title, String type) {
        this.title = title;
        this.imageId = imageId;
        this.type = type;
    }

    public int getImageId() {
        return imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

}
