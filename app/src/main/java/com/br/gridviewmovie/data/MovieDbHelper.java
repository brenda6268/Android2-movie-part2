package com.br.gridviewmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private  static  final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "Movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.MOVIE_ID + " TEXT UNIQUE NOT NULL," +
                MovieContract.MovieEntry.MOVIE_TITLE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_POSTER + " TEXT NOT NULL," +
                MovieContract.MovieEntry.MOVIE_VOTE_AVERAGE + " TEXT NOT NULL," +
                "UNIQUE (" + MovieContract.MovieEntry.MOVIE_ID +") ON CONFLICT IGNORE"+
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}

