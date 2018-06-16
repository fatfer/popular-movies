package com.udacity.popularmovies.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udacity.popularmovies.Database.MovieDbHelper;

public class MovieProvider extends ContentProvider {

    private MovieDbHelper dbHelper = null;
    private static final String AUTHORITY = "com.udacity.popularmovies.ContentProvider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/movies");

    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI(AUTHORITY, "movies", MOVIES);
        uriMatcher.addURI(AUTHORITY, "movies/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if(uriMatcher.match(uri) == MOVIE_ID) {
            String id = uri.getPathSegments().get(1);
            return dbHelper.getMovies(id, projection, selection, selectionArgs, sortOrder);
        }else if(uriMatcher.match(uri) == MOVIES){
            return dbHelper.getMovies(null, projection, selection, selectionArgs, sortOrder);
        }
        else{
            throw new UnsupportedOperationException("uri not found: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if(uriMatcher.match(uri) == MOVIES) {
            long id = dbHelper.addMovie(values);
            getContext().getContentResolver().notifyChange(uri, null);

            if (id > 0) {
                return Uri.parse(CONTENT_URI + "/" + id);
            } else {
                throw new SQLException("Failed to insert into " + uri);
            }
        }else{
            throw new UnsupportedOperationException("uri not found: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        if(uriMatcher.match(uri) == MOVIE_ID) {
            String id = uri.getPathSegments().get(1);
            return dbHelper.deleteMovie(id);
        }else{
            throw new UnsupportedOperationException("uri not found: " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        if(uriMatcher.match(uri) == MOVIE_ID) {
           String id = uri.getPathSegments().get(1);
            return dbHelper.updateMovie(id, values);
        }else{
            throw new UnsupportedOperationException("uri not found: " + uri);
        }
    }
}
