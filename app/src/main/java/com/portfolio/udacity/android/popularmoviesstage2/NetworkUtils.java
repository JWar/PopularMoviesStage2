package com.portfolio.udacity.android.popularmoviesstage2;

import android.net.Uri;
import android.util.Log;

import com.portfolio.udacity.android.popularmoviesstage2.data.model.Movie;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Review;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Trailer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.portfolio.udacity.android.popularmoviesstage2.ui.MainActivity.LOG_TAG;

/**
 * Created by JonGaming on 16/02/2018.
 * Handles network stuff...
 */

public class NetworkUtils {

    private static final String DATA_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_QUERY = "api_key";
    private static final String JSON_KEY_RESULTS = "results";

    private static final String VIDEOS = "videos?";
    private static final String REVIEWS = "reviews?";
    public static final String TOP_RATED = "top_rated?";
    public static final String POPULAR = "popular?";
    public static final String FAVOURITE = "favourite";

    //Default search by particular size of w185. Of course can put this as an append if needs be...
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    private static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_TOKEN;

    public static synchronized List<Movie> getMovies(String aOrderBy) throws Exception {
        switch (aOrderBy) {
            case NetworkUtils.FAVOURITE:
                List<Movie> popularList = getMoviesOrderBy(POPULAR);
                List<Movie> ratedList = getMoviesOrderBy(TOP_RATED);
                popularList.removeAll(ratedList);//Remove duplicates. Just throwing any exceptions for now..
                popularList.addAll(ratedList);
                return popularList;
            case NetworkUtils.POPULAR:
                return getMoviesOrderBy(aOrderBy);
            case NetworkUtils.TOP_RATED:
                return getMoviesOrderBy(aOrderBy);
            default:
                return null;
        }
    }

    public static synchronized List<Movie> getMoviesOrderBy(String aOrderBy) {
        try {
            ArrayList<Movie> toReturn = new ArrayList<>();
            String result = getResponseFromHttpUrl(buildUrlWithSortOrder(aOrderBy));
            JSONArray results = new JSONObject(result).getJSONArray(JSON_KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObj = results.getJSONObject(i);
                Movie movie = new Movie(
                        movieObj.optInt(Movie.ID),
                        movieObj.optString(Movie.TITLE),
                        movieObj.optString(Movie.RELEASE_DATE),
                        movieObj.optString(Movie.POSTER_PATH),
                        movieObj.optString(Movie.VOTE_AVERAGE),
                        movieObj.optString(Movie.OVERVIEW),
                        false
                );
                toReturn.add(movie);
            }
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LOG_TAG,"Error in NetworkUtils.getMoviesOrderBy: "+e.getLocalizedMessage());
            return null;
        }
    }
    private static URL buildUrlWithSortOrder(String aOrderBy) {
        String urlToParse = DATA_URL + aOrderBy;
        Uri uri = Uri.parse(urlToParse).buildUpon()
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .build();
        try {
            URL url = new URL(uri.toString());
            Log.i(LOG_TAG, "URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static synchronized Movie getTrailersAndReviews(int aId) {
        try {
            String trailerResult = getResponseFromHttpUrl(buildTrailerUrl(aId));
            String reviewResult = getResponseFromHttpUrl(buildReviewUrl(aId));

            JSONArray resultsTrailer = new JSONObject(trailerResult).getJSONArray(JSON_KEY_RESULTS);
            JSONArray resultsReview = new JSONObject(reviewResult).getJSONArray(JSON_KEY_RESULTS);
            Movie movie = new Movie();
            for (int i = 0; i < resultsTrailer.length(); i++) {
                JSONObject trailerObj = resultsTrailer.getJSONObject(i);
                Trailer trailer = new Trailer(aId,
                        trailerObj.optString(Trailer.TRAILER_ID),
                        trailerObj.optString(Trailer.KEY),
                        trailerObj.optString(Trailer.SITE)
                );
                movie.mTrailers.add(trailer);
            }
            for (int i = 0; i < resultsReview.length(); i++) {
                JSONObject reviewObj = resultsReview.getJSONObject(i);
                Review review = new Review(aId,
                        reviewObj.optString(Review.REVIEW_ID),
                        reviewObj.optString(Review.AUTHOR),
                        reviewObj.optString(Review.CONTENT),
                        reviewObj.optString(Review.URL)
                );
                movie.mReviews.add(review);
            }
            return movie;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(LOG_TAG,"Error in NetworkUtils.getTrailersAndReviews: "+e.getLocalizedMessage());
            return null;
        }
    }
    private static URL buildReviewUrl(int aId) {
        String urlToParse = DATA_URL + aId + REVIEWS;
        Uri uri = Uri.parse(urlToParse).buildUpon()
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .build();
        try {
            URL url = new URL(uri.toString());
            Log.i(LOG_TAG, "URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static URL buildTrailerUrl(int aId) {
        String urlToParse = DATA_URL + aId + VIDEOS;
        Uri uri = Uri.parse(urlToParse).buildUpon()
                .appendQueryParameter(API_KEY_QUERY, API_KEY)
                .build();
        try {
            URL url = new URL(uri.toString());
            Log.i(LOG_TAG, "URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
//            Log.i(LOG_TAG, "HttpUrl response: " + response);
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
