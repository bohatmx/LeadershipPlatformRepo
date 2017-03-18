package com.oneconnect.leadership.admin.crud;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DTOEntity;
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
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CrudActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CrudContract.View, CacheContract.View, BasicEntityAdapter.EntityListener {

    private EntityListFragment entityListFragment;
    private ResponseBag bag;
    private CrudPresenter presenter;
    private CachePresenter cachePresenter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private UserDTO user;
    private int type;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
        Log.d(TAG, "onCreate: ************************");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leadership Platform");
        getSupportActionBar().setSubtitle("Data Management");
        setup();

        presenter = new CrudPresenter(this);
        cachePresenter = new CachePresenter(this, this);
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
                case ResponseBag.PRICE:
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
        }

    }

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
        drawer.openDrawer(GravityCompat.START,true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment" );
        SharedPrefUtil.saveFragmentType(bag.getType(), this);
        type = bag.getType();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        entityListFragment = EntityListFragment.newInstance(bag);
        entityListFragment.setListener(this);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_daily:
                cachePresenter.getCacheDailyThoughts();
                break;
            case R.id.action_news:
                cachePresenter.getCacheNews();
                break;
            case R.id.action_subs:
                cachePresenter.getCacheSubscriptions();
                break;
            case R.id.action_weekly_message:
                cachePresenter.getCacheWeeklyMessages();
                break;
            case R.id.action_help:
                Toasty.warning(this,"Under construction", Toast.LENGTH_SHORT,true).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_categories:
                cachePresenter.getCacheCategories();
                break;
            case R.id.nav_daily:
                cachePresenter.getCacheDailyThoughts();
                break;
            case R.id.nav_ebooks:
                cachePresenter.getCacheEbooks();
                break;
            case R.id.nav_podcasts:
                cachePresenter.getCachePodcasts();
                break;
            case R.id.nav_subscrip:
                cachePresenter.getCacheSubscriptions();
                break;
            case R.id.nav_news:
                cachePresenter.getCacheNews();
                break;
            case R.id.nav_weekly_master:
                cachePresenter.getCacheWeeklyMasterclasses();
                break;
            case R.id.nav_weekly_message:
                cachePresenter.getCacheWeeklyMessages();
                break;
            case R.id.nav_prices:
                cachePresenter.getCachePrices();
                break;
            case R.id.nav_users:
                cachePresenter.getCacheUsers();
                break;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onEntityAdded(String key) {
        Log.w(TAG, "onEntityAdded: ++++++++++ data has been added, key: ".concat(key));
    }

    @Override
    public void onUserCreated(UserDTO user) {

    }

    @Override
    public void onCategories(List<CategoryDTO> list) {
        Log.i(TAG, "onCategories: "+ list.size());
        bag = new ResponseBag();
        bag.setCategories(list);
        bag.setType(ResponseBag.CATEGORIES);
        cachePresenter.cacheCategories(list);
        setFragment();
    }

    @Override
    public void onCompanies(List<CompanyDTO> list) {

    }

    @Override
    public void onDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "onDailyThoughts: " + list.size());
        bag = new ResponseBag();
        bag.setDailyThoughts(list);
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
    }

    @Override
    public void onEbooks(List<EBookDTO> list) {
        Log.i(TAG, "onEbooks " + list.size());
        bag = new ResponseBag();
        bag.seteBooks(list);
        bag.setType(ResponseBag.EBOOKS);
        setFragment();
    }

    @Override
    public void onPayments(List<PaymentDTO> list) {
        Log.i(TAG, "onPayments " + list.size());
    }

    @Override
    public void onPodcasts(List<PodcastDTO> list) {
        Log.i(TAG, "onPodcasts " + list.size());
        bag = new ResponseBag();
        bag.setPodcasts(list);
        bag.setType(ResponseBag.PODCASTS);
        setFragment();
    }

    @Override
    public void onPhotos(List<PhotoDTO> list) {

    }

    @Override
    public void onPrices(List<PriceDTO> list) {
        Log.i(TAG, "onPrices " + list.size());
        bag = new ResponseBag();
        bag.setPrices(list);
        bag.setType(ResponseBag.PRICE);
        setFragment();
    }

    @Override
    public void onUsers(List<UserDTO> list) {
        Log.i(TAG, "onUsers " + list.size());
        bag = new ResponseBag();
        bag.setUsers(list);
        bag.setType(ResponseBag.USERS);
        setFragment();
    }

    @Override
    public void onNews(List<NewsDTO> list) {
        Log.i(TAG, "onNews " + list.size());
        bag = new ResponseBag();
        bag.setNews(list);
        bag.setType(ResponseBag.NEWS);
        setFragment();
    }

    @Override
    public void onSubscriptions(List<SubscriptionDTO> list) {
        Log.i(TAG, "onSubscriptions " + list.size());
        bag = new ResponseBag();
        bag.setSubscriptions(list);
        bag.setType(ResponseBag.SUBSCRIPTIONS);
        setFragment();
    }

    @Override
    public void onVideos(List<VideoDTO> list) {

    }

    @Override
    public void onWeeklyMasterclasses(List<WeeklyMasterClassDTO> list) {
        Log.i(TAG, "onWeeklyMasterclasses " + list.size());
        bag = new ResponseBag();
        bag.setWeeklyMasterClasses(list);
        bag.setType(ResponseBag.WEEKLY_MASTERCLASS);
        setFragment();
    }

    @Override
    public void onWeeklyMessages(List<WeeklyMessageDTO> list) {
        Log.i(TAG, "onWeeklyMessages " + list.size());
        bag = new ResponseBag();
        bag.setWeeklyMessages(list);
        bag.setType(ResponseBag.WEEKLY_MESSAGE);
        setFragment();
    }

    @Override
    public void onDevices(List<DeviceDTO> companyID) {

    }

    @Override
    public void onDataCached() {
        Log.d(TAG, "onDataCached: $$$$$$$$$$$$$ data's on disk now, bro!");
    }

    @Override
    public void onCacheCategories(List<CategoryDTO> list) {
        Log.i(TAG, "onCacheCategories " + list.size());
        bag = new ResponseBag();
        bag.setCategories(list);
        bag.setType(ResponseBag.CATEGORIES);
        setFragment();
        //refresh anyway
        presenter.getCategories(user.getCompanyID());

    }

    @Override
    public void onCacheDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "onCacheDailyThoughts " + list.size());
        bag = new ResponseBag();
        bag.setDailyThoughts(list);
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
        presenter.getDailyThoughts(user.getCompanyID());
    }

    @Override
    public void onCacheEbooks(List<EBookDTO> list) {
        Log.i(TAG, "onCacheEbooks " + list.size());
        bag = new ResponseBag();
        bag.seteBooks(list);
        bag.setType(ResponseBag.EBOOKS);
        setFragment();
        presenter.getEbooks(user.getCompanyID());
    }

    @Override
    public void onCacheNews(List<NewsDTO> list) {
        Log.i(TAG, "onCacheNews " + list.size());
        bag = new ResponseBag();
        bag.setNews(list);
        bag.setType(ResponseBag.NEWS);
        setFragment();
        presenter.getNews(user.getCompanyID());
    }

    @Override
    public void onCachePodcasts(List<PodcastDTO> list) {
        Log.i(TAG, "onCachePodcasts " + list.size());
        bag = new ResponseBag();
        bag.setPodcasts(list);
        bag.setType(ResponseBag.PODCASTS);
        setFragment();
        presenter.getPodcasts(user.getCompanyID());
    }

    @Override
    public void onCachePrices(List<PriceDTO> list) {
        Log.i(TAG, "onCachePrices " + list.size());
        bag = new ResponseBag();
        bag.setPrices(list);
        bag.setType(ResponseBag.PRICE);
        setFragment();
        presenter.getPrices(user.getCompanyID());
    }

    @Override
    public void onCacheSubscriptions(List<SubscriptionDTO> list) {
        Log.i(TAG, "onCacheSubscriptions " + list.size());
        bag = new ResponseBag();
        bag.setSubscriptions(list);
        bag.setType(ResponseBag.SUBSCRIPTIONS);
        setFragment();
        presenter.getSubscriptions(user.getCompanyID());
    }

    @Override
    public void onCacheUsers(List<UserDTO> list) {
        Log.i(TAG, "onCacheUsers " + list.size());
        bag = new ResponseBag();
        bag.setUsers(list);
        bag.setType(ResponseBag.USERS);
        setFragment();
        presenter.getUsers(user.getCompanyID());
    }

    @Override
    public void onCacheWeeklyMasterclasses(List<WeeklyMasterClassDTO> list) {
        Log.i(TAG, "onCacheWeeklyMasterclasses " + list.size());
        bag = new ResponseBag();
        bag.setWeeklyMasterClasses(list);
        bag.setType(ResponseBag.WEEKLY_MASTERCLASS);
        setFragment();
        presenter.getWeeklyMasterclasses(user.getCompanyID());
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
          showSnackbar(message,"Not OK","red");
    }
    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }

    public static final String TAG = CrudActivity.class.getSimpleName();

    @Override
    public void onAddEntity() {
        //todo start editor of type n
        Log.w(TAG, "onAddEntity: ................." );
    }

    @Override
    public void onDeleteClicked(DTOEntity entity) {
        Log.w(TAG, "onDeleteClicked: .............." );

    }

    @Override
    public void onUpdateClicked(DTOEntity entity) {
        Log.w(TAG, "onUpdateClicked: .............." );
    }

    @Override
    public void onPhotoCaptureRequested(DTOEntity entity) {
        Log.w(TAG, "onPhotoCaptureRequested: ................." );
    }

    @Override
    public void onVideoCaptureRequested(DTOEntity entity) {
        Log.w(TAG, "onVideoCaptureRequested: ................." );
    }

    @Override
    public void onLocationRequested(DTOEntity entity) {
        Log.w(TAG, "onLocationRequested: ......................" );
    }

    @Override
    public void onEntityClicked(DTOEntity entity) {
        Log.w(TAG, "onEntityClicked: .........................." );
    }
}
