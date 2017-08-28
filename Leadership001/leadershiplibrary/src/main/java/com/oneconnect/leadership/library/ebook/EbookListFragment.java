package com.oneconnect.leadership.library.ebook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.links.LinksActivity;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.EbookCache;
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
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.PageFragment;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


public class EbookListFragment extends Fragment implements PageFragment, SubscriberContract.View, CacheContract.View,
        BasicEntityAdapter.EntityListener{

    private EBookListener mListener;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private ResponseBag bag;
    public static final String TAG = EbookListFragment.class.getSimpleName();
    private List<EBookDTO> eBooks = new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;
    private Context ctx;
    private EBookDTO eBook;
    private int type;
    private UserDTO user;

    public EbookListFragment() {
        // Required empty public constructor
    }

    public static EbookListFragment newInstance(HashMap<String, EBookDTO> list) {
        EbookListFragment fragment = new EbookListFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ebook_list, container, false);

        presenter = new SubscriberPresenter(this);
        ctx = getActivity();
        recyclerView = (RecyclerView)view.findViewById(com.oneconnect.leadership.library.R.id.recyclerView);
        /*LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);*/
        recyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));

        /*recyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));*/

        getCachedEBooks();
        getEBooks();

        return view;
    }

    List<String> filePathList = new ArrayList<>();
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
                fileContainerList.add(new EbookListFragment.FileContainer(d));
            }
            Collections.sort(fileContainerList);
            filePathList.clear();
            for (EbookListFragment.FileContainer f : fileContainerList) {
                filePathList.add(f.fileName);
            }
            setList();
        }
    }

    private void getEBooks() {
        Log.d(LOG, "************** getEBooks: " );
        if (SharedPrefUtil.getUser(ctx).getCompanyID() != null) {
            presenter.getAllEBooks();
        } else {
            Log.d(LOG, "user is null");
        }

    }

    AdminEbookAdapter adapter;

    private void setList() {
        adapter = new AdminEbookAdapter(eBooks, ctx, new AdminEbookAdapter.EbookAdapterListener() {
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


            }

            @Override
            public void onAttachPhoto(EBookDTO ebook) {
              //  pickGalleryOrCamera(ebook);
                startPhotoGallerySelection(eBook);
            }

            @Override
            public void onVideoRequired(BaseDTO base) {
                startVideoSelection(base);
            }

            @Override
            public void onPoadcastRequired(BaseDTO base) {
                startPodcastSelection(base);
            }

            @Override
            public void onUrlRequired(BaseDTO base) {
                startLinksActivity(base);
            }



           /* @Override
            public void onAttachPhoto(BaseDTO base) {
                pickGalleryOrCamera(base);
            }*/
        });

        recyclerView.setAdapter(adapter);
    }

    private void startLinksActivity(BaseDTO base){
        Intent m = new Intent(ctx, LinksActivity.class);
        m.putExtra("type", type);
        switch (type) {
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) base;
                m = new Intent(ctx, LinksActivity.class);
                m.putExtra("weeklyMasterClass", weeklyMasterClass);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) base;
                m = new Intent(ctx, LinksActivity.class);
                m.putExtra("weeklyMessage", weeklyMessage);
                break;
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                m = new Intent(ctx, LinksActivity.class);
                m.putExtra("dailyThought", dailyThought);
                break;
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) base;
                m = new Intent(ctx, LinksActivity.class);
                m.putExtra("podcast", podcast);
                break;
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) base;
                m = new Intent(ctx, LinksActivity.class);
                m.putExtra("eBook", eBook);
                break;
        }
        startActivity(m);
       // startActivityForResult(m, REQUEST_LINKS);
    }
   DailyThoughtDTO dailyThought;
    WeeklyMessageDTO weeklyMessage;
    WeeklyMasterClassDTO weeklyMasterClass;

    private void startPodcastSelection(BaseDTO base){
        Intent intent = new Intent(ctx, PodcastSelectionActivity.class);
        intent.putExtra("type", type);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                intent.putExtra("dailyThought", dailyThought);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) base;
                intent.putExtra("weeklyMasterClass", weeklyMasterClass);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) base;
                intent.putExtra("weeklyMessage", weeklyMessage);
                break;

            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) base;
                intent.putExtra("eBook", eBook);
                break;
        }
        startActivity(intent);
    }

    private void pickGalleryOrCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(ctx);
        b.setTitle("Select Images")
                .setMessage("Please select the source of the photos")
                .setPositiveButton("Use Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // startCamera(base);
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
        intent.putExtra("type", 0/*type*/);
        switch (type) {
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) base;
                intent.putExtra("eBook", eBook);
                break;
        }
        startActivity(intent);
    }

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

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setPageTitle(String pageTitle) {

    }

    @Override
    public void onAddEntity() {

    }

    @Override
    public void onDeleteClicked(BaseDTO entity) {

    }
       PodcastDTO podcast;
    @Override
    public void onLinksRequired(BaseDTO entity) {

            Intent m = null;
            switch (type) {
                case ResponseBag.WEEKLY_MASTERCLASS:
                    weeklyMasterClass = (WeeklyMasterClassDTO) entity;
                    m = new Intent(ctx, LinksActivity.class);
                    m.putExtra("weeklyMasterClass", weeklyMasterClass);
                    break;
                case ResponseBag.WEEKLY_MESSAGE:
                    weeklyMessage = (WeeklyMessageDTO) entity;
                    m = new Intent(ctx, LinksActivity.class);
                    m.putExtra("weeklyMessage", weeklyMessage);
                    break;
                case ResponseBag.DAILY_THOUGHTS:
                    dailyThought = (DailyThoughtDTO) entity;
                    m = new Intent(ctx, LinksActivity.class);
                    m.putExtra("dailyThought", dailyThought);
                    break;
                case ResponseBag.PODCASTS:
                    podcast = (PodcastDTO) entity;
                    m = new Intent(ctx, LinksActivity.class);
                    m.putExtra("podcast", podcast);
                    break;

                case ResponseBag.EBOOKS:
                    eBook = (EBookDTO) entity;
                    m = new Intent(ctx, LinksActivity.class);
                    m.putExtra("eBook", eBook);
                    break;
            }
            m.putExtra("type", type);
            startActivityForResult(m, REQUEST_LINKS);
        }

    public static final int REQUEST_LINKS = 1875;

    @Override
    public void onPhotoCaptureRequested(BaseDTO entity) {
        startPhotoGallerySelection(entity);
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
    public void onAllRatings(List<RatingDTO> list) {

    }

    @Override
    public void onDailyThoughtRatings(List<RatingDTO> list) {

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
    public void onAllNewsArticle(List<NewsDTO> list) {

    }

    @Override
    public void onAllCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onAllVideos(List<VideoDTO> list) {

    }

    private void startVideoSelection(BaseDTO base) {
        Intent m = new Intent(ctx, VideoSelectionActivity.class);
        m.putExtra("type", type);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                m.putExtra("dailyThought", dailyThought);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) base;
                m.putExtra("weeklyMasterClass", weeklyMasterClass);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) base;
                m.putExtra("weeklyMessage", weeklyMessage);
                break;
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) base;
                m.putExtra("eBook", eBook);
                break;
        }

        startActivity(m);
    }

    @Override
    public void onAllEBooks(List<EBookDTO> list) {
        Log.i(LOG, "onEbooks: " + list.size());
        this.eBooks = list;
//        Collections.sort(list);
        adapter = new AdminEbookAdapter(list, ctx, new AdminEbookAdapter.EbookAdapterListener() {
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

            @Override
            public void onAttachPhoto(EBookDTO ebook) {
                startPhotoGallerySelection(ebook);
            }

            @Override
            public void onVideoRequired(BaseDTO base) {
                startVideoSelection(base);
            }

            @Override
            public void onPoadcastRequired(BaseDTO base) {
                startPodcastSelection(base);
            }

            @Override
            public void onUrlRequired(BaseDTO base) {
                startLinksActivity(base);
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
    public void onError(String message) {

    }
    public interface EBookListener {
        void onEBookTapped(EBookDTO eBook);
    }

    static final String LOG = EbookListFragment.class.getSimpleName();
}
