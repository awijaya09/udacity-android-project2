package com.twiscode.movie_stage1;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Andree on 7/9/17.
 */

public class NetworkUtils {

    final static String MOVIEDB_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";
    final static String MOVIEDB_TOPRATED_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    final static String MOVIEDB_UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=";
    final static String API_KEY = "<input your API KEY here>";
    final static String IMG_BASE_URL = "https://image.tmdb.org/t/p/w500";


    public static URL buildUrl(String firstUrl, String apiKey){
        String completeUrl = firstUrl + apiKey;
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

}
