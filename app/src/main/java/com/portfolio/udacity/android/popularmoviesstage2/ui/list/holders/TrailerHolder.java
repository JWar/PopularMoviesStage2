package com.portfolio.udacity.android.popularmoviesstage2.ui.list.holders;

import android.view.View;

import com.portfolio.udacity.android.popularmoviesstage2.data.model.Trailer;

/**
 * Created by JonGaming on 18/02/2018.
 *
 */

public class TrailerHolder extends AbstractHolder {
    private View mView;
    public TrailerHolder(View aView) {
        super(aView);
        mView=aView;
    }
    @Override
    public void setListener(View.OnClickListener aListener) {
        mView.setOnClickListener(aListener);
    }
    //Returns key for trailer to load on click.
    public String bindData(Trailer aTrailer) {
        return aTrailer.mKey;
    }
}
