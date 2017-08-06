package com.twiscode.movie_stage1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andree on 8/6/17.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {

    private ArrayList<VideoItem> videos;

    public VideoListAdapter() {

    }

    @Override
    public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForYoutubeItem = R.layout.video_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        Boolean shouldAttachToParent = false;

        View view = inflater.inflate(layoutIdForYoutubeItem, parent, shouldAttachToParent);
        return new VideoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoListViewHolder holder, int position) {
        VideoItem videoItem = videos.get(position);
        holder.mVideoTitle.setText(videoItem.getVideoTitle());
    }

    @Override
    public int getItemCount() {
        if (null == videos) return 0;
        return videos.size();
    }

    public void setVideoData(ArrayList<VideoItem> videoItems) {
        videos = videoItems;
        notifyDataSetChanged();
    }

    class VideoListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.youtube_video_thumbnail) YouTubeThumbnailView mYoutubeView;
        @BindView(R.id.tv_movie_video_title) TextView mVideoTitle;

        public VideoListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initializeVideos();

        }

        private void initializeVideos() {
            mVideoTitle.setVisibility(View.INVISIBLE);
            mYoutubeView.initialize(NetworkUtils.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    VideoItem videoItem = videos.get(getAdapterPosition());
                    if (null != videoItem) {
                        youTubeThumbnailLoader.setVideo(videoItem.getYoutubeKey());
                        mVideoTitle.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        }

    }


}
