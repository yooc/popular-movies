package com.example.android.popularmoviesstage1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.popularmoviesstage1.data.Movie;
import com.example.android.popularmoviesstage1.data.MovieRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private MovieRepository mRepository;

    public MainViewModel(Application application) {
        super(application);
        mRepository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getFavorites() {
        return mRepository.getFavorites();
    }
}
