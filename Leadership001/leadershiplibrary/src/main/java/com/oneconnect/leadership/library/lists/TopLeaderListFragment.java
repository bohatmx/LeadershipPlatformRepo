package com.oneconnect.leadership.library.lists;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.CreatePldpActivity;
import com.oneconnect.leadership.library.activities.RatingActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.DailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.MiniPhotoAdapter;
import com.oneconnect.leadership.library.adapters.PhotoAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.DailyThoughtCache;
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
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TopLeaderListFragment extends Fragment implements PageFragment, SubscriberContract.View, CacheContract.View,
        BasicEntityAdapter.EntityListener{


    private TopLeaderListFragment.TopLeaderAdapterlistener mListener;
    private RecyclerView recyclerView, photoRecyclerView;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private ResponseBag bag;
    private EntityListFragment entityListFragment;
    private RecyclerView.LayoutManager mLayoutManager;
    public SearchView search;

    public TopLeaderListFragment() {
        // Required empty public constructor
    }

    public static TopLeaderListFragment newInstance() {
        TopLeaderListFragment fragment = new TopLeaderListFragment();
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

    DailyThoughtAdapter adapter;
    private List<DailyThoughtDTO> dailyThoughtList = new ArrayList<>();


    CategoryDTO category;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_topleader_list, container, false);
        search = view.findViewById(R.id.search);
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
        getTopLeaderThoughts();

        return view;
    }
    private void setRecyclerView(List<DailyThoughtDTO> dailyThoughtList) {

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        search.setOnQueryTextListener(listener);

        adapter = new DailyThoughtAdapter(ctx, dailyThoughtList, new DailyThoughtAdapter.DailyThoughtAdapterlistener() {


            @Override
            public void onPldpRequested(DailyThoughtDTO dailyThought) {
                Intent intent = new Intent(ctx, /*PLDPActivity*/CreatePldpActivity.class);
                intent.putExtra("dailyThought", dailyThought);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                ctx.startActivity(intent);
            }

            @Override
            public void onPhotoRequired(PhotoDTO photo) {

            }

            @Override
            public void onVideoRequired(VideoDTO video) {

            }

            @Override
            public void onPodcastRequired(PodcastDTO podcast) {

            }

            @Override
            public void onDailyThoughtRating(DailyThoughtDTO dailyThought) {

            }

            @Override
            public void onUrlRequired(UrlDTO url) {

            }

            @Override
            public void onPhotosRequired(List<PhotoDTO> list) {

            }

        });
        recyclerView.setAdapter(adapter);

    }
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<DailyThoughtDTO> filteredList = new ArrayList<>();

            for (int i = 0; i < dailyThoughtList.size(); i++) {

                final String text = dailyThoughtList.get(i).getJournalUserName().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(dailyThoughtList.get(i));
                }

            }
            setRecyclerView(filteredList);
            return true;

        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    private FirebaseAuth firebaseAuth;
    private UserDTO user = SharedPrefUtil.getUser(ctx);

    public void getTopLeaderThoughts() {
        Log.d(LOG, "************** getCompanyDailyThoughts: " );
        presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
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
        if (context instanceof TopLeaderListFragment.TopLeaderAdapterlistener) {
            mListener = (TopLeaderListFragment.TopLeaderAdapterlistener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TopLeaderAdapterlistener");
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

    static final String LOG = TopLeaderListFragment.class.getSimpleName();

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
        Log.i(LOG, "** onUserFound **" + user.getFullName());
        presenter.getDailyThoughtsByUserType(UserDTO.DESC_PLATINUM_USER);
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
        Log.i(LOG, "onDailyThoughts: " + list.size());
        this.dailyThoughtList = list;
        if(category != null) {
            list = getCategoryList(list, category.getCategoryName());
        }
        Collections.sort(list);
        setRecyclerView(list);
        adapter = new DailyThoughtAdapter(ctx, list, new DailyThoughtAdapter.DailyThoughtAdapterlistener() {

            @Override
            public void onPldpRequested(DailyThoughtDTO dailyThought) {
                Intent intent = new Intent(ctx, /*PLDPActivity*/CreatePldpActivity.class);
                intent.putExtra("dailyThought", dailyThought);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                ctx.startActivity(intent);
            }

            @Override
            public void onPhotoRequired(PhotoDTO photo) {

            }

            @Override
            public void onVideoRequired(VideoDTO video) {

            }

            @Override
            public void onPodcastRequired(PodcastDTO podcast) {

            }

            @Override
            public void onDailyThoughtRating(DailyThoughtDTO dailyThought) {

            }

            @Override
            public void onUrlRequired(UrlDTO url) {

            }

            @Override
            public void onPhotosRequired(List<PhotoDTO> list) {
                miniPhotoAdapter = new MiniPhotoAdapter(list, ctx, new PhotoAdapter.PhotoAdapterlistener() {
                    @Override
                    public void onPhotoClicked(PhotoDTO photo) {

                    }
                });
                photoRecyclerView.setAdapter(miniPhotoAdapter);
            }

        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPldps(List<PldpDTO> list) {

    }

    MiniPhotoAdapter miniPhotoAdapter;
    @Override
    public void onAllCompanyDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(LOG, "onAllCompanyDailyThoughts: " + list.size());
        this.dailyThoughtList = list;
        list = getCategoryList(list, category.getCategoryName());
        Collections.sort(list);
        setRecyclerView(list);
        adapter = new DailyThoughtAdapter(ctx, list, new DailyThoughtAdapter.DailyThoughtAdapterlistener() {

            @Override
            public void onPldpRequested(DailyThoughtDTO dailyThought) {
                Intent intent = new Intent(ctx, /*PLDPActivity*/CreatePldpActivity.class);
                intent.putExtra("dailyThought", dailyThought);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                ctx.startActivity(intent);
            }

            @Override
            public void onPhotoRequired(PhotoDTO photo) {

            }

            @Override
            public void onVideoRequired(VideoDTO video) {

            }

            @Override
            public void onPodcastRequired(PodcastDTO podcast) {

            }

            @Override
            public void onDailyThoughtRating(DailyThoughtDTO dailyThought) {
                Intent intent = new Intent(ctx, RatingActivity.class);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                intent.putExtra("dailyThought", dailyThought);
                ctx.startActivity(intent);
            }

            @Override
            public void onUrlRequired(UrlDTO url) {

            }

            @Override
            public void onPhotosRequired(List<PhotoDTO> list) {
                miniPhotoAdapter = new MiniPhotoAdapter(list, ctx, new PhotoAdapter.PhotoAdapterlistener() {
                    @Override
                    public void onPhotoClicked(PhotoDTO photo) {

                    }
                });
                photoRecyclerView.setAdapter(miniPhotoAdapter);
            }

        });
        recyclerView.setAdapter(adapter);
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
        setRecyclerView(list);
        adapter = new DailyThoughtAdapter(ctx, list, new DailyThoughtAdapter.DailyThoughtAdapterlistener() {


            @Override
            public void onPldpRequested(DailyThoughtDTO dailyThought) {
                Intent intent = new Intent(ctx, /*PLDPActivity*/CreatePldpActivity.class);
                intent.putExtra("dailyThought", dailyThought);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                ctx.startActivity(intent);
            }

            @Override
            public void onPhotoRequired(PhotoDTO photo) {
                /*photoAdapter = new PhotoAdapter(dailyThoughtList)*/
            }

            @Override
            public void onVideoRequired(VideoDTO video) {

            }

            @Override
            public void onPodcastRequired(PodcastDTO podcast) {

            }

            @Override
            public void onDailyThoughtRating(DailyThoughtDTO dailyThought) {
                Intent intent = new Intent(ctx, RatingActivity.class);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                intent.putExtra("dailyThought", dailyThought);
                ctx.startActivity(intent);
            }

            @Override
            public void onUrlRequired(UrlDTO url) {

            }

            @Override
            public void onPhotosRequired(List<PhotoDTO> list) {
                miniPhotoAdapter = new MiniPhotoAdapter(list, ctx, new PhotoAdapter.PhotoAdapterlistener() {
                    @Override
                    public void onPhotoClicked(PhotoDTO photo) {

                    }
                });
                photoRecyclerView.setAdapter(miniPhotoAdapter);
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


    public interface TopLeaderAdapterlistener {
        void onDailyThoughtTapped(DailyThoughtDTO dailyThought);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }
}
