package com.oneconnect.leadership.library.adapters;

import android.content.Context;
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
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Kurisani on 2017/05/02.
 */

public class MasterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<WeeklyMasterClassDTO> mList;
    private Context ctx;
    private MasterAdapterListener listener;


    public interface MasterAdapterListener{
       void onMasterClicked(int position);
    }

    public MasterAdapter(Context ctx, List<WeeklyMasterClassDTO> mList){
        this.ctx = ctx;
        this.mList = mList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item_two,parent, false);
        return new MasterViewHolder(v);
    }

    static final SimpleDateFormat sd1 = new SimpleDateFormat(" dd-MM-yyyy HH:mm");

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final WeeklyMasterClassDTO dt = mList.get(position);
        final MasterViewHolder dvh = (MasterViewHolder) holder;
        dvh.txtEvents.setText("" + position);
        dvh.txtTitle.setText(dt.getTitle());
        dvh.txtSubtitle.setText(dt.getSubtitle());
        dvh.txtDate.setText(dt.getStringDateScheduled());
        dvh.iconCamera.setImageDrawable(ctx.getDrawable(R.drawable.ic_photo_black_24dp));
        dvh.iconUpdate.setImageDrawable(ctx.getDrawable(R.drawable.ic_link_black_24dp));

        if (dt.getVideos() != null) {
            dvh.txtVideo.setText("" + dt.getVideos().size());
        }
        if (dt.getPhotos() != null) {
            dvh.txtCamera.setText("" + dt.getPhotos().size());
            dvh.iconCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dvh.photoAdapterLayout.setVisibility(View.VISIBLE);
                    WeeklyMasterClassDTO dtd = mList.get(position);
                    Map map = dtd.getPhotos();
                    PhotoDTO vDTO;
                    String url;
                    for (Object value : map.values()) {
                        vDTO = (PhotoDTO) value;
                        url = vDTO.getUrl();
                        Glide.with(ctx)
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(dvh.photoView);
                        dvh.captiontxt.setText(vDTO.getCaption());
                    }
                }
            });
        }
        if (dt.getPodcasts() != null) {
            dvh.txtMicrophone.setText("" + dt.getPodcasts().size());
        }

        if (dt.getUrls() != null) {
            dvh.txtLinks.setText("" + dt.getUrls().size());
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
    public class MasterViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtEvents, txtTitle, txtDate, txtSubtitle, txtLinks, txtMicrophone,
                txtVideo, txtCamera, captiontxt;
        protected ImageView iconCalendar, iconUpdate, iconDelete, iconMicrophone, iconVideo, iconCamera, photoView;
        protected RelativeLayout bottomLayout;
        protected LinearLayout iconLayout;
        protected RelativeLayout deleteLayout, linksLayout, micLayout, videosLayout, photosLayout, podcastAdapterLayout, videoAdapterLayout, photoAdapterLayout;

        public MasterViewHolder(View itemView) {
            super(itemView);
            txtEvents = (TextView) itemView.findViewById(R.id.txtEvents);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            iconCalendar = (ImageView) itemView.findViewById(R.id.iconCalendar);
            iconCalendar.setVisibility(View.GONE);
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
