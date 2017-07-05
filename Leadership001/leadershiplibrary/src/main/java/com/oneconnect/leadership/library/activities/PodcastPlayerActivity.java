package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PodcastPlayerActivity extends AppCompatActivity {

     TextView fileName,count, txtSubtitle, txtLinks, txtMicrophone, txtVideo, txtCamera, urlTxt, podcastfileName;
     ImageView image, overflow, playbtn, pausebtn, podcastIMGAE, iconUpdate, iconDelete, iconMicrophone,
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_player);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        ctx = getApplicationContext();
        fileName = (TextView) findViewById(R.id.fileName);
        image = (ImageView) findViewById(R.id.image);
        podcastIMGAE = (ImageView) findViewById(R.id.podcastIMGAE);
        podcastIMGAE.setVisibility(View.GONE);
        //url
        urlAdapterLayout = (RelativeLayout) findViewById(R.id.urlAdapterLayout);
        urlRecyclerView = (RecyclerView) findViewById(R.id.urlRecyclerView);
        urlTxt = (TextView) findViewById(R.id.urlTxt);
        urlRecyclerView.setVisibility(View.GONE);
        LinearLayoutManager llm3 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        urlRecyclerView.setLayoutManager(llm3);
        urlRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
        urlRecyclerView.setHasFixedSize(true);
        //
        //btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
        //btnPlay.setVisibility(View.GONE);
        bottomLayout = (RelativeLayout) findViewById(R.id.bottomLayout);
        videoView = (VideoView) findViewById(R.id.videoView);
        overflow = (ImageView) findViewById(R.id.overflow);
        overflow.setVisibility(View.GONE);
        count = (TextView) findViewById(R.id.fileName);
        playbtn = (ImageView) findViewById(R.id.playbtn);
        playbtn.setVisibility(View.GONE);
        pausebtn = (ImageView) findViewById(R.id.pausebtn);
        pausebtn.setVisibility(View.GONE);
        playbtn = (ImageView) findViewById(R.id.playbtn);

        txtLinks = (TextView) findViewById(R.id.txtLinks);
        txtMicrophone = (TextView) findViewById(R.id.txtMicrophone);
        txtVideo = (TextView) findViewById(R.id.txtVideo);
        txtCamera = (TextView) findViewById(R.id.txtCamera);
        iconMicrophone = (ImageView) findViewById(R.id.iconMicrophone);
        iconVideo = (ImageView) findViewById(R.id.iconVideo);
        iconCamera = (ImageView) findViewById(R.id.iconCamera);
        iconUpdate = (ImageView) findViewById(R.id.iconUpdate);

        videoSeekBar = (SeekBar) findViewById(R.id.videoSeekBar);
        videoSeekBar.setVisibility(View.GONE);

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

        podcastPlayerLayout = (LinearLayout) findViewById(R.id.podcastPlayerLayout);

        iconShare = (ImageView) findViewById(R.id.iconShare);
        iconShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(iconShare, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showFullView();
                    }
                });
            }
        });

        deleteLayout = (RelativeLayout) findViewById(R.id.deleteLayout);
        deleteLayout.setVisibility(View.GONE);

        videoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showFullView();
                return true;
            }
        });



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

    private void showFullView() {
        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoView.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);*/
        bottomLayout.setVisibility(View.GONE);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width =  metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);
    }

    private void showHalfView() {
        bottomLayout.setVisibility(View.VISIBLE);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) /*videoView*/podcastPlayerLayout.getLayoutParams();
       // params.width =  (int) (300*metrics.density);
        params.height = (int) (250*metrics.density);
        params.leftMargin = 30;
        /*videoView*/podcastPlayerLayout.setLayoutParams(params);
    }



    String url;
    private void playVideo() {
       // videoView.setVisibility(View.VISIBLE);
       // showFullView();
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        */bottomLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);

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
                            //    playIMG.setVisibility(View.GONE);
                            //    pauseIMG.setVisibility(View.VISIBLE);
                            //    stopIMG.setVisibility(View.VISIBLE);
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
                            //}
                       // });
                        //

                        //
                       /* dvh.pauseIMG.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mediaPlayer.pause();
                                dvh.pauseIMG.setVisibility(View.GONE);
                                dvh.playIMG.setVisibility(View.VISIBLE);
                                dvh.stopIMG.setVisibility(View.VISIBLE);
                            }
                        });

                        dvh.stopIMG.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mediaPlayer.stop();
                                dvh.playIMG.setVisibility(View.VISIBLE);
                                dvh.pauseIMG.setVisibility(View.GONE);
                                dvh.stopIMG.setVisibility(View.GONE);
                            }
                        });*/
                        //
                    }

                    miniPodcastAdapter = new MiniPodcastAdapter(podcastList, ctx, new PodcastAdapter.PodcastAdapterListener() {
                        @Override
                        public void onPlayClicked(int position) {

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
                //   dvh.captiontxt.setText(vDTO.getCaption());


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
             mediaController = new MediaController(ctx);
             mediaController.setAnchorView(videoView);
            Uri videoURL = Uri.parse(video.getUrl());
            //  vvh.videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoURL);
            videoView.seekTo(300);
            videoView.start();
        } catch (Exception e) {
            Log.e(LOG,"Video something went wrong: " + e.getMessage());
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoSeekBar.setMax(videoView.getDuration());
                // vvh.videoSeekBar.postDelayed(onEverySecond, 1000);

            }
        });
    }



    private MediaPlayer mediaPlayer;
    MiniPhotoAdapter miniPhotoAdapter;
    MiniVideoAdapter miniVideoAdapter;
    MiniPodcastAdapter miniPodcastAdapter;

    private void playPodcast() {
//        showHalfView();
        videoView.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.leadership_logo);
        getSupportActionBar().setSubtitle(podcast.getTitle());
        podcastIMGAE.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //showHalfView();
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
                //   dvh.captiontxt.setText(vDTO.getCaption());


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

                    /*Intent intent = new Intent(ctx, LeExoPlayerActivity.class);
                    intent.putExtra("video", vDTO);
                    ctx.startActivity(intent);*/

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

        /*try {
             mediaController = new MediaController(ctx);
             mediaController.setAnchorView(videoView);
            Uri podcastURL = Uri.parse(podcast.getUrl());
            //  vvh.videoView.setMediaController(mediaController);
            *//*mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);*//*
            videoView.setVideoURI(podcastURL);
            videoView.seekTo(300);
            videoView.start();
        } catch (Exception e) {
            Log.e(LOG,"podcast something went wrong: " + e.getMessage());
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoSeekBar.setMax(videoView.getDuration());
                // vvh.videoSeekBar.postDelayed(onEverySecond, 1000);

            }
        });*/
    }
    static final String LOG = PodcastPlayerActivity.class.getSimpleName();
}
