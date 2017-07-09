package com.twiscode.movie_stage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MovieDetailActivity extends AppCompatActivity {

    TextView mMovieDetailTitle;
    TextView mMovieDetailDesc;
    TextView mMovieDetailRating;
    TextView mMovieReleaseDate;
    ImageView mMovieImage;
    ImageView mBannerImage;

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

        Intent intent = getIntent();

        if (intent.hasExtra("MovieItem")){
            MovieItem item = (MovieItem) intent.getSerializableExtra("MovieItem");
            mMovieDetailTitle.setText(item.getMovieTitle());
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
