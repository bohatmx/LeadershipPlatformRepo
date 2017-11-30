package com.oneconnect.leadership.library.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.firebase.crash.FirebaseCrash;
import com.ocg.backend.endpointAPI.model.Data;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.ocg.backend.endpointAPI.model.FCMessageDTO;
import com.ocg.backend.endpointAPI.model.PayLoad;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.crud.CrudContract;
import com.oneconnect.leadership.library.crud.CrudPresenter;
import com.oneconnect.leadership.library.data.BaseDTO;
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
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.fcm.EndpointContract;
import com.oneconnect.leadership.library.fcm.EndpointPresenter;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.Util;

import java.util.Date;
import java.util.List;

import static com.oneconnect.leadership.library.fcm.EndpointUtil.DAILY_THOUGHT;


public class UpdateEntityActivity extends AppCompatActivity implements CrudContract.View, EndpointContract.View{

    private DailyThoughtDTO dailyThought;
    private WeeklyMasterClassDTO masterClass;
    private WeeklyMessageDTO weeklyMessage;
    private NewsDTO news;
    private SubscriptionDTO subscription;
    private TextInputEditText editTitle, editSubtitle;
    private RecyclerView recyclerView;
    private ImageView iconCamera, iconVideo, iconDate, iconURLs;
    private View iconLayout;
    private Button btn, btnDate;
    private Date selectedDate;
    private Spinner catSpinner;
    private SubscriberPresenter Catpresenter;
    private CachePresenter cachePresenter;
    private CrudPresenter crudPresenter;
    private Spinner spinner;
    private RadioButton approvedButton, declinedButton;
    Data data;
    PayLoad payLoad;
    EndpointPresenter endpointPresenter;
    FCMessageDTO fcmMessage;
    FCMUserDTO fcmUser;
    UserDTO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entity);
        crudPresenter = new CrudPresenter(this);
        endpointPresenter = new EndpointPresenter(this);

        btn = (Button) findViewById(R.id.btn);
        editTitle = (TextInputEditText) findViewById(R.id.editTitle);
        editSubtitle = (TextInputEditText) findViewById(R.id.editSubtitle);
        approvedButton = (RadioButton) findViewById(R.id.approvedButton);
        declinedButton = (RadioButton) findViewById(R.id.declinedButton);

        if (getIntent().getSerializableExtra("user") != null) {
            user = (UserDTO) getIntent().getSerializableExtra("user");
        }

        if (getIntent().getSerializableExtra("dailyThought") != null){
            dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
            editTitle.setText(dailyThought.getTitle());
            editSubtitle.setText(dailyThought.getSubtitle());
        }
        if (getIntent().getSerializableExtra("masterClass") != null){
            masterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("masterClass");
            editTitle.setText(masterClass.getTitle());
            editSubtitle.setText(masterClass.getSubtitle());
        }
        if (getIntent().getSerializableExtra("weeklyMessage") != null){
            weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
            editTitle.setText(weeklyMessage.getTitle());
            editSubtitle.setText(weeklyMessage.getSubtitle());
        }
        if (getIntent().getSerializableExtra("news") != null){
            news = (NewsDTO) getIntent().getSerializableExtra("news");
            editTitle.setText(news.getTitle());
            editSubtitle.setText(news.getSubtitle());
        }

        if (getIntent().getSerializableExtra("subscription") != null){
            subscription = (SubscriptionDTO) getIntent().getSerializableExtra("subscription");
            editTitle.setText(subscription.getSubscriptionType().getSubscriptionName());
            editSubtitle.setText((int) subscription.getAmount());
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        if (dailyThought != null){
                            update();
                            return;
                        }
                        if (masterClass != null){
                            update();
                            return;
                        }
                        if (weeklyMessage != null){
                            update();
                            return;
                        }

                        if (subscription != null){
                            update();
                            return;
                        }

                    }



        });
    }

    private void update() {

        if (TextUtils.isEmpty(editTitle.getText())) {
            editTitle.setError("Enter title");
            return;
        }
        if (TextUtils.isEmpty(editSubtitle.getText())) {
            editSubtitle.setError("Enter subtitle");
            return;
        }
        String number_1 = "1";
        String number_2 = "2";

        if (dailyThought != null) {
            if (dailyThought.getDailyThoughtType() == DailyThoughtDTO.INTERNAL_DAILY_THOUGHT) {
                if (approvedButton.isChecked()) {
                    dailyThought.setStatus(Constants.APPROVED);
                    dailyThought.setCompanyID_status(dailyThought.getCompanyID().concat("_").concat(Constants.APPROVED));
                    dailyThought.setDailyThoughtType_status(number_1.concat("_").concat(Constants.APPROVED));
                }
                if (declinedButton.isChecked()) {
                    dailyThought.setStatus(Constants.DECLINED);
                    dailyThought.setCompanyID_status(dailyThought.getCompanyID().concat("_").concat(Constants.DECLINED));
                    dailyThought.setDailyThoughtType_status(number_1.concat("_").concat(Constants.DECLINED));
                }

            } else if (dailyThought.getDailyThoughtType() == DailyThoughtDTO.GLOBAL_DAILY_THOUGHT) {
                if (approvedButton.isChecked()) {
                    dailyThought.setStatus(Constants.APPROVED);
                    dailyThought.setCompanyID_status(dailyThought.getCompanyID().concat("_").concat(Constants.APPROVED));
                    dailyThought.setDailyThoughtType_status(number_2.concat("_").concat(Constants.APPROVED));
                }
                if (declinedButton.isChecked()) {
                    dailyThought.setStatus(Constants.DECLINED);
                    dailyThought.setCompanyID_status(dailyThought.getCompanyID().concat("_").concat(Constants.DECLINED));
                    dailyThought.setDailyThoughtType_status(number_2.concat("_").concat(Constants.DECLINED));
                }

            }

            dailyThought.setTitle(editTitle.getText().toString());
            dailyThought.setSubtitle(editSubtitle.getText().toString());

            /*if (approvedButton.isChecked()) {
                dailyThought.setStatus(Constants.APPROVED);
                dailyThought.setCompanyID_status(dailyThought.getCompanyID().concat("_").concat(Constants.APPROVED));
                dailyThought.setDailyThoughtType_status(dailyThought.getDailyThoughtType());
            }
            if (declinedButton.isChecked()) {
                dailyThought.setStatus(Constants.DECLINED);
                dailyThought.setCompanyID_status(dailyThought.getCompanyID().concat("_").concat(Constants.DECLINED));
            }*/

            crudPresenter.updateDailyThought(dailyThought);
            //Intent intent = new Intent(this, UpdateEntityActivity.class);
            //startActivity(intent);
         //   finish();
            return;
        }
        if (masterClass != null) {
            masterClass.setTitle(editTitle.getText().toString());
            masterClass.setSubtitle(editSubtitle.getText().toString());
            crudPresenter.updateWeeklyMasterClass(masterClass);
            finish();
            return;
        }
        if (weeklyMessage != null) {
            weeklyMessage.setTitle(editTitle.getText().toString());
            weeklyMessage.setSubtitle(editSubtitle.getText().toString());
            crudPresenter.updateWeeklyMessage(weeklyMessage);
            finish();
            return;
        }
        if (news != null) {
            news.setTitle(editTitle.getText().toString());
            news.setSubtitle(editSubtitle.getText().toString());
            crudPresenter.updateNews(news);
            finish();
            return;
        }
        if (subscription != null) {
            subscription.setSubscriptionName(editTitle.getText().toString());
            subscription.setAmount(Double.parseDouble(editSubtitle.getText().toString()));
            crudPresenter.updateSubscription(subscription);
            finish();
            return;
        }
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
    public void onCompanyFound(CompanyDTO company) {

    }

    @Override
    public void onUserUpdated(UserDTO user) {

    }

    static final String TAG = UpdateEntityActivity.class.getSimpleName();

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateEntityActivity.this, DailyThoughtApprovalActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDailyThoughtUpdated(DailyThoughtDTO dailyThought) {
        Log.i(TAG, "onDailyThoughtUpdated status: " + dailyThought.getStatus());
        if (dailyThought.getStatus().equalsIgnoreCase(Constants.APPROVED)) {
            data = new Data();
            fcmUser = new FCMUserDTO();
            payLoad = new PayLoad();
            fcmMessage = new FCMessageDTO();
            data.setUserID(user.getUserID());
            data.setTitle("Leadership Platform"/*dailyThought.getSubtitle()*/);
            data.setFromUser(user.getFullName());
            data.setMessageType(DAILY_THOUGHT);
            data.setMessage(dailyThought.getSubtitle()/*dailyThought.getTitle()*/ /*+ " - " + dailyThought.getSubtitle()*/);
            data.setDate(new Date().getTime());
            payLoad.setData(data);
            fcmMessage.setCompanyID(user.getCompanyID());
            fcmMessage.setDailyThoughtID(dailyThought.getDailyThoughtID());
            fcmMessage.setTitle("Leadership Platform"/*dailyThought.getSubtitle()*/);
            fcmMessage.setData(data);
            if (dailyThought.getDailyThoughtDescription().equalsIgnoreCase(DailyThoughtDTO.DESC_INTERNAL_DAILY_THOUGHT)) {
                endpointPresenter.sendDailyThought(dailyThought.getCompanyID(), payLoad);
            }
            if (dailyThought.getDailyThoughtDescription().equalsIgnoreCase(DailyThoughtDTO.DESC_GLOBAL_DAILY_THOUGHT)) {
                endpointPresenter.sendDailyThought(/*user*/dailyThought.getCompanyID(), payLoad);
                //    endpointPresenter.sendMessage(fcmMessage);
            }
        }

        showSnackBar("Thought Status: " + dailyThought.getStatus(), "Dismiss", "green");
       // crudPresenter.getPendingDailyThoughts("pending");
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
    public void onFCMUserSaved(FCMResponseDTO response) {

    }

    @Override
    public void onMessageSent(FCMResponseDTO response) {
        if (response.getStatusCode() == 0) {
            Log.i(TAG, "onMessageSent: daily thought sent" + response.getMessage());

        } else {
            Log.e(TAG, "onMessageSent: daily thought failed");
            FirebaseCrash.report(new Exception("daily thought failed"));
        }
    }

    @Override
    public void onEmailSent(EmailResponseDTO response) {

    }

    @Override
    public void onError(String message) {
        showSnackBar(message, "Dismiss", "red");
    }

    @Override
    public void onCategoryUpdated(CategoryDTO category) {

    }

    @Override
    public void onLinksRequired(BaseDTO entity) {

    }

    Snackbar snackbar;

    public void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(btn, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // snackbar.dismiss();
                Intent intent = new Intent(UpdateEntityActivity.this, DailyThoughtApprovalActivity.class);
                startActivity(intent);
                finish();
            }
        });
        snackbar.show();
    }


}
