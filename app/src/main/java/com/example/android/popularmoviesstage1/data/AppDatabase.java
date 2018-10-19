package com.example.android.popularmoviesstage1.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import static android.arch.persistence.room.Room.databaseBuilder;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorites";
    private static AppDatabase mInstance;

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new instance");
                mInstance = databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG, "Getting instance");
        return mInstance;
    }

    public abstract MovieDao movieDao();
}