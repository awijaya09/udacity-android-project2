package com.twiscode.movie_stage1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import static android.R.attr.id;
import static android.R.attr.rating;

/**
 * Created by Andree on 7/7/17.
 */

public class MovieItem implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imgUrl);
        parcel.writeString(backdropImgUrl);
        parcel.writeString(movieTitle);
        parcel.writeDouble(movieRating);
        parcel.writeString(movieDescription);
        parcel.writeString(releaseDate);
        parcel.writeInt(voteCount);
        parcel.writeInt(movieID);
    }

    private MovieItem (Parcel in) {
        this.imgUrl = in.readString();
        this.backdropImgUrl = in.readString();
        this.movieTitle = in.readString();
        this.movieRating = in.readDouble();
        this.movieDescription = in.readString();
        this.releaseDate = in.readString();
        this.voteCount = in.readInt();
        this.movieID = in.readInt();
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {

        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel);
        }

        @Override
        public MovieItem[] newArray(int i) {
            return new MovieItem[i];
        }
    };

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
