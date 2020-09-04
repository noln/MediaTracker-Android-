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
    private final String SEARCH_STRING_E ="){idMal averageScore format description episodes siteUrl coverImage{large}title{userPreferred} startDate{year month day}}}}\",  \"variables\": {}}";

    private final String SEARCH_TRENDING ="{  \"query\": \"query{Page(perPage: 50){media(sort: TRENDING_DESC type: ANIME isAdult: false){idMal averageScore format description episodes siteUrl coverImage{large}title{userPreferred} startDate{year month day}}}}\",  \"variables\": {}}";

    private final String SEARCH_RECOMMEND = "{  \"query\": \"query{Page(perPage: 50){media(sort: SCORE_DESC type: ANIME isAdult: false){idMal averageScore format description episodes siteUrl coverImage{large}title{userPreferred} startDate{year month day}}}}\",  \"variables\": {}}";

    public AniList() {}

    public String searchTop(String query) throws IOException {
        Response response = SearchData.responseBuilderQ(ANI_RAPID_HOST_URL, query, ANI_HEADER);
        String responseBody = response.body().string();
        response.body().close();
        return responseBody;
    }

    @Override
    public String search(String query) throws IOException {
        return searchTop(SEARCH_STRING_B + "\\\"" +  query + "\\\"" + SEARCH_STRING_E);
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

