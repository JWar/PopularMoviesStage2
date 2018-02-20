package com.portfolio.udacity.android.popularmoviesstage2.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JonGaming on 20/02/2018.
 * Room can avoid all this...
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase aSQLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE =
                //Elected to ignore constraint conflicts as if the movie id is already there then thats ok!
                "CREATE TABLE " + MoviesContract.FavouritesEntry.TABLE_NAME + " (" +
                        MoviesContract.FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        MoviesContract.FavouritesEntry.COLUMN_MOVIE_TITLE + " VARCHAR NOT NULL, " +
                        " UNIQUE (" + MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT IGNORE);";
        aSQLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase aSQLiteDatabase, int aI, int aI1) {
        aSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavouritesEntry.TABLE_NAME);
        onCreate(aSQLiteDatabase);
    }
}
