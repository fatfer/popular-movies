package com.udacity.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.udacity.popularmovies.Adapter.MovieAdapter;
import com.udacity.popularmovies.ContentProvider.MovieProvider;
import com.udacity.popularmovies.Database.MovieDbHelper;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.Utils.Network;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.popularmovies.Utils.Network.isInternetAvailable;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    @BindView(R.id.rv_movies)
     RecyclerView rv_movies;
    @BindView(R.id.progressBar)
     ProgressBar progressBar;
    private List<Movie> mMovies;
    private Cursor mFavouritesCursor;
    private static final String CURRENT_LIST_TYPE = "current_list_type";
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    public enum currentListType {
        POPULAR,
        HIGHEST_RATED,
        FAVOURITES
    }
    private int mCurrentListType;
    private GridLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Stetho.initializeWithDefaults(this);

        if (savedInstanceState != null) {
            int savedListType = savedInstanceState.getInt(CURRENT_LIST_TYPE);

            loadSavedListTypeMovies(savedListType);
        }else{
            getMostPopularFilms();
        }

    }

    private void loadSavedListTypeMovies(int savedListType) {
        if(savedListType == currentListType.POPULAR.ordinal()){
            getMostPopularFilms();
        }else if(savedListType == currentListType.HIGHEST_RATED.ordinal()){
            getHighestRatedFilms();
        }else if(savedListType == currentListType.FAVOURITES.ordinal()){
            getFavouriteFilms();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = rv_movies.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadSavedListTypeMovies(mCurrentListType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_favourites){
            mMovies = null;
            getFavouriteFilms();
            return true;
        } else if (isInternetAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            if(id == R.id.action_most_popular){
                getMostPopularFilms();
            }
            else if(id == R.id.action_highest_rated){
                getHighestRatedFilms();
            }
            return true;
        }else{
            Toast.makeText(MainActivity.this, R.string.get_data_from_api_error, Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_LIST_TYPE, mCurrentListType);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private Cursor getAllFavouriteMovies() {

        CursorLoader cursorLoader = new CursorLoader(getBaseContext(), MovieProvider.CONTENT_URI,
                null, null, null, null);
        return cursorLoader.loadInBackground();
    }

    private void drawMovies(Cursor cursor){
        int posterWidth = 250;
        mLayoutManager = new GridLayoutManager(getApplicationContext(), calculateBestSpanCount(posterWidth));
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_movies.setLayoutManager(mLayoutManager);
        rv_movies.setHasFixedSize(true);

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            RecyclerView.LayoutManager layout = rv_movies.getLayoutManager();
            layout.onRestoreInstanceState(listState);
        }

        MovieAdapter adapter = new MovieAdapter(mMovies, cursor, this);
        rv_movies.setAdapter(adapter);
    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        MovieDbHelper dbHelper = new MovieDbHelper(this);
        Movie movie;
        if(mFavouritesCursor != null && mFavouritesCursor.moveToFirst()){
            mFavouritesCursor.moveToPosition(position);
            movie = dbHelper.getMovieByCursor(mFavouritesCursor);
            mMovies = null;
        }else{
            movie = mMovies.get(position);
            mFavouritesCursor = null;
        }

        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);

        boolean isFavourite = false;

        CursorLoader cursorMovie= new CursorLoader(getBaseContext(), MovieProvider.CONTENT_URI.buildUpon()
                .appendPath(movie.getId()).build(),null, null, null, null);
        Cursor cursor = cursorMovie.loadInBackground();

        if(cursor.getCount() == 1) {
            isFavourite = true;
        }

        intent.putExtra(DetailActivity.EXTRA_IS_FAVOURITE, isFavourite);

        startActivity(intent);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        launchDetailActivity(clickedItemIndex);
    }


    private void getMostPopularFilms() {
        getSupportActionBar().setTitle(R.string.most_popular_films);
        URL popularMoviesUrl = Network.buildPopularMoviesUrl();
        new TheMovieDBQueryTask(this, new TheMovieDBQueryTaskCompleteListener())
                .execute(popularMoviesUrl);
        mCurrentListType = currentListType.POPULAR.ordinal();
    }

    private void getHighestRatedFilms() {
        getSupportActionBar().setTitle(R.string.highest_rated_films);
        URL highestRatedMoviesUrl = Network.buildHighestRatedMoviesUrl();
        new TheMovieDBQueryTask(this, new TheMovieDBQueryTaskCompleteListener())
                .execute(highestRatedMoviesUrl);
        mCurrentListType = currentListType.HIGHEST_RATED.ordinal();
    }

    private void getFavouriteFilms() {
        getSupportActionBar().setTitle(R.string.favourite_films);
        Cursor cursor = getAllFavouriteMovies();
        mFavouritesCursor = cursor;
        drawMovies(cursor);
        progressBar.setVisibility(View.INVISIBLE);
        mCurrentListType = currentListType.FAVOURITES.ordinal();
    }


    public class TheMovieDBQueryTaskCompleteListener implements AsyncTaskCompleteListener<List<Movie>>
    {

        @Override
        public void onTaskComplete(List<Movie> result) {
            progressBar.setVisibility(View.INVISIBLE);
            mMovies = result;
            drawMovies(null);
            mFavouritesCursor = null;
        }
    }

}
