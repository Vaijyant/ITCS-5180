package com.vaijyant.a800990636_midterm;
/**
 * Assignment   : Midterm
 * File         : MainActivity.java
 * Author       : Vaijyant Tomar
 * */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetMoviesAsyncTask.IData, AdapterView.OnItemClickListener {

    MovieAdapter adapter;
    ArrayList<Movie> searchMovieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        ListView listView = (ListView) findViewById(R.id.listViewSearchResult);
        adapter = new MovieAdapter(this, R.layout.listitem, searchMovieList);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                String movie = ((EditText) findViewById(R.id.editMovie)).getText().toString();

                if (movie == null || movie.length() == 0) {
                    Toast.makeText(this, "Please provide a movie name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchMovieList.clear();
                adapter.notifyDataSetChanged();
                String url = "http://api.themoviedb.org/3/search/movie/?query=" + movie.replace(" ", "+") + "&api_key=efd79693869c18df1580cbb8d72c0b69&page=1";
                new GetMoviesAsyncTask(this).execute(url);
                break;
        }
    }

    @Override
    public void setUpData(ArrayList<Movie> movieArrayList) {
        if (movieArrayList == null || movieArrayList.size() == 0) {
            Toast.makeText(this, "No movies found!", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            return;
        }
        searchMovieList.addAll(movieArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Movie movie = searchMovieList.get(position);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_fa; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.actionSortPopularity:
                Collections.sort(searchMovieList, new MovieComparator.MoviePopularityComparator());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Sorted by Popularity.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.actionSortRating:
                Collections.sort(searchMovieList, new MovieComparator.MovieRatingComparator());
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Sorted by Rating.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.actionShowFavorites:
                Intent intent = new Intent(this, FavoriteMoviesActivity.class);
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