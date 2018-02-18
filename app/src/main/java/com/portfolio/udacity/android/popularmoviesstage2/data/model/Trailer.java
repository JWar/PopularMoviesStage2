package com.portfolio.udacity.android.popularmoviesstage2.data.model;

/**
 * Created by JonGaming on 18/02/2018.
 *
 */

public class Trailer {
    //JSON keys
    public static final String TRAILER_ID = "id";
    public static final String KEY = "key";
    public static final String SITE = "site";

    public int mMovieId;
    public String mTrailerId;
    public String mKey;
    public String mSite;

    public Trailer(int aMovieId, String aTrailerId, String aKey, String aSite) {
        mMovieId=aMovieId;
        mTrailerId=aTrailerId;
        mKey=aKey;
        mSite=aSite;
    }
}
