package com.twiscode.movie_stage1;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andree on 7/6/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private ArrayList<MovieItem> movies;

    //Add On click listener
    final private MovieAdapterOnClickHandler mClickHandler;

    interface MovieAdapterOnClickHandler{
        void onItemClick(MovieItem item);
    }

    //constructor of the adapter
    public MovieListAdapter(MovieAdapterOnClickHandler handler) {
        mClickHandler = handler;
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        @BindView(R.id.iv_movie_image) ImageView mMovieImageView;
        public MovieListViewHolder (View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MovieItem movieItem = movies.get(getAdapterPosition());
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
        MovieItem movieSingle = movies.get(position);

        //using picasso to load image
        Context context = holder.mMovieImageView.getContext();
        String imageUrl = movieSingle.getImgUrl();
        Picasso.with(context)
                .load(imageUrl)
                .into(holder.mMovieImageView);

    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public void setMovieData(ArrayList<MovieItem> movieItems){
        movies = movieItems;
        notifyDataSetChanged();
    }
}
