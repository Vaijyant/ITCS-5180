package com.vaijyant.a800990636_midterm;

/**
 * Assignment   : Midterm
 * File         : MovieUtil.java
 * Author       : Vaijyant Tomar
 * */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;


public class MovieUtil {
    static public class MovieJSONParser {
        static ArrayList<Movie> parseMovies(String in) throws JSONException {
            ArrayList<Movie> movieList = new ArrayList<Movie>();
            JSONObject root = new JSONObject(in);
            JSONArray JSONArrayMovie = root.getJSONArray("results");

            for (int i = 0; i < JSONArrayMovie.length(); i++) {
                JSONObject JSONObjectMovie = JSONArrayMovie.getJSONObject(i);
                Movie movie = new Movie();

                movie.setOriginal_title(JSONObjectMovie.getString("original_title"));
                movie.setOverview(JSONObjectMovie.getString("overview"));
                movie.setRelease_date(JSONObjectMovie.getString("release_date"));
                movie.setVote_average(JSONObjectMovie.getString("vote_average"));
                movie.setPopularity(JSONObjectMovie.getString("popularity"));
                movie.setPoster_path(JSONObjectMovie.getString("poster_path"));

                movieList.add(movie);
            }
            return movieList;
        }
    }

    static ArrayList<Movie> getSharedPreferences(Context mContext) {
        String name = mContext.getPackageName();
        SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("favorites", null);
        Type type = new TypeToken<ArrayList<Movie>>() {
        }.getType();
        ArrayList<Movie> favoriteList = gson.fromJson(json, type);
        if (favoriteList != null && favoriteList.size() > 0) {
            return favoriteList;
        }
        favoriteList = new ArrayList<>();
        return favoriteList;
    }

    public static void editSharedPreferences(Movie movie, String method, Context mContext) {

        ArrayList<Movie> favoriteList = getSharedPreferences(mContext);

        String name = mContext.getPackageName();
        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = pref.edit();


        switch (method) {
            case "add":
                favoriteList.add(movie);

                break;
            case "remove":
                for(int i=0; i<favoriteList.size();i++){
                    Movie m = favoriteList.get(i);
                    if(m.getOriginal_title().equals(movie.getOriginal_title())){
                        favoriteList.remove(m);
                    }
                }
                break;

            default:
                Log.d("VT", "editSharedPreferences: Invalid option");
        }

        Gson gson = new Gson();
        String json = gson.toJson(favoriteList);
        prefsEditor.putString("favorites", json);
        prefsEditor.commit();
    }
}
