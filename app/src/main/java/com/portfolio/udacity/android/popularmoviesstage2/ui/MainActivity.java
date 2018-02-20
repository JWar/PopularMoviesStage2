package com.portfolio.udacity.android.popularmoviesstage2.ui;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.portfolio.udacity.android.popularmoviesstage2.NetworkUtils;
import com.portfolio.udacity.android.popularmoviesstage2.R;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Movie;
import com.portfolio.udacity.android.popularmoviesstage2.data.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "PopularMovies";
    public static final String FAVOURITES = "favs";

    private MovieRepository mMovieRepository;
    private GridView mGridView;
    private GetMoviesAsync mGetMoviesAsync;
    private static final String SORT_TYPE = "sortType";
    private String mSortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState!=null) {
            mSortType=savedInstanceState.getString(SORT_TYPE);
        } else {
            mSortType= NetworkUtils.POPULAR;
        }
        mMovieRepository = MovieRepository.getInstance();
        mGridView = findViewById(R.id.main_activity_gv);
        //180217_Does it matter if onClick is here? Or in adapter?
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> aAdapterView, View aView, int aI, long aL) {
//                DetailActivity.start(aView.getContext(),(int)aView.getTag());
//            }
//        });
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
                mSortType=NetworkUtils.POPULAR;
                mGetMoviesAsync = new GetMoviesAsync();
                mGetMoviesAsync.execute(mSortType);
                return true;
            case R.id.action_order_by_highest_rated:
                //Make new call to search by rated
                mSortType=NetworkUtils.TOP_RATED;
                mGetMoviesAsync = new GetMoviesAsync();
                mGetMoviesAsync.execute(mSortType);
                return true;
            case R.id.action_order_by_favourite:
                //Make new call to search by both sort orders
                mSortType=NetworkUtils.FAVOURITE;
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
        if (mGetMoviesAsync!=null) {
            mGetMoviesAsync.cancel(true);
            mGetMoviesAsync=null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_TYPE,mSortType);
    }

    //Would never usually do it this way but its quick and dirty and should work! Should be static etc...
    public class GetMoviesAsync extends AsyncTask<String,Void,List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... aStrings) {
            try {
                return NetworkUtils.getMovies(aStrings[0]);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> aMovies) {
            super.onPostExecute(aMovies);
            if (aMovies==null) {
                Toast.makeText(MainActivity.this, getString(R.string.error_message_data_load), Toast.LENGTH_SHORT).show();
            } else {
                if (mMovieRepository != null && mGridView != null) {
                    //Sigh no better way of checking for favourited Movies than using mSortType?
                    if (mSortType.equals(NetworkUtils.FAVOURITE)) {
                        //Need to check favourites and create a list containing only them.
                        //Sigh doing all this in onPostExecute? This is why we never use AsyncTask...
                        //Get csv string from SharedPreferences. Run through it getting movie using movie id.
                        SharedPreferences sharedPreferences = getPreferences(0);
                        String csvFavs= sharedPreferences.getString(FAVOURITES,null);
                        List<Movie> favouritesList = new ArrayList<>();
                        if (csvFavs!=null) {
                            String[] split = csvFavs.split(",");
                            //Sigh gotta love the double loop
                            for (String s: split) {
                                for (Movie movie: aMovies) {
                                    if (movie.mId==Integer.parseInt(s)) {
                                        favouritesList.add(movie);
                                    }
                                }
                            }
                        }
                        aMovies=favouritesList;//Switch aMovies to favouritesList. If theres no favourites then empty list
                    }
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
