package com.portfolio.udacity.android.popularmoviesstage2.ui.list.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JonGaming on 18/02/2018.
 *
 */

public abstract class AbstractHolder extends RecyclerView.ViewHolder {
    public AbstractHolder(View itemView) {
        super(itemView);
    }
    public abstract void setListener(View.OnClickListener aListener);
}
