package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PhotoDTO;

import java.util.List;

/**
 * Created by Nkululeko on 2017/05/05.
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PhotoDTO> mList;
    private Context ctx;
    private PhotoAdapterlistener listener;


    public interface PhotoAdapterlistener{
        void onPhotoClicked(int position);

    }

    public PhotoAdapter(List<PhotoDTO> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final PhotoDTO p = mList.get(position);
        final PhotoViewHolder pvh = (PhotoViewHolder) holder;
        pvh.captiontxt.setText(p.getCaption());
        Glide.with(ctx)
                .load(p.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pvh.photoView);
        pvh.txtDate.setText(p.getStringDate());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }



    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        protected TextView captiontxt, txtDate;
        protected ImageView photoView;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            captiontxt = (TextView) itemView.findViewById(R.id.captiontxt);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtDate.setVisibility(View.GONE);
            photoView = (ImageView) itemView.findViewById(R.id.photoView);

        }
    }
}
