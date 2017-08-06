package com.twiscode.movie_stage1;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    @BindView(R.id.tv_movie_detail_release_date) TextView mMovieReleaseDate;
    @BindView(R.id.iv_movie_detail_image) ImageView mMovieImage;
    @BindView(R.id.iv_image_banner) ImageView mBannerImage;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.movie_detail_toolbar) Toolbar mToolbar;
    @BindView(R.id.tv_movie_detail_total_vote) TextView mTotalVote;
    @BindView(R.id.rv_videos) RecyclerView mRecyclerViewVideos;
    @BindView(R.id.pb_video_loading_indicator) ProgressBar mLoadingVideos;

    private ArrayList<VideoItem> videoItems;
    private VideoListAdapter mVideoListAdapter;
    private LinearLayoutManager mLinearLayoutManager;
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
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewVideos.setLayoutManager(mLinearLayoutManager);
        mRecyclerViewVideos.setHasFixedSize(true);
        mVideoListAdapter = new VideoListAdapter();
        mRecyclerViewVideos.setAdapter(mVideoListAdapter);

        loadVideos();

    }

    private void loadVideos() {

        if (null != movieId) {
            URL urlRequest = NetworkUtils.buildVideoRequestUrl(movieId);
            new FetchVideos().execute(urlRequest);
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

            Picasso.with(this).load(item.getImgUrl()).into(mMovieImage);
            Picasso.with(this).load(item.getBackdropImgUrl()).into(mBannerImage);
        }else{
            Log.d("Intent description", "onCreate: No Intent found");
        }
    }

    private void showVideoLoading() {
        mLoadingVideos.setVisibility(View.VISIBLE);
        mRecyclerViewVideos.setVisibility(View.INVISIBLE);
    }

    private void showAllVideos() {
        mLoadingVideos.setVisibility(View.INVISIBLE);
        mRecyclerViewVideos.setVisibility(View.VISIBLE);
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

            URL paramUrl = urls[0];
            try{
                videoItems.addAll(NetworkUtils.getAllVideos(paramUrl));
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

        }
    }
}
