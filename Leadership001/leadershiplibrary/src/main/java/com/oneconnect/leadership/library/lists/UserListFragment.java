package com.oneconnect.leadership.library.lists;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.UserListAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.UserCache;
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
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserListFragment extends Fragment implements PageFragment, SubscriberContract.View,
        CacheContract.View, BasicEntityAdapter.EntityListener {

    private UserDTO user;
    private SubscriberPresenter presenter = new SubscriberPresenter(this);
    private CachePresenter cachePresenter;
    private ResponseBag bag;
    public static final String TAG = UserListFragment.class.getSimpleName();
    private UserListListener mListener;
    private CompanyDTO company;
    private List<UserDTO> userList = new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;
    private Context ctx;
    private int type;
    private RecyclerView.LayoutManager mLayoutManager;
    public SearchView search;

    public interface UserListListener {
        void onUsersTapped(UserDTO user);
        void onPhotoRequired(BaseDTO base);
    }

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(HashMap<String,UserDTO> list) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        ResponseBag bag = new ResponseBag();
        bag.setUsers(new ArrayList<UserDTO>());
        for (UserDTO p: list.values()) {
            bag.getUsers().add(p);
        }
        args.putSerializable("bag", bag);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ResponseBag  bag = (ResponseBag) getArguments().getSerializable("bag");
            user = (UserDTO) getArguments().getSerializable("user");
            type = getArguments().getInt("type", 0);
            userList = bag.getUsers();
            presenter = new SubscriberPresenter(this);
            cachePresenter = new CachePresenter(this, ctx);
            user = (UserDTO) SharedPrefUtil.getUser(ctx);
            type = SharedPrefUtil.getFragmentType(ctx);
        }

    }

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_list, container, false);
        search = view.findViewById(R.id.search);
        presenter = new SubscriberPresenter(this);
        ctx = getActivity();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);

        getCachedUsers();
        getUsers();
        return view;

    }
    private void setRecyclerView(List<UserDTO> users) {

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        search.setOnQueryTextListener(listener);


        adapter = new UserListAdapter(users, ctx, new UserListAdapter.IconListener() {


            @Override
            public void onTakePicture(UserDTO user) {

            }

            @Override
            public void onGallery(UserDTO user) {

            }

            @Override
            public void onLocation(UserDTO user) {

            }

            @Override
            public void onPhone(UserDTO user) {

            }

            @Override
            public void onDriverLogs(UserDTO driver) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<UserDTO> filteredList = new ArrayList<>();

            for (int i = 0; i < userList.size(); i++) {

                final String text = userList.get(i).getFirstName().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(userList.get(i));
                }

            }
            setRecyclerView(filteredList);
            return true;

        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };
    public void getUsers() {
        Log.d(TAG, "************** getUsers: " );
        if (user == null) {
            presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
        }  else {
            presenter.getUsers(user.getCompanyID());
        }
    }

    private void getCachedUsers() {
        UserCache.getUsers(ctx, new UserCache.ReadListener() {
            @Override
            public void onDataRead(List<UserDTO> users) {
                Log.d(TAG, "onDataRead: users: " + users);
            }

            @Override
            public void onError(String message) {

                getCachedUsers();
            }
        });
    }

    UserListAdapter adapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserListListener) {
            mListener = (UserListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UserListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    int primaryColor, primaryDarkColor;
    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
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
        Log.i(TAG, "onCacheUsers " + list.size());
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
        presenter.getUsers(user.getCompanyID());
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

        Log.i(TAG, "onUsers: " + list.size());
        this.userList = list;
        setRecyclerView(list);
        adapter = new UserListAdapter(list, ctx, new UserListAdapter.IconListener() {


            @Override
            public void onTakePicture(UserDTO user) {

            }

            @Override
            public void onGallery(UserDTO user) {

            }

            @Override
            public void onLocation(UserDTO user) {

            }

            @Override
            public void onPhone(UserDTO user) {

            }

            @Override
            public void onDriverLogs(UserDTO driver) {

            }
        });
        recyclerView.setAdapter(adapter);
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


}
