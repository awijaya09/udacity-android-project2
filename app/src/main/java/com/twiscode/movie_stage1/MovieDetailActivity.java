package com.twiscode.movie_stage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twiscode.movie_stage1.Model.MovieContract;
import com.twiscode.movie_stage1.Model.MovieItem;
import com.twiscode.movie_stage1.Model.ReviewItem;
import com.twiscode.movie_stage1.Model.VideoItem;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

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

    @BindView(R.id.fb_add_button) FloatingActionButton mFloatingButton;


    private ArrayList<VideoItem> videoItems;
    private ArrayList<ReviewItem> reviewItems;

    private VideoListAdapter mVideoListAdapter;
    private ReviewListAdapter mReviewListAdapter;

    private LinearLayoutManager mVideoLinearLayoutManager;
    private LinearLayoutManager mReviewLinearLayoutManager;

    private String movieId;
    private MovieItem movieItem;
    private Cursor mMovieData;
    private static final int TASK_LOADER_ID = 0;


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

        //get the movie item object from intent
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

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        loadVideosReviews();

        mFloatingButton.setImageResource(R.drawable.ic_action_add);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFaveButtonClick(view);
            }
        });

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
            movieItem = (MovieItem) intent.getParcelableExtra("MovieItem");
            movieId = Integer.toString(movieItem.getMovieID());
            mMovieDetailTitle.setText(movieItem.getMovieTitle());
            mMovieDetailDesc.setText(movieItem.getMovieDescription());
            mMovieDetailRating.setText(movieItem.getMovieRating() + " /10");

            // Get release date in good string
            String date = Helper.convertStringToDate(movieItem.getReleaseDate());
            mMovieReleaseDate.setText(date);

            // Put thousand separator on the vote count
            String totalVoteCount = String.format("%,d", movieItem.getVoteCount());
            mTotalVote.setText(totalVoteCount + " votes");

            setTitle(" ");
            Picasso.with(this).load(movieItem.getImgUrl()).into(mMovieImage);
            Picasso.with(this).load(movieItem.getBackdropImgUrl()).into(mBannerImage);
        } else {
            Log.d("Intent description", "onCreate: No Intent found");
        }
    }

    public void onFaveButtonClick(View view) {
        Log.e("Error on click", "onFaveButtonClick: Error in content values");

        if (null != movieItem && mMovieData != null) {
            Log.e("Error on data check", "onFaveButtonClick: Error in content values");
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIEDB_ID, movieItem.getMovieID());
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieItem.getMovieTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, movieItem.getMovieDescription());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieItem.getReleaseDate());
            contentValues.put(MovieContract.MovieEntry.COLUMN_IMG_URL, movieItem.getImgUrl());
            contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_URL, movieItem.getBackdropImgUrl());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, movieItem.getMovieRating());
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movieItem.getVoteCount());

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            if ( uri != null ){
                Toast.makeText(getBaseContext(), "Movie has been added to your favourite list", Toast.LENGTH_SHORT).show();
                mFloatingButton.setImageResource(R.drawable.checked);
            } else {
                Toast.makeText(getBaseContext(), "Failed to add movie", Toast.LENGTH_SHORT).show();
            }
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

    // Loader methods implementation


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

    // Loader Manager

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    if (null != movieId) {
                        return getContentResolver().query(MovieContract.MovieEntry.contentItemUri(movieId), null, null, null, null);
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    Log.e("Loader Manager Failed", "loadInBackground: Failed to load data" );
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (null != mMovieData) {
            mMovieData = data;
            mFloatingButton.setImageResource(R.drawable.checked);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieData = null;
    }
}
