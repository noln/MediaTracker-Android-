package model.jsonreaders;

import org.json.JSONException;

import java.util.List;

import model.exceptions.EmptyStringException;
import model.model.MediaItem;

public interface AnimeReader {

    List<MediaItem> readTop(String Response, int num) throws JSONException, EmptyStringException;

    List<MediaItem> readSearch(String response, int num) throws JSONException, EmptyStringException;

}
