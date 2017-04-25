package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.util.List;

/**
 * Created by Nkululeko on 2017/04/19.
 */

public class PodcastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PodcastDTO> mList;
    private Context ctx;
    private PodcastAdapterListener listener;

    public interface PodcastAdapterListener {
        void onPlayClicked();
    }

    public PodcastAdapter(List<PodcastDTO> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent, false);
        return new PodcastAdapter.PodcastsViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final PodcastDTO v = mList.get(position);
        final PodcastsViewHolder pvh = (PodcastsViewHolder) holder;
        pvh.fileName.setText(v.getStorageName());
        pvh.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPlayClicked();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public class PodcastsViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileName;
        protected ImageView image;
        protected Button btnPlay, btnUpload;

        public PodcastsViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnUpload.setVisibility(View.GONE);

        }
    }

}
