package com.oneconnect.leadership.library.ebook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.PhotoActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.audio.PodcastAdapter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.camera.VideoListActivity;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.photo.PhotoAdapter;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.photo.PhotoUploadContract;
import com.oneconnect.leadership.library.photo.PhotoUploadPresenter;
import com.oneconnect.leadership.library.activities.ProgressBottomSheet;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.DeleteContract;
import com.oneconnect.leadership.library.util.DeletePresenter;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import static com.oneconnect.leadership.library.R.id.image1;
import static com.oneconnect.leadership.library.R.id.image2;


public class EbookSelectionActivity extends AppCompatActivity implements EbookUploadContract.View, PhotoUploadContract.View,
        EbookListFragment.EBookListener, BasicEntityAdapter.EntityListener, SubscriberContract.View, DeleteContract.View{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private PodcastDTO podcast;
    private VideoDTO video;
    private  UrlDTO url;
    private int type;
    private Snackbar snackbar;
    private EbookUploadPresenter presenter;
    private DeletePresenter deletePresenter;
    private PhotoUploadPresenter photoPresenter;
    private SubscriberPresenter presenterEbook;
    private EBookDTO eBook;
    public static final String TAG = EbookSelectionActivity.class.getSimpleName();
    private List<EBookDTO> eBooks = new ArrayList<>();

    ImageView books, booksimg;
    TextView nameTxt, bookCounterTxt;
    private Toolbar toolbar;
    Context ctx;
    SearchView searchView = null;
    public List<String> filePathList;
    EbookAdapter adapter;
   // MultipleEbookSelectorAdapter adapter;
    public List<String> serverList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("EBook Selection & Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        deletePresenter = new DeletePresenter(this);
        presenter = new EbookUploadPresenter(this);
        photoPresenter = new PhotoUploadPresenter(this);
        presenterEbook = new SubscriberPresenter(this);

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
            video = (VideoDTO) getIntent().getSerializableExtra("video");
            getSupportActionBar().setSubtitle(video.getStorageName());
        }
        if (getIntent().getSerializableExtra("url") != null) {
            type = ResponseBag.URLS;
            url = (UrlDTO) getIntent().getSerializableExtra("url");
            getSupportActionBar().setSubtitle(url.getTitle());
        }

        setup();
        //getEbooksOnDevice();
        walkdir(Environment.getExternalStorageDirectory());
        getEBooks();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void getEBooks() {
        Log.d(LOG, "************** getEBooks: " );
      //  if (SharedPrefUtil.getUser(ctx).getCompanyID() != null) {
            presenterEbook.getAllEBooks();
       // } else {
       //     Log.d(LOG, "user is null");
       // }

    }

    public void walkdir(File dir) {
        String pdfPattern = ".pdf";

        File listFile[] = dir.listFiles();
        filePathList = new ArrayList<>();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++)  {
                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                    if (listFile[i].getName().contains(".pdf")) {
                        filePathList.add(listFile[i].getAbsolutePath());
                        Log.d(LOG, "FilePathList: " + filePathList.toString());
                    }

                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        filePathList.add(listFile[i].getAbsolutePath());
                        Log.d(LOG, "FileName: " + listFile[i].getName());

                        //
                        /*adapter = new EbookAdapter(filePathList, this, null, new EbookAdapter.EbookAdapterListener() {
                            @Override
                            public void onUploadEbook(String path) {
                                confirmUpload(path);
                            }

                            @Override
                            public void onReadEbook(String path) {
                                readEbook(path);
                            }

                            @Override
                            public void onPhotoUpload(BaseDTO base) {

                             //   pickGalleryOrCamera(base);
                            }

                            @Override
                            public void onAttachPhoto(EBookDTO ebook) {
                                //startPhotoGallerySelection(ebook);
                                pickGalleryOrCamera(ebook);
                            }
                        });*/

                        adapter = new EbookAdapter(filePathList, this, null, new EbookAdapter.EbookAdapterListener() {
                            @Override
                            public void onUploadEbook(String path) {
                                confirmUpload(path);
                            }

                            @Override
                            public void onReadEbook(String path) {
                                readEbook(path);
                            }

                            @Override
                            public void onAttachPhoto(EBookDTO ebook) {
                                pickGalleryOrCamera(ebook);
                            }

                            @Override
                            public void onPhotoUpload(BaseDTO base) {

                            }

                            @Override
                            public void onDeleteEbook(EBookDTO eBook) {
                                deletePresenter.deleteEbook(eBook);
                            }
                        });

                        recyclerView.setAdapter(adapter);


                    }
                }
            }
        }
    }

    BaseDTO base;
    public void getEbooksOnDevice() {

        File dir = Environment.getExternalStorageDirectory();

        File[] files = dir.listFiles();
        Log.d(LOG, "getEBooksOnDevice " + dir.getName());
        filePathList = new ArrayList<>();
        if (files != null) {
            for (File file: files) {
                if (file.getName().contains("dp.pdf")) {
                    filePathList.add(file.getAbsolutePath());
                }
            }
        }
        if (filePathList.isEmpty()) {
            Log.d(LOG, "file pathPathList is empty");
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
            adapter = new EbookAdapter(filePathList, this, null, new EbookAdapter.EbookAdapterListener() {
                @Override
                public void onUploadEbook(String path) {

                }

                @Override
                public void onReadEbook(String path) {

                }

                @Override
                public void onAttachPhoto(EBookDTO ebook) {

                }

                @Override
                public void onPhotoUpload(BaseDTO base) {

                }

                @Override
                public void onDeleteEbook(EBookDTO eBook) {
                    deletePresenter.deleteEbook(eBook);
                }
            });/*EbookAdapter.EbookAdapterListener() {
                @Override
                public void onUploadEbook(String path) {
                    confirmUpload(path);
                }

                @Override
                public void onReadEbook(String path) {
                    readEbook(path);
                }

                @Override
                public void onPhotoUpload(BaseDTO base) {
                    pickGalleryOrCamera(base);
                }

                @Override
                public void onAttachPhoto(EBookDTO ebook) {
                    //startPhotoGallerySelection(ebook);
                    pickGalleryOrCamera(ebook);
                }
            });*/
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "eBook to search: " + query);
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                ArrayList<String> searchResult = getSearchList(query);
                adapter.setPaths(searchResult);
                recyclerView.getAdapter().notifyDataSetChanged();

                adapter = new EbookAdapter(searchResult, EbookSelectionActivity.this, null/*eBook*/, new EbookAdapter.EbookAdapterListener() {
                    @Override
                    public void onUploadEbook(String path) {
                        confirmUpload(path);
                    }

                    @Override
                    public void onReadEbook(String path) {
                        readEbook(path);
                    }

                    @Override
                    public void onAttachPhoto(EBookDTO ebook) {

                    }

                    @Override
                    public void onPhotoUpload(BaseDTO base) {
                        pickGalleryOrCamera(base);
                    }

                    @Override
                    public void onDeleteEbook(EBookDTO eBook) {
                        deletePresenter.deleteEbook(eBook);
                    }

                });
                recyclerView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }

    private ArrayList<String> getSearchList(String word){
        ArrayList<String> list = new ArrayList<>();
        String path;
        for (int i = 0; i < filePathList.size(); i++){
            path = filePathList.get(i);
            if(Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(path).find()){
                list.add(filePathList.get(i));
            }
        }
        return  list;
    }


    /*private void pickGalleryOrCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
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
    } */

    private void pickGalleryOrCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(this/*ctx*/);
        b.setTitle("Select Images")
                .setMessage("Please select the source of the photos")
                .setPositiveButton("Use Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EbookSelectionActivity.this, PhotoActivity.class);
                        eBook = (EBookDTO) base;
                        intent.putExtra("eBook", eBook);
                        startActivity(intent);
                    }
                }).show();
    }

    private void startPhotoGallerySelection(BaseDTO base){
        Intent intent = new Intent(this, PhotoSelectionActivity.class);
        intent.putExtra("type", type);
        switch (type) {
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) base;
                intent.putExtra("eBook", eBook);
                break;
        }
        startActivity(intent);
    }

    public ArrayList<String> getFilePaths()
    {


        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {

                    if(imagePath.isDirectory())
                    {
                        imageList = imagePath.listFiles();

                    }
                    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
                            )
                    {
                        String path= imagePath.getAbsolutePath();
                        resultIAV.add(path);

                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //
            PhotoAdapter adapter = new PhotoAdapter(resultIAV, this, new PhotoAdapter.PhotoAdapterListener() {
                @Override
                public void onUploadPhoto(String path, int position) {
                    confirmPhotoUpload(path);
                }

                @Override
                public void onViewPhoto(String path) {
                    //viewPhoto(path);
                }

            });

            recyclerView.setAdapter(adapter);
            //
        }

        return resultIAV;


    }

    private void confirmPhotoUpload(final String path) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to upload this photo to the database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadPhoto(path);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }





    List<FileContainer> fileContainerList = new ArrayList<>();



    @Override
    public void onEBookTapped(EBookDTO eBook) {

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

    boolean isTooltip;

    @Override
    public void onPhotoCaptureRequested(BaseDTO entity) {
        Log.w(TAG, "onPhotoCaptureRequested: .................");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        pickGalleryOrCamera(entity);
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

    List<LocalEbook> localEbooks = new ArrayList<>();
    class LocalEbook {
        long duration;
        String path;

        public LocalEbook(long duration, String path) {
            this.duration = duration;
            this.path = path;
        }
    }

    static final String LOG = EbookSelectionActivity.class.getSimpleName();
    FragmentManager fm;

    private void setup() {
        nameTxt = (TextView) findViewById(R.id.nameTxt);
        nameTxt.setText("EBook Selection & Upload");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));
        books = (ImageView) findViewById(R.id.books);

        booksimg = (ImageView) findViewById(R.id.booksimg);
        booksimg.setColorFilter(ContextCompat.getColor(EbookSelectionActivity.this,R.color.black));
        books.setColorFilter(ContextCompat.getColor(EbookSelectionActivity.this,R.color.green_500));
        booksimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recyclerView.setVisibility(View.GONE);
                booksimg.setColorFilter(ContextCompat.getColor(EbookSelectionActivity.this,R.color.green_500));
                books.setColorFilter(ContextCompat.getColor(EbookSelectionActivity.this,R.color.black));
                adapter.setServerList(true);
                adapter.setPaths(serverList);
                adapter.setmList(eBooks);
              //  listView.deferNotifyDataSetChanged();
                recyclerView.getAdapter().notifyDataSetChanged();
                //isServerList = true;

            }
        });
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recyclerView.setVisibility(View.GONE);
                booksimg.setColorFilter(ContextCompat.getColor(EbookSelectionActivity.this,R.color.black));
                books.setColorFilter(ContextCompat.getColor(EbookSelectionActivity.this,R.color.green_500));
                adapter.setPaths(serverList);
                recyclerView.getAdapter().notifyDataSetChanged();
                adapter.setServerList(false);
                walkdir(Environment.getExternalStorageDirectory());

            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void confirmUpload(final String path) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to upload this ebook to the database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadEbook(path);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void readEbook(String path) {

        File f = new File(path);
        if (f.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            Uri apkURI = FileProvider.getUriForFile(
                    ctx,
                    ctx.getApplicationContext()
                            .getPackageName() + ".provider", f);
            intent.setDataAndType(apkURI, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
    }



    private void uploadEbook(String path) {
        showSnackbar("Uploading ebook ...", "OK", Constants.CYAN);
        EBookDTO v = new EBookDTO();
        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
       // v.setCompanyName(u.getCompanyName());
       // v.setCompanyID(u.getCompanyID());
        v.setFilePath(path);
        File file = new File(path);
        v.setEbookSize(file.length());
        v.setActive(true);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                v.setDailyThoughtID(dailyThought.getDailyThoughtID());
                v.setTitle(dailyThought.getTitle());
                v.setDescription(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                v.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
                v.setTitle(weeklyMasterClass.getTitle());
                v.setDescription(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                v.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                v.setTitle(weeklyMessage.getTitle());
                v.setDescription(weeklyMessage.getTitle());
                break;
            case ResponseBag.PODCASTS:
                v.setPodcast(podcast);
               // v.setPodcastID(podcast.getPodcastID());
                break;
            case  ResponseBag.VIDEOS:
                v.setVideo(video);
              //  v.setVideoID(video.getVideoID());
                break;
            case ResponseBag.URLS:
                v.setUrlDTO(url);
                break;
        }
        //openProgressSheet();
        presenter.uploadEbook(v);
    }

    private ProgressBottomSheet progressBottomSheet;
    private void openProgressSheet() {
        progressBottomSheet = ProgressBottomSheet.newInstance();
        progressBottomSheet.show(getSupportFragmentManager(),"PROGRESS_SHEET");
    }

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }



    private void uploadPhoto(String path) {
        showSnackbar("Uploading photo ...", "OK", Constants.CYAN);
        PhotoDTO p = new PhotoDTO();

        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
        // v.setCompanyName(u.getCompanyName());
        // v.setCompanyID(u.getCompanyID());
        p.setFilePath(path);
        File file = new File(path);
        p.setImageSize(file.length());
        p.setBytes(file.length());
        p.setCaption("Leadership & Motivation");
        switch (type) {
            case ResponseBag.EBOOKS:
                p.seteBookID(eBook.geteBookID());
                p.setTitle(eBook.getStorageName());
                break;
        }
        //openProgressSheet();
        photoPresenter.uploadPhoto(p);
    }

    private List<String> getEbookUrl(List<EBookDTO> list){
        List<String> urlList = new ArrayList<>();
        EBookDTO eBookDTO;
        String eBookUrl = null;
        for(int i = 0; i < list.size(); i++ ){
            eBookDTO = list.get(i);
            urlList.add(eBookDTO.getCoverUrl());
        }
        return urlList;
    }

    @Override
    public void onEbookUpdated(String key) {

    }

    @Override
    public void onEbookUploaded(String key) {
        showSnackbar("Book successfully uploaded ...", "OK", Constants.CYAN);
    }

    @Override
    public void onPhotoUploaded(String key) {
        showSnackbar("Photo successfully uploaded ...", "OK", Constants.CYAN);
    }

    @Override
    public void onPhotoUserUploaded(String key) {

    }

    @Override
    public void onProgress(long transferred, long size) {

    }

    @Override
    public void onUserDeleted(UserDTO user) {

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
    public void onError(String message) {

    }

    @Override
    public void onAllEBooks(List<EBookDTO> list) {
        Log.i(LOG, "onEbooks: " + list.size());
        this.eBooks = list;
        serverList = getEbookUrl(list);
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
    public void onCompanyFound(CompanyDTO company) {

    }


}
