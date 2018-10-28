package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.persistence.AppDatabase;
import com.example.android.popularmoviesstage1.persistence.Movie;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private AppDatabase mDatabase;

    private RecyclerView mRecyclerView;
    private Switch mSwitch;
    private static MovieAdapter mMovieAdapter;

    private Boolean sortByRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.movie_rv);
        mSwitch = findViewById(R.id.filter_switch);

        sortByRating = mSwitch.isChecked();

        mSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fetchMovies(isChecked);
            }
        });

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mDatabase = AppDatabase.getInstance(getApplicationContext());

        if(NetworkUtils.isNetworkAvailable(this)) {
            fetchMovies(sortByRating);
        } else {
            Toast.makeText(this, "No network available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMovies(Boolean isChecked) {
        new FetchMovieDataTask().execute(isChecked);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationActivity = DetailActivity.class;
        Intent intent = new Intent(context, destinationActivity);

        intent
                .putExtra("id", movie.getMovieId())
                .putExtra("title", movie.getTitle())
                .putExtra("rating", movie.getRating())
                .putExtra("releaseDate", movie.getReleaseDate())
                .putExtra("synopsis", movie.getSynopsis())
                .putExtra("poster", movie.getMoviePoster());

        startActivity(intent);
    }

    public static class FetchMovieDataTask extends AsyncTask<Boolean, Void, String[]> {

        @Override
        protected String[] doInBackground(Boolean... booleans) {
            if (booleans.length == 0) return null;
            Boolean filterByRating = booleans[0];

            URL requestUrl = NetworkUtils.buildListURL(filterByRating);

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
                mMovieAdapter.setMovieData(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchFavorites() {
        LiveData<List<Movie>> favorites = mDatabase.movieDao().loadFavorites();
        favorites.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieAdapter.setFavorites(movies);
            }
        });
    }
}