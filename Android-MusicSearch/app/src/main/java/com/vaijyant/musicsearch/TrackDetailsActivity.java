package com.vaijyant.musicsearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrackDetailsActivity extends AppCompatActivity implements View.OnClickListener, TrackAsyncTask.IData, AdapterView.OnItemClickListener {

    ArrayList<Track> similarTrackList;
    TrackAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);
        Intent intent = getIntent();

        Track track = (Track) intent.getParcelableExtra("track");
        similarTrackList = intent.getParcelableArrayListExtra("similarTrackList");

        setView(track);
        new ImageAsyncTask().execute(track.getLargeImageURL(), findViewById(R.id.imgAlbumArt));


        findViewById(R.id.viewURLDA).setOnClickListener(this);


        listView = (ListView) findViewById(R.id.listViewTrackDetails);

        adapter = new TrackAdapter(this, R.layout.listitem, similarTrackList);

        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(this);
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
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.viewURLDA:
                String url = ((TextView) findViewById(R.id.viewURLDA)).getText().toString();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Track track = similarTrackList.get(position);
        String apiKey = "e4768d6dd483ddaeaf4a41154f699da4";
        setView(track);
        String url = "http://ws.audioscrobbler.com/2.0/?" +
                "method=track.getsimilar&artist=" + track.getArtist().replace(" ", "%20") +
                "&track=" + track.getName().replace(" ", "%20") +
                "&api_key=" + apiKey +
                "&format=json&limit=10";
        similarTrackList.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Details for " + track.getName(), Toast.LENGTH_SHORT).show();
        new TrackAsyncTask(this).execute(url);


    }

    @Override
    public void setUpData(ArrayList<Track> trackArrayList) {

        similarTrackList.addAll(trackArrayList);
        adapter.notifyDataSetChanged();
    }

    public void setView(Track track) {

        ((TextView) findViewById(R.id.viewNameDA)).setText(track.getName());
        ((TextView) findViewById(R.id.viewArtistDA)).setText(track.getArtist());
        ((TextView) findViewById(R.id.viewURLDA)).setText(track.getUrl());
        new ImageAsyncTask().execute(track.getLargeImageURL(), findViewById(R.id.imgAlbumArt));


    }
}
