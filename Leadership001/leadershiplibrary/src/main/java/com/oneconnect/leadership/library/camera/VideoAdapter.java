package com.oneconnect.leadership.library.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.oneconnect.leadership.library.R;

import java.io.File;
import java.util.List;

/**
 * Created by aubreymalabie on 3/29/17.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<String> paths;
    private Context context;
    private VideoAdapterListener listener;

    public interface VideoAdapterListener {
        void onPlayVideoTapped(String path);
        void onUploadVideoTapped(String path);
    }

    public VideoAdapter(List<String> paths, Context context, VideoAdapterListener listener) {
        this.paths = paths;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoViewHolder holder, int position) {
        final String path = paths.get(position);
        File file = new File(path);
        Log.w("VideoAdapter", "onBindViewHolder: ".concat(path).concat(" size: ").concat(String.valueOf(file.length())) );
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
        holder.image.setImageBitmap(thumb);
        int i = path.lastIndexOf("/");
        holder.txtFileName.setText(path.substring(i + 1));
        holder.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUploadVideoTapped(path);
            }
        });
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPlayVideoTapped(path);
            }
        });

    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtFileName;
        protected ImageView image;
        protected Button btnUpload, btnPlay;

        public VideoViewHolder(View itemView) {
            super(itemView);
            txtFileName = (TextView) itemView.findViewById(R.id.fileName);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
        }

    }

}
