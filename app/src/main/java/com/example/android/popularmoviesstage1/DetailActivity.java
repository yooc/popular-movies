package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView mMovieTitleTextView, mRatingTextView, mReleaseDateTextView, mSynopsisTextView;
    private ImageView mMoviePosterImageView;

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
                .load("http://image.tmdb.org/t/p/" + "w185/" + intent.getStringExtra("poster"))
                .error(R.drawable.ic_launcher_foreground)
                .into(mMoviePosterImageView);
    }
}
