package model.jsonreaders.imdb;

public class IMDbOverviewMediaItem {

    private String id;
    private IMDbImage image;
    private String title;
    private String episode;
    private String year;
    private IMDbRating ratings;
    private String websiteLink;
    private IMDbSummary plotSummary;


    public String getId() {
        return id;
    }

    public String getImageLink() {
        return image.getUrl();
    }

    public String getTitle() {
        return title;
    }

    public String getEpisode() {
        return episode;
    }

    public String getDate() {
        return year;
    }

    public String getRating() {
        return ratings.getRating();
    }

    public String getPlot() {
        return plotSummary.getPlotSummary();
    }

    public String getWebsiteLink() {
        return "https://www.imdb.com" + title;
    }

}
