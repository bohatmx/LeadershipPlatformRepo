package com.oneconnect.leadership.library.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.CreateDailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.MyDailyThoughtAdapter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.camera.CameraActivity;
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
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.editors.DailyThoughtEditor;
import com.oneconnect.leadership.library.links.LinksActivity;
import com.oneconnect.leadership.library.lists.BaseListingFragment;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.oneconnect.leadership.library.ebook.EbookListFragment.REQUEST_LINKS;

public class CreateDailyThoughtActivity extends AppCompatActivity implements CrudContract.View, CacheContract.View,
        CreateDailyThoughtAdapter.CreateThoughtListener {

    CrudPresenter presenter;
    private CachePresenter cachePresenter;
    private Context ctx;
    ResponseBag bag;
    private UserDTO user;
    private int type;
    EntityListFragment entityListFragment;
    RecyclerView recyclerView;
    ImageView iconAdd;
    TextView txtCount, txtTitle;
    //CreateDailyThoughtAdapter adapter;
    MyDailyThoughtAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "*** onCreate ***");
        setContentView(R.layout.activity_create_daily_thought);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();

        if (getIntent().getSerializableExtra("user") != null) {
            user = (UserDTO) getIntent().getSerializableExtra("user");

        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        iconAdd = (ImageView) findViewById(R.id.iconAdd);
        txtCount = (TextView) findViewById(R.id.txtCount);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
              /*  Util.flashOnce(iconAdd, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                    startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
                    }
                });*/
            }
        });


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
                    cachePresenter.getCacheVideos();
                    break;
                case ResponseBag.PAYMENTS:
                    break;
            }
          //  startPhotoService();
         //   startVideoService();
       // entityListFragment = (EntityListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        user = SharedPrefUtil.getUser(this);
    }
    }
    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment");
        SharedPrefUtil.saveFragmentType(bag.getType(), this);
        type = bag.getType();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        entityListFragment = EntityListFragment.newInstance(bag);
        //   entityListFragment.setListener(this);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    static final String TAG = CreateDailyThoughtActivity.class.getSimpleName();
    private DailyThoughtEditor dailyThoughtEditor;

    @Override
    public void onAddEntity() {
        Log.w(TAG, "onAddEntity: ........open bottom appropriate sheet");
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
                break;
        }
    }

    private void startDailyThoughtBottomSheet(final DailyThoughtDTO thought, int type) {

        dailyThoughtEditor = DailyThoughtEditor.newInstance(thought, type);
        dailyThoughtEditor.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                DailyThoughtDTO m = (DailyThoughtDTO) entity;
                /*if (bag.getDailyThoughts() == null) {
                    bag.setDailyThoughts(new ArrayList<DailyThoughtDTO>());
                }
                bag.getDailyThoughts().add(0, m);*/
                // setFragment();
                showSnackbar(m.getTitle().concat(" is being added"), getString(R
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



    private DatePickerDialog datePickerDialog;
    private void getDate(final int sheetType) {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(ctx/*this*/, new DatePickerDialog.OnDateSetListener() {
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
    public void onDeleteUser(UserDTO user) {

    }
    private CrudPresenter presenterCrud;
    @Override
    public void onDeleteDailyThought(DailyThoughtDTO dailyThought) {
        Log.i(TAG, "onDeleteDailyThought");
        presenterCrud.deleteDailyThought(dailyThought);
    }

    @Override
    public void onDeleteWeeklyMessage(WeeklyMessageDTO weeklyMessage) {

    }

    @Override
    public void onDeleteWeeklyMasterClass(WeeklyMasterClassDTO masterClass) {

    }

    @Override
    public void onDeletePodcast(PodcastDTO podcast) {

    }

    @Override
    public void onDeleteNews(NewsDTO news) {

    }

    @Override
    public void onDeleteVideo(VideoDTO video) {

    }

    @Override
    public void onDeleteEbook(EBookDTO eBook) {

    }

    @Override
    public void onDeleteCategory(CategoryDTO category) {

    }

    @Override
    public void onDeleteSubscription(SubscriptionDTO subscription) {

    }
    boolean isTooltip;
    private DailyThoughtDTO dailyThought;

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
    public void onCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onCompanies(List<CompanyDTO> list) {

    }

    @Override
    public void onDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "onDailyThoughts: " + list.size());
        adapter = new MyDailyThoughtAdapter(ctx, list, new MyDailyThoughtAdapter.MyDailyThoughtAdapterlistener() {
            @Override
            public void onPhotoRequired(BaseDTO base) {

            }

            @Override
            public void onPodcastRequired(BaseDTO base) {

            }

            @Override
            public void onVideoRequired(BaseDTO base) {

            }

            @Override
            public void onLinkRequired(BaseDTO base) {

            }

            @Override
            public void onDeleteDailyThought(DailyThoughtDTO dailyThought) {

            }


        });

        recyclerView.setAdapter(adapter);
       /* bag = new ResponseBag();
        bag.setDailyThoughts(list);
        Collections.sort(bag.getDailyThoughts());
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
        cachePresenter.cacheDailyThoughts(list);*/
    }

    @Override
    public void onPendingDailyThoughts(List<DailyThoughtDTO> list) {

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
    public void onUserFound(UserDTO user) {

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
        Log.w(TAG, "onLinksRequired: ..............");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        Intent m = null;
        switch (type) {

            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                m = new Intent(this, LinksActivity.class);
                m.putExtra("dailyThought", dailyThought);
                break;

        }
        m.putExtra("type", type);
        startActivityForResult(m, REQUEST_LINKS);
    }

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
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                m.putExtra("dailyThought", dailyThought);
                Log.d(TAG, "startCamera: ".concat(GSON.toJson(dailyThought)));
                break;
        }

        startActivityForResult(m, CameraActivity.CAMERA_REQUEST);
    }

    private void startPhotoGallerySelection(BaseDTO base){
        Intent intent = new Intent(this, PhotoSelectionActivity.class);
        intent.putExtra("type", type);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                intent.putExtra("dailyThought", dailyThought);
                break;


        }
        startActivity(intent);
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
                })
                .setNegativeButton("Pick from Device", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startVideoSelection(base);
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
    private void startVideoSelection(BaseDTO base) {
        Intent m = new Intent(this, VideoSelectionActivity.class);
        m.putExtra("type", type);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                m.putExtra("dailyThought", dailyThought);
                break;
        }

        startActivity(m);
    }
    @Override
    public void onSomeActionRequired(BaseDTO entity) {

    }
    private Snackbar snackbar;
    private Toolbar toolbar;
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
    private void startPodcastSelection(BaseDTO base){
        Intent intent = new Intent(this, PodcastSelectionActivity.class);
        intent.putExtra("type", type);
        switch (type) {

            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                intent.putExtra("dailyThought", dailyThought);
                break;

        }
        startActivity(intent);
    }
    @Override
    public void onMicrophoneRequired(BaseDTO entity) {
        Log.w(TAG, "onMicrophoneRequired: ,,,,,,,,,,,,,,,,,,,,");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        showSnackbar("Audio recording under construction", "OK", "cyan");
        startPodcastSelection(entity);

    }

    @Override
    public void onEntityClicked(BaseDTO entity) {
        Log.w(TAG, "onEntityClicked: .......".concat(GSON.toJson(entity)));
    }

    @Override
    public void onCalendarRequested(BaseDTO entity) {

    }

    @Override
    public void onEntityDetailRequested(BaseDTO entity, int type) {

    }

    @Override
    public void onDeleteTooltipRequired(int type) {
        isTooltip = true;
        Toasty.warning(this, "Remove this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onLinksTooltipRequired(int type) {
        isTooltip = true;
        Toasty.info(this, "Add internet links to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onPhotoCaptureTooltipRequired(int type) {
        isTooltip = true;
        Toasty.warning(this, "Add photos to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onVideoCaptureTooltipRequired(int type) {
        isTooltip = true;
        Toasty.info(this, "Add videos to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onSomeActionTooltipRequired(int type) {

    }

    @Override
    public void onMicrophoneTooltipRequired(int type) {
        isTooltip = true;
        Toasty.success(this, "Add audio recording to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onCalendarTooltipRequired(int type) {

    }

    @Override
    public void onNewsArticleRequested(BaseDTO entity) {

    }

    @Override
    public void onUpdateUser(UserDTO user) {

    }

    @Override
    public void onUpdateDailyThought(DailyThoughtDTO dailyThought) {
       /* Intent intent = new Intent(this, UpdateEntityActivity.class);
        intent.putExtra("dailyThought", dailyThought);
        startActivity(intent);*/
    }

    @Override
    public void onUpdateWeeklyMessage(WeeklyMessageDTO weeklyMessage) {

    }

    @Override
    public void onUpdateWeeklyMasterClass(WeeklyMasterClassDTO masterClass) {

    }

    @Override
    public void onUpdateNews(NewsDTO news) {

    }

    @Override
    public void onUpdateCategory(CategoryDTO category) {

    }

    @Override
    public void onUpdateSubscription(SubscriptionDTO subscription) {

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
}
