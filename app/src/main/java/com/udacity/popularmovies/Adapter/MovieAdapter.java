package com.udacity.popularmovies.Adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.Database.MovieContract;
import com.udacity.popularmovies.Model.Movie;
import com.udacity.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private final int mNumberItems;
    private final List<Movie> mMovies;
    final private ListItemClickListener mOnClickListener;
    private final Cursor mCursor;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieAdapter(List<Movie> movies, Cursor cursor, ListItemClickListener mOnClickListener) {
        int mNumberItems1;
        mMovies = movies;
        mNumberItems1 = 0;
        if(cursor == null)
            mNumberItems1 = movies.size();
        mNumberItems = mNumberItems1;
        mCursor = cursor;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mCursor != null){
            return mCursor.getCount();
        }
        return mNumberItems;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.iv_poster)
        ImageView iv_poster;
        final Context mContext;

        MovieViewHolder(View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {

            Movie movie;
            if(mCursor != null){
                if (!mCursor.moveToPosition(listIndex))
                    return;

                movie = new Movie();
                String posterPath = mCursor.getString(mCursor
                        .getColumnIndex(MovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH));
                movie.setmPosterPath(posterPath);
            }else {
                movie = mMovies.get(listIndex);
            }

            String baseUrl = "http://image.tmdb.org/t/p/";

            Picasso.get()
                    .load(baseUrl + "w342" + movie.getmPosterPath())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(iv_poster);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
