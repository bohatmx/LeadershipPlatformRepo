package com.oneconnect.leadership.library.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.WebViewActivity;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Nkululeko on 2017/04/19.
 */

public class EbookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EBookDTO> mList;
    private Context ctx;
    private EbookAdapterListener listener;

    public interface EbookAdapterListener {
        void onReadClicked(String path);
        void onPldpRequired(EBookDTO ebook);
    }

    public EbookAdapter(List<EBookDTO> mList, Context ctx, EbookAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ebook_item, parent, false);
        return new EbookAdapter.EbookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final EBookDTO eBook = mList.get(position);
        final EbookViewHolder vvh = (EbookViewHolder) holder;
        final String displayName = eBook.getStorageName().split("\\.", 2)[0];
        int i = displayName.lastIndexOf("/");

        vvh.fileName.setText(displayName.substring(i + 1));
        final String bookUrl = eBook.getUrl();
        vvh.bookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReadClicked(bookUrl);
                /*String path = displayName;
                FirebaseStorageAPI fbs = new FirebaseStorageAPI();
                int i = displayName.lastIndexOf("/");
                fbs.downloadEbook(bookUrl, displayName.substring(i + 1), path,  ctx);*/

            }
        });

        vvh.iconPldp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPldpRequired(eBook);
            }
        });

        if(eBook != null){
            if(eBook.getCoverUrl() != null){
                Glide.with(ctx)
                        .load(eBook.getCoverUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(vvh.bookIcon);
            }
        }

        vvh.iconshar.setOnClickListener(new View.OnClickListener() {
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

    private void readEbook(File path) {
       if (path.exists()) {
           Intent target = new Intent(Intent.ACTION_VIEW);
           target.setDataAndType(Uri.fromFile(path),"application/pdf");
           target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
           Intent intent = Intent.createChooser(target, "Open File");
        try {
            ctx.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }

       }
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public class EbookViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileName;
        protected ImageView image, bookIcon, iconshar, uploadIcon, readIcon, imageUploadIcon, eBookMenu, iconDelete, iconPldp;
        protected Button btnPlay, btnUpload;
        protected RelativeLayout bottomLayout;
        protected CheckBox checkBox;

        public EbookViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            checkBox.setVisibility(View.GONE);
            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);
            iconDelete.setVisibility(View.GONE);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            btnPlay.setVisibility(View.GONE);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnUpload.setVisibility(View.GONE);
            bookIcon = (ImageView) itemView.findViewById(R.id.bookIcon);
            iconshar = (ImageView) itemView.findViewById(R.id.iconshar);
            imageUploadIcon = (ImageView) itemView.findViewById(R.id.imageUploadIcon);
            imageUploadIcon.setVisibility(View.GONE);
            uploadIcon = (ImageView) itemView.findViewById(R.id.uploadIcon);
            uploadIcon.setVisibility(View.GONE);
            readIcon = (ImageView) itemView.findViewById(R.id.readIcon);
            readIcon.setVisibility(View.GONE);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            bottomLayout.setVisibility(View.GONE);
            eBookMenu = (ImageView) itemView.findViewById(R.id.ebook_menu);
            eBookMenu.setVisibility(View.GONE);
            iconPldp = (ImageView) itemView.findViewById(R.id.iconPldp);

        }
    }
}

