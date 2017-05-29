package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
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
        void onPhotoClicked(PhotoDTO photo);

    }

    public PhotoAdapter(List<PhotoDTO> mList, Context ctx, PhotoAdapterlistener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
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

        pvh.iconshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

        pvh.shareTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });
    }
    private void shareIt() {
        //sharing implementation here
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Leadership Platform");
        String sAux = "\nLet me recommend you this application\n\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.minisass&hl=en \n\n";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        ctx.startActivity(Intent.createChooser(i, "choose one"));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }



    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        protected TextView captiontxt, txtDate, shareTxt, likeCountTxt;
        protected ImageView photoView, iconshare, likeIMG;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            captiontxt = (TextView) itemView.findViewById(R.id.captiontxt);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtDate.setVisibility(View.GONE);
            photoView = (ImageView) itemView.findViewById(R.id.photoView);
            iconshare = (ImageView) itemView.findViewById(R.id.shareIcon);
            shareTxt = (TextView) itemView.findViewById(R.id.shareTxt);
           /* likeIMG = (ImageView) itemView.findViewById(R.id.likeIMG);
            likeIMG.setVisibility(View.GONE);
            likeCountTxt = (TextView) itemView.findViewById(R.id.likeCountTxt);
            likeCountTxt.setVisibility(View.GONE);*/


        }
    }
}
