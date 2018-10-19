package com.example.android.popularmoviesstage1.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final int movieId;
    private final String title;
    private final String moviePoster;
    private final String synopsis;
    private final String releaseDate;
    private final float rating;

    @Ignore
    public Movie(int movieId, String title, String moviePoster, String synopsis, String releaseDate, float rating) {
        this.movieId = movieId;
        this.title = title;
        this.moviePoster = moviePoster;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public Movie(int id, int movieId, String title, String moviePoster, String synopsis, String releaseDate, float rating) {
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.moviePoster = moviePoster;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getRating() {
        return rating;
    }
}
