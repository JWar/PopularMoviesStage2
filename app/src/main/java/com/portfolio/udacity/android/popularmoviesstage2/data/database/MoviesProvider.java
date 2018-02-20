package com.portfolio.udacity.android.popularmoviesstage2.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by JonGaming on 20/02/2018.
 * Basic Content Provider
 */

public class MoviesProvider extends ContentProvider {

    public static final int CODE_FAVOURITES = 100;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mMoviesDbHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MoviesContract.PATH_FAVOURITES, CODE_FAVOURITES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMoviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri aUri, @Nullable String[] aProjection, @Nullable String aSelection,
                        @Nullable String[] aSelectionArgs, @Nullable String aSortOrder) {
        Cursor cursor;
        //Mostly redundant since theres only one but meh...
        switch (sUriMatcher.match(aUri)) {
            case CODE_FAVOURITES: {
                cursor = mMoviesDbHelper.getReadableDatabase().query(
                        MoviesContract.FavouritesEntry.TABLE_NAME,
                        aProjection,
                        aSelection,
                        aSelectionArgs,
                        null,
                        null,
                        aSortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + aUri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), aUri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri aUri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri aUri, @Nullable ContentValues aContentValues) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        long id = db.insertOrThrow(MoviesContract.FavouritesEntry.TABLE_NAME,
                null,
                aContentValues);
        getContext().getContentResolver().notifyChange(aUri, null);
        return ContentUris.withAppendedId(aUri, id);
    }

    @Override
    public int delete(@NonNull Uri aUri, @Nullable String aSelection, @Nullable String[] aSelectionArgs) {
        int numRowsDeleted;
        if (aSelection == null) {
            aSelection = "1";
        }
        numRowsDeleted = mMoviesDbHelper.getWritableDatabase().delete(
                MoviesContract.FavouritesEntry.TABLE_NAME,
                aSelection,
                aSelectionArgs);
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(aUri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri aUri, @Nullable ContentValues aContentValues,
                      @Nullable String aSelection, @Nullable String[] aSelectionArgs) {
        int numRowsUpdated;
        numRowsUpdated = mMoviesDbHelper.getWritableDatabase().update(
                MoviesContract.FavouritesEntry.TABLE_NAME,
                aContentValues,
                aSelection,
                aSelectionArgs);
        if (numRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(aUri, null);
        }
        return numRowsUpdated;
    }
}
