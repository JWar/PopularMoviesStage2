package com.portfolio.udacity.android.popularmoviesstage2.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.portfolio.udacity.android.popularmoviesstage2.NetworkUtils;
import com.portfolio.udacity.android.popularmoviesstage2.R;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Movie;
import com.portfolio.udacity.android.popularmoviesstage2.data.repository.MovieRepository;
import com.portfolio.udacity.android.popularmoviesstage2.ui.list.ListHandlerCallback;
import com.portfolio.udacity.android.popularmoviesstage2.ui.list.RecyclerViewAdapter;
import com.squareup.picasso.Picasso;

/**
 * For the moment I am simply getting the data from the MovieRepo and using the
 * arguments position to get the correct movie in the list. Naturally this isnt how you'd properly design
 * it. But that is stage two!
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    private static final String POS = "pos";
    private int mPosition;

    private static final String MOVIE_ID = "movieId";

    private RecyclerView mTrailersRV;
    private RecyclerView mReviewsRV;

    public static void start(Context aContext,int aPosition) {
        Intent intent = new Intent(aContext,DetailActivity.class);
        intent.putExtra(POS,aPosition);
        aContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState!=null) {
            mPosition = savedInstanceState.getInt(POS,-1);
        } else {
            if (getIntent().hasExtra(POS)) {
                mPosition = getIntent().getIntExtra(POS,-1);
            }
        }
        mTrailersRV = findViewById(R.id.activity_detail_trailers_rv);
        mTrailersRV.setAdapter(new RecyclerViewAdapter(new ListHandlerCallback() {
            @Override
            public void onListClick(int aPosition, String aKey) {
                watchYoutubeVideo(aKey);
            }

            @Override
            public void onListTouch(View aView, MotionEvent aMotionEvent) {

            }
        }, R.layout.list_item_trailer));

        mReviewsRV = findViewById(R.id.activity_detail_reviews_rv);
        mReviewsRV.setAdapter(new RecyclerViewAdapter(new ListHandlerCallback() {
            @Override
            public void onListClick(int aPosition, String aId) {
                //Do nothing on review list click for the moment
            }

            @Override
            public void onListTouch(View aView, MotionEvent aMotionEvent) {

            }
        }, R.layout.list_item_review));
        setViews();
    }
    private void setViews() {
        MovieRepository movieRepository = MovieRepository.getInstance();
        if (movieRepository.getMovies()!=null) {
            Movie movie = movieRepository.getMovies().get(mPosition);
            TextView title = findViewById(R.id.activity_detail_title_tv);
            title.setText(movie.mTitle);
            TextView releaseDate = findViewById(R.id.activity_detail_release_date_tv);
            releaseDate.setText(movie.mReleaseDate);
            TextView voteAverage = findViewById(R.id.activity_detail_vote_average_tv);
            voteAverage.setText(movie.mVoteAverage);
            TextView synopsis = findViewById(R.id.activity_detail_synopsis_tv);
            synopsis.setText(movie.mSynopsis);
            ImageView poster = findViewById(R.id.activity_detail_poster_iv);
            //Load image last.
            String url = NetworkUtils.IMAGE_URL;
            url += movie.mPoster;
            Picasso.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_image_black_48px)
                    .error(R.drawable.ic_error_black_48px)
                    .resize(getResources().getDimensionPixelSize(R.dimen.movie_detail_poster_size),
                            getResources().getDimensionPixelSize(R.dimen.movie_detail_poster_size))
                    .into(poster);
            Bundle args = new Bundle();
            args.putInt(MOVIE_ID,movie.mId);
            getSupportLoaderManager().initLoader(1,args,this);
        } else {
            Toast.makeText(this, getString(R.string.error_message_data_load), Toast.LENGTH_SHORT).show();
        }
    }
    public void watchYoutubeVideo(String aId){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + aId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + aId));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new MovieLoader(this,args);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        if (data!=null&&mReviewsRV!=null&&mTrailersRV!=null) {
            //TODO:180218_Should I add MovieRepository update of Movie?? Its just those three lines...
            Movie movie = MovieRepository.getInstance().getMovies().get(mPosition);
            movie.mReviews=data.mReviews;
            movie.mTrailers=data.mTrailers;
            ((RecyclerViewAdapter)mReviewsRV.getAdapter()).swapReviewList(data.mReviews);
            ((RecyclerViewAdapter)mTrailersRV.getAdapter()).swapTrailerList(data.mTrailers);
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POS,mPosition);
    }

    private static class MovieLoader extends android.support.v4.content.AsyncTaskLoader<Movie> {
        Movie mMovie;
        Bundle mArgs;
        int mId=-1;
        MovieLoader(Context aContext, Bundle aArgs) {
            super(aContext);
            mArgs=aArgs;
        }

        @Override
        protected void onStartLoading() {
            if (mMovie!=null&&mId==mArgs.getInt(MOVIE_ID)) {
                deliverResult(mMovie);
            } else {
                forceLoad();
            }
        }

        @Override
        public Movie loadInBackground() {
            return NetworkUtils.getTrailersAndReviews(mArgs.getInt(MOVIE_ID));
        }

        @Override
        public void deliverResult(Movie data) {
            mId=mArgs.getInt(MOVIE_ID);
            mMovie=data;
            super.deliverResult(data);
        }
    }
}
