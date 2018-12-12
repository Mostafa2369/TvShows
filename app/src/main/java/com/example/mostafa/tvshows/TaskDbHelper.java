package com.example.mostafa.tvshows;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mostafa.tvshows.TaskContract.TaskEntry;

public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorateDB1.db";


    private static final int VERSION = 3;


    TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        final String CREATE_TABLE = "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskEntry.COLUMN_DESCRIPTION + " INTEGER NOT NULL," +
                TaskEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                TaskEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                TaskEntry.COLUMN_RELASE_DATE + " TEXT NOT NULL," +
                TaskEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                TaskEntry.COLUMN_VOTE_AVRAGE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
