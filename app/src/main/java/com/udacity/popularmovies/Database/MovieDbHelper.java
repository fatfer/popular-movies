package com.udacity.popularmovies.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
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


    public long addMovie(ContentValues values){

        return getWritableDatabase().insert(MovieEntry.TABLE_NAME, "", values);
    }

    public int deleteMovie(String id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(MovieEntry.TABLE_NAME,
                MovieEntry._ID + " = ?",
                new String[] {id});

    }

    public int updateMovie(String id, ContentValues values){
        return getWritableDatabase().update(MovieEntry.TABLE_NAME, values,
                MovieEntry._ID + " = ?",
                new String[]{id});
    }

    public Cursor getMovies(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqliteQueryBuilder = new SQLiteQueryBuilder();
        sqliteQueryBuilder.setTables(MovieEntry.TABLE_NAME);

        if(id != null) {
            sqliteQueryBuilder.appendWhere("_id" + " = " + id);
        }

        if(sortOrder == null || sortOrder == "") {
            sortOrder = MovieEntry._ID;
        }
        Cursor cursor = sqliteQueryBuilder.query(getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return cursor;
    }

    @NonNull
    public Movie getMovieByCursor(Cursor cursor) {
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
