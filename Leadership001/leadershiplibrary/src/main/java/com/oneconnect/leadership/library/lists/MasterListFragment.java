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

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.MasterAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.WeeklyMasterclassCache;
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
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kurisani on 2017/05/02.
 */

public class MasterListFragment extends Fragment implements PageFragment, SubscriberContract.View,
        CacheContract.View, BasicEntityAdapter.EntityListener {

    private MasterAdapter.MasterAdapterListener mListener;
    private ResponseBag bag;
    private EntityListFragment entityListFragment;
    private RecyclerView recyclerView;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private UserDTO user;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weekly_master_class_list, container, false);
        presenter = new SubscriberPresenter(this);
        ctx = getActivity();

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

    public void getWeeklyMasterClasses() {
        Log.d(LOG, "************** getWeeklyMasterClasses: " );
        if (SharedPrefUtil.getUser(ctx).getCompanyID() != null) {
            presenter.getAllWeeklyMasterClasses();
        } else {
            Log.d(LOG, "user is null");
        }
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
    public String getTitle() {
        return null;
    }

    @Override
    public void onAddEntity() {

    }

    @Override
    public void onDeleteClicked(BaseDTO entity) {

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
    public void onEntityAdded(String key) {

    }

    @Override
    public void onEntityUpdated() {

    }

    @Override
    public void onUserCreated(UserDTO user) {

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
    public void onAllVideos(List<VideoDTO> list) {

    }

    @Override
    public void onAllEBooks(List<EBookDTO> list) {

    }

    @Override
    public void onAllWeeklyMessages(List<WeeklyMessageDTO> list) {

    }

    @Override
    public void onAllPodcasts(List<PodcastDTO> list) {

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
        Log.i(LOG, "onWeeklyMasterclasses: " + list.size());
        this.weeklyMasterList = list;
        adapter = new MasterAdapter(ctx, list);
        recyclerView.setAdapter(adapter);
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
}