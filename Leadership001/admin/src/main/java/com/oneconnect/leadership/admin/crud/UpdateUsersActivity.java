package com.oneconnect.leadership.admin.crud;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
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
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.UpdateContract;
import com.oneconnect.leadership.library.util.UpdatePresenter;
import com.oneconnect.leadership.library.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kurisani on 2017/08/30.
 */

public class UpdateUsersActivity extends AppCompatActivity implements CrudContract.View {

    private UserDTO user;
    private TextInputEditText eFirst,eLast,eMail;
    private Spinner spinner;
    private Button btn;
    private CrudPresenter crudPresenter;
    Context ctx;
    private CategoryDTO category;
    private SubscriptionDTO subscription;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_update_editor);
        crudPresenter = new CrudPresenter(this);

        btn = (Button) findViewById(R.id.btn);
        eFirst = (TextInputEditText) findViewById(R.id.editFirstName);
        eLast = (TextInputEditText) findViewById(R.id.editLastName);
        eMail = (TextInputEditText) findViewById(R.id.editEmail);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        if (getIntent().getSerializableExtra("user") != null){
            user = (UserDTO) getIntent().getSerializableExtra("user");
            eFirst.setText(user.getFirstName());
            eLast.setText(user.getLastName());
            eMail.setText(user.getEmail());
            spinner.setSelection(user.getUserType());
        } if (getIntent().getSerializableExtra("category") != null) {
            category = (CategoryDTO) getIntent().getSerializableExtra("category");
            eFirst.setText(category.getCategoryName());
            eLast.setVisibility(View.GONE);
            eMail.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
        }if (getIntent().getSerializableExtra("subscription") != null) {
            subscription = (SubscriptionDTO) getIntent().getSerializableExtra("subscription");
            eFirst.setText(subscription.getSubscriptionName());
            eLast.setVisibility(View.GONE);
            eMail.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.flashOnce(btn, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (user != null){
                            update();
                            finish();
                            return;
                        }
                        if (category!= null){
                            updateCategory();
                            finish();
                            return;
                        }
                        if (subscription!= null){
                            updateSubscription();
                            finish();
                            return;
                        }
                    }
                });

            }
        });
    }


    static final String LOG = UpdateUsersActivity.class.getSimpleName();

    private void updateCategory() {
        Log.i(LOG, "updating category");

        if (TextUtils.isEmpty(eFirst.getText())) {
            eFirst.setError("Enter category name");
            return;
        }

        if (category != null) {
            category.setCategoryName(eFirst.getText().toString());
            crudPresenter.updateCategory(category);
            return;
        }

    }

    private void updateSubscription() {
        Log.i(LOG, "updating subscription");

        if (TextUtils.isEmpty(eFirst.getText())) {
            eFirst.setError("Enter subscription name");
            return;
        }
        if (TextUtils.isEmpty(eLast.getText())) {
            eLast.setError("Enter subscription Amount");
            return;
        }

        if (subscription != null) {
            subscription.setSubscriptionName(eFirst.getText().toString());
            subscription.setAmount(eLast.getInputType());
            crudPresenter.updateSubscription(subscription);
            return;
        }

    }
    private void update() {

        if (TextUtils.isEmpty(eFirst.getText())) {
            eFirst.setError("Enter first name");
            return;
        }
        if (TextUtils.isEmpty(eLast.getText())) {
            eLast.setError("Enter last name");
            return;
        }
        if (TextUtils.isEmpty(eMail.getText())) {
            eMail.setError("Enter email address");
            return;
        }
        if (user != null) {
            user.setFirstName(eFirst.getText().toString());
            user.setLastName(eLast.getText().toString());
            crudPresenter.updateUser(user);
            return;
        }
    }
    int userType;

    public void setCrudPresenter(CrudPresenter crudPresenter) {
        this.crudPresenter = crudPresenter;
    }
    private void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Select User Type");
        list.add(UserDTO.DESC_STAFF);
        list.add(UserDTO.DESC_SUBSCRIBER);
        list.add(UserDTO.DESC_LEADER);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ctx,android.R.layout.simple_list_item_1,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        userType = 0;
                        break;
                    case 1:
                        userType = UserDTO.COMPANY_STAFF;
                        break;
                    case 2:
                        userType = UserDTO.SUBSCRIBER;
                        break;
                    case 3:
                        userType = UserDTO.LEADER;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
    public void onCategoryUpdated(CategoryDTO category) {

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
}

