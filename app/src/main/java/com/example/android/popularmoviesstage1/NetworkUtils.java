package com.example.android.popularmoviesstage1;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BY_POPULARITY = "top_rated";
    private static final String BY_RATING = "popular";
    private static final String API_KEY = "";

    public static URL buildURL(Boolean filterByRating) {
        //Build Uri based on whether is sorting type, get poster, etc
        String filterURL;

        if (filterByRating) {
            filterURL = BASE_URL + BY_RATING;
        } else {
            filterURL = BASE_URL + BY_POPULARITY;
        }

        Uri builtUri = Uri.parse(filterURL).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
