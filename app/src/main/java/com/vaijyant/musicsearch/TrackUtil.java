package com.vaijyant.musicsearch;

/**
 * Created by vaijy on 2017-10-07.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by vaijy on 2017-09-25.
 */

public class TrackUtil {
    static public class TrackJSONParser {
        static ArrayList<Track> parseTracks(String in) throws JSONException {

            JSONObject root = new JSONObject(in);
            JSONArray JSONArrayTrack = null;
            if(root.has("results")) {
                JSONArrayTrack = root.getJSONObject("results").getJSONObject("trackmatches").getJSONArray("track");
                return getSearchedTracks(JSONArrayTrack);
            }
            else if(root.has("similartracks")) {
                JSONArrayTrack = root.getJSONObject("similartracks").getJSONArray("track");
                return getSimilarTracks(JSONArrayTrack);
            }
            return null;
        }
        static ArrayList<Track> getSearchedTracks(JSONArray JSONArrayTrack) throws JSONException {
            ArrayList<Track> trackArrayList = new ArrayList<Track>();
            for (int i = 0; i < JSONArrayTrack.length(); i++) {
                JSONObject JSONObjectTrack = JSONArrayTrack.getJSONObject(i);
                Track track = new Track();
                track.setName(JSONObjectTrack.getString("name"));
                track.setArtist(JSONObjectTrack.getString("artist"));
                track.setUrl(JSONObjectTrack.getString("url"));
                track.setSmallImageURL(JSONObjectTrack.getJSONArray("image").getJSONObject(0).getString("#text"));
                track.setLargeImageURL(JSONObjectTrack.getJSONArray("image").getJSONObject(2).getString("#text"));

                trackArrayList.add(track);
            }
            return trackArrayList;
        }

        static ArrayList<Track> getSimilarTracks(JSONArray JSONArrayTrack) throws JSONException {
            ArrayList<Track> trackArrayList = new ArrayList<Track>();
            for (int i = 0; i < JSONArrayTrack.length(); i++) {
                JSONObject JSONObjectTrack = JSONArrayTrack.getJSONObject(i);
                Track track = new Track();
                track.setName(JSONObjectTrack.getString("name"));
                track.setArtist(JSONObjectTrack.getJSONObject("artist").getString("name"));
                track.setUrl(JSONObjectTrack.getString("url"));
                track.setSmallImageURL(JSONObjectTrack.getJSONArray("image").getJSONObject(0).getString("#text"));
                track.setLargeImageURL(JSONObjectTrack.getJSONArray("image").getJSONObject(2).getString("#text"));

                trackArrayList.add(track);
            }
            return trackArrayList;
        }
    }
}
