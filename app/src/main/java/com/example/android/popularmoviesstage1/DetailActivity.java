package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.persistence.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private Movie mCurrentMovie;
    private TextView mMovieTitleTextView, mRatingTextView, mReleaseDateTextView, mSynopsisTextView;
    private ImageView mMoviePosterImageView;
    private Button mFavoriteButton;
    private RecyclerView mReviewRecyclerView, mTrailerRecyclerView;
    private static ReviewAdapter mReviewAdapter;
    private static TrailerAdapter mTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setCurrentMovie();

        mMovieTitleTextView = findViewById(R.id.movieTitle_tv);
        mMovieTitleTextView.setText(mCurrentMovie.getTitle());

        mRatingTextView = findViewById(R.id.rating_tv);
        mRatingTextView.setText(String.valueOf(mCurrentMovie.getRating()));

        mReleaseDateTextView = findViewById(R.id.releaseDate_tv);
        mReleaseDateTextView.setText(mCurrentMovie.getReleaseDate());

        mSynopsisTextView = findViewById(R.id.synopsis_tv);
        mSynopsisTextView.setText(mCurrentMovie.getSynopsis());

        mMoviePosterImageView = findViewById(R.id.moviePoster_iv);

        Picasso
                .with(this)
                .load("http://image.tmdb.org/t/p/" + "w500/" + mCurrentMovie.getMoviePoster())
                .error(R.drawable.ic_launcher_foreground)
                .into(mMoviePosterImageView);

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

        setFavoriteButtonText();
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteButtonClicked();
            }
        });

        if (NetworkUtils.isNetworkAvailable(this)) {
            fetchReviews(mCurrentMovie.getMovieId());
            fetchTrailers(mCurrentMovie.getMovieId());
        } else {
            Toast.makeText(this, "No network available.", Toast.LENGTH_SHORT).show();
        }
    }

    private Movie getCurrentMovie() {
        return new Movie(
                getIntent().getIntExtra("id", 0),
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("poster"),
                getIntent().getStringExtra("synopsis"),
                getIntent().getStringExtra("releaseDate"),
                getIntent().getFloatExtra("rating", 0)
        );
    }

    private void setCurrentMovie() {
        mCurrentMovie = getCurrentMovie();
    }

    private void onFavoriteButtonClicked() {
        if (mFavoriteButton.getText().equals(R.string.unfavorite_button)) {
            removeFromFavorites(mCurrentMovie);
        } else {
            addToFavorites(mCurrentMovie);
        }
    }

    private void setFavoriteButtonText() {
        mFavoriteButton = findViewById(R.id.favorite_btn);
        DetailViewModelFactory factory = new DetailViewModelFactory(getApplication(), mCurrentMovie.getMovieId());
        final DetailViewModel viewModel = ViewModelProviders
                .of(this, factory)
                .get(DetailViewModel.class);

        viewModel.getMovieById(mCurrentMovie.getMovieId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                viewModel.getMovieById(mCurrentMovie.getMovieId()).removeObserver(this);

                if (movie != null) {
                    mFavoriteButton.setText(R.string.unfavorite_button);
                } else {
                    mFavoriteButton.setText(R.string.favorite_button);
                }
            }
        });
    }

    private void addToFavorites(final Movie movie) {
        final DetailViewModel viewModel = ViewModelProviders
                .of(this)
                .get(DetailViewModel.class);

        viewModel.addToFavorite(movie);
        setFavoriteButtonText();
    }

    private void removeFromFavorites(final Movie movie) {
        final DetailViewModel viewModel = ViewModelProviders
                .of(this)
                .get(DetailViewModel.class);

        viewModel.removeFromFavorite(movie);
        setFavoriteButtonText();
    }

    private void fetchReviews(int movieId) {
        new FetchReviewsDataTask().execute(movieId);
    }

    private void fetchTrailers(int movieId) {
        new FetchTrailersDataTask().execute(movieId);
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
