package com.oneconnect.leadership.library.lists;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oneconnect.leadership.library.activities.CreatePldpActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.activities.VideoActivity;
import com.oneconnect.leadership.library.activities.VideoRecordActivity;
import com.oneconnect.leadership.library.adapters.DailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.VideosAdapter;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.audio.AudioRecordTest;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.DailyThoughtCache;
import com.oneconnect.leadership.library.cache.VideoCache;
import com.oneconnect.leadership.library.camera.CameraActivity;
import com.oneconnect.leadership.library.camera.VideoAdapter;
import com.oneconnect.leadership.library.camera.VideoSelectionActivity;
import com.oneconnect.leadership.library.camera.VideoUploadContract;
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
import com.oneconnect.leadership.library.data.PldpDTO;
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
import com.oneconnect.leadership.library.services.VideoUploaderService;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.video.LeExoPlayerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class VideoListFragment extends Fragment implements VideoAdapter.VideoAdapterListener, PageFragment, SubscriberContract.View,
        CacheContract.View, BasicEntityAdapter.EntityListener {
    private VideoListener mListener;
    public static final String TAG = VideoListFragment.class.getSimpleName();
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private ResponseBag  bag;
    private int type;
    private RecyclerView.LayoutManager mLayoutManager;
    public SearchView search;

    public VideoListFragment() {
        // Required empty public constructor
    }

    private List<VideoDTO> videos = new ArrayList<>();;
    private List<String> paths;
    private View view;
    private RecyclerView recyclerView;

    public static VideoListFragment newInstance(HashMap<String, VideoDTO> list) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        ResponseBag bag = new ResponseBag();
        bag.setVideos(new ArrayList<VideoDTO>());
        for (VideoDTO v: list.values()) {
            bag.getVideos().add(v);
        }
        args.putSerializable("bag", bag);
        fragment.setArguments(args);
        return fragment;
    }

    private VideoDTO video;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResponseBag  bag = (ResponseBag) getArguments().getSerializable("bag");
            videos= bag.getVideos();
            Log.d(LOG, "bagSize: " + bag.getVideos().size());
            video = (VideoDTO) getArguments().getSerializable("video");
            type = getArguments().getInt("type", 0);

            presenter = new SubscriberPresenter(this);
            cachePresenter = new CachePresenter(this, ctx);

            user = SharedPrefUtil.getUser(ctx);
            type = SharedPrefUtil.getFragmentType(ctx);
        }
    }

    private void pickGalleryOrVideoCamera() {
        android.support.v7.app.AlertDialog.Builder b = new android.support.v7.app.AlertDialog.Builder(ctx);
        b.setTitle("Select Video")
                .setMessage("Please select the source of the Video")
                .setPositiveButton("Use Video Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startVideo();
                    }
                })
                .setNegativeButton("Pick video from Device", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startVideoSelection();
                    }
                }).show();
    }

    private void startVideoSelection() {
        Intent intent = new Intent(ctx, VideoSelectionActivity.class);
        if (hexColor != null) {
            intent.putExtra("hexColor", hexColor);
        }
        startActivity(intent);
    }

    private void startVideo() {
        Intent intent = new Intent(ctx, VideoRecordActivity.class);
        intent.putExtra("type", VideoRecordActivity.VIDEO_REQUEST);
        /*if (hexColor != null) {
            intent.putExtra("hexColor", hexColor);
        }*/
        startActivityForResult(intent, VideoRecordActivity.VIDEO_REQUEST);
    }


    VideosAdapter adapter;
    ArrayList<String> downloadedList = new ArrayList<>();
    private UserDTO user;
    private Context ctx;
    MediaController mediaController;
    private FirebaseAuth firebaseAuth;
    FloatingActionButton fabIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: %%%%%%%%%%%%%%%%%%%%%%");
        view =  inflater.inflate(R.layout.fragment_video_list, container, false);
        presenter = new SubscriberPresenter(this);
        search = view.findViewById(R.id.search);
        ctx = getActivity();
        firebaseAuth = FirebaseAuth.getInstance();

        if (getActivity().getIntent().getSerializableExtra("type") != null) {
            type = (int) getActivity().getIntent().getSerializableExtra("type");
        }

        fabIcon = (FloatingActionButton) view.findViewById(R.id.fabIcon);
        fabIcon.setVisibility(View.GONE);
        fabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickGalleryOrVideoCamera();
            }
        });
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);

        getCachedVideos();
        getVideos();

        return view;
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();




    private void getCachedVideos() {
        VideoCache.getVideos(ctx, new VideoCache.ReadListener() {
            @Override
            public void onDataRead(List<VideoDTO> videos) {
                Log.d(LOG, "onDataRead: Videos: " + videos);
            }

            @Override
            public void onError(String message) {
                getCachedVideos();
            }
        });
    }

    private void setRecyclerView(List<VideoDTO> list) {

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        search.setOnQueryTextListener(listener);

        adapter = new VideosAdapter(list, ctx, new VideosAdapter.VideosAdapterListener() {
            @Override
            public void onPlayClicked(String path) {
                mediaController = new MediaController(ctx);
                mediaController.setAnchorView(recyclerView);
            }

            @Override
            public void onVideoRequired(VideoDTO video) {

            }

            @Override
            public void onPldpRequested(VideoDTO video) {
                Intent intent = new Intent(ctx, CreatePldpActivity.class);
                intent.putExtra("video", video);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                if (user != null) {
                    intent.putExtra("user", user);
                }
                startActivity(intent);
              //  ctx.startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<VideoDTO> filteredList = new ArrayList<>();

            for (int i = 0; i < videos.size(); i++) {

                final String text = videos.get(i).getCompanyName().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(videos.get(i));
                }

            }
            setRecyclerView(filteredList);
            return true;

        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    private void getVideos() {
        Log.d(LOG, "************** getVideos: " );
        presenter.getAllVideos();
        presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
      /*  switch (type) {
            case Constants.INTERNAL_DATA:
                if (user == null) {
                    presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
                }  else {
                    presenter.getVideos(user.getCompanyID());
                }
                break;
            case Constants.EXTERNAL_DATA:
                presenter.getAllVideos();
                break;
        }*/

    }

    private void playVideo(String path) {

        Intent m = new Intent(getContext(), LeExoPlayerActivity.class);
        //ResponseBag bag = new ResponseBag();
        //bag.setVideos(new ArrayList<VideoDTO>());
        //VideoDTO v = new VideoDTO();
        File f = new File(path);
        //v.setUrl(Uri.fromFile(f).toString());
        Uri.fromFile(f).toString();
        //bag.getVideos().add(v);
        m.putExtra("bag",bag);
        startActivity(m);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VideoListener) {
            mListener = (VideoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement VideoListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    String pageTitle;

    @Override
    public String getTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }


    static final String LOG = VideoListFragment.class.getSimpleName();

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
        Log.i(LOG, "onCacheVideos " + list.size());
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
        if (user.getUserDescription().equalsIgnoreCase(UserDTO.DESC_PLATINUM_USER)) {
            fabIcon.setVisibility(View.VISIBLE);
        }
        else if (user.getUserDescription().equalsIgnoreCase(UserDTO.DESC_STANDARD_USER)) {
            fabIcon.setVisibility(View.GONE);
        }
        else if (user.getUserDescription().equalsIgnoreCase(UserDTO.DESC_GOLD_USER)) {
            fabIcon.setVisibility(View.GONE);
        }
        else if (user.getUserDescription().equalsIgnoreCase(UserDTO.DESC_PLATINUM_ADMIN)) {
            fabIcon.setVisibility(View.GONE);
        }
        else if (user.getUserDescription().equalsIgnoreCase(UserDTO.DESC_COMPANY_ADMIN)) {
            fabIcon.setVisibility(View.GONE);
        }

        presenter.getVideos(user.getCompanyID());
        presenter.getCompanyProfile(user.getCompanyID());

    }

    String hexColor;
    @Override
    public void onCompanyFound(CompanyDTO company) {
        if (company.getPrimaryColor() != 0) {
            Log.i(LOG, "*** converting primary color to a hex color ***");
            hexColor = String.format("#%06X", (0xFFFFFF & company.getPrimaryColor()));
        }
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
    public void onPldps(List<PldpDTO> list) {

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
        Log.i(LOG, "onAllVideos: " + list.size());
        this.videos = list;
        Collections.sort(list);
        adapter = new VideosAdapter(list, ctx, new VideosAdapter.VideosAdapterListener() {
            @Override
            public void onPlayClicked(String path) {
                mediaController = new MediaController(ctx);
                 mediaController.setAnchorView(recyclerView);
               // playVideo(path);
            }

            @Override
            public void onVideoRequired(VideoDTO video) {

            }

            @Override
            public void onPldpRequested(VideoDTO video) {
                Intent intent = new Intent(ctx, CreatePldpActivity.class);
                intent.putExtra("video", video);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                if (user != null) {
                    intent.putExtra("user", user);
                }
                startActivity(intent);
               // ctx.startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
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
        Log.i(LOG, "onVideos: " + list.size());
        this.videos = list;
        Collections.sort(list);
        adapter = new VideosAdapter(list, ctx, new VideosAdapter.VideosAdapterListener() {
            @Override
            public void onPlayClicked(String path) {
                playVideo(path);
            }

            @Override
            public void onVideoRequired(VideoDTO video) {

            }

            @Override
            public void onPldpRequested(VideoDTO video) {
                Intent intent = new Intent(ctx, CreatePldpActivity.class);
                intent.putExtra("video", video);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                if (user != null) {
                    intent.putExtra("user", user);
                }
                startActivity(intent);
               // ctx.startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
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
    public void onPlayVideoTapped(String path) {

    }

    @Override
    public void onUploadVideoTapped(String path) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface VideoListener {
        void onVideoTapped(VideoDTO video);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }
}
