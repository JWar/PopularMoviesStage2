package com.portfolio.udacity.android.popularmoviesstage2.ui.list;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by JonGaming on 18/02/2018.
 *
 */

public interface ListHandlerCallback {
    void onListClick(int aPosition, String aId);
    void onListTouch(View aView, MotionEvent aMotionEvent);
}

