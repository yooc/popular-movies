package com.example.android.popularmoviesstage1;

import java.util.concurrent.Executors;

public class AppExecutor {
    private static final Object LOCK = new Object();
    private static AppExecutor mInstance;
    private final java.util.concurrent.Executor diskIO;

    private AppExecutor(java.util.concurrent.Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutor getInstance() {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new AppExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return mInstance;
    }

    public java.util.concurrent.Executor diskIO() {
        return diskIO;
    }
}
