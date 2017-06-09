package com.oneconnect.leadership.admin.ebook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.admin.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by Nkululeko on 2017/05/24.
 */

public class AdminEbookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EBookDTO> mList;
    private Context ctx;
    private EbookAdapterListener listener;

    public interface EbookAdapterListener {
        void onReadClicked(String path);
        void onAttachPhoto(EBookDTO ebook);
    }

    public AdminEbookAdapter(List<EBookDTO> mList, Context ctx, EbookAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ebook_item, parent, false);
        return new EbookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final EBookDTO v = mList.get(position);
        final EbookViewHolder vvh = (EbookViewHolder) holder;
        final String displayName = v.getStorageName().split("\\.", 2)[0];
        int i = displayName.lastIndexOf("/");
        //

        //
        vvh.fileName.setText(displayName.substring(i + 1));
        final String bookUrl = v.getUrl();
        if(v != null){
            if(v.getCoverUrl() != null){
                Glide.with(ctx)
                        .load(v.getCoverUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(vvh.bookIcon);
            }
        }
        /*vvh.bookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = displayName;
                FirebaseStorageAPI fbs = new FirebaseStorageAPI();
                int i = displayName.lastIndexOf("/");
                fbs.downloadEbook(bookUrl, displayName.substring(i + 1), path,  ctx);
            }
        });*/

        vvh.iconshar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

        vvh.imageUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(vvh.imageUploadIcon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onAttachPhoto(v);
                       /* Intent intent = new Intent(ctx, PhotoSelectionActivity.class);
                        intent.putExtra("type", type);
                        switch (type) {
                            case ResponseBag.EBOOKS:
                                eBook = (EBookDTO) base;
                                intent.putExtra("ebook", eBook);
                                break;
                        }
                        ctx.startActivity(intent);*/
                    }
                });
            }
        });

    }

    EBookDTO eBook;
    BaseDTO base;

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
        protected ImageView image, bookIcon, iconshar, imageUploadIcon, uploadIcon, readIcon;
        protected Button btnPlay, btnUpload;

        public EbookViewHolder(View itemView) {
            super(itemView);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            btnPlay.setVisibility(View.GONE);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnUpload.setVisibility(View.GONE);
            bookIcon = (ImageView) itemView.findViewById(R.id.bookIcon);
            iconshar = (ImageView) itemView.findViewById(R.id.iconshar);
            imageUploadIcon = (ImageView) itemView.findViewById(R.id.imageUploadIcon);
            uploadIcon = (ImageView) itemView.findViewById(R.id.uploadIcon);
            uploadIcon.setVisibility(View.GONE);
            readIcon = (ImageView) itemView.findViewById(R.id.readIcon);
            readIcon.setVisibility(View.GONE);
        }
    }
}
