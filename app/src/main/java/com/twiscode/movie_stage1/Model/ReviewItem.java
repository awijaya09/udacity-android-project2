package com.twiscode.movie_stage1.Model;

/**
 * Created by Andree on 8/6/17.
 */

public class ReviewItem {

    private String movieId;
    private String reviewId;
    private String reviewContent;
    private String authorName;

    public ReviewItem(String movieId, String reviewId, String reviewContent, String authorName) {
        this.movieId = movieId;
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
        this.authorName = authorName;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


}
