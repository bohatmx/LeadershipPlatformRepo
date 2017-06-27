package com.oneconnect.leadership.library.ebook;

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

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.audio.PodcastAdapter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
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
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;



public class EbookSelectionActivity extends AppCompatActivity implements EbookUploadContract.View, PhotoUploadContract.View,
        EbookListFragment.EBookListener, BasicEntityAdapter.EntityListener{

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
    private PhotoUploadPresenter photoPresenter;
    private EBookDTO eBook;
    public static final String TAG = EbookSelectionActivity.class.getSimpleName();
   // private EbookListFragment eBookListFragment;

    ImageView books, booksimg;
    private Toolbar toolbar;
    Context ctx;
    SearchView searchView = null;
    List<String> filePathList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EBook Selection & Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        presenter = new EbookUploadPresenter(this);
        photoPresenter = new PhotoUploadPresenter(this);

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
       // getEbooksOnDevice();
        walkdir(Environment.getExternalStorageDirectory());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
                        EbookAdapter adapter = new EbookAdapter(filePathList, this, eBook, new EbookAdapter.EbookAdapterListener() {
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

                           /* @Override
                            public void onPhotoUpload(String path) {

                                AlertDialog.Builder b = new AlertDialog.Builder(ctx);
                                b.setTitle("Select Images")
                                        .setMessage("Please select the source of the photos")
                                        .*//*setPositiveButton("Use Camera", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // startCamera(base);
                                            }
                                        })*//*setNegativeButton("Use Gallery", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                        // startActivityForResult(intent, RESULT_LOAD_IMG);
                                        startPhotoGallerySelection(base);

                                    }
                                }).show();
                                //pickGalleryOrCamera(base);
                            }*/
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
            EbookAdapter adapter = new EbookAdapter(filePathList, this, eBook, new EbookAdapter.EbookAdapterListener() {
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

                /*@Override
                public void onPhotoUpload(String path) {
                    pickGalleryOrCamera(base);
                    //startActivity(base);
                   // getFilePaths();
                    //confirmPhotoUpload();
                   // uploadPhoto();
                }*/
            });

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
                EbookAdapter adapter = new EbookAdapter(searchResult, EbookSelectionActivity.this, eBook, new EbookAdapter.EbookAdapterListener() {
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


    private void pickGalleryOrCamera(final BaseDTO base) {
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
                public void onUploadPhoto(String path) {
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(ctx, 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        books = (ImageView) findViewById(R.id.books);

        booksimg = (ImageView) findViewById(R.id.booksimg);
        booksimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                Intent intent = new Intent(EbookSelectionActivity.this, EbookListActivity.class);
                startActivity(intent);
                /*eBookListFragment.getView().setVisibility(View.VISIBLE);*/

                //fm.beginTransaction().show(eBookListFragment).commit();
                //ft.show(eBookListFragment);
               // eBookListFragment.isVisible();
               // eBookListFragment = (EbookListFragment) getSupportFragmentManager().findFragmentById(R.id.ebookFragment);
                //eBook = (EBookDTO) getIntent().getSerializableExtra("ebook");
            }
        });

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                /*eBookListFragment.getView().setVisibility(View.GONE);*/
                //eBookListFragment.setUserVisibleHint(false);
                // fm = getSupportFragmentManager();
                //fm.beginTransaction().remove(eBookListFragment).commit();
                //ft.hide(eBookListFragment);
                // eBookListFragment.isHidden();
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

                    }
                })
                .show();
    }

    private void readEbook(String path) {

        File f = new File(path);
        if (f.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(f), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    @Override
    public void onEbookUpdated(String key) {

    }

    @Override
    public void onEbookUploaded(String key) {

    }

    @Override
    public void onPhotoUploaded(String key) {

    }

    @Override
    public void onProgress(long transferred, long size) {

    }

    @Override
    public void onError(String message) {

    }
}
