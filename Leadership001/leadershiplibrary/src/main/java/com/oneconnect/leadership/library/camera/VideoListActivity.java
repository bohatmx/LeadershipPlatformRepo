package com.oneconnect.leadership.library.camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.audio.PodcastListActivity;
import com.oneconnect.leadership.library.ebook.EbookListActivity;
import com.oneconnect.leadership.library.ebook.EbookSelectionActivity;
import com.oneconnect.leadership.library.links.LinksActivity;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.VideoCache;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.util.ArrayList;
import java.util.List;


public class VideoListActivity extends AppCompatActivity implements SubscriberContract.View, CacheContract.View{

    RecyclerView recyclerView;
    private ImageView image1, image2;
    private int type;
    private DailyThoughtDTO dailyThought;
    private PodcastDTO podcast;
    private EBookDTO eBook;
    private UrlDTO url;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    Toolbar toolbar;
    Context ctx;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Video Attachment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = getApplicationContext();

        presenter = new SubscriberPresenter(this);
        cachePresenter = new CachePresenter(this, ctx);

        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
                getSupportActionBar().setSubtitle(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");
                getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
                getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
                break;
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
                getSupportActionBar().setSubtitle(eBook.getStorageName());
                break;
           /* case ResponseBag.VIDEOS:
                video = (VideoDTO) getIntent().getSerializableExtra("video");
                getSupportActionBar().setSubtitle(video.getStorageName());
                break; */
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
                getSupportActionBar().setSubtitle(podcast.getStorageName());
                break;
            case ResponseBag.URLS:
                url = (UrlDTO) getIntent().getSerializableExtra("url");
                getSupportActionBar().setSubtitle(url.getTitle());
                break;
        }
        if (getIntent().getSerializableExtra("eBook") != null) {
            type = ResponseBag.EBOOKS;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
            getSupportActionBar().setSubtitle(eBook.getStorageName());
        }
        if (getIntent().getSerializableExtra("poedcast") != null) {
            type = ResponseBag.PODCASTS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
            getSupportActionBar().setSubtitle(podcast.getStorageName());
        }
        if (getIntent().getSerializableExtra("url") != null) {
            type = ResponseBag.URLS;
            url = (UrlDTO) getIntent().getSerializableExtra("url");
            getSupportActionBar().setSubtitle(url.getTitle());
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        image1 = (ImageView) findViewById(R.id.image1);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(image2, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(VideoListActivity.this, VideoSelectionActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        image2 = (ImageView) findViewById(R.id.image2);
        image2.setVisibility(View.GONE);

        getCachedVideos();
        getVideos();
    }

    static final String LOG = VideoListActivity.class.getSimpleName();

    private void getCachedVideos() {
        VideoCache.getVideos(ctx, new VideoCache.ReadListener() {
            @Override
            public void onDataRead(List<VideoDTO> videos) {
                Log.d(LOG, "onDataRead: Videos: " + videos);
            }

            @Override
            public void onError(String message) {
                getCachedVideos();
            }
        });
    }

    private void getVideos() {
        Log.d(LOG, "************** getVideos: " );
       /* if (SharedPrefUtil.getUser(ctx).getCompanyID() != null) {
        */    presenter.getAllVideos();
       /* } else {
            Log.d(LOG, "user is null");
        }*/

    }


    @Override
    public void onDataCached() {

    }

    @Override
    public void onCacheCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onCacheDailyThoughts(List<DailyThoughtDTO> list) {

    }

    @Override
    public void onCacheEbooks(List<EBookDTO> list) {

    }

    @Override
    public void onCacheNews(List<NewsDTO> list) {

    }

    @Override
    public void onCachePodcasts(List<PodcastDTO> list) {

    }

    @Override
    public void onCacheVideos(List<VideoDTO> list) {

    }

    @Override
    public void onCachePrices(List<PriceDTO> list) {

    }

    @Override
    public void onCacheSubscriptions(List<SubscriptionDTO> list) {

    }

    @Override
    public void onCacheUsers(List<UserDTO> list) {

    }

    @Override
    public void onCacheWeeklyMasterclasses(List<WeeklyMasterClassDTO> list) {

    }

    @Override
    public void onCacheWeeklyMessages(List<WeeklyMessageDTO> list) {

    }

    @Override
    public void onCachePhotos(List<PhotoDTO> list) {

    }

    @Override
    public void onCacheCalendarEvents(List<CalendarEventDTO> list) {

    }

    @Override
    public void onEntityAdded(String key) {

    }

    @Override
    public void onEntityUpdated() {

    }

    @Override
    public void onUserCreated(UserDTO user) {

    }

    @Override
    public void onCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onCompanies(List<CompanyDTO> list) {

    }

    @Override
    public void onDailyThoughts(List<DailyThoughtDTO> list) {

    }

    @Override
    public void onAllCompanyDailyThoughts(List<DailyThoughtDTO> list) {

    }

    @Override
    public void onAllDailyThoughts(List<DailyThoughtDTO> list) {

    }

    @Override
    public void onAllNewsArticle(List<NewsDTO> list) {

    }

    private List<VideoDTO> videos = new ArrayList<>();
    AdminVideoAdapter adapter;

    @Override
    public void onAllVideos(List<VideoDTO> list) {
        Log.i(LOG, "onAllVideos: " + list.size());
        this.videos = list;
        adapter = new AdminVideoAdapter(list, ctx, new AdminVideoAdapter.AdminVideoAdapterListener() {
            @Override
            public void onAttachPhoto(BaseDTO base) {
                startPhotoGallerySelection(base);
            }

            @Override
            public void onPoadcastRequired(BaseDTO base) {
                startPodcastSelection(base);
            }

            @Override
            public void onUrlRequired(BaseDTO base) {
                startLinksActivity(base);
            }

            @Override
            public void onEbookRequired(BaseDTO base) {
                startEbookSelection(base);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void startPhotoGallerySelection(BaseDTO base){
        Intent intent = new Intent(this, PhotoSelectionActivity.class);
        type = ResponseBag.VIDEOS;
        video = (VideoDTO) base;
        intent.putExtra("video", video);
        startActivity(intent);
    }

    VideoDTO video;

    private void startPodcastSelection(BaseDTO base){
        Intent intent = new Intent(VideoListActivity.this, PodcastSelectionActivity.class);
        type = ResponseBag.VIDEOS;
        video = (VideoDTO) base;
        intent.putExtra("video", video);
        startActivity(intent);
      //  startActivityForResult(intent, type);
    }

    private void startLinksActivity(BaseDTO base){
        Intent m = new Intent(VideoListActivity.this, LinksActivity.class);
        type = ResponseBag.VIDEOS;
        video = (VideoDTO) base;
        m.putExtra("video", video);
        startActivity(m);
    }

    private void startEbookSelection(BaseDTO base){
        Intent intent = new Intent(VideoListActivity.this, EbookSelectionActivity.class);
        type = ResponseBag.VIDEOS;
        video = (VideoDTO) base;
        intent.putExtra("video", video);
        startActivity(intent);
      //  startActivityForResult(intent, type);
    }

    @Override
    public void onAllEBooks(List<EBookDTO> list) {

    }

    @Override
    public void onAllPhotos(List<PhotoDTO> list) {

    }

    @Override
    public void onAllWeeklyMessages(List<WeeklyMessageDTO> list) {

    }

    @Override
    public void onAllPodcasts(List<PodcastDTO> list) {

    }

    @Override
    public void onAllCalendarEvents(List<CalendarEventDTO> list) {

    }

    @Override
    public void onEbooks(List<EBookDTO> list) {

    }

    @Override
    public void onPayments(List<PaymentDTO> list) {

    }

    @Override
    public void onPodcasts(List<PodcastDTO> list) {

    }

    @Override
    public void onPhotos(List<PhotoDTO> list) {

    }

    @Override
    public void onPrices(List<PriceDTO> list) {

    }

    @Override
    public void onUsers(List<UserDTO> list) {

    }

    @Override
    public void onNews(List<NewsDTO> list) {

    }

    @Override
    public void onSubscriptions(List<SubscriptionDTO> list) {

    }

    @Override
    public void onVideos(List<VideoDTO> list) {
        Log.i(LOG, "onVideos: " + list.size());
        this.videos = list;
        adapter = new AdminVideoAdapter(list, ctx, new AdminVideoAdapter.AdminVideoAdapterListener() {
            @Override
            public void onAttachPhoto(BaseDTO base) {
                startPhotoGallerySelection(base);
            }

            @Override
            public void onPoadcastRequired(BaseDTO base) {
                startPodcastSelection(base);
            }

            @Override
            public void onUrlRequired(BaseDTO base) {
                startLinksActivity(base);
            }

            @Override
            public void onEbookRequired(BaseDTO base) {
                startEbookSelection(base);
            }
        });

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onWeeklyMasterclasses(List<WeeklyMasterClassDTO> list) {

    }

    @Override
    public void onWeeklyMessages(List<WeeklyMessageDTO> list) {

    }

    @Override
    public void onDevices(List<DeviceDTO> companyID) {

    }

    @Override
    public void onError(String message) {

    }
}
