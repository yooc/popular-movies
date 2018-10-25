package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.popularmoviesstage1.data.AppDatabase;
import com.example.android.popularmoviesstage1.data.Movie;

public class DetailViewModel extends ViewModel {
    private LiveData<Movie> movie;

    public DetailViewModel(AppDatabase db, int movieId) {
        movie = db.movieDao().findMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

}
