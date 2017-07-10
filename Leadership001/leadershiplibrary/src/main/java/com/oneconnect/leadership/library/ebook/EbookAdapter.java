package com.oneconnect.leadership.library.ebook;

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
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by Nkululeko on 2017/04/12.
 */

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.EbookViewHolder> {

    private List<String> paths;
    private Context context;
    private EbookAdapterListener listener;
    EBookDTO eBook;

    public interface EbookAdapterListener {
        void onUploadEbook(String path);
        void onReadEbook(String path);
        void onPhotoUpload(BaseDTO base/*String path*/);
    }

    public EbookAdapter(List<String> paths, Context context, EBookDTO book, EbookAdapterListener listener) {
        this.paths = paths;
        this.context = context;
        this.listener = listener;
        this.eBook = book;
    }

    @Override
    public EbookAdapter.EbookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ebook_item, parent, false);
        return new EbookAdapter.EbookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EbookViewHolder holder, int position) {

        final String path = paths.get(position);
        File file = new File(path);
        Log.w("EbookAdapter", "onBindViewHolder: ".concat(path).concat(" size: ").concat(String.valueOf(file.length())) );
        int i = path.lastIndexOf("/");
        holder.txtFileName.setText(path.substring(i + 1));

        holder.uploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUploadEbook(path);
            }
        });

        holder.readIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReadEbook(path);
            }
        });

        holder.imageUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, PhotoSelectionActivity.class);
                context.startActivity(intent);*/
                //listener.onPhotoUpload(path);
                Util.flashOnce(holder.imageUploadIcon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        /*Intent intent = new Intent(context, PhotoSelectionActivity.class);
                        intent.putExtra("type", type);
                        switch (type) {
                            case ResponseBag.EBOOKS:
                                eBook = (EBookDTO) base;
                                intent.putExtra("ebook", eBook);
                                break;
                        }
                        context.startActivity(intent);*/
                        //startPhotoGallerySelection(path);
                        listener.onPhotoUpload(eBook);
                    }
                });

            }
        });

       /* holder.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUploadEbook(path);
            }
        });
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReadEbook(path);
            }
        });*/
    }

    BaseDTO base;

    private void startPhotoGallerySelection(BaseDTO base){
        Intent intent = new Intent(context, PhotoSelectionActivity.class);
        intent.putExtra("type", type);
        switch (type) {
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) base;
                intent.putExtra("ebook", eBook);
                break;
        }
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class EbookViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtFileName;
        protected Button btnUpload, btnPlay;
        protected RelativeLayout bottomLayout;
        protected ImageView uploadIcon, readIcon, bookIcon, imageUploadIcon;

        public EbookViewHolder(View itemView) {
            super(itemView);
            txtFileName = (TextView) itemView.findViewById(R.id.fileName);
            uploadIcon = (ImageView) itemView.findViewById(R.id.uploadIcon);
            readIcon = (ImageView) itemView.findViewById(R.id.readIcon);
            bookIcon = (ImageView) itemView.findViewById(R.id.bookIcon);
            imageUploadIcon = (ImageView) itemView.findViewById(R.id.imageUploadIcon);
            imageUploadIcon.setVisibility(View.GONE);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            bottomLayout.setVisibility(View.GONE);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            //btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
        }

    }

}
