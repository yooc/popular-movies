package com.example.android.popularmoviesstage1;

import java.util.concurrent.Executors;

public class Executor {
    private static final Object LOCK = new Object();
    private static Executor mInstance;
    private final java.util.concurrent.Executor diskIO;

    private Executor(java.util.concurrent.Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static Executor getInstance() {
        if (mInstance == null) {
            synchronized (LOCK) {
                mInstance = new Executor(Executors.newSingleThreadExecutor());
            }
        }
        return mInstance;
    }

    public java.util.concurrent.Executor diskIO() {
        return diskIO;
    }
}
