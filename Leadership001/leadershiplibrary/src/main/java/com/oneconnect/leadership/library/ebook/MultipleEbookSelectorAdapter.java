package com.oneconnect.leadership.library.ebook;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.util.Util;

import java.util.List;

/**
 * Created by nkululekochrisskosana on 2017/08/14.
 */

public class MultipleEbookSelectorAdapter extends /*ArrayAdapter*/BaseAdapter/*<EBookDTO>*/ {

    private List<String> paths;
    private List<EBookDTO> mList;
    private Context ctx;
    private EbookAdapterSelectorListener listener;
    EBookDTO eBook;
    LayoutInflater inflater;
    boolean isServerList;
    //
    TextView txtFileName;
    Button btnUpload, btnPlay;
    RelativeLayout bottomLayout;
    ImageView uploadIcon, readIcon, bookIcon, imageUploadIcon, eBookMenu;
    CheckBox checkbox;

    public interface EbookAdapterSelectorListener {
        void onUploadEbook(String path);

        void onReadEbook(String path);

        void onAttachPhoto(EBookDTO ebook);

        void onPhotoUpload(BaseDTO base/*String path*/);
    }

    public MultipleEbookSelectorAdapter(Context context, List<String> paths, List<EBookDTO> mList, EbookAdapterSelectorListener listener) {
        this.ctx = context;
        this.paths = paths;
        inflater = LayoutInflater.from(ctx);
        this.listener = listener;
        this.mList = mList;

    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        final EBookDTO v = mList.get(position);
        final ViewHolder holder;
        if(mList != null){
            eBook = mList.get(position);
        }
        //View view = null;
        final String path = paths.get(position);
        View view = inflater.inflate(R.layout.ebook_item, parent, false);
        /*checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        checkbox.setVisibility(View.GONE);

            txtFileName = (TextView) view.findViewById(R.id.fileName);
            uploadIcon = (ImageView) view.findViewById(R.id.uploadIcon);
                        uploadIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Util.flashOnce(uploadIcon, 300, new Util.UtilAnimationListener() {
                                    @Override
                                    public void onAnimationEnded() {
                                        listener.onUploadEbook(path);
                                    }
                                });
                            }
                        });
            readIcon = (ImageView) view.findViewById(R.id.readIcon);
           bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
        bookIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                checkbox.setVisibility(View.VISIBLE);
                checkbox.isChecked();

                return true;
            }
        });*/
            /*holder.*/imageUploadIcon = (ImageView) view.findViewById(R.id.imageUploadIcon);
            /*holder.*/imageUploadIcon.setVisibility(View.GONE);
            /*holder.*/bottomLayout = (RelativeLayout) view.findViewById(R.id.bottomLayout);
            /*holder.*/bottomLayout.setVisibility(View.GONE);
            /*holder.*/btnUpload = (Button) view.findViewById(R.id.btnUpload);
            /*holder.*/eBookMenu = (ImageView) view.findViewById(R.id.ebook_menu);
            /*holder.*/eBookMenu.setVisibility(View.GONE);
        if (convertView == null) {
            holder = new ViewHolder();
           // View view = inflater.inflate(R.layout.ebook_item, parent, false);

            holder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            holder.checkbox.setVisibility(View.GONE);
            holder.txtFileName = (TextView) view.findViewById(R.id.fileName);
            holder.uploadIcon = (ImageView) view.findViewById(R.id.uploadIcon);
            holder.readIcon = (ImageView) view.findViewById(R.id.readIcon);
            holder.bookIcon = (ImageView) view.findViewById(R.id.bookIcon);
            holder.bookIcon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    checkbox.setVisibility(View.VISIBLE);
                    checkbox.isChecked();

                    return true;
                }
            });
            holder.imageUploadIcon = (ImageView) view.findViewById(R.id.imageUploadIcon);
            holder.imageUploadIcon.setVisibility(View.GONE);
            holder.bottomLayout = (RelativeLayout) view.findViewById(R.id.bottomLayout);
            holder.bottomLayout.setVisibility(View.GONE);
            holder.btnUpload = (Button) view.findViewById(R.id.btnUpload);
            holder.eBookMenu = (ImageView) view.findViewById(R.id.ebook_menu);
            holder.eBookMenu.setVisibility(View.GONE);
            view.setTag(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }



        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        if(paths != null){
            return paths.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return paths.size();
    }

    public class ViewHolder {
         TextView txtFileName;
         Button btnUpload, btnPlay;
         RelativeLayout bottomLayout;
         ImageView uploadIcon, readIcon, bookIcon, imageUploadIcon, eBookMenu;
        CheckBox checkbox;

    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public void setServerList(boolean serverList) {
        isServerList = serverList;
    }

    public void setmList(List<EBookDTO> mList) {
        this.mList = mList;
    }


}
