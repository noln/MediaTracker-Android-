package model.jsonreaders.mal;

public class MalMediaItem {

    private String mal_id;
    private String url;
    private String image_url;
    private String title;
    private String synopsis;
    private String episodes;
    private String score;
    private String start_date;


    public String getId() {
        return mal_id;
    }

    public String getImageLink() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getEpisode() {
        return episodes;
    }

    public String getDate() {
        return start_date;
    }

    public String getRating() {
        return score;
    }

    public String getPlot() {
        return synopsis;
    }

    public String getWebsiteLink() {
        return url;
    }
}
