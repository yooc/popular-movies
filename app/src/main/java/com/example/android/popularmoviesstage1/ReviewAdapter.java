package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private Review[] mReviewData;

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int reviewListItemLayout = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(reviewListItemLayout, null);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.mAuthorTextView.setText(mReviewData[position].getmAuthor());
        holder.mContentTextView.setText(mReviewData[position].getmContent());
    }

    @Override
    public int getItemCount() {
        if (mReviewData == null) {
            return 0;
        }
        return mReviewData.length;
    }

    public void setReviewData(String[] data) throws JSONException {
        JSONObject networkCallResponseJson = new JSONObject(data[0]);
        JSONArray reviewArray = networkCallResponseJson.getJSONArray("results");

        Review[] reviewData = new Review[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject currentReview = reviewArray.getJSONObject(i);
            String author = currentReview.getString("author");
            String content = currentReview.getString("content");

            Review reviewToAdd = new Review(author, content);

            reviewData[i] = reviewToAdd;
        }

        this.mReviewData = reviewData;
        notifyDataSetChanged();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAuthorTextView, mContentTextView;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = view.findViewById(R.id.author_tv);
            mContentTextView = view.findViewById(R.id.content_tv);
        }
    }
}
