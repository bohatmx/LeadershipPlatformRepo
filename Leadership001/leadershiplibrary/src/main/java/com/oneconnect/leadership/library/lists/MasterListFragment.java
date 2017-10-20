package com.oneconnect.leadership.library.lists;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.DailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.MasterAdapter;
import com.oneconnect.leadership.library.adapters.MiniPhotoAdapter;
import com.oneconnect.leadership.library.adapters.PhotoAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.WeeklyMasterclassCache;
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
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kurisani on 2017/05/02.
 */

public class MasterListFragment extends Fragment implements PageFragment, SubscriberContract.View,
        CacheContract.View, BasicEntityAdapter.EntityListener {

    //private MasterAdapter.MasterAdapterListener mListener;
    private WeeklyMasterClassListener mListener;
    private ResponseBag bag;
    private EntityListFragment entityListFragment;
    private RecyclerView recyclerView,photoRecyclerView;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private UserDTO user;
    private MasterListener masterListener;
    private FirebaseAuth firebaseAuth;
    private RecyclerView.LayoutManager mLayoutManager;
    public SearchView search;

    public interface MasterListener {
        void onWeeklyMasterClassesTapped(WeeklyMasterClassDTO master);
    }

    public MasterListFragment(){

    }

    public static MasterListFragment newInstance(){
        MasterListFragment fragment = new MasterListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private int type;
    private WeeklyMasterClassDTO weeklyMaster;
    private View view;
    private Context ctx;
    MasterAdapter adapter;
    private List<WeeklyMasterClassDTO> weeklyMasterList = new ArrayList<>();

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        if (getArguments() != null) {
            bag = (ResponseBag) getArguments().getSerializable("bag");
            weeklyMaster = (WeeklyMasterClassDTO) getArguments().getSerializable("weeklyMasterClass");
            type = getArguments().getInt("type", 0);

            presenter = new SubscriberPresenter(this);
            cachePresenter = new CachePresenter(this, ctx);

            user = SharedPrefUtil.getUser(ctx);
            type = SharedPrefUtil.getFragmentType(ctx);
        }
    }

    CategoryDTO category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weekly_master_class_list, container, false);
        presenter = new SubscriberPresenter(this);
        search = view.findViewById(R.id.search);
        ctx = getActivity();
        firebaseAuth = FirebaseAuth.getInstance();
        if (getActivity().getIntent().getSerializableExtra("category") != null) {
            category = (CategoryDTO) getActivity().getIntent().getSerializableExtra("category");
            Log.i(LOG, "category: " + category.getCategoryName());
        } else {
            Log.i(LOG, "No Category");
        }

        if (getActivity().getIntent().getSerializableExtra("type") != null) {
            type = (int) getActivity().getIntent().getSerializableExtra("type");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);

        getCachedWeeklyMasterClasses();
        getWeeklyMasterClasses();

        return view;
    }
    static final String LOG = MasterListFragment.class.getSimpleName();

    private void setRecyclerView(List<WeeklyMasterClassDTO> masterList) {

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        search.setOnQueryTextListener(listener);

        adapter = new MasterAdapter(ctx, masterList, new MasterAdapter.MasterAdapterListener() {


            @Override
            public void onThoughtClicked(int position) {

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
            public void onUrlRequired(UrlDTO url) {

            }

            @Override
            public void onPhotosRequired(List<PhotoDTO> list) {

            }

            @Override
            public void onMasterClicked(int position) {

            }
        });
        recyclerView.setAdapter(adapter);

    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<WeeklyMasterClassDTO> filteredList = new ArrayList<>();

            for (int i = 0; i < weeklyMasterList.size(); i++) {

                final String text = weeklyMasterList.get(i).getTitle().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(weeklyMasterList.get(i));
                }

            }
            setRecyclerView(filteredList);
            return true;

        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    public void getWeeklyMasterClasses() {
        Log.d(LOG, "************** getWeeklyMasterClasses: " );
        presenter.getAllWeeklyMasterClasses();
       /* switch (type) {
            case Constants.INTERNAL_DATA:
                if (user == null) {
                    presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
                }  else {
                    presenter.getWeeklyMasterclasses(user.getCompanyID());
                }
                break;
            case Constants.EXTERNAL_DATA:
                presenter.getAllWeeklyMasterClasses();
                break;
        }*/

    }

    private void getCachedWeeklyMasterClasses() {
        WeeklyMasterclassCache.getWeeklyMasterclasses(ctx, new WeeklyMasterclassCache.ReadListener() {
            @Override
            public void onDataRead(List<WeeklyMasterClassDTO> weeklyMasterClass) {
                Log.d(LOG, "onDataRead: WeeklyMasterClasses: " + weeklyMasterClass);
            }

            @Override
            public void onError(String message) {

                getCachedWeeklyMasterClasses();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WeeklyMasterClassListener) {
            mListener = (WeeklyMasterClassListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WeeklyMasterClassListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface WeeklyMasterClassListener {
        void onWeeklyMasterClassTapped(WeeklyMasterClassDTO weeklyMasterClass);
    }

    @Override
    public String getTitle() {
        return pageTitle;
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
        WeeklyMasterclassCache.getWeeklyMasterclasses(ctx, new WeeklyMasterclassCache.ReadListener() {
            @Override
            public void onDataRead(List<WeeklyMasterClassDTO> weeklyMasterClass) {
                Log.d(LOG, "onDataRead: weeklyMasterClass: " + weeklyMasterClass);
            }

            @Override
            public void onError(String message) {

                getCachedWeeklyMasterClasses();
            }
        });

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
        Log.i(LOG, "*** onUserFound ***" + user.getFullName() + "\n" + "fetching company master classes");
        presenter.getWeeklyMasterclasses(user.getCompanyID());
    }

    @Override
    public void onCompanyFound(CompanyDTO company) {

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

        Log.w(LOG, "onWeeklyMasterclasses: " + list.size());
        this.weeklyMasterList = list;
        if (category != null) {
            list = getCategoryList(list, category.getCategoryName());
        }
        setRecyclerView(list);
        adapter = new MasterAdapter(ctx, list, new MasterAdapter.MasterAdapterListener(){

            @Override
            public void onThoughtClicked(int position) {

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

            @Override
            public void onMasterClicked(int position) {

            }
        });
        recyclerView.setAdapter(adapter);

    }
    MiniPhotoAdapter miniPhotoAdapter;
    private List<WeeklyMasterClassDTO> getCategoryList(List<WeeklyMasterClassDTO> list, String typeName){
        List<WeeklyMasterClassDTO> returnList = new ArrayList<>();
        for(WeeklyMasterClassDTO weeklyMasterClassDTO : list){
            if (weeklyMasterClassDTO.getCategory().getCategoryName() != null){
                if(weeklyMasterClassDTO.getCategory().getCategoryName().equals(typeName)){
                    returnList.add(weeklyMasterClassDTO);
                }
            }
        }
        return returnList;

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

    String pageTitle;

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }
}
