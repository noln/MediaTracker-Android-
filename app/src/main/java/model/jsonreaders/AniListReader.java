package model.jsonreaders;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.exceptions.EmptyStringException;
import model.model.MediaItem;

public class AniListReader implements AnimeReader{

    public AniListReader() {}

    public List<MediaItem> readTop(String response, int num) throws JSONException, EmptyStringException {
        JSONObject object = new JSONObject(response);
        JSONObject data = object.getJSONObject("data");
        JSONObject page = data.getJSONObject("Page");
        JSONArray media = page.getJSONArray("media");
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        for(int i = 0; i < (media.length() < num ? media.length() : num); i++) {
            JSONObject obj = media.getJSONObject(i);
            MediaItem m = new MediaItem(obj.getJSONObject("title").getString("userPreferred"));
            String rating = obj.getString("averageScore");
            m.setItemInfo("Rating", (rating == "null"? rating : Float.toString(Float.parseFloat(rating)/10)));
            m.setItemInfo("WebsiteLink", obj.getString("siteUrl"));
            JSONObject date = obj.getJSONObject("startDate");
            m.setItemInfo("Date", date.getString("year") + "-"+ date.getString("month") + "-" + date.getString("day"));
            m.setItemInfo("Id", obj.getString("idMal"));
            m.setItemInfo("ImageLink", obj.getJSONObject("coverImage").getString("large"));
            m.setItemInfo("Type", "ANILIST");
            m.setItemInfo("Episodes", obj.getString("episodes") == "null" ? "??" : obj.getString("episodes"));
            m.setItemInfo("Plot", obj.getString("description").replaceAll("<.*>", ""));
            m.setItemInfo("Format", obj.getString("format"));
            mediaItems.add(m);
        }
        return mediaItems;
    }

    @Override
    public List<MediaItem> readSearch(String response, int num) throws JSONException, EmptyStringException {
        return readTop(response, num);
    }

}
