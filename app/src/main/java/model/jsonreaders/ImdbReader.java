package model.jsonreaders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import model.model.MediaItem;

public class ImdbReader {

    public static List<MediaItem> readSearch(String response) throws JSONException {
        JSONObject imdbObject = new JSONObject(response);
        //JSONArray
        return null;
    }
}
