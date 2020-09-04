package model.jsonreaders;


import model.model.MetaData;

import java.util.ArrayList;

// Dummy class used to read in information about file
public class ReadUserItem {
    private ArrayList<MetaData> metaDataTag;
    private ArrayList<MetaData> metaDataList;
    private String title;
    private String type;
    private String plot;
    private String rating;
    private String status;
    private String imageLink;
    private String websiteLink;
    private String date;
    private String episode;
    private String id;
    private String userRating;
    private String userReview;
    private String format;

    public ReadUserItem() {}

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String rating) {
        userRating = rating;
    }

    public String getEpisode() {
        return episode;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getPlot() {
        return plot;
    }

    public String getRating() {
        return rating;
    }

    public String getStatus() {
        return status;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getWebsiteLink() {
        return  websiteLink;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public ArrayList<MetaData> getMetaDataList() {
        return metaDataList;
    }

    public ArrayList<MetaData> getMetaDataTag() {
        return metaDataTag;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

}
