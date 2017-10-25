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


    private double startTime = 0;
    private double finalTime = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    public static int oneTimeOnly = 0;

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
        /*long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) milliseconds);*/
        timeFormat.format(minutes);
        timeFormat.format(seconds);
        return minutes + ":" + seconds;
    }

 /*   public void doStart(View view)  {
        // The duration in milliseconds
        int duration = this.mediaPlayer.getDuration();

        int currentPosition = this.mediaPlayer.getCurrentPosition();
        if(currentPosition== 0)  {
            this.videoSeekBar.setMax(duration);
            String maxTimeString = this.millisecondsToString(duration);
            this.textView_maxTime.setText(maxTimeString);
        } else if(currentPosition== duration)  {
            // Resets the MediaPlayer to its uninitialized state.
            this.mediaPlayer.reset();
        }
        this.mediaPlayer.start();
        // Create a thread to update position of SeekBar.
       // UpdateSeekBarThread updateSeekBarThread= new UpdateSeekBarThread();
        threadHandler.postDelayed(UpdateSongTime,50);


      //  this.buttonPause.setEnabled(true);
      //  this.buttonStart.setEnabled(false);
    }*/

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mediaPlayer.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    // Thread to Update position for SeekBar.
    /*class UpdateSeekBarThread implements Runnable {

        public void run()  {
            int currentPosition = mediaPlayer.getCurrentPosition();
            timeFormat.format(currentPosition);
            String currentPositionStr = millisecondsToString(currentPosition);
            textCurrentPosition.setText(currentPositionStr);


            videoSeekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }
    }*/

    String url;
    private void playVideo() {
       // videoView.setVisibility(View.VISIBLE);
       // showFullView();
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        */bottomLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        videoSeekBar.setVisibility(View.GONE);
        textCurrentPosition.setVisibility(View.GONE);
        textView_maxTime.setVisibility(View.GONE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (video.getPodcasts() != null) {
            txtMicrophone.setText("" + video.getPodcasts().size());
            iconMicrophone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (podcastAdapterLayout.getVisibility() == View.GONE){
                        podcastAdapterLayout.setVisibility(View.VISIBLE);
                        podcastRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        podcastAdapterLayout.setVisibility(View.GONE);
                        podcastRecyclerView.setVisibility(View.GONE);
                    }
                    VideoDTO dtd = video;
                    List<PodcastDTO> podcastList = new ArrayList<>();
                    Map map = dtd.getPodcasts();
                    PodcastDTO vDTO;
                    ;
                    for (Object value : map.values()) {
                        vDTO = (PodcastDTO) value;
                        url = vDTO.getUrl();
                        podcastList.add(vDTO);
                        //
                        int i = vDTO.getStorageName().lastIndexOf("/");
                        podcastfileName.setText(vDTO.getStorageName().substring(i + 1));
                        //
                        /*playIMG.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {*/
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                try {
                                    mediaPlayer.setDataSource(url);
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

                    miniPodcastAdapter = new MiniPodcastAdapter(podcastList, ctx, new PodcastAdapter.PodcastAdapterListener() {


                        @Override
                        public void onPlayClicked(PodcastDTO podcast) {

                        }

                        @Override
                        public void onPodcastRequired(PodcastDTO podcast) {

                        }
                    });
                    podcastRecyclerView.setAdapter(miniPodcastAdapter);
                }
            });

        }

        if (video.getPhotos() != null) {
            txtCamera.setText("" + video.getPhotos().size());

            VideoDTO dtd = video;
            List<PhotoDTO> urlList = new ArrayList<>();

            Map map = dtd.getPhotos();
            PhotoDTO vDTO;
            String photoUrl;
            for (Object value : map.values()) {
                vDTO = (PhotoDTO) value;
                photoUrl = vDTO.getUrl();
                urlList.add(vDTO);

                Glide.with(ctx)
                        .load(photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(photoView);
            }

            miniPhotoAdapter = new MiniPhotoAdapter(urlList, ctx, new PhotoAdapter.PhotoAdapterlistener() {
                @Override
                public void onPhotoClicked(PhotoDTO photo) {

                }
            });
            imageRecyclerView.setAdapter(miniPhotoAdapter);

        }



        if (video.getUrls() != null) {
            txtLinks.setText("" + video.getUrls().size());
            iconUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (urlAdapterLayout.getVisibility() == View.GONE){
                        urlAdapterLayout.setVisibility(View.VISIBLE);
                        urlRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        urlAdapterLayout.setVisibility(View.GONE);
                        urlRecyclerView.setVisibility(View.GONE);
                    }

                    VideoDTO dtd = video;
                    Map map = dtd.getUrls();
                    UrlDTO vDTO;
                    final List<UrlDTO> urlList = new ArrayList<>();
                    String url;
                    for (Object value : map.values()) {
                        vDTO = (UrlDTO) value;
                        url = vDTO.getUrl();
                        urlTxt.setText(url);
                        urlList.add(vDTO);
                    }

                    urlAdapter = new UrlAdapter(urlList, ctx, new UrlAdapter.UrlAdapterListener() {
                        @Override
                        public void onUrlClicked(final String url) {
                        }
                    });

                    urlRecyclerView.setAdapter(urlAdapter);
                }
            });
        }

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
    MiniPhotoAdapter miniPhotoAdapter;
    MiniVideoAdapter miniVideoAdapter;
    MiniPodcastAdapter miniPodcastAdapter;

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
      //  mediaPlayer.start();

        int duration = mediaPlayer.getDuration();

        int currentPosition = mediaPlayer.getCurrentPosition();
        /*if(currentPosition== 0)  {
            timeFormat.format(currentPosition);
            videoSeekBar.setMax(duration);
           // timeFormat.format(duration);
            String maxTimeString = millisecondsToString(duration);
            textView_maxTime.setText(maxTimeString);

           // timeFormat.format(seconds);
        } else if(currentPosition== duration)  {
            // Resets the MediaPlayer to its uninitialized state.
            mediaPlayer.reset();
        }*/
       // videoSeekBar.setProgress(0/*duration*//*(int)startTime*/);
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            videoSeekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

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

        videoSeekBar.setProgress((int) startTime);
        threadHandler.postDelayed(UpdateSongTime,50);
        fowardIMG.setEnabled(true);
        stopbtn.setEnabled(false);

        // Create a thread to update position of SeekBar.
        /*UpdateSeekBarThread updateSeekBarThread= new UpdateSeekBarThread();
        threadHandler.postDelayed(updateSeekBarThread,50);*/

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

        /*stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        mediaPlayer.stop();
                      //  mediaPlayer.reset();
                        stopbtn.setVisibility(View.GONE);
                        pausebtn.setVisibility(View.GONE);
                        rewindIMG.setVisibility(View.VISIBLE);
                         fowardIMG.setVisibility(View.VISIBLE);
                        playbtn.setVisibility(View.VISIBLE);
            }
        });*/

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

        if (podcast.getPhotos() != null) {
            txtCamera.setText("" + podcast.getPhotos().size());

            PodcastDTO dtd = podcast;
            List<PhotoDTO> urlList = new ArrayList<>();

            Map map = dtd.getPhotos();
            PhotoDTO vDTO;
            String photoUrl;
            for (Object value : map.values()) {
                vDTO = (PhotoDTO) value;
                photoUrl = vDTO.getUrl();
                urlList.add(vDTO);

                Glide.with(ctx)
                        .load(photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(photoView);
            }

            miniPhotoAdapter = new MiniPhotoAdapter(urlList, ctx, new PhotoAdapter.PhotoAdapterlistener() {
                @Override
                public void onPhotoClicked(PhotoDTO photo) {

                }
            });
            imageRecyclerView.setAdapter(miniPhotoAdapter);

        }

        if (podcast.getVideos() != null) {
            txtVideo.setText("" + podcast.getVideos().size());
            iconVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoAdapterLayout.getVisibility() == View.GONE){
                        videoAdapterLayout.setVisibility(View.VISIBLE);
                        videoRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        videoAdapterLayout.setVisibility(View.GONE);
                        videoRecyclerView.setVisibility(View.GONE);
                    }

                    PodcastDTO dtd = podcast;
                    List<VideoDTO> videoList = new ArrayList<>();
                    Map map = dtd.getVideos();
                    VideoDTO vDTO = new VideoDTO();
                    for (Object value : map.values()) {
                        vDTO = (VideoDTO) value;
                        videoList.add(vDTO);
                    }

                    miniVideoAdapter = new MiniVideoAdapter(videoList, ctx, new MiniVideoAdapter.MiniVideoAdapterListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onPause() {

                        }

                        @Override
                        public void onStop() {

                        }
                    });

                    videoRecyclerView.setAdapter(miniVideoAdapter);
                    btnPlay.setVisibility(View.GONE);

                    btnPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            });
        }

        if (podcast.getUrls() != null) {
            txtLinks.setText("" + podcast.getUrls().size());
            iconUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (urlAdapterLayout.getVisibility() == View.GONE){
                        urlAdapterLayout.setVisibility(View.VISIBLE);
                        urlRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        urlAdapterLayout.setVisibility(View.GONE);
                        urlRecyclerView.setVisibility(View.GONE);
                    }

                    PodcastDTO dtd = podcast;
                    Map map = dtd.getUrls();
                    UrlDTO vDTO;
                    final List<UrlDTO> urlList = new ArrayList<>();
                    String url;
                    for (Object value : map.values()) {
                        vDTO = (UrlDTO) value;
                        url = vDTO.getUrl();
                        urlTxt.setText(url);
                        urlList.add(vDTO);
                    }

                    urlAdapter = new UrlAdapter(urlList, ctx, new UrlAdapter.UrlAdapterListener() {
                        @Override
                        public void onUrlClicked(final String url) {
                        }
                    });

                    urlRecyclerView.setAdapter(urlAdapter);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        mediaPlayer.reset();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.reset();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.isFinishing()){ //basically BACK was pressed from this activity
            mediaPlayer.stop();
          //  Toast.makeText(PodcastPlayerActivity.this, "YOU PRESSED BACK FROM YOUR 'HOME/MAIN' ACTIVITY", Toast.LENGTH_SHORT).show();
        }
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                mediaPlayer.stop();
            //    Toast.makeText(PodcastPlayerActivity.this, "YOU LEFT YOUR APP", Toast.LENGTH_SHORT).show();
            }
            else {
       //         Toast.makeText(PodcastPlayerActivity.this, "YOU SWITCHED ACTIVITIES WITHIN YOUR APP", Toast.LENGTH_SHORT).show();
            }
        }
        super.onPause();
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
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
    static final String LOG = PodcastPlayerActivity.class.getSimpleName();
}
