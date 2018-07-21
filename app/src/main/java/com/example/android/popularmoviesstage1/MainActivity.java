package com.example.android.popularmoviesstage1;

import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_movie);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mMovieAdapter.setmMovieData(new String[]
                {
                        "https://kraftmint.com/wp-content/uploads/2015/07/phone_wallpaper.jpg",
                        "https://freshmommyblog.com/wp-content/uploads/2015/01/Youve-Got-This-Phone-Background-from-Fresh-Mommy-Blog.jpg"
                });
        mRecyclerView.setAdapter(mMovieAdapter);

        new FetchMovieDataTask().execute("Hello");
    }

    @Override
    public void onClick(String s) {
        Toast t = new Toast(this);
        String toastText = s;
        t.setText(toastText);
        t.show();
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            String location = strings[0];
            URL requestUrl = NetworkUtils.buildURL();

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(requestUrl);

                return new String[]{jsonResponse};

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
