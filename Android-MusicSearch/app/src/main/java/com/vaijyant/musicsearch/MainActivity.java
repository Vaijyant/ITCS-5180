package com.vaijyant.musicsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TrackAsyncTask.IData, AdapterView.OnItemClickListener {

    TrackAdapter adapter;
    ArrayList<Track> favoriteTrackArrayList;
    int REQUEST = -1;
    Track favTrack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSearch).setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.listViewFavorites);
        favoriteTrackArrayList = new ArrayList<>();

        updateFavouriteTracks();
        adapter = new TrackAdapter(this, R.layout.listitem, favoriteTrackArrayList);

        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateFavouriteTracks();
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionHome:
                Toast.makeText(this, "Already at home.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.actionQuit:
                finishAffinity();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:

                String track = ((EditText) findViewById(R.id.editTrack)).getText().toString();

                if (track == null || track.length() == 0) {
                    Toast.makeText(this, "Please provide a track name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                REQUEST = 0;
                String editTrack = ((EditText) findViewById(R.id.editTrack)).getText().toString();
                String apiKey = "e4768d6dd483ddaeaf4a41154f699da4";
                new TrackAsyncTask(this).execute("http://ws.audioscrobbler.com/2.0/?format=json&method=track.search&track="
                        + editTrack + "&api_key=" + apiKey + "&limit=20");
                break;
        }
    }

    @Override
    public void setUpData(ArrayList<Track> trackArrayList) {
        if (REQUEST == 0) {
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putParcelableArrayListExtra("trackArrayList", trackArrayList);
            startActivity(intent);
        } else if (REQUEST == 1) {

            Intent intent = new Intent(this, TrackDetailsActivity.class);
            intent.putExtra("track", favTrack);
            intent.putParcelableArrayListExtra("similarTrackList", trackArrayList);
            startActivity(intent);
        }
        finish();
    }

    public void updateFavouriteTracks() {
        favoriteTrackArrayList.removeAll(favoriteTrackArrayList);
        String prefName = getPackageName();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(prefName, MODE_PRIVATE);
        try {
            favoriteTrackArrayList.addAll(TrackUtil.getTracksFromPref(sharedPreferences));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        REQUEST = 1;
        favTrack = favoriteTrackArrayList.get(position);
        String apiKey = "e4768d6dd483ddaeaf4a41154f699da4";

        String url = "http://ws.audioscrobbler.com/2.0/?" +
                "method=track.getsimilar&artist=" + favTrack.getArtist().replace(" ", "%20") +
                "&track=" + favTrack.getName().replace(" ", "%20").replace("&", "%26") +
                "&api_key=" + apiKey +
                "&format=json&limit=10";
        new TrackAsyncTask(this).execute(url);
    }
}
