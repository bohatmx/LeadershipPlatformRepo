package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;
import com.oneconnect.leadership.library.util.Util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kurisani on 2017/05/02.
 */

public class MasterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<WeeklyMasterClassDTO> mList;
    private Context ctx;
    private MasterAdapterListener listener;
    MiniPhotoAdapter miniPhotoAdapter;
    MiniPodcastAdapter miniPodcastAdapter;
    MiniVideoAdapter miniVideoAdapter;
    UrlAdapter urlAdapter;
    private MediaPlayer mediaPlayer;
    String url;


    public interface MasterAdapterListener{
       void onMasterClicked(int position);
    }

    public MasterAdapter(Context ctx, List<WeeklyMasterClassDTO> mList){
        this.ctx = ctx;
        this.mList = mList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item,parent, false);
        return new MasterViewHolder(v);
    }

    static final SimpleDateFormat sd1 = new SimpleDateFormat(" dd-MM-yyyy HH:mm");

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final WeeklyMasterClassDTO dt = mList.get(position);
        final MasterViewHolder dvh = (MasterViewHolder) holder;
//        dvh.txtEvents.setText("" + (position + 1));
        dvh.txtTitle.setText(dt.getTitle());
        dvh.txtSubtitle.setText(dt.getSubtitle());
        StringBuilder sb = new StringBuilder(dt.getStringDateRegistered());
        sb.deleteCharAt(sb.indexOf(","));
        long miliSecs = Util.getMiliseconds(sb.toString());
        String formatedDate = Util.getFormattedDate(miliSecs);
        dvh.txtDate.setText(formatedDate);
        dvh.iconCamera.setImageDrawable(ctx.getDrawable(R.drawable.ic_photo_black_24dp));
        dvh.iconUpdate.setImageDrawable(ctx.getDrawable(R.drawable.ic_link_black_24dp));

        dvh.iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Util.flashOnce(dvh.iconCalendar, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showPopupMenu(v);
                    }
                });
            }
        });

        if (dt.getVideos() != null) {
            dvh.txtVideo.setText("" + dt.getVideos().size());
            dvh.iconVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.flashOnce(dvh.iconVideo, 300, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {
                            if (dvh.videoAdapterLayout.getVisibility() == View.GONE){
                                dvh.videoAdapterLayout.setVisibility(View.VISIBLE);
                                dvh.videoRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                dvh.videoAdapterLayout.setVisibility(View.GONE);
                                dvh.videoRecyclerView.setVisibility(View.GONE);
                            }

                            WeeklyMasterClassDTO dtd = mList.get(position);
                            List<VideoDTO> videoList = new ArrayList<>();
                            Map map = dtd.getVideos();
                            VideoDTO vDTO;
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

                            dvh.videoRecyclerView.setAdapter(miniVideoAdapter);
                            dvh.btnPlay.setVisibility(View.GONE);

                            dvh.btnPlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                        }
                    });
                }
            });
        }
        if (dt.getPhotos() != null) {
            dvh.txtCamera.setText("" + dt.getPhotos().size());
            WeeklyMasterClassDTO dtd = mList.get(position);
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
                        .into(dvh.imageView);
            }
            dvh.iconCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Util.flashOnce(dvh.iconCamera, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                        if (dvh.photoAdapterLayout.getVisibility() == View.GONE){
                            dvh.photoAdapterLayout.setVisibility(View.VISIBLE);
                            dvh.imageRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            dvh.photoAdapterLayout.setVisibility(View.GONE);
                            dvh.imageRecyclerView.setVisibility(View.GONE);
                        }

                        WeeklyMasterClassDTO dtd = mList.get(position);
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
                                    .into(dvh.photoView);
                            dvh.captiontxt.setText(vDTO.getCaption());

                        }

                        miniPhotoAdapter = new MiniPhotoAdapter(urlList, ctx, new PhotoAdapter.PhotoAdapterlistener() {
                            @Override
                            public void onPhotoClicked(PhotoDTO photo) {

                            }
                        });
                        dvh.imageRecyclerView.setAdapter(miniPhotoAdapter);
                    }
                });
                }
            });
        }
        if (dt.getPodcasts() != null) {
            dvh.txtMicrophone.setText("" + dt.getPodcasts().size());
            dvh.iconMicrophone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Util.flashOnce(dvh.iconMicrophone, 300, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {
                            if (dvh.podcastAdapterLayout.getVisibility() == View.GONE){
                                dvh.podcastAdapterLayout.setVisibility(View.VISIBLE);
                                dvh.podcastRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                dvh.podcastAdapterLayout.setVisibility(View.GONE);
                                dvh.podcastRecyclerView.setVisibility(View.GONE);
                            }
                            WeeklyMasterClassDTO dtd = mList.get(position);
                            List<PodcastDTO> podcastList = new ArrayList<>();
                            Map map = dtd.getPodcasts();
                            PodcastDTO vDTO;
                            for (Object value : map.values()) {
                                vDTO = (PodcastDTO) value;
                                url = vDTO.getUrl();
                                podcastList.add(vDTO);
                                //
                                int i = vDTO.getStorageName().lastIndexOf("/");
                                dvh.podcastfileName.setText(vDTO.getStorageName().substring(i + 1));
                                //
                                dvh.playIMG.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mediaPlayer = new MediaPlayer();
                                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        dvh.playIMG.setVisibility(View.GONE);
                                        dvh.pauseIMG.setVisibility(View.VISIBLE);
                                        dvh.stopIMG.setVisibility(View.VISIBLE);
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
                                });

                                //

                                //
                                dvh.pauseIMG.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.flashOnce(dvh.pauseIMG, 300, new Util.UtilAnimationListener() {
                                            @Override
                                            public void onAnimationEnded() {
                                                mediaPlayer.pause();
                                                dvh.pauseIMG.setVisibility(View.GONE);
                                                dvh.playIMG.setVisibility(View.VISIBLE);
                                                dvh.stopIMG.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                });

                                dvh.stopIMG.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.flashOnce(dvh.stopIMG, 300, new Util.UtilAnimationListener() {
                                            @Override
                                            public void onAnimationEnded() {
                                                mediaPlayer.stop();
                                                dvh.playIMG.setVisibility(View.VISIBLE);
                                                dvh.pauseIMG.setVisibility(View.GONE);
                                                dvh.stopIMG.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                });
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
                            dvh.podcastRecyclerView.setAdapter(miniPodcastAdapter);

                        }
                    });
                }
            });
        }

        dvh.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(dvh.txtTitle, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (dvh.bottomLayout.getVisibility() == View.GONE){
                            dvh.bottomLayout.setVisibility(View.VISIBLE);
                        } else {
                            dvh.bottomLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        if (dt.getUrls() != null) {
            dvh.txtLinks.setText("" + dt.getUrls().size());
            dvh.iconUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.flashOnce(dvh.iconUpdate, 300, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {
                            if (dvh.urlAdapterLayout.getVisibility() == View.GONE){
                                dvh.urlAdapterLayout.setVisibility(View.VISIBLE);
                                dvh.urlRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                dvh.urlAdapterLayout.setVisibility(View.GONE);
                                dvh.urlRecyclerView.setVisibility(View.GONE);
                            }

                            WeeklyMasterClassDTO dtd = mList.get(position);
                            Map map = dtd.getUrls();
                            UrlDTO vDTO;
                            final List<UrlDTO> urlList = new ArrayList<>();
                            String url;
                            for (Object value : map.values()) {
                                vDTO = (UrlDTO) value;
                                url = vDTO.getUrl();
                                dvh.urlTxt.setText(url);
                                urlList.add(vDTO);
                            }

                            urlAdapter = new UrlAdapter(urlList, ctx, new UrlAdapter.UrlAdapterListener() {
                                @Override
                                public void onUrlClicked(final String url) {
                                }
                            });

                            dvh.urlRecyclerView.setAdapter(urlAdapter);
                        }
                    });
                }
            });
        }
    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(ctx, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_masterclass, popup.getMenu());
        popup.setOnMenuItemClickListener(new MasterAdapter.MyMenuItemClickListener());
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
                shareIt();
             /*   Util.flashOnce(, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        shareIt();
                    }
                });*/

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
    public class MasterViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtEvents, txtTitle, txtDate, txtSubtitle, txtLinks, txtMicrophone,
                txtVideo, txtCamera, captiontxt, podcastfileName, urlTxt;
        protected ImageView iconCalendar, iconUpdate, iconDelete, iconMicrophone, iconVideo,
                iconCamera, photoView, playIMG, pauseIMG, stopIMG, imageView;
        protected RelativeLayout bottomLayout;
        protected LinearLayout iconLayout;
        protected RelativeLayout deleteLayout, linksLayout, micLayout, videosLayout, photosLayout, podcastAdapterLayout, videoAdapterLayout, photoAdapterLayout,
                urlAdapterLayout;

        protected RecyclerView imageRecyclerView, videoRecyclerView, urlRecyclerView, podcastRecyclerView;
        protected Button btnPlay;

        public MasterViewHolder(View itemView) {
            super(itemView);
            //txtEvents = (TextView) itemView.findViewById(R.id.txtEvents);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            iconCalendar = (ImageView) itemView.findViewById(R.id.iconCalendar);
            //iconCalendar.setVisibility(View.GONE);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            bottomLayout.setVisibility(View.GONE);
            iconLayout = (LinearLayout) itemView.findViewById(R.id.iconLayout);
            deleteLayout = (RelativeLayout) itemView.findViewById(R.id.deleteLayout);
            deleteLayout.setVisibility(View.GONE);
            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);
            txtLinks = (TextView) itemView.findViewById(R.id.txtLinks);
            txtMicrophone = (TextView) itemView.findViewById(R.id.txtMicrophone);
            txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
            txtCamera = (TextView) itemView.findViewById(R.id.txtCamera);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            /*videoFileName = (TextView) itemView.findViewById(R.id.fileName);
            videoFileName.setVisibility(View.GONE);*/
            playIMG = (ImageView) itemView.findViewById(R.id.playIMG);
            pauseIMG = (ImageView)itemView.findViewById(R.id.pauseIMG);
            stopIMG = (ImageView) itemView.findViewById(R.id.stopIMG);
            podcastfileName = (TextView) itemView.findViewById(R.id.fileName);

            imageView = (ImageView) itemView.findViewById(R.id.ImageView);
            iconMicrophone = (ImageView) itemView.findViewById(R.id.iconMicrophone);

            iconVideo = (ImageView) itemView.findViewById(R.id.iconVideo);


            iconCamera = (ImageView) itemView.findViewById(R.id.iconCamera);

            iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);
            //
            imageRecyclerView = (RecyclerView) itemView.findViewById(R.id.imageRecyclerView);
            imageRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            imageRecyclerView.setLayoutManager(llm);
            imageRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            imageRecyclerView.setHasFixedSize(true);
            //

            videoRecyclerView = (RecyclerView) itemView.findViewById(R.id.videoRecyclerView);
            videoRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm2 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            videoRecyclerView.setLayoutManager(llm2);
            videoRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            videoRecyclerView.setHasFixedSize(true);

            urlRecyclerView = (RecyclerView) itemView.findViewById(R.id.urlRecyclerView);
            urlRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm3 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            urlRecyclerView.setLayoutManager(llm3);
            urlRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            urlRecyclerView.setHasFixedSize(true);

            podcastRecyclerView = (RecyclerView) itemView.findViewById(R.id.podcastRecyclerView);
            podcastRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm4 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            podcastRecyclerView.setLayoutManager(llm4);
            podcastRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            podcastRecyclerView.setHasFixedSize(true);

            // layouts
            podcastAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.podcastAdapterLayout);
            //

            //
            videoAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.videoAdapterLayout);
            //

            //
            photoAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.photoAdapterLayout);
            photoView = (ImageView) itemView.findViewById(R.id.photoView);
            captiontxt = (TextView) itemView.findViewById(R.id.captiontxt);
            //

            urlAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.urlAdapterLayout);
            urlTxt = (TextView) itemView.findViewById(R.id.urlTxt);

        }
    }


    static final String LOG = MasterAdapter.class.getSimpleName();
}
