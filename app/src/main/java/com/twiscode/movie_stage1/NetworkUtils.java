package com.twiscode.movie_stage1;

import android.net.Uri;
import android.util.Log;

import com.twiscode.movie_stage1.Model.MovieItem;
import com.twiscode.movie_stage1.Model.ReviewItem;
import com.twiscode.movie_stage1.Model.VideoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Andree on 7/9/17.
 */

public class NetworkUtils {

    final static String MOVIEDB_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";
    final static String MOVIEDB_TOPRATED_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    final static String MOVIEDB_UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=";
    final static String MOVIEDB_QUERY_PAGE = "&page=";
    final static String API_KEY = "48de4eca0f403aabc3c290ae441792a1";
    final static String IMG_BASE_URL = "https://image.tmdb.org/t/p/w500";

    // Video/Trailer link
    final static String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    final static String YOUTUBE_API_KEY = "AIzaSyB1tq0nInntUrIWFIsxPx1qPBZJ-kE3yYo";
    final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    final static String MOVIEDB_VIDEOS_API = "/videos?api_key=";
    final static String MOVIEDB_REVIEWS_API = "/reviews?api_key=";
    final static String MOVIEDB_VIDEOS_LANG = "&language=en-US";


    public static URL buildUrl(String firstUrl, String apiKey, int pageNumber){
        String completeUrl = firstUrl + apiKey + MOVIEDB_QUERY_PAGE + pageNumber;
        Uri builtUri = Uri.parse(completeUrl).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildVideoRequestUrl(String apiRequestType, String movieID){
        String completeUrl = MOVIEDB_BASE_URL + movieID + apiRequestType + API_KEY + MOVIEDB_VIDEOS_LANG;
        Uri builtUri = Uri.parse(completeUrl).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<MovieItem> getMovieData(URL paramUrl) throws IOException{
        MovieItem newMovie;
        ArrayList<MovieItem> movieLists = new ArrayList<MovieItem>();
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
                        String backdropImgPath = resultItem.getString("backdrop_path");
                        int voteCount = resultItem.getInt("vote_count");
                        String fullImgUrl = NetworkUtils.IMG_BASE_URL + imgPath;
                        String backdropImgUrl = NetworkUtils.IMG_BASE_URL + backdropImgPath;

                        newMovie = new MovieItem(fullImgUrl, backdropImgUrl, itemTitle, itemDesc, itemID, rating, releaseDate, voteCount);

                        movieLists.add(newMovie);

                    }

                    return movieLists;
                } catch (final JSONException e){
                    Log.e("JSON Error", "Json parsing error: " + e.getMessage());
                }
            }
        return movieLists;
    }

    public static ArrayList<VideoItem> getAllVideos(URL paramUrl) throws IOException {
        VideoItem newVideo;
        ArrayList<VideoItem> videoList = new ArrayList<VideoItem>();
        String jsonString = NetworkUtils.getResponseFromHttpUrl(paramUrl);


        if (null != jsonString) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                String movieId = Integer.toString(jsonObject.getInt("id"));
                JSONArray results = jsonObject.getJSONArray("results");

                for(int i = 0; i < results.length(); i++) {
                    JSONObject resultItem = results.getJSONObject(i);
                    String youtubeKey = resultItem.getString("key");
                    String videoId = resultItem.getString("id");
                    String videoName = resultItem.getString("name");

                    newVideo = new VideoItem(videoId, movieId, youtubeKey, videoName);
                    videoList.add(newVideo);
                }

                return videoList;
            } catch (final JSONException e) {
                Log.e("JSON Error", "Json parsing error: " + e.getMessage());
            }

        }
        return videoList;
    }

    public static ArrayList<ReviewItem> getAllReviews(URL paramUrl) throws IOException {
        ReviewItem newReview;
        ArrayList<ReviewItem> reviewList = new ArrayList<ReviewItem>();
        String jsonString = NetworkUtils.getResponseFromHttpUrl(paramUrl);


        if (null != jsonString) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                String movieId = Integer.toString(jsonObject.getInt("id"));
                JSONArray results = jsonObject.getJSONArray("results");

                for(int i = 0; i < results.length(); i++) {
                    JSONObject resultItem = results.getJSONObject(i);
                    String reviewId = resultItem.getString("id");
                    String reviewContent = resultItem.getString("content");
                    String reviewAuthor = resultItem.getString("author");

                    Log.d("Review author", "getAllReviews: Review Author:" + reviewAuthor);
                    newReview = new ReviewItem(movieId, reviewId, reviewContent, reviewAuthor);
                    reviewList.add(newReview);
                }

                return reviewList;
            } catch (final JSONException e) {
                Log.e("JSON Error", "Json parsing error: " + e.getMessage());
            }

        }
        return reviewList;
    }

}
