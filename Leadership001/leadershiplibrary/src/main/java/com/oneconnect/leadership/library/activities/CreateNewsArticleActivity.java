package com.oneconnect.leadership.library.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.CreateDailyThoughtAdapter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.camera.VideoSelectionActivity;
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
import com.oneconnect.leadership.library.links.LinksActivity;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

public class CreateNewsArticleActivity extends AppCompatActivity implements CrudContract.View,
         EndpointContract.View{

    EditText editTitle, editSubtitle, editContent;
    Spinner Catspinner, selectedCatSpinner;
    Button btnDate, btn, btnDone, btnNewArticle;
    TextView selectTxt, countTxt, txtDelete, txtLinks, txtMicrophone, txtVideo, txtCamera;
    ImageView iconDelete, iconLink, iconMicrophone, iconVideo, iconCamera;
    LinearLayout iconLayout;
    String hexColor;
    Context ctx;
    CrudPresenter presenter;
    UserDTO user;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news_article);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        presenter = new CrudPresenter(this);
        ctx = getApplicationContext();

        if (getIntent().getSerializableExtra("hexColor") != null) {
            hexColor = (String) getIntent().getSerializableExtra("hexColor");
            toolbar.setBackgroundColor(Color.parseColor(hexColor));
        }
        if(getIntent().getSerializableExtra("user") != null) {
            user = (UserDTO) getIntent().getSerializableExtra("user");
            presenter.getCategories(user.getCompanyID());
        } else {
            presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
        }

        editTitle = (EditText) findViewById(R.id.editTitle);
        editTitle.setHint("Enter article title");
        editSubtitle = (EditText) findViewById(R.id.editSubtitle);
        editSubtitle.setHint("Enter author name");
        editContent = (TextInputEditText) findViewById(R.id.editContent);
        editContent.setHint("Enter article content");

        Catspinner = (Spinner) findViewById(R.id.Catspinner);
        selectedCatSpinner = (Spinner) findViewById(R.id.selectedCatSpinner);
        selectedCatSpinner.setVisibility(View.GONE);

        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTitle.setText(" ");
                editSubtitle.setText(" ");
                editTitle.setHint("Enter article title");
                editSubtitle.setHint("Enter author name");
                finish();
            }
        });
        btnDone.setVisibility(View.GONE);
        btnNewArticle = (Button) findViewById(R.id.btnNewArticle);
        btnNewArticle.setVisibility(View.GONE);

        selectTxt = (TextView) findViewById(R.id.selectTxt);
        selectTxt.setVisibility(GONE);
        countTxt = (TextView) findViewById(R.id.countTxt);
        countTxt.setVisibility(GONE);
        txtDelete = (TextView) findViewById(R.id.txtDelete);
        txtDelete.setVisibility(View.GONE);
        txtLinks = (TextView) findViewById(R.id.txtLinks);
        txtMicrophone = (TextView) findViewById(R.id.txtMicrophone);
        txtVideo = (TextView) findViewById(R.id.txtVideo);
        txtCamera = (TextView) findViewById(R.id.txtCamera);

        iconDelete = (ImageView) findViewById(R.id.iconDelete);
        iconDelete.setVisibility(View.GONE);
        iconLink = (ImageView) findViewById(R.id.iconLink);
        iconLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewsArticleActivity.this, LinksActivity.class);
                intent.putExtra("newsArticle", article);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                startActivity(intent);
            }
        });
        iconMicrophone = (ImageView) findViewById(R.id.iconMicrophone);
        iconMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewsArticleActivity.this, PodcastSelectionActivity.class);
                intent.putExtra("newsArticle", article);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                startActivity(intent);
            }
        });
        iconVideo = (ImageView) findViewById(R.id.iconVideo);
        iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewsArticleActivity.this, VideoSelectionActivity.class);
                intent.putExtra("newsArticle", article);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                startActivity(intent);
            }
        });
        iconCamera = (ImageView) findViewById(R.id.iconCamera);
        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewsArticleActivity.this, PhotoSelectionActivity.class);
                intent.putExtra("newsArticle", article);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                startActivity(intent);
            }
        });

        iconLayout = (LinearLayout) findViewById(R.id.iconLayout);
        iconLayout.setVisibility(GONE);

    }

    Date d;
    private DatePickerDialog datePickerDialog;
    TimePickerFragment timePickerFragment;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");

    private void getDate() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day);
                d = cal.getTime();
                timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "DIALOG_TIME");
                d = timePickerFragment.getSetTime(d);
                btnDate.setText(sdf.format(d));
            }
        }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
    NewsDTO article;

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

        if(Catspinner == null) {
            showSnackbar("Choose a category", "OK", "red");
            return;
        }

        Log.d(TAG, "send: @@@@@@@@@@@ starting to send article to Firebase");
        if (article == null) {
            article = new NewsDTO();
            if (user != null) {
                article.setCompanyID(user.getCompanyID());
                article.setCompanyName(user.getCompanyName());
                article.setJournalUserID(user.getUserID());
                article.setJournalUserName(user.getFullName());
            }

        }
        if (d != null) {
            article.setDateScheduled(d.getTime());
        }
        article.setTitle(editTitle.getText().toString());
        article.setSubtitle(editSubtitle.getText().toString());
        article.setBody(editContent.getText().toString());
        article.setTag(getTagsString());
        //article.setCategory(category);

        Log.w(TAG, "...sending to Firebase: ".concat(GSON.toJson(article)));
        presenter.addEntity(article);


    }

    private Snackbar snackbar;
    private Toolbar toolbar;
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

    List<String> selectedCategory = new ArrayList<>();

    private String getTagsString() {
        StringBuilder sb = new StringBuilder();
        if (!selectedCategory.isEmpty()) {
            // sb.append(", ");
        }

        int index = 0;
        for (String u : selectedCategory) {
            sb.append(u);
            if (index < selectedCategory.size()) {
                sb.append(", ");
            }
            index++;
        }
        Log.d(TAG, "getCategoriesString: ".concat(sb.toString()));

        return sb.toString();
    }

    static final String TAG = CreateNewsArticleActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onFCMUserSaved(FCMResponseDTO response) {

    }

    @Override
    public void onMessageSent(FCMResponseDTO response) {

    }

    @Override
    public void onEmailSent(EmailResponseDTO response) {

    }

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "******* onEntityAdded: article added to firebase "
                .concat(key));
        article.setNewsID(key);
        attachmentsQuestion(article);
    }

    private void attachmentsQuestion(NewsDTO article) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Add attachments")
                .setMessage("Would you like to add attachments to this article?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        iconLayout.setVisibility(View.VISIBLE);
                        btn.setVisibility(GONE);
                        btnDone.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editTitle.setText(" ");
                        editSubtitle.setText(" ");
                        editContent.setText(" ");
                        editTitle.setHint("Enter article title");
                        editSubtitle.setHint("Enter author name");
                        editContent.setHint("Enter article content");
                        btn.setVisibility(GONE);
                        btnDone.setVisibility(View.VISIBLE);
                        btnNewArticle.setVisibility(View.VISIBLE);

                        /*Toasty.warning(getApplicationContext(), "Media file(s) released",
                                Toast.LENGTH_LONG, true).show();*/
                    }
                })
                .show();
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
    List<CategoryDTO> categoryList = new ArrayList<>();
    @Override
    public void onCategories(List<CategoryDTO> list) {
        Log.i(TAG, "onCategores: " + list.size());
        this.categoryList = list;
        List<String> lis = new ArrayList<String>();
        lis.add("Select Category");
        for (CategoryDTO cat : categoryList) {
            lis.add(cat.getCategoryName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lis);
        Catspinner.setAdapter(adapter);
        Catspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                Log.d(TAG, "onItemSelected: category: " + categoryList.size() + " index: " + position);
                Log.i(TAG, "selectedItem: " + "\n" + "category: " + Catspinner.getSelectedItem().toString());

                Log.d(TAG, "onItemSelected: selectedCategory: ".concat(GSON.toJson(Catspinner.getSelectedItem().toString())));
                selectedCategory.add(Catspinner.getSelectedItem().toString());
                setAttendeeSpinner();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setAttendeeSpinner() {
        selectedCatSpinner.setVisibility(View.VISIBLE);
        countTxt.setVisibility(View.VISIBLE);
        selectTxt.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<>();
        if (!selectedCategory.isEmpty()) {
            list.add("Tap to remove category");
        }
        for (String c : selectedCategory) {
            list.add(c);
        }
        countTxt.setText(String.valueOf(selectedCategory.size()));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, list);
        selectedCatSpinner.setAdapter(adapter);
        selectedCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                if (selectedCategory.isEmpty()) {
                    return;
                }
                Log.d(TAG, "onItemSelected: category: " + selectedCategory.size() + " index: " + i);
                if (selectedCategory.size() == 1) {
                    selectedCategory.clear();
                } else {
                    boolean isFound = false;
                    int j = 0;
                    String f = selectedCategory.get(i - 1);
                    for (String u : selectedCategory) {
                        if (u.equalsIgnoreCase(f)) {
                            isFound = true;
                            break;
                        }
                        j++;
                    }
                    if (isFound) {
                        selectedCategory.remove(j);
                        setAttendeeSpinner();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
    public void onUserFound(UserDTO u) {
        presenter.getCategories(u.getCompanyID());
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

    @Override
    public void onLinksRequired(BaseDTO entity) {

    }
}
