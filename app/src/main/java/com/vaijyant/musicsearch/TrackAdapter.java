package com.vaijyant.musicsearch;

import android.content.Context;
import android.content.SharedPreferences;
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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vaijy on 2017-10-08.
 */

public class TrackAdapter extends ArrayAdapter<Track> {
    List<Track> mData;
    Context mContext;
    int mResource;
String TAG = "vt";
    public TrackAdapter(Context context, int resource, List<Track> objects) {
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
        if (mData.size() == 0)
            return convertView;

        Track track = mData.get(position);

        TextView viewName = convertView.findViewById(R.id.viewName);
        viewName.setText(track.getName());

        TextView viewArtist = convertView.findViewById(R.id.viewArtist);
        viewArtist.setText(track.getArtist());

        ImageView imageViewTrack = convertView.findViewById(R.id.imageViewTrack);
        if(track.getSmallImageURL()!= null) {
            new ImageAsyncTask().execute(track.getSmallImageURL(), imageViewTrack);
        }

        final ImageButton imgBtnFavourite  = convertView.findViewById(R.id.imgBtnFavourite);
        if(mContext.toString().contains("MainActivity") && mData != null) {
            imgBtnFavourite.setImageResource(android.R.drawable.btn_star_big_on);
            imgBtnFavourite.setTag("on");
        }

        if (mContext.toString().contains("SearchResultActivity") || mContext.toString().contains("TrackDetailsActivity")) {
            String name = mContext.getPackageName();
            ArrayList<Track> favourites = null;
            SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);
            try {
                favourites = TrackUtil.getTracksFromPref(pref);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < favourites.size(); i++) {
                if (favourites.get(i).getName().equals(mData.get(position).getName())) {
                    imgBtnFavourite.setImageResource(android.R.drawable.btn_star_big_on);
                    imgBtnFavourite.setTag("on");
                }
            }
        }

        imgBtnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgBtnFavourite.getTag().equals("off")){
                    String name = mContext.getPackageName();
                    ArrayList<Track> favourites = null;
                    SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);
                    try {
                        favourites = TrackUtil.getTracksFromPref(pref);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (favourites.size() <= 19) {
                        imgBtnFavourite.setImageResource(android.R.drawable.btn_star_big_on);
                        imgBtnFavourite.setTag("on");

                        Track track = mData.get(position);
                        TrackUtil.addTrackToPreferences(track, mContext);
                        Toast.makeText(mContext, track.getName() + " added to favorites.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "20 favourites allowed.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(imgBtnFavourite.getTag().equals("on")){
                    imgBtnFavourite.setImageResource(android.R.drawable.btn_star_big_off);
                    imgBtnFavourite.setTag("off");

                    Track track = mData.get(position);
                    try {
                        if (mContext.toString().contains("MainActivity") && mData != null) {
                            mData.remove(position);
                            notifyDataSetChanged();
                        }
                        TrackUtil.removeTrackFromPreferences(track, mContext);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(mContext, track.getName() + " removed form favorites.", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
