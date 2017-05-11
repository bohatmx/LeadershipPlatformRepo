package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PodcastDTO;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nkululeko on 2017/05/10.
 */

public class MiniPodcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MediaPlayer mediaPlayer;
    private List<PodcastDTO> mList;
    private Context ctx;
    private PodcastAdapter.PodcastAdapterListener listener;
    private MediaController mediaController;

    public MiniPodcastAdapter(List<PodcastDTO> mList, Context ctx, PodcastAdapter.PodcastAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_podcast, parent, false);
        return new MiniPodcastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final PodcastDTO v = mList.get(position);
        final PodcastAdapter.PodcastsViewHolder pvh = (PodcastAdapter.PodcastsViewHolder) holder;
        int i = v.getStorageName().lastIndexOf("/");
        pvh.fileName.setText(v.getStorageName().substring(i + 1));
        final String podcastURL = v.getUrl();

        pvh.playIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                pvh.playIMG.setVisibility(View.GONE);
                pvh.pauseIMG.setVisibility(View.VISIBLE);
                pvh.stopIMG.setVisibility(View.VISIBLE);
                try {
                    mediaPlayer.setDataSource(podcastURL);
                } catch (IllegalArgumentException e) {
                    Log.e(LOG, "You might not set the URI correctly!");
                } catch (SecurityException e) {
                    Log.e(LOG, "You might not set the URI correctly!");
                } catch (IllegalStateException e) {
                    Log.e(LOG, "You might not set the URI correctly!");
                } catch (IOException e) {
                    Log.e(LOG, e.getMessage());
                }
                try {
                    mediaPlayer.prepare();
                } catch (IllegalStateException e) {
                    Log.e(LOG, "You might not set the URI correctly!");
                } catch (IOException e) {
                    Log.e(LOG, "You might not set the URI correctly!");
                }
                mediaPlayer.start();
            }
        });

        pvh.headerpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

        pvh.pauseIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                pvh.pauseIMG.setVisibility(View.GONE);
                pvh.playIMG.setVisibility(View.VISIBLE);
                pvh.stopIMG.setVisibility(View.VISIBLE);
            }
        });

        pvh.stopIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                pvh.playIMG.setVisibility(View.VISIBLE);
                pvh.pauseIMG.setVisibility(View.GONE);
                pvh.stopIMG.setVisibility(View.GONE);
            }
        });
    }

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        ctx.startActivity(sharingIntent);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public class MiniPodcastViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileName;
        protected ImageView image, playIMG, pauseIMG, stopIMG,headerpic;
        protected Button btnPlay, btnUpload;

        public MiniPodcastViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            headerpic = (ImageView) itemView.findViewById(R.id.headerpic);
            playIMG = (ImageView) itemView.findViewById(R.id.playIMG);
            pauseIMG = (ImageView) itemView.findViewById(R.id.pauseIMG);
            pauseIMG.setVisibility(View.GONE);
            stopIMG = (ImageView) itemView.findViewById(R.id.stopIMG);
            stopIMG.setVisibility(View.GONE);

            /*mediaPlayer = new MediaPlayer();*/
            //mediaController = new MediaController(ctx);
            //mediaController.setAnchorView(image);
        }
    }

    static final String LOG = MiniPodcastAdapter.class.getSimpleName();
}
