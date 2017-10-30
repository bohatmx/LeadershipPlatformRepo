package com.oneconnect.leadership.library.activities;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.MiniPhotoAdapter;
import com.oneconnect.leadership.library.adapters.MiniPodcastAdapter;
import com.oneconnect.leadership.library.adapters.MiniVideoAdapter;
import com.oneconnect.leadership.library.adapters.PhotoAdapter;
import com.oneconnect.leadership.library.adapters.PodcastAdapter;
import com.oneconnect.leadership.library.adapters.UrlAdapter;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;
import com.oneconnect.leadership.library.util.Util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PodcastPlayerActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

     TextView fileName,count, txtSubtitle, txtLinks, txtMicrophone, txtVideo, txtCamera, urlTxt, podcastfileName,textCurrentPosition,
             textView_maxTime, textView2;
     ImageView image, overflow, playbtn, pausebtn, stopbtn, rewindIMG, fowardIMG,podcastIMGAE, iconUpdate, iconDelete, iconMicrophone,
             iconVideo, iconCamera, photoView, iconShare;
     Button btnPlay;
     VideoView videoView;
     SeekBar videoSeekBar;
     RelativeLayout bottomLayout;
    PodcastDTO podcast;
    VideoDTO video;
    Context ctx;
    MediaController mediaController;
    RecyclerView urlRecyclerView, imageRecyclerView, videoRecyclerView, podcastRecyclerView;
    UrlAdapter urlAdapter;
    Toolbar toolbar;

    RelativeLayout urlAdapterLayout, videoAdapterLayout, deleteLayout, podcastAdapterLayout;
    LinearLayout podcastPlayerLayout;
    private Handler threadHandler = new Handler();


    private int startTime = 0;
    private int finalTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_player);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();

        fileName = (TextView) findViewById(R.id.fileName);
        image = (ImageView) findViewById(R.id.image);
        podcastIMGAE = (ImageView) findViewById(R.id.podcastIMGAE);
        podcastIMGAE.setVisibility(View.GONE);
        textCurrentPosition = (TextView) findViewById(R.id.textCurrentPosition);
        textView_maxTime = (TextView) findViewById(R.id.textView_maxTime);
        //url
        urlAdapterLayout = (RelativeLayout) findViewById(R.id.urlAdapterLayout);
        urlRecyclerView = (RecyclerView) findViewById(R.id.urlRecyclerView);
        urlTxt = (TextView) findViewById(R.id.urlTxt);
        urlRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager llm3 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        urlRecyclerView.setLayoutManager(llm3);
        urlRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
        urlRecyclerView.setHasFixedSize(true);
        textView2 = (TextView) findViewById(R.id.textView2);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        videoView = (VideoView) findViewById(R.id.videoView);
        overflow = (ImageView) findViewById(R.id.overflow);
        overflow.setVisibility(View.GONE);
        count = (TextView) findViewById(R.id.fileName);
        playbtn = (ImageView) findViewById(R.id.playIMG);
        playbtn.setVisibility(View.GONE);
        pausebtn = (ImageView) findViewById(R.id.pauseIMG);
        pausebtn.setVisibility(View.GONE);
        stopbtn = (ImageView) findViewById(R.id.stopIMG);
        stopbtn.setVisibility(View.GONE);
        rewindIMG = (ImageView) findViewById(R.id.rewindIMG);
        rewindIMG.setVisibility(View.GONE);
        fowardIMG = (ImageView) findViewById(R.id.fowardIMG);
        fowardIMG.setVisibility(View.GONE);
        txtLinks = (TextView) findViewById(R.id.txtLinks);
        txtMicrophone = (TextView) findViewById(R.id.txtMicrophone);
        txtVideo = (TextView) findViewById(R.id.txtVideo);
        txtCamera = (TextView) findViewById(R.id.txtCamera);
        iconMicrophone = (ImageView) findViewById(R.id.iconMicrophone);
        iconVideo = (ImageView) findViewById(R.id.iconVideo);
        iconCamera = (ImageView) findViewById(R.id.iconCamera);
        iconUpdate = (ImageView) findViewById(R.id.iconUpdate);

        videoSeekBar = (SeekBar) findViewById(R.id.videoSeekBar);
        videoSeekBar.setOnSeekBarChangeListener(this);
        videoSeekBar.setClickable(false);
       // videoSeekBar.setVisibility(View.GONE);

        //photo
        photoView = (ImageView) findViewById(R.id.photoView);
        imageRecyclerView = (RecyclerView) findViewById(R.id.imageRecyclerView);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));
        imageRecyclerView.setVisibility(View.GONE);

        //video
        videoAdapterLayout = (RelativeLayout) findViewById(R.id.videoAdapterLayout);
        videoRecyclerView = (RecyclerView) findViewById(R.id.videoRecyclerView);
        videoRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager llm2 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        videoRecyclerView.setLayoutManager(llm2);
        videoRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
        videoRecyclerView.setHasFixedSize(true);

        //podcast
        podcastRecyclerView = (RecyclerView) findViewById(R.id.podcastRecyclerView);
        podcastRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager llm4 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        podcastRecyclerView.setLayoutManager(llm4);
        podcastRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
        podcastRecyclerView.setHasFixedSize(true);
        // layouts
        podcastAdapterLayout = (RelativeLayout) findViewById(R.id.podcastAdapterLayout);
        podcastfileName = (TextView) findViewById(R.id.fileName);
        //

        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        bottomLayout.setVisibility(View.GONE);

        podcastPlayerLayout = (LinearLayout) findViewById(R.id.podcastPlayerLayout);

        iconShare = (ImageView) findViewById(R.id.iconShare);

        deleteLayout = (RelativeLayout) findViewById(R.id.deleteLayout);
        deleteLayout.setVisibility(View.GONE);


        if (getIntent().getSerializableExtra("video") != null) {
           // type = ResponseBag.VIDEOS;
            video = (VideoDTO) getIntent().getSerializableExtra("video");
          //  getSupportActionBar().setSubtitle(video.getStorageName());
            playVideo();
        }
        if (getIntent().getSerializableExtra("podcast") != null) {
          //  type = ResponseBag.URLS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");

            playPodcast();
        }

    }

    DecimalFormat timeFormat = new DecimalFormat("#.00");


    //convert millisecond to string
    private String millisecondsToString(int milliseconds) {
        long minutes = TimeUnit.MINUTES.toMinutes((long) milliseconds);
        long seconds = TimeUnit.SECONDS.toSeconds((long) milliseconds);
        timeFormat.format(minutes);
        timeFormat.format(seconds);
        return minutes + ":" + seconds;
    }

 @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

         if (fromUser) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(progress);
                } else {
                    try {
                        mediaPlayer.start();
                        mediaPlayer.seekTo(progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    String url;
    private void playVideo() {
       bottomLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        pausebtn.setVisibility(View.GONE);
        videoSeekBar.setVisibility(View.GONE);
        textCurrentPosition.setVisibility(View.GONE);
        textView_maxTime.setVisibility(View.GONE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        try {
            mediaPlayer = new MediaPlayer();
             mediaController = new MediaController(PodcastPlayerActivity.this/*ctx*/);
             mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            Uri videoURL = Uri.parse(video.getUrl());
            //  vvh.videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoURL);
            videoView.seekTo(300);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaController.show();
                }
            });

            /*videoView.start();*/
        } catch (Exception e) {
            Log.e(LOG,"Video something went wrong: " + e.getMessage());
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
              //  videoView.seekTo(position);
                videoView.start();
                videoSeekBar.setMax(videoView.getDuration());
                // vvh.videoSeekBar.postDelayed(onEverySecond, 1000);

                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });


    }
    private MediaPlayer mediaPlayer;
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            finalTime = mediaPlayer.getDuration();
            videoSeekBar.setMax(finalTime);
            textCurrentPosition.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            videoSeekBar.setProgress((int)startTime);
            threadHandler.postDelayed(this, 50);
        }
    };

    private void playPodcast() {
        videoView.setVisibility(View.GONE);
        pausebtn.setVisibility(View.VISIBLE);
        stopbtn.setVisibility(View.GONE);
        fowardIMG.setVisibility(View.VISIBLE);
        rewindIMG.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(podcast.getTitle());
        podcastIMGAE.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(podcast.getUrl());

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
        startTime = mediaPlayer.getCurrentPosition();
        finalTime = mediaPlayer.getDuration();
        videoSeekBar.setMax(finalTime);
        videoSeekBar.setProgress((int) startTime);
        threadHandler.postDelayed(UpdateSongTime,50);

        textView_maxTime.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );
        textCurrentPosition.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );

        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.pause();
                pausebtn.setVisibility(View.GONE);
                playbtn.setVisibility(View.VISIBLE);
                stopbtn.setVisibility(View.GONE);
                rewindIMG.setVisibility(View.VISIBLE);
                fowardIMG.setVisibility(View.VISIBLE);
            }
        });


        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.start();
                playbtn.setVisibility(View.GONE);
                rewindIMG.setVisibility(View.VISIBLE);
                fowardIMG.setVisibility(View.VISIBLE);
                pausebtn.setVisibility(View.VISIBLE);
                stopbtn.setVisibility(View.GONE);

            }
        });

        fowardIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        rewindIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
       // mediaPlayer.reset();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.pause();/*.stop();*/
      //  finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        playbtn.setVisibility(View.GONE);
        pausebtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // cleanUp();
        if (mediaPlayer != null && mediaPlayer.isPlaying() && this.isFinishing())
        {
            mediaPlayer./*stop*/pause();
            mediaPlayer.release();
            mediaPlayer = null;

        }

       /* if (this.isFinishing()){ //basically BACK was pressed from this activity
            mediaPlayer.stop();
           Toast.makeText(PodcastPlayerActivity.this, "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY", Toast.LENGTH_SHORT).show();
        }*/

        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
               mediaPlayer./*stop*/pause();
        //       Toast.makeText(PodcastPlayerActivity.this, "YOU LEFT YOUR APP", Toast.LENGTH_SHORT).show();
            }
            else {
        //       Toast.makeText(PodcastPlayerActivity.this, "YOU SWITCHED ACTIVITIES WITHIN YOUR APP", Toast.LENGTH_SHORT).show();
            }
        }
        super.onPause();
    }

    static final String LOG = PodcastPlayerActivity.class.getSimpleName();
}
