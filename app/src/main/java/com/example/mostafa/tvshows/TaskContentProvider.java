package com.example.mostafa.tvshows;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.mostafa.tvshows.TaskContract.TaskEntry.TABLE_NAME;

public class TaskContentProvider extends ContentProvider {


    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;
    private String MOV = "movieid=?";

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private String FAILD = "Failed to insert row into ";
    private String UNKOWN = "Unknown uri: ";

    public static UriMatcher buildUriMatcher() {


        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS, TASKS);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_WITH_ID);

        return uriMatcher;
    }


    private TaskDbHelper mTaskDbHelper;


    @Override
    public boolean onCreate() {

        Context context = getContext();
        mTaskDbHelper = new TaskDbHelper(context);
        return true;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();


        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TASKS:

                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException(FAILD + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException(UNKOWN + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {


        final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();


        int match = sUriMatcher.match(uri);
        Cursor retCursor;


        switch (match) {

            case TASKS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException(UNKOWN + uri);
        }


        retCursor.setNotificationUri(getContext().getContentResolver(), uri);


        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {


        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int tasksDeleted;


        switch (match) {

            case TASK_WITH_ID:

                String id = uri.getPathSegments().get(1);

                tasksDeleted = db.delete(TABLE_NAME, MOV, new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException(UNKOWN + uri);
        }


        if (tasksDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }


        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}
