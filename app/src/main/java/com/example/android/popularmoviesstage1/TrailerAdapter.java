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

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private Trailer[] mTrailerData;
    final private TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int trailerListItemLayout = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(trailerListItemLayout, null);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        holder.mTrailerTextView.setText(mTrailerData[position].getmName());
    }

    @Override
    public int getItemCount() {
        if (mTrailerData == null) {
            return 0;
        }
        return mTrailerData.length;
    }

    public void setTrailerData(String[] data) throws JSONException {
        JSONObject networkCallResponseJson = new JSONObject(data[0]);
        JSONArray trailerArray = networkCallResponseJson.getJSONArray("results");

        Trailer[] trailerData = new Trailer[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject currentTrailer = trailerArray.getJSONObject(i);

            String key = currentTrailer.getString("key");
            String name = currentTrailer.getString("name");

            Trailer trailerToAdd = new Trailer(key, name);

            trailerData[i] = trailerToAdd;
        }
        this.mTrailerData = trailerData;
        notifyDataSetChanged();
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mTrailerTextView;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mTrailerTextView = itemView.findViewById(R.id.trailer_name_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Trailer selectedTrailer = mTrailerData[position];
            mClickHandler.onClick(selectedTrailer);
        }
    }
}
