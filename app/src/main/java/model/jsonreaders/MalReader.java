package model.jsonreaders;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.exceptions.EmptyStringException;
import model.model.MediaItem;

public class MalReader implements AnimeReader{

    public MalReader() {}

    public  List<MediaItem> readSearch(String response, int num) throws JSONException, EmptyStringException {
        JSONObject object = new JSONObject(response);
        JSONArray results = object.getJSONArray("results");
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        for(int i = 0; i < results.length(); i++) {
            JSONObject obj = results.getJSONObject(i);
            MediaItem m = new MediaItem(obj.getString("title"));
            m.setItemInfo("Rating", obj.getString("score"));
            m.setItemInfo("Plot", obj.getString("synopsis"));
            m.setItemInfo("WebsiteLink", obj.getString("url"));
            m.setItemInfo("Date", obj.getString("start_date"));
            m.setItemInfo("Id", obj.getString("mal_id"));
            m.setItemInfo("ImageLink", obj.getString("image_url"));
            m.setItemInfo("Type", "MAL");
            m.setItemInfo("Episodes", obj.getString("episodes") == "null" ? "??" : obj.getString("episodes"));
            //m.setItemInfo("UserRating", "");
            mediaItems.add(m);
        }
        return mediaItems;
    }

    public List<MediaItem> readTop(String response, int num) throws JSONException, EmptyStringException {
        JSONObject object = new JSONObject(response);
        JSONArray results = object.getJSONArray("top");
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        for(int i = 0; i < (num > 50 ? 50 : num); i++) {
            JSONObject obj = results.getJSONObject(i);
            MediaItem m = new MediaItem(obj.getString("title"));
            m.setItemInfo("Rating", obj.getString("score"));
            m.setItemInfo("WebsiteLink", obj.getString("url"));
            m.setItemInfo("Date", obj.getString("start_date"));
            m.setItemInfo("Id", obj.getString("mal_id"));
            m.setItemInfo("ImageLink", obj.getString("image_url"));
            m.setItemInfo("Type", "MAL");
            m.setItemInfo("Episodes", obj.getString("episodes") == "null" ? "??" : obj.getString("episodes"));
            //m.setItemInfo("UserRating", "");
            mediaItems.add(m);
        }
        return mediaItems;
    }


}
