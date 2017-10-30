package com.ocg.leadership.company;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.camera.CameraActivity;
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
import com.oneconnect.leadership.library.lists.CalendarEventListFragment;
import com.oneconnect.leadership.library.lists.CompanyMainFragment;
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.EBookListFragment;
import com.oneconnect.leadership.library.lists.HarmonyListFragment;
import com.oneconnect.leadership.library.lists.MasterListFragment;
import com.oneconnect.leadership.library.lists.MyDailyThoughtList;
import com.oneconnect.leadership.library.lists.NewsListFragment;
import com.oneconnect.leadership.library.lists.PageFragment;
import com.oneconnect.leadership.library.lists.PhotoListFragment;
import com.oneconnect.leadership.library.lists.PodcastListFragment;
import com.oneconnect.leadership.library.lists.TopLeaderListFragment;
import com.oneconnect.leadership.library.lists.UserListFragment;
import com.oneconnect.leadership.library.lists.VideoListFragment;
import com.oneconnect.leadership.library.lists.WeeklyMessageListFragment;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.util.Base64;
import com.oneconnect.leadership.library.util.DepthPageTransformer;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.WHITE;

public class StandardUserActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener,
        SubscriberContract.View, CrudContract.View, CacheContract.View, MasterListFragment.WeeklyMasterClassListener,
        TopLeaderListFragment.TopLeaderAdapterlistener, PodcastListFragment.PodcastListener, VideoListFragment.VideoListener,
        PhotoListFragment.PhotoListener, EBookListFragment.EBookListener, DailyThoughtListFragment.DailyThoughtListener,
        NewsListFragment.NewsArticleListener, HarmonyListFragment.HarmonyThoughtAdapterlistener {

    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private PodcastDTO podcast;
    private DailyThoughtListFragment dailyThoughtListFragment;
    private EBookListFragment eBookListFragment;
    private PodcastListFragment podcastListFragment;
    private HarmonyListFragment harmonyListFragment;
    private TopLeaderListFragment topLeaderListFragment;
    MasterListFragment masterListFragment;
    WeeklyMessageListFragment weeklyMessageListFragment;
    MyDailyThoughtList myDailyThoughtList;
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
    private ImageView imageView;
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
    NavigationView standard_nav_view;
    private PagerAdapter adapter;
    private StandardUserActivity activity;
    private ActionBar ab;
    RelativeLayout nav_layout;
    TextView userName, userEmail, companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        setContentView(R.layout.activity_standard_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        logoIMG = (ImageView) findViewById(R.id.logoIMG);
        firebaseAuth = firebaseAuth.getInstance();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ctx = getApplicationContext();
        activity = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
    }

    private void setup() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        user = SharedPrefUtil.getUser(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        standard_nav_view = (NavigationView) findViewById(R.id.standard_nav_view);
        standard_nav_view.setNavigationItemSelectedListener(this);
        standard_nav_view.setVisibility(View.VISIBLE);
        final View header = standard_nav_view.getHeaderView(0);

        userName = header.findViewById(R.id.owner_name);
        userEmail = header.findViewById(R.id.owner_email);
        nav_layout = header.findViewById(R.id.nav_layout);
        imageView = header.findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickGalleryOrCamera(user);
            }
        });

        companyName = (TextView) findViewById(R.id.companyName);


        userName.setText(user.getFullName());
        userEmail.setText(user.getEmail());

        setUpStandardUserViewPager();
    }

    private void setUpStandardUserViewPager() {
        setStandardMenuDestination();

        pageFragmentList = new ArrayList<>();

        newsListFragment = NewsListFragment.newInstance();
        dailyThoughtListFragment = DailyThoughtListFragment.newInstance();
        masterListFragment = MasterListFragment.newInstance();
        harmonyListFragment = harmonyListFragment.newInstance();
        topLeaderListFragment = topLeaderListFragment.newInstance();
        podcastListFragment = PodcastListFragment.newInstance(new HashMap<String, PodcastDTO>());
        videoListFragment = VideoListFragment.newInstance(new HashMap<String, VideoDTO>());
        eBookListFragment = EBookListFragment.newInstance(new HashMap<String, EBookDTO>());

        newsListFragment.setPageTitle(ctx.getString(R.string.news_article));
        harmonyListFragment.setPageTitle(ctx.getString(R.string.hormony_list));
        dailyThoughtListFragment.setPageTitle(ctx.getString(R.string.daily_thought));
        masterListFragment.setPageTitle(ctx.getString(R.string.weeky_master_class));
        topLeaderListFragment.setPageTitle(ctx.getString(R.string.weekly_message));
        podcastListFragment.setPageTitle(ctx.getString(R.string.podcast));
        videoListFragment.setPageTitle(ctx.getString(R.string.video));
        eBookListFragment.setPageTitle(ctx.getString(R.string.ebooks));

        dailyThoughtListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        harmonyListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        newsListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        masterListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        topLeaderListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        podcastListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        eBookListFragment.setThemeColors(themePrimaryColor, themeDarkColor);
        videoListFragment.setThemeColors(themePrimaryColor, themeDarkColor);

        pageFragmentList.add(dailyThoughtListFragment);
        pageFragmentList.add(harmonyListFragment);
        pageFragmentList.add(newsListFragment);
        pageFragmentList.add(masterListFragment);
        pageFragmentList.add(topLeaderListFragment);
        pageFragmentList.add(podcastListFragment);
        pageFragmentList.add(videoListFragment);
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
            Log.e(TAG, "PagerAdapter failed", e);
            if (page != null) {
                if (page.equalsIgnoreCase("Leadership Daily Thoughts")) {
                    mPager.setCurrentItem(0);
                }
                if (page.equalsIgnoreCase("Company Thoughts")) {
                    mPager.setCurrentItem(1);
                }

                if (page.equalsIgnoreCase("Leadership News Article")) {
                    mPager.setCurrentItem(2);
                }

                if (page.equalsIgnoreCase("Leadership Weekly Masterclass")) {
                    mPager.setCurrentItem(3);
                }
                if (page.equalsIgnoreCase("Top Leader Thoughts")) {
                    mPager.setCurrentItem(4);
                }
                if (page.equalsIgnoreCase("Leadership Podcasts")) {
                    mPager.setCurrentItem(5);
                }
                if (page.equalsIgnoreCase("Leadership Videos")) {
                    mPager.setCurrentItem(6);
                }
                if (page.equalsIgnoreCase("Leadership eBooks")) {
                    mPager.setCurrentItem(7);
                }

            }
        }
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
                Intent intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // startActivityForResult(intent, RESULT_LOAD_IMG);
                startPhotoGallerySelection(base);

            }
        }).show();
    }

    private void startCamera(BaseDTO entity) {

        Intent m = new Intent(this, CameraActivity.class);
        m.putExtra("type", CameraActivity.CAMERA_REQUEST);
        switch (type) {
            case ResponseBag.USERS:
                user = (UserDTO) entity;
                m.putExtra("user", user);
                Log.d(TAG, "startCamera: ".concat(GSON.toJson(user)));
                break;
        }

        startActivityForResult(m, CameraActivity.CAMERA_REQUEST);
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private void startPhotoGallerySelection(BaseDTO base){
        Intent intent = new Intent(this, PhotoSelectionActivity.class);

        user = (UserDTO) base;
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void setStandardMenuDestination() {
        Menu menu = standard_nav_view.getMenu();


        standard_nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawers();
                if (item.getItemId() == R.id.nav_daily_thought) {
                    mPager.setCurrentItem(0, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_harmony_thought) {
                    mPager.setCurrentItem(1, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_news_article) {
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
                if (item.getItemId() == R.id.nav_podcast) {
                    mPager.setCurrentItem(5, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_video) {
                    mPager.setCurrentItem(6, true);
                    return true;
                }
                if (item.getItemId() == R.id.nav_eBooks) {
                    mPager.setCurrentItem(7, true);
                    return true;
                }

                if (item.getItemId() == R.id.nav_sign_out) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(StandardUserActivity.this, CompanySigninActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return true;
                /// return false;
            }
        });
    }


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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
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
    public void onUserFound(UserDTO user) {
        Log.i(TAG, "*** onUserFound ***" + user.getFullName());
        if (user.getPhotos() != null) {
            List<PhotoDTO> urlList = new ArrayList<>();
            Map map = user.getPhotos();
            PhotoDTO vDTO;
            String photoUrl;
            for (Object value : map.values()) {
                vDTO = (PhotoDTO) value;
                photoUrl = vDTO.getUrl();
                urlList.add(vDTO);

                Glide.with(ctx)
                        .load(photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        }
        presenter.getCompanyProfile(user.getCompanyID());
    }
    String hexColor;
    ImageView logoIMG;

    @Override
    public void onCompanyFound(CompanyDTO company) {
        Log.i(TAG, "*** onCompanyFound ***" + company.getCompanyName());
        logoIMG.setVisibility(View.GONE);
        if(company.getCompanyName() != null) {
            companyName.setText(company.getCompanyName());
        } else {
            companyName.setVisibility(View.GONE);
        }
        // companyName.setText(company.getCompanyName());

        if (company.getPrimaryColor() != 0) {
            Log.i(TAG, "*** converting primary color to a hex color ***");
            hexColor = String.format("#%06X", (0xFFFFFF & company.getPrimaryColor()));

            toolbar.setBackgroundColor(Color.parseColor(hexColor));
            strip.setBackgroundColor(Color.parseColor(hexColor));
            nav_layout.setBackgroundColor(Color.parseColor(hexColor));
        }
        if (company.getSecondaryColor() != 0) {
            Log.i(TAG, "*** converting primary color to a hex color ***");
            hexColor = String.format("#%06X", (0xFFFFFF & company.getSecondaryColor()));
            strip.setUnderlineColor(Color.parseColor(hexColor));
            strip.setIndicatorColor(Color.parseColor(hexColor));
            strip.setDividerColor(Color.parseColor(hexColor));
        } else {
            strip.setUnderlineColor(Color.WHITE);
            strip.setIndicatorColor(Color.WHITE);
            strip.setDividerColor(Color.WHITE);
        }

        if (company.getPhotos() != null) {
            List<PhotoDTO> urlList = new ArrayList<>();

            Map map = company.getPhotos();
            PhotoDTO vDTO;
            String photoUrl;
            for (Object value : map.values()) {
                vDTO = (PhotoDTO) value;
                photoUrl = vDTO.getUrl();
                urlList.add(vDTO);

                getImage(photoUrl);
                // picassoLoader(this, logoIMG, photoUrl);
            }
        }
    }

    private void getImage(final String url) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmImg = null;
                try  {
                    //Your code goes here
                    try {
                        bmImg = BitmapFactory.decodeStream((Base64.InputStream)new URL(url).getContent());
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                        // e.printStackTrace();
                    }
                    BitmapDrawable background = new BitmapDrawable(bmImg);
                    nav_layout.setBackgroundDrawable(background);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    //  e.printStackTrace();
                }
            }
        });
        thread.start();
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
    public void onDailythoughtsByUser(List<DailyThoughtDTO> list) {

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
    public void onPendingDailyThoughts(List<DailyThoughtDTO> list) {

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
    public void onLinksRequired(BaseDTO entity) {

    }

    @Override
    public void onCompanyCreated(CompanyDTO company) {

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
    public void onWeeklyMasterClassTapped(WeeklyMasterClassDTO weeklyMasterClass) {

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
    public void onEBookTapped(EBookDTO eBook) {

    }

    @Override
    public void onDailyThoughtTapped(DailyThoughtDTO dailyThought) {

    }

    static final String TAG = StandardUserActivity.class.getSimpleName();
}
