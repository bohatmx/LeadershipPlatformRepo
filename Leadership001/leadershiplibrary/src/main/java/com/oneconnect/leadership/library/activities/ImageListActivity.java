package com.oneconnect.leadership.library.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.ImageAdapter;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.ebook.EbookUploadContract;
import com.oneconnect.leadership.library.ebook.EbookUploadPresenter;
import com.oneconnect.leadership.library.photo.PhotoAdapter;
import com.oneconnect.leadership.library.photo.PhotoDownloadContract;
import com.oneconnect.leadership.library.photo.PhotoDownloadPresenter;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.photo.PhotoUploadContract;
import com.oneconnect.leadership.library.photo.PhotoUploadPresenter;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class ImageListActivity extends AppCompatActivity implements PhotoUploadContract.View, EbookUploadContract.View, PhotoDownloadContract.View,
        ActionMode.Callback{

    ListView imageListView;
    Context ctx;

    ImageAdapter adapter;
    private PhotoUploadPresenter presenter;
    private PhotoDownloadPresenter presenterPhotoDownload;
    private EbookUploadPresenter eBookpresenter;
    private int type;
    FirebaseStorageAPI fbs;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private EBookDTO eBook;
    private NewsDTO news;
    private VideoDTO video;
    private UrlDTO url;
    private PodcastDTO podcast;
    boolean isServerList;
    Toolbar toolbar;
    List<PhotoDTO> photoDTOs;
    private Snackbar snackbar;
    public int selectedItem = -1;
    private Object mActionMode;
    TextView txtUpload, txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        ctx = getApplicationContext();

        /*getSupportActionBar().setTitle("Image Selector");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        presenter = new PhotoUploadPresenter(this);
        presenterPhotoDownload = new PhotoDownloadPresenter(this);
        fbs = new FirebaseStorageAPI();
        //remove if it doesnt work
        eBookpresenter = new EbookUploadPresenter(this);

        type = getIntent().getIntExtra("type", 0/*type*/);

        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
         //       getSupportActionBar().setSubtitle(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");
          //      getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
         //       getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
                break;
            //testing
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
         //       getSupportActionBar().setSubtitle(eBook.getStorageName());
                break;
            case ResponseBag.NEWS:
                news = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
         //       getSupportActionBar().setSubtitle(news.getTitle());
                break;
        }
        if (getIntent().getSerializableExtra("eBook") != null) {
            type = 3;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
         //   getSupportActionBar().setSubtitle(eBook.getStorageName());
        }
        if (getIntent().getSerializableExtra("podcast") != null) {
            type = ResponseBag.PODCASTS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
        //    getSupportActionBar().setSubtitle(podcast.getStorageName());
        }
        if (getIntent().getSerializableExtra("video") != null) {
            type = ResponseBag.VIDEOS;
            video = (VideoDTO) getIntent().getSerializableExtra("video");
       //     getSupportActionBar().setSubtitle(video.getStorageName());
        }
        if (getIntent().getSerializableExtra("url") != null) {
            type = ResponseBag.URLS;
            url = (UrlDTO) getIntent().getSerializableExtra("url");
         //   getSupportActionBar().setSubtitle(url.getTitle());
        }
        if (getIntent().getSerializableExtra("newsArticle") != null) {
            type = ResponseBag.NEWS;
            news = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
       //     getSupportActionBar().setSubtitle(news.getTitle());
        }


       /* imageListView = getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode != null) {
                    return false;
                }

                selectedItem = position;


                mActionMode = ImageListActivity.this.startActionMode(ImageListActivity.this);
                view.setSelected(true);
                return true;
            }
        });*/
       // imageListView = (ListView) findViewById(R.id.imageListView);
       /* imageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode != null) {
                    return false;
                }

                selectedItem = position;


                mActionMode = *//*ImageListActivity.this.*//*startActionMode(ImageListActivity.this);

                return true;
            }
        });*/

        imageListView = (ListView) findViewById(R.id.imageListView);
   //     imageListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        txtUpload = (TextView) findViewById(R.id.txtUpload);
        txtCount = (TextView) findViewById(R.id.txtCount);


        getFilePaths();

    }

    private void uploadSelectedImages(String path, List<String> selectedimages){
        Log.i(LOG, "******* uploadSelectedImages ****");

        PhotoDTO p = new PhotoDTO();

        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
        for (String path1 : selectedimages) {
            path1 = path;
           // showSnackbar("Uploading photo ...", "OK", Constants.CYAN);

            // v.setCompanyName(u.getCompanyName());
            // v.setCompanyID(u.getCompanyID());
            p.setFilePath(path1);
            File file = new File(path1);
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
            //openProgressSheet();
            presenter.uploadPhoto(p);
        }


        /*imageListView.getCheckedItemCount();
        if (mActionMode != null) {
            *//*return false;*//*
        }
        Log.i(LOG, "checked images: " + imageListView.getCheckedItemCount());*/

    }


    ArrayList<String> resultIAV;
    List<String> selectedimages;
    boolean isChecked;
    int checkAccumulator = 0;

    public void countCheck(boolean isChecked) {
        checkAccumulator += isChecked ? 1 : -1;
    }

    public ArrayList<String> getFilePaths()
    {
        Log.i(LOG, "******getFilePaths********");


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
            adapter = new ImageAdapter(/*ctx*/this, /*R.layout.photo_item, */resultIAV,  new ImageAdapter.ImageAdapterListener() {
                @Override
                public void onUploadPhoto(String path, int pos) {
                    confirmUpload(path, pos);
                }

                @Override
                public void onViewPhoto(String path) {
                    //viewPhoto(path);
                }

                @Override
                public void onCheckedItem(String path, int numberChecked) {
                    selectedimages = new ArrayList<>();
                    Log.i(LOG, numberChecked + "" + "\n" +"Image path: " + path);
                    txtCount.setText(/*checkAccumulator*/numberChecked + "");
                    //imageListView.getCheckedItemCount();
                  //  String p;
                    for(String p : selectedimages) {
                        p = path;
                        uploadSelectedImages(p, selectedimages);
                    }
                    txtUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Util.flashOnce(txtUpload, 300, new Util.UtilAnimationListener() {
                                @Override
                                public void onAnimationEnded() {

                                }
                            });

                        }
                    });
                }


            });

            imageListView.setAdapter(adapter);

          //  uploadSelectedImages();

            ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, resultIAV);
           // setListAdapter(adapt);
          //  getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            //
        }

        return resultIAV;
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

    ArrayList<String> searchResult;
    SearchView searchView = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                adapter = new ImageAdapter(ImageListActivity.this, searchResult, /*resultIAV*/ new ImageAdapter.ImageAdapterListener() {
                    @Override
                    public void onUploadPhoto(String path, int pos) {
                        confirmUpload(path, pos);
                    }

                    @Override
                    public void onViewPhoto(String path) {
                        //viewPhoto(path);
                    }

                    @Override
                    public void onCheckedItem(String path, int numberChecked) {

                    }

                });

                imageListView.setAdapter(adapter);
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
        //openProgressSheet();
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

    static final String LOG = ImageListActivity.class.getSimpleName();

    @Override
    public void onPhotoUploaded(String key) {

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
    public void onError(String message) {

    }

    @Override
    public void onAllPhotos(List<PhotoDTO> list) {

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



    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.rowselection, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        show();
        mode.finish();
       // item = mode.findItem( R.id.menuitem1_show);
       /* switch (item.getItemId()) {
            final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
            case R.id.menuitem1_show:
                show();
                mode.finish();
                return true;
            default:
                return false;
        }*/
       return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        selectedItem = -1;
    }

    private void show() {
        Toast.makeText(ImageListActivity.this, String.valueOf(selectedItem), Toast.LENGTH_LONG).show();
    }
}
