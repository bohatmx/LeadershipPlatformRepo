package com.oneconnect.leadership.library.lists;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.oneconnect.leadership.library.R;
import com.google.android.youtube.player.internal.e;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.DailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.EbookAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.DailyThoughtCache;
import com.oneconnect.leadership.library.cache.EbookCache;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EBookListener} interface
 * to handle interaction events.
 * Use the {@link EBookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EBookListFragment extends Fragment implements PageFragment, SubscriberContract.View, CacheContract.View,
        BasicEntityAdapter.EntityListener{

    private EBookListener mListener;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private ResponseBag  bag;
    public static final String TAG = EBookListFragment.class.getSimpleName();
    private List<EBookDTO> eBooks = new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;
    private Context ctx;
    private  EbookAdapter ebookAdapter;
    private EBookDTO eBook;
    private int type;
    private UserDTO user;

    public EBookListFragment() {

    }

    public void setEBook(EBookDTO ebook) {
        Log.d(LOG, "### setEBook");
        this.eBook = ebook;
        ebook.getTitle();
        getCachedEBooks();
    }


    public static EBookListFragment newInstance(HashMap<String, EBookDTO> list) {
        EBookListFragment fragment = new EBookListFragment();
        Bundle args = new Bundle();
        ResponseBag bag = new ResponseBag();
        bag.seteBooks(new ArrayList<EBookDTO>());
        for (EBookDTO v: list.values()) {
            bag.geteBooks().add(v);
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
            eBooks = bag.geteBooks();
            eBook = (EBookDTO) getArguments().getSerializable("eBook");
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
        Log.d(TAG, "onCreateView: #################");
        view =  inflater.inflate(R.layout.fragment_ebook_list, container, false);
        presenter = new SubscriberPresenter(this);
        ctx = getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        /*LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);*/
        recyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));
        /*recyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));*/
        getCachedEBooks();
        getEBooks();

        return view;
    }

    private void getCachedEBooks() {
        EbookCache.getEbooks(ctx, new EbookCache.ReadListener() {
            @Override
            public void onDataRead(List<EBookDTO> ebooks) {
                Log.d(LOG, "onDataRead: EBooks: " + ebooks);
            }

            @Override
            public void onError(String message) {
                getCachedEBooks();
            }
        });

        File dir = Environment.getExternalStorageDirectory();
        File myDir = new File(dir, "leadership");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        File[] files = myDir.listFiles();
        filePathList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
               // if (file.getName().contains(eBook.getTitle())) {
                    if (file.getName().contains(".pdf")) {
                        filePathList.add(file.getAbsolutePath());
                    }
             //   }
            }
        }

        if (filePathList.isEmpty()) {
            Log.d(LOG, "getCachedEBooks: filePathList is empty");
        } else {
            fileContainerList.clear();
            for (String d : filePathList) {
                fileContainerList.add(new FileContainer(d));
            }
            Collections.sort(fileContainerList);
            filePathList.clear();
            for (FileContainer f : fileContainerList) {
                filePathList.add(f.fileName);
            }
            setList();
        }
    }


    private void setList() {
        adapter = new EbookAdapter(eBooks, ctx, new EbookAdapter.EbookAdapterListener() {
            @Override
            public void onReadClicked(String path) {

                File file = new File(filePathList.get(0));
                Log.e(LOG, "pdf file: " + file.getAbsolutePath() + " length: " + file.length());
                if (file.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                } else {
                    Log.d(LOG, "Statement is still downloading");
                }
                LinearLayoutManager lm = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(lm);
                recyclerView.setAdapter(ebookAdapter);
            }


        });
    }

    List<FileContainer> fileContainerList = new ArrayList<>();

    class FileContainer implements Comparable<FileContainer> {
        String fileName;
        Date date;

        public FileContainer(String filename) {
            this.fileName = filename;
            try {
                Pattern patt = Pattern.compile("-");
                String[] parts = patt.split(filename);

                DateTime dateTime = new DateTime(Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]), 1, 0, 0);
                date = dateTime.toDate();
            } catch (Exception e) {
                Log.e("FileContainer", "problem", e);
                date = new Date();
            }
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public int compareTo(FileContainer another) {
            if (this.date.before(another.date)) {
                return 1;
            }
            if (this.date.after(another.date)) {
                return -1;
            }
            return 0;
        }
    }

    private void getEBooks() {
        Log.d(LOG, "************** getEBooks: " );
            presenter.getAllEBooks();
    }

    EbookAdapter adapter;
    static final String LOG = EBookListFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EBookListener) {
            mListener = (EBookListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EBookListener");
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

        Log.i(LOG, "onCacheEbooks " + list.size());

        File dir = Environment.getExternalStorageDirectory();
        File myDir = new File(dir, "leadership");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        File[] files = myDir.listFiles();
        filePathList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(eBook.getTitle())) {
                    if (file.getName().contains("statement.pdf")) {
                        filePathList.add(file.getAbsolutePath());
                    }
                }
            }

        }


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
    public void onUserFound(UserDTO user) {

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

    List<String> filePathList = new ArrayList<>();


    @Override
    public void onAllEBooks(final List<EBookDTO> list) {
        Log.i(LOG, "onEbooks: " + list.size());
        this.eBooks = list;
//        Collections.sort(list);
        adapter = new EbookAdapter(list, ctx, new EbookAdapter.EbookAdapterListener() {
            @Override
            public void onReadClicked(String path) {
          //      File f = new File(path);
            //    if (f.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(path), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent newIntent = Intent.createChooser(intent, "Open File");
                try {
                    ctx.startActivity(newIntent);
                } catch(ActivityNotFoundException e) {
                    Log.e(LOG, "Failed to open pdf");
                }
               // startActivity(intent);
              //  }
                //readEbook(path);


            }



        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAllPhotos(List<PhotoDTO> list) {

    }

    @Override
    public void onAllWeeklyMessages(List<WeeklyMessageDTO> list) {

    }

    private void readEbook(String path) {
    Log.d(LOG, "Path for reading Ebook: " + path);
        File f = new File(path);
        if (f.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(f), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } {
            Log.d(LOG, "File doesn't exist, try something else");
        }
    }

    @Override
    public void onAllPodcasts(List<PodcastDTO> list) {

    }

    @Override
    public void onAllCalendarEvents(List<CalendarEventDTO> list) {

    }

    @Override
    public void onEbooks(List<EBookDTO> list) {
        Log.i(LOG, "onEbooks: " + list.size());
        this.eBooks = list;
        Collections.sort(list);
        adapter = new EbookAdapter(list, ctx, new EbookAdapter.EbookAdapterListener() {
            @Override
            public void onReadClicked(String path) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(path), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
               // Intent newIntent = Intent.createChooser(intent, "Choose an Application");
                try {
                    ctx.startActivity(intent);
                } catch(ActivityNotFoundException e) {
                    Log.e(LOG, "Failed to open pdf");
                }
                //readEbook(path);
            }


        });
        recyclerView.setAdapter(adapter);
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
    public void onError(String message) {

    }
    public interface EBookListener {
        void onEBookTapped(EBookDTO eBook);
    }

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }
}
