package com.oneconnect.leadership.library.activities;

/**
 * Created by Kurisani on 2017/07/12.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.ratings.RatingContract;
import com.oneconnect.leadership.library.ratings.RatingPresenter;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.util.Date;

public class MyAndroidAppActivity extends AppCompatActivity implements RatingContract.View {

    private RatingBar ratingBar;
    private TextView txtRatingValue, lblRateMe;
    private Button btnSubmit;
    protected EditText ratingCom;
    private RatingPresenter ratingPresenter;
    private CachePresenter cachePresenter;
    private Context ctx;
    private Snackbar snackbar;
    private Toolbar toolbar;
    int type;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private NewsDTO news;
    private EBookDTO eBook;
    private PodcastDTO podcast;
    private VideoDTO video;
    private UrlDTO url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating);
        ctx = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rating Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratingPresenter = new RatingPresenter(this);
        //cachePresenter = new CachePresenter(this, ctx);
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
        if(getIntent().getSerializableExtra("dailyThought") != null){
             type = ResponseBag.DAILY_THOUGHTS;
            dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
            getSupportActionBar().setSubtitle(dailyThought.getTitle());
        }
        if(getIntent().getSerializableExtra("weeklyMasterClass") != null){
            type = ResponseBag.WEEKLY_MASTERCLASS;
            weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");
            getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
        }
        if(getIntent().getSerializableExtra("weeklyMessage") != null){
            type = ResponseBag.WEEKLY_MESSAGE;
            weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
            getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
        }
        if(getIntent().getSerializableExtra("newsArticle") != null){
            type = ResponseBag.NEWS;
            news = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
        }
        if(getIntent().getSerializableExtra("eBook") != null){
            type = ResponseBag.EBOOKS;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
            getSupportActionBar().setSubtitle(eBook.getStorageName());
        }
        if(getIntent().getSerializableExtra("podcast") != null){
            type = ResponseBag.PODCASTS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
            getSupportActionBar().setSubtitle(podcast.getStorageName());
        }

      //  addListenerOnRatingBar();
      //  addListenerOnButton();

        setup();

    }

    private void setup() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
        txtRatingValue.setVisibility(View.GONE);
        ratingCom = (EditText) findViewById(R.id.ratingCom);
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });

        lblRateMe = (TextView) findViewById(R.id.lblRateMe);
        lblRateMe.setVisibility(View.GONE);
       // ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        // ratingCom = (EditText) findViewById(R.id.ratingCom);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSubmit, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        uploadRating();
                    }
                });

               /* Toast.makeText(MyAndroidAppActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show(); */

            }

        });
    }
    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
        ratingCom = (EditText) findViewById(R.id.ratingCom);
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
      /*  ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                //  txtRatingValue.setText(String.valueOf(rating));

            }
        }); */
    }

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(ratingBar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void uploadRating(){
        Log.d(LOG, "****uploadingRating****");
       // showSnackbar("Uploading Rating ...", "OK", Constants.CYAN);
        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());

        RatingDTO rate = new RatingDTO();
//        rate.setUserID(u.getUserID());
 //       rate.setUserName(u.getFirstName());

        float ratingValue = ratingBar.getRating();

        rate.setRating((int)Math.round(ratingValue)/*getNumStars()*/);
        if(ratingCom.getText().toString() != null){
            rate.setComment(ratingCom.getText().toString());
        }
        rate.setDate(new Date().getTime());
        switch (type){
            case ResponseBag.DAILY_THOUGHTS:
                rate.setDailyThoughtID(dailyThought.getDailyThoughtID());
                break;

            case ResponseBag.WEEKLY_MASTERCLASS:
                rate.setWeeklyMasterclassID(weeklyMasterClass.getWeeklyMasterClassID());
                break;

            case ResponseBag.WEEKLY_MESSAGE:
                rate.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                break;

            case ResponseBag.EBOOKS:
                rate.seteBookID(eBook.geteBookID());
                break;

            /*case ResponseBag.PODCASTS:
                rate.setPodcastID(podcast.getPodcastID());
                break;*/
        }

        ratingPresenter.uploadRating(rate);
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
       // ratingCom = (EditText) findViewById(R.id.ratingCom);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadRating();
               /* Toast.makeText(MyAndroidAppActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();*/

            }

        });

    }

    @Override
    public void onRatingUploaded(String key) {
        showSnackbar("rating successfully added ...", "OK", Constants.CYAN);
        ratingCom.setText(" ");
    }

    @Override
    public void onError(String message) {
        Log.e(LOG, "onError: " + message);
    }

    static final String LOG = MyAndroidAppActivity.class.getSimpleName();
}