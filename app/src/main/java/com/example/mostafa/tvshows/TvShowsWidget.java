package com.example.mostafa.tvshows;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class TvShowsWidget extends AppWidgetProvider {
    static Bitmap[] b = new Bitmap[4];
    private static String GET = "GET";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, TvShow[] name) throws IOException {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tvshow_app_widget);
        if (name.length != 0) {

            new GetBit().execute(name);
            if (b[0] != null) {
                views.setImageViewBitmap(R.id.image, b[0]);

                if (b[1] != null) {

                    views.setImageViewBitmap(R.id.image2, b[1]);


                    if (b[2] != null) {
                        views.setImageViewBitmap(R.id.image3, b[2]);
                        if (b[3] != null)
                            views.setImageViewBitmap(R.id.image4, b[3]);
                    }
                }
            }

        }


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, TvShow[] name) throws IOException {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, name);
        }
    }

    public static class GetBit extends AsyncTask<TvShow, Void, Bitmap[]> {
        @Override
        protected Bitmap[] doInBackground(TvShow... url) {

            Bitmap[] bit = new Bitmap[4];
            Bitmap myBitmap;
            try {
                for (int i = 0; i < url.length; ++i) {
                    if (url[i] != null) {
                        TvShow urll = url[i];
                        URL url2 = new URL(urll.getPosterPath2());
                        HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                        connection.setDoInput(true);
                        connection.setRequestMethod(GET);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        myBitmap = BitmapFactory.decodeStream(input);
                        bit[i] = myBitmap;
                    }
                }


                return bit;

            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;

        }


        @Override
        protected void onPostExecute(Bitmap[] bitmap) {
            super.onPostExecute(bitmap);
            b = bitmap;
        }
    }


}
