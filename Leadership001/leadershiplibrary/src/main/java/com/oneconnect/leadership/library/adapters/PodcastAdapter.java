package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.RatingActivity;
import com.oneconnect.leadership.library.activities.PodcastPlayerActivity;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Nkululeko on 2017/04/19.
 */

public class PodcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MediaController.MediaPlayerControl{

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
    private MediaController mediaController;

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    public interface PodcastAdapterListener {
        void onPodcastRating(PodcastDTO podcast);
        void onPodcastRequired(PodcastDTO podcast);
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
        final String podcastURL = v.getUrl();

        pvh.ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPodcastRating(v);
            }
        });

        pvh.audio_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPodcastRequired(v);
                /*Intent intent = new Intent(ctx, PodcastPlayerActivity.class);
                intent.putExtra("podcast", v);
                ctx.startActivity(intent);*/

            }
        });
        /*pvh.fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPodcastRequired(v);
               *//* Intent intent = new Intent(ctx, PodcastPlayerActivity.class);
                intent.putExtra("podcast", v);
                ctx.startActivity(intent);*//*

            }
        });*/

        pvh.playIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(pvh.playIMG, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
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

            }
        });

        pvh.headerpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Util.flashOnce(seekBar, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showPopupMenu(v);
                    }
                });

            }
        });

      /*  pvh.fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(pvh.fileName, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (pvh.bottomLayout.getVisibility() == View.GONE) {
                            pvh.bottomLayout.setVisibility(View.VISIBLE);
                        } else {
                            pvh.bottomLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });*/

        if (v.getPhotos() != null) {
           // pvh.fileName.setText("" + v.getPhotos().size());
            PodcastDTO dtd = mList.get(position);
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
                        .into(pvh.iconImage);
            }

        }

        /*pvh.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                pvh.playIMG.setVisibility(View.GONE);
                pvh.pauseIMG.setVisibility(View.VISIBLE);
            }
        });*/

        pvh.pauseIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(pvh.pauseIMG, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mediaPlayer.pause();
                        pvh.pauseIMG.setVisibility(View.GONE);
                        pvh.playIMG.setVisibility(View.VISIBLE);
                        pvh.stopIMG.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        pvh.stopIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(pvh.stopIMG, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mediaPlayer.stop();
                        pvh.playIMG.setVisibility(View.VISIBLE);
                        pvh.pauseIMG.setVisibility(View.GONE);
                        pvh.stopIMG.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(ctx, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_dailythought, popup.getMenu());
        popup.setOnMenuItemClickListener(new PodcastAdapter.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int i = menuItem.getItemId();
            if (i == R.id.share) {
                Util.flashOnce(seekBar, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        shareIt();
                    }
                });
                return true;
            }
            return false;
        }
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
        protected TextView fileName, duration;
        protected ImageView image, playIMG, pauseIMG, stopIMG, headerpic, iconImage, iconVideo;
        protected Button btnPlay, btnUpload;
        protected RelativeLayout uploadLayout, bottomLayout, podControlLayout;
        protected CardView audio_card;
        protected ImageView ratingBar;

        public PodcastsViewHolder(View itemView) {
            super(itemView);
            ratingBar =(ImageView) itemView.findViewById(R.id.ratingBar);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            duration = (TextView) itemView.findViewById(R.id.duration);
            audio_card = (CardView) itemView.findViewById(R.id.audio_card);
            duration.setVisibility(View.GONE);
            image = (ImageView) itemView.findViewById(R.id.image);
            iconImage = (ImageView) itemView.findViewById(R.id.list_image);
           // image.setVisibility(View.GONE);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            btnPlay.setVisibility(View.GONE);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnUpload.setVisibility(View.GONE);
            seekbar = (SeekBar) itemView.findViewById(R.id.seekBar);
            headerpic = (ImageView) itemView.findViewById(R.id.headerpic);
            headerpic.setVisibility(View.GONE);
            /*seekBar.setProgress(0);
            seekBar.setMax(100);*/

            playIMG = (ImageView) itemView.findViewById(R.id.playIMG);
            pauseIMG = (ImageView) itemView.findViewById(R.id.pauseIMG);
            pauseIMG.setVisibility(View.GONE);
            stopIMG = (ImageView) itemView.findViewById(R.id.stopIMG);
            stopIMG.setVisibility(View.GONE);

            //initTimetxt = (TextView) itemView.findViewById(R.id.initialTime);
           // initTimetxt.setVisibility(View.GONE);
           // finalTimetxt = (TextView) itemView.findViewById(R.id.finalTime);
           // finalTimetxt.setVisibility(View.GONE);

            /*mediaPlayer = new MediaPlayer();*/
            //mediaController = new MediaController(ctx);
            //mediaController.setAnchorView(image);

            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            bottomLayout.setVisibility(View.GONE);

            podControlLayout = (RelativeLayout) itemView.findViewById(R.id.podControlLayout);
            podControlLayout.setVisibility(View.GONE);

            uploadLayout = (RelativeLayout) itemView.findViewById(R.id.uploadLayout);
            uploadLayout.setVisibility(View.GONE);
        }
    }

    static final String LOG = PodcastAdapter.class.getSimpleName();

}
