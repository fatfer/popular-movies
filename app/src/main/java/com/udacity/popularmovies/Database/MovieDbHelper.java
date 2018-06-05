package com.udacity.popularmovies.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.udacity.popularmovies.Database.MovieContract.*;
import com.udacity.popularmovies.Model.Movie;

import java.util.LinkedList;
import java.util.List;

public class MovieDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " TEXT PRIMARY KEY," +
                MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_NAME_ADULT + " INTEGER DEFAULT 0, " +
                MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT, " +
                MovieEntry.COLUMN_NAME_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE + " TEXT, " +
                MovieEntry.COLUMN_NAME_TITLE + " TEXT, " +
                MovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT, " +
                MovieEntry.COLUMN_NAME_POPULARITY + " INTEGER, " +
                MovieEntry.COLUMN_NAME_VOTE_COUNT + " INTEGER, " +
                MovieEntry.COLUMN_NAME_VIDEO + " INTEGER DEFAULT 0, " +
                MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " INTEGER " +
                "); ";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }


    public void addMovie(Movie movie){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MovieEntry._ID, movie.getId());
        values.put(MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(MovieEntry.COLUMN_NAME_ADULT, movie.isAdult());
        values.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        values.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        values.put(MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.getBackdropPath());
        values.put(MovieEntry.COLUMN_NAME_POPULARITY, movie.getPopularity());
        values.put(MovieEntry.COLUMN_NAME_VOTE_COUNT,movie.getVoteCount());
        values.put(MovieEntry.COLUMN_NAME_VIDEO, movie.isVideo());
        values.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());

        db.insert(MovieEntry.TABLE_NAME,
                null,
                values);

        db.close();
    }

    public Movie getMovie(String id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(MovieEntry.TABLE_NAME, // a. table
                        null, // b. column names
                        " _ID = ?", // c. selections
                        new String[] {id}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        Movie movie = null;
        if( cursor != null && cursor.moveToFirst() ){
            movie = getMovie(cursor);
            cursor.close();
        }

        return movie;
    }

    public void deleteMovie(Movie movie) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MovieEntry.TABLE_NAME,
                MovieEntry._ID+" = ?",
                new String[] { String.valueOf(movie.getId()) });

        db.close();
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new LinkedList<>();

        String query = "SELECT  * FROM " + MovieEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Movie movie;
        if (cursor.moveToFirst()) {
            do {
                movie = getMovie(cursor);
                movies.add(movie);
            } while (cursor.moveToNext());
        }

        return movies;
    }

    @NonNull
    public Movie getMovie(Cursor cursor) {
        Movie movie;
        movie = new Movie();
        movie.setId(cursor.getString(cursor.getColumnIndex(MovieEntry._ID)));
        movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_PATH)));
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE)));
        boolean isAdult = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ADULT) == 0 ? true : false;
        movie.setAdult(isAdult);
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_DATE)));
        movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ORIGINAL_TITLE)));
        movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE)));
        movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_BACKDROP_PATH)));
        movie.setPopularity(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POPULARITY)));
        movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_COUNT)));
        boolean isVideo = cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VIDEO) == 0 ? true : false;
        movie.setVideo(isVideo);
        movie.setVoteAverage(cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_AVERAGE)));
        return movie;
    }
}
