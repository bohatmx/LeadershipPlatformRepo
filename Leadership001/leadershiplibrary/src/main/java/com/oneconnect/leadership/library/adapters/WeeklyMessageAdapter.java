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
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.List;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final WeeklyMessageDTO wm = mList.get(position);
        final WeeklyMessageViewHolder wmvh = (WeeklyMessageViewHolder) holder;
        wmvh.txtEvents.setText("" + position);
        wmvh.txtTitle.setText(wm.getTitle());
        wmvh.txtSubtitle.setText(wm.getSubtitle());
        wmvh.txtDate.setText(wm.getStringDateScheduled());

        if (wm.getVideos() != null) {
            wmvh.txtVideo.setText("" + wm.getVideos().size());
        }
        if (wm.getPhotos() != null) {
            wmvh.txtCamera.setText("" + wm.getPhotos().size());
        }
        if (wm.getPodcasts() != null) {
            wmvh.txtMicrophone.setText("" + wm.getPodcasts().size());
        }
        if (wm.getUrls() != null) {
            wmvh.txtLinks.setText("" + wm.getUrls().size());
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class WeeklyMessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtEvents, txtTitle, txtDate, txtSubtitle, txtLinks, txtMicrophone,
                txtVideo, txtCamera;
        protected ImageView iconCalendar, iconUpdate, iconDelete, iconMicrophone, iconVideo, iconCamera;
        protected RelativeLayout bottomLayout;
        protected LinearLayout iconLayout;
        protected RelativeLayout deleteLayout, linksLayout, micLayout, videosLayout, photosLayout;

        public WeeklyMessageViewHolder(View itemView) {
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
