package com.example.mostafa.tvshows;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements TvShowAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    private TvShowAdapter mAdapter;
    private RecyclerView mView;
    private TvShow[] data = null;
    private TvShow[] mFavorate;
    private URL link = null;
    private TvShow[] movi;
    private boolean isConnected;
    private String mSort = "popular";
    private FetchJson mFetchJson = new FetchJson();
    private Context con = this;
    private boolean fav = false;

    private String POSITION = "position";
    private String SAVED = "saved";


    @Override
    public void onClick(int mMoviePosition) {
        Intent intent = new Intent(MainActivity.this, TvShowDetails.class);
        if (fav == false) {
            intent.putExtra(POSITION, data[mMoviePosition]);
        } else {
            intent.putExtra(POSITION, mFavorate[mMoviePosition]);
        }
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAdapter = new TvShowAdapter(this);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(0, null, this);



        try {
            link = mFetchJson.getUrl(mSort);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {

            if (savedInstanceState == null) {
                new getMoviesData().execute(link);

            } else {

                // Get data from local resources
                // Get TvShow objects
                Parcelable[] parcelable = savedInstanceState.
                        getParcelableArray(SAVED);
                if (parcelable != null) {
                    int numMovieObjects = parcelable.length;
                    movi = new TvShow[numMovieObjects];
                    for (int i = 0; i < numMovieObjects; i++) {
                        movi[i] = (TvShow) parcelable[i];
                    }
                }
                data = movi;
                mView = (RecyclerView) findViewById(R.id.Movie_recycle);
                mView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                if (fav)
                    mAdapter.getData(setFavorate());
                else
                    mAdapter.getData(movi);
                mView.setAdapter(mAdapter);

            }
        } else {
            Toast.makeText(con, getResources().getString(R.string.net), Toast.LENGTH_LONG).show();
        }
    }

    public class getMoviesData extends AsyncTask<URL, Void, TvShow[]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected TvShow[] doInBackground(URL... urls) {
            URL link = urls[0];


            String mJsonCode = null;
            try {
                mJsonCode = mFetchJson.getJsonStr(link);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                data = mFetchJson.getData(mJsonCode);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return data;

        }

        @Override
        protected void onPostExecute(TvShow[] tvShows) {

            mView = (RecyclerView) findViewById(R.id.Movie_recycle);
            mView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            mAdapter.getData(tvShows);
            mView.setAdapter(mAdapter);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {

        if (fav == true) {
            mView = (RecyclerView) findViewById(R.id.Movie_recycle);
            mView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            mAdapter.getData(setFavorate());
            mView.setAdapter(mAdapter);

        }
        getSupportLoaderManager().restartLoader(0, null, this);
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        if (fav)
            outState.putParcelableArray(SAVED, setFavorate());
        else
            outState.putParcelableArray(SAVED, data);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.rated:
                try {
                    fav = false;
                    link = mFetchJson.getUrl(getResources().getString(R.string.top_rated));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                mSort = getResources().getString(R.string.top_rated);
                ConnectivityManager cm1 = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission") NetworkInfo activeNetwork1 = cm1.getActiveNetworkInfo();
                isConnected = activeNetwork1 != null && activeNetwork1.isConnectedOrConnecting();
                if (isConnected)
                    new getMoviesData().execute(link);
                else {
                    Toast.makeText(con,  getResources().getString(R.string.net), Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.popular:

                try {
                    link = mFetchJson.getUrl(getResources().getString(R.string.popular_key));
                    fav = false;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                mSort = getResources().getString(R.string.popular_key);
                ConnectivityManager cm = (ConnectivityManager) con.getSystemService(con.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (isConnected)
                    new getMoviesData().execute(link);
                else {
                    Toast.makeText(con,  getResources().getString(R.string.net), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.rated2:

                fav = true;
                mView = (RecyclerView) findViewById(R.id.Movie_recycle);
                mView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                mAdapter.getData(setFavorate());
                mView.setAdapter(mAdapter);


                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public TvShow[] setFavorate() {

        return mFavorate;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {

                    deliverResult(mTaskData);
                } else {

                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {



                try {
                    return getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                            null,
                            null, null, null);

                } catch (Exception e) {

                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {

                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        int descriptionIndex = data.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
        int overIndex = data.getColumnIndex(TaskContract.TaskEntry.COLUMN_OVERVIEW);
        int titleIndex = data.getColumnIndex(TaskContract.TaskEntry.COLUMN_TITLE);
        int dateIndex = data.getColumnIndex(TaskContract.TaskEntry.COLUMN_RELASE_DATE);
        int voteIndex = data.getColumnIndex(TaskContract.TaskEntry.COLUMN_VOTE_AVRAGE);
        int pathIndex = data.getColumnIndex(TaskContract.TaskEntry.COLUMN_POSTER_PATH);

        mFavorate = new TvShow[data.getCount()];
        for (int i = 0; i < data.getCount(); ++i) {
            data.moveToPosition(i);
            mFavorate[i] = new TvShow();


            mFavorate[i].setmID(data.getInt(descriptionIndex));
            mFavorate[i].setOverview(data.getString(overIndex));
            mFavorate[i].setReleaseDate(data.getString(dateIndex));
            mFavorate[i].setPosterPath(data.getString(pathIndex));


            mFavorate[i].setVoteAverage(data.getDouble(voteIndex));
            mFavorate[i].setOriginalTitle(data.getString(titleIndex));

        }



        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, TvShowsWidget.class));
        try {
            TvShowsWidget.updateAllWidgets(this, appWidgetManager, appWidgetIds, mFavorate);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


}
