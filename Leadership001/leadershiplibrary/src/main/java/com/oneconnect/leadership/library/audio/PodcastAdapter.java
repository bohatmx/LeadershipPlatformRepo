package com.oneconnect.leadership.library.audio;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;

import java.io.File;
import java.util.List;

/**
 * Created by Nkululeko on 2017/04/11.
 */

public class PodcastAdapter  extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {

    private List<String> paths;
    private Context context;
    private AudioAdapterListener listener;

    public interface AudioAdapterListener {
        void onPlayAudioTapped(String path);
        void onUploadAudioTapped(String path);
    }

    public PodcastAdapter(List<String> paths, Context context, AudioAdapterListener listener) {
        this.paths = paths;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public PodcastAdapter.PodcastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.audio_item, parent, false);
        return new PodcastAdapter.PodcastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PodcastViewHolder holder, int position) {
        final String path = paths.get(position);
        File file = new File(path);
        Log.w("PodcastAdapter", "onBindViewHolder: ".concat(path).concat(" size: ").concat(String.valueOf(file.length())) );
        /*Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Audio.Thumbnails.MINI_KIND);
        holder.image.setImageBitmap(thumb);*/
       // holder.image.setImageDrawable(context.getDrawable(R.drawable.audios));
        int i = path.lastIndexOf("/");
        holder.txtFileName.setText(path.substring(i + 1));
        holder.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUploadAudioTapped(path);
            }
        });
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPlayAudioTapped(path);
            }
        });

       /* holder.btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,AudioRecordTest.class);
                context.startActivity(i);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class PodcastViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtFileName, initialTime, finalTime;
        protected ImageView image;
        protected Button btnUpload, btnPlay, btnRec;
        protected RelativeLayout controlLayout, bottomLayout;

        public PodcastViewHolder(View itemView) {
            super(itemView);
            txtFileName = (TextView) itemView.findViewById(R.id.fileName);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            bottomLayout.setVisibility(View.GONE);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            controlLayout = (RelativeLayout) itemView.findViewById(R.id.controlLayout);
           // controlLayout.setVisibility(View.GONE);
          // btnRec = (Button) itemView.findViewById(R.id.btnRec);
            //initialTime = (TextView) itemView.findViewById(R.id.initialTime);
            //initialTime.setVisibility(View.GONE);
            //finalTime = (TextView) itemView.findViewById(R.id.finalTime);
           // finalTime.setVisibility(View.GONE);
        }

    }

}
