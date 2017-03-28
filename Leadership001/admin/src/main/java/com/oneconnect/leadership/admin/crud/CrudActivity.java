package com.oneconnect.leadership.admin.crud;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.admin.calendar.CalendarActivity;
import com.oneconnect.leadership.admin.camera.CameraActivity;
import com.oneconnect.leadership.admin.links.LinksActivity;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.PhotoCache;
import com.oneconnect.leadership.library.cache.VideoCache;
import com.oneconnect.leadership.library.data.BaseDTO;
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
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.lists.MediaListActivity;
import com.oneconnect.leadership.library.services.PhotoUploaderService;
import com.oneconnect.leadership.library.services.VideoUploaderService;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    private ProgressDialog progressDialog;
    private DatePickerDialog datePickerDialog;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private PodcastDTO podcast;

    private DailyThoughtEditor dailyThoughtEditor;
    private WeeklyMessageEditor weeklyMessageEditor;
    private WeeklyMasterclassEditor weeklyMasterclassEditor;

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
            startPhotoService();
            startVideoService();

            IntentFilter f = new IntentFilter(PhotoUploaderService.BROADCAST_PHOTO_UPLOADED);
            LocalBroadcastManager.getInstance(this)
                    .registerReceiver(new PhotoUploadReceiver(), f);

            IntentFilter f2 = new IntentFilter(VideoUploaderService.BROADCAST_VIDEO_UPLOADED);
            LocalBroadcastManager.getInstance(this)
                    .registerReceiver(new VideoUploadReceiver(), f2);
        }

    }

    private void getDate(final int sheetType) {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day);
                Date d = cal.getTime();
                switch (sheetType) {
                    case ResponseBag.DAILY_THOUGHTS:
                        dailyThoughtEditor.setSelectedDate(d);
                        break;
                    case ResponseBag.WEEKLY_MESSAGE:
                        weeklyMessageEditor.setSelectedDate(d);
                        break;
                    case ResponseBag.WEEKLY_MASTERCLASS:
                        weeklyMasterclassEditor.setSelectedDate(d);
                        break;
                }
            }
        }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void startUserBottomSheet(final UserDTO user, int type) {

        final UserEditorBottomSheet myBottomSheet =
                UserEditorBottomSheet.newInstance(user, type);
        myBottomSheet.setCrudPresenter(presenter);
        myBottomSheet.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                UserDTO m = (UserDTO) entity;
                if (bag.getUsers() == null) {
                    bag.setUsers(new ArrayList<UserDTO>());
                }
                bag.getUsers().add(0, m);
                setFragment();
                myBottomSheet.dismiss();
                showSnackbar(m.getFullName().concat(" is being added/updated"), getString(R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                Log.d(TAG, "onDateRequired: date not required");
            }

            @Override
            public void onError(String message) {
                showSnackbar(message,"bad",Constants.RED);
            }
        });
        myBottomSheet.show(getSupportFragmentManager(), "SHEET_USER");

    }

    private void startCategoryBottomSheet(final CategoryDTO category, int type) {

        final CategoryEditorBottomSheet myBottomSheet =
                CategoryEditorBottomSheet.newInstance(category, type);
        myBottomSheet.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                CategoryDTO m = (CategoryDTO) entity;
                if (bag.getCategories() == null) {
                    bag.setCategories(new ArrayList<CategoryDTO>());
                }
                bag.getCategories().add(0, m);
                setFragment();
                myBottomSheet.dismiss();
                showSnackbar(m.getCategoryName().concat(" is being added/updated"), getString(R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                Log.d(TAG, "onDateRequired: none needed");
            }

            @Override
            public void onError(String message) {
                showSnackbar(message,"bad",Constants.RED);
            }
        });
        myBottomSheet.show(getSupportFragmentManager(), "SHEET_CATEGORY");

    }

    private void startDailyThoughtBottomSheet(final DailyThoughtDTO thought, int type) {

        dailyThoughtEditor =
                DailyThoughtEditor.newInstance(thought, type);
        dailyThoughtEditor.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                DailyThoughtDTO m = (DailyThoughtDTO) entity;
                if (bag.getDailyThoughts() == null) {
                    bag.setDailyThoughts(new ArrayList<DailyThoughtDTO>());
                }
                bag.getDailyThoughts().add(0, m);
                setFragment();
                showSnackbar(m.getTitle().concat(" is being added/updated"), getString(R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                getDate(ResponseBag.DAILY_THOUGHTS);
            }

            @Override
            public void onError(String message) {
                showSnackbar(message,"bad",Constants.RED);
            }
        });

        dailyThoughtEditor.show(getSupportFragmentManager(), "SHEET_DAILY_THOUGHT");

    }
    private void startWeeklyMessageBottomSheet(final WeeklyMessageDTO weeklyMessage, int type) {

        weeklyMessageEditor =
                WeeklyMessageEditor.newInstance(weeklyMessage, type);
        weeklyMessageEditor.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                WeeklyMessageDTO m = (WeeklyMessageDTO) entity;
                if (bag.getWeeklyMessages() == null) {
                    bag.setWeeklyMessages(new ArrayList<WeeklyMessageDTO>());
                }
                bag.getWeeklyMessages().add(0, m);
                setFragment();
                showSnackbar(m.getTitle().concat(" is being added/updated"), getString(R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                getDate(ResponseBag.WEEKLY_MESSAGE);
            }

            @Override
            public void onError(String message) {
                showSnackbar(message,"bad",Constants.RED);
            }
        });
        weeklyMessageEditor.show(getSupportFragmentManager(), "SHEET_WEEKLY_MESSAGE");

    }

    private void startWeeklyMasterclassBottomSheet(final WeeklyMasterClassDTO weeklyMasterClass, int type) {

        weeklyMasterclassEditor =
                WeeklyMasterclassEditor.newInstance(weeklyMasterClass, type);
        weeklyMasterclassEditor.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                WeeklyMasterClassDTO m = (WeeklyMasterClassDTO) entity;
                if (bag.getWeeklyMasterClasses() == null) {
                    bag.setWeeklyMasterClasses(new ArrayList<WeeklyMasterClassDTO>());
                }
                bag.getWeeklyMasterClasses().add(0, m);
                setFragment();
                showSnackbar(m.getTitle().concat(" is being added/updated"), getString(R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                getDate(ResponseBag.WEEKLY_MASTERCLASS);
            }

            @Override
            public void onError(String message) {
                showSnackbar(message,"bad",Constants.RED);
            }
        });
        weeklyMasterclassEditor.show(getSupportFragmentManager(), "SHEET_WEEKLY_MASTERCLASS");

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
        drawer.openDrawer(GravityCompat.START, true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment");
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
                Toasty.warning(this, "Under construction", Toast.LENGTH_SHORT, true).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_categories:
                type = ResponseBag.CATEGORIES;
                cachePresenter.getCacheCategories();
                break;
            case R.id.nav_daily:
                type = ResponseBag.DAILY_THOUGHTS;
                cachePresenter.getCacheDailyThoughts();
                break;
            case R.id.nav_ebooks:
                type = ResponseBag.EBOOKS;
                cachePresenter.getCacheEbooks();
                break;
            case R.id.nav_podcasts:
                type = ResponseBag.PODCASTS;
                cachePresenter.getCachePodcasts();
                break;
            case R.id.nav_subscrip:
                type = ResponseBag.SUBSCRIPTIONS;
                cachePresenter.getCacheSubscriptions();
                break;

            case R.id.nav_weekly_master:
                type = ResponseBag.WEEKLY_MASTERCLASS;
                cachePresenter.getCacheWeeklyMasterclasses();
                break;
            case R.id.nav_weekly_message:
                type = ResponseBag.WEEKLY_MESSAGE;
                cachePresenter.getCacheWeeklyMessages();
                break;

            case R.id.nav_users:
                type = ResponseBag.USERS;
                cachePresenter.getCacheUsers();
                break;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onEntityAdded(String key) {
        Log.w(TAG, "onEntityAdded: ++++++++++ data has been added, key: ".concat(key));
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThoughtEditor.dismiss();
                break;
            case ResponseBag.CATEGORIES:

                break;
            case ResponseBag.PODCASTS:

                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                break;
        }
    }

    @Override
    public void onEntityUpdated() {
        Log.w(TAG, "onEntityUpdated: data has been updated");
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onUserCreated(UserDTO user) {
        Log.i(TAG, "onUserCreated: ######### CREATED: ".concat(GSON.toJson(user)));
        presenter.getUsers(this.user.getCompanyID());
        showSnackbar(user.getFullName().concat(" has been added"), getString(R.string.ok_label), "green");
    }

    @Override
    public void onCategories(List<CategoryDTO> list) {
        Log.i(TAG, "onCategories: " + list.size());
        bag = new ResponseBag();
        bag.setCategories(list);
        Collections.sort(bag.getCategories());
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
        Collections.sort(bag.getDailyThoughts());
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
        cachePresenter.cacheDailyThoughts(list);
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
        Log.i(TAG, "onPayments " + list.size());
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
        Log.i(TAG, "onPrices " + list.size());
        bag = new ResponseBag();
        bag.setPrices(list);
        bag.setType(ResponseBag.PRICES);
        setFragment();
        cachePresenter.cachePrices(list);
    }

    @Override
    public void onUsers(List<UserDTO> list) {
        Log.i(TAG, "onUsers " + list.size());
        bag = new ResponseBag();
        bag.setUsers(list);
        Collections.sort(bag.getUsers());
        bag.setType(ResponseBag.USERS);
        setFragment();
        cachePresenter.cacheUsers(list);
    }

    @Override
    public void onNews(List<NewsDTO> list) {
        Log.i(TAG, "onNews " + list.size());
        bag = new ResponseBag();
        bag.setNews(list);
        bag.setType(ResponseBag.NEWS);
        setFragment();
        cachePresenter.cacheNews(list);
    }

    @Override
    public void onSubscriptions(List<SubscriptionDTO> list) {
        Log.i(TAG, "onSubscriptions " + list.size());
        bag = new ResponseBag();
        bag.setSubscriptions(list);
        bag.setType(ResponseBag.SUBSCRIPTIONS);
        setFragment();
        cachePresenter.cacheSubscriptions(list);
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
        cachePresenter.cacheWeeklyMasterclasses(list);
    }

    @Override
    public void onWeeklyMessages(List<WeeklyMessageDTO> list) {
        Log.i(TAG, "onWeeklyMessages " + list.size());
        bag = new ResponseBag();
        bag.setWeeklyMessages(list);
        bag.setType(ResponseBag.WEEKLY_MESSAGE);
        setFragment();
        cachePresenter.cacheWeeklyMessages(list);
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
        Collections.sort(bag.getCategories());
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
        Collections.sort(bag.getDailyThoughts());
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
        bag.setType(ResponseBag.PRICES);
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
        Collections.sort(bag.getUsers());
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
        showSnackbar(message, "Not OK", "red");
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
        Log.w(TAG, "onAddEntity: ........open bottom appropriate sheet");
        switch (type) {
            case ResponseBag.USERS:
                startUserBottomSheet(null, Constants.NEW_ENTITY);
                break;
            case ResponseBag.CATEGORIES:
                startCategoryBottomSheet(null, Constants.NEW_ENTITY);
                break;
            case ResponseBag.DAILY_THOUGHTS:
                startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                startWeeklyMessageBottomSheet(null, Constants.NEW_ENTITY);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                startWeeklyMasterclassBottomSheet(null, Constants.NEW_ENTITY);
                break;
        }

    }

    @Override
    public void onDeleteClicked(BaseDTO entity) {
        Log.w(TAG, "onDeleteClicked: ..............");
        if (isTooltip) {
            isTooltip = false;
            return;
        }

    }

    @Override
    public void onLinksRequired(BaseDTO entity) {
        Log.w(TAG, "onLinksRequired: ..............");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        Intent m = null;
        switch (type) {
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO)entity;
                m = new Intent(this, LinksActivity.class);
                m.putExtra("weeklyMasterClass", weeklyMasterClass);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO)entity;
                m = new Intent(this, LinksActivity.class);
                m.putExtra("weeklyMessage", weeklyMessage);
                break;
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO)entity;
                m = new Intent(this, LinksActivity.class);
                m.putExtra("dailyThought", dailyThought);
                break;
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO)entity;
                m = new Intent(this, LinksActivity.class);
                m.putExtra("podcast", podcast);
                break;
        }
        m.putExtra("type", type);
        startActivityForResult(m, REQUEST_LINKS);
    }

    public static final int REQUEST_LINKS = 1875;

    @Override
    public void onPhotoCaptureRequested(BaseDTO entity) {
        Log.w(TAG, "onPhotoCaptureRequested: .................");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        pickGalleryOrCamera(entity);
    }

    private void pickGalleryOrCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Select Images")
                .setMessage("Please select the source of the photos")
                .setPositiveButton("Use Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startCamera(base);
                    }
                }).setNegativeButton("Use Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void startCamera(BaseDTO entity) {

        Intent m = new Intent(this, CameraActivity.class);
        m.putExtra("type", CameraActivity.CAMERA_REQUEST);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                m.putExtra("dailyThought", dailyThought);
                Log.d(TAG, "startCamera: ".concat(GSON.toJson(dailyThought)));
                break;
        }

        startActivityForResult(m, CameraActivity.CAMERA_REQUEST);
    }

    @Override
    public void onVideoCaptureRequested(BaseDTO entity) {
        Log.w(TAG, "onVideoCaptureRequested: .................");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        pickGalleryOrVideoCamera(entity);
    }

    private void pickGalleryOrVideoCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Select Images")
                .setMessage("Please select the source of the photos")
                .setPositiveButton("Use Video Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startVideo(base);
                    }
                }).setNegativeButton("Use Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void startVideo(BaseDTO entity) {

        Intent m = new Intent(this, CameraActivity.class);
        m.putExtra("type", CameraActivity.VIDEO_REQUEST);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                m.putExtra("dailyThought", dailyThought);
                break;
        }

        startActivityForResult(m, CameraActivity.VIDEO_REQUEST);
    }

    public static final int REQUEST_CALENDAR = 6258;

    @Override
    public void onSomeActionRequired(BaseDTO entity) {
        Log.d(TAG, "onSomeActionRequired: ");
    }

    private void startCalendar(int type, BaseDTO base) {
        Intent m = new Intent(this, CalendarActivity.class);
        m.putExtra("type", type);
        m.putExtra("base", base);

        Log.d(TAG, "onSomeActionRequired: send to CalendarActivity: "
                .concat(GSON.toJson(base)));

        startActivityForResult(m, REQUEST_CALENDAR);
    }

    @Override
    public void onMicrophoneRequired(BaseDTO entity) {
        Log.w(TAG, "onMicrophoneRequired: ,,,,,,,,,,,,,,,,,,,,");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        showSnackbar("Audio recording under construction", "OK", "cyan");
    }

    DailyThoughtDTO dailyThought;

    @Override
    public void onEntityClicked(BaseDTO entity) {
        Log.w(TAG, "onEntityClicked: .......".concat(GSON.toJson(entity)));
    }

    @Override
    public void onCalendarRequested(BaseDTO entity) {
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        Log.w(TAG, "onCalendarRequested: .......".concat(GSON.toJson(entity)));
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                SharedPrefUtil.saveDailyThought(dailyThought, this);
                startCalendar(ResponseBag.DAILY_THOUGHTS, dailyThought);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) entity;
                SharedPrefUtil.saveWeeklyMasterclass(weeklyMasterClass, this);
                startCalendar(ResponseBag.WEEKLY_MASTERCLASS, weeklyMasterClass);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) entity;
                SharedPrefUtil.saveWeeklyMessage(weeklyMessage, this);
                startCalendar(ResponseBag.WEEKLY_MESSAGE, weeklyMessage);
                break;
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) entity;
                SharedPrefUtil.savePodcast(podcast, this);
                startCalendar(ResponseBag.PODCASTS, podcast);
                break;
        }
    }

    @Override
    public void onEntityDetailRequested(BaseDTO entity, int entityType) {
        Log.d(TAG, "onEntityDetailRequested: ");
        Intent m = new Intent(this, MediaListActivity.class);
        m.putExtra("type",entityType);
        switch (entityType) {
            case ResponseBag.DAILY_THOUGHTS:
                m.putExtra("dailyThought", (DailyThoughtDTO)entity);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                m.putExtra("weeklyMasterClass", (WeeklyMasterClassDTO)entity);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                m.putExtra("weeklyMessage", (WeeklyMessageDTO)entity);
                break;
            case ResponseBag.PODCASTS:
                m.putExtra("podcast", (PodcastDTO)entity);
                break;
            case ResponseBag.EBOOKS:
                m.putExtra("eBook", (EBookDTO)entity);
                break;
        }
        startActivityForResult(m,REQUEST_MEDIALIST);
    }
    private static final int REQUEST_MEDIALIST = 690;
    boolean isTooltip;
    @Override
    public void onDeleteTooltipRequired(int type) {
        isTooltip = true;
        Toasty.warning(this,"Remove this record",
                Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onLinksTooltipRequired(int type) {
        isTooltip = true;
        Toasty.info(this,"Add internet links to this record",
                Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onPhotoCaptureTooltipRequired(int type) {
        isTooltip = true;
        Toasty.warning(this,"Add photos to this record",
                Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onVideoCaptureTooltipRequired(int type) {
        isTooltip = true;
        Toasty.info(this,"Add videos to this record",
                Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onSomeActionTooltipRequired(int type) {
        isTooltip = true;
        Toasty.error(this,"Add calendar event to this record",
                Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onMicrophoneTooltipRequired(int type) {
        isTooltip = true;
        Toasty.success(this,"Add audio recording to this record",
                Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onCalendarTooltipRequired(int type) {
        isTooltip = true;
        Toasty.success(this,"Add calendar event to this record",
                Toast.LENGTH_SHORT,true).show();
    }

    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        Log.w(TAG, "########## onActivityResult: result: " + result
                + " reqCode: " + reqCode);
        switch (reqCode) {
            case REQUEST_CALENDAR:
                Log.d(TAG, "onActivityResult: calendar activity returned: " + result);
                break;
            case CameraActivity.CAMERA_REQUEST:
                confirmUpload(data);
                break;
            case CameraActivity.VIDEO_REQUEST:
                confirmUpload(data);
                break;
        }

    }

    private void confirmUpload(final Intent data) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Upload Confirmation")
                .setMessage("Do you want to upload the captured files?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveFiles(data);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toasty.warning(getApplicationContext(), "Media file(s) released",
                                Toast.LENGTH_LONG, true).show();
                    }
                })
                .show();
    }

    private void saveFiles(Intent data) {
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                saveDailyThoughtFiles(data);
                break;
            case ResponseBag.PODCASTS:
                savePodcastFiles(data);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                saveWeeklyMessageFiles(data);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                saveWeeklyMasterclassFiles(data);
                break;
        }
    }

    private void saveDailyThoughtFiles(Intent data) {
        ResponseBag bag = (ResponseBag) data.getSerializableExtra("bag");
        if (bag == null) return;
        Log.d(TAG, "saveDailyThoughtFiles: .......");
        for (PhotoDTO p : bag.getPhotos()) {
            p.setDailyThoughtID(dailyThought.getDailyThoughtID());
            p.setCaption(dailyThought.getTitle());
            PhotoCache.addPhoto(p, this, null);
        }
        for (VideoDTO p : bag.getVideos()) {
            p.setDailyThoughtID(dailyThought.getDailyThoughtID());
            p.setCaption(dailyThought.getTitle());
            Log.e(TAG, "saveDailyThoughtFiles: ".concat(GSON.toJson(p)));
            VideoCache.addVideo(p, this, null);
        }
        startPhotoService();
        startVideoService();

    }

    private void saveWeeklyMessageFiles(Intent data) {
        ResponseBag bag = (ResponseBag) data.getSerializableExtra("bag");
        if (bag == null) return;
        Log.d(TAG, "saveWeeklyMessageFiles: ..........");
        for (PhotoDTO p : bag.getPhotos()) {
            p.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
            p.setCaption(weeklyMessage.getTitle());
            PhotoCache.addPhoto(p, this, null);
        }
        for (VideoDTO p : bag.getVideos()) {
            p.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
            p.setCaption(weeklyMessage.getTitle());
            VideoCache.addVideo(p, this, null);
        }
        startPhotoService();
        startVideoService();

    }

    private void saveWeeklyMasterclassFiles(Intent data) {
        ResponseBag bag = (ResponseBag) data.getSerializableExtra("bag");
        if (bag == null) return;
        Log.d(TAG, "saveWeeklyMasterclassFiles: ..............");
        for (PhotoDTO p : bag.getPhotos()) {
            p.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
            p.setCaption(weeklyMasterClass.getTitle());
            PhotoCache.addPhoto(p, this, null);
        }
        for (VideoDTO p : bag.getVideos()) {
            p.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
            p.setCaption(weeklyMasterClass.getTitle());
            VideoCache.addVideo(p, this, null);
        }
        startPhotoService();
        startVideoService();

    }

    private void savePodcastFiles(Intent data) {
        ResponseBag bag = (ResponseBag) data.getSerializableExtra("bag");
        if (bag == null) return;
        Log.d(TAG, "savePodcastFiles: ................");
        for (PhotoDTO p : bag.getPhotos()) {
            p.setPodcastID(podcast.getPodcastID());
            p.setCaption(podcast.getTitle());
            PhotoCache.addPhoto(p, this, null);
        }
        for (VideoDTO p : bag.getVideos()) {
            p.setPodcastID(podcast.getPodcastID());
            p.setCaption(podcast.getTitle());
            VideoCache.addVideo(p, this, null);
        }
        startPhotoService();
        startVideoService();

    }

    private void startVideoService() {
        Log.d(TAG, "startVideoService: @@@@@@@@@@@@@@@@@@@@@@");
        Intent m = new Intent(this, VideoUploaderService.class);
        startService(m);
    }

    private void startPhotoService() {
        Log.d(TAG, "startPhotoService: ###################");
        Intent m = new Intent(this, PhotoUploaderService.class);
        startService(m);
    }

    class PhotoUploadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: PhotoUploadReceiver");
            int cnt = intent.getIntExtra("photos", 0);
            String msg = "" + cnt + " Photos have been uploaded";
            Toasty.success(getApplicationContext(), msg,
                    Toast.LENGTH_LONG, true).show();
            showSnackbar(msg, getString(R.string.ok_label), Constants.GREEN);
        }
    }

    class VideoUploadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: VideoUploadReceiver");
            int cnt = intent.getIntExtra("videos", 0);
            String msg = "" + cnt + " Videos have been uploaded";
            Toasty.success(getApplicationContext(), msg,
                    Toast.LENGTH_LONG, true).show();
            showSnackbar(msg, getString(R.string.ok_label), Constants.GREEN);
        }
    }
}
