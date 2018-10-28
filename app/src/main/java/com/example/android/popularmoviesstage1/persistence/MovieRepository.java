package com.example.android.popularmoviesstage1.persistence;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class MovieRepository {

    private static final String LOG_TAG = MovieRepository.class.getSimpleName();

    private MovieDao mMovieDao;
    private LiveData<Movie> mQueryResult;
    private LiveData<List<Movie>> mFavorites;

    public MovieRepository(Application application, int movieId) {
        AppDatabase database = AppDatabase.getInstance(application.getApplicationContext());
        mMovieDao = database.movieDao();
        mQueryResult = mMovieDao.findMovieById(movieId);
        mFavorites = mMovieDao.loadFavorites();
    }

    LiveData<List<Movie>> getFavorites() {
        return mFavorites;
    }

    public LiveData<Movie> getMovieFromId(int movieId) {
//        new queryByIdAsyncTask(mMovieDao).execute(movieId);
        new queryByIdAsyncTask(mMovieDao).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, movieId);
        return mQueryResult;
    }

    public void insert(Movie movie) {
//        new insertAsyncTask(mMovieDao).execute(movie);
        new insertAsyncTask(mMovieDao).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, movie);
    }

    public void delete(Movie movie) {
//        new deleteAsyncTask(mMovieDao).execute(movie);
        new deleteAsyncTask(mMovieDao).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, movie);
    }

    private class queryByIdAsyncTask extends AsyncTask<Integer, Void, LiveData<Movie>> {
        private MovieDao mAsyncTaskDao;

        queryByIdAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<Movie> doInBackground(Integer... integers) {
            return mAsyncTaskDao.findMovieById(integers[0]);
        }

        @Override
        protected void onPostExecute(LiveData<Movie> movieLiveData) {
            mQueryResult = movieLiveData;
        }
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

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(LOG_TAG, "Added to database");
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

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(LOG_TAG, "Removed from database");
        }
    }

}
