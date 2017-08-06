package com.twiscode.movie_stage1;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_movie_detail_title) TextView mMovieDetailTitle;
    @BindView(R.id.tv_movie_detail_desc) TextView mMovieDetailDesc;
    @BindView(R.id.tv_movie_detail_rating) TextView mMovieDetailRating;
    @BindView(R.id.tv_movie_detail_release_date) TextView mMovieReleaseDate;
    @BindView(R.id.iv_movie_detail_image) ImageView mMovieImage;
    @BindView(R.id.iv_image_banner) ImageView mBannerImage;
    @BindView(R.id.collapsing_toolbar)
    net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.movie_detail_toolbar) Toolbar mToolbar;
    @BindView(R.id.tv_movie_detail_total_vote) TextView mTotalVote;


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
        Intent intent = getIntent();

        if (intent.hasExtra("MovieItem")){
            MovieItem item = (MovieItem) intent.getSerializableExtra("MovieItem");
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
}
