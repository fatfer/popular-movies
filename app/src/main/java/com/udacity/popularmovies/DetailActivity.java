package com.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.udacity.popularmovies.Adapter.ReviewAdapter;
import com.udacity.popularmovies.Adapter.TrailerAdapter;
import com.udacity.popularmovies.Database.MovieDbHelper;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.Model.Review;
import com.udacity.popularmovies.Model.Trailer;
import com.udacity.popularmovies.Utils.Network;
import com.udacity.popularmovies.Utils.Youtube;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

import static com.udacity.popularmovies.Utils.Network.isInternetAvailable;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListTrailerItemClickListener
        ,ReviewAdapter.ListReviewItemClickListener {

    public static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_IS_FAVOURITE = "extra_is_favourite";
    @BindView(R.id.tv_original_title) TextView tv_original_title;
    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_vote_average) TextView tv_vote_average;
    @BindView(R.id.iv_detail_poster) ImageView iv_detail_poster;
    @BindView(R.id.tb_favourite) ToggleButton tb_favourite;
    @BindView(R.id.rv_trailers) RecyclerView rv_trailers;
    @BindView(R.id.rv_reviews) RecyclerView rv_reviews;
    private List<Trailer> mTrailers;
    private List<Review> mReviews;
    private Movie mMovie;
    private boolean isFavourite;

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

         isFavourite = intent.getBooleanExtra(EXTRA_IS_FAVOURITE, false);

        if (movie == null) {
            closeOnError();
            return;
        }

        mMovie = movie;
        populateUI(movie);
        getTrailers(movie.getId());
        getReviews(movie.getId());

        tb_favourite.setChecked(isFavourite);
    }

    @OnCheckedChanged(R.id.tb_favourite)
    public void handleFavourite(ToggleButton button){
        if(button.isChecked()) {
            if(isFavourite == false) {
                Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
                isFavourite = true;
            }
            MovieDbHelper movieDbHelper = new MovieDbHelper(getBaseContext());
            movieDbHelper.addMovie(mMovie);
        }else {
            MovieDbHelper movieDbHelper = new MovieDbHelper(getBaseContext());
            movieDbHelper.deleteMovie(mMovie);
            if (isFavourite) {
                Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                isFavourite = false;
            }
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

    private void getReviews(String id) {
        URL movieTrailersUrl = Network.buildMovieReviewsUrl(id);
        new TheMovieDBReviewQueryTask(this, new TheMovieDBReviewQueryTaskCompleteListener())
                .execute(movieTrailersUrl);
    }

    private void drawReviews(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_reviews.setLayoutManager(layoutManager);
        rv_reviews.setHasFixedSize(true);

        ReviewAdapter adapter = new ReviewAdapter(mReviews, this);
        rv_reviews.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv_reviews.setAdapter(adapter);
    }

    @Override
    public void onListReviewItemClick(int clickedItemIndex) {

    }

    @Override
    public void onListTrailerItemClick(int clickedItemIndex) {
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

    public class TheMovieDBReviewQueryTaskCompleteListener implements AsyncTaskCompleteListener<List<Review>>
    {

        @Override
        public void onTaskComplete(List<Review> result) {
            mReviews = result;
            drawReviews();
        }
    }

}
