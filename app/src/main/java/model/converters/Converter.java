package model.converters;

import android.util.Log;

import com.google.gson.Gson;

import model.exceptions.EmptyStringException;
import model.jsonreaders.ReadUserItem;
import model.jsonreaders.imdb.IMDbOverviewMediaItem;
import model.jsonreaders.imdb.IMDbSearchResults;
import model.jsonreaders.mal.MalMediaItem;
import model.jsonreaders.mal.MalResults;
import model.model.MediaItem;


public class Converter {

    public static MediaItem malToMediaItem(MalMediaItem item) {
        MediaItem newItem = null;
        try {
            newItem = new MediaItem(item.getTitle());
        } catch (EmptyStringException e) {
            Log.e("Converting error", "No title from MAL Item");
        }
        newItem.setItemInfo("Rating", item.getRating());
        newItem.setItemInfo("Plot", item.getPlot());
        newItem.setItemInfo("WebsiteLink", item.getWebsiteLink());
        newItem.setItemInfo("Date", item.getDate());
        newItem.setItemInfo("ImageLink", item.getImageLink());
        newItem.setItemInfo("Type", "MAL");
        newItem.setItemInfo("Episodes", item.getEpisode());
        newItem.setItemInfo("Id", item.getId());
        return newItem;

    }

    public static MediaItem imDbToMediaItem(IMDbOverviewMediaItem item) {
        MediaItem newItem = null;
        try {
            newItem = new MediaItem(item.getTitle());
        } catch (EmptyStringException e) {
            Log.e("Converter error", "no title from imDb item");
        }
        newItem.setItemInfo("Rating", item.getRating());
        newItem.setItemInfo("Plot", item.getPlot());
        newItem.setItemInfo("WebsiteLink", item.getWebsiteLink());
        newItem.setItemInfo("Date", item.getDate());
        newItem.setItemInfo("ImageLink", item.getImageLink());
        newItem.setItemInfo("Type", "MAL");
        newItem.setItemInfo("Episodes", item.getEpisode());
        newItem.setItemInfo("Id", item.getId());
        return newItem;
    }

    public static MediaItem readItemToMediaItem(ReadUserItem item) {
        MediaItem newItem = null;
        try {
            newItem = new MediaItem(item.getTitle());
        } catch (EmptyStringException e) {
            Log.e("Converter error", "no title from readItem");
        }
        newItem.setItemInfo("Status", item.getStatus());
        newItem.setItemInfo("Rating", item.getRating());
        newItem.setItemInfo("Type", item.getType());
        newItem.setItemInfo("Plot", item.getPlot());
        newItem.setItemInfo("ImageLink", item.getImageLink());
        newItem.setItemInfo("WebsiteLink", item.getWebsiteLink());
        newItem.setItemInfo("Date", item.getDate());
        newItem.setItemInfo("Episodes", item.getEpisode());
        newItem.setItemInfo("Id", item.getId());
        newItem.getMetaDataOfType("List").addAll(item.getMetaDataList());
        newItem.getMetaDataOfType("Tag").addAll(item.getMetaDataTag());
        newItem.setItemInfo("Format", item.getFormat());
        newItem.setItemInfo("UserReview", item.getUserReview());
        newItem.setItemInfo("UserRating", item.getUserRating());
        return newItem;
    }

    public static MalResults jsonToMalResults(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, MalResults.class);
    }

    public static IMDbOverviewMediaItem jsonToIMDbOverviewMediaItem(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, IMDbOverviewMediaItem.class);
    }

    public static IMDbSearchResults jsonToIMDbSearchResults(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, IMDbSearchResults.class);
    }
}
