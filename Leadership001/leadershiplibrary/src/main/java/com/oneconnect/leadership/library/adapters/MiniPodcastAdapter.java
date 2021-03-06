package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        final MiniPodcastViewHolder pvh = (MiniPodcastViewHolder) holder;
        int i = v.getStorageName().lastIndexOf("/");
        String newName = null;/*=  v.getStorageName();*/
        int spacePos = v.getStorageName().indexOf(".mp3");
        if (spacePos > 0) {
             newName = v.getStorageName().substring(0, spacePos - 1);
        }


        if (newName/*v.getStorageName()*/ != null) {
        pvh.podcastNametxt.setText(newName/*v.getStorageName()*/.substring(i + 1));
        }
        pvh.podcastCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPodcastRequired(v);
            }
        });
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


    public class MiniPodcastViewHolder extends RecyclerView.ViewHolder {
        protected TextView podcastNametxt;
        protected ImageView image, playIMG, pauseIMG, stopIMG,headerpic;
        protected Button btnPlay, btnUpload;
        protected LinearLayout mediaControlLayout;
        protected CardView podcastCard;

        public MiniPodcastViewHolder(View itemView) {
            super(itemView);
            podcastNametxt = (TextView) itemView.findViewById(R.id.podcastNametxt);
            headerpic = (ImageView) itemView.findViewById(R.id.headerpic);
            playIMG = (ImageView) itemView.findViewById(R.id.playIMG);
            playIMG.setVisibility(View.GONE);
            pauseIMG = (ImageView) itemView.findViewById(R.id.pauseIMG);
            pauseIMG.setVisibility(View.GONE);
            stopIMG = (ImageView) itemView.findViewById(R.id.stopIMG);
            stopIMG.setVisibility(View.GONE);
            mediaControlLayout = (LinearLayout) itemView.findViewById(R.id.mediaControlLayout);
            mediaControlLayout.setVisibility(View.GONE);
            podcastCard = (CardView) itemView.findViewById(R.id.podcastCard);

            /*mediaPlayer = new MediaPlayer();*/
            //mediaController = new MediaController(ctx);
            //mediaController.setAnchorView(image);
        }
    }

    static final String LOG = MiniPodcastAdapter.class.getSimpleName();
}
