package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.PodcastPlayerActivity;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.Util;
import com.oneconnect.leadership.library.video.LeExoPlayerActivity;

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

    boolean isPlaying;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final VideoDTO v = mList.get(position);
        final MiniVideoViewHolder vvh = (MiniVideoViewHolder) holder;
        int i = v.getStorageName().lastIndexOf("/");
        vvh.vidFileNametxt.setText(v.getStorageName().substring(i + 1));

        vvh.vidFileNametxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, PodcastPlayerActivity.class);
                intent.putExtra("video", v);
                ctx.startActivity(intent);
            }
        });
        try {
           // mediaController = new MediaController(ctx);
           // mediaController.setAnchorView(vvh.videoVideoView);
            Uri video = Uri.parse(v.getUrl());
            vvh.videoVideoView.setMediaController(mediaController);
            vvh.videoVideoView.setVideoURI(video);
            vvh.videoVideoView.seekTo(300);
        } catch (Exception e) {
            Log.e(LOG,"Video something went wrong: " + e.getMessage());
        }

        vvh.videoVideoView.requestFocus();
        vvh.videoVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                /*mp.getCurrentPosition();*/
            }
        });

        vvh.videoVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    vvh.pausebtn.setVisibility(View.VISIBLE);
                    vvh.playbtn.setVisibility(View.GONE);
                } else if (!isPlaying) {
                    vvh.pausebtn.setVisibility(View.GONE);
                    vvh.playbtn.setVisibility(View.VISIBLE);
                }
            }
        });

       /* if (isPlaying == true) {
            vvh.pasuebtn.setVisibility(View.VISIBLE);
        }*/

        vvh.playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = true;
                vvh.videoVideoView.start();
                vvh.playbtn.setVisibility(View.GONE);
                vvh.pausebtn.setVisibility(View.VISIBLE);
            }
        });

        vvh.pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = false;
                vvh.videoVideoView.pause();
                vvh.pausebtn.setVisibility(View.GONE);
                vvh.playbtn.setVisibility(View.VISIBLE);

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
    private void shareIt() {
        //sharing implementation here
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Leadership Platform");
        String sAux = "\nLet me recommend you this application\n\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.minisass&hl=en \n\n";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        ctx.startActivity(Intent.createChooser(i, "choose one"));
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MiniVideoViewHolder extends RecyclerView.ViewHolder {
        protected VideoView videoVideoView;
        protected TextView vidFileNametxt;
        protected ImageView playbtn, pausebtn;

        public MiniVideoViewHolder(View itemView) {
            super(itemView);
            pausebtn = (ImageView) itemView.findViewById(R.id.pausebtn);
            pausebtn.setVisibility(View.GONE);
            playbtn = (ImageView) itemView.findViewById(R.id.playbtn);
            videoVideoView = (VideoView) itemView.findViewById(R.id.videoVideoView);
            vidFileNametxt = (TextView) itemView.findViewById(R.id.vidFileNametxt);
        }
    }

    static final String LOG = MiniVideoAdapter.class.getSimpleName();
}
