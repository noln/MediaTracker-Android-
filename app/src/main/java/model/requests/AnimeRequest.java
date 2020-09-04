package model.requests;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import model.model.MediaItem;

public interface AnimeRequest {

    public String searchTop(String query) throws IOException;

    public String search(String query) throws IOException;

    public String getRecommended() throws IOException;

    public String getTrending() throws IOException;
}
