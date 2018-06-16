package com.udacity.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.udacity.popularmovies.Model.Trailer;
import com.udacity.popularmovies.Utils.Json;
import com.udacity.popularmovies.Utils.Network;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

class TheMovieDBTrailerQueryTask extends AsyncTask<URL, Void, String> {

    private static final String TAG = TheMovieDBQueryTask.class.getSimpleName();
    private final Context context;
    private final AsyncTaskCompleteListener<List<Trailer>> listener;

    public TheMovieDBTrailerQueryTask(Context context, AsyncTaskCompleteListener<List<Trailer>> listener)
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
                List<Trailer> mTrailers = Json.parseTrailersJson(searchResults);
                listener.onTaskComplete(mTrailers);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(context, R.string.get_data_from_api_error, Toast.LENGTH_SHORT).show();
        }
    }
}
