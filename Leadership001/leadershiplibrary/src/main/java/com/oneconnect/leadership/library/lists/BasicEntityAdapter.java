package com.oneconnect.leadership.library.lists;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.TextViewExpandableAnimation;
import com.oneconnect.leadership.library.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by aubreyM on 14/12/17.
 */
public class BasicEntityAdapter extends RecyclerView.Adapter<BasicEntityAdapter.EntityViewHolder> {

    public interface EntityListener {
        void onAddEntity();
        void onDeleteClicked(BaseDTO entity);
        void onDeleteUser(UserDTO user);
        void onDeleteDailyThought(DailyThoughtDTO dailyThought);
        void onDeleteWeeklyMessage(WeeklyMessageDTO weeklyMessage);
        void onDeleteWeeklyMasterClass(WeeklyMasterClassDTO masterClass);
        void onDeletePodcast(PodcastDTO podcast);
        void onDeleteNews(NewsDTO news);
        void onDeleteVideo(VideoDTO video);
        void onDeleteEbook(EBookDTO eBook);
        void onDeleteCategory(CategoryDTO category);
        void onLinksRequired(BaseDTO entity);
        void onPhotoCaptureRequested(BaseDTO entity);
        void onVideoCaptureRequested(BaseDTO entity);
        void onSomeActionRequired(BaseDTO entity);
        void onMicrophoneRequired(BaseDTO entity);
        void onEntityClicked(BaseDTO entity);
        void onCalendarRequested(BaseDTO entity);
        void onEntityDetailRequested(BaseDTO entity, int type);
        void onDeleteTooltipRequired(int type);
        void onLinksTooltipRequired(int type);
        void onPhotoCaptureTooltipRequired(int type);
        void onVideoCaptureTooltipRequired(int type);
        void onSomeActionTooltipRequired(int type);
        void onMicrophoneTooltipRequired(int type);
        void onCalendarTooltipRequired(int type);
        void onNewsArticleRequested(BaseDTO entity);
        void onUpdateUser(UserDTO user);
        void onUpdateDailyThought(DailyThoughtDTO dailyThought);
        void onUpdateWeeklyMessage(WeeklyMessageDTO weeklyMessage);
        void onUpdateWeeklyMasterClass(WeeklyMasterClassDTO masterClass);
        void onUpdateNews(NewsDTO news);
        void onUpdateCategory(CategoryDTO category);
    }

    private EntityListener mListener;
    private List<BaseDTO> entities;
    private Context ctx;
    int type;
    public static final DecimalFormat df = new DecimalFormat("###,###,###,###,##0.0");
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm");


    public BasicEntityAdapter(List<BaseDTO> entities, int type,
                              Context ctx, EntityListener listener) {
        this.entities = entities;
        this.mListener = listener;
        this.ctx = ctx;
        this.type = type;
    }

    @Override
    public EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_item_two, parent, false);
        return new EntityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EntityViewHolder h, final int position) {

        final BaseDTO p = entities.get(position);

        h.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h.bottomLayout.getVisibility() == View.GONE) {
                    animateIn(h.bottomLayout, p);
                } else {
                    animateOut(h.bottomLayout, p);
                }
            }
        });
        h.titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(h.titleLayout, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (h.bottomLayout.getVisibility() == View.GONE) {
                            h.bottomLayout.setVisibility(View.VISIBLE);

                        } else {
                            h.bottomLayout.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });
        h.txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h.bottomLayout.getVisibility() == View.GONE) {
                    animateIn(h.bottomLayout, p);
                } else {
                    animateOut(h.bottomLayout, p);
                }
            }
        });


        h.iconLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLinksRequired(p);
            }
        });
        h.iconLink.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onLinksTooltipRequired(type);
                return false;
            }
        });
        //
        h.iconLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSomeActionRequired(p);
            }
        });
        h.iconLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onSomeActionTooltipRequired(type);
                return false;
            }
        });
        //
        h.iconPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPhotoCaptureRequested(p);
            }
        });
        h.iconPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onPhotoCaptureTooltipRequired(type);
                return false;
            }
        });
        //
        h.iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onVideoCaptureRequested(p);
            }
        });
        h.iconVideo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onVideoCaptureTooltipRequired(type);
                return false;
            }
        });
        //
        h.iconMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMicrophoneRequired(p);
            }
        });
        h.iconMicrophone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onMicrophoneTooltipRequired(type);
                return false;
            }
        });
//
        h.iconCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCalendarRequested(p);
            }
        });
        h.iconCalendar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onCalendarTooltipRequired(type);
                return false;
            }
        });

      /*  h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteClicked(p);
            }
        });*/

        h.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h.bottomLayout.getVisibility() == View.GONE) {
                    animateIn(h.bottomLayout, p);
                } else {
                    animateOut(h.bottomLayout, p);
                }
            }
        });
        h.txtLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEntityDetailRequested(p, type);
            }
        });
        h.txtPodcasts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEntityDetailRequested(p, type);
            }
        });
        h.txtPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEntityDetailRequested(p, type);
            }
        });
        h.txtVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEntityDetailRequested(p, type);
            }
        });

        switch (type) {
            case ResponseBag.CATEGORIES:
                setCategories(h, position, (CategoryDTO) p);
                break;


            case ResponseBag.NEWS:
                setNews(h, position, (NewsDTO) p);
                break;

            case ResponseBag.DAILY_THOUGHTS:
                setDailyThought(h, position, (DailyThoughtDTO) p);
                break;
            case ResponseBag.EBOOKS:
                setEBook(h, position + 1, (EBookDTO) p);
                break;

            case ResponseBag.PODCASTS:
                setPodcast(h, position, (PodcastDTO) p);
                break;

            case ResponseBag.PRICES:
                setPrice(h, position, (PriceDTO) p);
                break;

            case ResponseBag.USERS:
                setUser(h, position, (UserDTO) p);
                break;

            case ResponseBag.WEEKLY_MASTERCLASS:
                setWeeklyMasterclass(h, position + 1, (WeeklyMasterClassDTO) p);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                setWeeklyMessage(h, position + 1, (WeeklyMessageDTO) p);
                break;
        }


    }

    private void setPrice(EntityViewHolder h, int position, PriceDTO p) {
        h.txtTitle.setText(p.getProductName());
        h.txtDate.setVisibility(View.GONE);
        h.txtSubTitle.setVisibility(View.GONE);
        h.txtNumBlack.setVisibility(View.VISIBLE);
        h.txtNumBlack.setText(String.valueOf(position + 1));
        h.iconLayout.setVisibility(View.GONE);
    }

    private void setCategories(final EntityViewHolder h, int position,final CategoryDTO p) {
        h.txtTitle.setText(p.getCategoryName());
        h.txtDate.setVisibility(View.GONE);
        h.txtSubTitle.setVisibility(View.GONE);
        h.txtNumBlack.setVisibility(View.GONE);
        h.txtLinks.setVisibility(View.GONE);
        h.txtupdate.setVisibility(View.GONE);
        h.iconLink.setVisibility(View.GONE);
        h.iconShare.setVisibility(View.GONE);
        h.iconRate.setVisibility(View.GONE);
        h.txtNumBlack.setText(String.valueOf(position + 1));
        h.iconLayout.setVisibility(View.VISIBLE);
       // h.iconDelete.setVisibility(View.VISIBLE);
        //h.iconLinks.setVisibility(View.VISIBLE);
        h.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(h.iconUpdate, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onUpdateCategory(p);
                    }
                });
            }
        });
        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(h.iconDelete, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onDeleteCategory(p);
                    }
                });
            }
        });
    }

    private void setUser(final EntityViewHolder h, int position,final UserDTO p) {
        h.txtTitle.setText(p.getFullName());
        h.txtDate.setVisibility(View.GONE);
        h.txtSubTitle.setText(p.getEmail());
        h.iconLocation.setImageDrawable(ContextCompat.getDrawable(ctx, android.R.drawable.ic_menu_send));
        h.iconVideo.setVisibility(View.GONE);
        h.iconCalendar.setVisibility(View.GONE);
        /*h.iconLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(h.iconLinks, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                    }
                });
            }
        });*/
        h.txtLinks.setVisibility(View.GONE);
        h.txtupdate.setVisibility(View.GONE);
        h.iconLink.setVisibility(View.GONE);
        h.iconRate.setVisibility(View.GONE);
        h.iconShare.setVisibility(View.GONE);
        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(h.iconDelete, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onDeleteUser(p);
                    }
                });
            }
        });

        h.txtNumBlack.setVisibility(View.GONE);
        h.txtNumBlack.setText(String.valueOf(position + 1));

        h.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(h.iconUpdate, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onUpdateUser(p);
                    }
                });
            }
        });
    }

    private void setDailyThought(final EntityViewHolder h, int position,final DailyThoughtDTO p) {
        h.txtTitle.setText(p.getTitle());
        if (p.getDateScheduled() != null) {
            h.txtDate.setText(sdf.format(new Date(p.getDateScheduled())));
            h.txtDate.setVisibility(View.VISIBLE);
        } else {
            h.txtDate.setVisibility(View.GONE);
        }
        h.txtSubTitle.setText(p.getSubtitle());

        if (p.getPhotos() != null) {
            h.txtPhotos.setText(String.valueOf(p.getPhotos().size()));

            DailyThoughtDTO dtd = p;//mList.get(position);
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
                        .into(h.imageView);
                //h.captiontxt.setText(vDTO.getCaption());


            }
        }
        if (p.getVideos() != null) {
            h.txtVideos.setText(String.valueOf(p.getVideos().size()));
        }
        if (p.getUrls() != null) {
            h.txtLinks.setText(String.valueOf(p.getUrls().size()));
        }
        if (p.getPodcasts() != null) {
            h.txtPodcasts.setText(String.valueOf(p.getPodcasts().size()));
        }
        h.txtNumBlack.setVisibility(View.VISIBLE);
        h.txtNumBlack.setText(String.valueOf(position + 1));
        h.iconLocation.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_schedule));
        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(h.iconDelete, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onDeleteDailyThought(p);
                    }
                });
            }
        });

        h.linksLayout.setVisibility(View.VISIBLE);
        h.photosLayout.setVisibility(View.VISIBLE);
        h.videosLayout.setVisibility(View.VISIBLE);
        h.micLayout.setVisibility(View.VISIBLE);

        h.calLayout.setVisibility(View.VISIBLE);

        h.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(h.iconUpdate, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onUpdateDailyThought(p);
                    }
                });
            }
        });
       /* if (p.getCalendarEvents() == null) {
            h.txtEvents.setVisibility(View.GONE);
        } else {
            h.txtEvents.setText(String.valueOf(p.getCalendarEvents().size()));
            h.txtEvents.setVisibility(View.VISIBLE);
        }*/
    }

    private void setNews(final EntityViewHolder h, int position,final NewsDTO p) {
        h.txtTitle.setText(p.getTitle());
        if (p.getDateScheduled() != null) {
            h.txtDate.setText(sdf.format(new Date(p.getDateScheduled())));
            h.txtDate.setVisibility(View.VISIBLE);
        } else {
            h.txtDate.setVisibility(View.GONE);
        }
        h.txtSubTitle.setText(p.getSubtitle());

        if (p.getPhotos() != null) {
            h.txtPhotos.setText(String.valueOf(p.getPhotos().size()));
            NewsDTO dtd = p;//mList.get(position);
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
                        .into(h.imageView);
                //h.captiontxt.setText(vDTO.getCaption());


            }
        }
        if (p.getVideos() != null) {
            h.txtVideos.setText(String.valueOf(p.getVideos().size()));
        }
        if (p.getUrls() != null) {
            h.txtLinks.setText(String.valueOf(p.getUrls().size()));
        }
        /*if (p.getPodcasts() != null) {
            h.txtPodcasts.setText(String.valueOf(p.getPodcasts().size()));
        }*/
        h.txtNumBlack.setVisibility(View.VISIBLE);
        h.txtNumBlack.setText(String.valueOf(position + 1));
        h.iconLocation.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_schedule));

        h.linksLayout.setVisibility(View.VISIBLE);
        h.photosLayout.setVisibility(View.VISIBLE);
        h.videosLayout.setVisibility(View.VISIBLE);
        h.micLayout.setVisibility(View.VISIBLE);

        h.calLayout.setVisibility(View.VISIBLE);

        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(h.iconDelete, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onDeleteNews(p);
                    }
                });
            }
        });
        h.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(h.iconUpdate, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onUpdateNews(p);
                    }
                });
            }
        });
       /* if (p.getCalendarEvents() == null) {
            h.txtEvents.setVisibility(View.GONE);
        } else {
            h.txtEvents.setText(String.valueOf(p.getCalendarEvents().size()));
            h.txtEvents.setVisibility(View.VISIBLE);
        }*/
    }

    private void setPodcast(final EntityViewHolder h, int position,final PodcastDTO p) {
        h.txtTitle.setText(p.getTitle());
        if (p.getDateScheduled() != null) {
            h.txtDate.setText(sdf.format(new Date(p.getDateScheduled())));
            h.txtDate.setVisibility(View.VISIBLE);
        } else {
            h.txtDate.setVisibility(View.GONE);
        }
        h.txtSubTitle.setText(p.getSubtitle());

        if (p.getPhotos() != null) {
            h.txtPhotos.setText(String.valueOf(p.getPhotos().size()));
        }
        if (p.getVideos() != null) {
            h.txtVideos.setText(String.valueOf(p.getVideos().size()));
        }
        if (p.getUrls() != null) {
            h.txtLinks.setText(String.valueOf(p.getUrls().size()));
        }
        h.txtPodcasts.setVisibility(View.GONE);

        h.txtNumBlack.setVisibility(View.VISIBLE);
        h.txtNumBlack.setText(String.valueOf(position + 1));
        h.iconLocation.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_schedule));

        h.linksLayout.setVisibility(View.VISIBLE);
        h.photosLayout.setVisibility(View.VISIBLE);
        h.videosLayout.setVisibility(View.VISIBLE);
        h.micLayout.setVisibility(View.VISIBLE);

        h.calLayout.setVisibility(View.VISIBLE);

        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(h.iconDelete, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onDeletePodcast(p);
                    }
                });
            }
        });
        /*if (p.getCalendarEvents() == null) {
            h.txtEvents.setVisibility(View.GONE);
        } else {
            h.txtEvents.setText(String.valueOf(p.getCalendarEvents().size()));
            h.txtEvents.setVisibility(View.VISIBLE);
        }*/
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private void setWeeklyMessage(final EntityViewHolder h, int position,final WeeklyMessageDTO p) {
        Log.d("BasicEntityAdapter", "setWeeklyMessage: ".concat(GSON.toJson(p)));
        h.txtTitle.setText(p.getTitle());
        if (p.getDateScheduled() != null) {
            h.txtDate.setText(sdf.format(new Date(p.getDateScheduled())));
            h.txtDate.setVisibility(View.VISIBLE);
        } else {
            h.txtDate.setVisibility(View.GONE);
        }
        h.txtSubTitle.setText(p.getSubtitle());

        if (p.getPhotos() != null) {
            h.txtPhotos.setText(String.valueOf(p.getPhotos().size()));
            WeeklyMessageDTO dtd = p;//mList.get(position);
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
                        .into(h.imageView);
                //h.captiontxt.setText(vDTO.getCaption());


            }
        }
        if (p.getVideos() != null) {
            h.txtVideos.setText(String.valueOf(p.getVideos().size()));
        }
        if (p.getUrls() != null) {
            h.txtLinks.setText(String.valueOf(p.getUrls().size()));
        }
        if (p.getPodcasts() != null) {
            h.txtPodcasts.setText(String.valueOf(p.getPodcasts().size()));
        }
        h.txtNumBlack.setVisibility(View.VISIBLE);
        h.txtNumBlack.setText(String.valueOf(position + 1));
        h.iconLocation.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_schedule));

        h.linksLayout.setVisibility(View.VISIBLE);
        h.photosLayout.setVisibility(View.VISIBLE);
        h.videosLayout.setVisibility(View.VISIBLE);
        h.micLayout.setVisibility(View.VISIBLE);
        h.calLayout.setVisibility(View.VISIBLE);

        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(h.iconDelete, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onDeleteWeeklyMessage(p);
                    }
                });
            }
        });

        h.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(h.iconUpdate, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onUpdateWeeklyMessage(p);
                    }
                });
            }
        });
        /*if (p.getCalendarEvents() == null) {
            h.txtEvents.setVisibility(View.GONE);
        } else {
            h.txtEvents.setText(String.valueOf(p.getCalendarEvents().size()));
            h.txtEvents.setVisibility(View.VISIBLE);
        }*/

    }

    private void setWeeklyMasterclass(final EntityViewHolder h, int position,final WeeklyMasterClassDTO p) {
        h.txtTitle.setText(p.getTitle());
        if (p.getDateScheduled() != null) {
            h.txtDate.setText(sdf.format(new Date(p.getDateScheduled())));
            h.txtDate.setVisibility(View.VISIBLE);
        } else {
            h.txtDate.setVisibility(View.GONE);
        }
        h.txtSubTitle.setText(p.getSubtitle());

        if (p.getPhotos() != null) {
            h.txtPhotos.setText(String.valueOf(p.getPhotos().size()));
            WeeklyMasterClassDTO dtd = p;//mList.get(position);
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
                        .into(h.imageView);
                //h.captiontxt.setText(vDTO.getCaption());


            }
        }
        if (p.getVideos() != null) {
            h.txtVideos.setText(String.valueOf(p.getVideos().size()));
        }
        if (p.getUrls() != null) {
            h.txtLinks.setText(String.valueOf(p.getUrls().size()));
        }
        if (p.getPodcasts() != null) {
            h.txtPodcasts.setText(String.valueOf(p.getPodcasts().size()));
        }
        h.txtNumBlack.setVisibility(View.VISIBLE);
        h.txtNumBlack.setText(String.valueOf(position + 1));
        h.iconLocation.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_schedule));

        h.linksLayout.setVisibility(View.VISIBLE);
        h.photosLayout.setVisibility(View.VISIBLE);
        h.videosLayout.setVisibility(View.VISIBLE);
        h.micLayout.setVisibility(View.VISIBLE);
        h.calLayout.setVisibility(View.VISIBLE);
        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(h.iconDelete, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onDeleteWeeklyMasterClass(p);
                    }
                });
            }
        });
        h.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(h.iconUpdate, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onUpdateWeeklyMasterClass(p);
                    }
                });
            }
        });
       /* if (p.getCalendarEvents() == null) {
            h.txtEvents.setVisibility(View.GONE);
        } else {
            h.txtEvents.setText(String.valueOf(p.getCalendarEvents().size()));
            h.txtEvents.setVisibility(View.VISIBLE);
        }*/

    }

    private void setEBook(final EntityViewHolder h, int position,final EBookDTO p) {
        h.txtTitle.setText(p.getTitle());
        h.txtDate.setVisibility(View.GONE);
        h.txtSubTitle.setText(p.getDescription());

        if (p.getPhotos() != null) {
            h.txtPhotos.setText(String.valueOf(p.getPhotos().size()));
        }
        if (p.getVideos() != null) {
            h.txtVideos.setText(String.valueOf(p.getVideos().size()));
        }
        if (p.getUrls() != null) {
            h.txtLinks.setText(String.valueOf(p.getUrls().size()));
        }
        if (p.getPodcasts() != null) {
            h.txtPodcasts.setText(String.valueOf(p.getPodcasts().size()));
        }
        h.txtNumBlack.setVisibility(View.VISIBLE);
        h.txtNumBlack.setText(String.valueOf(position + 1));
        h.iconLocation.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.ic_schedule));

        h.linksLayout.setVisibility(View.VISIBLE);
        h.photosLayout.setVisibility(View.VISIBLE);
        h.videosLayout.setVisibility(View.VISIBLE);
        h.micLayout.setVisibility(View.VISIBLE);
        h.calLayout.setVisibility(View.VISIBLE);

        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Util.flashOnce(h.iconDelete, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onDeleteEbook(p);
                    }
                });
            }
        });

        /*if (p.getCalendarEvents() == null) {
            h.txtEvents.setVisibility(View.GONE);
        } else {
            h.txtEvents.setText(String.valueOf(p.getCalendarEvents().size()));
            h.txtEvents.setVisibility(View.VISIBLE);
        }*/
    }

    private void animateIn(View view, final BaseDTO entity) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mListener.onEntityClicked(entity);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        an.start();
    }

    private void animateOut(final View view, final BaseDTO entity) {
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.5f);
        an.setDuration(200);

        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
                //mListener.onEntityClicked(entity);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        an.start();

    }

    @Override
    public int getItemCount() {
        return entities == null ? 0 : entities.size();
    }

    public class EntityViewHolder extends RecyclerView.ViewHolder {
        protected TextView /*txtTitle,*/ txtSubTitle, txtDate,
                txtNumBlack, txtNumBlue, txtNumGrey, txtNumGreen, txtNumRed,
                txtPhotos, txtVideos, txtEvents, txtLinks, txtPodcasts, txtupdate;

        protected ImageView iconCalendar, iconDelete,iconUpdate,iconShare,iconRate, iconLink,
                iconPhoto, iconVideo, iconLocation, iconMicrophone, imageView;

        protected View bottomLayout, frameLayout, linksLayout, iconLayout,
                photosLayout, videosLayout, micLayout, calLayout;
        //protected TextView txtTitle;
        protected TextViewExpandableAnimation txtTitle;

        protected RelativeLayout titleLayout;


        public EntityViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ImageView);
            txtTitle = (TextViewExpandableAnimation/*TextView*/) itemView.findViewById(R.id.txtTitle);
            //txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);

            txtNumBlack = (TextView) itemView.findViewById(R.id.txtNumberBlack);
            txtNumBlue = (TextView) itemView.findViewById(R.id.txtNumberBlue);
            txtNumGreen = (TextView) itemView.findViewById(R.id.txtNumberGreen);
            txtNumGrey = (TextView) itemView.findViewById(R.id.txtNumberGrey);
            txtNumRed = (TextView) itemView.findViewById(R.id.txtNumberRed);

            txtPhotos = (TextView) itemView.findViewById(R.id.txtCamera);
            txtVideos = (TextView) itemView.findViewById(R.id.txtVideo);
            txtPodcasts = (TextView) itemView.findViewById(R.id.txtMicrophone);
           // txtEvents = (TextView) itemView.findViewById(R.id.txtEvents);
            txtLinks = (TextView) itemView.findViewById(R.id.txtLinks);
            txtupdate = (TextView) itemView.findViewById(R.id.txtupdate);

            iconCalendar = (ImageView) itemView.findViewById(R.id.iconCalendar);
            iconMicrophone = (ImageView) itemView.findViewById(R.id.iconMicrophone);
            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);
            iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);
            iconPhoto = (ImageView) itemView.findViewById(R.id.iconCamera);
            iconVideo = (ImageView) itemView.findViewById(R.id.iconVideo);
            iconLocation = (ImageView) itemView.findViewById(R.id.iconLocation);
            iconLink = (ImageView) itemView.findViewById(R.id.iconLink);

            //iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);
            iconShare = (ImageView) itemView.findViewById(R.id.iconShare);
            iconShare.setVisibility(View.GONE);
            iconRate = (ImageView) itemView.findViewById(R.id.ratingBar);
            iconRate.setVisibility(View.GONE);

            frameLayout = itemView.findViewById(R.id.frame);
            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            titleLayout = (RelativeLayout) itemView.findViewById(R.id.titleLayout);
            linksLayout = itemView.findViewById(R.id.linksLayout);
            photosLayout = itemView.findViewById(R.id.photosLayout);
            videosLayout = itemView.findViewById(R.id.videosLayout);
            micLayout = itemView.findViewById(R.id.micLayout);
            calLayout = itemView.findViewById(R.id.t0);

            iconLayout = itemView.findViewById(R.id.iconLayout);

            linksLayout.setVisibility(View.VISIBLE);
            photosLayout.setVisibility(View.GONE);
            videosLayout.setVisibility(View.GONE);
            micLayout.setVisibility(View.GONE);
            calLayout.setVisibility(View.GONE);

            txtNumRed.setVisibility(View.GONE);
            txtNumGrey.setVisibility(View.GONE);
            txtNumGreen.setVisibility(View.GONE);
            txtNumBlue.setVisibility(View.GONE);
            txtNumBlack.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }

    }


    private void hide(View itemView) {

    }

    static final String LOG = BasicEntityAdapter.class.getSimpleName();
}
