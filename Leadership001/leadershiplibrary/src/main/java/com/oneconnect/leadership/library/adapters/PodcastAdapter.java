package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.Util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nkululeko on 2017/04/19.
 */

public class PodcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PodcastDTO> mList;
    private Context ctx;
    private PodcastAdapterListener listener;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    boolean isPlaying = true;
    private SeekBar seekBar;
    private double startTime = 0;
    private double finalTime = 0;
    private Util util;

    public interface PodcastAdapterListener {
        void onPlayClicked(int position);
    }

    public PodcastAdapter(List<PodcastDTO> mList, Context ctx, PodcastAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        return new PodcastAdapter.PodcastsViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final PodcastDTO v = mList.get(position);
        final PodcastsViewHolder pvh = (PodcastsViewHolder) holder;
        int i = v.getStorageName().lastIndexOf("/");
        pvh.fileName.setText(v.getStorageName().substring(i + 1));
        pvh.image.setImageDrawable(ctx.getDrawable(R.drawable.ic_microphone));
        final String podcastURL = v.getUrl();

        pvh.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
    }

    private TextView  initTimetxt, finalTimetxt;
    private SeekBar seekbar;

    private Runnable upDatePodcastTime = new Runnable() {
        @Override
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();
            //displaying total duration time
            finalTimetxt.setText("" + util.milliSecondsToTimer(totalDuration));
            //displaying time completed playing
            initTimetxt.setText("" + util.milliSecondsToTimer(currentDuration));

            // updating progress bar
            int progress = (int) (util.getProgressPercentage(currentDuration, totalDuration));
            seekbar.setProgress(progress);

            // seekbar.setProgress((int)startTime);

            //running this thread after 100 miliseconds
            handler.postDelayed(this, 100);
        }
    };

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public class PodcastsViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileName;
        protected ImageView image;
        protected Button btnPlay, btnUpload;

        public PodcastsViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnUpload.setVisibility(View.GONE);
            seekbar = (SeekBar) itemView.findViewById(R.id.seekBar);
            initTimetxt = (TextView) itemView.findViewById(R.id.initialTime);
            finalTimetxt = (TextView) itemView.findViewById(R.id.finalTime);


        }
    }

    static final String LOG = PodcastAdapter.class.getSimpleName();

}
