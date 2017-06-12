package com.oneconnect.leadership.library.audio;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.ProgressBottomSheet;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.internal.schedulers.SchedulerPoolFactory;

public class PodcastSelectionActivity extends AppCompatActivity implements PodcastUploadContract.View{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private EBookDTO eBook;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private int type;
    private Snackbar snackbar;
    private PodcastUploadPresenter presenter;
    public static final String TAG = PodcastSelectionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Podcast Selection & Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new PodcastUploadPresenter(this);

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
        }

        setup();
        getPodcastsOnDevice();
    }

    public void getPodcastsOnDevice() {

        HashSet<String> podcastItemHashSet = new HashSet<>();
        String[] projection = {
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME
        };
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        try {
            cursor.moveToFirst();
            do {

                Log.d(TAG, "getPodcastsOnDevice: ".concat(cursor.getColumnNames().toString()));
                String path =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION));
                Log.e(TAG, "getPodcastsOnDevice: duration: " + duration + " path: ".concat(path));
                localPodcasts.add(new LocalPodcast(duration,path));
                podcastItemHashSet.add(path);
            } while (cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "getPodcastsOnDevice: ", e);
        }
        ArrayList<String> downloadedList = new ArrayList<>(podcastItemHashSet);
        for (String id : downloadedList) {
            Log.e(TAG, "getPodcastsOnDevice: ".concat(id));
        }

        PodcastAdapter adapter = new PodcastAdapter(downloadedList, this, new PodcastAdapter.AudioAdapterListener() {
            @Override
            public void onPlayAudioTapped(String path) {
                playAudio(path);
            }

            @Override
            public void onUploadAudioTapped(String path) {
                confirmUpload(path);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void playAudio(String path) {

        Intent m = new Intent(this, AudioPlayerActivity.class);
        ResponseBag bag = new ResponseBag();
        bag.setPodcasts(new ArrayList<PodcastDTO>());
        PodcastDTO v = new PodcastDTO();
        File f = new File(path);
        v.setUrl(Uri.fromFile(f).toString());
        bag.getPodcasts().add(v);
        m.putExtra("bag",bag);
        startActivity(m);
    }
    private void confirmUpload(final String path) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to upload this podcast to the database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadPodcast(path);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void uploadPodcast(String path) {
        showSnackbar("Uploading podcast ...", "OK", Constants.CYAN);
        PodcastDTO v = new PodcastDTO();
        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
        v.setCompanyName(u.getCompanyName());
        v.setCompanyID(u.getCompanyID());
        v.setFilePath(path);
        File file = new File(path);
        v.setPodcastSize(file.length());
        v.setActive(true);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                v.setDailyThoughtID(dailyThought.getDailyThoughtID());
                v.setSubjectTitle(dailyThought.getTitle());
                v.setSubtitle(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                v.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
                v.setSubjectTitle(weeklyMasterClass.getTitle());
                v.setSubtitle(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                v.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                v.setSubjectTitle(weeklyMessage.getTitle());
                v.setSubtitle(weeklyMessage.getTitle());
                break;
            case ResponseBag.EBOOKS:
                v.seteBookID(eBook.geteBookID());
                v.setTitle(eBook.getTitle());
                //v.setSubtitle(eBook.);
                break;
        }
        openProgressSheet();
        presenter.uploadPodcast(v);
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

    private void setup() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(PodcastSelectionActivity.this, AudioRecordTest.class);
                //startActivity(intent);
                File dir = Environment.getExternalStorageDirectory();
                File myDir = new File(dir, "leadership");
                if (!myDir.exists()) {
                    myDir.mkdir();
                }
                recordAudio(myDir.toString());
            }
        });
    }

    List<LocalPodcast> localPodcasts = new ArrayList<>();
    class LocalPodcast {
        long duration;
        String path;

        public LocalPodcast(long duration, String path) {
            this.duration = duration;
            this.path = path;
        }
    }

    public void recordAudio(String fileName) {
        final MediaRecorder recorder = new MediaRecorder();
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.MediaColumns.TITLE, "leaderShip Podcast1");
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        File podName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "lsPodcast1");
        recorder.setOutputFile(podName.toString());
        try {
            recorder.prepare();
        } catch (Exception e){
            e.printStackTrace();
        }

        final ProgressDialog mProgressDialog = new ProgressDialog(PodcastSelectionActivity.this);
        mProgressDialog.setTitle("Recording Audio...."/*R.string.lbl_recording*/);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setButton("Stop recording", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mProgressDialog.dismiss();
                recorder.stop();
                recorder.release();
            }
        });

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
            public void onCancel(DialogInterface p1) {
                recorder.stop();
                recorder.release();
            }
        });
        recorder.start();
        mProgressDialog.show();
    }

    @Override
    public void onPodcastUploaded(String key) {
        progressBottomSheet.dismiss();
        showSnackbar("Podcast has been uploaded", "OK", Constants.GREEN);
    }
    private static final DecimalFormat df = new DecimalFormat("##0.00");

    @Override
    public void onProgress(long transferred, long size) {
        float percent = (float) transferred * 100 / size;
        Log.i(TAG, "onProgress: video upload, transferred: "
                + df.format(percent)
                + " %");
        progressBottomSheet.onProgress(transferred,size);
    }

    @Override
    public void onError(String message) {

    }
}
