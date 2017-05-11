package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.WebViewActivity;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.UrlDTO;

import java.util.List;

/**
 * Created by Nkululeko on 2017/05/10.
 */

public class UrlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UrlDTO> mList;
    private Context ctx;
    private UrlAdapterListener listener;

    public UrlAdapter(List<UrlDTO> mList, Context ctx, UrlAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    public interface UrlAdapterListener{
        void onUrlClicked(String url);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.url_item, parent, false);
        return new UrlViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final UrlDTO u = mList.get(position);
        final UrlViewHolder uvh = (UrlViewHolder) holder;

        uvh.urlTxt.setText(u.getTitle());


        uvh.urlTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, WebViewActivity.class);
                intent.putExtra("url", u.getUrl());
                ctx.startActivity(intent);
            }
        });

        //listener.onUrlClicked(u.getUrl());

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class UrlViewHolder extends RecyclerView.ViewHolder {
        protected TextView urlTxt;
        public UrlViewHolder(View itemView) {
            super(itemView);

            urlTxt = (TextView) itemView.findViewById(R.id.urlTxt);
        }
    }
}
