package com.portfolio.udacity.android.popularmoviesstage2.data.repository;

import com.portfolio.udacity.android.popularmoviesstage2.data.model.Movie;

import java.util.List;

/**
 * Created by JonGaming on 16/02/2018.
 * For ensuring Main and Detail Activity can access same data set.
 * Not a strict 'repository' but really just used to ensure activity's can share data...
 */

public class MovieRepository {
    private static MovieRepository sInstance;
    private List<Movie> mMovies;

    public static MovieRepository getInstance() {
        if (sInstance==null) {
            sInstance = new MovieRepository();
        }
        return sInstance;
    }
    private MovieRepository() {}
    public void setMovies(List<Movie> aMovies) {
        mMovies = aMovies;
    }
    public List<Movie> getMovies() {
        return mMovies;
    }
    public void destroyInstance() {
        sInstance=null;
    }
}
