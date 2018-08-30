package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Movie[] mMovieData;
    final private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int movieListItemLayout = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(movieListItemLayout, null);
        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Picasso
                .with(holder.mPosterImageView.getContext())
                .load("http://image.tmdb.org/t/p/" + "w154/" + mMovieData[position].getmMoviePoster())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) {
            return 0;
        }
        return mMovieData.length;
    }

    public void setMovieData(String[] data) throws JSONException {
        JSONObject networkCallReponseJson = new JSONObject(data[0]);
        JSONArray movieArray = networkCallReponseJson.getJSONArray("results");

        Movie[] movieData = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject currentMovie = movieArray.getJSONObject(i);
            String title = currentMovie.getString("title");
            String posterPath = currentMovie.getString("poster_path");
            String synopsis = currentMovie.getString("overview");
            String releaseDate = currentMovie.getString("release_date");
            float rating = (float) currentMovie.getDouble("vote_average");

            Movie movieToAdd = new Movie(title, posterPath, synopsis, releaseDate, rating);

            movieData[i] = movieToAdd;
        }

        this.mMovieData = movieData;
        notifyDataSetChanged();
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPosterImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = view.findViewById(R.id.posterThumbnail_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Movie selectedMovie = mMovieData[position];
            mClickHandler.onClick(selectedMovie);
        }
    }
}