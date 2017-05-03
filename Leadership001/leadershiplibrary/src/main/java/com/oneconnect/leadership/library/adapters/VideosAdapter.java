package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.video.LeExoPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nkululeko on 2017/04/19.
 */

public class VideosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoDTO> mList;
    private Context ctx;
    private VideosAdapterListener listener;

    public interface VideosAdapterListener {
        void onPlayClicked(String path);
    }

    public VideosAdapter(List<VideoDTO> mList, Context ctx, VideosAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_item, parent, false);
        return new VideosAdapter.VideosViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final VideoDTO v = mList.get(position);
        final VideosViewHolder vvh = (VideosViewHolder) holder;
        int i = v.getStorageName().lastIndexOf("/");
        vvh.fileName.setText(v.getStorageName().substring(i + 1));
        try {
            /*MediaController mediaController = new MediaController(ctx);*/
            /*mediaController.setAnchorView(vvh.videoView);*/

            Uri video = Uri.parse(v.getUrl());
            vvh.videoView.setMediaController(mediaController);
            vvh.videoView.setVideoURI(video);
        } catch (Exception e) {
            Log.e(LOG,"Video something went wrong: " + e.getMessage());
        }

        vvh.videoView.requestFocus();
        vvh.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });

        vvh.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vvh.videoView.start();
                //listener.onPlayClicked(s);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    MediaController mediaController; /*= new MediaController(ctx);*/
    public class VideosViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileName;
        protected ImageView image;
        protected Button btnPlay;
        protected VideoView videoView;

        public VideosViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);
            mediaController = new MediaController(ctx);
            mediaController.setAnchorView(videoView);

        }
    }
    static final String LOG = VideosAdapter.class.getSimpleName();
}
