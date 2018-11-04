package com.example.android.popularmoviesstage1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.popularmoviesstage1.data.Movie;
import com.example.android.popularmoviesstage1.data.MovieRepository;

public class DetailViewModel extends AndroidViewModel {
    private MovieRepository mRepository;

    public DetailViewModel(Application application, int movieId) {
        super(application);
        mRepository = new MovieRepository(application, movieId);
    }

    public LiveData<Movie> getMovieById(int movieId) {
        return mRepository.getMovieFromId(movieId);
    }

    public void addToFavorite(Movie movie) {
        mRepository.insert(movie);
    }

    public void removeFromFavorite(Movie movie) {
        mRepository.delete(movie);
    }
}