package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.popularmoviesstage1.data.AppDatabase;
import com.example.android.popularmoviesstage1.data.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private TextView mMovieTitleTextView, mRatingTextView, mReleaseDateTextView, mSynopsisTextView;
    private ImageView mMoviePosterImageView;
    private ToggleButton mFavoriteToggleButton;
    private RecyclerView mReviewRecyclerView, mTrailerRecyclerView;
    private static ReviewAdapter mReviewAdapter;
    private static TrailerAdapter mTrailerAdapter;

    private AppDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        mMovieTitleTextView = findViewById(R.id.movieTitle_tv);
        mMovieTitleTextView.setText(intent.getStringExtra("title"));

        mRatingTextView = findViewById(R.id.rating_tv);
        mRatingTextView.setText(String.valueOf(intent.getFloatExtra("rating", (float) 0.0)));

        mReleaseDateTextView = findViewById(R.id.releaseDate_tv);
        mReleaseDateTextView.setText(intent.getStringExtra("releaseDate"));

        mSynopsisTextView = findViewById(R.id.synopsis_tv);
        mSynopsisTextView.setText(intent.getStringExtra("synopsis"));

        mMoviePosterImageView = findViewById(R.id.moviePoster_iv);
        Picasso
                .with(this)
                .load("http://image.tmdb.org/t/p/" + "w500/" + intent.getStringExtra("poster"))
                .error(R.drawable.ic_launcher_foreground)
                .into(mMoviePosterImageView);

        mFavoriteToggleButton = findViewById(R.id.favorite_tb);
        mFavoriteToggleButton.setText(R.string.favorite_button);
        mFavoriteToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteClicked();
            }
        });

        mReviewRecyclerView = findViewById(R.id.reviews_rv);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        LinearLayoutManager reviewsManager = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(reviewsManager);
        mReviewRecyclerView.setNestedScrollingEnabled(false);

        mTrailerRecyclerView = findViewById(R.id.trailers_rv);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        LinearLayoutManager trailersManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(trailersManager);
        mTrailerRecyclerView.setNestedScrollingEnabled(false);

        mDatabase = AppDatabase.getInstance(getApplicationContext());

        if (NetworkUtils.isNetworkAvailable(this)) {
            fetchReviews(intent.getIntExtra("id", 0));
            fetchTrailers(intent.getIntExtra("id", 0));
        } else {
            Toast.makeText(this, "No network available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchReviews(int movieId) {
        new FetchReviewsDataTask().execute(movieId);
    }

    private void fetchTrailers(int movieId) {
        new FetchTrailersDataTask().execute(movieId);
    }

    private void onFavoriteClicked() {
        Movie currentMovie = getCurrentMovie();

        if (inFavorites(currentMovie.getMovieId())) {
            removeFromFavorites(currentMovie);
            Log.d("Halp", "Removed");
        } else {
            addToFavorites(currentMovie);
            Log.d("Halp", "Added");
        }

//        Movie[] currentFavorites = getFavorites();
    }

    private Movie getCurrentMovie() {
        return new Movie(
                    getIntent().getIntExtra("id", 0),
                    getIntent().getStringExtra("moviePoster"),
                    getIntent().getStringExtra("title"),
                    getIntent().getStringExtra("synopsis"),
                    getIntent().getStringExtra("releaseDate"),
                    getIntent().getFloatExtra("rating", 0)
            );
    }

    private boolean inFavorites(final int movieId) {
        final boolean[] isAdded = {false};
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
//                Log.d("Hello", mDatabase.movieDao().findMovieById(movieId).getTitle());
                if (mDatabase.movieDao().findMovieById(movieId) != null) {
                    isAdded[0] = true;
                }
            }
        });
        return isAdded[0];
    }

    private Movie[] getFavorites() {
        final Movie[][] favorites = {{}};
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Movie> currentFavorites = mDatabase.movieDao().loadFavorites();
                int i = 0;
                for (Movie movie : currentFavorites) {
                    favorites[0][i] = movie;
                    i++;
                }
            }
        });
        return favorites[0];
    }

    private void addToFavorites(final Movie movie) {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.movieDao().insertMovie(movie);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFavoriteToggleButton.setPressed(true);
                        mFavoriteToggleButton.setText(R.string.unfavorite_button);
                    }
                });
            }
        });
    }

    private void removeFromFavorites(final Movie movie) {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.movieDao().deleteMovie(movie);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFavoriteToggleButton.setPressed(false);
                        mFavoriteToggleButton.setText(R.string.favorite_button);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(Trailer trailer) {
        Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + trailer.getmKey());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public static class FetchReviewsDataTask extends AsyncTask<Integer, Void, String[]> {

        @Override
        protected String[] doInBackground(Integer... id) {
            if (id.length == 0) return null;
            int movieId = id[0];

            URL requestUrl = NetworkUtils.buildReviewsURL(movieId);

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(requestUrl);

                return new String[]{jsonResponse};

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            try {
                mReviewAdapter.setReviewData(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class FetchTrailersDataTask extends AsyncTask<Integer, Void, String[]> {

        @Override
        protected String[] doInBackground(Integer... id) {
            if (id.length == 0) return null;
            int movieId = id[0];

            URL requestUrl = NetworkUtils.buildTrailersURL(movieId);

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(requestUrl);

                return new String[]{jsonResponse};

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            try {
                mTrailerAdapter.setTrailerData(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
