package com.oneconnect.leadership.library.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;

import java.io.File;
import java.util.List;

/**
 * Created by Nkululeko on 2017/05/11.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<String> paths;
    private Context context;
    private PhotoAdapterListener listener;

    public PhotoAdapter(List<String> paths, Context context, PhotoAdapterListener listener) {
        this.paths = paths;
        this.context = context;
        this.listener = listener;
    }

    public interface PhotoAdapterListener {
        void onUploadPhoto(String path);
        void onViewPhoto(String path);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_admin_item, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {

        final String path = paths.get(position);
        File file = new File(path);
        Log.w("PhotoAdapter", "onBindViewHolder: ".concat(path).concat(" size: ").concat(String.valueOf(file.length())) );
        /*Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
        holder.image.setImageBitmap(thumb);*/
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
        //holder.image.setImageDrawable(context.getDrawable(R.drawable.ic_camera));
        int i = path.lastIndexOf("/");
        holder.photoFileNametxt.setText(path.substring(i + 1));
        holder.uploadIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUploadPhoto(path);
            }
        });
        /*holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReadEbook(path);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        protected TextView photoFileNametxt;
        protected ImageView image, uploadIMG;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            photoFileNametxt = (TextView) itemView.findViewById(R.id.photoFileNametxt);
            image = (ImageView) itemView.findViewById(R.id.image);
            uploadIMG = (ImageView) itemView.findViewById(R.id.uploadIMG);
        }

    }
}
