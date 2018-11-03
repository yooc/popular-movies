package com.example.android.popularmoviesstage1;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class MainModelViewFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application mApplication;
    private String mFilter;

    public MainModelViewFactory(Application application, String filter) {
        mApplication = application;
        mFilter = filter;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mApplication, mFilter);
    }
}
