package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.FullPhotoActivity;
import com.oneconnect.leadership.library.activities.PodcastPlayerActivity;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.util.Util;

import java.util.List;

/**
 * Created by Nkululeko on 2017/05/10.
 */

public class MiniPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PhotoDTO> mList;
    private Context ctx;
    private PhotoAdapter.PhotoAdapterlistener listener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_item, parent, false);
        return new MiniPhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final PhotoDTO p = mList.get(position);
        final MiniPhotoViewHolder pvh = (MiniPhotoViewHolder) holder;
        pvh.captiontxt.setText(p.getCaption());
        Glide.with(ctx)
                .load(p.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pvh.photoView);

        pvh.photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(pvh.photoView, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, FullPhotoActivity.class);
                        intent.putExtra("photo", p);
                        ctx.startActivity(intent);
                        /*Intent intent = new Intent(ctx, FullPhotoActivity.class);
                        intent.putExtra("photo", p);
                        ctx.startActivity(intent);*/
                    }
                });
            }
        });

        /*pvh.iconshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });*/
    }

    private void shareIt() {
        PhotoDTO p = new PhotoDTO();
        //sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse("file://" + p.getFilePath());

        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        ctx.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public interface PhotoAdapterlistener{
        void onPhotoClicked(PhotoDTO photo);

    }

    public MiniPhotoAdapter(List<PhotoDTO> mList, Context ctx, PhotoAdapter.PhotoAdapterlistener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }


    public class MiniPhotoViewHolder extends RecyclerView.ViewHolder {
        protected TextView captiontxt;
        protected ImageView photoView, iconshare;

        public MiniPhotoViewHolder(View itemView) {
            super(itemView);

            captiontxt = (TextView) itemView.findViewById(R.id.captiontxt);
            captiontxt.setVisibility(View.GONE);
            photoView = (ImageView) itemView.findViewById(R.id.photoView);

        }
    }
}
