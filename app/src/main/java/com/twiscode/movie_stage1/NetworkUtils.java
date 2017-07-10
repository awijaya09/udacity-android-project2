package com.twiscode.movie_stage1;

import android.net.Uri;
import android.util.Log;

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
    final static String API_KEY = "<Enter your API KEY here>";
    final static String IMG_BASE_URL = "https://image.tmdb.org/t/p/w500";


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

                        String fullImgUrl = NetworkUtils.IMG_BASE_URL + imgPath;
                        String backdropImgUrl = NetworkUtils.IMG_BASE_URL + backdropImgPath;

                        newMovie = new MovieItem(fullImgUrl, backdropImgUrl, itemTitle, itemDesc, itemID, rating, releaseDate);

                        movieLists.add(newMovie);

                    }

                    return movieLists;
                } catch (final JSONException e){
                    Log.e("JSON Error", "Json parsing error: " + e.getMessage());
                }
            }
        return movieLists;
    }

}
