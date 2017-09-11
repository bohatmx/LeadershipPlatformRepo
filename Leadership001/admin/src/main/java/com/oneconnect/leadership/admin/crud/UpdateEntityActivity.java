package com.oneconnect.leadership.admin.crud;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.crud.CrudContract;
import com.oneconnect.leadership.library.crud.CrudPresenter;
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
import com.oneconnect.leadership.library.util.Util;

import java.util.Date;
import java.util.List;


public class UpdateEntityActivity extends AppCompatActivity implements CrudContract.View{

    private DailyThoughtDTO dailyThought;
    private WeeklyMasterClassDTO masterClass;
    private WeeklyMessageDTO weeklyMessage;
    private NewsDTO news;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entity);
        crudPresenter = new CrudPresenter(this);

        btn = (Button) findViewById(R.id.btn);
        editTitle = (TextInputEditText) findViewById(R.id.editTitle);
        editSubtitle = (TextInputEditText) findViewById(R.id.editSubtitle);
      //  spinner = (Spinner) findViewById(R.id.spinner);
       // spinner.setVisibility(View.GONE);

        if (getIntent().getSerializableExtra("dailyThought") != null){
            dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
            editTitle.setText(dailyThought.getTitle());
            editSubtitle.setText(dailyThought.getSubtitle());
          //  spinner.setSelection(dailyThought.getCategories().size());
        }
        if (getIntent().getSerializableExtra("masterClass") != null){
            masterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("masterClass");
            editTitle.setText(masterClass.getTitle());
            editSubtitle.setText(masterClass.getSubtitle());
            //  spinner.setSelection(dailyThought.getCategories().size());
        }
        if (getIntent().getSerializableExtra("weeklyMessage") != null){
            weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
            editTitle.setText(weeklyMessage.getTitle());
            editSubtitle.setText(weeklyMessage.getSubtitle());
            //  spinner.setSelection(dailyThought.getCategories().size());
        }
        if (getIntent().getSerializableExtra("news") != null){
            news = (NewsDTO) getIntent().getSerializableExtra("news");
            editTitle.setText(news.getTitle());
            editSubtitle.setText(news.getSubtitle());
            //  spinner.setSelection(dailyThought.getCategories().size());
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(btn, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        //update();
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
                        if (news != null){
                            update();
                            return;
                        }

                    }
                });

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

        if (dailyThought != null) {
            dailyThought.setTitle(editTitle.getText().toString());
            dailyThought.setSubtitle(editSubtitle.getText().toString());
            crudPresenter.updateDailyThought(dailyThought);
            finish();
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
    public void onNewsUpdated(NewsDTO news) {

    }

    @Override
    public void onUserDeleted(UserDTO user) {

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
    public void onError(String message) {

    }

    @Override
    public void onCategoryUpdated(CategoryDTO category) {

    }
}
