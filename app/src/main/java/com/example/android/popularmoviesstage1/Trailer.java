package com.example.android.popularmoviesstage1;

public class Trailer {
    private final String mKey;
    private final String mName;

    public Trailer(String mKey, String mName) {
        this.mKey = mKey;
        this.mName = mName;
    }

    public String getmKey() {
        return mKey;
    }

    public String getmName() {
        return mName;
    }
}
