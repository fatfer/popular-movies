package com.udacity.popularmovies.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.Model.Trailer;
import com.udacity.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private final int mNumberItems;
    private final List<Trailer> mTrailers;
    final private TrailerAdapter.ListTrailerItemClickListener mOnClickListener;

    public TrailerAdapter(List<Trailer> trailers, TrailerAdapter.ListTrailerItemClickListener mOnClickListener) {
        mTrailers = trailers;
        mNumberItems = trailers.size();
        this.mOnClickListener = mOnClickListener;
    }


    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface ListTrailerItemClickListener {
        void onListTrailerItemClick(int clickedItemIndex);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_trailer_name)
        TextView tv_trailer_name;

        TrailerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            Trailer trailer = mTrailers.get(listIndex);
            tv_trailer_name.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListTrailerItemClick(clickedPosition);
        }
    }
}
