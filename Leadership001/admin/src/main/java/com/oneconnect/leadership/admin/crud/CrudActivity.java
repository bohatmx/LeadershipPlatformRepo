package com.oneconnect.leadership.admin.crud;

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

import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
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
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.List;

public class CrudActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CrudContract.View, CacheContract.View {

    private EntityListFragment entityListFragment;
    private ResponseBag bag;
    private CrudPresenter presenter;
    private CachePresenter cachePresenter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private UserDTO user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leadership Platform");
        getSupportActionBar().setSubtitle("Data Management");

        presenter = new CrudPresenter(this);
        cachePresenter = new CachePresenter(this,this);
        user = SharedPrefUtil.getUser(this);
        setup();
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        entityListFragment = EntityListFragment.newInstance(bag);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }

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
        getMenuInflater().inflate(R.menu.crud, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
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
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "onEntityAdded: data has been added, key: ".concat(key));
    }

    @Override
    public void onUserCreated(UserDTO user) {

    }

    @Override
    public void onCategories(List<CategoryDTO> list) {
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
        bag = new ResponseBag();
        bag.setDailyThoughts(list);
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
    }

    @Override
    public void onEbooks(List<EBookDTO> list) {
        bag = new ResponseBag();
        bag.seteBooks(list);
        bag.setType(ResponseBag.EBOOKS);
        setFragment();
    }

    @Override
    public void onPayments(List<PaymentDTO> list) {

    }

    @Override
    public void onPodcasts(List<PodcastDTO> list) {
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
        bag = new ResponseBag();
        bag.setPrices(list);
        bag.setType(ResponseBag.PRICE);
        setFragment();
    }

    @Override
    public void onUsers(List<UserDTO> list) {

    }

    @Override
    public void onNews(List<NewsDTO> list) {
        bag = new ResponseBag();
        bag.setNews(list);
        bag.setType(ResponseBag.NEWS);
        setFragment();
    }

    @Override
    public void onSubscriptions(List<SubscriptionDTO> list) {
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
        bag = new ResponseBag();
        bag.setWeeklyMasterClasses(list);
        bag.setType(ResponseBag.WEEKLY_MASTERCLASS);
        setFragment();
    }

    @Override
    public void onWeeklyMessages(List<WeeklyMessageDTO> list) {
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

    }

    @Override
    public void onCacheCategories(List<CategoryDTO> list) {
        bag = new ResponseBag();
        bag.setCategories(list);
        bag.setType(ResponseBag.CATEGORIES);
        setFragment();
        //refresh anyway
        presenter.getCategories(user.getCompanyID());

    }

    @Override
    public void onCacheDailyThoughts(List<DailyThoughtDTO> list) {
        bag = new ResponseBag();
        bag.setDailyThoughts(list);
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
        presenter.getDailyThoughts(user.getCompanyID());
    }

    @Override
    public void onCacheEbooks(List<EBookDTO> list) {
        bag = new ResponseBag();
        bag.seteBooks(list);
        bag.setType(ResponseBag.EBOOKS);
        setFragment();
        presenter.getEbooks(user.getCompanyID());
    }

    @Override
    public void onCacheNews(List<NewsDTO> list) {
        bag = new ResponseBag();
        bag.setNews(list);
        bag.setType(ResponseBag.NEWS);
        setFragment();
        presenter.getNews(user.getCompanyID());
    }

    @Override
    public void onCachePodcasts(List<PodcastDTO> list) {
        bag = new ResponseBag();
        bag.setPodcasts(list);
        bag.setType(ResponseBag.PODCASTS);
        setFragment();
        presenter.getPodcasts(user.getCompanyID());
    }

    @Override
    public void onCachePrices(List<PriceDTO> list) {
        bag = new ResponseBag();
        bag.setPrices(list);
        bag.setType(ResponseBag.PRICE);
        setFragment();
        presenter.getPrices(user.getCompanyID());
    }

    @Override
    public void onCacheSubscriptions(List<SubscriptionDTO> list) {
        bag = new ResponseBag();
        bag.setSubscriptions(list);
        bag.setType(ResponseBag.SUBSCRIPTIONS);
        setFragment();
        presenter.getSubscriptions(user.getCompanyID());
    }

    @Override
    public void onCacheUsers(List<UserDTO> list) {

    }

    @Override
    public void onCacheWeeklyMasterclasses(List<WeeklyMasterClassDTO> list) {
        bag = new ResponseBag();
        bag.setWeeklyMasterClasses(list);
        bag.setType(ResponseBag.WEEKLY_MASTERCLASS);
        setFragment();
        presenter.getWeeklyMasterclasses(user.getCompanyID());
    }

    @Override
    public void onCacheWeeklyMessages(List<WeeklyMessageDTO> list) {
        bag = new ResponseBag();
        bag.setWeeklyMessages(list);
        bag.setType(ResponseBag.WEEKLY_MESSAGE);
        setFragment();
        presenter.getWeeklyMessages(user.getCompanyID());
    }

    @Override
    public void onError(String message) {

    }

    public static final String TAG = CrudActivity.class.getSimpleName();
}
