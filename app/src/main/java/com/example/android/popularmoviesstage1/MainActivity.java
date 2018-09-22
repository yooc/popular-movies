package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private Switch mSwitch;
    private static MovieAdapter mMovieAdapter;

    private Boolean sortByRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movie);
        mSwitch = findViewById(R.id.filter_switch);

        sortByRating = mSwitch.isChecked();

        mSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fetchMovies(isChecked);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

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
                .putExtra("title", movie.getmTitle())
                .putExtra("rating", movie.getmRating())
                .putExtra("releaseDate", movie.getmReleaseDate())
                .putExtra("synopsis", movie.getmSynopsis())
                .putExtra("poster", movie.getmMoviePoster());

        startActivity(intent);
    }

    public static class FetchMovieDataTask extends AsyncTask<Boolean, Void, String[]> {

        @Override
        protected String[] doInBackground(Boolean... booleans) {
            if (booleans.length == 0) return null;
            Boolean filterByRating = booleans[0];

            URL requestUrl = NetworkUtils.buildURL(filterByRating);

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
}