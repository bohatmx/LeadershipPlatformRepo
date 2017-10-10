package com.oneconnect.leadership.library.lists;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.DailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.MiniPhotoAdapter;
import com.oneconnect.leadership.library.adapters.MyDailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.PhotoAdapter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.DailyThoughtCache;
import com.oneconnect.leadership.library.camera.CameraActivity;
import com.oneconnect.leadership.library.camera.VideoSelectionActivity;
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
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.editors.DailyThoughtEditor;
import com.oneconnect.leadership.library.links.LinksActivity;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyDailyThoughtList extends Fragment implements PageFragment, SubscriberContract.View, CacheContract.View,
        BasicEntityAdapter.EntityListener{


    private MyDailyThoughtListener mListener;
    private RecyclerView recyclerView, photoRecyclerView;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private ResponseBag  bag;
    private EntityListFragment entityListFragment;


    public MyDailyThoughtList() {
        // Required empty public constructor
    }

    public static MyDailyThoughtList newInstance() {
        MyDailyThoughtList fragment = new MyDailyThoughtList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private int type;
    private DailyThoughtDTO dailyThought;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bag = (ResponseBag) getArguments().getSerializable("bag");
            dailyThought = (DailyThoughtDTO) getArguments().getSerializable("dailyThought");
            type = getArguments().getInt("type", 0);

            presenter = new SubscriberPresenter(this);
            cachePresenter = new CachePresenter(this, ctx);

            user = SharedPrefUtil.getUser(ctx);
            type = SharedPrefUtil.getFragmentType(ctx);
        }
    }

    private View view;
    private Context ctx;

    MyDailyThoughtAdapter adapter;
    private List<DailyThoughtDTO> dailyThoughtList = new ArrayList<>();


    CategoryDTO category;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_daily_thought_list, container, false);
        presenter = new SubscriberPresenter(this);
        ctx = getActivity();
        if (getActivity().getIntent().getSerializableExtra("category") != null) {
            category = (CategoryDTO) getActivity().getIntent().getSerializableExtra("category");
            Log.i(LOG, "category: " + category.getCategoryName());
        } else {
            Log.i(LOG, "No Category");
        }

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        photoRecyclerView = (RecyclerView) view.findViewById(R.id.photoRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        photoRecyclerView.setLayoutManager(llm2);
        photoRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        photoRecyclerView.setHasFixedSize(true);


        getCachedDailyThoughts();
        getDailyThoughts();

        return view;
    }
    private  FirebaseAuth firebaseAuth;
    private UserDTO user = SharedPrefUtil.getUser(ctx);

    public void getDailyThoughts() {
        Log.d(LOG, "************** getMyDailyThoughts: " );
        if(user == null){
            presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
        }
        else{
            presenter.getDailyThoughtsByUser(user.getUserID());
        }

    }

    private void getCachedDailyThoughts() {
        DailyThoughtCache.getDailyThoughts(ctx, new DailyThoughtCache.ReadListener() {
            @Override
            public void onDataRead(List<DailyThoughtDTO> dailyThoughts) {
                Log.d(LOG, "onDataRead: DailyThoughts: " + dailyThoughts);
            }

            @Override
            public void onError(String message) {

                getCachedDailyThoughts();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyDailyThoughtListener) {
            mListener = (MyDailyThoughtListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MyDailyThoughtListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    String pageTitle;

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    static final String LOG = MyDailyThoughtList.class.getSimpleName();

    @Override
    public String getTitle() {
        return pageTitle;
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
     presenter.getDailyThoughtsByUser(user.getUserID());
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
        Log.i(LOG, "onDailythoughtsByUser: " + list.size());
        this.dailyThoughtList = list;
       // list = getCategoryList(list, category.getCategoryName());
        Collections.sort(list);

        adapter = new MyDailyThoughtAdapter(ctx, list, new MyDailyThoughtAdapter.MyDailyThoughtAdapterlistener() {

            @Override
            public void onLinksRequired(BaseDTO entity) {
                Log.w(TAG, "onLinksRequired: ..............");
                if (isTooltip) {
                    isTooltip = false;
                    return;
                }
                Intent m = null;
                type = ResponseBag.DAILY_THOUGHTS;
                dailyThought = (DailyThoughtDTO) entity;
                m = new Intent(ctx, LinksActivity.class);
                m.putExtra("dailyThought", dailyThought);
                startActivity(m);
            }

            @Override
            public void onAddEntity() {
                Log.w(TAG, "onAddEntity: ........open bottom appropriate sheet");
                switch (type) {

                    case ResponseBag.DAILY_THOUGHTS:
                        startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
                        break;

                }
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
            public void onPictureRequired(BaseDTO entity) {
                Log.w(TAG, "onPhotoCaptureRequested: .................");
                if (isTooltip) {
                    isTooltip = false;
                    return;
                }
                pickGalleryOrCamera(entity);
            }

            @Override
            public void onVideoRequired(BaseDTO entity) {
                Log.w(TAG, "onVideoCaptureRequested: .................");
                if (isTooltip) {
                    isTooltip = false;
                    return;
                }
                pickGalleryOrVideoCamera(entity);
            }
        });
        recyclerView.setAdapter(adapter);

    }
    private void pickGalleryOrVideoCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(ctx);
        b.setTitle("Select Video")
                .setMessage("Please select the source of the video")
                .setPositiveButton("Use Video Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startVideo(base);
                    }
                })
                .setNegativeButton("Pick Video from Device", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startVideoSelection(base);
                    }
                }).show();
    }

    private void startVideoSelection(BaseDTO base) {
        Intent m = new Intent(ctx, VideoSelectionActivity.class);
        type = ResponseBag.DAILY_THOUGHTS;
        dailyThought = (DailyThoughtDTO) base;
        m.putExtra("dailyThought", dailyThought);
        startActivity(m);
    }

    private void startVideo(BaseDTO entity) {

        Intent m = new Intent(ctx, CameraActivity.class);
        m.putExtra("type", CameraActivity.VIDEO_REQUEST);
        switch (type) {

            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                m.putExtra("dailyThought", dailyThought);
                break;
        }

        startActivityForResult(m, CameraActivity.VIDEO_REQUEST);
    }

    @Override
    public void onCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onCompanies(List<CompanyDTO> list) {

    }

    @Override
    public void onDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(LOG, "onDailyThoughts: " + list.size());
        this.dailyThoughtList = list;
        list = getCategoryList(list, category.getCategoryName());
        Collections.sort(list);
        adapter = new MyDailyThoughtAdapter(ctx, list, new MyDailyThoughtAdapter.MyDailyThoughtAdapterlistener() {

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
                        m = new Intent(ctx, LinksActivity.class);
                        m.putExtra("dailyThought", dailyThought);
                        break;

                }
                m.putExtra("type", type);
                startActivityForResult(m, REQUEST_LINKS);
            }

            @Override
            public void onAddEntity() {
                Log.w(TAG, "onAddEntity: ........open bottom appropriate sheet");
                switch (type) {

                    case ResponseBag.DAILY_THOUGHTS:
                        startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
                        break;

                }
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
            public void onPictureRequired(BaseDTO entity) {
                Log.w(TAG, "onPhotoCaptureRequested: .................");
                if (isTooltip) {
                    isTooltip = false;
                    return;
                }
                pickGalleryOrCamera(entity);
            }

            @Override
            public void onVideoRequired(BaseDTO entity) {
                Log.w(TAG, "onVideoCaptureRequested: .................");
                if (isTooltip) {
                    isTooltip = false;
                    return;
                }
                pickGalleryOrVideoCamera(entity);
            }
        });
        recyclerView.setAdapter(adapter);

    }
    boolean isTooltip;
    MiniPhotoAdapter miniPhotoAdapter;
    public static final int REQUEST_LINKS = 1875;
    public static final String TAG = MyDailyThoughtList.class.getSimpleName();
    @Override
    public void onAllCompanyDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(LOG, "onAllCompanyDailyThoughts: " + list.size());
        this.dailyThoughtList = list;
        list = getCategoryList(list, category.getCategoryName());
        Collections.sort(list);
        adapter = new MyDailyThoughtAdapter(ctx, list, new MyDailyThoughtAdapter.MyDailyThoughtAdapterlistener() {


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
                        m = new Intent(ctx, LinksActivity.class);
                        m.putExtra("dailyThought", dailyThought);
                        break;

                }
                m.putExtra("type", type);
                startActivityForResult(m, REQUEST_LINKS);
            }

            @Override
            public void onAddEntity() {
                Log.w(TAG, "onAddEntity: ........open bottom appropriate sheet");
                switch (type) {

                    case ResponseBag.DAILY_THOUGHTS:
                        startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
                        break;

                }
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
            public void onPictureRequired(BaseDTO entity) {
                Log.w(TAG, "onPhotoCaptureRequested: .................");
                if (isTooltip) {
                    isTooltip = false;
                    return;
                }
                pickGalleryOrCamera(entity);
            }

            @Override
            public void onVideoRequired(BaseDTO entity) {
                Log.w(TAG, "onVideoCaptureRequested: .................");
                if (isTooltip) {
                    isTooltip = false;
                    return;
                }
                pickGalleryOrVideoCamera(entity);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private Toolbar toolbar;
    private Snackbar snackbar;

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(recyclerView, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private DatePickerDialog datePickerDialog;
    private DailyThoughtEditor dailyThoughtEditor;

    private void getDate(final int sheetType) {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
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

    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment");
        SharedPrefUtil.saveFragmentType(bag.getType(), ctx);
        type = bag.getType();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        entityListFragment = EntityListFragment.newInstance(bag);
        entityListFragment.setListener(this);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
        // ft.commitAllowingStateLoss();
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
                showSnackbar(message, "bad", Constants.RED);
            }
        });

        dailyThoughtEditor.show(getFragmentManager(), "SHEET_DAILY_THOUGHT");

    }
    private void startPodcastSelection(BaseDTO base){
        Intent intent = new Intent(ctx, PodcastSelectionActivity.class);
        intent.putExtra("type", type);
        switch (type) {

            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                intent.putExtra("dailyThought", dailyThought);
                break;

        }
        startActivity(intent);
    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private void startCamera(BaseDTO entity) {

        Intent m = new Intent(ctx, CameraActivity.class);
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
    private void pickGalleryOrCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(ctx);
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
    private void startPhotoGallerySelection(BaseDTO base){

        Intent intent = new Intent(ctx, PhotoSelectionActivity.class);
        type = ResponseBag.DAILY_THOUGHTS;
        dailyThought = (DailyThoughtDTO) base;
        intent.putExtra("dailyThought", dailyThought);
        startActivity(intent);

    }
    private List<DailyThoughtDTO> getCategoryList(List<DailyThoughtDTO> list, String typeName){
        //DailyThoughtDTO dailyThoughtDTO = null;
        List<DailyThoughtDTO> returnList = new ArrayList<>();
        for(DailyThoughtDTO dailyThoughtDTO : list){
            if (dailyThoughtDTO.getCategory().getCategoryName() != null){
                if(dailyThoughtDTO.getCategory().getCategoryName().equals(typeName)){
                    returnList.add(dailyThoughtDTO);
                }
            }
        }
        return returnList;

    }
    @Override
    public void onAllDailyThoughts(List<DailyThoughtDTO> list) {
        Log.w(LOG, "onAllDailyThoughts: " + list.size());
        this.dailyThoughtList = list;
        if (category != null) {
            list = getCategoryList(list, category.getCategoryName());
        }
        Collections.sort(list);
        adapter = new MyDailyThoughtAdapter(ctx, list, new MyDailyThoughtAdapter.MyDailyThoughtAdapterlistener() {


            @Override
            public void onLinksRequired(BaseDTO entity) {

            }

            @Override
            public void onAddEntity() {

            }

            @Override
            public void onMicrophoneRequired(BaseDTO entity) {

            }

            @Override
            public void onPictureRequired(BaseDTO entity) {

            }

            @Override
            public void onVideoRequired(BaseDTO entity) {

            }
        });
        recyclerView.setAdapter(adapter);
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
        Log.i(LOG, "onCacheDailyThoughts " + list.size());

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

    }

    @Override
    public void onAddEntity() {

    }

    @Override
    public void onDeleteClicked(BaseDTO entity) {

    }

    @Override
    public void onDeleteUser(UserDTO user) {

    }

    @Override
    public void onDeleteDailyThought(DailyThoughtDTO dailyThought) {

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
        isTooltip = true;
        Toasty.info(ctx, "Add internet links to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onPhotoCaptureTooltipRequired(int type) {
        isTooltip = true;
        Toasty.warning(ctx, "Add photos to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onVideoCaptureTooltipRequired(int type) {
        isTooltip = true;
        Toasty.info(ctx, "Add videos to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onSomeActionTooltipRequired(int type) {

    }

    @Override
    public void onMicrophoneTooltipRequired(int type) {
        isTooltip = true;
        Toasty.success(ctx, "Add audio recording to this record",
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


    public interface MyDailyThoughtListener {
        void onDailyThoughtTapped(DailyThoughtDTO dailyThought);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

}
