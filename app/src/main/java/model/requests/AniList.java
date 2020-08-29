package model.requests;

import android.content.Context;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class AniList implements AnimeRequest{

    private final String ANI_RAPID_HOST_URL =  "https://anilist-graphql.p.rapidapi.com/";
    private final String ANI_HEADER = "anilist-graphql.p.rapidapi.com";
    private final String SEARCH_STRING_B = "{  \"query\": \"query{Page(perPage: 50){media(sort: SEARCH_MATCH isAdult: false type: ANIME search:";
           // " \\\"test\\\""
    private final String SEARCH_STRING_E ="){idMal averageScore description episodes siteUrl coverImage{large}title{userPreferred} startDate{year month day}}}}\",  \"variables\": {}}";

    private final String SEARCH_TRENDING ="{  \"query\": \"query{Page(perPage: 50){media(sort: TRENDING_DESC type: ANIME isAdult: false){idMal averageScore description episodes siteUrl coverImage{large}title{userPreferred} startDate{year month day}}}}\",  \"variables\": {}}";

    private final String SEARCH_RECOMMEND = "{  \"query\": \"query{Page(perPage: 50){media(sort: SCORE_DESC type: ANIME isAdult: false){idMal averageScore description episodes siteUrl coverImage{large}title{userPreferred} startDate{year month day}}}}\",  \"variables\": {}}";

    public AniList() {}

    public String searchTop(String query ,Context context) throws IOException {
        Response response = SearchData.responseBuilderQ(ANI_RAPID_HOST_URL, query, ANI_HEADER, context);
        ResponseBody responseBody = response.body();
        return response.body().string();
    }

    @Override
    public String search(String query, Context context) throws IOException {
        return searchTop(SEARCH_STRING_B + "\\\"" +  query + "\\\"" + SEARCH_STRING_E, context);
    }

    @Override
    public String getRecommended(Context context) throws IOException {
        return searchTop(SEARCH_RECOMMEND, context);
    }

    @Override
    public String getTrending(Context context) throws IOException {
        return searchTop(SEARCH_TRENDING, context);
    }
}

