package com.example.mostafa.tvshows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class FetchJson extends AppCompatActivity {

    private HttpURLConnection urlConnection = null;

    private String mApiKey ="3f240f7d8a4b186fa413732eeb4146ea";
    private String API_KEY="api_key";
    private String LANG="language";
    private String ENG="en-US";
    private String PAGE="page";
    private String ONE = "1";
    private String GET="GET";

    public URL getUrl(String sort) throws MalformedURLException {

        final String TMDB_BASE_URL = "https://api.themoviedb.org/3/tv/"
                +sort+"?";

        final String API_KEY_PARAM = API_KEY;
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, mApiKey)
                .appendQueryParameter(LANG,ENG)
                .appendQueryParameter(PAGE, ONE)
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

    public TvShow[] getData(String JsonStr) throws JSONException {
        final String RESULTS = "results";
        final String ORIGINAL_TITLE = "original_name";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "first_air_date";
        JSONObject moviesRoot = new JSONObject(JsonStr);
        JSONArray resultsArray = moviesRoot.getJSONArray(RESULTS);
        TvShow[] mtvShows = new TvShow[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {

            JSONObject movieInfo = resultsArray.getJSONObject(i);
            mtvShows[i] = new TvShow();
            mtvShows[i].setmID(movieInfo.getInt("id"));
            mtvShows[i].setOriginalTitle(movieInfo.getString(ORIGINAL_TITLE));
            mtvShows[i].setPosterPath(movieInfo.getString(POSTER_PATH));
            mtvShows[i].setOverview(movieInfo.getString(OVERVIEW));
            mtvShows[i].setVoteAverage(movieInfo.getDouble(VOTE_AVERAGE));
            mtvShows[i].setReleaseDate(movieInfo.getString(RELEASE_DATE));
        }

        return mtvShows;
    }

}







