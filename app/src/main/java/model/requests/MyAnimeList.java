package model.requests;

import android.content.Context;

import java.io.IOException;

import okhttp3.Response;

public class MyAnimeList implements AnimeRequest{

    private final String MAL_RAPID_HOST_URL = "jikan1.p.rapidapi.com";
    private final String MAL_ANIME_SEARCH_URL = "https://jikan1.p.rapidapi.com/search/anime?q=";
    private final String MAL_MANGA_SEARCH_URL = "https://jikan1.p.rapidapi.com/search/manga?q=";
    private final String MAL_Top_URL = "https://jikan1.p.rapidapi.com/top/";
    private final String SEARCH_RECOMMEND = "anime/1/bypopularity";
    private final String SEARCH_TRENDING = "anime/1/airing";

    public MyAnimeList() {

    }

    public String malSearchAnimeTitle(String query) throws IOException {
        return SearchData.searchTitle(query, MAL_ANIME_SEARCH_URL, MAL_RAPID_HOST_URL);
    }

    public String malSearchMangaTitle(String query) throws IOException {
        return SearchData.searchTitle(query, MAL_MANGA_SEARCH_URL, MAL_RAPID_HOST_URL);
    }

    public String searchTop(String query) throws IOException {
        Response response = SearchData.responseBuilder(MAL_Top_URL, query, MAL_RAPID_HOST_URL);
        String responseBody = response.body().string();
        response.body().close();
        return responseBody;
    }

    @Override
    public String search(String query) throws IOException {
        return SearchData.searchTitle(query, MAL_ANIME_SEARCH_URL, MAL_RAPID_HOST_URL);
    }

    @Override
    public String getRecommended() throws IOException {
       return searchTop(SEARCH_RECOMMEND);
    }

    @Override
    public String getTrending() throws IOException {
        return searchTop(SEARCH_TRENDING);
    }

}
