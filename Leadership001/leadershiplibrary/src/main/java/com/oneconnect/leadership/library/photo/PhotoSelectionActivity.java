package com.oneconnect.leadership.library.photo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.audio.PodcastAdapter;
import com.oneconnect.leadership.library.audio.PodcastListActivity;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.crud.CrudContract;
import com.oneconnect.leadership.library.crud.CrudPresenter;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.ebook.EbookUploadContract;
import com.oneconnect.leadership.library.ebook.EbookUploadPresenter;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class PhotoSelectionActivity extends AppCompatActivity implements PhotoUploadContract.View, EbookUploadContract.View, PhotoDownloadContract.View, CrudContract.View,SubscriberContract.View{


    private RecyclerView recyclerView;
    private int type;
    private PhotoUploadPresenter presenter;
    private PhotoDownloadPresenter presenterPhotoDownload;
    private CrudPresenter crudPresenter;
    private EbookUploadPresenter eBookpresenter;
    private Toolbar toolbar;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private EBookDTO eBook;
    private PodcastDTO podcast;
    private NewsDTO news;
    private UserDTO user;
    private CompanyDTO company;
    private VideoDTO video;
    private UrlDTO url;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private Snackbar snackbar;
    public static final int PHOTO_REQUEST = 100000;
    SearchView searchView = null;
    ArrayList<String> resultIAV;
    private ListAPI listAPI;
    public List<String> serverList;
    ImageView galleryImage, serverImage;
    TextView noPhotoTxt;
    ArrayList<String> searchResult;
    PhotoAdapter adapter;
    FirebaseStorageAPI fbs;
    List<PhotoDTO> photoDTOs;
    boolean isServerList;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photo Selection & Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new PhotoUploadPresenter(this);
        presenterPhotoDownload = new PhotoDownloadPresenter(this);
        fbs = new FirebaseStorageAPI();
        user = SharedPrefUtil.getUser(ctx);
        subscriberPresenter = new SubscriberPresenter(this);
        eBookpresenter = new EbookUploadPresenter(this);
        crudPresenter = new CrudPresenter(this);

        type = getIntent().getIntExtra("type", 0/*type*/);

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
            //testing
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
                getSupportActionBar().setSubtitle(eBook.getStorageName());
                break;
            case ResponseBag.NEWS:
                news = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
                getSupportActionBar().setSubtitle(news.getTitle());
                break;
            case ResponseBag.USERS:
                user = (UserDTO) getIntent().getSerializableExtra("user");
                getSupportActionBar().setSubtitle(user.getFirstName());
                break;
            case ResponseBag.COMPANIES:
                company = (CompanyDTO) getIntent().getSerializableExtra("company");
                getSupportActionBar().setSubtitle(company.getCompanyLogoUrl());
                break;
        }
        if (getIntent().getSerializableExtra("eBook") != null) {
            type = 3;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
            getSupportActionBar().setSubtitle(eBook.getStorageName());
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
        if (getIntent().getSerializableExtra("newsArticle") != null) {
            type = ResponseBag.NEWS;
            news = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
            getSupportActionBar().setSubtitle(news.getTitle());
        }
        if (getIntent().getSerializableExtra("user") != null) {
            type = ResponseBag.USERS;
            user = (UserDTO) getIntent().getSerializableExtra("user");
            getSupportActionBar().setSubtitle(user.getFirstName());
        }
        if (getIntent().getSerializableExtra("company") != null) {
            type = ResponseBag.COMPANIES;
            company = (CompanyDTO) getIntent().getSerializableExtra("company");
            getSupportActionBar().setSubtitle(company.getCompanyName());
        }
        if (getIntent().getSerializableExtra("dailyThought") != null) {
            type = ResponseBag.DAILY_THOUGHTS;
            dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
            getSupportActionBar().setSubtitle(dailyThought.getTitle());
        }
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        noPhotoTxt = (TextView) findViewById(R.id.noPhotoTxt);
        noPhotoTxt.setVisibility(View.GONE);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        serverImage = (ImageView) findViewById(R.id.booksimg);
        galleryImage = (ImageView) findViewById(R.id.books);
        galleryImage.setColorFilter(ContextCompat.getColor(PhotoSelectionActivity.this,R.color.green_500));
        serverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverImage.setColorFilter(ContextCompat.getColor(PhotoSelectionActivity.this,R.color.green_500));
                galleryImage.setColorFilter(ContextCompat.getColor(PhotoSelectionActivity.this,R.color.black));
                adapter.setPaths(serverList);
                recyclerView.getAdapter().notifyDataSetChanged();
                isServerList = true;
            }
        });
        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverImage.setColorFilter(ContextCompat.getColor(PhotoSelectionActivity.this,R.color.black));
                galleryImage.setColorFilter(ContextCompat.getColor(PhotoSelectionActivity.this,R.color.green_500));
                getFilePaths();
            }
        });

       setUp();
        //walkdir(Environment.getExternalStorageDirectory());
        //getFilePaths();
        getAllPhotos();
    }

    private void setUp() {

        ActivityCompat.requestPermissions(PhotoSelectionActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                   //a sample method called
                    getFilePaths();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(PhotoSelectionActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }

    /*public void walkdir(File dir) {
        String pdfPattern = ".jpg";

        File listFile[] = dir.listFiles();
        filePathList = new ArrayList<>();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++)  {
                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                    if (listFile[i].getName().contains(".jpg")|| listFile[i].getName().contains(".JPG")
                            || listFile[i].getName().contains(".jpeg")|| listFile[i].getName().contains(".JPEG")
                            || listFile[i].getName().contains(".png") || listFile[i].getName().contains(".PNG")
                            || listFile[i].getName().contains(".gif") || listFile[i].getName().contains(".GIF")
                            || listFile[i].getName().contains(".bmp") || listFile[i].getName().contains(".BMP")) {
                        filePathList.add(listFile[i].getAbsolutePath());
                        Log.d(LOG, "FilePathList: " + filePathList.toString());
                    }

                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        filePathList.add(listFile[i].getAbsolutePath());
                        Log.d(LOG, "FileName: " + listFile[i].getName());

                        //
                        PhotoAdapter adapter = new PhotoAdapter(filePathList, this, new PhotoAdapter.PhotoAdapterListener() {
                            @Override
                            public void onUploadPhoto(String path) {
                                confirmUpload(path);
                            }

                            @Override
                            public void onViewPhoto(String path) {
                                //viewPhoto(path);
                            }

                        });

                        recyclerView.setAdapter(adapter);


                    }
                }
            }
        }
    }*/

    public ArrayList<String> getFilePaths()
    {


        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            noPhotoTxt.setVisibility(View.GONE);
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

        } else {
            Log.e(LOG, "**** no photo/s found on device and we not crashing ****");
            noPhotoTxt.setVisibility(View.VISIBLE);
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
            adapter = new PhotoAdapter(resultIAV, this, new PhotoAdapter.PhotoAdapterListener() {
                @Override
                public void onUploadPhoto(String path, int pos) {
                    confirmUpload(path, pos);
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


    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                searchResult = getSearchList(query);
                adapter = new PhotoAdapter(searchResult, PhotoSelectionActivity.this, new PhotoAdapter.PhotoAdapterListener() {
                    @Override
                    public void onUploadPhoto(String path, int pos) {
                        confirmUpload(path, pos);
                    }

                    @Override
                    public void onViewPhoto(String path) {
                        //viewPhoto(path);
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
        for (int i = 0; i < resultIAV.size(); i++){
            path = resultIAV.get(i);
            if(Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(path).find()){
                list.add(resultIAV.get(i));
            }
        }
        return  list;
    }
    List<String> filePathList;

    private void confirmUpload(final String path, final int pos) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to upload this photo to the database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isServerList){
                            addExistingPhoto(pos);
                        }else{
                            uploadPhoto(path);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private SubscriberPresenter subscriberPresenter;
    private FirebaseAuth firebaseAuth;
    private void uploadPhoto(String path) {
        showSnackbar("Uploading photo ...", "OK", Constants.CYAN);
        PhotoDTO p = new PhotoDTO();

        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
        subscriberPresenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
        p.setUserName(u.getFullName());
        p.setUserID(u.getUserID());
        p.setFilePath(path);
        File file = new File(path);
        p.setImageSize(file.length());
        p.setBytes(file.length());
        if (file.getName() != null) {
            p.setCaption(file.getName());
        } else {
            p.setCaption("Leadership & Motivation");
        }
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                p.setDailyThoughtID(dailyThought.getDailyThoughtID());
                p.setTitle(dailyThought.getTitle());
               // p.setDescription(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                p.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
                p.setTitle(weeklyMasterClass.getTitle());
                //p.setDescription(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                p.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                p.setTitle(weeklyMessage.getTitle());
                //p.setDescription(weeklyMessage.getTitle());
                break;
            case ResponseBag.EBOOKS:
                p.seteBookID(eBook.geteBookID());
                p.setTitle(eBook.getStorageName());
                eBook.setPhotoUrl(p.getUrl());
                eBook.setPhotoID(p.getPhotoID());
                break;
            case ResponseBag.USERS:
                p.setUserID(user.getUserID());
                p.setTitle(user.getFirstName());
                p.setPhotoID(p.getPhotoID());
                p.setTitle(user.getFilePath());
                break;
            case ResponseBag.PODCASTS:
                p.setPodcast(podcast);
                break;
            case ResponseBag.VIDEOS:
                p.setVideo(video);
                break;
            case ResponseBag.URLS:
                p.setUrlDTO(url);
                break;
            case ResponseBag.NEWS:
                p.setNewsID(news.getNewsID());
                p.setTitle(news.getTitle());
                //p.setCaption(news.getBody());
                break;
           /* case ResponseBag.COMPANIES:
                p.setCompany(company);
               // p.setCompanyID(company.getCompanyID());
             //   p.setTitle(company.getCompanyName());

                //p.setCaption(news.getBody());
                break;*/
        }
        if (type == 0) {
            if (type == ResponseBag.EBOOKS) {
                eBookpresenter.uploadEbook(eBook);
                return;
            }
            if (type == ResponseBag.USERS) {
                p.setUserID(user.getUserID());
                presenter.uploadPhoto(p);
                return;
            }
            if (type == ResponseBag.COMPANIES) {
                p.setCompanyID(company.getCompanyID());
                fbs.uploadPhoto(p, new FirebaseStorageAPI.StorageListener() {
                    @Override
                    public void onResponse(String key) {

                    }

                    @Override
                    public void onProgress(long transferred, long size) {

                    }

                    @Override
                    public void onError(String message) {
                        showSnackbar(message, "Dismiss", "red");
                    }
                });
            }
        }

        presenter.uploadPhoto(p);
    }

    private void addExistingPhoto(int pos) {
        showSnackbar("Uploading photo ...", "OK", Constants.CYAN);
        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
        PhotoDTO p = photoDTOs.get(pos);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                p.setDailyThoughtID(dailyThought.getDailyThoughtID());
                p.setTitle(dailyThought.getTitle());
                // p.setDescription(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                p.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
                p.setTitle(weeklyMasterClass.getTitle());
                //p.setDescription(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                p.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                p.setTitle(weeklyMessage.getTitle());
                //p.setDescription(weeklyMessage.getTitle());
                break;
            case ResponseBag.USERS:
                p.setUserID(user.getUserID());
                p.setTitle(user.getFirstName());
                break;
            case ResponseBag.EBOOKS:
                p.seteBookID(eBook.geteBookID());
                p.setTitle(eBook.getStorageName());
                eBook.setPhotoUrl(p.getUrl());
                eBook.setPhotoID(p.getPhotoID());
                break;
            case ResponseBag.PODCASTS:
                p.setPodcast(podcast);
                break;
            case ResponseBag.VIDEOS:
                p.setVideo(video);
                break;
            case ResponseBag.URLS:
                p.setUrlDTO(url);
                break;
            case ResponseBag.NEWS:
                p.setNewsID(news.getNewsID());
                p.setTitle(news.getTitle());
                //p.setCaption(news.getBody());
                break;
        }
        if (type == ResponseBag.EBOOKS) {
            eBookpresenter.uploadEbook(eBook);
            return;
        }

        if (type == ResponseBag.USERS) {
            presenter.uploadUserPhoto(user);
            return;
        }
        fbs.addExistingPhotoToFirebase(p, new FirebaseStorageAPI.StorageListener() {
            @Override
            public void onResponse(String key) {
                //view.onPhotoUploaded(key);
            }

            @Override
            public void onProgress(long transferred, long size) {
                //view.onProgress(transferred, size);
            }

            @Override
            public void onError(String message) {
                //view.onError(message);
            }
        });
    }

    private void getAllPhotos() {
        Log.d(LOG, "************** getAllPhotos: " );
        presenterPhotoDownload.getAllPhotos();
    }

    private File photoFile,  currentThumbFile;
    private List<PhotoDTO> photos = new ArrayList<>();


    private void savePhotoFile(Uri uri) {
        photoFile = new File(uri.getPath());
        PhotoDTO p = new PhotoDTO();
        p.setFilePath(photoFile.getAbsolutePath());
        UserDTO u = SharedPrefUtil.getUser(this);
        p.setJournalUserName(u.getFirstName() + "" + u.getLastName());
        p.setJournalUserID(u.getUserID());
        p.setCompanyID(u.getCompanyID());
        p.setCompanyName(u.getCompanyName());
        p.setImageSize(photoFile.length());
        p.setBytes(photoFile.length());
        photos.add(p);
        Log.i(LOG, "onActivityResult: photoFile: ".concat(getSize(photoFile.length())));
        Toasty.success(this,"Photo taken OK", Toast.LENGTH_SHORT).show();
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

    private static final DecimalFormat df = new DecimalFormat("###,###,###,###,##0.00");

    private String getSize(long num) {
        BigDecimal d = new BigDecimal(num).divide(new BigDecimal(1024*1024));
        return df.format(d.doubleValue()) + " MB";
    }

    @Override
    public void onPhotoUploaded(String key) {
        Log.i(LOG, "onPhotoUploaded: .................. ".concat(key));
        showSnackbar("Photo".concat(" ADDED."), "OK", "green");
    }

    @Override
    public void onPhotoUserUploaded(String key) {

    }

    @Override
    public void onEbookUpdated(String key) {

    }

    @Override
    public void onEbookUploaded(String key) {

    }

    @Override
    public void onProgress(long transferred, long size) {

    }

    @Override
    public void onAllPhotos(List<PhotoDTO> list) {
        photoDTOs = list;
        serverList = getThumbNailUrl(list);
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
        Log.i(LOG, "*** onUserFound ***" + user.getFullName() + "\n" + "fetching user photos");
        subscriberPresenter.getPhotos(user.getCompanyID());
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

    private List<String> getThumbNailUrl(List<PhotoDTO> photoDTOs){
        List<String> urlList = new ArrayList<>();
        PhotoDTO photoDTO;
        String photoUrl = null;
        for(int i = 0; i < photoDTOs.size(); i++ ){
            photoDTO = photoDTOs.get(i);
            if(photoDTO.getThumbNailUrl() != null){
                photoUrl = photoDTO.getThumbNailUrl();
            }else{
                photoUrl = photoDTO.getUrl();
            }
            urlList.add(photoUrl);
        }
      return urlList;
    }

    static final String LOG = PhotoSelectionActivity.class.getSimpleName();
}
