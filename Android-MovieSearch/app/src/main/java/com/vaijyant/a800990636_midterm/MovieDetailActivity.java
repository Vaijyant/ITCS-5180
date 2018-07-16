package com.vaijyant.a800990636_midterm;

/**
 * Assignment   : Midterm
 * File         : MovieDetailActivity.java
 * Author       : Vaijyant Tomar
 * */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        Movie movie = intent.getParcelableExtra("movie");

        String textViewTitle = "Title: "+ movie.getOriginal_title();
        ((TextView)findViewById(R.id.textViewTitle)).setText(textViewTitle);

        String textViewOverview = "Overview: "+movie.getOverview();
        ((TextView)findViewById(R.id.textViewOverview)).setText(textViewOverview);

        String textViewReleaseDate = "Release Date: "+movie.getRelease_date();
        ((TextView)findViewById(R.id.textViewReleaseDate)).setText(textViewReleaseDate);

        String textViewRating = "Rating: "+movie.getVote_average();
        ((TextView)findViewById(R.id.textViewRating)).setText(textViewRating);

        ImageView imageViewMoviePoster = (ImageView)findViewById(R.id.imageViewMoviePoster);
        String baseUrl = "http://image.tmdb.org/t/p/w342";
        new GetImageAsyncTask().execute(baseUrl+movie.getPoster_path(), imageViewMoviePoster);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_fa; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_da, menu);
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
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
