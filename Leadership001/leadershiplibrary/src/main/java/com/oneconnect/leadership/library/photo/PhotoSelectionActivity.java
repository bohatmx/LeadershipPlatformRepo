package com.oneconnect.leadership.library.photo;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.audio.PodcastAdapter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.data.PodcastDTO;
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

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class PhotoSelectionActivity extends AppCompatActivity implements PhotoUploadContract.View, EbookUploadContract.View{


    private RecyclerView recyclerView;
    private int type;
    private PhotoUploadPresenter presenter;

    private EbookUploadPresenter eBookpresenter;
    private Toolbar toolbar;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private EBookDTO eBook;
    private PodcastDTO podcast;
    private VideoDTO video;
    private UrlDTO url;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private Snackbar snackbar;
    public static final int PHOTO_REQUEST = 100000;
    SearchView searchView = null;
    ArrayList<String> resultIAV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photo Selection & Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new PhotoUploadPresenter(this);
        //remove if it doesnt work
        eBookpresenter = new EbookUploadPresenter(this);

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



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);


       // setUp();
        //walkdir(Environment.getExternalStorageDirectory());
        getFilePaths();
    }

    private void setUp() {


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
                    confirmUpload(path);
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
                ArrayList<String> searchResult = getSearchList(query);
                PhotoAdapter adapter = new PhotoAdapter(searchResult, PhotoSelectionActivity.this, new PhotoAdapter.PhotoAdapterListener() {
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

    private void confirmUpload(final String path) {
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
            case ResponseBag.PODCASTS:
                p.setPodcast(podcast);
                break;
            case ResponseBag.VIDEOS:
                p.setVideo(video);
                break;
            case ResponseBag.URLS:
                p.setUrlDTO(url);
                break;
        }
        if (type == ResponseBag.EBOOKS) {
            eBookpresenter.uploadEbook(eBook);
            return;
        }
        //openProgressSheet();
        presenter.uploadPhoto(p);
    }

    private File photoFile,  currentThumbFile;
    private List<PhotoDTO> photos = new ArrayList<>();


    private void savePhotoFile(Uri uri) {
        photoFile = new File(uri.getPath());
        PhotoDTO p = new PhotoDTO();
        p.setFilePath(photoFile.getAbsolutePath());
        UserDTO u = SharedPrefUtil.getUser(this);
        p.setJournalUserName(u.getFullName());
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
    public void onError(String message) {

    }

    static final String LOG = PhotoSelectionActivity.class.getSimpleName();
}
