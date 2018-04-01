package com.udacity.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    @BindView(R.id.rv_movies)
    RecyclerView rv_movies;
    private Toast mToast;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        URL popularMoviesUrl = Network.buildPopularMoviesUrl();
        new TheMovieDBQueryTask().execute(popularMoviesUrl);

    }

    public void drawMovies(List<Movie> movies){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_movies.setLayoutManager(layoutManager);
        rv_movies.setHasFixedSize(true);

        mAdapter = new MovieAdapter(movies, this);
        rv_movies.setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }


    public class TheMovieDBQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL popularMoviesUrl = params[0];
            String searchResults = null;
            try {
                searchResults = Network.getResponseFromHttpUrl(popularMoviesUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                try {
                    List<Movie> movies = Json.parseMoviesJson(searchResults);
                    drawMovies(movies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

            }

        }
    }
}
