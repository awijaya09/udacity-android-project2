package com.twiscode.movie_stage1.Model;

import java.io.Serializable;

/**
 * Created by Andree on 7/7/17.
 */

public class MovieItem implements Serializable {

    private String imgUrl;
    private String backdropImgUrl;
    private String movieTitle;
    private double movieRating;
    private String movieDescription;
    private String releaseDate;
    private int voteCount;
    private int movieID;

    public MovieItem(String imgUrl, String backImg, String title, String desc, int id, double rating, String releaseDate, int voteCount){
        this.imgUrl = imgUrl;
        this.backdropImgUrl = backImg;
        this.movieTitle = title;
        this.movieRating = rating;
        this.movieDescription = desc;
        this.releaseDate = releaseDate;
        this.movieID = id;
        this.voteCount = voteCount;

    }
    public int getVoteCount() { return voteCount; }

    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBackdropImgUrl() { return backdropImgUrl; }

    public void setBackdropImgUrl(String backdropImgUrl) {
        this.backdropImgUrl = backdropImgUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(double movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getReleaseDate() { return releaseDate; }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }
}
