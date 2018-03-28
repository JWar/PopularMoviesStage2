package com.portfolio.udacity.android.popularmoviesstage2.ui;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.portfolio.udacity.android.popularmoviesstage2.NetworkUtils;
import com.portfolio.udacity.android.popularmoviesstage2.R;
import com.portfolio.udacity.android.popularmoviesstage2.Utils;
import com.portfolio.udacity.android.popularmoviesstage2.data.database.MoviesContract;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Movie;
import com.portfolio.udacity.android.popularmoviesstage2.data.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String LOG_TAG = "PopularMovies";

    private MovieRepository mMovieRepository;
    private GridView mGridView;
    private static final String GRID_VIEW_STATE = "gridViewState";
    private Parcelable mGridViewState;
    private GetMoviesAsync mGetMoviesAsync;
    private static final String SORT_TYPE = "sortType";
    private String mSortType;

    private LinearLayout mTelRowsLL;
    private int mTelPositionId;


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
//        mGridView = findViewById(R.id.main_activity_gv);
        mTelPositionId=1;
        mTelRowsLL = findViewById(R.id.main_activity_row_ll);
//        mGetMoviesAsync = new GetMoviesAsync();
//        mGetMoviesAsync.execute(mSortType);
    }
    public void onAddNewRow(View aView) {
        onAddTelRow("Dummy tel",-1);
    }
    private void onAddTelRow(@Nullable String aTelNum, int aTelType) {
        try {
            mTelPositionId += 1;
            int buttonsId = View.generateViewId();
            int spinnerId = View.generateViewId();
            int etId = View.generateViewId();
            RelativeLayout newRL = new RelativeLayout(this);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(Utils.pixelsToDp(this,2f),
                    Utils.pixelsToDp(this,2f),
                    Utils.pixelsToDp(this,2f),
                    Utils.pixelsToDp(this,12f));
            newRL.setLayoutParams(lp);

            //ImageButton: first because building from End
            ImageButton delIB = new ImageButton(this);
            delIB.setId(buttonsId);
            delIB.setTag(mTelPositionId);
//            delIB.setBackground(getResources().getDrawable(R.drawable.white_ripple,null));
            delIB.setImageResource(android.R.drawable.ic_delete);

            if (mTelRowsLL.getChildCount()==0) {//This is the first tel!
                //Issue new Id to delIB as going to need ET to align with linear layout.
                delIB.setId(View.generateViewId());
                delIB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View aView) {
//                        onDelTel1();
                    }
                });

                ImageButton addIB = new ImageButton(this);
                addIB.setId(View.generateViewId());
                addIB.setTag(mTelPositionId);
//                addIB.setBackground(getResources().getDrawable(R.drawable.white_ripple,null));
                addIB.setImageResource(android.R.drawable.ic_input_add);
                addIB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View aView) {
                        onAddTelRow(null,-1);
                    }
                });
                //Add rules/params for layout
                lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,0,0,0);
                addIB.setLayoutParams(lp);
                delIB.setLayoutParams(lp);
                LinearLayout buttonsLL = new LinearLayout(this);
                buttonsLL.setId(buttonsId);
                RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                llp.setMargins(Utils.pixelsToDp(this,4f),
                        0,0,0);
                llp.addRule(RelativeLayout.ALIGN_PARENT_END);
                buttonsLL.setLayoutParams(llp);
                buttonsLL.setOrientation(LinearLayout.VERTICAL);
                buttonsLL.addView(addIB);
                buttonsLL.addView(delIB);
                newRL.addView(buttonsLL,llp);
            } else {
                delIB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View aView) {
//                        onRemoveTelRow((int) aView.getTag());
                    }
                });
                //Add rules/params for layout
                lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(Utils.pixelsToDp(this,4f),
                        0,0,0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_END);
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                newRL.addView(delIB, lp);
            }

            //Spinner
            Spinner spinner = new Spinner(this);
            spinner.setId(spinnerId);
            spinner.setTag(mTelPositionId);
            spinner.setPadding(0,0,0,0);
            spinner.setPaddingRelative(0,0,0,0);

            setSpinner(spinner, aTelType);
//            //Add rules/params for layout
            lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    (int)getResources().getDimension(R.dimen.spinner_width),//This forces spinner width
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_START);
            lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            lp.setMargins(Utils.pixelsToDp(this,12f),
                    Utils.pixelsToDp(this,8f),
                    0,0);
            spinner.setLayoutParams(lp);

            newRL.addView(spinner);

            //ET
            EditText telET = new EditText(this);
            telET.setId(etId);
            telET.setTag(mTelPositionId);
//            telET.setBackground(getResources().getDrawable(R.drawable.rounded_corner,null));
            telET.setInputType(InputType.TYPE_CLASS_PHONE);
            telET.setImeOptions(EditorInfo.IME_ACTION_DONE);
            telET.setMaxLines(1);

            telET.setMinHeight(Utils.pixelsToDp(this,54f));
            telET.setPadding(Utils.pixelsToDp(this,2f),
                    Utils.pixelsToDp(this,2f),
                    0,
                    Utils.pixelsToDp(this,2f));
            telET.setHorizontallyScrolling(false);
            if (aTelNum != null) {
                //Add tel to TelET.
                telET.setText(aTelNum);
            }
            //This sets the ET to between the IB and Spinner
            lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

//            lp.setMarginStart(-108);//Seems the ET isnt shifted enough to the left. match parent failing?
            lp.addRule(RelativeLayout.START_OF, buttonsId);
            lp.addRule(RelativeLayout.END_OF, spinnerId);
            lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            lp.setMargins(Utils.pixelsToDp(this,4f),
                    Utils.pixelsToDp(this,16f),
                    0,
                    Utils.pixelsToDp(this,20f));
            newRL.addView(telET, lp);
            mTelRowsLL.addView(newRL);

            //If got to here then we can happily add them all to list...
//            mTelRows.add(Pair.create(mTelPositionId, newRL));//Add to RL list
//            mSpinners.add(Pair.create(mTelPositionId, spinner));//Add to spinners list
//            mTelETs.add(Pair.create(mTelPositionId, telET));//Add to TelET list
        } catch (Exception e) {
            //Mostly for debugging purposes..
            Utils.logDebug("ClientDetailFragment.onAddTelRow: " + e.getLocalizedMessage());
        }
    }

    private void setSpinner(Spinner aSpinner, int aTelType) {
        String[] telTypes;
        switch (aTelType) {
            case 0:
                telTypes = new String[]{
                        getString(R.string.client_detail_tel_type_mobile),
                        getString(R.string.client_detail_tel_type_work),
                        getString(R.string.client_detail_tel_type_home)
                };
                break;
            case 1:
                telTypes = new String[]{
                        getString(R.string.client_detail_tel_type_work),
                        getString(R.string.client_detail_tel_type_mobile),
                        getString(R.string.client_detail_tel_type_home)
                };
                break;
            case 2:
                telTypes = new String[]{
                        getString(R.string.client_detail_tel_type_home),
                        getString(R.string.client_detail_tel_type_mobile),
                        getString(R.string.client_detail_tel_type_work)
                };
                break;
            default:
                telTypes = new String[]{
                        getString(R.string.client_detail_tel_type_mobile),
                        getString(R.string.client_detail_tel_type_work),
                        getString(R.string.client_detail_tel_type_home)
                };
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                R.layout.simple_spinner_item,
                telTypes);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the mSpinner
        aSpinner.setAdapter(adapter);
        aSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> aAdapterView, View aView, int aI, long aL) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> aAdapterView) {

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
    protected void onDestroy() {
        super.onDestroy();
        if (mGetMoviesAsync != null) {
            mGetMoviesAsync.cancel(true);
            mGetMoviesAsync = null;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mGridViewState =savedInstanceState.getParcelable(GRID_VIEW_STATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_TYPE, mSortType);
        outState.putParcelable(GRID_VIEW_STATE, mGridView.onSaveInstanceState());
    }

    //Would never usually do it this way but its quick and dirty and should work! Should be static etc...
    public class GetMoviesAsync extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... aStrings) {
            try {
                if (!isCancelled()) {
                    List<Movie> movies = NetworkUtils.getMovies(aStrings[0]);
                    //Sigh no better way of checking for favourited Movies than using mSortType?
                    if (movies == null) {
                        return null;
                    }
                    if (mSortType.equals(NetworkUtils.FAVOURITE)) {
                        Cursor cursor = getContentResolver().query(MoviesContract.FavouritesEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
                        if (cursor != null) {
                            List<Movie> favouritesList = new ArrayList<>();
                            if (cursor.getCount() > 0) {
                                while (cursor.moveToNext()) {
                                    int movieId = cursor.getInt(cursor.getColumnIndex(MoviesContract.FavouritesEntry.COLUMN_MOVIE_ID));
                                    for (Movie movie : movies) {
                                        if (movieId == movie.mId) {
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
                }
                return null;
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
                        if (mGridViewState!=null) {
                            mGridView.onRestoreInstanceState(mGridViewState);
                        }
                    }
                }
            }
        }
    }
}
