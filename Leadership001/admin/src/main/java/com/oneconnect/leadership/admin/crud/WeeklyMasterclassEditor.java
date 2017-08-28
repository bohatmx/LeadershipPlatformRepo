package com.oneconnect.leadership.admin.crud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.activities.SheetContract;
import com.oneconnect.leadership.library.activities.SheetPresenter;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.cache.CategoryCache;
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
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by aubreymalabie on 3/18/17.
 */

public class WeeklyMasterclassEditor extends BaseBottomSheet
        implements SheetContract.View, SubscriberContract.View {
    private WeeklyMasterClassDTO weeklyMasterClass;
    private TextInputEditText editTitle, editSubtitle;
    private Button btnSend, btnDate;
    private Date selectedDate;
    private Spinner catSpinner;
    private SubscriberPresenter Catpresenter;
    List<CategoryDTO> categoryList;

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "******* onEntityAdded: daily thought added to firebase "
                .concat(key));
        weeklyMasterClass.setWeeklyMasterClassID(key);
        bottomSheetListener.onWorkDone(weeklyMasterClass);
        this.dismiss();
    }

    @Override
    public void onEntityUpdated() {

    }

    @Override
    public void onUserCreated(UserDTO user) {

    }

    @Override
    public void onAllRatings(List<RatingDTO> list) {

    }

    @Override
    public void onDailyThoughtRatings(List<RatingDTO> list) {

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
    public void onAllNewsArticle(List<NewsDTO> list) {

    }

    @Override
    public void onAllCategories(List<CategoryDTO> list) {

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
    public void onError(String message) {
         bottomSheetListener.onError(message);
    }


    static WeeklyMasterclassEditor newInstance(WeeklyMasterClassDTO weeklyMasterClass, int type) {
        WeeklyMasterclassEditor f = new WeeklyMasterclassEditor();
        Bundle args = new Bundle();
        args.putInt("type", type);
        if (weeklyMasterClass != null) {
            args.putSerializable("weeklyMasterClass", weeklyMasterClass);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weeklyMasterClass = (WeeklyMasterClassDTO) getArguments().getSerializable("weeklyMasterClass");
        type = getArguments().getInt("type", 0);
        presenter = new SheetPresenter(this);
        Catpresenter = new SubscriberPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.daily_thought_editor, container, false);
        btnSend = (Button) view.findViewById(R.id.btn);
        btnSend.setText("SUBMIT CLASS MESSAGE");
        catSpinner = (Spinner) view.findViewById(R.id.Catspinner);
        editTitle = (TextInputEditText) view.findViewById(R.id.editTitle);
        editTitle.setHint("Enter class message");
        editSubtitle = (TextInputEditText) view.findViewById(R.id.editSubtitle);
        editSubtitle.setHint("Enter author name");
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
        btnDate = (Button) view.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListener.onDateRequired();
            }
        });

        getCachedCategories();
        getCategories();
        return view;
    }
    public void getCategories() {
        // Log.d(LOG, "******* getCategories: ");
        Catpresenter.getAllCategories();/*getCategories();*/
    }

    public void getCachedCategories() {
        CategoryCache.getCategories(getActivity(), new CategoryCache.ReadListener() {
            @Override
            public void onDataRead(List<CategoryDTO> categories) {
                Log.d(LOG, "onDataRead: Categories: " + categories);
                categoryList = categories;
                setCategorySpinner();
            }

            @Override
            public void onError(String message) {
                getCachedCategories();
            }
        });
    }
    public String LOG = WeeklyMasterclassEditor.class.getSimpleName();
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

    private CategoryDTO category;

    private boolean isReadyToSend;

    private void send() {
        if (TextUtils.isEmpty(editTitle.getText())) {
            editTitle.setError(getString(R.string.enter_class_message));
            return;
        }
        if (TextUtils.isEmpty(editSubtitle.getText())) {
            editTitle.setError(getString(R.string.enter_author_name));
            return;
        }
        if(catSpinner == null) {
            isReadyToSend = true;
            bottomSheetListener.onError("Choose a category");
            return;
        }

        Log.d(TAG, "send: @@@@@@@@@@@ starting to send weeky message to Firebase");
        if (weeklyMasterClass == null) {
            weeklyMasterClass = new WeeklyMasterClassDTO();
            UserDTO me = SharedPrefUtil.getUser(getActivity());
            weeklyMasterClass.setCompanyID(me.getCompanyID());
            weeklyMasterClass.setCompanyName(me.getCompanyName());
            weeklyMasterClass.setActive(true);
            weeklyMasterClass.setJournalUserID(me.getUserID());
            weeklyMasterClass.setJournalUserName(me.getFullName());

        }
        if (selectedDate == null) {
            isReadyToSend = true;
            bottomSheetListener.onDateRequired();
            return;
        } else {
            weeklyMasterClass.setDateScheduled(selectedDate.getTime());
        }
        weeklyMasterClass.setTitle(editTitle.getText().toString());
        weeklyMasterClass.setSubtitle(editSubtitle.getText().toString());
        weeklyMasterClass.setCategory(category);

        switch (type) {
            case Constants.NEW_ENTITY:
                Log.w(TAG, "...sending to Firebase: ".concat(GSON.toJson(weeklyMasterClass)));
                presenter.addEntity(weeklyMasterClass);
                break;
            case Constants.UPDATE_ENTITY:

                break;
            case Constants.DELETE_ENTITY:

                break;
        }
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = Util.getDateAtMidnite(selectedDate);
        btnDate.setText(sdf.format(this.selectedDate));
        if (weeklyMasterClass != null) {
            weeklyMasterClass.setDateScheduled(this.selectedDate.getTime());
            if (isReadyToSend) {
                isReadyToSend = false;
                send();
            }

        }
    }

    public static final String TAG = WeeklyMasterclassEditor.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");

}
