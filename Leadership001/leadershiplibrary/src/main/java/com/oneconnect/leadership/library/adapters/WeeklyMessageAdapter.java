package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.oneconnect.leadership.library.activities.RatingActivity;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;
import com.oneconnect.leadership.library.util.TextViewExpandableAnimation;
import com.oneconnect.leadership.library.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Nkululeko on 2017/05/03.
 */

public class WeeklyMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<WeeklyMessageDTO> mList;
    private Context ctx;
    private WeeklyMessageAdapterListener listener;
    MiniPhotoAdapter miniPhotoAdapter;
    MiniPodcastAdapter miniPodcastAdapter;
    MiniVideoAdapter miniVideoAdapter;
    UrlAdapter urlAdapter;
    private MediaPlayer mediaPlayer;
    String url;

    public interface WeeklyMessageAdapterListener {
        void onMessageClicked();
    }

    public WeeklyMessageAdapter(List<WeeklyMessageDTO> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item_two, parent, false);
        return new WeeklyMessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final WeeklyMessageDTO wm = mList.get(position);
        final WeeklyMessageViewHolder wmvh = (WeeklyMessageViewHolder) holder;
      //  wmvh.txtEvents.setText("" + (position + 1));
        wmvh.txtTitle.setText(wm.getTitle());
        /*if (wmvh.txtTitle.getLineCount() > 3) {
            wmvh.txtTitle.setLines(3);
            wmvh.txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        }*/
        wmvh.profile.setText(wm.getCompanyName());
        wmvh.txtSubtitle.setText(wm.getSubtitle());
        StringBuilder sb = new StringBuilder(wm.getStringDateRegistered());
        sb.deleteCharAt(sb.indexOf(","));
        long miliSecs = Util.getMiliseconds(sb.toString());
        String formatedDate = Util.getFormattedDate(miliSecs);
        wmvh.txtDate.setText(formatedDate);
        wmvh.iconCamera.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_photo_black_24dp)/*ctx.getDrawable(R.drawable.ic_photo_black_24dp)*/);
      //  wmvh.iconUpdate.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_link_black_24dp)/*ctx.getDrawable(R.drawable.ic_link_black_24dp)*/);


        wmvh.iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                showPopupMenu(v);
            }
        });

        if (wm.getVideos() != null) {
            wmvh.txtVideo.setText("" + wm.getVideos().size());
            wmvh.iconVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            if (wmvh.videoAdapterLayout.getVisibility() == View.GONE){
                                wmvh.videoAdapterLayout.setVisibility(View.VISIBLE);
                                wmvh.videoRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                wmvh.videoAdapterLayout.setVisibility(View.GONE);
                                wmvh.videoRecyclerView.setVisibility(View.GONE);
                            }

                            WeeklyMessageDTO dtd = mList.get(position);
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

                            wmvh.videoRecyclerView.setAdapter(miniVideoAdapter);
                            wmvh.btnPlay.setVisibility(View.GONE);

                            wmvh.btnPlay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            });
                        }
                    });

        }
        if (wm.getPhotos() != null) {
            wmvh.txtCamera.setText("" + wm.getPhotos().size());

            WeeklyMessageDTO dtd = mList.get(position);
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
                        .into(wmvh.imageView);
            }
            wmvh.iconCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (wmvh.photoAdapterLayout.getVisibility() == View.GONE) {
                        wmvh.photoAdapterLayout.setVisibility(View.VISIBLE);
                        wmvh.imageRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        wmvh.photoAdapterLayout.setVisibility(View.GONE);
                        wmvh.imageRecyclerView.setVisibility(View.GONE);
                    }

                    WeeklyMessageDTO dtd = mList.get(position);
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
                                .into(wmvh.photoView);
                        wmvh.captiontxt.setText(vDTO.getCaption());

                    }

                    miniPhotoAdapter = new MiniPhotoAdapter(urlList, ctx, new PhotoAdapter.PhotoAdapterlistener() {
                        @Override
                        public void onPhotoClicked(PhotoDTO photo) {

                        }
                    });
                    wmvh.imageRecyclerView.setAdapter(miniPhotoAdapter);
                }


            });
        }


        if (wm.getPodcasts() != null) {
            wmvh.txtMicrophone.setText("" + wm.getPodcasts().size());
            wmvh.iconMicrophone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            if (wmvh.podcastAdapterLayout.getVisibility() == View.GONE){
                                wmvh.podcastAdapterLayout.setVisibility(View.VISIBLE);
                                wmvh.podcastRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                wmvh.podcastAdapterLayout.setVisibility(View.GONE);
                                wmvh.podcastRecyclerView.setVisibility(View.GONE);
                            }
                            WeeklyMessageDTO dtd = mList.get(position);
                            List<PodcastDTO> podcastList = new ArrayList<>();
                            Map map = dtd.getPodcasts();
                            PodcastDTO vDTO;
                            for (Object value : map.values()) {
                                vDTO = (PodcastDTO) value;
                                url = vDTO.getUrl();
                                podcastList.add(vDTO);
                                //
                                int i = vDTO.getStorageName().lastIndexOf("/");
                                wmvh.podcastfileName.setText(vDTO.getStorageName().substring(i + 1));
                                //
                                wmvh.playIMG.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mediaPlayer = new MediaPlayer();
                                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        wmvh.playIMG.setVisibility(View.GONE);
                                        wmvh.pauseIMG.setVisibility(View.VISIBLE);
                                        wmvh.stopIMG.setVisibility(View.VISIBLE);
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

                                wmvh.pauseIMG.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.flashOnce(wmvh.pauseIMG, 300, new Util.UtilAnimationListener() {
                                            @Override
                                            public void onAnimationEnded() {
                                                mediaPlayer.pause();
                                                wmvh.pauseIMG.setVisibility(View.GONE);
                                                wmvh.playIMG.setVisibility(View.VISIBLE);
                                                wmvh.stopIMG.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                });

                                wmvh.stopIMG.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.flashOnce(wmvh.stopIMG, 300, new Util.UtilAnimationListener() {
                                            @Override
                                            public void onAnimationEnded() {
                                                mediaPlayer.stop();
                                                wmvh.playIMG.setVisibility(View.VISIBLE);
                                                wmvh.pauseIMG.setVisibility(View.GONE);
                                                wmvh.stopIMG.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                });
                            }


                            miniPodcastAdapter = new MiniPodcastAdapter(podcastList, ctx, new PodcastAdapter.PodcastAdapterListener() {


                                @Override
                                public void onPodcastRating(PodcastDTO podcast) {

                                }

                                @Override
                                public void onPodcastRequired(PodcastDTO podcast) {

                                }
                            });
                            wmvh.podcastRecyclerView.setAdapter(miniPodcastAdapter);
                        }
                    });
        }

        wmvh.txtSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            if (wmvh.txtSubtitle.getLineCount() > 3) {
                                wmvh.txtSubtitle.setLines(7);
                            }
                            /*dvh.txtTitle.getEllipsize()setEllipsize(TextUtils.TruncateAt.END);*/
                            //   dvh.txtTitle.setText(dvh.txtTitle.getText());
                         else {
                            if (wmvh.txtSubtitle.getLineCount() > 3) {
                                wmvh.txtSubtitle.setLines(3);
                                wmvh.txtSubtitle.setEllipsize(TextUtils.TruncateAt.END);
                            }
                        }
                    }
                });

        if (wm.getUrls() != null) {
            wmvh.txtLinks.setText("" + wm.getUrls().size());
            wmvh./*iconUpdate*/iconLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            if (wmvh.urlAdapterLayout.getVisibility() == View.GONE){
                                wmvh.urlAdapterLayout.setVisibility(View.VISIBLE);
                                wmvh.urlRecyclerView.setVisibility(View.VISIBLE);
                            } else {
                                wmvh.urlAdapterLayout.setVisibility(View.GONE);
                                wmvh.urlRecyclerView.setVisibility(View.GONE);
                            }

                            WeeklyMessageDTO dtd = mList.get(position);
                            Map map = dtd.getUrls();
                            UrlDTO vDTO;
                            final List<UrlDTO> urlList = new ArrayList<>();
                            String url;
                            for (Object value : map.values()) {
                                vDTO = (UrlDTO) value;
                                url = vDTO.getUrl();
                                wmvh.urlTxt.setText(url);
                                urlList.add(vDTO);
                            }


                            urlAdapter = new UrlAdapter(urlList, ctx, new UrlAdapter.UrlAdapterListener() {
                                @Override
                                public void onUrlClicked(final String url) {
                                }
                            });

                            wmvh.urlRecyclerView.setAdapter(urlAdapter);
                        }
                    });

        }

        wmvh.ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, RatingActivity.class);
                intent.putExtra("weeklyMessage", wm);
                ctx.startActivity(intent);
            }
        });

    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(ctx, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_masterclass, popup.getMenu());
        popup.setOnMenuItemClickListener(new WeeklyMessageAdapter.MyMenuItemClickListener());
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
                return true;
            }
            return false;
        }
    }



    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        ctx.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class WeeklyMessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtEvents, /*txtTitle,*/ txtDate, txtSubtitle, txtLinks, txtMicrophone,
                txtVideo, txtCamera, captiontxt, podcastfileName, urlTxt,profile;
        protected ImageView iconCalendar, /*iconUpdate*/iconLink, iconDelete, iconMicrophone, iconVideo, iconCamera, photoView,
                playIMG, pauseIMG, stopIMG, imageView;
        protected RelativeLayout bottomLayout;
        protected LinearLayout iconLayout;
        protected RelativeLayout deleteLayout, linksLayout, micLayout, videosLayout, photosLayout, podcastAdapterLayout, videoAdapterLayout, photoAdapterLayout,
                urlAdapterLayout, updateLayout;

        protected RecyclerView imageRecyclerView, videoRecyclerView, urlRecyclerView, podcastRecyclerView;
        protected Button btnPlay;
        protected TextViewExpandableAnimation txtTitle;
        protected ImageView ratingBar;

        public WeeklyMessageViewHolder(View itemView) {
            super(itemView);
            //txtEvents = (TextView) itemView.findViewById(R.id.txtEvents);
            ratingBar =(ImageView) itemView.findViewById(R.id.ratingBar);
            txtTitle = (TextViewExpandableAnimation/*TextView*/) itemView.findViewById(R.id.txtTitle);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            iconCalendar = (ImageView) itemView.findViewById(R.id.iconCalendar);
            iconCalendar.setVisibility(View.GONE);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            //bottomLayout.setVisibility(View.GONE);
            updateLayout = (RelativeLayout) itemView.findViewById(R.id.updateLayout);
            updateLayout.setVisibility(View.GONE);
            iconLayout = (LinearLayout) itemView.findViewById(R.id.iconLayout);
            deleteLayout = (RelativeLayout) itemView.findViewById(R.id.deleteLayout);
            deleteLayout.setVisibility(View.GONE);
            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);
            txtLinks = (TextView) itemView.findViewById(R.id.txtLinks);
            profile = (TextView) itemView.findViewById(R.id.profile);
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

         //   iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);
            iconLink = (ImageView) itemView.findViewById(R.id.iconLink);

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


 static final String LOG = WeeklyMessageAdapter.class.getSimpleName();
}
