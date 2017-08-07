package com.twiscode.movie_stage1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.twiscode.movie_stage1.Model.MovieContract;
import com.twiscode.movie_stage1.Model.MovieItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private MovieListAdapter adapter;
    private RecyclerView mRecyclerView;
    private ArrayList<MovieItem> movieLists;
    private int currentPage = 1;
    private GridLayoutManager mGridLayoutManager;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loadingNewItem = true;
    private int pageID;
    private Cursor mMovieData;
    private static final int TASK_LOADER_ID = 1;

    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message) TextView mErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);

        // Setting the recycler view
        movieLists = new ArrayList<MovieItem>();
        mGridLayoutManager = new GridLayoutManager(this, getNoOfColumns());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if(animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        pageID = 0;

        loadMoviesData(pageID);

        addPagination();
        setTitle("Upcoming Movies");

    }

    private void loadMoviesData(int movieType){
        showLoading();
        if (movieType == R.id.popular) {
            URL urlRequest = NetworkUtils.buildUrl(NetworkUtils.MOVIEDB_POPULAR_URL, NetworkUtils.API_KEY, currentPage);
            new FetchMovies().execute(urlRequest);
            setTitle("Popular Movies");
        } else if (movieType == R.id.top_rated) {
            URL urlRequest = NetworkUtils.buildUrl(NetworkUtils.MOVIEDB_TOPRATED_URL, NetworkUtils.API_KEY, currentPage);
            new FetchMovies().execute(urlRequest);
            setTitle("Top Rated Movies");
        } else if (movieType == R.id.favourite){
            getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
            setTitle("Favourite Movies");
        } else {
            URL urlRequest = NetworkUtils.buildUrl(NetworkUtils.MOVIEDB_UPCOMING_URL, NetworkUtils.API_KEY, currentPage);
            new FetchMovies().execute(urlRequest);
            setTitle("Upcoming Movies");
        }

    }
    private void addPagination(){

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0){
                    visibleItemCount = mGridLayoutManager.getChildCount();
                    totalItemCount = mGridLayoutManager.getItemCount();
                    pastVisibleItems = mGridLayoutManager.findFirstVisibleItemPosition();

                    if (loadingNewItem) {
                        if ( (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loadingNewItem = false;
                            loadMoviesData(pageID);
                        }
                    }
                }
            }
        });
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

    private void showAllMovies(){
        mErrorText.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }

    private void showLoading(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private int getNoOfColumns(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width/widthDivider ;
        if (nColumns < 2) return 2;

        return nColumns;
    }

    public class FetchMovies extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (currentPage == 1){
                showLoading();
            }
        }

        @Override
        protected String doInBackground(URL... urls) {

            if (urls.length == 0) { return null; }

            URL paramUrl = urls[0];
            try{
                movieLists.addAll(NetworkUtils.getMovieData(paramUrl));
            } catch (IOException e) {
                Log.e("Networking Error", "doInBackground: error in getting JSON: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (movieLists != null && !movieLists.isEmpty()){
                showAllMovies();
                currentPage += 1;
                loadingNewItem = true;
                adapter.setMovieData(movieLists, null);
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
            resetAllData(id);
            return true;
        } else if (id == R.id.top_rated) {
            resetAllData(id);
            return true;
        } else if (id == R.id.upcoming) {
            resetAllData(id);
            return true;
        } else if (id == R.id.favourite) {
            resetAllData(id);
            return true;
        } else {
            return false;
        }
    }

    private void resetAllData(int id){
        showLoading();
        adapter.setMovieData(null, null);
        movieLists.clear();
        pageID = id;
        currentPage = 1;
        loadMoviesData(id);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                showLoading();
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

                } catch (Exception e) {
                    Log.e("Loader Manager Failed", "loadInBackground: Failed to load data" );
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                super.deliverResult(data);
                showAllMovies();
                currentPage += 1;
                loadingNewItem = true;
                mMovieData = data;
                adapter.setMovieData(null, mMovieData);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (null != mMovieData) {
            adapter.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       adapter.swapCursor(null);
    }
}
