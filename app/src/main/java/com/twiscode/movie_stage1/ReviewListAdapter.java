package com.twiscode.movie_stage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.twiscode.movie_stage1.Model.ReviewItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andree on 8/6/17.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewListViewHolder> {


    private ArrayList<ReviewItem> reviews;
    public ReviewListAdapter() {}

    @Override
    public ReviewListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForReviewItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        Boolean shouldAttachToParent = false;

        View view = inflater.inflate(layoutIdForReviewItem, parent, shouldAttachToParent);
        return new ReviewListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewListViewHolder holder, int position) {
        final ReviewItem reviewItem = reviews.get(position);
        holder.mReviewContent.setText(reviewItem.getReviewContent());
        holder.mReviewAuthor.setText("by " + reviewItem.getAuthorName());

    }

    @Override
    public int getItemCount() {
        if (null == reviews) return 0;
        return reviews.size();
    }

    public void setReviewsData(ArrayList<ReviewItem> reviewItems) {
        reviews = reviewItems;
        notifyDataSetChanged();
    }

    class ReviewListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_review_content) TextView mReviewContent;
        @BindView(R.id.tv_review_author) TextView mReviewAuthor;

        public ReviewListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
