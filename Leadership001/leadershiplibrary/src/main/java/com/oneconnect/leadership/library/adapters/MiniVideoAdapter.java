package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.util.List;

/**
 * Created by Nkululeko on 2017/05/11.
 */

public class MiniVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoDTO> mList;
    private Context ctx;
    private MiniVideoAdapterListener listener;

    public interface MiniVideoAdapterListener {
        void onStart();
        void onPause();
        void onStop();
    }

    public MiniVideoAdapter(List<VideoDTO> mList, Context ctx, MiniVideoAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    MediaController mediaController;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_video_item, parent, false);
        return new MiniVideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final VideoDTO v = mList.get(position);
        final MiniVideoViewHolder vvh = (MiniVideoViewHolder) holder;
        int i = v.getStorageName().lastIndexOf("/");
        vvh.vidFileNametxt.setText(v.getStorageName().substring(i + 1));
        try {
            mediaController = new MediaController(ctx);
            mediaController.setAnchorView(vvh.videoVideoView);
            Uri video = Uri.parse(v.getUrl());
            vvh.videoVideoView.setMediaController(mediaController);
            vvh.videoVideoView.setVideoURI(video);
            vvh.videoVideoView.seekTo(100);
        } catch (Exception e) {
            Log.e(LOG,"Video something went wrong: " + e.getMessage());
        }

        vvh.videoVideoView.requestFocus();
        vvh.videoVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });

       /* vvh.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vvh.videoVideoView.start();
                //listener.onPlayClicked(s);
            }
        });

        vvh.shareicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MiniVideoViewHolder extends RecyclerView.ViewHolder {
        protected VideoView videoVideoView;
        protected TextView vidFileNametxt;

        public MiniVideoViewHolder(View itemView) {
            super(itemView);
            videoVideoView = (VideoView) itemView.findViewById(R.id.videoVideoView);
            vidFileNametxt = (TextView) itemView.findViewById(R.id.vidFileNametxt);
        }
    }

    static final String LOG = MiniVideoAdapter.class.getSimpleName();
}
