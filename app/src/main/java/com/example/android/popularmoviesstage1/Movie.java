package com.example.android.popularmoviesstage1;

public class Movie {
    public final String mTitle, mMoviePoster, mSynopsis, mReleaseDate;
    public final float mRating;

    public Movie(String mTitle, String mMoviePoster, String mSynopsis, String mReleaseDate, float mRating) {
        this.mTitle = mTitle;
        this.mMoviePoster = mMoviePoster;
        this.mSynopsis = mSynopsis;
        this.mReleaseDate = mReleaseDate;
        this.mRating = mRating;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmMoviePoster() {
        return mMoviePoster;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public float getmRating() {
        return mRating;
    }
}
