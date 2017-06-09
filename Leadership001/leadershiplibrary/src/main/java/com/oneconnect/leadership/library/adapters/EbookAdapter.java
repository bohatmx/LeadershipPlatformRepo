package com.oneconnect.leadership.library.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
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
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;
import com.oneconnect.leadership.library.util.Util;


import java.io.File;
import java.io.IOException;
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
    MiniPhotoAdapter miniPhotoAdapter;
    MiniPodcastAdapter miniPodcastAdapter;
    MiniVideoAdapter miniVideoAdapter;
    UrlAdapter urlAdapter;

    public interface EbookAdapterListener {
        void onReadClicked(String path);
        void onThoughtClicked(int position);
        void onPhotoRequired(PhotoDTO photo);
        void onVideoRequired(VideoDTO video);
        void onPodcastRequired(PodcastDTO podcast);
        void onUrlRequired(UrlDTO url);
        void onPhotosRequired(List<PhotoDTO> list);
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

    private MediaPlayer mediaPlayer;
    String url;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final EBookDTO v = mList.get(position);
        final EbookViewHolder vvh = (EbookViewHolder) holder;
        final String displayName = v.getStorageName().split("\\.", 2)[0];
        int i = displayName.lastIndexOf("/");

        //
        //vvh.txtEvents.setText("" + (position + 1));
        //vvh.txtTitle.setText(v.getTitle());
        vvh.iconCamera.setImageDrawable(ctx.getDrawable(R.drawable.ic_photo_black_24dp));
        vvh.iconUpdate.setImageDrawable(ctx.getDrawable(R.drawable.ic_link_black_24dp));

        if (v.getVideos() != null) {
            vvh.txtVideo.setText("" + v.getVideos().size());
            vvh.iconVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vvh.videoAdapterLayout.getVisibility() == View.GONE){
                        vvh.videoAdapterLayout.setVisibility(View.VISIBLE);
                        vvh.videoRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        vvh.videoAdapterLayout.setVisibility(View.GONE);
                        vvh.videoRecyclerView.setVisibility(View.GONE);
                    }

                    EBookDTO dtd = mList.get(position);
                    List<VideoDTO> videoList = new ArrayList<>();
                    Map map = dtd.getVideos();
                    VideoDTO vDTO;
                    for (Object value : map.values()) {
                        vDTO = (VideoDTO) value;
                        videoList.add(vDTO);
                    }

                    miniVideoAdapter = new MiniVideoAdapter(videoList, ctx, new MiniVideoAdapter.MiniVideoAdapterListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onPause() {

                        }

                        @Override
                        public void onStop() {

                        }
                    });

                    vvh.videoRecyclerView.setAdapter(miniVideoAdapter);
                    vvh.btnPlay.setVisibility(View.GONE);

                    vvh.btnPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            });
        }

        if (v.getPhotos() != null) {
            vvh.txtCamera.setText("" + v.getPhotos().size());
            vvh.iconCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vvh.photoAdapterLayout.getVisibility() == View.GONE){
                        vvh.photoAdapterLayout.setVisibility(View.VISIBLE);
                        vvh.imageRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        vvh.photoAdapterLayout.setVisibility(View.GONE);
                        vvh.imageRecyclerView.setVisibility(View.GONE);
                    }

                    EBookDTO dtd = mList.get(position);
                    List<PhotoDTO> urlList = new ArrayList<>();

                    Map map = dtd.getPhotos();
                    PhotoDTO vDTO;
                    String photoUrl;
                    for (Object value : map.values()) {
                        vDTO = (PhotoDTO) value;
                        photoUrl = vDTO.getUrl();
                        urlList.add(vDTO);

                        Glide.with(ctx)
                                .load(photoUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(vvh.photoView);
                        vvh.captiontxt.setText(vDTO.getCaption());
                    }

                    miniPhotoAdapter = new MiniPhotoAdapter(urlList, ctx, new PhotoAdapter.PhotoAdapterlistener() {
                        @Override
                        public void onPhotoClicked(PhotoDTO photo) {

                        }
                    });
                    vvh.imageRecyclerView.setAdapter(miniPhotoAdapter);
                }
            });
        }
        if (v.getPodcasts() != null) {
            vvh.txtMicrophone.setText("" + v.getPodcasts().size());
            vvh.iconMicrophone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vvh.podcastAdapterLayout.getVisibility() == View.GONE){
                        vvh.podcastAdapterLayout.setVisibility(View.VISIBLE);
                        vvh.podcastRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        vvh.podcastAdapterLayout.setVisibility(View.GONE);
                        vvh.podcastRecyclerView.setVisibility(View.GONE);
                    }
                    EBookDTO dtd = mList.get(position);
                    List<PodcastDTO> podcastList = new ArrayList<>();
                    Map map = dtd.getPodcasts();
                    PodcastDTO vDTO;
                    for (Object value : map.values()) {
                        vDTO = (PodcastDTO) value;
                        url = vDTO.getUrl();
                        podcastList.add(vDTO);
                        //
                        int i = vDTO.getStorageName().lastIndexOf("/");
                        vvh.podcastfileName.setText(vDTO.getStorageName().substring(i + 1));
                        //
                        vvh.playIMG.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                vvh.playIMG.setVisibility(View.GONE);
                                vvh.pauseIMG.setVisibility(View.VISIBLE);
                                vvh.stopIMG.setVisibility(View.VISIBLE);
                                try {
                                    mediaPlayer.setDataSource(url);
                                } catch (IllegalArgumentException e) {
                                    Log.e(LOG, "You might not set the URI correctly!");
                                } catch (SecurityException e) {
                                    Log.e(LOG, "You might not set the URI correctly!");
                                } catch (IllegalStateException e) {
                                    Log.e(LOG, "You might not set the URI correctly!");
                                } catch (IOException e) {
                                    Log.e(LOG, e.getMessage());
                                }
                                try {
                                    mediaPlayer.prepare();
                                } catch (IllegalStateException e) {
                                    Log.e(LOG, "You might not set the URI correctly!");
                                } catch (IOException e) {
                                    Log.e(LOG, "You might not set the URI correctly!");
                                }
                                mediaPlayer.start();
                            }
                        });
                        //

                        //
                        vvh.pauseIMG.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mediaPlayer.pause();
                                vvh.pauseIMG.setVisibility(View.GONE);
                                vvh.playIMG.setVisibility(View.VISIBLE);
                                vvh.stopIMG.setVisibility(View.VISIBLE);
                            }
                        });

                        vvh.stopIMG.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mediaPlayer.stop();
                                vvh.playIMG.setVisibility(View.VISIBLE);
                                vvh.pauseIMG.setVisibility(View.GONE);
                                vvh.stopIMG.setVisibility(View.GONE);
                            }
                        });
                        //
                    }

                    miniPodcastAdapter = new MiniPodcastAdapter(podcastList, ctx, new PodcastAdapter.PodcastAdapterListener() {
                        @Override
                        public void onPlayClicked(int position) {

                        }

                        @Override
                        public void onPodcastRequired(PodcastDTO podcast) {

                        }
                    });
                    vvh.podcastRecyclerView.setAdapter(miniPodcastAdapter);
                }
            });

        }

        if (v.getUrls() != null) {
            vvh.txtLinks.setText("" + v.getUrls().size());
            vvh.iconUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vvh.urlAdapterLayout.getVisibility() == View.GONE){
                        vvh.urlAdapterLayout.setVisibility(View.VISIBLE);
                        vvh.urlRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        vvh.urlAdapterLayout.setVisibility(View.GONE);
                        vvh.urlRecyclerView.setVisibility(View.GONE);
                    }

                        EBookDTO dtd = mList.get(position);
                    Map map = dtd.getUrls();
                    UrlDTO vDTO;
                    final List<UrlDTO> urlList = new ArrayList<>();
                    String url;
                    for (Object value : map.values()) {
                        vDTO = (UrlDTO) value;
                        url = vDTO.getUrl();
                        vvh.urlTxt.setText(url);
                        urlList.add(vDTO);
                    }

                    urlAdapter = new UrlAdapter(urlList, ctx, new UrlAdapter.UrlAdapterListener() {
                        @Override
                        public void onUrlClicked(final String url) {
                        }
                    });

                    vvh.urlRecyclerView.setAdapter(urlAdapter);
                }
            });
        }


        vvh.fileName.setText(displayName.substring(i + 1));
        final String bookUrl = v.getUrl();
        vvh.bookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(vvh.bookIcon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        String path = displayName;
                        FirebaseStorageAPI fbs = new FirebaseStorageAPI();
                        int i = displayName.lastIndexOf("/");
                        fbs.downloadEbook(bookUrl, displayName.substring(i + 1), path,  ctx);
                    }
                });
            }
        });

        vvh.readIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(vvh.readIcon, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        String path = displayName;
                        FirebaseStorageAPI fbs = new FirebaseStorageAPI();
                        int i = displayName.lastIndexOf("/");
                        fbs.downloadEbook(bookUrl, displayName.substring(i + 1), path,  ctx);
                    }
                });
            }
        });


        vvh.iconshar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(vvh.iconshar, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        shareIt();
                    }
                });
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
        protected TextView txtEvents, txtTitle, txtDate, txtSubtitle, txtLinks, txtMicrophone,
                txtVideo, txtCamera, captiontxt, /*videoFileName,*/ podcastfileName, urlTxt;
        protected ImageView image, bookIcon, iconshar, uploadIcon,readIcon, imageUploadIcon;
        protected Button btnPlay, btnUpload;
        protected RelativeLayout deleteLayout, linksLayout, micLayout, videosLayout, photosLayout, podcastAdapterLayout, videoAdapterLayout,
                photoAdapterLayout, urlAdapterLayout, bottomLayout;
        protected RecyclerView imageRecyclerView, videoRecyclerView, urlRecyclerView, podcastRecyclerView;
        protected ImageView iconCalendar, iconUpdate, iconDelete, iconMicrophone, iconVideo, iconCamera, photoView,
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
            imageUploadIcon.setVisibility(View.GONE);
            uploadIcon = (ImageView) itemView.findViewById(R.id.uploadIcon);
            uploadIcon.setVisibility(View.GONE);
            readIcon = (ImageView) itemView.findViewById(R.id.readIcon);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            bottomLayout.setVisibility(View.GONE);

            iconMicrophone = (ImageView) itemView.findViewById(R.id.iconMicrophone);

            iconVideo = (ImageView) itemView.findViewById(R.id.iconVideo);


            iconCamera = (ImageView) itemView.findViewById(R.id.iconCamera);

            iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);

            imageRecyclerView = (RecyclerView) itemView.findViewById(R.id.imageRecyclerView);
            imageRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            imageRecyclerView.setLayoutManager(llm);
            imageRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            imageRecyclerView.setHasFixedSize(true);
            //

            videoRecyclerView = (RecyclerView) itemView.findViewById(R.id.videoRecyclerView);
            videoRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm2 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            videoRecyclerView.setLayoutManager(llm2);
            videoRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            videoRecyclerView.setHasFixedSize(true);

            urlRecyclerView = (RecyclerView) itemView.findViewById(R.id.urlRecyclerView);
            urlRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm3 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            urlRecyclerView.setLayoutManager(llm3);
            urlRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            urlRecyclerView.setHasFixedSize(true);

            podcastRecyclerView = (RecyclerView) itemView.findViewById(R.id.podcastRecyclerView);
            podcastRecyclerView.setVisibility(View.GONE);
            LinearLayoutManager llm4 = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
            podcastRecyclerView.setLayoutManager(llm4);
            podcastRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(ctx));
            podcastRecyclerView.setHasFixedSize(true);
            // layouts
            podcastAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.podcastAdapterLayout);
            //

            //
            videoAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.videoAdapterLayout);
            //

            //
            photoAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.photoAdapterLayout);
            photoView = (ImageView) itemView.findViewById(R.id.photoView);
            captiontxt = (TextView) itemView.findViewById(R.id.captiontxt);
            //

            urlAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.urlAdapterLayout);
            urlTxt = (TextView) itemView.findViewById(R.id.urlTxt);
        }
    }
    static final String LOG = EbookAdapter.class.getSimpleName();
}

