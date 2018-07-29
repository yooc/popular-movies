package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mMovieTitleTextView, mRatingTextView, mReleaseDateTextView, mSynopsisTextView;
    private ImageView mMoviePosterImageVIew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        // Null object reference
        mMovieTitleTextView.setText(intent.getStringExtra("title"));
    }
}
