package com.oneconnect.leadership.library.lists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.CreatePldpActivity;
import com.oneconnect.leadership.library.activities.FullArticleActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.MiniPhotoAdapter;
import com.oneconnect.leadership.library.adapters.NewsArticleAdapter;
import com.oneconnect.leadership.library.adapters.PhotoAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.NewsCache;
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
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kurisani on 2017/06/26.
 */

public class NewsListFragment extends Fragment implements PageFragment, SubscriberContract.View, CacheContract.View,
        BasicEntityAdapter.EntityListener , NewsArticleAdapter.NewsArticleListener {

    private NewsArticleListener mListener;
    private RecyclerView recyclerView, photoRecyclerView;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private ResponseBag bag;
    private EntityListFragment entityListFragment;
    private int type;
    private NewsDTO article;
    private UserDTO user;
    private RecyclerView.LayoutManager mLayoutManager;
    public SearchView search;
    private View view;
    private Context ctx;
    private FirebaseAuth firebaseAuth;

    NewsArticleAdapter adapter;
    private List<NewsDTO> newsArticletList = new ArrayList<>();

    public NewsListFragment() {
        // Required empty public constructor
    }

    public static NewsListFragment newInstance() {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bag = (ResponseBag) getArguments().getSerializable("bag");
            article = (NewsDTO) getArguments().getSerializable("newsArticle");
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news_article, container, false);
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
        photoRecyclerView = (RecyclerView) view.findViewById(R.id.photoRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        photoRecyclerView.setLayoutManager(llm2);
        photoRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        photoRecyclerView.setHasFixedSize(true);


        getCachedNewsArticle();
        getNewsArticle();

        return view;
    }
    private void setRecyclerView(List<NewsDTO> list) {

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        search.setOnQueryTextListener(listener);

        adapter = new NewsArticleAdapter(ctx, list, new NewsArticleAdapter.NewsArticleListener() {
            @Override
            public void onArticleSelected(NewsDTO newsArticle) {
                if (newsArticle.getBody() != null) {
                    Intent intent = new Intent(ctx, FullArticleActivity.class);
                    intent.putExtra("newsArticle", newsArticle);
                    ctx.startActivity(intent);
                }
            }

            @Override
            public void onPldpRequested(NewsDTO news) {
                Intent intent = new Intent(ctx, CreatePldpActivity.class);
                intent.putExtra("newsArticle", news);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                if (user != null) {
                    intent.putExtra("user", user);
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

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<NewsDTO> filteredList = new ArrayList<>();

            for (int i = 0; i < newsArticletList.size(); i++) {

                final String text = newsArticletList.get(i).getTitle().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(newsArticletList.get(i));
                }

            }
            setRecyclerView(filteredList);
            return true;

        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };
    public void getNewsArticle() {
        Log.d(LOG, "************** getNewsArticles: " );
        presenter.getAllNewsArticle();
        presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
       /* switch (type) {
            case Constants.INTERNAL_DATA:
                if (user == null) {
                    presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
                }  else {
                    presenter.getNews(user.getCompanyID());
                }
                break;
            case Constants.EXTERNAL_DATA:
                presenter.getAllNewsArticle();
                break;
        }*/

    }

    private void getCachedNewsArticle() {
        NewsCache.getNews(ctx, new NewsCache.ReadListener() {
            @Override
            public void onDataRead(List<NewsDTO> article) {
                Log.d(LOG, "onDataRead: newsArticle: " + article);
            }

            @Override
            public void onError(String message) {

                getCachedNewsArticle();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsArticleListener) {
            mListener = (NewsArticleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NewsArticleListener");
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

    static final String LOG = NewsListFragment.class.getSimpleName();

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
    public void onUserFound(UserDTO u) {
        Log.i(LOG, "*** onUserFound ***" + u.getFullName() + "\n" + "fetching company Articles");
        user = u;
        presenter.getCompanyProfile(u.getCompanyID());
     //   presenter.getNews(user.getCompanyID());
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

    MiniPhotoAdapter miniPhotoAdapter;

    @Override
    public void onAllNewsArticle(final List<NewsDTO> list) {
        Log.w(LOG, "onAllNewsArticle: " + list.size());
        this.newsArticletList= list;
        //list = getCategoryList(list, category.getCategoryName());
        /*if (category != null) {
            list = getCategoryList(list, category.getCategoryName());
        }*/
        Collections.sort(list);
        adapter = new NewsArticleAdapter(ctx, list, new NewsArticleAdapter.NewsArticleListener() {
           @Override
            public void onArticleSelected(NewsDTO newsArticle) {
               if (newsArticle.getBody() != null) {
                   Intent intent = new Intent(ctx, FullArticleActivity.class);
                   intent.putExtra("newsArticle", newsArticle);
                   ctx.startActivity(intent);
               }
            }

            @Override
            public void onPldpRequested(NewsDTO news) {
                Intent intent = new Intent(ctx, CreatePldpActivity.class);
                intent.putExtra("newsArticle", news);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                if (user != null) {
                    intent.putExtra("user", user);
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

    /*private List<NewsDTO> getCategoryList(List<NewsDTO> list, String typeName){
        //DailyThoughtDTO dailyThoughtDTO = null;
        List<NewsDTO> returnList = new ArrayList<>();
        for(NewsDTO news : list){
            if (NewsDTO.getCategory().getCategoryName() != null){
                if(news.getCategory().getCategoryName().equals(typeName)){
                    returnList.add(news);
                }
            }
        }
        return returnList;

    }*/

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
        Log.i(LOG, "onNews: " + list.size());
        this.newsArticletList = list;
        Collections.sort(list);
        adapter = new NewsArticleAdapter(ctx, list, new NewsArticleAdapter.NewsArticleListener() {
            @Override
            public void onArticleSelected(NewsDTO newsArticle) {
                if (newsArticle.getBody() != null) {
                    Intent intent = new Intent(ctx, FullArticleActivity.class);
                    intent.putExtra("newsArticle", newsArticle);
                    ctx.startActivity(intent);
                }
            }

            @Override
            public void onPldpRequested(NewsDTO news) {
                Intent intent = new Intent(ctx, CreatePldpActivity.class);
                intent.putExtra("newsArticle", news);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                if (user != null) {
                    intent.putExtra("user", user);
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


    @Override
    public void onArticleSelected(NewsDTO newsArticle) {

    }

    @Override
    public void onPldpRequested(NewsDTO news) {

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


    public interface NewsArticleListener {
        void onNewsArticleTapped(NewsDTO article);
    }
    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }
}
