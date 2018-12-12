package com.example.mostafa.tvshows;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

public class TvShowDetails extends AppCompatActivity implements ReviewAdapter.ReviewAdapterOnClickHandler {
    private ReviewAdapter mAdapter;
    private RecyclerView mView;
    private TvShow data;
    private boolean infav;
    private ImageView mFav;
    private FetchTrailer mFetchTrailer = new FetchTrailer();
    private FetchReview mFetchReview = new FetchReview();
    private URL link = null;
    private String[] mTrailerList;
    private String[] mReviewlist;
    private String NO_REV = " No Reviews";
    private String POS = "position";
    private URL linkRev = null;
    private TextView mRevList;
    Context con = this;

    @Override
    public void onClick(int mMoviePosition) {
        String url = "https://www.youtube.com/watch?v=" + mTrailerList[mMoviePosition];
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        infav = false;
        mFav = (ImageView) findViewById(R.id.fav2);
        Intent intent = getIntent();

        data = intent.getParcelableExtra(POS);

        updateUi();

        mAdapter = new ReviewAdapter(this);
        String id = "" + data.getID();
        try {
            link = mFetchTrailer.getUrl(id);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        try {
            linkRev = mFetchReview.getUrl(id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new getMoviesTrailer().execute(link);
        updateFavorate(data.getID());


    }

    public void clickeventd(View view) {

        if (infav) {
            mFav = (ImageView) findViewById(R.id.fav2);
            mFav.setColorFilter(0x00000000);
            String stringId = "" + data.getID();
            Uri uri = TaskContract.TaskEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            getContentResolver().delete(uri, null, null);
            infav = false;
        } else if (!infav) {
            mFav = (ImageView) findViewById(R.id.fav2);
            mFav.setColorFilter(0xFFFF0000);

            ContentValues contentValues = new ContentValues();

            contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, data.getID());
            contentValues.put(TaskContract.TaskEntry.COLUMN_OVERVIEW, data.getOverview());
            contentValues.put(TaskContract.TaskEntry.COLUMN_POSTER_PATH, data.getPosterPath());
            contentValues.put(TaskContract.TaskEntry.COLUMN_RELASE_DATE, data.getReleaseDate());
            contentValues.put(TaskContract.TaskEntry.COLUMN_TITLE, data.getOriginalTitle());
            contentValues.put(TaskContract.TaskEntry.COLUMN_VOTE_AVRAGE, "" + data.getVoteAverage());
            getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);
            infav = true;
        }


    }


    public class getMoviesTrailer extends AsyncTask<URL, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            URL link = urls[0];

            try {
                mReviewlist = mFetchReview.getReview(mFetchReview.getJsonStr(linkRev));
                return mFetchTrailer.getTrailer(mFetchTrailer.getJsonStr(link));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] reviews) {
            mTrailerList = reviews;
            mView = (RecyclerView) findViewById(R.id.Review_recycle);
            mView.setLayoutManager(new LinearLayoutManager(TvShowDetails.this));
            mAdapter.getData(reviews);
            mView.setAdapter(mAdapter);
            mRevList = (TextView) findViewById(R.id.reviewlist);
            if (mReviewlist.length < 1) {
                mRevList.append(NO_REV);
            }
            mRevList.append("\n");

            for (int j = 0; j < mReviewlist.length; ++j) {
                String[] item = mReviewlist[j].split("@");
                mRevList.append("\n" + item[0] + ":" + "\n" + item[1] + "\n___________________________");


            }


        }
    }

    public void updateUi() {
        ImageView poster = (ImageView) findViewById(R.id.poster);
        if (data.getPosterPath().length() < 90) {
            Picasso.get()
                    .load(data.getPosterPath())
                    .resize(185, 278)
                    .centerCrop()
                    .into(poster);
        } else {
            Picasso.get()
                    .load(data.getPosterPath2())
                    .resize(185, 278)
                    .centerCrop()
                    .into(poster);
        }


        TextView title = (TextView) findViewById(R.id.titlee);
        title.append(data.getOriginalTitle());
        TextView vote = (TextView) findViewById(R.id.vote);
        vote.append("" + data.getVoteAverage());
        String mDate = data.getReleaseDate();
        try {
            mDate = Date_Format.getLocalizedDate(con,
                    mDate, data.getDateFormat());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TextView date = (TextView) findViewById(R.id.date);
        date.append(mDate);
        TextView over = (TextView) findViewById(R.id.overview);
        over.append(data.getOverview() + "\n\n_____________________");
    }

    public void updateFavorate(int id) {
        String where = "movieid=" + id;
        Cursor c = getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                where,
                null,
                null);
        mFav = (ImageView) findViewById(R.id.fav2);
        if (c.moveToFirst() == true) {
            int idIndex = c.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
            int idindex = c.getInt(idIndex);
            if (idindex == id)

            {
                mFav.setColorFilter(0xFFFF0000);
                infav = true;

            }
            c.close();
        }

    }


}






