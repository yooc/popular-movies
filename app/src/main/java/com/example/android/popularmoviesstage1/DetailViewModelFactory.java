package com.example.android.popularmoviesstage1;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.android.popularmoviesstage1.data.AppDatabase;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDatabase;
    private final int mMovieId;

    public DetailViewModelFactory(AppDatabase database, int movieId) {
        mDatabase = database;
        mMovieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new DetailViewModel(mDatabase, mMovieId);
    }
}
