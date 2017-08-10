package com.oneconnect.leadership.subscriber;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.activities.DailyThoughtActivity;
import com.oneconnect.leadership.library.activities.MasterActivity;
import com.oneconnect.leadership.library.activities.PhotoActivity;
import com.oneconnect.leadership.library.activities.PodcastActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.activities.VideoActivity;
import com.oneconnect.leadership.library.activities.eBookActivity;
import com.oneconnect.leadership.library.adapters.DailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.NewsArticleAdapter;
import com.oneconnect.leadership.library.adapters.WeeklyMessageAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.FCMData;
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
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.CalendarEventListFragment;
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.EBookListFragment;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.lists.MasterListFragment;
import com.oneconnect.leadership.library.lists.MediaListActivity;
import com.oneconnect.leadership.library.lists.NewsListFragment;
import com.oneconnect.leadership.library.lists.PageFragment;
import com.oneconnect.leadership.library.lists.PhotoListFragment;
import com.oneconnect.leadership.library.lists.PodcastListFragment;
import com.oneconnect.leadership.library.lists.VideoListFragment;
import com.oneconnect.leadership.library.lists.WeeklyMessageListFragment;
import com.oneconnect.leadership.library.login.BaseLoginActivity;
import com.oneconnect.leadership.library.util.DepthPageTransformer;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.subscriber.services.SubscriberMessagingService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.RED;


public class SubscriberMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SubscriberContract.View, CacheContract.View, BasicEntityAdapter.EntityListener,
        MasterListFragment.WeeklyMasterClassListener, WeeklyMessageListFragment.WeeklyMessageListener,
        CalendarEventListFragment.CalendarEventListener,
        PodcastListFragment.PodcastListener, VideoListFragment.VideoListener,
        PhotoListFragment.PhotoListener, EBookListFragment.EBookListener, DailyThoughtListFragment.DailyThoughtListener, NewsListFragment.NewsArticleListener{


    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private PodcastDTO podcast;
    private EntityListFragment entityListFragment;
    private EBookListFragment eBookListFragment;
    private PodcastListFragment podcastListFragment;
    private VideoListFragment videoListFragment;

    private Snackbar snackbar;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    DailyThoughtListFragment dailyThoughtListFragment;

    private Context ctx;
    private String page;
    private ViewPager mPager;
    private CachePresenter cachePresenter;
    private SubscriberPresenter presenter;
    private UserDTO user;
    TextView usernametxt;
    PagerSlidingTabStrip strip;

    //Bottom Navigation
    private TextView textFavorites;
    private TextView textSchedules;
    private TextView textMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        setContentView(R.layout.activity_subscriber_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.leadership_logo);
        ctx = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        page = getIntent().getStringExtra("page");

        usernametxt = (TextView) findViewById(R.id.usernametxt);

      //  user = SharedPrefUtil.getUser(ctx);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        //PagerTitleStrip strip = (PagerTitleStrip) mPager.findViewById(com.oneconnect.leadership.library.R.id.pager_title_strip);
        strip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        strip.setVisibility(View.VISIBLE);
        strip.setTextColor(GRAY);
         setup();

        presenter = new SubscriberPresenter(this);
        cachePresenter = new CachePresenter(this, ctx);
        user = SharedPrefUtil.getUser(this);
        type = SharedPrefUtil.getFragmentType(this);
        if (type == 0) {
            cachePresenter.getCacheUsers();
        } else {
            switch (type) {
                case ResponseBag.CATEGORIES:
                    cachePresenter.getCacheCategories();
                    break;
                case ResponseBag.DAILY_THOUGHTS:
                    cachePresenter.getCacheDailyThoughts();
                    break;
                case ResponseBag.EBOOKS:
                    cachePresenter.getCacheEbooks();
                    break;
                case ResponseBag.PODCASTS:
                    cachePresenter.getCachePodcasts();
                    break;
                case ResponseBag.NEWS:
                    cachePresenter.getCacheNews();
                    break;
                case ResponseBag.PRICES:
                    cachePresenter.getCachePrices();
                    break;
                case ResponseBag.SUBSCRIPTIONS:
                    cachePresenter.getCacheSubscriptions();
                    break;
                case ResponseBag.USERS:
                    cachePresenter.getCacheUsers();
                    break;
                case ResponseBag.WEEKLY_MASTERCLASS:
                    cachePresenter.getCacheWeeklyMasterclasses();
                    break;
                case ResponseBag.WEEKLY_MESSAGE:
                    cachePresenter.getCacheWeeklyMessages();
                    break;
                case ResponseBag.PHOTOS:
                    break;
                case ResponseBag.VIDEOS:
                    break;
                case ResponseBag.PAYMENTS:
                    break;
            }

            IntentFilter filter = new IntentFilter(SubscriberMessagingService.BROADCAST_MESSAGE_RECEIVED);
            LocalBroadcastManager.getInstance(this).registerReceiver(new MessageReceiver(), filter);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        if (user != null) {
            Log.i(LOG, "user: " + user.getFullName());
            if (usernametxt != null)
                usernametxt.setText(user.getFirstName() + " " + user.getLastName());
        } else if(FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
            usernametxt.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
        else {
            usernametxt.setVisibility(View.GONE);
        }



        /*if (SharedPrefUtil.getUser(ctx).getEmail() != null) {
            usernametxt.setText(SharedPrefUtil.getUser(ctx).getEmail());
        }*/
    }

    static List<PageFragment> pageFragmentList;
    ResponseBag bag;
    int currentIndex;
    NavigationView navigationView;

    private void setup() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpViewPager();
        }

    private int type;

    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment");
        SharedPrefUtil.saveFragmentType(bag.getType(), this);
        type = bag.getType();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        entityListFragment = EntityListFragment.newInstance(bag);
        //entityListFragment.setListener(this);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }

    private PagerAdapter adapter;
    MasterListFragment masterListFragment;
    WeeklyMessageListFragment weeklyMessageListFragment;
    PhotoListFragment photoListFragment;
    CalendarEventListFragment calendarEventListFragment;
    NewsListFragment newsListFragment;


    private void setUpViewPager() {
        setMenuDestination();

        pageFragmentList = new ArrayList<>();

        newsListFragment = NewsListFragment.newInstance();
        dailyThoughtListFragment = DailyThoughtListFragment.newInstance();
        masterListFragment = MasterListFragment.newInstance();
        weeklyMessageListFragment = WeeklyMessageListFragment.newInstance();
        calendarEventListFragment = CalendarEventListFragment.newInstance();
        //photoListFragment = PhotoListFragment.newInstance(new HashMap<String, PhotoDTO>());
        podcastListFragment = PodcastListFragment.newInstance(new HashMap<String, PodcastDTO>());
        //videoListFragment = VideoListFragment.newInstance(new HashMap<String, VideoDTO>());
        eBookListFragment = EBookListFragment.newInstance(new HashMap<String, EBookDTO>());

        newsListFragment.setPageTitle(ctx.getString(R.string.news_article));
        dailyThoughtListFragment.setPageTitle(ctx.getString(R.string.daily_thought));
        masterListFragment.setPageTitle(ctx.getString(R.string.weeky_master_class));
        weeklyMessageListFragment.setPageTitle(ctx.getString(R.string.weekly_message));
        calendarEventListFragment.setPageTitle(ctx.getString(R.string.calendar_events));
        //photoListFragment.setPageTitle(ctx.getString(R.string.photo));
        podcastListFragment.setPageTitle(ctx.getString(R.string.podcast));
        //videoListFragment.setPageTitle(ctx.getString(R.string.video));
        eBookListFragment.setPageTitle(ctx.getString(R.string.ebooks));


        pageFragmentList.add(newsListFragment);
        pageFragmentList.add(dailyThoughtListFragment);
        pageFragmentList.add(masterListFragment);
        pageFragmentList.add(weeklyMessageListFragment);
        pageFragmentList.add(calendarEventListFragment);
        //pageFragmentList.add(photoListFragment);
        pageFragmentList.add(podcastListFragment);
       // pageFragmentList.add(videoListFragment);
        pageFragmentList.add(eBookListFragment);


        try {
            adapter = new PagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(adapter);
            strip.setViewPager(mPager);
            mPager.setPageTransformer(true, new DepthPageTransformer());
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentIndex = position;
                    PageFragment pf = pageFragmentList.get(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mPager.setCurrentItem(currentIndex);
        } catch (Exception e) {
            Log.e(LOG, "PagerAdapter failed", e);
            if (page != null) {

                if (page.equalsIgnoreCase("News Article")) {
                    mPager.setCurrentItem(0);
                }
                if (page.equalsIgnoreCase("Daily Thoughts")) {
                    mPager.setCurrentItem(1);
                }
                if (page.equalsIgnoreCase("Weekly Masterclass")) {
                    mPager.setCurrentItem(2);
                }
                if (page.equalsIgnoreCase("Weekly Message")) {
                    mPager.setCurrentItem(3);
                }
                if (page.equalsIgnoreCase("Calendar Event")) {
                    mPager.setCurrentItem(4);
                }
               /* if (page.equalsIgnoreCase("Photo")) {
                    mPager.setCurrentItem(5);
                }*/
                if (page.equalsIgnoreCase("Podcasts")) {
                    mPager.setCurrentItem(5);
                }
               /* if (page.equalsIgnoreCase("Video")) {
                    mPager.setCurrentItem(7);
                }*/
                if (page.equalsIgnoreCase("eBooks")) {
                    mPager.setCurrentItem(6);
                }

            }
        }

    }

    private void setMenuDestination() {

        Menu menu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawers();
                if (item.getItemId() == R.id.nav_news_article) {
                    mPager.setCurrentItem(0, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_daily_thought) {
                    mPager.setCurrentItem(1, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_master) {
                    mPager.setCurrentItem(2, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_weekly) {
                    mPager.setCurrentItem(3, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_calender_events) {
                    mPager.setCurrentItem(4, true);
                    return true;
                }
               /* if (item.getItemId() == R.id.nav_photo) {
                    mPager.setCurrentItem(5, true);
                    return true;
                }*/
                if (item.getItemId() == R.id.nav_podcast) {
                    mPager.setCurrentItem(5, true);
                    return true;
                }
                /*if (item.getItemId() == R.id.nav_video) {
                    mPager.setCurrentItem(7, true);
                    return true;
                }*/
                if (item.getItemId() == R.id.nav_eBooks) {
                    mPager.setCurrentItem(6, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_share) {
                   onInviteClicked();
                    /*Intent intent = new AppInviteInvitation.IntentBuilder(getString(com.oneconnect.leadership.library.R.string.invitation_title))
                            .setMessage(getString(R.string.invitation_message))
                            .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                            .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                            .setCallToActionText(getString(R.string.invitation_cta))
                            .build();
                    //startActivity(intent);
                    startActivityForResult(intent, REQUEST_INVITE);*/
                    return true;
                }

                if (item.getItemId() == R.id.nav_sign_out) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(SubscriberMainActivity.this, SubscriberSignInActivityBase.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(LOG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
                Log.d(LOG, "failed to send invite");
            }
        }
    }

    private void onInviteClicked() {
        Log.d(LOG, "****onInviteClicked****");
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
       // startActivity(intent);
        startActivityForResult(intent, REQUEST_INVITE);
    }

    private static final int REQUEST_INVITE = 321;
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

    }

    @Override
    public void onCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onCompanies(List<CompanyDTO> list) {

    }

    @Override
    public void onDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "onDailyThoughts: " + list.size());
        bag = new ResponseBag();
        bag.setDailyThoughts(list);
        Collections.sort(bag.getDailyThoughts());
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
        cachePresenter.cacheDailyThoughts(list);
    }

    @Override
    public void onAllCompanyDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "onAllCompanyDailyThoughts: " + list.size());
        bag = new ResponseBag();
        bag.setDailyThoughts(list);
        Collections.sort(bag.getDailyThoughts());
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
        cachePresenter.cacheDailyThoughts(list);
    }

    @Override
    public void onAllDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "onAllDailyThoughts: " + list.size());
        bag = new ResponseBag();
        bag.setDailyThoughts(list);
        Collections.sort(bag.getDailyThoughts());
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
        cachePresenter.cacheDailyThoughts(list);
        presenter.getAllDailyThoughts();
    }

    @Override
    public void onAllNewsArticle(List<NewsDTO> list) {

    }

    @Override
    public void onAllVideos(List<VideoDTO> list) {
        Log.i(TAG, "onAllVideos: " + list.size());
        bag = new ResponseBag();
        bag.setVideos(list);
        Collections.sort(bag.getVideos());
        bag.setType(ResponseBag.VIDEOS);
        setFragment();
        cachePresenter.cacheVideos(list);
        presenter.getAllVideos();

    }

    @Override
    public void onAllEBooks(List<EBookDTO> list) {
        Log.i(TAG, "onAllEBooks: " + list.size());
        bag = new ResponseBag();
        bag.seteBooks(list);
        Collections.sort(bag.geteBooks());
        bag.setType(ResponseBag.EBOOKS);
        setFragment();
        cachePresenter.cacheEbooks(list);
        presenter.getAllEBooks();
    }

    @Override
    public void onAllPhotos(List<PhotoDTO> list) {
        Log.i(TAG, "onAllPhotos: " + list.size());
        bag = new ResponseBag();
        bag.setPhotos(list);
        Collections.sort(bag.getPhotos());
        bag.setType(ResponseBag.PHOTOS);
        setFragment();
        cachePresenter.cachePhotos(list);
        presenter.getAllPhotos();
    }

    @Override
    public void onAllWeeklyMessages(List<WeeklyMessageDTO> list) {
        Log.i(TAG, "onAllWeeklyMessages: " + list.size());
        bag = new ResponseBag();
        bag.setWeeklyMessages(list);
        Collections.sort(bag.getWeeklyMessages());
        bag.setType(ResponseBag.WEEKLY_MESSAGE);
        setFragment();
        cachePresenter.cacheWeeklyMessages(list);
        presenter.getAllWeeklyMessages();
    }

    @Override
    public void onAllPodcasts(List<PodcastDTO> list) {
        Log.i(TAG, "onALlPodcasts: " + list.size());
        bag = new ResponseBag();
        bag.setPodcasts(list);
        Collections.sort(bag.getPodcasts());
        bag.setType(ResponseBag.PODCASTS);
        setFragment();
        cachePresenter.cachePodcasts(list);
        presenter.getAllPodcasts();
    }

    @Override
    public void onAllCalendarEvents(List<CalendarEventDTO> list) {
        Log.i(TAG, "onAllCalendarEvents: " + list.size());
        bag = new ResponseBag();
        bag.setCalendarEvents(list);
        Collections.sort(bag.getCalendarEvents());
        bag.setType(ResponseBag.CALENDAR_EVENTS);
        setFragment();
        cachePresenter.cacheCalendarEvents(list);
        presenter.getAllCalendarEvents();
    }

    @Override
    public void onEbooks(List<EBookDTO> list) {
        Log.i(TAG, "onEbooks " + list.size());
        bag = new ResponseBag();
        bag.seteBooks(list);
        bag.setType(ResponseBag.EBOOKS);
        setFragment();
        cachePresenter.cacheEbooks(list);
        presenter.getAllEBooks();
    }

    @Override
    public void onPayments(List<PaymentDTO> list) {

    }

    @Override
    public void onPodcasts(List<PodcastDTO> list) {
        Log.i(TAG, "onPodcasts " + list.size());
        bag = new ResponseBag();
        bag.setPodcasts(list);
        bag.setType(ResponseBag.PODCASTS);
        setFragment();
        cachePresenter.cachePodcasts(list);
        presenter.getAllPodcasts();
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
        Log.i(TAG, "onVideos " + list.size());
        bag = new ResponseBag();
        bag.setVideos(list);
        bag.setType(ResponseBag.VIDEOS);
        setFragment();
        cachePresenter.cacheVideos(list);
    }

    @Override
    public void onWeeklyMasterclasses(List<WeeklyMasterClassDTO> list) {
        Log.i(TAG, "onWeeklyMasterclasses: " + list.size());
        bag = new ResponseBag();
        bag.setWeeklyMasterClasses(list);
        Collections.sort(bag.getWeeklyMasterClasses());
        bag.setType(ResponseBag.WEEKLY_MASTERCLASS);
        setFragment();
        cachePresenter.cacheWeeklyMasterclasses(list);
        presenter.getAllWeeklyMasterClasses();
    }

    @Override
    public void onWeeklyMessages(List<WeeklyMessageDTO> list) {
        Log.i(TAG, "onWeeklyMessages: " + list.size());
        bag = new ResponseBag();
        bag.setWeeklyMessages(list);
        Collections.sort(bag.getWeeklyMessages());
        bag.setType(ResponseBag.WEEKLY_MASTERCLASS);
        setFragment();
        cachePresenter.cacheWeeklyMessages(list);
        presenter.getAllWeeklyMessages();
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
        Log.i(TAG, "onCachePodcasts " + list.size());
        bag = new ResponseBag();
        bag.setPodcasts(list);
        bag.setType(ResponseBag.PODCASTS);
        setFragment();
        presenter.getAllPodcasts();
    }

    @Override
    public void onCacheVideos(List<VideoDTO> list) {
        Log.i(TAG, "onCacheVideos " + list.size());
        bag = new ResponseBag();
        bag.setVideos(list);
        bag.setType(ResponseBag.VIDEOS);
        setFragment();
        presenter.getAllVideos();
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
        Log.i(TAG, "onCacheWeeklyMessages " + list.size());
        bag = new ResponseBag();
        bag.setWeeklyMessages(list);
        bag.setType(ResponseBag.WEEKLY_MESSAGE);
        setFragment();
        presenter.getAllWeeklyMessages();
    }

    @Override
    public void onCachePhotos(List<PhotoDTO> list) {
        Log.i(TAG, "onCachePhotos " + list.size());
        bag = new ResponseBag();
        bag.setPhotos(list);
        bag.setType(ResponseBag.PHOTOS);
        setFragment();
        presenter.getAllPhotos();
    }

    @Override
    public void onCacheCalendarEvents(List<CalendarEventDTO> list) {
        Log.i(TAG, "onCacheCalendarEvents " + list.size());
        bag = new ResponseBag();
        bag.setCalendarEvents(list);
        bag.setType(ResponseBag.CALENDAR_EVENTS);
        setFragment();
        presenter.getAllCalendarEvents();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onAddEntity() {

    }

    @Override
    public void onDeleteClicked(BaseDTO entity) {

    }

    @Override
    public void onLinksRequired(BaseDTO entity) {

    }

    @Override
    public void onPhotoCaptureRequested(BaseDTO entity) {

    }

    @Override
    public void onVideoCaptureRequested(BaseDTO entity) {

    }

    @Override
    public void onSomeActionRequired(BaseDTO entity) {

    }

    @Override
    public void onMicrophoneRequired(BaseDTO entity) {

    }

    @Override
    public void onEntityClicked(BaseDTO entity) {

    }

    @Override
    public void onCalendarRequested(BaseDTO entity) {

    }

    @Override
    public void onEntityDetailRequested(BaseDTO entity, int type) {

    }

    @Override
    public void onDeleteTooltipRequired(int type) {

    }

    @Override
    public void onLinksTooltipRequired(int type) {

    }

    @Override
    public void onPhotoCaptureTooltipRequired(int type) {

    }

    @Override
    public void onVideoCaptureTooltipRequired(int type) {

    }

    @Override
    public void onSomeActionTooltipRequired(int type) {

    }

    @Override
    public void onMicrophoneTooltipRequired(int type) {

    }

    @Override
    public void onCalendarTooltipRequired(int type) {

    }

    @Override
    public void onNewsArticleRequested(BaseDTO entity) {

    }

    @Override
    public void onPhotoTapped(PhotoDTO photo) {

    }

    @Override
    public void onPodcastTapped(PodcastDTO podcast) {

    }

    @Override
    public void onVideoTapped(VideoDTO video) {

    }

    @Override
    public void onEBookTapped(EBookDTO eBook) {

    }

    @Override
    public void onDailyThoughtTapped(DailyThoughtDTO dailyThought) {

    }

    @Override
    public void onWeeklyMasterClassTapped(WeeklyMasterClassDTO weeklyMasterClass) {

    }

    @Override
    public void onMessageTapped(WeeklyMessageDTO weeklyMessage) {

    }

    @Override
    public void onCalendarEventTapped(CalendarEventDTO calendarEvent) {

    }

    @Override
    public void onNewsArticleTapped(NewsDTO article) {

    }

   /* @Override
    public void onThoughtClicked(int position) {

    }*/

    /**
     * Adapter to manage fragments in view pager
     */
    private static class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return (Fragment) pageFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return pageFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            PageFragment pf = pageFragmentList.get(position);
            return pf.getTitle();
        }
    }


static final String LOG = SubscriberMainActivity.class.getSimpleName();
    @Override
    public void onBackPressed() {
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        } */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.subscriber_main, menu);
        return true;
    }

    static boolean logOff;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

      //  int id = item.getItemId();

        //noinspection SimplifiableIfStatement
  /*      if (id == R.id.cancel_action) {
            SharedPrefUtil.clearProfile(ctx);
            Intent intent = new Intent(SubscriberMainActivity.this, SubscriberSignInActivityBase.class);
            startActivity(intent);
            logOff = true;
            finish();
            return true;
        }
        if (id == R.id.action_media) {
            Intent intent = new Intent(SubscriberMainActivity.this, MediaListActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_photo){
            Intent intent = new Intent(SubscriberMainActivity.this, PhotoActivity.class);
            startActivity(intent);
            return true;
        }
*/


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_news_article) {
            mPager.setCurrentItem(0, true);
            return true;
        }
        else if (id == R.id.nav_daily_thought) {
            mPager.setCurrentItem(1, true);
            return true;

        }  else if (id == R.id.nav_master) {
            mPager.setCurrentItem(2, true);
            return true;

        } else if (id == R.id.nav_weekly_message) {
            mPager.setCurrentItem(3, true);
            return true;
        }else if (id == R.id.nav_podcast) {
            Intent intent = new Intent(SubscriberMainActivity.this, PodcastActivity.class);
            startActivity(intent);
            return true;

        } /*else if (id == R.id.nav_video) {
            Intent intent = new Intent(SubscriberMainActivity.this, VideoActivity.class);
            startActivity(intent);
            return true;

        }*/ else if (id == R.id.nav_eBooks) {
            Intent intent = new Intent(SubscriberMainActivity.this, eBookActivity.class);
            startActivity(intent);
            return true;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void logoff() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        onLoggedOut();
                    }
                });
    }

    private void onLoggedOut() {
        finish();
    }

    private FCMData fcmData;

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            fcmData = (FCMData)intent.getSerializableExtra("data");
            if (fcmData != null) {
                Log.i(TAG, "onReceive: ".concat(fcmData.getTitle().concat("\n ").concat(fcmData.getMessage())));
                showSnackbar(fcmData.getTitle(),"OK","green");
            }

        }
    }
    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                showMessage();
            }
        });
        snackbar.show();

    }

    private void showMessage() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(fcmData.getTitle())
                .setMessage(fcmData.getMessage())
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }




    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String TAG = SubscriberMainActivity.class.getSimpleName();

}
