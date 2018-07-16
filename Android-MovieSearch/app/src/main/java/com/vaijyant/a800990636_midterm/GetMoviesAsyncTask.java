package com.vaijyant.a800990636_midterm;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Assignment   : Midterm
 * File         : GetMoviesAsyncTask.java
 * Author       : Vaijyant Tomar
 * */
class GetMoviesAsyncTask extends AsyncTask<String/*input*/, Void/*progress*/, ArrayList<Movie>/*result*/> {

    IData activity;
    String TAG = "VT";

    public GetMoviesAsyncTask(IData activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected /*result*/ArrayList<Movie> doInBackground(/*input*/String... params) {
        String stURL = params[0];
        Log.d(TAG, "doInBackground: "+stURL);
        BufferedReader reader = null;
        try {
            URL url = new URL(stURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statusCode = con.getResponseCode();
            Log.d(TAG, "doInBackground: status code "+statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    sb.append(line);
                return MovieUtil.MovieJSONParser.parseMovies(sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    @Override
    protected void onProgressUpdate(/*progress*/Void... values) {

    }

    @Override
    protected void onPostExecute(/*result*/ArrayList<Movie> result) {
        activity.setUpData(result);

    }

    public static interface IData {
        public void setUpData(ArrayList<Movie> trackArrayList);

    }
}