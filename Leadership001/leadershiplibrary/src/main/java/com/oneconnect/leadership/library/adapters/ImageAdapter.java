package com.oneconnect.leadership.library.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PhotoDTO;

import java.util.List;

/**
 * Created by Nkululeko on 2017/07/24.
 */

public class ImageAdapter extends BaseAdapter/*ArrayAdapter<PhotoDTO>*/{

    private List<String> paths;
    private /*Context*/Activity context;
    private ImageAdapterListener listener;
    private LayoutInflater mInflater;
    private int mLayoutRes;

    public ImageAdapter(/*Context*/Activity context, List<String> paths,
                        ImageAdapterListener listener) {
        this.context = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.paths = paths;
        this.listener = listener;
    }

    public interface ImageAdapterListener {
        void onUploadPhoto(String path, int position);
        void onViewPhoto(String path);
        void onCheckedItem(String path, int numberChecked);
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageViewHolder item;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.photo_item, null);
            item = new ImageViewHolder();
            item.photoView = (ImageView) convertView.findViewById(R.id.photoView);
            item.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            item.shareIcon = (ImageView) convertView.findViewById(R.id.shareIcon);
            item.captiontxt = (TextView) convertView.findViewById(R.id.captiontxt);
            //item.shareIcon.setVisibility(View.GONE);
            item.shareTxt = (TextView) convertView.findViewById(R.id.shareTxt);
            //item.shareTxt.setVisibility(View.GONE);

            convertView.setTag(item);
        } else {
            item = (ImageViewHolder) convertView.getTag();
        }

        final String path = paths.get(position);
        item.captiontxt.setText(path);
        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countCheck(isChecked);
               // showSnackbar("Selected books: " + checkAccumulator + "", "Upload Selected","green");
                //  holder.bookCounterTxt.setText(checkAccumulator + "");
                Log.i(LOG, checkAccumulator + "" + "\n" +"Image path: " + path);
                for (String p : paths) {
                    p = path;
                    listener.onCheckedItem(p, checkAccumulator);
               //     listener.onUploadEbook(p);

                }
            }
        };

        item.checkBox.setOnCheckedChangeListener(checkListener);
       /* if (item.checkBox.isChecked())
        {

        }*/
        item.photoView.setImageBitmap(BitmapFactory.decodeFile(path));

        /*Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(item.photoView);*/


        return (convertView);

    }

    int checkAccumulator = 0;
    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1;
    }

    static class ImageViewHolder {
        protected ImageView photoView, shareIcon;
        protected TextView txtDate, shareTxt, captiontxt;
        protected CheckBox checkBox;
    }
    static final String LOG = ImageAdapter.class.getSimpleName();
}
