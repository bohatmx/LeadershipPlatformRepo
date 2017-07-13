package com.oneconnect.leadership.library.ebook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.PhotoActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.audio.PodcastListActivity;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.EbookCache;
import com.oneconnect.leadership.library.camera.VideoListActivity;
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
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.links.LinksActivity;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.PhotoListFragment;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class EbookListActivity extends AppCompatActivity implements SubscriberContract.View, CacheContract.View,
        BasicEntityAdapter.EntityListener, PhotoListFragment.PhotoListener{

    Toolbar toolbar;
    Context ctx;
    private EbookListFragment ebookListFragment;
    static public EBookDTO eBook;
    private int type;
    private DailyThoughtDTO dailyThought;
    private PodcastDTO podcast;
    private UrlDTO url;
    private VideoDTO video;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private List<EBookDTO> eBooks = new ArrayList<>();
    private RecyclerView recyclerView;
    private SubscriberPresenter presenter;
    private CachePresenter cachePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
   //     setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book Cover Image Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        presenter = new SubscriberPresenter(this);
        cachePresenter = new CachePresenter(this, ctx);

        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
                getSupportActionBar().setSubtitle(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");
                getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
                getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
                break;
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
                getSupportActionBar().setSubtitle(eBook.getStorageName());
                break;
            case ResponseBag.VIDEOS:
                video = (VideoDTO) getIntent().getSerializableExtra("video");
                getSupportActionBar().setSubtitle(video.getStorageName());
                break;
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
                getSupportActionBar().setSubtitle(podcast.getStorageName());
                break;
            case ResponseBag.URLS:
                url = (UrlDTO) getIntent().getSerializableExtra("url");
                getSupportActionBar().setSubtitle(url.getTitle());
                break;
        }
        if (getIntent().getSerializableExtra("podcast") != null) {
            type = ResponseBag.PODCASTS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
            getSupportActionBar().setSubtitle(podcast.getStorageName());
        }
        if (getIntent().getSerializableExtra("video") != null) {
            type = ResponseBag.VIDEOS;
            eBook = (EBookDTO) getIntent().getSerializableExtra("video");
            getSupportActionBar().setSubtitle(eBook.getStorageName());
        }
        if (getIntent().getSerializableExtra("url") != null) {
            type = ResponseBag.URLS;
            url = (UrlDTO) getIntent().getSerializableExtra("url");
            getSupportActionBar().setSubtitle(url.getTitle());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));

        //getCachedEBooks();
        getEBooks();
      //  ebookListFragment = (EbookListFragment) getSupportFragmentManager().findFragmentById(R.id.ebookFragment);
       // eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");

    }

   // @Override
   //    {
   //     super.onRestart();
   //     finish();
   //     startActivity(getIntent());
   // }

    private void pickGalleryOrCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(this/*ctx*/);
        b.setTitle("Select Images")
                .setMessage("Please select the source of the photos")
                .setPositiveButton("Use Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                   //    Intent intent = new Intent(EbookListActivity.this, PhotoSelectionActivity.class);
                   //     eBook = (EBookDTO) base;
                   //     intent.putExtra("eBook", eBook);
                   //     intent.putExtra("type", ResponseBag.EBOOKS/*3*//*type*/);
                        /*switch (type) {
                            case ResponseBag.EBOOKS:
                                // eBook = (EBookDTO) base;
                                intent.putExtra("eBook", base*//*eBook*//*);
                                break;
                        }*/
                   //     startActivity(intent);
                        //startPhotoGallerySelection(base);
                        // startCamera(base);

                        Intent intent = new Intent(EbookListActivity.this, PhotoActivity.class);
                        eBook = (EBookDTO) base;
                        intent.putExtra("eBook", eBook);
                        startActivity(intent);
                    }
                }).show();
    }

    private void startPhotoGallerySelection(BaseDTO base){
        Intent intent = new Intent(EbookListActivity.this, PhotoSelectionActivity.class);
        type = ResponseBag.EBOOKS;
        eBook = (EBookDTO) base;
        intent.putExtra("eBook", eBook);
     //   type = ResponseBag.EBOOKS;
     //   eBook = (EBookDTO) base;
      /*  m.putExtra("eBook", eBook);
        intent.putExtra("type", type);
        switch (type) {
            case ResponseBag.EBOOKS:
               // eBook = (EBookDTO) base;
                intent.putExtra("eBook", base);
                break;
        }*/
        startActivity(intent);
    }

    List<String> filePathList = new ArrayList<>();
    List<FileContainer> fileContainerList = new ArrayList<>();

    @Override
    public void onPhotoTapped(PhotoDTO photo) {

    }

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
                    /*ctx.*/startActivity(intent);
                } else {
                    Log.d(LOG, "Statement is still downloading");
                }


            }

            @Override
            public void onAttachPhoto(EBookDTO ebook) {
                //startPhotoGallerySelection(ebook);
                pickGalleryOrCamera(ebook);
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
        Intent m = new Intent(EbookListActivity.this, LinksActivity.class);
        type = ResponseBag.EBOOKS;
        eBook = (EBookDTO) base;
        m.putExtra("eBook", eBook);
      /*  m.putExtra("type", type);
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
        }*/
        startActivity(m);
        // startActivityForResult(m, REQUEST_LINKS);
    }

    private void startPodcastSelection(BaseDTO base){
        Intent intent = new Intent(EbookListActivity.this, PodcastSelectionActivity.class);
        type = ResponseBag.EBOOKS;
        eBook = (EBookDTO) base;
        intent.putExtra("eBook", eBook);
       //type=  ResponseBag.PODCASTS;
       // podcast = (PodcastDTO) base;
        //intent.putExtra("podcast", podcast);
      //  intent.putExtra("type", type);
     /*   switch (type) {
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
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) base;
                intent.putExtra("podcast", podcast);
                break;
            case ResponseBag.VIDEOS:
                video = (VideoDTO) base;
                intent.putExtra("video", video);
                break;
        } */
     startActivity(intent);
        //startActivityForResult(intent, type);
    }

    private void startVideoSelection(BaseDTO base) {
        Intent m = new Intent(EbookListActivity.this, VideoSelectionActivity.class);
        type = ResponseBag.EBOOKS;
        eBook = (EBookDTO) base;
        m.putExtra("eBook", eBook);
    /*    m.putExtra("type", type);
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
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) base;
                //m = new Intent(this, LinksActivity.class);
                m.putExtra("podcast", podcast);
                break;
            case ResponseBag.VIDEOS:
                video = (VideoDTO) base;
                m.putExtra("video", video);
                break;
        } */

        startActivity(m);
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
    public void onAllVideos(List<VideoDTO> list) {

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
                    /*ctx.*/startActivity(newIntent);
                } catch(ActivityNotFoundException e) {
                    Log.e(LOG, "Failed to open pdf");
                }
                // startActivity(intent);
                //  }
                //readEbook(path);


            }

            @Override
            public void onAttachPhoto(EBookDTO ebook) {
                //startPhotoGallerySelection(ebook);
                 pickGalleryOrCamera(ebook);
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

    static final String LOG = EbookListActivity.class.getSimpleName();
}
