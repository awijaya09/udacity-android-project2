package com.twiscode.movie_stage1.Model;

import java.io.Serializable;

/**
 * Created by Andree on 8/6/17.
 */

public class VideoItem implements Serializable {

    private String videoId;
    private String movieId;
    private String youtubeKey;
    private String videoTitle;

    public VideoItem(String id, String movieId, String youtubeKey, String videoTitle) {
        this.videoId = id;
        this.movieId = movieId;
        this.youtubeKey = youtubeKey;
        this.videoTitle = videoTitle;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

}
