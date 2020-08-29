package model.requests;

import android.content.Context;

import java.io.IOException;

import okhttp3.Response;

public class IMDB {

    private final String IM_DB_FIND_URL = "https://imdb8.p.rapidapi.com/title/find?q=";
    private final String IM_DB_OVERVIEW_URL = "https://imdb8.p.rapidapi.com/title/get-overview-details?currentCountry=US&tconst=";
    private final String IM_DB_RAPID_HOST_URL = "imdb8.p.rapidapi.com";

        public  String imDbFindTitle(String query, Context context) throws IOException {
            return SearchData.searchTitle(query, IM_DB_FIND_URL, IM_DB_RAPID_HOST_URL, context);
        }

        public  String imDbOverviewDetails(String movieId, Context context) throws IOException {
            Response response = SearchData.responseBuilder(IM_DB_OVERVIEW_URL, movieId, IM_DB_RAPID_HOST_URL, context);
            return response.body().string();
        }
}
