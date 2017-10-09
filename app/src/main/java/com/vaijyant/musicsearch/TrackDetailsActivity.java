package com.vaijyant.musicsearch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class TrackDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);
        Intent intent = getIntent();

        Track track = (Track) intent.getParcelableExtra("track");
        ArrayList<Track> similarTrackList = intent.getParcelableArrayListExtra("similarTrackList");

        ((TextView)findViewById(R.id.viewNameDA)).setText(track.getName());
        ((TextView)findViewById(R.id.viewArtistDA)).setText(track.getArtist());
        ((TextView)findViewById(R.id.viewURLDA)).setText(track.getUrl());
        new ImageAsyncTask().execute(track.getLargeImageURL(), findViewById(R.id.imgAlbumArt));


        findViewById(R.id.viewURLDA).setOnClickListener(this);


        ListView listView = (ListView) findViewById(R.id.listViewTrackDetails);

        TrackAdapter adapter = new TrackAdapter(this, R.layout.listitem, similarTrackList);

        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
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
                finish();
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
}
