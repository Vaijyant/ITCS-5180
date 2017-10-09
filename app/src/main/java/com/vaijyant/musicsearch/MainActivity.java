package com.vaijyant.musicsearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TrackAsyncTask.IData{

    TrackAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSearch).setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.listViewFavorites);

        adapter = new TrackAdapter(this, R.layout.listitem, Data.favoriteTrackArrayList);

        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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
                Intent intent = new Intent(this, TrackDetailsActivity.class);
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
        switch (view.getId()) {
            case R.id.btnSearch:
                String editTrack = ((EditText) findViewById(R.id.editTrack)).getText().toString();
                String apiKey = "e4768d6dd483ddaeaf4a41154f699da4";
                new TrackAsyncTask(this).execute("http://ws.audioscrobbler.com/2.0/?format=json&method=track.search&track="
                        + editTrack + "&api_key=" + apiKey + "&limit=20");
                break;
        }
    }

    @Override
    public void setUpData(ArrayList<Track> trackArrayList) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putParcelableArrayListExtra("trackArrayList", trackArrayList);
        startActivity(intent);

    }
}
