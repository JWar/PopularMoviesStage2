package com.portfolio.udacity.android.popularmoviesstage2.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JonGaming on 20/02/2018.
 * Very basic. Only need to store the id of the movie favourited. Of course could extend this
 * to have the movies stored as a backup. Tbh thats the best way of doing things, remote to local, store
 * in local then theres always a copy. Update/sync with remote every so often to keep the local data fresh.
 * Really depends on the apps needs...
 */

public class MoviesContract {

   public static final String CONTENT_AUTHORITY = "com.portfolio.udacity.android.popularmoviesstage2";
   public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

   //Add more depending on tables...
   public static final String PATH_FAVOURITES = "favourites";

   public static final class FavouritesEntry implements BaseColumns {
       public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
               .appendPath(PATH_FAVOURITES)
               .build();

       public static final String TABLE_NAME = "favourites";
       public static final String COLUMN_MOVIE_ID = "movieid";
       public static final String COLUMN_MOVIE_TITLE = "movietitle";
   }

}
