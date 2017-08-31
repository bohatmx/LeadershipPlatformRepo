package com.oneconnect.leadership.library.ebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
    List<EBookDTO> mList;
    int checkAccumulator = 0;



    boolean isServerList;

    public interface EbookAdapterListener {
        void onUploadEbook(String path);

        void onReadEbook(String path);

        void onAttachPhoto(EBookDTO ebook);

        void onPhotoUpload(BaseDTO base/*String path*/);

        void onDeleteEbook(EBookDTO eBook);
    }

    public EbookAdapter(List<String> paths, Context context, List<EBookDTO> mList, EbookAdapterListener listener) {
        this.paths = paths;
        this.context = context;
        this.listener = listener;
        this.mList = mList;
    }

    @Override
    public EbookAdapter.EbookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ebook_item, parent, false);
        return new EbookAdapter.EbookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EbookViewHolder holder, final int position) {
        if(mList != null){
            eBook = mList.get(position);
        }
        final String path = paths.get(position);
        //File file = new File(path);
        //.w("EbookAdapter", "onBindViewHolder: ".concat(path).concat(" size: ").concat(String.valueOf(file.length())) );
        //int i = path.lastIndexOf("/");
        //holder.txtFileName.setText(path.substring(i + 1));
        if (isServerList) {
            holder.eBookMenu.setVisibility(View.VISIBLE);
            holder.uploadIcon.setVisibility(View.GONE);
            holder.readIcon.setVisibility(View.GONE);
            holder.imageUploadIcon.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.GONE);
            holder.iconDelete.setVisibility(View.VISIBLE);
            holder.iconDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.flashOnce(holder.iconDelete, 300, new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {
                            listener.onDeleteEbook(eBook);
                        }
                    });
                }
            });
        } else{
            holder.iconDelete.setVisibility(View.GONE);
        }





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
        holder.eBookMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(holder.imageUploadIcon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onAttachPhoto(mList.get(position));

                    }
                });
            }
        });
        if(eBook != null){
            if(eBook.getCoverUrl() != null){
                Glide.with(context)
                        .load(eBook.getCoverUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.bookIcon);
            }
        }

       /* if (holder.checkbox.isChecked()) {
            holder.uploadIcon.setVisibility(View.GONE);
            Toast.makeText(context, "counter: " + holder.checkbox.);
        }*/

        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countCheck(isChecked);
                showSnackbar("Selected books: " + checkAccumulator + "", "Upload Selected","green");
              //  holder.bookCounterTxt.setText(checkAccumulator + "");
                Log.i(LOG, checkAccumulator + "" + "\n" +"Book path: " + path);
                for (String p : paths) {
                    p = path;
                    listener.onUploadEbook(p);

                }
            }
        };

        holder.checkbox.setOnCheckedChangeListener(checkListener);

        holder.bookIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.iconDelete.setVisibility(View.GONE);
                holder.checkbox.setVisibility(View.VISIBLE);
                holder.checkbox.isChecked();

                return false;
            }
        });
    }

    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1;
    }
    public Snackbar snackbar;
    RelativeLayout mainLay;

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(mainLay, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // snackbar.dismiss();

            }
        });
        snackbar.show();

    }



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


    BaseDTO base;

    private void startPhotoGallerySelection(BaseDTO base) {
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
        if(paths != null){
            return paths.size();
        }else{
            return 0;
        }
    }

    public class EbookViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtFileName, bookCounterTxt;
        protected Button btnUpload, btnPlay;
        protected RelativeLayout bottomLayout;
        protected ImageView uploadIcon, readIcon, bookIcon, imageUploadIcon, eBookMenu, iconDelete;
        protected CheckBox checkbox;

        public EbookViewHolder(View itemView) {
            super(itemView);
      //      bookCounterTxt = (TextView) itemView.findViewById(R.id.bookCounterTxt);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            checkbox.setVisibility(View.GONE);
            txtFileName = (TextView) itemView.findViewById(R.id.fileName);
            uploadIcon = (ImageView) itemView.findViewById(R.id.uploadIcon);
            readIcon = (ImageView) itemView.findViewById(R.id.readIcon);
            bookIcon = (ImageView) itemView.findViewById(R.id.bookIcon);
            imageUploadIcon = (ImageView) itemView.findViewById(R.id.imageUploadIcon);
            imageUploadIcon.setVisibility(View.GONE);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            bottomLayout.setVisibility(View.GONE);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            eBookMenu = (ImageView) itemView.findViewById(R.id.ebook_menu);
            eBookMenu.setVisibility(View.GONE);
            mainLay = (RelativeLayout) itemView.findViewById(R.id.mainLay);
            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);

        }

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


    static final String LOG = EbookAdapter.class.getSimpleName();
}
