package com.example.android.popularmoviesstage1.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorites")
    List<Movie> loadFavorites();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
