package com.twiscode.movie_stage1;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailActivity extends AppCompatActivity {

    private TextView mMovieDetailTitle;
    private TextView mMovieDetailDesc;
    private TextView mMovieDetailRating;
    private TextView mMovieReleaseDate;
    private ImageView mMovieImage;
    private ImageView mBannerImage;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Initialize
        mMovieDetailTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
        mMovieDetailDesc = (TextView) findViewById(R.id.tv_movie_detail_desc);
        mMovieDetailRating = (TextView) findViewById(R.id.tv_movie_detail_rating);
        mMovieReleaseDate = (TextView) findViewById(R.id.tv_movie_detail_release_date);
        mMovieImage = (ImageView) findViewById(R.id.iv_movie_detail_image);
        mBannerImage = (ImageView) findViewById(R.id.iv_image_banner);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();

        if (intent.hasExtra("MovieItem")){
            MovieItem item = (MovieItem) intent.getSerializableExtra("MovieItem");
            mMovieDetailTitle.setText(item.getMovieTitle());
            // mCollapsingToolbar.setTitle(item.getMovieTitle());
            mMovieDetailDesc.setText(item.getMovieDescription());
            mMovieDetailRating.setText("Rating: " + item.getMovieRating());
            mMovieReleaseDate.setText("Released: " + item.getReleaseDate());
            setTitle(" ");
            Picasso.with(this).load(item.getImgUrl()).into(mMovieImage);
            Picasso.with(this).load(item.getBackdropImgUrl()).into(mBannerImage);
        }else{
            Log.d("Intent description", "onCreate: No Intent found");
        }

    }
}
