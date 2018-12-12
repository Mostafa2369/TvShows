package com.example.mostafa.tvshows;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class FetchReview extends AppCompatActivity {

    //  https://api.themoviedb.org/3/movie/299536/reviews?api_key=3f240f7d8a4b186fa413732eeb4146ea&language=en-US
    private HttpURLConnection urlConnection = null;

    private String mApiKey = "3f240f7d8a4b186fa413732eeb4146ea";


    private String LANG="language";
    private String ENG="en-US";

    private String GET="GET";
    public URL getUrl(String id) throws MalformedURLException {

        final String TMDB_BASE_URL = "https://api.themoviedb.org/3/tv/" + id + "/reviews?";

        final String API_KEY_PARAM = "api_key";
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, mApiKey)
                .appendQueryParameter(LANG, ENG)
                .build();
        return new URL(builtUri.toString());
    }


    public String getJsonStr(URL Url) throws IOException {
        URL url = Url;

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(GET);
        urlConnection.connect();

        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner mScan = new Scanner(inputStream);
            mScan.useDelimiter("\\A");
            boolean hasIn = mScan.hasNext();
            if (hasIn)
                return mScan.next();
            else
                return null;
        } finally {


            urlConnection.disconnect();
        }

    }

    public String[] getReview(String json) throws JSONException {
        final String RESULTS = "results";
        JSONObject moviesRoot = new JSONObject(json);
        JSONArray resultsArray = moviesRoot.getJSONArray(RESULTS);
        String[] review = new String[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {

            JSONObject movieInfo = resultsArray.getJSONObject(i);
            review[i] = movieInfo.getString("author") + "@" + movieInfo.getString("content");

        }
        return review;

    }


}