package com.oneconnect.leadership.subscriber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.activities.DailyThoughtActivity;
import com.oneconnect.leadership.library.activities.PodcastActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.activities.VideoActivity;
import com.oneconnect.leadership.library.activities.eBookActivity;
import com.oneconnect.leadership.library.adapters.DailyThoughtAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.BaseDTO;
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
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.EBookListFragment;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.lists.MediaListActivity;
import com.oneconnect.leadership.library.lists.PageFragment;
import com.oneconnect.leadership.library.lists.PodcastListFragment;
import com.oneconnect.leadership.library.lists.VideoListFragment;
import com.oneconnect.leadership.library.util.DepthPageTransformer;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.subscriber.services.SubscriberMessagingService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubscriberMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SubscriberContract.View, CacheContract.View, BasicEntityAdapter.EntityListener/*, DailyThoughtAdapter.DailyThoughtAdapterlistener*//*,
        PodcastListFragment.PodcastListener, VideoListFragment.VideoListener*/{


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        setContentView(R.layout.activity_subscriber_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();



        page = getIntent().getStringExtra("page");

        mPager = (ViewPager) findViewById(com.oneconnect.leadership.library.R.id.viewpager);
        PagerTitleStrip strip = (PagerTitleStrip) mPager.findViewById(com.oneconnect.leadership.library.R.id.pager_title_strip);
        strip.setVisibility(View.VISIBLE);


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
    }
    }

    static List<PageFragment> pageFragmentList;
    ResponseBag bag;

    private void setup() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

      //  dailyThoughtListFragment = (DailyThoughtListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       // setUpViewPager();
    }

    private int type;

    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment");
        SharedPrefUtil.saveFragmentType(bag.getType(), this);
        type = bag.getType();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
       // dailyThoughtListFragment = DailyThoughtListFragment.newInstance();
        entityListFragment = EntityListFragment.newInstance(bag);
        //entityListFragment.setListener(this);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }

    private PagerAdapter adapter;

    private void setUpViewPager() {
        //setMenuDestination();

        pageFragmentList = new ArrayList<>();
        dailyThoughtListFragment = DailyThoughtListFragment.newInstance();
        // entityListFragment = EntityListFragment.newInstance(bag);
        //podcastListFragment = PodcastListFragment.newInstance(podcast.getFilePath(), bag);
        //videoListFragment = VideoListFragment.newInstance("bag",bag);
        //eBookListFragment = EBookListFragment.newInstance();

         pageFragmentList.add(dailyThoughtListFragment);

        try {
            adapter = new PagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(adapter);
            mPager.setPageTransformer(true, new DepthPageTransformer());
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    PageFragment pf = pageFragmentList.get(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (Exception e) {
            Log.e(LOG, "PagerAdapter failed", e);
            if (page != null) {
                if (page.equalsIgnoreCase("Daily Thoughts")) {
                    mPager.setCurrentItem(0);
                }
                if (page.equalsIgnoreCase("Podcast")) {
                    mPager.setCurrentItem(1);
                }
                if (page.equalsIgnoreCase("Video")) {
                    mPager.setCurrentItem(2);
                }
                if (page.equalsIgnoreCase("eBooks")) {
                    mPager.setCurrentItem(3);
                }
            }
        }

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
    public void onEbooks(List<EBookDTO> list) {

        Log.i(TAG, "onEbooks " + list.size());
        bag = new ResponseBag();
        bag.seteBooks(list);
        bag.setType(ResponseBag.EBOOKS);
        setFragment();
        cachePresenter.cacheEbooks(list);
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
        Log.i(TAG, "onCachePodcasts " + list.size());
        bag = new ResponseBag();
        bag.setPodcasts(list);
        bag.setType(ResponseBag.PODCASTS);
        setFragment();
        presenter.getAllPodcasts();
       // presenter.getPodcasts(user.getCompanyID());
    }

    @Override
    public void onCacheVideos(List<VideoDTO> list) {
        Log.i(TAG, "onCacheVideos " + list.size());
        bag = new ResponseBag();
        bag.setVideos(list);
        bag.setType(ResponseBag.VIDEOS);
        setFragment();
        presenter.getAllVideos();
       // presenter.getVideos(user.getCompanyID());
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
        presenter.getWeeklyMessages(user.getCompanyID());
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subscriber_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_media) {
            Intent intent = new Intent(SubscriberMainActivity.this, MediaListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_daily_thought) {

            //mPager.setCurrentItem(0, true);
            Intent intent = new Intent(SubscriberMainActivity.this, DailyThoughtActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.nav_podcast) {
            Intent intent = new Intent(SubscriberMainActivity.this, PodcastActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.nav_video) {

            Intent intent = new Intent(SubscriberMainActivity.this, VideoActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_eBooks) {

            Intent intent = new Intent(SubscriberMainActivity.this, eBookActivity.class);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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