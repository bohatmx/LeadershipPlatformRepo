package com.oneconnect.leadership.library.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Nkululeko on 2017/05/05.
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PhotoDTO> mList;
    private Context ctx;
    private PhotoAdapterlistener listener;
    private  boolean isCoverPhoto;
    private EBookDTO eBookDTO;
    private DataAPI dataAPI;
    private Snackbar snackbar;


    public interface PhotoAdapterlistener{
        void onPhotoClicked(PhotoDTO photo);

    }

    public PhotoAdapter(List<PhotoDTO> mList, Context ctx, boolean isCoverPhoto, EBookDTO book, PhotoAdapterlistener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
        this.isCoverPhoto = isCoverPhoto;
        eBookDTO = book;
        dataAPI = new DataAPI();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final PhotoDTO p = mList.get(position);
        final PhotoViewHolder pvh = (PhotoViewHolder) holder;
        pvh.captiontxt.setText(p.getCaption());
        Glide.with(ctx)
                .load(p.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pvh.photoView);
        if(isCoverPhoto){
            pvh.uploadIMG.setVisibility(View.VISIBLE);
            pvh.iconshare.setVisibility(View.GONE);
            pvh.shareTxt.setVisibility(View.GONE);
            pvh.uploadIMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    Util.flashOnce(v, 300, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {
                            PhotoDTO photoDTO = mList.get(position);
                            if(isCoverPhoto){
                                confirmUpload(photoDTO);
                            }
                        }
                    });
                    v.setEnabled(true);
                   }
            });
        }else{
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

    }
    public Bitmap mBitmap;
    private void shareIt() {

        Bitmap icon = mBitmap;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
        ctx.startActivity(Intent.createChooser(share, "Share Image"));



    }

    private void confirmUpload(final PhotoDTO photoDTO) {
        AlertDialog.Builder b = new AlertDialog.Builder(ctx);
        b.setTitle("Confirmation")
                .setMessage("Do you want to upload this cover photo to the database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //showSnackbar("Uploading cover photo ...", "OK", Constants.CYAN);
                        dataAPI.updateEBook(eBookDTO.geteBookID(), photoDTO.getUrl(), new DataAPI.DataListener() {
                            @Override
                            public void onResponse(String key) {
                                ((Activity) ctx).finish();
                            }

                            @Override
                            public void onError(String message){}
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    public void showSnackbar(String title, String action, String color) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }



    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        protected TextView captiontxt, txtDate, shareTxt, likeCountTxt;
        protected ImageView photoView, iconshare, likeIMG, uploadIMG;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            captiontxt = (TextView) itemView.findViewById(R.id.captiontxt);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtDate.setVisibility(View.GONE);
            photoView = (ImageView) itemView.findViewById(R.id.photoView);
            iconshare = (ImageView) itemView.findViewById(R.id.shareIcon);
            shareTxt = (TextView) itemView.findViewById(R.id.shareTxt);
            uploadIMG =  (ImageView) itemView.findViewById(R.id.uploadIMG);
           /* likeIMG = (ImageView) itemView.findViewById(R.id.likeIMG);
            likeIMG.setVisibility(View.GONE);
            likeCountTxt = (TextView) itemView.findViewById(R.id.likeCountTxt);
            likeCountTxt.setVisibility(View.GONE);*/


        }
    }
}
