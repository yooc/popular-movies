package com.example.android.popularmoviesstage1;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application mApplication;
    private final int mMovieId;

    public DetailViewModelFactory(Application application, int movieId) {
        mApplication = application;
        mMovieId = movieId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(mApplication, mMovieId);
    }
}
