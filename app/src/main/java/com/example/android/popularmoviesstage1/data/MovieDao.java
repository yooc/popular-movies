package com.example.android.popularmoviesstage1.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorites")
    LiveData<List<Movie>> loadFavorites();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovie(Movie movie);

    @Query("DELETE FROM favorites WHERE movieId = :movieId")
    void deleteMovie(int movieId);

    @Query("SELECT * FROM favorites WHERE movieId = :movieId")
    LiveData<Movie> findMovieById(int movieId);
}
