package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.udacity.popularmovies.Adapter.MovieAdapter;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.Utils.Network;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.popularmovies.Utils.Network.isInternetAvailable;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener,AdapterView.OnItemSelectedListener {

    @BindView(R.id.rv_movies) RecyclerView rv_movies;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getMostPopularFilms();
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

        if (isInternetAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            if(id == R.id.action_most_popular){
                getMostPopularFilms();
            }
            else if(id == R.id.action_highest_rated){
                getHighestRatedFilms();
            }
            else if(id == R.id.action_favourites){
                Toast.makeText(MainActivity.this, R.string.favourites, Toast.LENGTH_SHORT).show();
            }
            return true;
        }else{
            Toast.makeText(MainActivity.this, R.string.get_data_from_api_error, Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void drawMovies(){
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_movies.setLayoutManager(layoutManager);
        rv_movies.setHasFixedSize(true);

        MovieAdapter adapter = new MovieAdapter(mMovies, this);
        rv_movies.setAdapter(adapter);
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

    //TODO: Revisar para que se usa esto
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        final String value = (String) adapterView.getItemAtPosition(position);

        if (isInternetAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            if (value.equals(getString(R.string.most_popular))) {
                getMostPopularFilms();
            }
            else if (value.equals(getString(R.string.highest_rated))) {
                getHighestRatedFilms();
            }
        }else{
            Toast.makeText(MainActivity.this, R.string.get_data_from_api_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void getMostPopularFilms() {
        URL popularMoviesUrl = Network.buildPopularMoviesUrl();
        new TheMovieDBQueryTask(this, new TheMovieDBQueryTaskCompleteListener())
                .execute(popularMoviesUrl);
    }

    private void getHighestRatedFilms() {
        URL highestRatedMoviesUrl = Network.buildHighestRatedMoviesUrl();
        new TheMovieDBQueryTask(this, new TheMovieDBQueryTaskCompleteListener())
                .execute(highestRatedMoviesUrl);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class TheMovieDBQueryTaskCompleteListener implements AsyncTaskCompleteListener<List<Movie>>
    {

        @Override
        public void onTaskComplete(List<Movie> result) {
            progressBar.setVisibility(View.INVISIBLE);
            mMovies = result;
            drawMovies();
        }
    }

}
