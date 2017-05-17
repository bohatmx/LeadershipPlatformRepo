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
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.WebViewActivity;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.io.File;
import java.util.List;

/**
 * Created by Nkululeko on 2017/04/19.
 */

public class EbookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EBookDTO> mList;
    private Context ctx;
    private EbookAdapterListener listener;

    public interface EbookAdapterListener {
        void onReadClicked(String path);
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

        final EBookDTO v = mList.get(position);
        final EbookViewHolder vvh = (EbookViewHolder) holder;
        String displayName = v.getStorageName().split("\\.", 2)[0];
        int i = displayName.lastIndexOf("/");
        //

        //
        vvh.fileName.setText(displayName.substring(i + 1));
        final String bookUrl = v.getUrl();
        vvh.bookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(ctx, WebViewActivity.class);
                    intent.putExtra("links", bookUrl/*mList.get(position).getUrls()*/);
                    ctx.startActivity(intent);
                //listener.onReadClicked(bookUrl);
                //File f = new File(bookUrl);
                //if (f.exists()) {
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(bookUrl), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    ctx.startActivity(intent);*/
              //  }
            }
        });

        vvh.iconshar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });
        /*vvh.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener.onReadClicked(book);
                *//*readEbook(bookUrl);*//*
                File f = new File(bookUrl);
                if (f.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(f), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                }
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

    private void readEbook(String path) {
        File f = new File(path);
       if (f.exists()) {
            //Intent intent = new Intent(Intent.ACTION_VIEW);
            //intent.setDataAndType(Uri.fromFile(f), "application/pdf");
           // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           // ctx.startActivity(intent);


        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
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
        protected ImageView image, bookIcon, iconshar;
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
        }
    }
}

