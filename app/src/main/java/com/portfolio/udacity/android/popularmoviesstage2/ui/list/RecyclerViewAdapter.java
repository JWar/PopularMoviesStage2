package com.portfolio.udacity.android.popularmoviesstage2.ui.list;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.portfolio.udacity.android.popularmoviesstage2.R;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Review;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Trailer;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.entity;
import com.portfolio.udacity.android.popularmoviesstage2.ui.list.holders.AbstractHolder;
import com.portfolio.udacity.android.popularmoviesstage2.ui.list.holders.ReviewHolder;
import com.portfolio.udacity.android.popularmoviesstage2.ui.list.holders.TrailerHolder;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_ID;
import static com.portfolio.udacity.android.popularmoviesstage2.ui.MainActivity.LOG_TAG;

/**
 * Created by JonGaming on 18/02/2018.
 *
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<AbstractHolder> {
    private final ListHandlerCallback mListener;
    //Specifies what sort of list wanted (client, user, activity etc...). Will use R.layout.fragment_list_item_...
    private int listItemType;

    private List<Trailer> mTrailerList;
    private List<Review> mReviewList;

    public RecyclerViewAdapter(ListHandlerCallback listener, int type) {
        mListener = listener;
        listItemType = type;
        setHasStableIds(false);
    }

    //This is where the holder type is specified. Must check param list type and change accordingly.
    //User Type to User Holder etc...
    @Override
    public AbstractHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(listItemType, parent, false);
        //Control which viewholder to call based upon list (item) type.
        switch(listItemType) {
            case R.layout.list_item_review:
                return new ReviewHolder(view);
            case R.layout.list_item_trailer:
                return new TrailerHolder(view);
            default:
                Log.i(LOG_TAG,"Error in ListRecyclerAdapter.onCreateViewHolder: listItemType unrecognised.");
                return null;
        }
    }
    //This is where the data is put into the holder (in bindData).
    @Override
    public void onBindViewHolder(final AbstractHolder holder, final int position) {
        try {
            String dId=null;
            if (holder instanceof TrailerHolder) {
                TrailerHolder trailerHolder = (TrailerHolder) holder;
                dId = trailerHolder.bindData(mTrailerList.get(position));
            } else if (holder instanceof ReviewHolder) {
                ReviewHolder reviewHolder = (ReviewHolder) holder;
                dId = reviewHolder.bindData(mReviewList.get(position));
            }
            final String dataId = dId;
            holder.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//This sets up what on click will be. Draws from parent Activity.
                    try {
                        if (null != mListener) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an item has been selected.
                            mListener.onListClick(holder.getAdapterPosition(), dataId);
                        }
                    } catch (Exception e) {
                        Log.i(LOG_TAG,"Error in onListListener: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.i(LOG_TAG,"Error in ListRecyclerAdapter.onBindViewHolder: " + e.getMessage());
        }
    }
    @Override
    public long getItemId(int aPos) {
        //Not needed as no stable ids?
        return NO_ID;
    }
    @Override
    public int getItemCount() {
        try {
            if (mTrailerList!= null) {
                return mTrailerList.size();
            } if (mReviewList!= null) {
                return mReviewList.size();
            } else {
                return -1;
            }
        } catch (Exception e) {
            Log.i(LOG_TAG,"Error in ListRecyclerAdapter.getItemCount: " + e.getMessage());
            return -1;
        }
    }
    public void swapTrailerList(List<Trailer> aList) {
        mTrailerList = aList;
        notifyDataSetChanged();
    }
    public void swapReviewList(List<Review> aList) {
        mReviewList = aList;
        notifyDataSetChanged();
    }
}


