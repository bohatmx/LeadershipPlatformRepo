package com.oneconnect.leadership.library.activities;

/**
 * Created by Kurisani on 2017/07/12.
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.RatingReviewAdapter;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.RatingCache;
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
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.ratings.RatingContract;
import com.oneconnect.leadership.library.ratings.RatingPresenter;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.util.Date;
import java.util.List;

public class RatingActivity extends AppCompatActivity implements RatingContract.View, SubscriberContract.View , CacheContract.View{

    private RatingBar ratingBar;
    private TextView txtRatingValue, lblRateMe;
    private Button btnSubmit;
    protected EditText ratingCom;
    private RatingPresenter ratingPresenter;
    private CachePresenter cachePresenter;
    private Context ctx;
    private Snackbar snackbar;
    private Toolbar toolbar;
    int type;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private NewsDTO news;
    private EBookDTO eBook;
    private PodcastDTO podcast;
    private RatingDTO rating;
    private VideoDTO video;
    private UrlDTO url;
    private RecyclerView recyclerView;
    private RatingReviewAdapter ratingReviewAdapter;
    private SubscriberPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        ctx = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rating Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new SubscriberPresenter(this);
        cachePresenter = new CachePresenter(this, ctx);
        ratingPresenter = new RatingPresenter(this);

        //cachePresenter = new CachePresenter(this, ctx);
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
            case ResponseBag.VIDEOS:
                video = (VideoDTO) getIntent().getSerializableExtra("video");
                getSupportActionBar().setSubtitle(video.getStorageName());
                break;
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
                getSupportActionBar().setSubtitle(podcast.getStorageName());
                break;
            case ResponseBag.URLS:
                url = (UrlDTO) getIntent().getSerializableExtra("url");
                getSupportActionBar().setSubtitle(url.getTitle());
                break;
        }
        if(getIntent().getSerializableExtra("dailyThought") != null){
             type = ResponseBag.DAILY_THOUGHTS;
            dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
            getSupportActionBar().setSubtitle(dailyThought.getTitle());
        }
        if(getIntent().getSerializableExtra("weeklyMasterClass") != null){
            type = ResponseBag.WEEKLY_MASTERCLASS;
            weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");
            getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
        }
        if(getIntent().getSerializableExtra("weeklyMessage") != null){
            type = ResponseBag.WEEKLY_MESSAGE;
            weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
            getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
        }
        if(getIntent().getSerializableExtra("newsArticle") != null){
            type = ResponseBag.NEWS;
            news = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
        }
        if(getIntent().getSerializableExtra("eBook") != null){
            type = ResponseBag.EBOOKS;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
            getSupportActionBar().setSubtitle(eBook.getStorageName());
        }
        if(getIntent().getSerializableExtra("podcast") != null){
            type = ResponseBag.PODCASTS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
            getSupportActionBar().setSubtitle(podcast.getStorageName());
        }

      //  addListenerOnRatingBar();
      //  addListenerOnButton();

        setup();
        getCachedRatings();
        getRatings();

    }
    private ListAPI listAPI;

    private void getCachedRatings() {
        RatingCache.getRatings(ctx, new RatingCache.ReadListener() {

            @Override
            public void onDataRead(List<RatingDTO> rating) {
                Log.d(LOG, "onDataRead: rating: " + rating);
            }

            @Override
            public void onError(String message) {
                getCachedRatings();
            }
        });
    }

    private void getRatings() {
        Log.d(LOG, "************** getRatings: " );
        presenter.getDailyThoughtsRating(dailyThought.getDailyThoughtID());
        //presenter.getAllRatings();
        /*if (SharedPrefUtil.getUser(ctx).getCompanyID() != null) {
            presenter.getAllRatings();
        } else {
            Log.d(LOG, "user is null");
        }*/

    }

    private void setup() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
        txtRatingValue.setVisibility(View.GONE);
        ratingCom = (EditText) findViewById(R.id.ratingCom);
        recyclerView = (RecyclerView) findViewById(R.id.review) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });

        lblRateMe = (TextView) findViewById(R.id.lblRateMe);
        lblRateMe.setVisibility(View.GONE);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSubmit, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        uploadRating();
                    }
                });

               /* Toast.makeText(RatingActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show(); */

            }

        });
    }
    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
        ratingCom = (EditText) findViewById(R.id.ratingCom);
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
      /*  ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                //  txtRatingValue.setText(String.valueOf(rating));

            }
        }); */
    }

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(ratingBar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void uploadRating(){
        Log.d(LOG, "****uploadingRating****");
       // showSnackbar("Uploading Rating ...", "OK", Constants.CYAN);
        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());

        RatingDTO rate = new RatingDTO();
//        rate.setUserID(u.getUserID());
 //       rate.setUserName(u.getFirstName());

        float ratingValue = ratingBar.getRating();

        rate.setRating((int)Math.round(ratingValue)/*getNumStars()*/);
        if(ratingCom.getText().toString() != null){
            rate.setComment(ratingCom.getText().toString());
        }
        rate.setDate(new Date().getTime());
        switch (type){
            case ResponseBag.DAILY_THOUGHTS:
                rate.setDailyThoughtID(dailyThought.getDailyThoughtID());
                break;

            case ResponseBag.WEEKLY_MASTERCLASS:
                rate.setWeeklyMasterclassID(weeklyMasterClass.getWeeklyMasterClassID());
                break;

            case ResponseBag.WEEKLY_MESSAGE:
                rate.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                break;

            case ResponseBag.EBOOKS:
                rate.seteBookID(eBook.geteBookID());
                break;

            /*case ResponseBag.PODCASTS:
                rate.setPodcastID(podcast.getPodcastID());
                break;*/
        }

        ratingPresenter.uploadRating(rate);
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
       // ratingCom = (EditText) findViewById(R.id.ratingCom);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadRating();
               /* Toast.makeText(RatingActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();*/

            }

        });

    }

    @Override
    public void onRatingUploaded(String key) {
        showSnackbar("rating successfully added ...", "OK", Constants.CYAN);
        ratingCom.setText(" ");
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
    public void onAllRatings(List<RatingDTO> list) {
        Log.i(LOG,"onAllRatings: " + list.size());
        ratingReviewAdapter = new RatingReviewAdapter(ctx, list, new RatingReviewAdapter.RatingAdapterlistener() {
            @Override
            public void addListenerOnButton() {

            }

            @Override
            public void addListenerOnRatingBar() {

            }
        });
        recyclerView.setAdapter(ratingReviewAdapter);
    }

    @Override
    public void onDailyThoughtRatings(List<RatingDTO> list) {
        Log.i(LOG,"onDailyThoughtRatings: " + list.size());
        ratingReviewAdapter = new RatingReviewAdapter(ctx, list, new RatingReviewAdapter.RatingAdapterlistener() {
            @Override
            public void addListenerOnButton() {

            }

            @Override
            public void addListenerOnRatingBar() {

            }
        });
        recyclerView.setAdapter(ratingReviewAdapter);
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
    public void onAllSubscriptions(List<SubscriptionDTO> list) {

    }

    @Override
    public void onAllNewsArticle(List<NewsDTO> list) {

    }

    @Override
    public void onAllCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onAllVideos(List<VideoDTO> list) {

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
    public void onError(String message) {
        Log.e(LOG, "onError: " + message);
    }

    static final String LOG = RatingActivity.class.getSimpleName();
}