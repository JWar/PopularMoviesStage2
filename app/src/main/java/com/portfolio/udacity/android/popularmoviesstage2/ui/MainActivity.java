package com.portfolio.udacity.android.popularmoviesstage2.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.portfolio.udacity.android.popularmoviesstage2.NetworkUtils;
import com.portfolio.udacity.android.popularmoviesstage2.R;
import com.portfolio.udacity.android.popularmoviesstage2.data.database.MoviesContract;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Movie;
import com.portfolio.udacity.android.popularmoviesstage2.data.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "PopularMovies";

    private MovieRepository mMovieRepository;
    private GridView mGridView;
    private GetMoviesAsync mGetMoviesAsync;
    private static final String SORT_TYPE = "sortType";
    private String mSortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mSortType = savedInstanceState.getString(SORT_TYPE);
        } else {
            mSortType = NetworkUtils.POPULAR;
        }
        mMovieRepository = MovieRepository.getInstance();
        mGridView = findViewById(R.id.main_activity_gv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_order_by_most_popular:
                //Make new call to search by popular
                mSortType = NetworkUtils.POPULAR;
                mGetMoviesAsync = new GetMoviesAsync();
                mGetMoviesAsync.execute(mSortType);
                return true;
            case R.id.action_order_by_highest_rated:
                //Make new call to search by rated
                mSortType = NetworkUtils.TOP_RATED;
                mGetMoviesAsync = new GetMoviesAsync();
                mGetMoviesAsync.execute(mSortType);
                return true;
            case R.id.action_order_by_favourite:
                //Make new call to search by both sort orders
                mSortType = NetworkUtils.FAVOURITE;
                mGetMoviesAsync = new GetMoviesAsync();
                mGetMoviesAsync.execute(mSortType);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGetMoviesAsync = new GetMoviesAsync();
        mGetMoviesAsync.execute(mSortType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGetMoviesAsync != null) {
            mGetMoviesAsync.cancel(true);
            mGetMoviesAsync = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_TYPE, mSortType);
    }

    //Would never usually do it this way but its quick and dirty and should work! Should be static etc...
    public class GetMoviesAsync extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... aStrings) {
            try {
                List<Movie> movies = NetworkUtils.getMovies(aStrings[0]);
                //Sigh no better way of checking for favourited Movies than using mSortType?
                if (movies==null) {return null;}
                if (mSortType.equals(NetworkUtils.FAVOURITE)) {
                    Cursor cursor = getContentResolver().query(MoviesContract.FavouritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                    if (cursor != null) {
                        List<Movie> favouritesList = new ArrayList<>();
                        if (cursor.getCount()>0) {
                            while (cursor.moveToNext()) {
                                int movieId = cursor.getInt(cursor.getColumnIndex(MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID));
                                for (Movie movie: movies) {
                                    if (movieId==movie.mId) {
                                        favouritesList.add(movie);
                                    }
                                }
                            }
                        }
                        cursor.close();
                        return favouritesList;
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.error_message_favourites), Toast.LENGTH_SHORT).show();
                    }
                }
                return movies;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> aMovies) {
            super.onPostExecute(aMovies);
            if (!isCancelled()) {
                if (aMovies == null) {
                    Toast.makeText(MainActivity.this, getString(R.string.error_message_data_load), Toast.LENGTH_SHORT).show();
                } else {
                    if (mMovieRepository != null && mGridView != null) {
                        mMovieRepository.setMovies(aMovies);
                        //Ok to use getBaseContext? MainActivity.this?
                        GridAdapter gridAdapter = new GridAdapter(MainActivity.this,
                                mMovieRepository.getMovies());
                        mGridView.setAdapter(gridAdapter);
                    }
                }
            }
        }
    }
}
