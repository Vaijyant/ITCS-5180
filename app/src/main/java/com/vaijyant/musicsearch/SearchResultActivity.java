package com.vaijyant.musicsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, TrackAsyncTask.IData {

    Track track;
    ArrayList<Track> trackArrayList;
    ListView listView;
    TrackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        trackArrayList = intent.getParcelableArrayListExtra("trackArrayList");
        listView = (ListView) findViewById(R.id.listViewResults);
        adapter = new TrackAdapter(this, R.layout.listitem, trackArrayList);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;
            case R.id.actionQuit:
                finishAffinity();
                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        track = trackArrayList.get(position);
        String apiKey = "e4768d6dd483ddaeaf4a41154f699da4";

        String url = "http://ws.audioscrobbler.com/2.0/?" +
                "method=track.getsimilar&artist="+ track.getArtist().replace(" ", "%20") +
                "&track=" + track.getName().replace(" ", "%20") +
                "&api_key=" + apiKey +
                "&format=json&limit=10";
        Log.d("VT", "onItemClick: " + "http://ws.audioscrobbler.com/2.0/?" +
                "method=track.getsimilar&artist=" + track.getArtist().replace(" ", "%20") +
                "&track=" + track.getName().replace(" ", "%20") +
                "&api_key=" + apiKey +
                "&format=json&limit=10");
        new TrackAsyncTask(this).execute(url);
    }

    @Override
    public void setUpData(ArrayList<Track> similarTrackList) {
        Intent intent = new Intent(this, TrackDetailsActivity.class);
        intent.putExtra("track", track);
        intent.putParcelableArrayListExtra("similarTrackList", similarTrackList);
        startActivity(intent);

    }
}
