package com.udacity.popularmovies.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.udacity.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Network {

    private final static String THEMOVIEDB_BASE = "https://api.themoviedb.org/3/movie";
    private final static String THEMOVIEDB_BASE_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    private final static String THEMOVIEDB_BASE_HIGHEST_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated";
    private final static String PARAM_API_KEY = "api_key";

    public static URL buildPopularMoviesUrl() {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_POPULAR_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildHighestRatedMoviesUrl() {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE_HIGHEST_RATED_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieTrailersUrl(String movieID) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE).buildUpon()
                .appendPath(movieID)
                .appendPath("videos")
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieReviewsUrl(String movieID) {
        Uri builtUri = Uri.parse(THEMOVIEDB_BASE).buildUpon()
                .appendPath(movieID)
                .appendPath("reviews")
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
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

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
