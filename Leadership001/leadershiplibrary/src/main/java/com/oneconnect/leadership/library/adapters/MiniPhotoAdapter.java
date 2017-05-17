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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final PhotoDTO p = mList.get(position);
        final MiniPhotoViewHolder pvh = (MiniPhotoViewHolder) holder;
        pvh.captiontxt.setText(p.getCaption());
        Glide.with(ctx)
                .load(p.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pvh.photoView);

        /*pvh.iconshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });*/
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
            photoView = (ImageView) itemView.findViewById(R.id.photoView);

        }
    }
}
