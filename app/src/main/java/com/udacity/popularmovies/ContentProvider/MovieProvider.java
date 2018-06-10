package com.udacity.popularmovies.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.udacity.popularmovies.Database.MovieContract;
import com.udacity.popularmovies.Database.MovieDbHelper;

public class MovieProvider extends ContentProvider {

    private MovieDbHelper dbHelper = null;
    private static final String AUTHORITY = "com.udacity.popularmovies.ContentProvider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/movies");

    static final int MOVIES = 1;
    static final int MOVIE_ID = 2;

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
        String id = null;
        if(uriMatcher.match(uri) == MOVIE_ID) {
            id = uri.getPathSegments().get(1);
        }
        return dbHelper.getMovies(id, projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long id = dbHelper.addMovie(values);
        getContext().getContentResolver().notifyChange(uri, null);

        if(id > 0) {
            return Uri.parse(CONTENT_URI + "/" + id);
        }else{
            throw new SQLException("Failed to insert into " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == MOVIE_ID) {
            id = uri.getPathSegments().get(1);
        }
        return dbHelper.deleteMovie(id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String id = null;
        if(uriMatcher.match(uri) == MOVIE_ID) {
            id = uri.getPathSegments().get(1);
        }
        return dbHelper.updateMovie(id, values);
    }
}
