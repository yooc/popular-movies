package com.example.android.popularmoviesstage1.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MovieRepository {
    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mFavorites;

    public MovieRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application.getApplicationContext());
        mMovieDao = database.movieDao();
        mFavorites = mMovieDao.loadFavorites();
    }

    LiveData<List<Movie>> getFavorites() {
        return mFavorites;
    }

    public LiveData<Movie> getMovieFromId(int movieId) {
        return mMovieDao.findMovieById(movieId);
    }

    public void insert(Movie movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }

    public void delete(Movie movie) {
        new deleteAsyncTask(mMovieDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... movies) {
            mAsyncTaskDao.insertMovie(movies[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            mAsyncTaskDao.deleteMovie(movies[0]);
            return null;
        }
    }

}
