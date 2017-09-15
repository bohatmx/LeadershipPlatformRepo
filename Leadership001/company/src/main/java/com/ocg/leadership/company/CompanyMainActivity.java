package com.ocg.leadership.company;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.activities.PodcastActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.activities.ThemeSelectorActivity;
import com.oneconnect.leadership.library.activities.UserActivity;
import com.oneconnect.leadership.library.activities.eBookActivity;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.camera.VideoSelectionActivity;
import com.oneconnect.leadership.library.crud.CrudContract;
import com.oneconnect.leadership.library.crud.CrudPresenter;
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
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.editors.DailyThoughtEditor;
import com.oneconnect.leadership.library.editors.UserEditorBottomSheet;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.CalendarEventListFragment;
import com.oneconnect.leadership.library.lists.CompanyMainFragment;
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.EBookListFragment;
import com.oneconnect.leadership.library.lists.MasterListFragment;
import com.oneconnect.leadership.library.lists.NewsListFragment;
import com.oneconnect.leadership.library.lists.PageFragment;
import com.oneconnect.leadership.library.lists.PhotoListFragment;
import com.oneconnect.leadership.library.lists.PodcastListFragment;
import com.oneconnect.leadership.library.lists.UserListFragment;
import com.oneconnect.leadership.library.lists.VideoListFragment;
import com.oneconnect.leadership.library.lists.WeeklyMessageListFragment;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.DepthPageTransformer;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.ThemeChooser;
import com.oneconnect.leadership.library.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.WHITE;

public class CompanyMainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener,
        SubscriberContract.View, CrudContract.View, CacheContract.View, MasterListFragment.WeeklyMasterClassListener,
        WeeklyMessageListFragment.WeeklyMessageListener, PodcastListFragment.PodcastListener, VideoListFragment.VideoListener,
        PhotoListFragment.PhotoListener, EBookListFragment.EBookListener, DailyThoughtListFragment.DailyThoughtListener,
        NewsListFragment.NewsArticleListener, CompanyMainFragment.CompanyFragmentListener, UserListFragment.UserListListener {

    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private PodcastDTO podcast;
    private DailyThoughtListFragment dailyThoughtListFragment;
    private EBookListFragment eBookListFragment;
    private PodcastListFragment podcastListFragment;
    MasterListFragment masterListFragment;
    WeeklyMessageListFragment weeklyMessageListFragment;
    PhotoListFragment photoListFragment;
    CalendarEventListFragment calendarEventListFragment;
    UserListFragment userListFragment;
    NewsListFragment newsListFragment;
    private VideoListFragment videoListFragment;
    private CompanyMainFragment companyMainFragment;
    private DrawerLayout drawer;
    private String page;
    private ViewPager mPager;
    private CachePresenter cachePresenter;
    private SubscriberPresenter presenter;
    private CrudPresenter crudPresenter;
    private UserDTO user;
    private TextView usernametxt;
    PagerSlidingTabStrip strip;
    CategoryDTO category;
    private DailyThoughtDTO dailyThought;
    int currentIndex;
    static List<PageFragment> pageFragmentList;
    int themeDarkColor, themePrimaryColor, logo;
    private Toolbar toolbar;
    Context ctx;
    CardView card1, card2, card3, card4;
    TextView companyThoughts, companyVideos, companyUsers, companyPodcasts, companyTitle;
    ImageView companyLogo;
    private FirebaseAuth firebaseAuth;
    ResponseBag bag;
    private DailyThoughtEditor dailyThoughtEditor;
    private DatePickerDialog datePickerDialog;
    private int type;
    NavigationView navigationView;
    private PagerAdapter adapter;
    private CompanyMainActivity activity;
    private ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        ThemeChooser.setTheme(this);
        setContentView(R.layout.activity_company_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.harmony);

        ctx = getApplicationContext();
        activity = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//        ab.setDisplayHomeAsUpEnabled(true);

        page = getIntent().getStringExtra("page");
        mPager = (ViewPager) findViewById(R.id.viewpager);
        strip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        Resources.Theme theme = getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        themeDarkColor = typedValue.data;
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        themePrimaryColor = typedValue.data;
        strip.setBackgroundColor(themeDarkColor);

        strip.setVisibility(View.VISIBLE);
        strip.setTextColor(WHITE);
        strip.setBackgroundColor(themeDarkColor);
        setup();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
            window.setNavigationBarColor(themeDarkColor);
        }

        presenter = new SubscriberPresenter(this);
        crudPresenter = new CrudPresenter(this);
        cachePresenter = new CachePresenter(this, ctx);
        user = SharedPrefUtil.getUser(this);
        type = SharedPrefUtil.getFragmentType(this);
        if (type == 0) {
            cachePresenter.getCacheUsers();
        } else {
            switch (type) {
                case ResponseBag.CATEGORIES:
                    cachePresenter.getCacheCategories();
                    category = (CategoryDTO) getIntent().getSerializableExtra("category");
                    break;
                case ResponseBag.DAILY_THOUGHTS:
                    cachePresenter.getCacheDailyThoughts();
                    dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
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

            /*IntentFilter filter = new IntentFilter(SubscriberMessagingService.BROADCAST_MESSAGE_RECEIVED);
            LocalBroadcastManager.getInstance(this).registerReceiver(new MessageReceiver(), filter);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());*/
        }
    }

    private void setup() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpViewPager();
    }

    private void setUpViewPager() {
        setMenuDestination();

        pageFragmentList = new ArrayList<>();

        companyMainFragment = CompanyMainFragment.newInstance();
        newsListFragment = NewsListFragment.newInstance();
        dailyThoughtListFragment = DailyThoughtListFragment.newInstance();
        masterListFragment = MasterListFragment.newInstance();
        weeklyMessageListFragment = WeeklyMessageListFragment.newInstance();
       // calendarEventListFragment = CalendarEventListFragment.newInstance();
        //photoListFragment = PhotoListFragment.newInstance(new HashMap<String, PhotoDTO>());
        podcastListFragment = PodcastListFragment.newInstance(new HashMap<String, PodcastDTO>());
        //videoListFragment = VideoListFragment.newInstance(new HashMap<String, VideoDTO>());
        eBookListFragment = EBookListFragment.newInstance(new HashMap<String, EBookDTO>());
        userListFragment = UserListFragment.newInstance(new HashMap<String, UserDTO>());


        companyMainFragment.setPageTitle(ctx.getString(R.string.company_profile));
        newsListFragment.setPageTitle(ctx.getString(R.string.news_article));
        dailyThoughtListFragment.setPageTitle(ctx.getString(R.string.daily_thought));
        masterListFragment.setPageTitle(ctx.getString(R.string.weeky_master_class));
        weeklyMessageListFragment.setPageTitle(ctx.getString(R.string.weekly_message));
     //   calendarEventListFragment.setPageTitle(ctx.getString(R.string.calendar_events));
        //photoListFragment.setPageTitle(ctx.getString(R.string.photo));
        podcastListFragment.setPageTitle(ctx.getString(R.string.podcast));
        //videoListFragment.setPageTitle(ctx.getString(R.string.video));
        eBookListFragment.setPageTitle(ctx.getString(R.string.ebooks));
        userListFragment.setPageTitle(ctx.getString(R.string.users));

        companyMainFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        newsListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        dailyThoughtListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        masterListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        weeklyMessageListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        podcastListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        eBookListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        userListFragment.setThemeColors(themePrimaryColor, themeDarkColor);

        pageFragmentList.add(companyMainFragment);
        pageFragmentList.add(newsListFragment);
        pageFragmentList.add(dailyThoughtListFragment);
        pageFragmentList.add(masterListFragment);
        pageFragmentList.add(weeklyMessageListFragment);
      //  pageFragmentList.add(calendarEventListFragment);
        //pageFragmentList.add(photoListFragment);
        pageFragmentList.add(podcastListFragment);
        // pageFragmentList.add(videoListFragment);
        pageFragmentList.add(eBookListFragment);
        pageFragmentList.add(userListFragment);


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
            Log.e(TAG, "PagerAdapter failed", e);
            if (page != null) {

                if (page.equalsIgnoreCase("Company Profile")) {
                    mPager.setCurrentItem(0);
                }
                if (page.equalsIgnoreCase("Leadership News Article")) {
                    mPager.setCurrentItem(1);
                }
                if (page.equalsIgnoreCase("Leadership Daily Thoughts")) {
                    mPager.setCurrentItem(2);
                }
                if (page.equalsIgnoreCase("Leadership Weekly Masterclass")) {
                    mPager.setCurrentItem(3);
                }
                if (page.equalsIgnoreCase("Leadership Weekly Message")) {
                    mPager.setCurrentItem(4);
                }
                /*if (page.equalsIgnoreCase("Leadership Calendar Event")) {
                    mPager.setCurrentItem(5);
                }*/
                if (page.equalsIgnoreCase("Leadership Podcasts")) {
                    mPager.setCurrentItem(5);
                }
                if (page.equalsIgnoreCase("Leadership eBooks")) {
                    mPager.setCurrentItem(6);
                }

                if (page.equalsIgnoreCase("Leadership Staff")) {
                    mPager.setCurrentItem(7);
                }

            }
        }

    }

    @Override
    public void onThoughtsSelected() {
        startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
    }

    private void startDailyThoughtBottomSheet(final DailyThoughtDTO thought, int type) {

        dailyThoughtEditor = DailyThoughtEditor.newInstance(thought, type);
        dailyThoughtEditor.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                DailyThoughtDTO m = (DailyThoughtDTO) entity;
                if (bag.getDailyThoughts() == null) {
                    bag.setDailyThoughts(new ArrayList<DailyThoughtDTO>());
                }
                bag.getDailyThoughts().add(0, m);
                // setFragment();
                showSnackbar(m.getTitle().concat(" is being added/updated"), getString(com.oneconnect.leadership.library.R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                getDate(ResponseBag.DAILY_THOUGHTS);
            }

            @Override
            public void onError(String message) {
                showSnackbar(message, "bad", Constants.RED);
            }
        });

        dailyThoughtEditor.show(getSupportFragmentManager(), "SHEET_DAILY_THOUGHT");

    }

    private void startUserBottomSheet(final UserDTO user, int type) {

        final UserEditorBottomSheet myBottomSheet =
                UserEditorBottomSheet.newInstance(user, type);
        myBottomSheet.setCrudPresenter(crudPresenter);
        myBottomSheet.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                UserDTO m = (UserDTO) entity;
                if (bag.getUsers() == null) {
                    bag.setUsers(new ArrayList<UserDTO>());
                }
                bag.getUsers().add(0, m);
                //  setFragment();
                myBottomSheet.dismiss();
                showSnackbar(m.getFullName().concat(" is being added/updated"), getString(com.oneconnect.leadership.library.R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                Log.d(TAG, "onDateRequired: date not required");
            }

            @Override
            public void onError(String message) {
                showSnackbar(message, "bad", Constants.RED);
            }
        });
        myBottomSheet.show(getSupportFragmentManager(), "SHEET_USER");

    }

    private void getDate(final int sheetType) {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(/*this*/ctx, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day);
                Date d = cal.getTime();
                switch (sheetType) {
                    /*case ResponseBag.NEWS:
                        newArticleEditor.setSelectedDate(d);
                        break;*/
                    case ResponseBag.DAILY_THOUGHTS:
                        dailyThoughtEditor.setSelectedDate(d);
                        break;
                    /*case ResponseBag.WEEKLY_MESSAGE:
                        weeklyMessageEditor.setSelectedDate(d);
                        break;
                    case ResponseBag.WEEKLY_MASTERCLASS:
                        weeklyMasterclassEditor.setSelectedDate(d);
                        break;*/
                }
            }
        }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @Override
    public void onVideosSelected() {
        Intent intent = new Intent(ctx, VideoSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUsersSelected() {
        startUserBottomSheet(null, Constants.NEW_ENTITY);
    }

    @Override
    public void onPodcastsSelected() {
        Intent intent = new Intent(CompanyMainActivity.this, PodcastSelectionActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onUsersTapped(UserDTO user) {

    }

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

    private void setMenuDestination() {

        Menu menu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawers();
                if (item.getItemId() == R.id.nav_company_profile) {
                    mPager.setCurrentItem(0, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_news_article) {
                    mPager.setCurrentItem(1, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_daily_thought) {
                    mPager.setCurrentItem(2, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_master) {
                    mPager.setCurrentItem(3, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_weekly) {
                    mPager.setCurrentItem(4, true);
                    return true;
                }
                /*if (item.getItemId() == R.id.nav_calender_events) {
                    mPager.setCurrentItem(5, true);
                    return true;
                }*/
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
                if (item.getItemId() == R.id.nav_users) {
                    mPager.setCurrentItem(7, true);
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
                    Intent intent = new Intent(CompanyMainActivity.this, CompanySigninActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void onInviteClicked() {
        Log.d(TAG, "****onInviteClicked****");
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

   /* private void startUserBottomSheet(final UserDTO user, int type) {

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
              //  setFragment();
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
                showSnackbar(message, "bad", Constants.RED);
            }
        });
        myBottomSheet.show(getSupportFragmentManager(), "SHEET_USER");

    }

    private void startDailyThoughtBottomSheet(final DailyThoughtDTO thought, int type) {

        dailyThoughtEditor = DailyThoughtEditor.newInstance(thought, type);
        dailyThoughtEditor.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                DailyThoughtDTO m = (DailyThoughtDTO) entity;
                if (bag.getDailyThoughts() == null) {
                    bag.setDailyThoughts(new ArrayList<DailyThoughtDTO>());
                }
                bag.getDailyThoughts().add(0, m);
               // setFragment();
                showSnackbar(m.getTitle().concat(" is being added/updated"), getString(R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                getDate(ResponseBag.DAILY_THOUGHTS);
            }

            @Override
            public void onError(String message) {
                showSnackbar(message, "bad", Constants.RED);
            }
        });

        dailyThoughtEditor.show(getSupportFragmentManager(), "SHEET_DAILY_THOUGHT");

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
                }
            }
        }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.company_drawer, menu);
        return true;
    }

    static boolean logOff;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

         int id = item.getItemId();

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
        }*/

        if (id == R.id.action_settings){
            Intent w = new Intent(CompanyMainActivity.this, ThemeSelectorActivity.class);
            w.putExtra("darkColor", themeDarkColor);
            startActivityForResult(w, THEME_REQUESTED);
            return true;
        }
        return true;
    }
    Snackbar snackbar;
    static final int THEME_REQUESTED = 8075;
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

    static final String TAG = CompanyMainActivity.class.getSimpleName();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            Intent intent = new Intent(CompanyMainActivity.this, PodcastActivity.class);
            startActivity(intent);
            return true;

        } /*else if (id == R.id.nav_video) {
            Intent intent = new Intent(SubscriberMainActivity.this, VideoActivity.class);
            startActivity(intent);
            return true;

        }*/
        else if (id == R.id.nav_eBooks) {
            Intent intent = new Intent(CompanyMainActivity.this, eBookActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.nav_users) {
            Intent intent = new Intent(CompanyMainActivity.this, UserActivity.class);
            startActivity(intent);
            return true;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        /*return false;*/
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
    public void onUserUpdated(UserDTO user) {

    }

    @Override
    public void onDailyThoughtUpdated(DailyThoughtDTO dailyThought) {

    }

    @Override
    public void onWeeklyMasterClassUpdated(WeeklyMasterClassDTO masterClass) {

    }

    @Override
    public void onWeeklyMessageUpdated(WeeklyMessageDTO weeklyMessage) {

    }

    @Override
    public void onSubscriptionUpdated(SubscriptionDTO subscription) {

    }

    @Override
    public void onNewsUpdated(NewsDTO news) {

    }

    @Override
    public void onUserDeleted(UserDTO user) {

    }

    @Override
    public void onSubscriptionDeleted(SubscriptionDTO subscription) {

    }

    @Override
    public void onDailyThoughtDeleted(DailyThoughtDTO dailyThought) {

    }

    @Override
    public void onWeeklyMessageDeleted(WeeklyMessageDTO weeklyMessage) {

    }

    @Override
    public void onWeeklyMasterClassDeleted(WeeklyMasterClassDTO masterClass) {

    }

    @Override
    public void onVideoDeleted(VideoDTO video) {

    }

    @Override
    public void onPodcastDeleted(PodcastDTO podcast) {

    }

    @Override
    public void onNewsDeleted(NewsDTO news) {

    }

    @Override
    public void onPhotoDeleted(PhotoDTO photo) {

    }

    @Override
    public void onEbookDeleted(EBookDTO eBook) {

    }

    @Override
    public void onCategoryDeleted(CategoryDTO category) {

    }

    @Override
    public void onUserFound(UserDTO user) {

    }

    @Override
    public void onAllRatings(List<RatingDTO> list) {

    }

    @Override
    public void onDailyThoughtRatings(List<RatingDTO> list) {

    }

    @Override
    public void onWeeklyMessageRatings(List<RatingDTO> list) {

    }

    @Override
    public void onWeeklyMasterClassRatings(List<RatingDTO> list) {

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
    public void onError(String message) {

    }

    @Override
    public void onCategoryUpdated(CategoryDTO category) {

    }

    @Override
    public void onWeeklyMasterClassTapped(WeeklyMasterClassDTO weeklyMasterClass) {

    }

    @Override
    public void onMessageTapped(WeeklyMessageDTO weeklyMessage) {

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
    public void onNewsArticleTapped(NewsDTO article) {

    }

    @Override
    public void onDailyThoughtTapped(DailyThoughtDTO dailyThought) {

    }

    @Override
    public void onEBookTapped(EBookDTO eBook) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "### onActivityResult requestCode: " + requestCode + " result: " + resultCode);
        switch(requestCode) {
            case THEME_REQUESTED:
                if (resultCode == RESULT_OK){
                    finish();
                    Intent intent = new Intent(this, CompanyMainActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }
}
