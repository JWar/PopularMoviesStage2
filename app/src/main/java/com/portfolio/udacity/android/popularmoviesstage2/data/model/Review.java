package com.portfolio.udacity.android.popularmoviesstage2.data.model;

/**
 * Created by JonGaming on 18/02/2018.
 *
 */

public class Review extends entity {
    //JSON keys
    public static final String REVIEW_ID = "id";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String URL = "url";

    public int mMovieId;
    public String mReviewId;
    public String mAuthor;
    public String mContent;
    public String mUrl;

    public Review(int aMovieId, String aReviewId, String aAuthor, String aContent, String aUrl) {
        mMovieId=aMovieId;
        mReviewId=aReviewId;
        mAuthor=aAuthor;
        mContent=aContent;
        mUrl=aUrl;
    }
}
