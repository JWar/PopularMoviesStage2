package com.portfolio.udacity.android.popularmoviesstage2.ui.list.holders;

import android.view.View;
import android.widget.TextView;

import com.portfolio.udacity.android.popularmoviesstage2.R;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Trailer;

/**
 * Created by JonGaming on 18/02/2018.
 *
 */

public class TrailerHolder extends AbstractHolder {
    private View mView;
    private TextView mTrailerTV;
    public TrailerHolder(View aView) {
        super(aView);
        mView=aView;
        mTrailerTV = mView.findViewById(R.id.list_item_trailer_tv);
    }
    @Override
    public void setListener(View.OnClickListener aListener) {
        mTrailerTV.setOnClickListener(aListener);
    }
    //Returns key for trailer to load on click.
    public String bindData(Trailer aTrailer) {
        return aTrailer.mKey;
    }
}
