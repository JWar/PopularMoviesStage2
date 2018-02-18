package com.portfolio.udacity.android.popularmoviesstage2.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.portfolio.udacity.android.popularmoviesstage2.NetworkUtils;
import com.portfolio.udacity.android.popularmoviesstage2.R;
import com.portfolio.udacity.android.popularmoviesstage2.data.model.Movie;
import com.portfolio.udacity.android.popularmoviesstage2.ui.DetailActivity;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by JonGaming on 16/02/2018.
 *
 */

public class GridAdapter extends ArrayAdapter<Movie> {
    GridAdapter(Context aContext, List<Movie> aMovies) {
        super(aContext,0,aMovies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);
        if (convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movies_item, parent, false
            );
        }
        ImageView imageView = convertView.findViewById(R.id.movies_item_poster_iv);
        String url = NetworkUtils.IMAGE_URL;
        if (movie!=null) {
            url += movie.mPoster;
            Picasso.with(getContext())
                    .load(url)
                    //Hmm its crashes saying Resource Not Found Exception...
                    .placeholder(R.drawable.ic_image_black_48px)
                    .error(R.drawable.ic_error_black_48px)
                    .resize(getContext().getResources().getDimensionPixelSize(R.dimen.movie_thumbnail_size),
                            getContext().getResources().getDimensionPixelSize(R.dimen.movie_thumbnail_size))
                    .into(imageView);
            imageView.setTag(position);
            //180217_Hmm should onclick be set here? Anywhere else is can be set? Must be here..
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View aView) {
                    //Start DetailActivity.... using position of movie selected. This will require repository
                    DetailActivity.start(getContext(),(int)aView.getTag());
                }
            });
        }
        return convertView;
    }


}
