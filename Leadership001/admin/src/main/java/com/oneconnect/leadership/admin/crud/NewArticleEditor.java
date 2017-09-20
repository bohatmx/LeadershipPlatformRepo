package com.oneconnect.leadership.admin.crud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
 * Created by Kurisani on 2017/06/26.
 */

public class NewArticleEditor extends BaseBottomSheet implements SheetContract.View, SubscriberContract.View {
    private NewsDTO article;
    private TextInputEditText editTitle, editSubtitle, editContent;
    private RecyclerView recyclerView;
    private ImageView iconCamera, iconVideo, iconDate, iconURLs;
    private View iconLayout;
    private Button btn, btnDate;
    private Date selectedDate;
    private Spinner catSpinner;
    private SubscriberPresenter Catpresenter;

    List<CategoryDTO> categoryList;

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "******* onEntityAdded: news article added to firebase "
                .concat(key));
        article.setNewsID(key);
        bottomSheetListener.onWorkDone(article);
        this.dismiss();
    }

    @Override
    public void onEntityUpdated() {

    }

    @Override
    public void onUserCreated(UserDTO user) {

    }

    @Override
    public void onUserFound(UserDTO user) {

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

    @Override
    public void onAllCategories(List<CategoryDTO> list) {
        Log.i(TAG, "*** onAllCategories ***");
        List<String> lis = new ArrayList<String>();
        lis.add("Select Category");
        //  Collections.sort(list);

        for (CategoryDTO cat : list) {
            lis.add(cat.getCategoryName());
            category = cat;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lis);
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

    static NewArticleEditor newInstance(NewsDTO article, int type) {
        NewArticleEditor f = new NewArticleEditor();
        Bundle args = new Bundle();
        args.putInt("type", type);
        if (article != null) {
            args.putSerializable("article", article);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        article = (NewsDTO) getArguments().getSerializable("newsArticle");
        type = getArguments().getInt("type", 0);
        presenter = new SheetPresenter(this);
        Catpresenter = new SubscriberPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_article_editor, container, false);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setText("SUBMIT ARTICLE");
        catSpinner = (Spinner) view.findViewById(R.id.Catspinner);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        editTitle = (TextInputEditText) view.findViewById(R.id.editTitle);
        editTitle.setHint("Enter article title");
        editSubtitle = (TextInputEditText) view.findViewById(R.id.editSubtitle);
        editSubtitle.setHint("Enter author name");
        editContent = (TextInputEditText) view.findViewById(R.id.editContent);
        editContent.setHint("Enter article content");
        btnDate = (Button) view.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListener.onDateRequired();
            }
        });

        getCategories();
        return view;
    }

    public void getCategories() {
        // Log.d(LOG, "******* getCategories: ");
        Catpresenter.getAllCategories();/*getCategories();*/
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

    private CategoryDTO category;

    private boolean isReadyToSend;

    private void send() {
        if (TextUtils.isEmpty(editTitle.getText())) {
            editTitle.setError(getString(R.string.enter_news_content));
            return;
        }
        if (TextUtils.isEmpty(editSubtitle.getText())) {
            editTitle.setError(getString(R.string.enter_author_name));
            return;
        }
        if (TextUtils.isEmpty(editContent.getText())) {
            editContent.setError(getString(R.string.enter_article_content));
            return;
        }
        Log.d(TAG, "send: @@@@@@@@@@@ starting to send daily thought to Firebase");
        if (article == null) {
            article = new NewsDTO();
            UserDTO me = SharedPrefUtil.getUser(getActivity());
            article.setCompanyID(me.getCompanyID());
            article.setCompanyName(me.getCompanyName());
            //article.setActive(true);
            article.setJournalUserID(me.getUserID());
            article.setJournalUserName(me.getFullName());

        }
        if (selectedDate == null) {
            isReadyToSend = true;
            bottomSheetListener.onDateRequired();
            return;
        } else {
            article.setDateScheduled(selectedDate.getTime());
        }

        if(catSpinner == null) {
            isReadyToSend = true;
            bottomSheetListener.onError("Choose a category");
            return;
        }
        article.setTitle(editTitle.getText().toString());
        article.setSubtitle(editSubtitle.getText().toString());
        article.setBody(editContent.getText().toString());
        article.setCategory(category);

        switch (type) {
            case Constants.NEW_ENTITY:
                Log.w(TAG, "...sending to Firebase: ".concat(GSON.toJson(article)));
                presenter.addEntity(article);
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
        if (article != null) {
            article.setDateScheduled(this.selectedDate.getTime());
            if (isReadyToSend) {
                isReadyToSend = false;
                send();
            }

        }


    }
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
    public static final String TAG = NewArticleEditor.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
