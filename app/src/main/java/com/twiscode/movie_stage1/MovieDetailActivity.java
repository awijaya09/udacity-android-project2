package com.twiscode.movie_stage1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twiscode.movie_stage1.Model.MovieItem;
import com.twiscode.movie_stage1.Model.ReviewItem;
import com.twiscode.movie_stage1.Model.VideoItem;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_movie_detail_title) TextView mMovieDetailTitle;
    @BindView(R.id.tv_movie_detail_desc) TextView mMovieDetailDesc;
    @BindView(R.id.tv_movie_detail_rating) TextView mMovieDetailRating;
    @BindView(R.id.tv_movie_detail_total_vote) TextView mTotalVote;
    @BindView(R.id.tv_no_reviews) TextView mNoReviews;
    @BindView(R.id.tv_movie_detail_release_date) TextView mMovieReleaseDate;

    @BindView(R.id.iv_movie_detail_image) ImageView mMovieImage;
    @BindView(R.id.iv_image_banner) ImageView mBannerImage;

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.movie_detail_toolbar) Toolbar mToolbar;

    @BindView(R.id.rv_videos) RecyclerView mRecyclerViewVideos;
    @BindView(R.id.rv_reviews) RecyclerView mRecyclerViewReviews;


    private ArrayList<VideoItem> videoItems;
    private ArrayList<ReviewItem> reviewItems;

    private VideoListAdapter mVideoListAdapter;
    private ReviewListAdapter mReviewListAdapter;

    private LinearLayoutManager mVideoLinearLayoutManager;
    private LinearLayoutManager mReviewLinearLayoutManager;

    private String movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Initialize
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setUpContent();

        videoItems = new ArrayList<VideoItem>();
        reviewItems = new ArrayList<ReviewItem>();

        mVideoLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mReviewLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewVideos.setLayoutManager(mVideoLinearLayoutManager);
        mRecyclerViewVideos.setHasFixedSize(true);

        mRecyclerViewReviews.setLayoutManager(mReviewLinearLayoutManager);
        mRecyclerViewReviews.setHasFixedSize(true);

        mVideoListAdapter = new VideoListAdapter(this);
        mReviewListAdapter = new ReviewListAdapter();

        mRecyclerViewVideos.setAdapter(mVideoListAdapter);
        mRecyclerViewReviews.setAdapter(mReviewListAdapter);

        loadVideosReviews();

    }

    private void loadVideosReviews() {

        if (null != movieId) {
            URL videosUrlRequest = NetworkUtils.buildVideoRequestUrl(NetworkUtils.MOVIEDB_VIDEOS_API , movieId);
            URL reviewsUrlRequest = NetworkUtils.buildVideoRequestUrl(NetworkUtils.MOVIEDB_REVIEWS_API , movieId);
            URL urls[] = {videosUrlRequest, reviewsUrlRequest};
            new FetchVideos().execute(urls);

        }
    }

    private void setUpContent() {
        Intent intent = getIntent();

        if (intent.hasExtra("MovieItem")){
            MovieItem item = (MovieItem) intent.getSerializableExtra("MovieItem");
            movieId = Integer.toString(item.getMovieID());
            mMovieDetailTitle.setText(item.getMovieTitle());
            mMovieDetailDesc.setText(item.getMovieDescription());
            mMovieDetailRating.setText(item.getMovieRating() + " /10");

            // Get release date in good string
            String date = Helper.convertStringToDate(item.getReleaseDate());
            mMovieReleaseDate.setText(date);

            // Put thousand separator on the vote count
            String totalVoteCount = String.format("%,d", item.getVoteCount());
            mTotalVote.setText(totalVoteCount + " votes");

            setTitle(" ");
            Picasso.with(this).load(item.getImgUrl()).into(mMovieImage);
            Picasso.with(this).load(item.getBackdropImgUrl()).into(mBannerImage);
        }else{
            Log.d("Intent description", "onCreate: No Intent found");
        }
    }

    private void showVideoLoading() {
        mRecyclerViewVideos.setVisibility(View.INVISIBLE);
    }


    private void showAllVideos() {
        mRecyclerViewVideos.setVisibility(View.VISIBLE);
    }

    private void showAllReview() {
        mRecyclerViewReviews.setVisibility(View.VISIBLE);
    }

    private class FetchVideos extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showVideoLoading();
        }

        @Override
        protected String doInBackground(URL... urls) {

            if (urls.length == 0) { return null; }

            URL trailerParamUrl = urls[0];
            URL reviewsParamUrl = urls[1];
            try{
                videoItems.addAll(NetworkUtils.getAllVideos(trailerParamUrl));
                reviewItems.addAll(NetworkUtils.getAllReviews(reviewsParamUrl));
            } catch (IOException e) {
                Log.e("Networking Error", "doInBackground: error in getting JSON: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (videoItems != null && !videoItems.isEmpty()){
                mVideoListAdapter.setVideoData(videoItems);
                showAllVideos();
            } else {
                Log.d("Video load error", "onPostExecute: No video found");;
            }

            if (reviewItems != null && !reviewItems.isEmpty()) {
                mReviewListAdapter.setReviewsData(reviewItems);
                showAllReview();
            } else {
                mNoReviews.setVisibility(View.VISIBLE);
            }

        }
    }
}
