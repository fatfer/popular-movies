package com.udacity.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.udacity.popularmovies.Model.Review;
import com.udacity.popularmovies.Utils.Json;
import com.udacity.popularmovies.Utils.Network;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

class TheMovieDBReviewQueryTask extends AsyncTask<URL, Void, String>
    {

        private static final String TAG = TheMovieDBQueryTask.class.getSimpleName();
        private final Context context;
        private final AsyncTaskCompleteListener<List<Review>> listener;

    public TheMovieDBReviewQueryTask(Context context, AsyncTaskCompleteListener<List<Review>> listener)
        {
            this.context = context;
            this.listener = listener;
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
            if (searchResults != null && !searchResults.equals("")) {
                try {
                    List<Review> mReviews = Json.parseReviewsJson(searchResults);
                    listener.onTaskComplete(mReviews);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(context, R.string.get_data_from_api_error, Toast.LENGTH_SHORT).show();
            }
        }
    }
