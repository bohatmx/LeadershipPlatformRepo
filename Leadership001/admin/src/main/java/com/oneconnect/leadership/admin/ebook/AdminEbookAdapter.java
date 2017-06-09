package com.oneconnect.leadership.admin.ebook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.admin.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.adapters.MiniPhotoAdapter;
import com.oneconnect.leadership.library.adapters.MiniPodcastAdapter;
import com.oneconnect.leadership.library.adapters.MiniVideoAdapter;
import com.oneconnect.leadership.library.adapters.PhotoAdapter;
import com.oneconnect.leadership.library.adapters.PodcastAdapter;
import com.oneconnect.leadership.library.adapters.UrlAdapter;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.R.attr.type;

/**
 * Created by Nkululeko on 2017/05/24.
 */

public class AdminEbookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EBookDTO> mList;
    private Context ctx;
    private EbookAdapterListener listener;
    MiniPhotoAdapter miniPhotoAdapter;
    MiniPodcastAdapter miniPodcastAdapter;
    MiniVideoAdapter miniVideoAdapter;
    UrlAdapter urlAdapter;

    public interface EbookAdapterListener {
        void onReadClicked(String path);
        void onAttachPhoto(EBookDTO ebook);
        void onThoughtClicked(int position);
        void onPhotoRequired(BaseDTO entity);
        void onVideoRequired(BaseDTO entity);
        void onPodcastRequired(BaseDTO entity);
        void onUrlRequired(BaseDTO entity);
        void onPhotosRequired(List<PhotoDTO> list);
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

    private MediaPlayer mediaPlayer;
    String url;

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final EBookDTO v = mList.get(position);
        final EbookViewHolder vvh = (EbookViewHolder) holder;
        final String displayName = v.getStorageName().split("\\.", 2)[0];
        int i = displayName.lastIndexOf("/");
        //
        vvh.iconCamera.setImageDrawable(ctx.getDrawable(com.oneconnect.leadership.library.R.drawable.ic_photo_black_24dp));
        vvh.iconUpdate.setImageDrawable(ctx.getDrawable(com.oneconnect.leadership.library.R.drawable.ic_link_black_24dp));

        vvh.iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(vvh.iconCamera, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onAttachPhoto(v);
                    }
                });
            }
        });

        vvh.iconMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPodcastRequired(v/*v.getPodcast()*/);
            }
        });

        vvh.iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVideoRequired(v);
            }
        });

        vvh.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUrlRequired(v);
            }
        });

        vvh.fileName.setText(displayName.substring(i + 1));
        final String bookUrl = v.getUrl();


        vvh.bookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = displayName;
                FirebaseStorageAPI fbs = new FirebaseStorageAPI();
                int i = displayName.lastIndexOf("/");
                fbs.downloadEbook(bookUrl, displayName.substring(i + 1), path,  ctx);
            }
        });

        //
        vvh.fileName.setText(displayName.substring(i + 1));
        final String url = v.getUrl();

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
        protected TextView captiontxt, urlTxt;
        protected RelativeLayout podcastAdapterLayout, videoAdapterLayout,
                photoAdapterLayout, urlAdapterLayout;
        protected RecyclerView imageRecyclerView, videoRecyclerView, urlRecyclerView, podcastRecyclerView;
        protected ImageView iconUpdate, iconMicrophone, iconVideo, iconCamera, photoView,
                playIMG, pauseIMG, stopIMG;

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

            iconMicrophone = (ImageView) itemView.findViewById(com.oneconnect.leadership.library.R.id.iconMicrophone);

            iconVideo = (ImageView) itemView.findViewById(com.oneconnect.leadership.library.R.id.iconVideo);

            iconCamera = (ImageView) itemView.findViewById(com.oneconnect.leadership.library.R.id.iconCamera);

            iconUpdate = (ImageView) itemView.findViewById(com.oneconnect.leadership.library.R.id.iconUpdate);

            imageRecyclerView = (RecyclerView) itemView.findViewById(com.oneconnect.leadership.library.R.id.imageRecyclerView);
            imageRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            imageRecyclerView.setLayoutManager(llm);
            imageRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            imageRecyclerView.setHasFixedSize(true);
            //

            videoRecyclerView = (RecyclerView) itemView.findViewById(com.oneconnect.leadership.library.R.id.videoRecyclerView);
            videoRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm2 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            videoRecyclerView.setLayoutManager(llm2);
            videoRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            videoRecyclerView.setHasFixedSize(true);

            urlRecyclerView = (RecyclerView) itemView.findViewById(com.oneconnect.leadership.library.R.id.urlRecyclerView);
            urlRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm3 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            urlRecyclerView.setLayoutManager(llm3);
            urlRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            urlRecyclerView.setHasFixedSize(true);

            podcastRecyclerView = (RecyclerView) itemView.findViewById(com.oneconnect.leadership.library.R.id.podcastRecyclerView);
            podcastRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm4 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            podcastRecyclerView.setLayoutManager(llm4);
            podcastRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            podcastRecyclerView.setHasFixedSize(true);
            // layouts
            podcastAdapterLayout = (RelativeLayout) itemView.findViewById(com.oneconnect.leadership.library.R.id.podcastAdapterLayout);
            //

            //
            videoAdapterLayout = (RelativeLayout) itemView.findViewById(com.oneconnect.leadership.library.R.id.videoAdapterLayout);
            //

            //
            photoAdapterLayout = (RelativeLayout) itemView.findViewById(com.oneconnect.leadership.library.R.id.photoAdapterLayout);
            photoView = (ImageView) itemView.findViewById(com.oneconnect.leadership.library.R.id.photoView);
            captiontxt = (TextView) itemView.findViewById(com.oneconnect.leadership.library.R.id.captiontxt);
            //

            urlAdapterLayout = (RelativeLayout) itemView.findViewById(com.oneconnect.leadership.library.R.id.urlAdapterLayout);
            urlTxt = (TextView) itemView.findViewById(com.oneconnect.leadership.library.R.id.urlTxt);
        }

    }
    static final String LOG = AdminEbookAdapter.class.getSimpleName();
}
