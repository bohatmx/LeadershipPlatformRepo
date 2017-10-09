package com.oneconnect.leadership.library.camera;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.ProgressBottomSheet;
import com.oneconnect.leadership.library.audio.PodcastListActivity;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.ebook.EbookAdapter;
import com.oneconnect.leadership.library.ebook.EbookSelectionActivity;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;
import com.oneconnect.leadership.library.video.LeExoPlayerActivity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;


public class VideoSelectionActivity extends AppCompatActivity implements VideoUploadContract.View {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private EBookDTO eBook;
    private PodcastDTO podcast;
    private UrlDTO url;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private int type;
    private Snackbar snackbar;
    private VideoUploadPresenter presenter;
    public static final String TAG = VideoSelectionActivity.class.getSimpleName();
    ImageView image1, image2;
    SearchView searchView = null;
    ArrayList<String> downloadedList;
    public static final int PERMISSIONS_REQUEST = 113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Video Selection & Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new VideoUploadPresenter(this);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);

        check();



        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
                getSupportActionBar().setSubtitle(dailyThought.getTitle());
                image2.setVisibility(View.GONE);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");
                getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
                image2.setVisibility(View.GONE);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
                getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
                image2.setVisibility(View.GONE);
                break;
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
                getSupportActionBar().setSubtitle(eBook.getStorageName());
                image2.setVisibility(View.GONE);
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
        if (getIntent().getSerializableExtra("eBook") != null) {
            type = ResponseBag.EBOOKS;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
            getSupportActionBar().setSubtitle(eBook.getStorageName());
        }
        if (getIntent().getSerializableExtra("podcast") != null) {
            type = ResponseBag.PODCASTS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
            getSupportActionBar().setSubtitle(podcast.getStorageName());
        }
        if (getIntent().getSerializableExtra("url") != null) {
            type = ResponseBag.URLS;
            url = (UrlDTO) getIntent().getSerializableExtra("url");
            getSupportActionBar().setSubtitle(url.getTitle());
        }
        if (getIntent().getSerializableExtra("dailyThought") != null) {
            type = ResponseBag.DAILY_THOUGHTS;
            dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
            getSupportActionBar().setSubtitle(dailyThought.getTitle());
        }

        setup();
        getVideosOnDevice();
    }

    private void check() {
        Log.w(TAG, "check: PERMISSIONS_REQUEST" );
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST);
            return;

        }
        // proceed();
    }

    private void setup() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        image2.setColorFilter(ContextCompat.getColor(VideoSelectionActivity.this,R.color.black));
        image1.setColorFilter(ContextCompat.getColor(VideoSelectionActivity.this,R.color.green_500));
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(image2, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(VideoSelectionActivity.this, VideoListActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });


    }

    public void getVideosOnDevice() {

        HashSet<String> videoItemHashSet = new HashSet<>();
        String[] projection = {
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Media.DISPLAY_NAME
        };
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
      //  if (cursor != null) {
            try {
                cursor.moveToFirst();
                do {

                    if (cursor != null) {
                        Log.d(TAG, "getVideosOnDevice: ".concat(cursor.getColumnNames().toString()));
                        if (cursor != null && cursor.moveToFirst()) {
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION));
                        Log.e(TAG, "getVideosOnDevice: duration: " + duration + " path: ".concat(path));
                        localVideos.add(new LocalVideo(duration, path));
                        videoItemHashSet.add(path);
                        } else {
                            Log.e(TAG, "**** no videos found on device and we not crashing ****");
                        }
                    }

                } while (cursor.moveToNext());

                cursor.close();
            } catch (Exception e) {
                Log.e(TAG, "getVideosOnDevice: ", e);
            }
            downloadedList = new ArrayList<>(videoItemHashSet);
            for (String id : downloadedList) {
                Log.e(TAG, "getVideosOnDevice: ".concat(id));
            }

            VideoAdapter adapter = new VideoAdapter(downloadedList, this, new VideoAdapter.VideoAdapterListener() {
                @Override
                public void onPlayVideoTapped(String path) {
                    playVideo(path);
                }

                @Override
                public void onUploadVideoTapped(String path) {
                    confirmUpload(path);

                }

            });
            recyclerView.setAdapter(adapter);
       // }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.menu_search, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "Video to search: " + query);
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                ArrayList<String> searchResult = getSearchList(query);
                VideoAdapter adapter = new VideoAdapter(searchResult, VideoSelectionActivity.this, new VideoAdapter.VideoAdapterListener() {
                    @Override
                    public void onPlayVideoTapped(String path) {
                        playVideo(path);
                    }

                    @Override
                    public void onUploadVideoTapped(String path) {
                        confirmUpload(path);

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
        for (int i = 0; i < downloadedList.size(); i++){
            path = downloadedList.get(i);
            if(Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(path).find()){
                list.add(downloadedList.get(i));
            }
        }
        return  list;
    }


    private void playVideo(String path) {

        Intent m = new Intent(this, LeExoPlayerActivity.class);
        ResponseBag bag = new ResponseBag();
        bag.setVideos(new ArrayList<VideoDTO>());
        VideoDTO v = new VideoDTO();
        File f = new File(path);
        v.setUrl(Uri.fromFile(f).toString());
        bag.getVideos().add(v);
        m.putExtra("bag",bag);
        startActivity(m);
    }
    private void confirmUpload(final String path) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to upload this video to the database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadVideo(path);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void uploadVideo(String path) {
        showSnackbar("Uploading video ...", "OK", Constants.CYAN);
        VideoDTO v = new VideoDTO();
        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
        v.setCompanyName(u.getCompanyName());
        v.setCompanyID(u.getCompanyID());
        v.setFilePath(path);
        File file = new File(path);
        v.setVideoSize(file.length());
        v.setActive(true);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                v.setDailyThoughtID(dailyThought.getDailyThoughtID());
                v.setDescription(dailyThought.getTitle());
                v.setCaption(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                v.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
                v.setDescription(weeklyMasterClass.getTitle());
                v.setCaption(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                v.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                v.setDescription(weeklyMessage.getTitle());
                v.setCaption(weeklyMessage.getTitle());
                break;
            case ResponseBag.EBOOKS:
                v.seteBook(eBook);
                break;
            case ResponseBag.PODCASTS:
                v.setPodcast(podcast);
                break;
            case ResponseBag.URLS:
                v.setUrlDTO(url);
                break;
        }
        openProgressSheet();
        presenter.uploadVideo(v);
    }

    @Override
    public void onVideoUploaded(String key) {
        progressBottomSheet.dismiss();
        showSnackbar("Video has been uploaded", "OK", Constants.GREEN);
    }

    @Override
    public void onProgress(long transferred, long size) {
        float percent = (float) transferred * 100 / size;
        Log.i(TAG, "onProgress: video upload, transferred: "
                + df.format(percent)
                + " %");
        progressBottomSheet.onProgress(transferred,size);
//        showSnackbar("Video upload, transferred: "
//                + df.format(percent)
//                + " %", "OK", Constants.YELLOW);
//        showSnackbar("Video upload " + transferred + " of " + size, "OK", Constants.YELLOW);
    }

    private ProgressBottomSheet progressBottomSheet;
    private void openProgressSheet() {
        progressBottomSheet = ProgressBottomSheet.newInstance();
        progressBottomSheet.show(getSupportFragmentManager(),"PROGRESS_SHEET");
    }
    private static final DecimalFormat df = new DecimalFormat("##0.00");

    @Override
    public void onError(String message) {
        showSnackbar(message, "bad", Constants.RED);
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
    List<LocalVideo> localVideos = new ArrayList<>();
    class LocalVideo {
        long duration;
        String path;

        public LocalVideo(long duration, String path) {
            this.duration = duration;
            this.path = path;
        }
    }
}
