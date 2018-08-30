package com.example.android.popularmoviesstage1;

class Movie {
    private final String mTitle;
    private final String mMoviePoster;
    private final String mSynopsis;
    private final String mReleaseDate;
    private final float mRating;

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
