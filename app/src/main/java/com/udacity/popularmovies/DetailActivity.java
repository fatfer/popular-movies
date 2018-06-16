package com.udacity.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.Adapter.ReviewAdapter;
import com.udacity.popularmovies.Adapter.TrailerAdapter;
import com.udacity.popularmovies.ContentProvider.MovieProvider;
import com.udacity.popularmovies.Database.MovieContract;
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

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListTrailerItemClickListener
        ,ReviewAdapter.ListReviewItemClickListener {

    public static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_IS_FAVOURITE = "extra_is_favourite";
    public static final String ARTICLE_SCROLL_POSITION = "article_scroll_position";
    @BindView(R.id.tv_original_title) TextView tv_original_title;
    @BindView(R.id.tv_overview)
     TextView tv_overview;
    @BindView(R.id.tv_release_date)
     TextView tv_release_date;
    @BindView(R.id.tv_vote_average)
     TextView tv_vote_average;
    @BindView(R.id.iv_detail_poster)
     ImageView iv_detail_poster;
    @BindView(R.id.tb_favourite)
     ToggleButton tb_favourite;
    @BindView(R.id.rv_trailers)
     RecyclerView rv_trailers;
    @BindView(R.id.rv_reviews)
     RecyclerView rv_reviews;
    @BindView(R.id.sv_details)
     ScrollView sv_details;
    private List<Trailer> mTrailers;
    private List<Review> mReviews;
    private Movie mMovie;
    private boolean mIsFavourite;
    private int[] mScrollPosition;

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

         mIsFavourite = intent.getBooleanExtra(EXTRA_IS_FAVOURITE, false);

        if (movie == null) {
            closeOnError();
            return;
        }

        mMovie = movie;
        populateUI(movie);
        getTrailers(movie.getId());
        getReviews(movie.getId());

        tb_favourite.setChecked(mIsFavourite);
    }


    @OnCheckedChanged(R.id.tb_favourite)
    public void handleFavourite(ToggleButton button){
        if(button.isChecked()) {
            if(mIsFavourite){
                updateFavourite();
            }else {
                saveToFavourites();
                Toast.makeText(this, R.string.added_to_favourites, Toast.LENGTH_SHORT).show();
            }
        }else {
            deleteFromFavourites();
            Toast.makeText(this, R.string.removed_from_favourites, Toast.LENGTH_SHORT).show();
            mIsFavourite = false;
        }
    }

    private void updateFavourite(){
        ContentValues values = getContentValues();

        getContentResolver().update(
                MovieProvider.CONTENT_URI.buildUpon()
                        .appendPath(mMovie.getId()).build(), values,null,null);
    }

    private void saveToFavourites(){

        ContentValues values = getContentValues();

        getContentResolver().insert(
                MovieProvider.CONTENT_URI, values);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int x = sv_details.getScrollX();
        int y = sv_details.getScrollY();
        outState.putIntArray(ARTICLE_SCROLL_POSITION,
                new int[]{x,y});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mScrollPosition = savedInstanceState.getIntArray(ARTICLE_SCROLL_POSITION);
    }

    @NonNull
    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, mMovie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH, mMovie.getmPosterPath());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_TITLE, mMovie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_ADULT, mMovie.ismAdult());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, mMovie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, mMovie.getmReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, mMovie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE, mMovie.getOriginalLanguage());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH, mMovie.getmBackdropPath());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_POPULARITY, mMovie.getmPopularity());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT, mMovie.getmVoteCount());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_VIDEO, mMovie.isVideo());
        values.put(MovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE, mMovie.getmVoteAverage());
        return values;
    }

    private void deleteFromFavourites(){

        getContentResolver().delete(MovieProvider.CONTENT_URI.buildUpon().appendPath(mMovie.getId()).build() ,
                null, null);

    }

    private void populateUI(Movie movie) {

        String baseUrl = "http://image.tmdb.org/t/p/";
        Picasso.get()
                .load(baseUrl + "w500" + movie.getmPosterPath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(iv_detail_poster);
        tv_original_title.setText(movie.getOriginalTitle());
        tv_release_date.setText(movie.getmReleaseDate());
        tv_overview.setText(movie.getOverview());
        tv_vote_average.setText(String.valueOf(movie.getmVoteAverage()));

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
        Youtube.watchYoutubeVideo(this,mTrailers.get(clickedItemIndex).getmKey());
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
            if(mScrollPosition != null)
                sv_details.post(new Runnable() {
                    public void run() {
                        sv_details.scrollTo(mScrollPosition[0], mScrollPosition[1]);
                    }
                });
        }
    }

}
