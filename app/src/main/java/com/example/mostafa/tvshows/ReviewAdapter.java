package com.example.mostafa.tvshows;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private ReviewAdapterOnClickHandler mClickHandler = null;
    private String[] revs;
    private String TRAIL = "Trial: ";

    public interface ReviewAdapterOnClickHandler
    {

        void onClick(int hello);
    }

    public ReviewAdapter(ReviewAdapterOnClickHandler click) {
        mClickHandler = click;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView mReviewText;

        public ReviewAdapterViewHolder(View itemView)
        {
            super(itemView);
            mReviewText = (TextView) itemView.findViewById(R.id.review);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int mAdapterPosition = getAdapterPosition();
            mClickHandler.onClick(mAdapterPosition);

        }
    }


    @NonNull
    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        int p = position + 1;
        holder.mReviewText.setText(TRAIL + p);
    }

    public void getData(String[] rev) {
        revs = rev;
        notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return revs.length;
    }
}
