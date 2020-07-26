package model.jsonreaders.imdb;

public class IMDbSearchMediaItem {

    private String id;
    private IMDbImage image;
    private String year;
    private String title;
    private String websiteLink;

    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return image.getUrl();
    }

    public String getImageWidth() {
        return image.getWidth();
    }

    public String getImageHeight() {
        return image.getWidth();
    }

    public String getWebsiteLink() {
        return "https://www.imdb.com" + title;
    }



}
