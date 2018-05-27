package com.udacity.popularmovies.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.popularmovies.Model.Review;
import com.udacity.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private final int mNumberItems;
    private final List<Review> mReviews;
    final private ReviewAdapter.ListReviewItemClickListener mOnClickListener;

    public ReviewAdapter(List<Review> reviews, ReviewAdapter.ListReviewItemClickListener mOnClickListener) {
        mReviews = reviews;
        mNumberItems = reviews.size();
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface ListReviewItemClickListener {
        void onListReviewItemClick(int clickedItemIndex);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_review_author)
        TextView tv_review_author;
        @BindView(R.id.tv_review_content)
        TextView tv_review_content;

        public ReviewViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            Review review = mReviews.get(listIndex);
            tv_review_author.setText(review.getAuthor());
            tv_review_content.setText(review.getContent());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListReviewItemClick(clickedPosition);
        }
    }
}