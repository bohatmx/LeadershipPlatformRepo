package com.oneconnect.leadership.library.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.ApprovalThoughtAdapter;
import com.oneconnect.leadership.library.adapters.DailyThoughtAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.DailyThoughtCache;
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
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DailyThoughtApprovalActivity extends AppCompatActivity implements CrudContract.View,
        CacheContract.View{

    CrudPresenter presenter;
    private CachePresenter cachePresenter;
    private Context ctx;
    ResponseBag bag;
    private UserDTO user;
    private int type;
    RecyclerView pendingRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_thought);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();
        //toolbar.setLogo(R.drawable.harmony);

        presenter = new CrudPresenter(this);

        pendingRecyclerView = (RecyclerView) findViewById(R.id.pendingRecyclerView);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCachedDailyThoughts();
        getPendingDailyThoughts();


        user = SharedPrefUtil.getUser(this);
    }

    public void getPendingDailyThoughts() {
        Log.d(TAG, "************** getPendingDailyThoughts: " );
        presenter.getPendingDailyThoughts("pending");


    }

    private void getCachedDailyThoughts() {
        DailyThoughtCache.getDailyThoughts(ctx, new DailyThoughtCache.ReadListener() {
            @Override
            public void onDataRead(List<DailyThoughtDTO> dailyThoughts) {
                Log.d(TAG, "onDataRead: DailyThoughts: " + dailyThoughts);
            }

            @Override
            public void onError(String message) {

                getCachedDailyThoughts();
            }
        });
    }



    static final String TAG = DailyThoughtApprovalActivity.class.getSimpleName();

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
    public void onCompanyCreated(CompanyDTO company) {

    }

    @Override
    public void onUserUpdated(UserDTO user) {

    }

    @Override
    public void onDailyThoughtUpdated(DailyThoughtDTO dailyThought) {
        Log.i(TAG, "onDailyThoughtUpdated status: " + dailyThought.getStatus());
        presenter.getPendingDailyThoughts("pending");

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

    }

    ApprovalThoughtAdapter adapter;

    @Override
    public void onPendingDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "onPendingDailyThoughts: " + list.size());
        Collections.sort(list);

        adapter = new ApprovalThoughtAdapter(ctx, list, new ApprovalThoughtAdapter.ApprovalThoughtAdapterlistener() {
            @Override
            public void onThoughtClicked(int position) {

            }

            @Override
            public void onThoughtSelected(DailyThoughtDTO dailyThought) {
                Intent intent = new Intent(DailyThoughtApprovalActivity.this, UpdateEntityActivity.class);
                intent.putExtra("dailyThought", dailyThought);
                startActivity(intent);
                finish();
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
        });
        pendingRecyclerView.setAdapter(adapter);
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

    }
}
