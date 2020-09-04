package model.requests;





import android.content.Context;

import java.io.IOException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SearchData {

    private static final OkHttpClient client = new OkHttpClient();

    private  static final String RAPID_HOST_URL = "x-rapidapi-host";
    private static final String RAPID_HOST_KEY = "x-rapidapi-key";


    public static String searchTitle(String query, String searchUrl, String hostUrl) throws IOException {
        String processedQuery = URLEncoder.encode(query, String.valueOf(StandardCharsets.UTF_8));
        Response response = responseBuilder(searchUrl, processedQuery, hostUrl);
        String responseBody = response.body().string();
        response.body().close();
        return responseBody;
    }

    public static Response responseBuilder(String url, String processedQuery, String apiURL) throws IOException {
        Request request = new Request.Builder()
                .url(url + processedQuery)
                .get()
                .addHeader(RAPID_HOST_URL, apiURL)
                .addHeader(RAPID_HOST_KEY, ApiKey.getKey())
                .build();
        Response response = client.newCall(request).execute();
        return  response;
    }

    public static Response responseBuilderQ(String url, String query, String header) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(query, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader(RAPID_HOST_URL, header)
                .addHeader(RAPID_HOST_KEY, ApiKey.getKey())
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        return  response;
    }

}
