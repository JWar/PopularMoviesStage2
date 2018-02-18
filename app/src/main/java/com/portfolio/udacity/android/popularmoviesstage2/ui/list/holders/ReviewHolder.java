package com.portfolio.udacity.android.popularmoviesstage2.ui.list.holders;

import android.view.View;
import android.widget.TextView;

import com.portfolio.udacity.android.popularmoviesstage2.R;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Review;

/**
 * Created by JonGaming on 18/02/2018.
 *
 */

public class ReviewHolder extends AbstractHolder {
    private View mView;
    private TextView mAuthorTV;
    private TextView mContentTV;
    public ReviewHolder(View aView) {
        super(aView);
        mView=aView;
        mAuthorTV = aView.findViewById(R.id.list_item_author_tv);
        mContentTV = aView.findViewById(R.id.list_item_content_tv);
    }
    @Override
    public void setListener(View.OnClickListener aListener) {
        mView.setOnClickListener(aListener);
    }
    //Returns review Id... unecessary at moment...
    public String bindData(Review aReview) {
        mAuthorTV.setText(aReview.mAuthor);
        mContentTV.setText(aReview.mContent);
        return aReview.mReviewId;
    }
}
