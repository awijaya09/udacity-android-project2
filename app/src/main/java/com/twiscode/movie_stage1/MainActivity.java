package com.twiscode.movie_stage1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieAdapterOnClickHandler {

    private MovieListAdapter adapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorText;
    private RecyclerView mRecyclerView;
    private ArrayList<MovieItem> movieLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorText = (TextView) findViewById(R.id.tv_error_message);

        movieLists = new ArrayList<MovieItem>();
        // Setting the recycler view
        int noColumn = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, noColumn);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(adapter);

        loadMoviesData(0);
        setTitle("Upcoming Movies");

    }

    private void loadMoviesData(int movieType){
        showLoading();
        if (movieType == R.id.popular) {
            URL urlRequest = NetworkUtils.buildUrl(NetworkUtils.MOVIEDB_POPULAR_URL, NetworkUtils.API_KEY);
            new FetchMovies().execute(urlRequest);
            setTitle("Popular Movies");
        } else if (movieType == R.id.top_rated) {
            URL urlRequest = NetworkUtils.buildUrl(NetworkUtils.MOVIEDB_TOPRATED_URL, NetworkUtils.API_KEY);
            new FetchMovies().execute(urlRequest);
            setTitle("Top Rated Movies");
        } else {
            URL urlRequest = NetworkUtils.buildUrl(NetworkUtils.MOVIEDB_UPCOMING_URL, NetworkUtils.API_KEY);
            new FetchMovies().execute(urlRequest);
        }

    }

    @Override
    public void onItemClick(MovieItem item) {
        Context context = this;
        Class movieDetail = MovieDetailActivity.class;

        // Passing Movie Object to another activity
        Intent intent = new Intent(context, movieDetail);
        intent.putExtra("MovieItem", item);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    public void showAllMovies(){
        mErrorText.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }

    public void showLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public class FetchMovies extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {

            if (urls.length == 0) { return null; }

            URL paramUrl = urls[0];
            try {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(paramUrl);

                if (null != jsonString) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonString);

                        JSONArray results = jsonObj.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++){
                            JSONObject resultItem = results.getJSONObject(i);

                            String itemTitle = resultItem.getString("title");
                            String imgPath = resultItem.getString("poster_path");
                            int itemID = resultItem.getInt("id");
                            double rating = resultItem.getDouble("vote_average");
                            String releaseDate = resultItem.getString("release_date");
                            String itemDesc = resultItem.getString("overview");

                            String fullImgUrl = NetworkUtils.IMG_BASE_URL + imgPath;

                            MovieItem newMovie = new MovieItem(fullImgUrl, itemTitle, itemDesc, itemID, rating, releaseDate);

                            movieLists.add(newMovie);
                        }
                    } catch (final JSONException e){
                        Log.e("JSON Error", "Json parsing error: " + e.getMessage());
                    }
                }

            } catch (IOException e) {
                Log.e("Networking Error", "doInBackground: error in getting JSON" + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieLists != null || !movieLists.isEmpty()){
                showAllMovies();
                adapter.setMovieData(movieLists);
            } else {
                showErrorMessage();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movies, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular) {
            showLoading();
            adapter.setMovieData(null);
            movieLists.clear();
            loadMoviesData(R.id.popular);
            return true;
        } else if (id == R.id.top_rated) {
            showLoading();
            adapter.setMovieData(null);
            movieLists.clear();
            loadMoviesData(R.id.top_rated);
            return true;
        } else {
            return false;
        }
    }
}
