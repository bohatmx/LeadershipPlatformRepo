package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by Nkululeko on 2017/05/03.
 */

public class WeeklyMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<WeeklyMessageDTO> mList;
    private Context ctx;
    private WeeklyMessageAdapterListener listener;

    public interface WeeklyMessageAdapterListener {
        void onMessageClicked();
    }

    public WeeklyMessageAdapter(List<WeeklyMessageDTO> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item, parent, false);
        return new WeeklyMessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final WeeklyMessageDTO wm = mList.get(position);
        final WeeklyMessageViewHolder wmvh = (WeeklyMessageViewHolder) holder;
        wmvh.txtEvents.setText("" + (position + 1));
        wmvh.txtTitle.setText(wm.getTitle());
        wmvh.txtSubtitle.setText(wm.getSubtitle());
        wmvh.txtDate.setText(wm.getStringDateScheduled());
        wmvh.iconCamera.setImageDrawable(ctx.getDrawable(R.drawable.ic_photo_black_24dp));
        wmvh.iconUpdate.setImageDrawable(ctx.getDrawable(R.drawable.ic_link_black_24dp));

        wmvh.iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

        if (wm.getVideos() != null) {
            wmvh.txtVideo.setText("" + wm.getVideos().size());
        }
        if (wm.getPhotos() != null) {
            wmvh.txtCamera.setText("" + wm.getPhotos().size());
            wmvh.iconCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wmvh.photoAdapterLayout.setVisibility(View.VISIBLE);
                        WeeklyMessageDTO dtd = mList.get(position);
                        Map map = dtd.getPhotos();
                        PhotoDTO vDTO;
                        String url;
                        for (Object value : map.values()) {
                            vDTO = (PhotoDTO) value;
                            url = vDTO.getUrl();
                            Glide.with(ctx)
                                    .load(url)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(wmvh.photoView);
                            wmvh.captiontxt.setText(vDTO.getCaption());
                        }
                    }
                });

        }
        if (wm.getPodcasts() != null) {
            wmvh.txtMicrophone.setText("" + wm.getPodcasts().size());
        }
        if (wm.getUrls() != null) {
            wmvh.txtLinks.setText("" + wm.getUrls().size());
        }

    }
    private void shareIt() {
        //sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/ ");
        ctx.startActivity(sharingIntent);
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class WeeklyMessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtEvents, txtTitle, txtDate, txtSubtitle, txtLinks, txtMicrophone,
                txtVideo, txtCamera, captiontxt;
        protected ImageView iconCalendar, iconUpdate, iconDelete, iconMicrophone, iconVideo, iconCamera, photoView;
        protected RelativeLayout bottomLayout;
        protected LinearLayout iconLayout;
        protected RelativeLayout deleteLayout, linksLayout, micLayout, videosLayout, photosLayout, podcastAdapterLayout, videoAdapterLayout, photoAdapterLayout;

        public WeeklyMessageViewHolder(View itemView) {
            super(itemView);
            txtEvents = (TextView) itemView.findViewById(R.id.txtEvents);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            iconCalendar = (ImageView) itemView.findViewById(R.id.iconCalendar);
           // iconCalendar.setVisibility(View.GONE);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            iconLayout = (LinearLayout) itemView.findViewById(R.id.iconLayout);
            deleteLayout = (RelativeLayout) itemView.findViewById(R.id.deleteLayout);
            deleteLayout.setVisibility(View.GONE);
            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);
            txtLinks = (TextView) itemView.findViewById(R.id.txtLinks);
            txtMicrophone = (TextView) itemView.findViewById(R.id.txtMicrophone);
            txtVideo = (TextView) itemView.findViewById(R.id.txtVideo);
            txtCamera = (TextView) itemView.findViewById(R.id.txtCamera);

            iconMicrophone = (ImageView) itemView.findViewById(R.id.iconMicrophone);
            iconMicrophone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (podcastAdapterLayout.getVisibility() == View.GONE){
                        podcastAdapterLayout.setVisibility(View.VISIBLE);
                    } else {
                        podcastAdapterLayout.setVisibility(View.GONE);
                    }
                }
            });
            iconVideo = (ImageView) itemView.findViewById(R.id.iconVideo);
            iconVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoAdapterLayout.getVisibility() == View.GONE){
                        videoAdapterLayout.setVisibility(View.VISIBLE);
                    } else {
                        videoAdapterLayout.setVisibility(View.GONE);
                    }
                }
            });
            iconCamera = (ImageView) itemView.findViewById(R.id.iconCamera);
            iconCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photoAdapterLayout.getVisibility() == View.GONE){
                        photoAdapterLayout.setVisibility(View.VISIBLE);
                    } else {
                        photoAdapterLayout.setVisibility(View.GONE);
                    }
                }
            });
            iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);

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

    }
}
}
