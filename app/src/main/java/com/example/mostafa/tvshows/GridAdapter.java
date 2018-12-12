package com.example.mostafa.tvshows;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridAdapter extends ArrayAdapter<TvShow> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<TvShow> data;

    public GridAdapter(Context context, int layoutResourceId, ArrayList<TvShow> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
    }

    static class ViewHolder {

        @BindView(R.id.text1)
        ImageView mImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        TvShow currentTvShow = data.get(position);

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        if (!currentTvShow.getPosterPath2().equals("")) {
            Picasso.get().load(currentTvShow.getPosterPath2()).into(holder.mImage);
        }
        return convertView;
    }

}
