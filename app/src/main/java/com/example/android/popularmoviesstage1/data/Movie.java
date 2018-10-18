package com.example.android.popularmoviesstage1.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final int mMovieId;
    private final String mTitle;
    private final String mMoviePoster;
    private final String mSynopsis;
    private final String mReleaseDate;
    private final float mRating;

    @Ignore
    public Movie(int mMovieId, String mTitle, String mMoviePoster, String mSynopsis, String mReleaseDate, float mRating) {
        this.mMovieId = mMovieId;
        this.mTitle = mTitle;
        this.mMoviePoster = mMoviePoster;
        this.mSynopsis = mSynopsis;
        this.mReleaseDate = mReleaseDate;
        this.mRating = mRating;
    }

    public Movie(int id, int mMovieId, String mTitle, String mMoviePoster, String mSynopsis, String mReleaseDate, float mRating) {
        this.id = id;
        this.mMovieId = mMovieId;
        this.mTitle = mTitle;
        this.mMoviePoster = mMoviePoster;
        this.mSynopsis = mSynopsis;
        this.mReleaseDate = mReleaseDate;
        this.mRating = mRating;
    }

    public int getId() {
        return id;
    }

    public int getmMovieId() {
        return mMovieId;
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
