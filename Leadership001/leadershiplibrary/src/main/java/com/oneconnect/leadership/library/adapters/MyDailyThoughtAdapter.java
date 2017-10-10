package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.RatingActivity;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;
import com.oneconnect.leadership.library.util.Util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by Kurisani on 2017/09/21.
 */

public class MyDailyThoughtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DailyThoughtDTO> mList;
    private Context ctx;
    private MyDailyThoughtAdapterlistener listener;
    MiniPhotoAdapter miniPhotoAdapter;
    MiniPodcastAdapter miniPodcastAdapter;
    MiniVideoAdapter miniVideoAdapter;
    UrlAdapter urlAdapter;
    private int type;

    public interface MyDailyThoughtAdapterlistener{
    /*  void onThoughtClicked(int position);
        void onPhotoRequired(PhotoDTO photo);
        void onVideoRequired(VideoDTO video);
        void onPodcastRequired(PodcastDTO podcast);
        void onUrlRequired(UrlDTO url);
        void onPhotosRequired(List<PhotoDTO> list);*/
        void onLinksRequired(BaseDTO entity);
        void onAddEntity();
        void onMicrophoneRequired(BaseDTO entity);
        void onPictureRequired(BaseDTO entity);
        void onVideoRequired(BaseDTO entity);

    }

    public MyDailyThoughtAdapter(Context ctx, List<DailyThoughtDTO> mList, MyDailyThoughtAdapterlistener listener) {
        this.ctx = ctx;
        this.mList = mList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item, parent, false);
        return new DailyViewHolder(v);
    }

    static final SimpleDateFormat sd1 = new SimpleDateFormat(" dd-MM-yyyy HH:mm");
    private MediaPlayer mediaPlayer;
    String url;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final DailyThoughtDTO dt = mList.get(position);
        final DailyViewHolder dvh = (DailyViewHolder) holder;
        dvh.txtTitle.setText(dt.getTitle());
        dvh.profile.setText(dt.getCompanyName());
        dvh.txtSubtitle.setText(dt.getSubtitle());
        if (dt.getJournalUserName() != null) {
            dvh.compName.setText(dt.getJournalUserName());
        }
        else{
            dvh.compName.setVisibility(View.GONE);
        }

        StringBuilder sb = new StringBuilder(dt.getStringDateRegistered());
        sb.deleteCharAt(sb.indexOf(","));
        long miliSecs = Util.getMiliseconds(sb.toString());
        String formatedDate = Util.getFormattedDate(miliSecs);
        dvh.txtDate.setText(formatedDate);
        dvh.iconCamera.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_photo_black_24dp)/*ctx.getDrawable(R.drawable.ic_photo_black_24dp)*/);
        dvh.iconCalendar.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.pending));

        if (dt.getStatus() != null) {
            if (dt.getStatus().equalsIgnoreCase("pending")) {
                dvh.iconCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toasty.warning(ctx,"You Thought is still pending", Toast.LENGTH_LONG,true).show();
                        //Toast.makeText(ctx, "You Thought is still pending", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (dt.getStatus().equalsIgnoreCase("approved")) {
                dvh.iconCalendar.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.approved));
                dvh.iconCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toasty.success(ctx, "You Thought is approved", Toast.LENGTH_LONG, true).show();
                       // Toast.makeText(ctx, "You Thought is approved", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (dt.getStatus().equalsIgnoreCase("declined")) {
                dvh.iconCalendar.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.declined));
                dvh.iconCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toasty.error(ctx,"You Thought is declined",Toast.LENGTH_LONG,true).show();
                        //Toast.makeText(ctx, "You Thought is declined", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }



        if (dt.getVideos() != null) {
            dvh.txtVideo.setText("" + dt.getVideos().size());
        }
        dvh.iconVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.onVideoRequired(dt);
            }
        });

        if (dt.getPhotos() != null) {
            dvh.txtCamera.setText("" + dt.getPhotos().size());
            }
            dvh.iconCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPictureRequired(dt);
                }
            });

        if (dt.getPodcasts() != null) {
            dvh.txtMicrophone.setText("" + dt.getPodcasts().size());
        }
        dvh.iconMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMicrophoneRequired(dt);
            }
        });
        dvh.ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, RatingActivity.class);
                //intent.putExtra("type", ResponseBag.DAILY_THOUGHTS);
                intent.putExtra("dailyThought", dt);
                ctx.startActivity(intent);
            }
        });
        dvh.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dvh.bottomLayout.getVisibility() == View.GONE){
                    dvh.bottomLayout.setVisibility(View.VISIBLE);
                          /* if (dvh.txtTitle.getLineCount() > 3) {
                               dvh.txtTitle.setLines(5);
                           }*/
                } else {
                    dvh.bottomLayout.setVisibility(View.GONE);
                           /* if (dvh.txtTitle.getLineCount() > 3) {
                                dvh.txtTitle.setLines(3);
                                dvh.txtTitle.setEllipsize(TextUtils.TruncateAt.END);
                            }*/

                }
            }
        });

        dvh.txtSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dvh.bottomLayout.getVisibility() == View.GONE){
                    dvh.bottomLayout.setVisibility(View.VISIBLE);
                          /* if (dvh.txtTitle.getLineCount() > 3) {
                               dvh.txtTitle.setLines(5);
                           }*/
                } else {
                    dvh.bottomLayout.setVisibility(View.GONE);
                           /* if (dvh.txtTitle.getLineCount() > 3) {
                                dvh.txtTitle.setLines(3);
                                dvh.txtTitle.setEllipsize(TextUtils.TruncateAt.END);
                            }*/

                }
            }
        });

        if (dt.getUrls() != null) {
            dvh.txtLinks.setText("" + dt.getUrls().size());
        }
        dvh.iconLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLinksRequired(dt);
            }
        });
        dvh.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dvh.deleteLayout.setVisibility(View.GONE);
            }
        });

        dvh.iconShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });
    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(ctx, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_dailythought, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyDailyThoughtAdapter.MyMenuItemClickListener());
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

   /* String myDynamicLink = "https://vfb7b.app.goo.gl/tobR";
    private void dynamicShare(){
    Intent sendIntent = new Intent();
    String msg = "Hey, check this out: " + myDynamicLink;
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
    sendIntent.setType("text/plain");
    ctx.startActivity(sendIntent);
}*/

    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        ctx.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    MediaController mediaController;

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtEvents, /*txtTitle,*/ txtDate, txtSubtitle, txtLinks, txtMicrophone,
                txtVideo, txtCamera, captiontxt, /*videoFileName,*/ podcastfileName, urlTxt;
        protected ImageView iconCalendar, /*iconUpdate*/iconLink, iconDelete, iconMicrophone, iconVideo, iconCamera, photoView,
                playIMG, pauseIMG, stopIMG, imageView, iconShare, iconReview;
        protected RelativeLayout bottomLayout;
        protected LinearLayout iconLayout;
        protected RelativeLayout deleteLayout, linksLayout, micLayout, videosLayout, photosLayout, podcastAdapterLayout, videoAdapterLayout,
                photoAdapterLayout, urlAdapterLayout, updateLayout;
        protected Button btnPlay;
        protected ImageView ratingBar;
        protected EditText ratingCom;
        //video
        /*protected VideoView videoView;*/
        //
        protected RecyclerView imageRecyclerView, videoRecyclerView, urlRecyclerView, podcastRecyclerView;
        //protected TextViewExpandableAnimation txtTitle;
        protected TextView txtTitle,profile, compName;

        public DailyViewHolder(View itemView) {
            super(itemView);
            ratingBar =(ImageView) itemView.findViewById(R.id.ratingBar);
            ratingCom = (EditText) itemView.findViewById(R.id.ratingCom);
            compName = (TextView) itemView.findViewById(R.id.compName);
            profile = (TextView) itemView.findViewById(R.id.profile);
            //txtEvents = (TextView) itemView.findViewById(R.id.txtEvents);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);

            //txtTitle = (TextViewExpandableAnimation/*TextView*/) itemView.findViewById(R.id.txtTitle);
            iconShare = (ImageView) itemView.findViewById(R.id.iconShare);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            iconCalendar = (ImageView) itemView.findViewById(R.id.iconCalendar);
            iconCalendar.setVisibility(View.VISIBLE);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            //bottomLayout.setVisibility(View.GONE);
            updateLayout = (RelativeLayout) itemView.findViewById(R.id.updateLayout);
            updateLayout.setVisibility(View.GONE);
            iconLayout = (LinearLayout) itemView.findViewById(R.id.iconLayout);
            deleteLayout = (RelativeLayout) itemView.findViewById(R.id.deleteLayout);
            deleteLayout.setVisibility(View.GONE);
            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);
            txtLinks = (TextView) itemView.findViewById(R.id.txtLinks);
            txtMicrophone = (TextView) itemView.findViewById(R.id.txtMicrophone);
            txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
            txtCamera = (TextView) itemView.findViewById(R.id.txtCamera);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            imageView = (ImageView) itemView.findViewById(R.id.ImageView);
            iconReview = (ImageView) itemView.findViewById(R.id.iconReview);
            /*videoFileName = (TextView) itemView.findViewById(R.id.fileName);
            videoFileName.setVisibility(View.GONE);*/


            playIMG = (ImageView) itemView.findViewById(R.id.playIMG);
            pauseIMG = (ImageView)itemView.findViewById(R.id.pauseIMG);
            stopIMG = (ImageView) itemView.findViewById(R.id.stopIMG);
            podcastfileName = (TextView) itemView.findViewById(R.id.fileName);


            iconMicrophone = (ImageView) itemView.findViewById(R.id.iconMicrophone);

            iconVideo = (ImageView) itemView.findViewById(R.id.iconVideo);


            iconCamera = (ImageView) itemView.findViewById(R.id.iconCamera);

            //   iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);
            iconLink = (ImageView) itemView.findViewById(R.id.iconLink);
            //
            imageRecyclerView = (RecyclerView) itemView.findViewById(R.id.imageRecyclerView);
            imageRecyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));
            imageRecyclerView.setVisibility(View.GONE);/*
            LinearLayoutManager llm = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            imageRecyclerView.setLayoutManager(llm);
            imageRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            imageRecyclerView.setHasFixedSize(true);
            *///

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



    static final String LOG = DailyThoughtAdapter.class.getSimpleName();
}
