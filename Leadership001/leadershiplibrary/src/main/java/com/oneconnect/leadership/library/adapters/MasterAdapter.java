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

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.List;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final WeeklyMasterClassDTO dt = mList.get(position);
        final MasterViewHolder dvh = (MasterViewHolder) holder;
        dvh.txtEvents.setText("" + position);
        dvh.txtTitle.setText(dt.getTitle());
        dvh.txtSubtitle.setText(dt.getSubtitle());
        dvh.txtDate.setText(dt.getStringDateScheduled());

        if (dt.getVideos() != null) {
            dvh.txtVideo.setText("" + dt.getVideos().size());
        }
        if (dt.getPhotos() != null) {
            dvh.txtCamera.setText("" + dt.getPhotos().size());
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
                txtVideo, txtCamera;
        protected ImageView iconCalendar, iconUpdate, iconDelete, iconMicrophone, iconVideo, iconCamera;
        protected RelativeLayout bottomLayout;
        protected LinearLayout iconLayout;
        protected RelativeLayout deleteLayout, linksLayout, micLayout, videosLayout, photosLayout;

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


        }
    }

}