package com.oneconnect.leadership.library.audio;

/**
 * Created by Kurisani on 2017/06/08.
 */

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.PodcastPlayerActivity;
import com.oneconnect.leadership.library.activities.ProgressBottomSheet;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

public class AudioRecordTest extends AppCompatActivity implements  PodcastUploadContract.View{

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording, buttonUpload ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    TextView recordProgress, textCurrentPosition, textView_maxTime, contentTxt;
    long miliSecs = 0;
    Thread t;
    String timer;
    PodcastUploadPresenter presenter;
    SeekBar recorderSeekBar;
    ImageView stopIMG, pauseIMG, playIMG, imageView, podcasts;
    RelativeLayout controlsLay;
    String hexColor;
    AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_audio);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentTxt = (TextView) findViewById(R.id.contentTxt);
        if (getIntent().getSerializableExtra("dailyThought") != null) {
            type = ResponseBag.DAILY_THOUGHTS;
            dailyThought =  (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
            contentTxt.setText(dailyThought.getTitle() + "-" + dailyThought.getSubtitle());
        }
        if (getIntent().getSerializableExtra("hexColor") != null) {
         hexColor = (String) getIntent().getSerializableExtra("hexColor");
         toolbar.setBackgroundColor(Color.parseColor(hexColor));
            appBar.setBackgroundColor(Color.parseColor(hexColor));
        }
        recorderSeekBar = (SeekBar) findViewById(R.id.recorderSeekBar);
        recorderSeekBar.setVisibility(View.GONE);
        textCurrentPosition = (TextView) findViewById(R.id.textCurrentPosition);
        textCurrentPosition.setVisibility(View.GONE);
        textView_maxTime = (TextView) findViewById(R.id.textView_maxTime);
        textView_maxTime.setVisibility(View.GONE);
        playIMG = (ImageView) findViewById(R.id.playIMG);
        playIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  playIMG.setVisibility(View.GONE);
                        pauseIMG.setVisibility(View.VISIBLE);
                        stopIMG.setVisibility(View.VISIBLE);
                        recordProgress.setVisibility(View.GONE);
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mediaPlayer.setDataSource(AudioSavePathInDevice);
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        /*mediaPlayer.start();*/
                        int duration = mediaPlayer.getDuration();
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        if(currentPosition== 0)  {
                             recorderSeekBar.setMax(duration);
                            String maxTimeString = /*this.*/millisecondsToString(duration);
                           textView_maxTime.setText(maxTimeString);
                        } else if(currentPosition== duration)  {
                            // Resets the MediaPlayer to its uninitialized state.
                           mediaPlayer.reset();
                        }
                            mediaPlayer.start();
                        // Create a thread to update position of SeekBar.
                        UpdateSeekBarThread updateSeekBarThread= new UpdateSeekBarThread();
                        threadHandler.postDelayed(updateSeekBarThread,50);

                        setTimerLabel("Playing Audio....");
                        Toast.makeText(AudioRecordTest.this, "Recording Playing",
                                Toast.LENGTH_LONG).show();

            }
        });
        pauseIMG = (ImageView) findViewById(R.id.pauseIMG);
        pauseIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseIMG.setVisibility(View.GONE);
                playIMG.setVisibility(View.VISIBLE);
                //mediaRecorder.pause();
                mediaPlayer.pause();
               /* Util.flashOnce(pauseIMG, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        pauseIMG.setVisibility(View.GONE);
                        playIMG.setVisibility(View.VISIBLE);
                        //mediaRecorder.pause();
                        mediaPlayer.pause();
                    }
                });*/
            }
        });
        stopIMG = (ImageView) findViewById(R.id.stopIMG);
        stopIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                stopIMG.setVisibility(View.GONE);
                pauseIMG.setVisibility(View.GONE);
                playIMG.setVisibility(View.VISIBLE);
                /* Util.flashOnce(stopIMG, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mediaPlayer.stop();
                        stopIMG.setVisibility(View.GONE);
                        pauseIMG.setVisibility(View.GONE);
                        playIMG.setVisibility(View.VISIBLE);
                    }
                });*/
            }
        });

        controlsLay = (RelativeLayout) findViewById(R.id.controlsLay);
        controlsLay.setVisibility(View.GONE);
        imageView = (ImageView) findViewById(R.id.imageView);
        podcasts =(ImageView) findViewById(R.id.podcastIMGAE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/    " +
                                    CreateRandomAudioFileName(5) + "AudioRecording.mp3";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    buttonUpload.setEnabled(false);
                    recordProgress.setVisibility(View.VISIBLE);
                    recordProgress.setText("");
                    buttonStop.setVisibility(View.VISIBLE);
                    setTimerLabel("Recording Audio....");
                    Toast.makeText(AudioRecordTest.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
                /*Util.flashOnce(imageView, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                    }
                });*/
            }
        });

        podcasts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/    " +
                                    CreateRandomAudioFileName(5) + "AudioRecording.mp3";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    buttonUpload.setEnabled(false);
                    recordProgress.setVisibility(View.VISIBLE);
                    recordProgress.setText("");
                    buttonStop.setVisibility(View.VISIBLE);
                    setTimerLabel("Recording Audio....");
                    Toast.makeText(AudioRecordTest.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
               /* Util.flashOnce(imageView, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                    }
                });*/
            }
        });

        buttonStart = (Button) findViewById(R.id.button);
        buttonStart.setVisibility(View.GONE);
        buttonStop = (Button) findViewById(R.id.button2);
        buttonStop.setVisibility(View.GONE);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.button3);
        buttonPlayLastRecordAudio.setVisibility(View.GONE);
        buttonStopPlayingRecording = (Button)findViewById(R.id.button4);
        buttonStopPlayingRecording.setVisibility(View.GONE);
        buttonUpload  = (Button)findViewById(R.id.button5);
        buttonUpload.setVisibility(View.GONE);
        recordProgress = (TextView) findViewById(R.id.timerText);

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);
        buttonUpload.setEnabled(false);
        recordProgress.setVisibility(View.GONE);

        random = new Random();

        presenter = new PodcastUploadPresenter(this);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/    " +
                                    CreateRandomAudioFileName(5) + "AudioRecording.mp3";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    buttonUpload.setEnabled(false);
                    recordProgress.setVisibility(View.VISIBLE);
                    recordProgress.setText("");
                    setTimerLabel("Recording Audio....");
                    Toast.makeText(AudioRecordTest.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setVisibility(View.GONE);
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonUpload.setEnabled(true);
                t.interrupt();
                miliSecs = 0;

                buttonUpload.setVisibility(View.VISIBLE);
                controlsLay.setVisibility(View.VISIBLE);
                pauseIMG.setVisibility(View.GONE);
                stopIMG.setVisibility(View.GONE);
                recorderSeekBar.setVisibility(View.VISIBLE);
                textCurrentPosition.setVisibility(View.VISIBLE);
                textView_maxTime.setVisibility(View.VISIBLE);
                recordProgress.setVisibility(View.GONE);


                Toast.makeText(AudioRecordTest.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);
                buttonUpload.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                setTimerLabel("Playing Audio....");
                Toast.makeText(AudioRecordTest.this, "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonUpload.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
                t.interrupt();
                miliSecs = 0;
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(false);
                buttonUpload.setEnabled(true);

                uploadPodcast(AudioSavePathInDevice);
            }
        });

    }

    private Handler threadHandler = new Handler();

    class UpdateSeekBarThread implements Runnable {

        public void run()  {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = millisecondsToString(currentPosition);
            textCurrentPosition.setText(currentPositionStr);

            recorderSeekBar.setProgress(currentPosition);
            // Delay thread 50 milisecond.
            threadHandler.postDelayed(this, 50);
        }
    }

    private String millisecondsToString(int milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) milliseconds);
        return minutes + ":" + seconds;
    }

    public  void setTimerLabel(final String label){
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isDaemon()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                miliSecs += 1000;
                                timer = Util.milliSecondsToTimer(miliSecs);
                                recordProgress.setText(label + "  " + timer);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    t.interrupt();
                }
            }
        };
        t.start();
     }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AudioRecordTest.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(AudioRecordTest.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AudioRecordTest.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onPodcastUploaded(String key) {

        progressBottomSheet.dismiss();

        showSnackbar("Podcast has been uploaded", "OK", Constants.GREEN);
    }

    @Override
    public void onPodcastAddedToEntity(String key) {

    }

    @Override
    public void onProgress(long transferred, long size) {
        float percent = (float) transferred * 100 / size;

        Log.i(TAG, "onProgress: video upload, transferred:" + df.format(percent)    + "%" );

        progressBottomSheet.onProgress(transferred,size);
    }

    @Override
    public void onError(String message) {

    }
    private ProgressBottomSheet progressBottomSheet;

    private void openProgressSheet() {

        progressBottomSheet = ProgressBottomSheet.newInstance();

        progressBottomSheet.show(getSupportFragmentManager(),"PROGRESS_SHEET");

    }


    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(buttonStart, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    private int type;

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
      //  v.setPodcastDescription(PodcastDTO.DESC_PODCAST);
      //  v.setPodcastType(PodcastDTO.PODCAST);

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

        }


        openProgressSheet();

        presenter.uploadPodcast(v);

    }
    public static final String TAG = AudioRecordTest.class.getSimpleName();
    private static final DecimalFormat df = new DecimalFormat("##0.00");
    private Snackbar snackbar;
    private Toolbar toolbar;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
}
