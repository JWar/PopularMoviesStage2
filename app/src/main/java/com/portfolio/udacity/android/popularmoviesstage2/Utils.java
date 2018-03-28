package com.portfolio.udacity.android.popularmoviesstage2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

import static com.portfolio.udacity.android.popularmoviesstage2.ui.MainActivity.LOG_TAG;

/**
 * Created by JonGaming on 28/03/2018.
 *
 */

public class Utils {
    private static boolean LOG_DEBUG = true;
    public static void logDebug(String aLog) {if (LOG_DEBUG) {
        Log.i(LOG_TAG, aLog);}
    }
    public static int pixelsToDp(@NonNull Context aContext, float aDp) {
        //Calculates pixels to dp.
        DisplayMetrics metrics = aContext.getResources().getDisplayMetrics();
        float fpixels = metrics.density * aDp;
        return(int) (fpixels + 0.5f);
    }
}
