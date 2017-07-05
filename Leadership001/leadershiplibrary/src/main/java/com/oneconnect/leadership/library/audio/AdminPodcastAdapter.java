package com.oneconnect.leadership.library.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.util.Util;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kurisani on 2017/06/13.
 */

public class AdminPodcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PodcastDTO> mList;
    private Context ctx;
    private PodcastAdapterListener listener;
    private MediaPlayer mediaPlayer;

    public AdminPodcastAdapter(List<PodcastDTO> mList, Context ctx, PodcastAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    public interface PodcastAdapterListener {
        void onPodcastRequired(PodcastDTO podcast);
        void onAttachPhoto(BaseDTO base);
        void onVideoRequired(BaseDTO base);
        void onEbookRequired(BaseDTO base);
        void onUrlRequired(BaseDTO base);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        return new PodcastsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PodcastDTO v = mList.get(position);
        final PodcastsViewHolder pvh = (PodcastsViewHolder) holder;
        int i = v.getStorageName().lastIndexOf("/");
        pvh.fileName.setText(v.getStorageName().substring(i + 1));
        final String podcastURL = v.getUrl();

        if (v.getVideos() != null) {
            pvh.txtVideo.setText("" + v.getVideos().size());
        }
        if (v.getUrls() != null) {
            pvh.txtLinks.setText("" + v.getUrls().size());
        }
        if (v.getEbooks() != null) {
            pvh.txtMicrophone.setText("" + v.getEbooks().size());
        }
        if (v.getPhotos() != null) {
            pvh.txtCamera.setText("" + v.getPhotos());
        }

        pvh.fileName.setOnClickListener(new View.OnClickListener() {
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
        });

        pvh.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(pvh.iconUpdate, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onUrlRequired(v);
                    }
                });
            }
        });
        pvh.iconMicrophone.setImageDrawable(ctx.getDrawable(R.drawable.books_stack));
        pvh.iconMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(pvh.iconMicrophone, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onEbookRequired(v);
                    }
                });
            }
        });
        pvh.iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(pvh.iconVideo, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onVideoRequired(v);
                    }
                });
            }
        });
        pvh.iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(pvh.iconCamera, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onAttachPhoto(v);
                    }
                });
            }
        });

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

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PodcastsViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileName, txtLinks, txtMicrophone,
                txtVideo, txtCamera;
        protected ImageView image, playIMG, pauseIMG, stopIMG, headerpic, iconCamera, iconVideo, iconMicrophone, iconUpdate;
        protected Button btnPlay, btnUpload;
        protected RelativeLayout uploadLayout, bottomLayout;

        public PodcastsViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            txtLinks = (TextView) itemView.findViewById(R.id.txtLinks);
            txtMicrophone = (TextView) itemView.findViewById(R.id.txtMicrophone);
            txtCamera = (TextView) itemView.findViewById(R.id.txtCamera);
            txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);
            iconMicrophone = (ImageView) itemView.findViewById(R.id.iconMicrophone);
            iconVideo = (ImageView) itemView.findViewById(R.id.iconVideo);
            iconCamera = (ImageView) itemView.findViewById(R.id.iconCamera);
            bottomLayout.setVisibility(View.GONE);
            image = (ImageView) itemView.findViewById(R.id.image);
            // image.setVisibility(View.GONE);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            btnPlay.setVisibility(View.GONE);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnUpload.setVisibility(View.GONE);
            headerpic = (ImageView) itemView.findViewById(R.id.headerpic);
            /*seekBar.setProgress(0);
            seekBar.setMax(100);*/

            playIMG = (ImageView) itemView.findViewById(R.id.playIMG);
            pauseIMG = (ImageView) itemView.findViewById(R.id.pauseIMG);
            pauseIMG.setVisibility(View.GONE);
            stopIMG = (ImageView) itemView.findViewById(R.id.stopIMG);
            stopIMG.setVisibility(View.GONE);

            uploadLayout = (RelativeLayout) itemView.findViewById(R.id.uploadLayout);
            uploadLayout.setVisibility(View.GONE);
        }
    }

    static final String LOG = AdminPodcastAdapter.class.getSimpleName();
}
