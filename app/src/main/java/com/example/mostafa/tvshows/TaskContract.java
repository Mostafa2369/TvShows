package com.example.mostafa.tvshows;

import android.net.Uri;
import android.provider.BaseColumns;


public class TaskContract {

    public static final String AUTHORITY = "com.example.android.movieapp";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String PATH_TASKS = "movie";

    public static final class TaskEntry implements BaseColumns {


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();


        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_DESCRIPTION = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELASE_DATE = "date";
        public static final String COLUMN_VOTE_AVRAGE = "vote";


    }
}
