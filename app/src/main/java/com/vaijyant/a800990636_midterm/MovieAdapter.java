package com.vaijyant.a800990636_midterm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

/**
 * Assignment   : Midterm
 * File         : MovieAdapter.java
 * Author       : Vaijyant Tomar
 * */

class MovieAdapter extends ArrayAdapter<Movie> {
    private List<Movie> mData;
    private Context mContext;
    private int mResource;

    public MovieAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @Override
    public int getViewTypeCount() {
        if (mData.size() == 0) {
            return 1;
        }
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        //===================== Set the View =======================================================
        final Movie movie = mData.get(position);

        TextView viewMovieName = convertView.findViewById(R.id.viewMovieName);
        viewMovieName.setText(movie.getOriginal_title());

        TextView viewReleaseDate = convertView.findViewById(R.id.viewReleaseDate);
        String released = "Released in " + movie.getRelease_date().substring(0, 4);
        viewReleaseDate.setText(released);

        ImageView imageViewMovie = convertView.findViewById(R.id.imageViewMovie);
        String baseUrl = "http://image.tmdb.org/t/p/w154";
        new ImageAsyncTask().execute(baseUrl + movie.getPoster_path(), imageViewMovie);

        //============= Events =====================================================================

        final ImageButton imgBtnFavourite = convertView.findViewById(R.id.imgBtnFavourite);

        //------------------------------------------------------------------------------------------
        if (mContext.toString().contains("FavoriteMoviesActivity") && mData != null) {
            imgBtnFavourite.setImageResource(android.R.drawable.btn_star_big_on);
            imgBtnFavourite.setTag("on");
        }
        //------------------------------------------------------------------------------------------

        imgBtnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imgBtnFavourite.getTag().equals("off")) {

                    MovieUtil.editSharedPreferences(movie,"add",  mContext);
                    imgBtnFavourite.setImageResource(android.R.drawable.btn_star_big_on);
                    imgBtnFavourite.setTag("on");
                    Toast.makeText(mContext, movie.getOriginal_title() + " added to favorites.", Toast.LENGTH_SHORT).show();

                } else if (imgBtnFavourite.getTag().equals("on")) {

                    imgBtnFavourite.setImageResource(android.R.drawable.btn_star_big_off);
                    imgBtnFavourite.setTag("off");
                    MovieUtil.editSharedPreferences(movie, "remove", mContext);
                    if (mContext.toString().contains("FavoriteActivity") && mData != null) {
                        mData.clear();
                        mData.addAll(MovieUtil.getSharedPreferences(mContext));
                    }
                    Toast.makeText(mContext, movie.getOriginal_title() + " removed from favorites.", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });


        if (mData.size() == 0)
            return convertView;

        return convertView;
    }

}
