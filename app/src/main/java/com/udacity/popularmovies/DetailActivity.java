package com.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.Adapter.MovieAdapter;
import com.udacity.popularmovies.Adapter.TrailerAdapter;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.Model.Trailer;
import com.udacity.popularmovies.Utils.Network;
import com.udacity.popularmovies.Utils.Youtube;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

import static com.udacity.popularmovies.Utils.Network.isInternetAvailable;

public class DetailActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener,AdapterView.OnItemSelectedListener {

    public static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "extra_movie";
    @BindView(R.id.tv_original_title) TextView tv_original_title;
    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_vote_average) TextView tv_vote_average;
    @BindView(R.id.iv_detail_poster) ImageView iv_detail_poster;
    @BindView(R.id.tb_favourite) ToggleButton tb_favourite;
    @BindView(R.id.rv_trailers) RecyclerView rv_trailers;
    private List<Trailer> mTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Movie movie = (Movie) (intent != null ? intent.getExtras()
                .getParcelable(EXTRA_MOVIE) : null);

        if (movie == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(movie);
        getTrailers(movie.getId());
    }

    @OnCheckedChanged(R.id.tb_favourite)
    public void handleFavourite(ToggleButton button){
        if(button.isChecked()) {
            Toast.makeText(this,"Fav",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"No Fav",Toast.LENGTH_SHORT).show();
        }
    }

    private void populateUI(Movie movie) {

        String baseUrl = "http://image.tmdb.org/t/p/";
        Picasso.get()
                .load(baseUrl + "w500" + movie.getPosterPath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(iv_detail_poster);
        tv_original_title.setText(movie.getOriginalTitle());
        tv_release_date.setText(movie.getReleaseDate());
        tv_overview.setText(movie.getOverview());
        tv_vote_average.setText(String.valueOf(movie.getVoteAverage()));

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


    private void getTrailers(String id) {
        URL movieTrailersUrl = Network.buildMovieTrailersUrl(id);
        new TheMovieDBTrailerQueryTask(this, new TheMovieDBTrailerQueryTaskCompleteListener())
                .execute(movieTrailersUrl);
    }

    private void drawTrailers(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_trailers.setLayoutManager(layoutManager);
        rv_trailers.setHasFixedSize(true);

        TrailerAdapter adapter = new TrailerAdapter(mTrailers, this);
        rv_trailers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv_trailers.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        final String value = (String) adapterView.getItemAtPosition(position);

        if (isInternetAvailable(this)) {
            if (value.equals(getString(R.string.most_popular))) {

            }
            else if (value.equals(getString(R.string.highest_rated))) {

            }
        }else{
            Toast.makeText(DetailActivity.this, R.string.get_data_from_api_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Youtube.watchYoutubeVideo(this,mTrailers.get(clickedItemIndex).getKey());
    }

    public class TheMovieDBTrailerQueryTaskCompleteListener implements AsyncTaskCompleteListener<List<Trailer>>
    {

        @Override
        public void onTaskComplete(List<Trailer> result) {
            mTrailers = result;
            drawTrailers();
        }
    }

}
