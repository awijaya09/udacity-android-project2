package com.twiscode.movie_stage1;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.twiscode.movie_stage1.Model.MovieContract;
import com.twiscode.movie_stage1.Model.MovieItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andree on 7/6/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private ArrayList<MovieItem> movies;
    private Cursor mCursor;
    private MovieItem movieItem;

    //Add On click listener
    final private MovieAdapterOnClickHandler mClickHandler;

    interface MovieAdapterOnClickHandler{
        void onItemClick(MovieItem item);
    }

    //constructor of the adapter
    public MovieListAdapter(MovieAdapterOnClickHandler handler) {
        mClickHandler = handler;
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_movie_image) ImageView mMovieImageView;
        public MovieListViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mCursor != null) {
                movieItem = getDataFromCursor(getAdapterPosition());
            } else {
                movieItem = movies.get(getAdapterPosition());
            }
            mClickHandler.onItemClick(movieItem);
        }
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForMovieItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        Boolean shouldAttachToParent = false;

        View view = inflater.inflate(layoutIdForMovieItem, parent, shouldAttachToParent);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        MovieItem movieSingle = null;
        if (null != mCursor) {
            Log.d("Cursor", "onBindViewHolder: cursor called");
            movieSingle = getDataFromCursor(position);
        } else {
            Log.d("Cursor", "onBindViewHolder: movies called");
            movieSingle = movies.get(position);
        }

        //using picasso to load image
        Context context = holder.mMovieImageView.getContext();
        String imageUrl = movieSingle.getImgUrl();
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.mMovieImageView);

    }

    private MovieItem getDataFromCursor(int position) {
        int movieIDIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIEDB_ID);
        int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int descriptionIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION);
        int ratingIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
        int voteIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);
        int imgUrlIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMG_URL);
        int backImgUrlIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_URL);
        int releaseDateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        mCursor.moveToPosition(position);

        int movieID = mCursor.getInt(movieIDIndex);
        String movieTitle = mCursor.getString(titleIndex);
        String movieDes = mCursor.getString(descriptionIndex);
        Double movieRating = mCursor.getDouble(ratingIndex);
        int movieVote = mCursor.getInt(voteIndex);
        String movieImgUrl = mCursor.getString(imgUrlIndex);
        String backImgUrl = mCursor.getString(backImgUrlIndex);
        String releaseDate = mCursor.getString(releaseDateIndex);

        MovieItem movieItem = new MovieItem(movieImgUrl, backImgUrl, movieTitle, movieDes, movieID, movieRating, releaseDate, movieVote);

        return movieItem;
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else if (movies != null){
            return movies.size();
        } else {
            return 0;
        }

    }

    public void setMovieData(ArrayList<MovieItem> movieItems, Cursor cursor){
        movies = movieItems;
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public void addMovieData(ArrayList<MovieItem> newMovieItems) {
        movies.addAll(newMovieItems);
        notifyDataSetChanged();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            movies = null;
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
