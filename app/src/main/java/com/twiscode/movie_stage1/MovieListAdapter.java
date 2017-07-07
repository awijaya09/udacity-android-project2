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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Andree on 7/6/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    public MovieItem[] movies;
    private String[] dummyText = {"Text 1", "Text 2", "Text 3", "Text 4"};

    class MovieListViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMovieImageView;
        public final TextView mMovieTitle;

        public MovieListViewHolder (View view){
            super(view);

            mMovieImageView = (ImageView) view.findViewById(R.id.iv_movie_image);
            mMovieTitle = (TextView) view.findViewById(R.id.tv_movie_title);
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
        //MovieItem movieSingle = movies[position];
        //String movieTitle = movieSingle.getMovieTitle();
        String textSingle = dummyText[position];
        holder.mMovieTitle.setText(textSingle);

    }

    @Override
    public int getItemCount() {
//        if (null == movies) return 0;
//        return movies.length;
        return dummyText.length;
    }

    public void setMovieData(MovieItem[] movieItems){
        movies = movieItems;
        notifyDataSetChanged();
    }
}
