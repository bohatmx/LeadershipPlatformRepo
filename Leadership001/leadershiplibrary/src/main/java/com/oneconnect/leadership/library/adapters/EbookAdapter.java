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
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.util.List;

/**
 * Created by Nkululeko on 2017/04/19.
 */

public class EbookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EBookDTO> mList;
    private Context ctx;
    private EbookAdapterListener listener;

    public interface EbookAdapterListener {
        void onReadClicked();
    }

    public EbookAdapter(List<EBookDTO> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ebook_item, parent, false);
        return new EbookAdapter.EbookViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final EBookDTO v = mList.get(position);
        final EbookViewHolder vvh = (EbookViewHolder) holder;
        //vvh.fileName.setText(v.getUrl());
        vvh.image.setImageDrawable(ctx.getDrawable(R.drawable.ic_clipboard));
        vvh.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.onReadClicked();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public class EbookViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileName;
        protected ImageView image;
        protected Button btnPlay, btnUpload;

        public EbookViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            fileName.setVisibility(View.GONE);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnUpload.setVisibility(View.GONE);

        }
    }
}

