package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.data.AppDatabase;
import com.example.android.popularmoviesstage1.data.Movie;
import com.facebook.stetho.Stetho;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static String FILTER_EXTRA = "filter";

    private RecyclerView mRecyclerView;
    private static MovieAdapter mMovieAdapter;

    private String mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mFilter = savedInstanceState.getString(FILTER_EXTRA);
        } else {
            mFilter = getString(R.string.popular_menu_item);
        }

        mRecyclerView = findViewById(R.id.movie_rv);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (NetworkUtils.isNetworkAvailable(this)) {
            fetchMovies(mFilter);
        } else {
            Toast.makeText(this, "No network available.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filters_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.by_popularity) {
            mFilter = getString(R.string.popular_menu_item);
            fetchMovies(mFilter);

            return true;
        }
        if (itemId == R.id.by_rating) {
            mFilter = getString(R.string.rating_menu_item);
            fetchMovies(mFilter);
            return true;
        }

        if (itemId == R.id.by_favorites) {
            mFilter = getString(R.string.favorites_menu_item);
            fetchMovies(mFilter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchMovies(String filter) {
        if (filter.equals(getString(R.string.favorites_menu_item))) {
            MainModelViewFactory factory = new MainModelViewFactory(getApplication(), filter);
            final MainViewModel viewModel = ViewModelProviders
                    .of(this, factory)
                    .get(MainViewModel.class);

            viewModel.getFavorites().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    mMovieAdapter.setFavoritesAsMovieData(movies);
                }
            });
        } else {
            new FetchMovieDataTask().execute(filter);
        }
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

    public class FetchMovieDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            if (strings.length == 0) return null;
            String filter = strings[0];

            URL requestUrl = NetworkUtils.buildListURL(filter);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(FILTER_EXTRA, mFilter);
    }
}