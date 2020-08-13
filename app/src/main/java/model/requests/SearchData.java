package model.requests;





import java.io.IOException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchData {

    private static final OkHttpClient client = new OkHttpClient();

    private static final String IM_DB_FIND_URL = "https://imdb8.p.rapidapi.com/title/find?q=";

    private static final String IM_DB_OVERVIEW_URL = "https://imdb8.p.rapidapi.com/title/get-overview-details?currentCountry=US&tconst=";
    private static final String IM_DB_RAPID_HOST_URL = "imdb8.p.rapidapi.com";

    private static final String RAPID_HOST_URL = "x-rapidapi-host";
    private static final String RAPID_HOST_KEY = "x-rapidapi-key";

    private static final String API_KEY = ""; //my api key stored locally. get your's at rapid api

    private static final String MAL_RAPID_HOST_URL = "jikan1.p.rapidapi.com";
    private static final String MAL_ANIME_SEARCH_URL = "https://jikan1.p.rapidapi.com/search/anime?q=";
    private static final String MAL_MANGA_SEARCH_URL = "https://jikan1.p.rapidapi.com/search/manga?q=";


    public static String imDbFindTitle(String query) throws IOException {
        return searchTitle(query, IM_DB_FIND_URL, IM_DB_RAPID_HOST_URL);
    }

    public static String imDbOverviewDetails(String movieId) throws IOException {
        Response response = responseBuilder(IM_DB_OVERVIEW_URL, movieId, IM_DB_RAPID_HOST_URL);
        return response.body().toString();
    }

    public static String malSearchAnimeTitle(String query) throws IOException {
        return searchTitle(query, MAL_ANIME_SEARCH_URL, MAL_RAPID_HOST_URL);
    }

    public static String malSearchMangaTitle(String query) throws IOException {
        return searchTitle(query, MAL_MANGA_SEARCH_URL, MAL_RAPID_HOST_URL);
    }

    private static String searchTitle(String query, String searchUrl, String hostUrl) throws IOException {
        String processedQuery = URLEncoder.encode(query, String.valueOf(StandardCharsets.UTF_8));
        Response response = responseBuilder(searchUrl, processedQuery, hostUrl);
        return response.body().toString();
    }

    private static Response responseBuilder(String url, String processedQuery, String apiURL) throws IOException {
        Request request = new Request.Builder()
                .url(url + processedQuery)
                .get()
                .addHeader(RAPID_HOST_URL, apiURL)
                .addHeader(RAPID_HOST_KEY, API_KEY)
                .build();
        Response response = client.newCall(request).execute();
        return  response;
    }
}
