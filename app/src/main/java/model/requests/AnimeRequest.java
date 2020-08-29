package model.requests;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import model.model.MediaItem;

public interface AnimeRequest {

    public String searchTop(String query, Context context) throws IOException;

    public String search(String query, Context context) throws IOException;

    public String getRecommended(Context context) throws IOException;

    public String getTrending(Context context) throws IOException;
}
