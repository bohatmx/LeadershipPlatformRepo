package com.oneconnect.leadership.library.camera;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.VideoView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.Util;

import java.util.List;

/**
 * Created by Kurisani on 2017/06/14.
 */

public class AdminVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<VideoDTO> mList;
    private Context ctx;
    private AdminVideoAdapterListener listener;

    public interface AdminVideoAdapterListener {
        void onAttachPhoto(BaseDTO base);
        void onPoadcastRequired(BaseDTO base);
        void onUrlRequired(BaseDTO base);
        void onEbookRequired(BaseDTO base);
    }

    public AdminVideoAdapter(List<VideoDTO> mList, Context ctx, AdminVideoAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_item, parent, false);
        return new AdminVideoViewHolder(v);

    }

    boolean isPlaying = false;
    Runnable onEverySecond;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final VideoDTO v = mList.get(position);
        final AdminVideoViewHolder vvh = (AdminVideoViewHolder) holder;
        int i = v.getStorageName().lastIndexOf("/");
        vvh.fileName.setText(v.getStorageName().substring(i + 1));


        if (v.getEbooks() != null) {
            vvh.txtVideo.setText("" + v.getEbooks().size());
        }
        if (v.getPhotos() != null) {
            vvh.txtCamera.setText("" + v.getPhotos().size());
        }
        if (v.getPodcasts() != null) {
            vvh.txtMicrophone.setText("" + v.getPodcasts().size());
        }
        if (v.getUrls() != null) {
            vvh.txtLinks.setText("" + v.getUrls().size());
        }

        vvh.iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(vvh.iconCamera, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onAttachPhoto(v);
                    }
                });
            }
        });

        vvh.iconMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(vvh.iconMicrophone, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onPoadcastRequired(v);
                    }
                });
            }
        });

        vvh.iconLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(vvh.iconLink, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onUrlRequired(v);
                    }
                });
            }
        });
        vvh.fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(vvh.fileName, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (vvh.bottomLayout.getVisibility() == View.GONE){
                            vvh.bottomLayout.setVisibility(View.VISIBLE);
                        } else {
                            vvh.bottomLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        vvh.iconVideo.setImageDrawable(ctx.getDrawable(R.drawable.books_stack));
        vvh.iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(vvh.iconVideo, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onEbookRequired(v);
                    }
                });
            }
        });
        try {
            // mediaController = new MediaController(ctx);
            // mediaController.setAnchorView(vvh.videoView);
            Uri video = Uri.parse(v.getUrl());
            //  vvh.videoView.setMediaController(mediaController);
            vvh.videoView.setVideoURI(video);
            vvh.videoView.seekTo(300);
        } catch (Exception e) {
            Log.e(LOG,"Video something went wrong: " + e.getMessage());
        }

        vvh.videoView.requestFocus();

         /*onEverySecond = new Runnable() {
            @Override
            public void run() {
                if (vvh.videoSeekBar != null) {
                    vvh.videoSeekBar.setProgress(vvh.videoView.getCurrentPosition());
                }
                if (vvh.videoView.isPlaying()) {
                      vvh.videoSeekBar.postDelayed(onEverySecond, 1000);
                }
            }
        };*/
        vvh.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vvh.videoSeekBar.setMax(vvh.videoView.getDuration());
                // vvh.videoSeekBar.postDelayed(onEverySecond, 1000);

            }
        });

        vvh.videoView.setOnClickListener(new View.OnClickListener() {
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

        vvh.videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vvh.videoView.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

       /* vvh.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vvh.videoView.start();
                //listener.onPlayClicked(s);
            }
        });*/

        vvh.playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = true;
                vvh.videoView.start();
                vvh.playbtn.setVisibility(View.GONE);
                vvh.pausebtn.setVisibility(View.VISIBLE);

            }
        });

        vvh.pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = false;
                vvh.videoView.pause();
                vvh.pausebtn.setVisibility(View.GONE);
                vvh.playbtn.setVisibility(View.VISIBLE);

            }
        });

        vvh.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);


            }
        });


    }



    /*private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if (vvh.videoSeekBar != null) {
                vvh.videoSeekBar.setProgress(vvh.videoView.getCurrentPosition());
            }
            if (vvh.videoView.isPlaying()) {
                vvh.videoSeekBar.postDelayed(onEverySecond, 1000);
            }
        }
    };*/
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(ctx, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_video, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
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
            if (i == R.id.action_share) {
                shareIt();
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



    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    MediaController mediaController;
    /*VideoView videoView;
   SeekBar videoSeekBar;*/
    public class AdminVideoViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileName,count, txtLinks, txtMicrophone, txtVideo, txtCamera;
        protected ImageView image, overflow, playbtn, pausebtn, iconMicrophone, iconVideo, iconCamera, iconLink, iconDelete;
        protected Button btnPlay;
        protected VideoView videoView;
        protected SeekBar videoSeekBar;
        protected RelativeLayout bottomLayout, shareLayout;


        public AdminVideoViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            txtLinks = (TextView) itemView.findViewById(R.id.txtLinks);
            txtMicrophone = (TextView) itemView.findViewById(R.id.txtMicrophone);
            txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
            txtCamera = (TextView) itemView.findViewById(R.id.txtCamera);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            shareLayout = (RelativeLayout) itemView.findViewById(R.id.shareLayout);
            shareLayout.setVisibility(View.GONE);
            iconLink = (ImageView) itemView.findViewById(R.id.iconLink);
            iconMicrophone = (ImageView) itemView.findViewById(R.id.iconMicrophone);
            iconVideo = (ImageView) itemView.findViewById(R.id.iconVideo);
            iconCamera = (ImageView) itemView.findViewById(R.id.iconCamera);
            bottomLayout.setVisibility(View.GONE);
            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);
            iconDelete.setVisibility(View.GONE);
            image = (ImageView) itemView.findViewById(R.id.image);
            //btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            //btnPlay.setVisibility(View.GONE);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);
            count = (TextView) itemView.findViewById(R.id.fileName);
            playbtn = (ImageView) itemView.findViewById(R.id.playbtn);
            pausebtn = (ImageView) itemView.findViewById(R.id.pausebtn);
            pausebtn.setVisibility(View.GONE);
            playbtn = (ImageView) itemView.findViewById(R.id.playbtn);

            videoSeekBar = (SeekBar) itemView.findViewById(R.id.videoSeekBar);
            videoSeekBar.setVisibility(View.GONE);
        }
    }
    static final String LOG = AdminVideoAdapter.class.getSimpleName();
}
