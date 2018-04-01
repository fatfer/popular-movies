package com.udacity.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.udacity.popularmovies.Utils.Json;
import com.udacity.popularmovies.Utils.Network;
import com.udacity.popularmovies.model.Movie;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_movies)
    RecyclerView rv_movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL popularMoviesUrl = Network.buildPopularMoviesUrl();
        new TheMovieDBQueryTask().execute(popularMoviesUrl);
    }


    public static class TheMovieDBQueryTask extends AsyncTask<URL, Void, String> {

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

            }

        }
    }
}
