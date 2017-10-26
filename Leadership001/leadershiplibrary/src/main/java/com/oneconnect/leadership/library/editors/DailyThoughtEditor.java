package com.oneconnect.leadership.library.editors;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.activities.ProgressBottomSheet;
import com.oneconnect.leadership.library.activities.SheetContract;
import com.oneconnect.leadership.library.activities.SheetPresenter;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.CategoryAdapter;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.audio.PodcastUploadContract;
import com.oneconnect.leadership.library.audio.PodcastUploadPresenter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.cache.CategoryCache;
import com.oneconnect.leadership.library.camera.CameraActivity;
import com.oneconnect.leadership.library.camera.VideoSelectionActivity;
import com.oneconnect.leadership.library.crud.CrudContract;
import com.oneconnect.leadership.library.crud.CrudPresenter;
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
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.links.LinksActivity;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.oneconnect.leadership.library.ebook.EbookListFragment.REQUEST_LINKS;

/**
 * Created by aubreymalabie on 3/18/17.
 */

public class DailyThoughtEditor extends BaseBottomSheet implements SheetContract.View,
        SubscriberContract.View, PodcastUploadContract.View,CacheContract.View,
        CategoryAdapter.CategoryAdapterListener {
    private DailyThoughtDTO dailyThought;


    private TextInputEditText editTitle, editSubtitle;
    private RecyclerView recyclerView;
    private ImageView iconCamera, iconVideo, iconDate, iconURLs, iconMicrophone;
    TextView timer;
    private View iconLayout;
    private Button btn, btnDate;
    private Date selectedDate;
    private Spinner catSpinner;
    private SubscriberPresenter Catpresenter;
    FirebaseStorageAPI fbs;
    private PodcastUploadPresenter podcastUploadPresenter;
    private CrudPresenter crudPresenter;
    private CachePresenter cachePresenter;
    private RadioButton internalButton, globalButton;

    List<CategoryDTO> categoryList;
    private CategoryDTO category;

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "******* onEntityAdded: daily thought added to firebase "
                .concat(key));
        dailyThought.setDailyThoughtID(key);
        bottomSheetListener.onWorkDone(dailyThought);
        this.dismiss();
        /*if (getOutputFile() != null) {
            AudioSavePathInDevice = getOutputFile().getAbsolutePath();
            sendPodcastWithDailyThought(AudioSavePathInDevice);
            /*sendPodcastWithDailyThought(getOutputFile().getAbsolutePath());
        }
     this.dismiss();*/
    }

    @Override
    public void onEntityUpdated() {

    }

    @Override
    public void onUserCreated(UserDTO user) {

    }

    UserDTO user;
    @Override
    public void onUserFound(UserDTO u) {
        Log.i(TAG, "*** onUserFound ***" + u.getFullName());
        user = u;
        userType = u.getUserType();

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

    CategoryAdapter.CategoryAdapterListener listener;
    int userType;
    @Override
    public void onAllCategories(List<CategoryDTO> list) {
        Log.i(TAG, "**** onAllCategories: ****");

        List<String> lis = new ArrayList<String>();
        lis.add("Select Category");
        for (CategoryDTO cat : list) {
            lis.add(cat.getCategoryName());
            category = cat;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lis);
        catSpinner.setAdapter(adapter);
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Category spinner: " + catSpinner.getSelectedItem().toString());
                if (category != null) {
                //   listener.onCategorySelected(category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onAllVideos(List<VideoDTO> list) {

    }

    @Override
    public void onAllEBooks(List<EBookDTO> list) {

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
    public void onEntityDeleted() {

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
    public void onPodcastUploaded(String key) {
        progressBottomSheet.dismiss();
        showSnackbar("Podcast has been uploaded", "OK", Constants.GREEN);
        this.dismiss();
    }

    private Snackbar snackbar;

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(btnDate, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

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
          bottomSheetListener.onError(message);
    }

    public static DailyThoughtEditor newInstance(DailyThoughtDTO dailyThought, int type) {
        DailyThoughtEditor f = new DailyThoughtEditor();
        Bundle args = new Bundle();
        args.putInt("type", type);
        if (dailyThought != null) {
            args.putSerializable("dailyThought", dailyThought);
        }
        f.setArguments(args);
        return f;
    }

    FirebaseAuth firebaseAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyThought = (DailyThoughtDTO) getArguments().getSerializable("dailyThought");
        type = getArguments().getInt("type", 0);
        presenter = new SheetPresenter(this);
        Catpresenter = new SubscriberPresenter(this);
        cachePresenter = new CachePresenter(this, getActivity());
        fbs = new FirebaseStorageAPI();
        podcastUploadPresenter = new PodcastUploadPresenter(this);

        firebaseAuth = FirebaseAuth.getInstance();
        Catpresenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.daily_thought_editor, container, false);
        btn = (Button) view.findViewById(R.id.btn);
        catSpinner = (Spinner) view.findViewById(R.id.Catspinner);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (getOutputFile() != null) {
                    Log.i(TAG, "outputFile is not null");
                    //stopRecording();
                    send();
                    *//*AudioSavePathInDevice = getOutputFile().getAbsolutePath();
                    sendPodcastWithDailyThought(AudioSavePathInDevice);*//*
                } else {*/
                    send();
                /*}*/


            }
        });
        editTitle = (TextInputEditText) view.findViewById(R.id.editTitle);
        editTitle.setHint("Enter Thought");
        editSubtitle = (TextInputEditText) view.findViewById(R.id.editSubtitle);
        editSubtitle.setHint("Enter thought author");
        btnDate = (Button) view.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListener.onDateRequired();
            }
        });
        internalButton = (RadioButton) view.findViewById(R.id.internalButton);
        globalButton = (RadioButton) view.findViewById(R.id.globalButton);
        timer = (TextView) view.findViewById(R.id.timer);
        timer.setVisibility(View.GONE);
        iconVideo = (ImageView) view.findViewById(R.id.iconVideo);
        iconMicrophone = (ImageView) view.findViewById(R.id.iconMicrophone);
        iconMicrophone.setVisibility(View.GONE);
        iconMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
                iconMicrophone.setVisibility(View.GONE);
                timer.setVisibility(View.VISIBLE);
            }
        });

        getCachedCategories();
        getCategories();

        return view;
    }
    private MediaRecorder mRecorder;
    private File mOutputFile;
    private long mStartTime = 0;

    private Handler mHandler = new Handler();
    private Runnable mTickExecutor = new Runnable() {
        @Override
        public void run() {
            tick();
            mHandler.postDelayed(mTickExecutor,100);
        }
    };

    private int[] amplitudes = new int[100];
    private int i = 0;
    private void tick() {
        long time = (mStartTime < 0) ? 0 : (SystemClock.elapsedRealtime() - mStartTime);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        int milliseconds = (int) (time / 100) % 10;
        timer.setText(minutes+":"+(seconds < 10 ? "0"+seconds : seconds)+"."+milliseconds);
        if (mRecorder != null) {
            amplitudes[i] = mRecorder.getMaxAmplitude();
            //Log.d("Voice Recorder","amplitude: "+(amplitudes[i] * 100 / 32767));
            if (i >= amplitudes.length -1) {
                i = 0;
            } else {
                ++i;
            }
        }
    }

    protected  void stopRecording() {
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
        mStartTime = 0;
        mHandler.removeCallbacks(mTickExecutor);
        if (/*!saveFile &&*/ mOutputFile != null) {
            Log.i(TAG, "outoutfile is not null");
          //  mOutputFile.delete();
        }
    }
    String AudioSavePathInDevice = null;

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mRecorder.setAudioEncodingBitRate(48000);
        } else {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioEncodingBitRate(64000);
        }
        mRecorder.setAudioSamplingRate(16000);*/
        mOutputFile = getOutputFile();
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartTime = SystemClock.elapsedRealtime();
            mHandler.postDelayed(mTickExecutor, 100);
            Log.d("Voice Recorder","started recording to "+mOutputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("Voice Recorder", "prepare() failed "+e.getMessage());
        }
    }

    private File getOutputFile() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
       // AudioSavePathInDevice = mOutputFile.getAbsolutePath();
        /*File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/Voice Recorder/RECORDING_"
                + dateFormat.format(new Date())
                + ".mp3");*/
        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                        + "/Voice Recorder/RECORDING_"
                        + dateFormat.format(new Date())
                        + ".mp3";
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/Voice Recorder/RECORDING_"
                + dateFormat.format(new Date())
                + ".mp3"/*".m4a"*/);


    }


    public void getCategories() {
        Log.d(TAG, "******* getAllCategories: ");
        Catpresenter.getAllCategories();
    }

    public void getCachedCategories() {
        CategoryCache.getCategories(getActivity(), new CategoryCache.ReadListener() {
            @Override
            public void onDataRead(List<CategoryDTO> categories) {
                Log.d(TAG, "onDataRead: Categories: " + categories.size());
            //    categoryList = categories;
            //    setCategorySpinner();
            }

            @Override
            public void onError(String message) {
                getCachedCategories();
            }
        });
    }

    private void setCategorySpinner() {
        List<String> list = new ArrayList<String>();
        list.add("Select Category");
        Collections.sort(categoryList);

        for (CategoryDTO cat : categoryList) {
            list.add(cat.getCategoryName());
            category = cat;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        catSpinner.setAdapter(adapter);
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private boolean isReadyToSend;

    private void sendPodcastWithDailyThought(String path) {
        PodcastDTO v = new PodcastDTO();

        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());

        v.setCompanyName(u.getCompanyName());

        v.setCompanyID(u.getCompanyID());

        v.setFilePath(path);

        File file = new File(path);

        v.setPodcastSize(file.length());

        v.setActive(true);
        v.setPodcastDescription(PodcastDTO.DESC_VOICE_RECORDING);
        v.setPodcastType(PodcastDTO.RECORDING);

        switch (type) {

            case ResponseBag.DAILY_THOUGHTS:

                v.setDailyThoughtID(dailyThought.getDailyThoughtID());

                v.setSubjectTitle(dailyThought.getTitle());

                v.setSubtitle(dailyThought.getTitle());

                break;
        }

        openProgressSheet();
        v.setDailyThoughtID(dailyThought.getDailyThoughtID());
        podcastUploadPresenter.uploadPodcastRecording(v);

      /*  PodcastDTO v = new PodcastDTO();
      //  if (getOutputFile() != null) {
            Log.i(TAG, "file not null: " + path*//*getOutputFile().getAbsolutePath()*//*);
         //   PodcastDTO v = new PodcastDTO();
        //    UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
           *//* if (user != null){
                v.setCompanyName(user.getCompanyName());
                v.setCompanyID(user.getCompanyID());
            } else if (u.getCompanyName() != null) {
                v.setCompanyName(u.getCompanyName());
                v.setCompanyID(u.getCompanyID());
            }*//*
            v.setFilePath(path*//*getOutputFile().getAbsolutePath()*//*);
            File file = new File(path*//*getOutputFile().getAbsolutePath()*//*);
            v.setPodcastSize(file.length());
            v.setActive(true);
            v.setPodcastDescription(PodcastDTO.DESC_VOICE_RECORDING);
            v.setPodcastType(PodcastDTO.RECORDING);*/

/*
        openProgressSheet();*/
      /*  fbs.uploadPodcast(v, new FirebaseStorageAPI.StorageListener() {
            @Override
            public void onResponse(String key) {

            }

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
                Log.e(TAG, "onError: " + message);
            }
        });*/
        /*podcastUploadPresenter.uploadPodcast(v);*/

    }

    private ProgressBottomSheet progressBottomSheet;
    private void openProgressSheet() {
        progressBottomSheet = ProgressBottomSheet.newInstance();
        progressBottomSheet.show(getFragmentManager()/*getSupportFragmentManager()*/,"PROGRESS_SHEET");
    }

    private void send() {

        if (TextUtils.isEmpty(editTitle.getText())) {
            editTitle.setError(getString(R.string.enter_thought));
            return;
        }

        if (TextUtils.isEmpty(editSubtitle.getText())) {
            editTitle.setError(getString(R.string.enter_thought_author));
            return;
        }

        if(catSpinner == null) {
            isReadyToSend = true;
            bottomSheetListener.onError("Choose a category");
            return;
        }

        Log.d(TAG, "send: @@@@@@@@@@@ starting to send daily thought to Firebase");

        if (dailyThought == null) {
            dailyThought = new DailyThoughtDTO();
            UserDTO me = SharedPrefUtil.getUser(getActivity());
            if (me != null) {
                dailyThought.setCompanyID(me.getCompanyID());
                dailyThought.setCompanyName(me.getCompanyName());
                dailyThought.setUser(me);

                dailyThought.setActive(true);
                dailyThought.setJournalUserID(me.getUserID());
                dailyThought.setJournalUserName(me.getFirstName() + " " + me.getLastName());
            } //else if (user)


        }
        if (selectedDate == null) {
            isReadyToSend = true;
            bottomSheetListener.onDateRequired();
            return;
        } else {
            dailyThought.setDateScheduled(selectedDate.getTime());
        }
        category.setCategoryName(catSpinner.getSelectedItem().toString());
        dailyThought.setTitle(editTitle.getText().toString());
        dailyThought.setSubtitle(editSubtitle.getText().toString());
        dailyThought.setCategory(category);

        if (internalButton.isChecked()) {
                     dailyThought.setDailyThoughtDescription(DailyThoughtDTO.DESC_INTERNAL_DAILY_THOUGHT);
                    dailyThought.setDailyThoughtType(DailyThoughtDTO.INTERNAL_DAILY_THOUGHT);
                    if (userType == UserDTO.PLATINUM_USER) {
                        dailyThought.setDailyThoughtType_status(Constants.INTERNAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
                    }
                    else if (userType == UserDTO.PLATINUM_ADMIN) {
                        dailyThought.setDailyThoughtType_status(Constants.INTERNAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
                    }
                    else if (userType == UserDTO.COMPANY_ADMIN) {
                        dailyThought.setDailyThoughtType_status(Constants.INTERNAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
                    }
                    else if (userType == UserDTO.GOLD_USER) {
                        dailyThought.setDailyThoughtType_status(Constants.INTERNAL_DAILY_THOUGHT.concat("_").concat(Constants.PENDING));
                    }
                  }
              if (globalButton.isChecked()) {
                      dailyThought.setDailyThoughtDescription(DailyThoughtDTO.DESC_GLOBAL_DAILY_THOUGHT);
                       dailyThought.setDailyThoughtType(DailyThoughtDTO.GLOBAL_DAILY_THOUGHT);
                  if (userType == UserDTO.PLATINUM_USER) {
                      dailyThought.setDailyThoughtType_status(Constants.GLOBAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
                  }
                  else if (userType == UserDTO.PLATINUM_ADMIN) {
                      dailyThought.setDailyThoughtType_status(Constants.GLOBAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
                  }
                  else if (userType == UserDTO.COMPANY_ADMIN) {
                      dailyThought.setDailyThoughtType_status(Constants.GLOBAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
                  }
                  else if (userType == UserDTO.GOLD_USER) {
                      dailyThought.setDailyThoughtType_status(Constants.GLOBAL_DAILY_THOUGHT.concat("_").concat(Constants.PENDING));
                  }
              }
                  if (userType == UserDTO.PLATINUM_USER) {
                      dailyThought.setStatus(Constants.APPROVED);
                      dailyThought.setUserType(UserDTO.DESC_PLATINUM_USER);
                      dailyThought.setCompanyID_status(user.getCompanyID().concat("_").concat(Constants.APPROVED));
                    //  dailyThought.setDailyThoughtType_status();
                  }
                  else if(userType == UserDTO.PLATINUM_ADMIN){
                    dailyThought.setStatus(Constants.APPROVED);
                    dailyThought.setUserType(UserDTO.DESC_PLATINUM_ADMIN);
                    dailyThought.setCompanyID_status(user.getCompanyID().concat("_").concat(Constants.APPROVED));
                  }
                  else if (userType == UserDTO.GOLD_USER) {
                      dailyThought.setStatus(Constants.PENDING);
                      dailyThought.setCompanyID_status(user.getCompanyID().concat("_").concat(Constants.PENDING));
                  }
                  else if (userType == UserDTO.COMPANY_ADMIN) {
                      dailyThought.setStatus(Constants.APPROVED);
                      dailyThought.setCompanyID_status(user.getCompanyID().concat("_").concat(Constants.APPROVED));
                  }

        switch (type) {
            case Constants.NEW_ENTITY:
                Log.w(TAG, "...sending to Firebase: ".concat(GSON.toJson(dailyThought)));
                presenter.addEntity(dailyThought);
                break;
            case Constants.UPDATE_ENTITY:

                break;
            case Constants.DELETE_ENTITY:

                break;
        }
    }

    public void setSelectedDate(Date selectedDate) {

        this.selectedDate  = Util.getDateAtMidnite(selectedDate);
        btnDate.setText(sdf.format(this.selectedDate));
        if (dailyThought != null) {
            dailyThought.setDateScheduled(this.selectedDate.getTime());
            if (isReadyToSend) {
                isReadyToSend = false;
                send();
            }

        }
    }

    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
    public static final String TAG = DailyThoughtEditor.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onCategorySelected(CategoryDTO category) {
        this.category = category;
    }
}
