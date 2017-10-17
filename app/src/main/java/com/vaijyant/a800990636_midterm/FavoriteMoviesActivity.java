package com.vaijyant.a800990636_midterm;

/**
 * Assignment   : Midterm
 * File         : FavoriteMoviesActivity.java
 * Author       : Vaijyant Tomar
 * */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

public class FavoriteMoviesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    MovieAdapter adapter;
    ArrayList<Movie> favoriteMovieList = new ArrayList<Movie>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        favoriteMovieList.clear();
        favoriteMovieList.addAll(MovieUtil.getSharedPreferences(getBaseContext()));

        ListView listView = (ListView) findViewById(R.id.listViewFavorites);
        adapter = new MovieAdapter(this, R.layout.listitem, favoriteMovieList);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_fa; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fa, menu);
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

            case R.id.actionSortPopularity:
                Collections.sort(favoriteMovieList, new MovieComparator.MoviePopularityComparator());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Sorted by Popularity.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.actionSortRating:
                Collections.sort(favoriteMovieList, new MovieComparator.MovieRatingComparator());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Sorted by Rating.", Toast.LENGTH_SHORT).show();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Movie movie = favoriteMovieList.get(position);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);

    }
}
