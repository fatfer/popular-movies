package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.Model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = DetailActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "extra_movie";
    @BindView(R.id.tv_original_title) TextView tv_original_title;
    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_vote_average) TextView tv_vote_average;
    @BindView(R.id.iv_detail_poster) ImageView iv_detail_poster;
    @BindView(R.id.tb_favourite) ToggleButton tb_favourite;

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

}
