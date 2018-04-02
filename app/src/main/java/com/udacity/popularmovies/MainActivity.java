package com.udacity.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.udacity.popularmovies.Adapter.MovieAdapter;
import com.udacity.popularmovies.Utils.Json;
import com.udacity.popularmovies.Utils.Network;
import com.udacity.popularmovies.Model.Movie;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener,AdapterView.OnItemSelectedListener {

    @BindView(R.id.rv_movies) RecyclerView rv_movies;
    @BindView(R.id.spinner_order_by) Spinner spinner_order_by;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private MovieAdapter mAdapter;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_by_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_order_by.setAdapter(adapter);
        spinner_order_by.setOnItemSelectedListener(this);

        URL popularMoviesUrl = Network.buildPopularMoviesUrl();
        new TheMovieDBQueryTask().execute(popularMoviesUrl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if(id == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void drawMovies(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_movies.setLayoutManager(layoutManager);
        rv_movies.setHasFixedSize(true);

        mAdapter = new MovieAdapter(mMovies, this);
        rv_movies.setAdapter(mAdapter);
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, mMovies.get(position));
        startActivity(intent);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        launchDetailActivity(clickedItemIndex);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

        final String value = (String) adapterView.getItemAtPosition(pos);
        if (value.equals(getString(R.string.most_popular))) {
            URL popularMoviesUrl = Network.buildPopularMoviesUrl();
            new TheMovieDBQueryTask().execute(popularMoviesUrl);
        }
        else if (value.equals(getString(R.string.highest_rated))) {
            URL highestRatedMoviesUrl = Network.buildHighestRatedMoviesUrl();
            new TheMovieDBQueryTask().execute(highestRatedMoviesUrl);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class TheMovieDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL apiUrl = params[0];
            String searchResults = null;
            try {
                searchResults = Network.getResponseFromHttpUrl(apiUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            progressBar.setVisibility(View.INVISIBLE);
            if (searchResults != null && !searchResults.equals("")) {
                try {
                    mMovies = Json.parseMoviesJson(searchResults);
                    drawMovies();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(MainActivity.this, R.string.get_data_from_api_error, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
