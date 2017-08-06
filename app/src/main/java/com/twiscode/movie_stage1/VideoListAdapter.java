package com.twiscode.movie_stage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.twiscode.movie_stage1.Model.VideoItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andree on 8/6/17.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder> {

    private ArrayList<VideoItem> videos;
    private Context context;

    public VideoListAdapter(Context context) {
        this.context = context;

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
    public void onBindViewHolder(final VideoListViewHolder holder, int position) {
        final VideoItem videoItem = videos.get(position);

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.mVideoTitle.setVisibility(View.VISIBLE);
                holder.mTrailerLoadingIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }
        };

        holder.mYoutubeView.initialize(NetworkUtils.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(videoItem.getYoutubeKey());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                holder.mVideoTitle.setText(videoItem.getVideoTitle());
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
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


    class VideoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.youtube_video_thumbnail) YouTubeThumbnailView mYoutubeView;
        @BindView(R.id.tv_movie_video_title) TextView mVideoTitle;
        @BindView(R.id.trailer_card_view) CardView mCardView;
        @BindView(R.id.trailer_loading_indicator) ProgressBar mTrailerLoadingIndicator;

        public VideoListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mYoutubeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            VideoItem videoItem = videos.get(getLayoutPosition());
            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context,
                    NetworkUtils.YOUTUBE_API_KEY,
                    videoItem.getYoutubeKey(),
                    100,
                    true,
                    true);
            context.startActivity(intent);
        }
    }


}
