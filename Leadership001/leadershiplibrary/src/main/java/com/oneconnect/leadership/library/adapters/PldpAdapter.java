package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PldpDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nkululekochrisskosana on 2017/11/30.
 */

public class PldpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PldpDTO> mList;
    private Context ctx;
    private PldpAdapterListener listener;
    /*MiniPhotoAdapter miniPhotoAdapter;
    MiniPodcastAdapter miniPodcastAdapter;
    MiniVideoAdapter miniVideoAdapter;
    UrlAdapter urlAdapter;*/
    private PhotoDTO photoDTO;
    private UserDTO userDTO;
    private int type;

    public interface PldpAdapterListener {
        void onDailyThoughtSelected(DailyThoughtDTO dailyThought);
        void onWeeklyMasterClassSelected(WeeklyMasterClassDTO masterClass);
        void onPodcastSelected(PodcastDTO podcast);
        void onVideoSelected(VideoDTO video);
        void onEbookSelected(EBookDTO ebook);
        void onArticleSelected(NewsDTO news);
    }

    public PldpAdapter(Context ctx, List<PldpDTO> mList, PldpAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pldp_item, parent, false);
        return new PldpViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final PldpDTO pldp = mList.get(position);
        final PldpViewHolder pvh = (PldpViewHolder) holder;

        pvh.pldpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pldp.getNews() != null) {
                    listener.onArticleSelected(pldp.getNews());
                } else if (pldp.getPodcast() != null) {
                    listener.onPodcastSelected(pldp.getPodcast());
                } else if (pldp.getVideo() != null) {
                    listener.onVideoSelected(pldp.getVideo());
                } else if (pldp.getWeeklyMasterClass() != null) {
                    listener.onWeeklyMasterClassSelected(pldp.getWeeklyMasterClass());
                } else if (pldp.getDailyThought() != null) {
                    listener.onDailyThoughtSelected(pldp.getDailyThought());
                } else if (pldp.geteBook() != null) {
                    listener.onEbookSelected(pldp.geteBook());
                }
            }
        });

        if (pldp.getNews() != null) {
            String s1 = pldp.getActionName();
            String action = s1.replaceAll("," , " * \n ");
            if (pldp.getNews().getPhotos() != null){
                List<PhotoDTO> urlList = new ArrayList<>();

                Map map = pldp.getNews().getPhotos();
                PhotoDTO vDTO;
                String photoUrl;
                for (Object value : map.values()) {
                    vDTO = (PhotoDTO) value;
                    photoUrl = vDTO.getUrl();
                    urlList.add(vDTO);

                    Glide.with(ctx)
                            .load(photoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(pvh.image);
                }
            } else {
                pvh.image.setVisibility(View.GONE);
            }
            pvh.titleText.setText(pldp.getNews().getTitle()/*.concat(" - ").concat(pldp.getNews().getSubtitle())*/);
            pvh.actionsText.setText(action/*pldp.getActionName()*/);
            pvh.videoView.setVisibility(View.GONE);
            pvh.audioLayout.setVisibility(View.GONE);
            pvh.audioText.setVisibility(View.GONE);
        }
        if (pldp.getDailyThought() != null) {
            String s1 = pldp.getActionName();
            String action = s1.replaceAll("," , " * \n ");
            if (pldp.getDailyThought().getPhotos() != null){
                List<PhotoDTO> urlList = new ArrayList<>();

                Map map = pldp.getDailyThought().getPhotos();
                PhotoDTO vDTO;
                String photoUrl;
                for (Object value : map.values()) {
                    vDTO = (PhotoDTO) value;
                    photoUrl = vDTO.getUrl();
                    urlList.add(vDTO);

                    Glide.with(ctx)
                            .load(photoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(pvh.image);
                }
            } else {
                pvh.image.setVisibility(View.GONE);
            }
            pvh.titleText.setText(pldp.getDailyThought().getTitle().concat(" - ").concat(pldp.getDailyThought().getSubtitle()));
            pvh.actionsText.setText(action);
            pvh.videoView.setVisibility(View.GONE);
            pvh.audioLayout.setVisibility(View.GONE);
            pvh.audioText.setVisibility(View.GONE);
        }

        if (pldp.getWeeklyMasterClass() != null) {
            String s1 = pldp.getActionName();
            String action = s1.replaceAll("," , " * \n ");
            if (pldp.getWeeklyMasterClass().getPhotos() != null){
                List<PhotoDTO> urlList = new ArrayList<>();

                Map map = pldp.getWeeklyMasterClass().getPhotos();
                PhotoDTO vDTO;
                String photoUrl;
                for (Object value : map.values()) {
                    vDTO = (PhotoDTO) value;
                    photoUrl = vDTO.getUrl();
                    urlList.add(vDTO);

                    Glide.with(ctx)
                            .load(photoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(pvh.image);
                }
            } else {
                pvh.image.setVisibility(View.GONE);
            }
            pvh.titleText.setText(pldp.getWeeklyMasterClass().getTitle().concat(" - ").concat(pldp.getWeeklyMasterClass().getSubtitle()));
            pvh.actionsText.setText(action/*pldp.getActionName()*/);
            pvh.videoView.setVisibility(View.GONE);
            pvh.audioLayout.setVisibility(View.GONE);
            pvh.audioText.setVisibility(View.GONE);
        }
        if (pldp.getPodcast() != null) {
            String s1 = pldp.getActionName();
            String action = s1.replaceAll("," , " * \n ");
            if (pldp.getPodcast().getPhotos() != null){
                List<PhotoDTO> urlList = new ArrayList<>();

                Map map = pldp.getPodcast().getPhotos();
                PhotoDTO vDTO;
                String photoUrl;
                for (Object value : map.values()) {
                    vDTO = (PhotoDTO) value;
                    photoUrl = vDTO.getUrl();
                    urlList.add(vDTO);

                    Glide.with(ctx)
                            .load(photoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(pvh.image);
                }
            }
            int i = pldp.getPodcast().getStorageName().lastIndexOf("/");
            pvh.titleText.setText(pldp.getPodcast().getStorageName().substring(i + 1));
            pvh.titleText.setVisibility(View.GONE);
            pvh.fileName.setText(pldp.getPodcast().getStorageName().substring(i + 1));

           // pvh.titleText.setText(pldp.getPodcast().getStorageName());
            pvh.actionsText.setText(action/*pldp.getActionName()*/);
            pvh.actionsText.setVisibility(View.GONE);
            pvh.videoView.setVisibility(View.GONE);
            pvh.image.setVisibility(View.GONE);
            pvh.audioText.setText(action);
        }

        if (pldp.getVideo() != null) {
            int i = pldp.getVideo().getStorageName().lastIndexOf("/");
            try {
                Uri video = Uri.parse(pldp.getVideo().getUrl());
                pvh.videoView.setVideoURI(video);
                pvh.videoView.seekTo(300);
            } catch (Exception e) {
                Log.e(LOG,"Video something went wrong: " + e.getMessage());
            }
            /*pvh.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    pvh.videoSeekBar.setMax(pvh.videoView.getDuration());
                    // vvh.videoSeekBar.postDelayed(onEverySecond, 1000);

                }
            });*/

            pvh.videoView.requestFocus();
            pvh.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // pvh.videoView.start();
                    listener.onVideoSelected(pldp.getVideo());
                }
            });
          //  pvh.titleText.setText(pldp.getVideo().getTitle().concat(" - ").concat(pldp.getNews().getSubtitle()));
            pvh.actionsText.setText(pldp.getActionName().replaceAll(",", "\n"));
            pvh.image.setVisibility(View.GONE);
            pvh.audioLayout.setVisibility(View.GONE);
            pvh.audioText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class PldpViewHolder extends RecyclerView.ViewHolder {
        protected TextView titleText, actionsText, duration, fileName, audioText;
        protected VideoView videoView;
        protected ImageView image;
        protected RelativeLayout podControlLayout, audioLayout;
        protected CardView pldpCard;

        public PldpViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.titleText);
            actionsText = (TextView) itemView.findViewById(R.id.actionsText);
            videoView =  (VideoView) itemView.findViewById(R.id.videoView);
            image = (ImageView) itemView.findViewById(R.id.image);

            // audio
            duration = (TextView) itemView.findViewById(R.id.duration);
            duration.setVisibility(View.GONE);
            podControlLayout = (RelativeLayout) itemView.findViewById(R.id.podControlLayout);
            podControlLayout.setVisibility(View.GONE);
            audioLayout = (RelativeLayout) itemView.findViewById(R.id.audioLayout);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
            audioText = (TextView) itemView.findViewById(R.id.audioText);

            //
            pldpCard = (CardView) itemView.findViewById(R.id.pldpCard);
        }
    }


    static final String LOG = PldpAdapter.class.getSimpleName();
}
