package com.oneconnect.leadership.library.lists;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.crud.CrudContract;
import com.oneconnect.leadership.library.crud.CrudPresenter;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.CountryDTO;
import com.oneconnect.leadership.library.data.BaseDTO;
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
import com.oneconnect.leadership.library.editors.DailyThoughtEditor;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class EntityListFragment extends BaseListingFragment implements CrudContract.View {

    public EntityListFragment() {
    }

    private List<BaseDTO> entities = new ArrayList<>();
    private BasicEntityAdapter adapter;
    private int type;
    private String stringTitle;
    private TextView txtTitle, txtCount;
    private ImageView iconAdd;
    private RecyclerView recyclerView;
    private BasicEntityAdapter.EntityListener mListener;
    Context ctx;
    private CrudPresenter presenter;

    public static EntityListFragment newInstance(ResponseBag bag) {
        EntityListFragment fragment = new EntityListFragment();
        Bundle args = new Bundle();
        args.putSerializable("bag", bag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            setupEntities();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category_list, container, false);
        presenter = new CrudPresenter(this);
        ctx = getActivity();

        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtCount = (TextView) view.findViewById(R.id.txtCount);
        iconAdd = (ImageView) view.findViewById(R.id.iconAdd);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        /*LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);*/
        txtTitle.setText(stringTitle);
        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddEntity();

            }
        });
        setList();
        return view;
    }

    public void setListener(BasicEntityAdapter.EntityListener mListener) {
        this.mListener = mListener;
    }

    public void setList() {
        txtCount.setText(String.valueOf(entities.size()));
        adapter = new BasicEntityAdapter(entities, type, getActivity(), new BasicEntityAdapter.EntityListener() {
            @Override
            public void onAddEntity() {
                mListener.onAddEntity();
            }

            @Override
            public void onDeleteClicked(BaseDTO entity) {
                mListener.onDeleteClicked(entity);
            }

            @Override
            public void onDeleteUser(UserDTO user) {
              mListener.onDeleteUser(user);
            }

            @Override
            public void onDeleteDailyThought(DailyThoughtDTO dailyThought) {
                mListener.onDeleteDailyThought(dailyThought);
            }

            @Override
            public void onDeleteWeeklyMessage(WeeklyMessageDTO weeklyMessage) {
                mListener.onDeleteWeeklyMessage(weeklyMessage);
            }

            @Override
            public void onDeleteWeeklyMasterClass(WeeklyMasterClassDTO masterClass) {
                mListener.onDeleteWeeklyMasterClass(masterClass);
            }

            @Override
            public void onDeletePodcast(PodcastDTO podcast) {
                mListener.onDeletePodcast(podcast);
            }

            @Override
            public void onDeleteNews(NewsDTO news) {
                mListener.onDeleteNews(news);
            }

            @Override
            public void onDeleteVideo(VideoDTO video) {
                mListener.onDeleteVideo(video);
            }

            @Override
            public void onDeleteEbook(EBookDTO eBook) {
                mListener.onDeleteEbook(eBook);
            }

            @Override
            public void onDeleteCategory(CategoryDTO category) {
                mListener.onDeleteCategory(category);
            }

            @Override
            public void onDeleteSubscription(SubscriptionDTO subscription) {
                mListener.onDeleteSubscription(subscription);
            }

            @Override
            public void onLinksRequired(BaseDTO entity) {
                mListener.onLinksRequired(entity);
            }

            @Override
            public void onPhotoCaptureRequested(BaseDTO entity) {
                mListener.onPhotoCaptureRequested(entity);
            }

            @Override
            public void onVideoCaptureRequested(BaseDTO entity) {
                mListener.onVideoCaptureRequested(entity);
            }

            @Override
            public void onSomeActionRequired(BaseDTO entity) {
                mListener.onSomeActionRequired(entity);
            }

            @Override
            public void onMicrophoneRequired(BaseDTO entity) {
                mListener.onMicrophoneRequired(entity);
            }

            @Override
            public void onEntityClicked(BaseDTO entity) {
                mListener.onEntityClicked(entity);
            }

            @Override
            public void onCalendarRequested(BaseDTO entity) {
                mListener.onCalendarRequested(entity);
            }

            @Override
            public void onEntityDetailRequested(BaseDTO entity, int type) {
                mListener.onEntityDetailRequested(entity,type);
            }


            @Override
            public void onDeleteTooltipRequired(int type) {
                mListener.onDeleteTooltipRequired(type);
            }

            @Override
            public void onLinksTooltipRequired(int type) {
               mListener.onLinksTooltipRequired(type);
            }

            @Override
            public void onPhotoCaptureTooltipRequired(int type) {
               mListener.onPhotoCaptureTooltipRequired(type);
            }

            @Override
            public void onVideoCaptureTooltipRequired(int type) {
               mListener.onVideoCaptureTooltipRequired(type);
            }

            @Override
            public void onSomeActionTooltipRequired(int type) {
               mListener.onSomeActionTooltipRequired(type);
            }

            @Override
            public void onMicrophoneTooltipRequired(int type) {
               mListener.onMicrophoneTooltipRequired(type);
            }

            @Override
            public void onCalendarTooltipRequired(int type) {
               mListener.onCalendarTooltipRequired(type);
            }

            @Override
            public void onNewsArticleRequested(BaseDTO entity) {
                mListener.onNewsArticleRequested(entity);
            }

            @Override
            public void onUpdateUser(UserDTO user) {
                mListener.onUpdateUser(user);
            }

            @Override
            public void onUpdateDailyThought(DailyThoughtDTO dailyThought) {
                mListener.onUpdateDailyThought(dailyThought);
            }

            @Override
            public void onUpdateWeeklyMessage(WeeklyMessageDTO weeklyMessage) {
                mListener.onUpdateWeeklyMessage(weeklyMessage);
            }

            @Override
            public void onUpdateWeeklyMasterClass(WeeklyMasterClassDTO masterClass) {
                mListener.onUpdateWeeklyMasterClass(masterClass);
            }

            @Override
            public void onUpdateNews(NewsDTO news) {
                mListener.onUpdateNews(news);
            }

            @Override
            public void onUpdateCategory(CategoryDTO category) {
                mListener.onUpdateCategory(category);
            }

            @Override
            public void onUpdateSubscription(SubscriptionDTO subscription) {
                mListener.onUpdateSubscription(subscription);
            }
        });

        recyclerView.setAdapter(adapter);
    }


    private void setupEntities() {
        ResponseBag bag = (ResponseBag) getArguments().getSerializable("bag");
        if (bag != null) {
            type = bag.getType();
            switch (type) {
                case ResponseBag.CATEGORIES:
                    stringTitle = ResponseBag.DESC_CATEGORIES;
                    for (CategoryDTO c : bag.getCategories()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.COMPANIES:
                    stringTitle = ResponseBag.DESC_COMPANY;
                    for (CompanyDTO c : bag.getCompanies()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.COUNTRIES:
                    stringTitle = ResponseBag.DESC_COUNTRY;
                    for (CountryDTO c : bag.getCountries()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.DAILY_THOUGHTS:
                    stringTitle = ResponseBag.DESC_DAILY_THOUGHTS;
                    for (DailyThoughtDTO c : bag.getDailyThoughts()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.EBOOKS:
                    stringTitle = ResponseBag.DESC_EBOOKS;
                    for (EBookDTO c : bag.geteBooks()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.NEWS:
                    stringTitle = ResponseBag.DESC_NEWS;
                    for (NewsDTO c : bag.getNews()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PAYMENTS:
                    stringTitle = ResponseBag.DESC_PAYMENTS;
                    for (PaymentDTO c : bag.getPayments()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PHOTOS:
                    stringTitle = ResponseBag.DESC_PHOTOS;
                    for (PhotoDTO c : bag.getPhotos()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PODCASTS:
                    stringTitle = ResponseBag.DESC_PODCASTS;
                    for (PodcastDTO c : bag.getPodcasts()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.PRICES:
                    stringTitle = ResponseBag.DESC_PRICE;
                    for (PriceDTO c : bag.getPrices()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.SUBSCRIPTIONS:
                    stringTitle = ResponseBag.DESC_SUBSCRIPTIONS;
                    for (SubscriptionDTO c : bag.getSubscriptions()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.USERS:
                    stringTitle = ResponseBag.DESC_USERS;
                    for (UserDTO c : bag.getUsers()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.VIDEOS:
                    stringTitle = ResponseBag.DESC_VIDEOS;
                    for (VideoDTO c : bag.getVideos()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.WEEKLY_MASTERCLASS:
                    stringTitle = ResponseBag.DESC_WEEKLY_MASTERCLASS;
                    for (WeeklyMasterClassDTO c : bag.getWeeklyMasterClasses()) {
                        entities.add(c);
                    }
                    break;
                case ResponseBag.WEEKLY_MESSAGE:
                    stringTitle = ResponseBag.DESC_WEEKLY_MESSAGE;
                    for (WeeklyMessageDTO c : bag.getWeeklyMessages()) {
                        entities.add(c);
                    }
                    break;
            }
        }
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

    }

    /*int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }*/
}
