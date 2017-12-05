package com.oneconnect.leadership.library.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ocg.backend.endpointAPI.model.Data;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.ocg.backend.endpointAPI.model.FCMessageDTO;
import com.ocg.backend.endpointAPI.model.PayLoad;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.CreateDailyThoughtAdapter;
import com.oneconnect.leadership.library.adapters.MyDailyThoughtAdapter;
import com.oneconnect.leadership.library.audio.PodcastSelectionActivity;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
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
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.editors.DailyThoughtEditor;
import com.oneconnect.leadership.library.fcm.EndpointContract;
import com.oneconnect.leadership.library.fcm.EndpointPresenter;
import com.oneconnect.leadership.library.links.LinksActivity;
import com.oneconnect.leadership.library.lists.BaseListingFragment;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.photo.PhotoSelectionActivity;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.TimePickerFragment;
import com.oneconnect.leadership.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;
import static com.oneconnect.leadership.library.ebook.EbookListFragment.REQUEST_LINKS;
import static com.oneconnect.leadership.library.fcm.EndpointUtil.DAILY_THOUGHT;

public class CreateDailyThoughtActivity extends AppCompatActivity implements CrudContract.View,
        CreateDailyThoughtAdapter.CreateThoughtListener, EndpointContract.View {

    CrudPresenter presenter;
    private CachePresenter cachePresenter;
    private Context ctx;
    ResponseBag bag;
    private UserDTO user;
    private int type;
    RecyclerView recyclerView;
    EditText editTitle, editSubtitle;
    ImageView iconDelete, iconLink, iconMicrophone, iconVideo, iconCamera;
    TextView txtLinks ,txtMicrophone, txtVideo, txtCamera, countTxt, selectTxt;
    MyDailyThoughtAdapter adapter;
    LinearLayout iconLayout;
    Button btn, btnDate, btnDone, btnNewThought;
    private RadioButton internalButton, globalButton;
    private Spinner catSpinner, selectedCatSpinner;
    CategoryDTO category;
    String hexColor;
    FirebaseAuth firebaseAuth;

    Data data;
    PayLoad payLoad;
    EndpointPresenter endpointPresenter;
    FCMessageDTO fcmMessage;
    FCMUserDTO fcmUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "*** onCreate ***");
        setContentView(R.layout.activity_create_daily_thought);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();
        firebaseAuth = FirebaseAuth.getInstance();

        if (getIntent().getSerializableExtra("user") != null) {
            user = (UserDTO) getIntent().getSerializableExtra("user");
        }

        if (getIntent().getSerializableExtra("hexColor") != null) {
            hexColor = (String) getIntent().getSerializableExtra("hexColor");
            toolbar.setBackgroundColor(Color.parseColor(hexColor));
        }


        btn = (Button) findViewById(R.id.btn);
        btnDone = (Button) findViewById(R.id.btnDone);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTitle.setText(" ");
                editSubtitle.setText(" ");
                editTitle.setHint("Enter Thought");
                editSubtitle.setHint("Enter thought author");
                finish();
            }
        });
        btnDone.setVisibility(GONE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        btnNewThought = (Button) findViewById(R.id.btnNewThought);
        btnNewThought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.GONE);
                btnNewThought.setVisibility(View.GONE);
            }
        });
        btnNewThought.setVisibility(GONE);

        iconLayout = (LinearLayout) findViewById(R.id.iconLayout);
        iconLayout.setVisibility(GONE);
        iconDelete = (ImageView) findViewById(R.id.iconDelete);
        iconDelete.setVisibility(GONE);
        iconLink = (ImageView) findViewById(R.id.iconLink);
        txtLinks = (TextView) findViewById(R.id.txtLinks);
        iconLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateDailyThoughtActivity.this, LinksActivity.class);
                intent.putExtra("dailyThought", dailyThought);
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
                Intent intent = new Intent(CreateDailyThoughtActivity.this, PodcastSelectionActivity.class);
                intent.putExtra("dailyThought", dailyThought);
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
                Intent intent = new Intent(CreateDailyThoughtActivity.this, VideoSelectionActivity.class);
                intent.putExtra("dailyThought", dailyThought);
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
                Intent intent = new Intent(CreateDailyThoughtActivity.this, PhotoSelectionActivity.class);
                intent.putExtra("dailyThought", dailyThought);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                startActivity(intent);
            }
        });

        txtLinks = (TextView) findViewById(R.id.txtLinks);
        txtMicrophone = (TextView) findViewById(R.id.txtMicrophone);
        txtVideo = (TextView) findViewById(R.id.txtVideo);
        txtCamera = (TextView) findViewById(R.id.txtCamera);
        if (dailyThought != null) {
            if (dailyThought.getUrls() != null) {
                txtLinks.setText(dailyThought.getUrls().size());
            }
            if (dailyThought.getPodcasts() != null) {
                txtMicrophone.setText(dailyThought.getPodcasts().size());
            }
            if (dailyThought.getVideos() != null) {
                txtVideo.setText(dailyThought.getVideos().size());
            }
            if (dailyThought.getPhotos() != null) {
                txtCamera.setText(dailyThought.getPhotos().size());
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editTitle = (EditText) findViewById(R.id.editTitle);
        editTitle.setHint("Enter Thought");
        editSubtitle = (EditText) findViewById(R.id.editSubtitle);
        editSubtitle.setHint("Enter thought author");

        internalButton = (RadioButton) findViewById(R.id.internalButton);
        globalButton = (RadioButton) findViewById(R.id.globalButton);

        catSpinner = (Spinner) findViewById(R.id.Catspinner);
        selectedCatSpinner = (Spinner) findViewById(R.id.selectedCatSpinner);
        selectedCatSpinner.setVisibility(GONE);
        countTxt = (TextView) findViewById(R.id.countTxt);
        countTxt.setVisibility(GONE);

        selectTxt = (TextView) findViewById(R.id.selectTxt);
        selectTxt.setVisibility(GONE);

        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDate();
            }
        });

        presenter = new CrudPresenter(this);
        endpointPresenter = new EndpointPresenter(this);

        if (user != null) {
            presenter.getCategories(user.getCompanyID());
        } else {
            presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
        }

    }

    TimePickerFragment timePickerFragment;
    TimePickerDialog timePickerDialog;
    int hours, minute;

    public void setSelectedDate(Date selectedDate) {
        timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getFragmentManager(), "DIALOG_TIME");
        // timePickerFragment.getSetTime(selectedDate);

        this.selectedDate  = timePickerFragment.getSetTime(selectedDate)/*selectedDate*//*Util.getDateAtMidnite(selectedDate)*/;
        btnDate.setText(sdf.format(this.selectedDate));
        if (dailyThought != null) {
            dailyThought.setDateScheduled(this.selectedDate.getTime());
            if (isReadyToSend) {
                isReadyToSend = false;
                send();
            }

        }
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
            showSnackbar("Choose a category", "OK", "red");
        //    bottomSheetListener.onError("Choose a category");
            return;
        }

        Log.d(TAG, "send: @@@@@@@@@@@ starting to send daily thought to Firebase");

        if (dailyThought == null) {
            dailyThought = new DailyThoughtDTO();
           // UserDTO me = SharedPrefUtil.getUser(getActivity());
            if (user != null) {
                dailyThought.setCompanyID(user.getCompanyID());
                dailyThought.setCompanyName(user.getCompanyName());
                // dailyThought.setUser(me);

                dailyThought.setActive(true);
                dailyThought.setJournalUserID(user.getUserID());
                dailyThought.setJournalUserName(user.getFirstName() + " " + user.getLastName());
            } //else if (user)


        }
        /*if (selectedDate == null) {
            isReadyToSend = true;
         //   bottomSheetListener.onDateRequired();
            return;
        } *//*else {

        }*/
        if (d != null) {
            dailyThought.setDateScheduled(d.getTime()/*getDate()*//*();selectedDate.getTime()*/);
        }
        // category.setCategoryName(/*catSpinner.getSelectedItem().toString()*/);
        dailyThought.setTitle(editTitle.getText().toString());
        dailyThought.setSubtitle(editSubtitle.getText().toString());
        if (category != null) {
        dailyThought.setCategory(category);
        }


        if (internalButton.isChecked()) {
            dailyThought.setDailyThoughtDescription(DailyThoughtDTO.DESC_INTERNAL_DAILY_THOUGHT);
            dailyThought.setDailyThoughtType(DailyThoughtDTO.INTERNAL_DAILY_THOUGHT);
            if (user.getUserType() == UserDTO.PLATINUM_USER) {
                dailyThought.setDailyThoughtType_status(Constants.INTERNAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
            }
            else if (user.getUserType() == UserDTO.PLATINUM_ADMIN) {
                dailyThought.setDailyThoughtType_status(Constants.INTERNAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
            }
            else if (user.getUserType() == UserDTO.COMPANY_ADMIN) {
                dailyThought.setDailyThoughtType_status(Constants.INTERNAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
            }
            else if (user.getUserType() == UserDTO.GOLD_USER) {
                dailyThought.setDailyThoughtType_status(Constants.INTERNAL_DAILY_THOUGHT.concat("_").concat(Constants.PENDING));
            }
        }
        if (globalButton.isChecked()) {
            dailyThought.setDailyThoughtDescription(DailyThoughtDTO.DESC_GLOBAL_DAILY_THOUGHT);
            dailyThought.setDailyThoughtType(DailyThoughtDTO.GLOBAL_DAILY_THOUGHT);
            if (user.getUserType() == UserDTO.PLATINUM_USER) {
                dailyThought.setDailyThoughtType_status(Constants.GLOBAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
            }
            else if (user.getUserType() == UserDTO.PLATINUM_ADMIN) {
                dailyThought.setDailyThoughtType_status(Constants.GLOBAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
            }
            else if (user.getUserType() == UserDTO.COMPANY_ADMIN) {
                dailyThought.setDailyThoughtType_status(Constants.GLOBAL_DAILY_THOUGHT.concat("_").concat(Constants.APPROVED));
            }
            else if (user.getUserType() == UserDTO.GOLD_USER) {
                dailyThought.setDailyThoughtType_status(Constants.GLOBAL_DAILY_THOUGHT.concat("_").concat(Constants.PENDING));
            }
        }
        if (user.getUserType() == UserDTO.PLATINUM_USER) {
            dailyThought.setStatus(Constants.APPROVED);
            dailyThought.setUserType(UserDTO.DESC_PLATINUM_USER);
            dailyThought.setCompanyID_status(user.getCompanyID().concat("_").concat(Constants.APPROVED));
            //  dailyThought.setDailyThoughtType_status();
        }
        else if(user.getUserType() == UserDTO.PLATINUM_ADMIN){
            dailyThought.setStatus(Constants.APPROVED);
            dailyThought.setUserType(UserDTO.DESC_PLATINUM_ADMIN);
            dailyThought.setCompanyID_status(user.getCompanyID().concat("_").concat(Constants.APPROVED));
        }
        else if (user.getUserType() == UserDTO.GOLD_USER) {
            dailyThought.setStatus(Constants.PENDING);
            dailyThought.setCompanyID_status(user.getCompanyID().concat("_").concat(Constants.PENDING));
        }
        else if (user.getUserType() == UserDTO.COMPANY_ADMIN) {
            dailyThought.setStatus(Constants.APPROVED);
            dailyThought.setCompanyID_status(user.getCompanyID().concat("_").concat(Constants.APPROVED));
        }

        dailyThought.setTags(getActionsString());

        presenter.addEntity(dailyThought);
    }

    private boolean isReadyToSend;
    private Date selectedDate;
    Date d;

    private void getDate() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day);
               /* Date */d = cal.getTime();
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

    @Override
    public void onAddEntity() {
        Log.w(TAG, "onAddEntity: ........open bottom appropriate sheet");
       /* switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);
                break;
        }*/
    }

    /*private void startDailyThoughtBottomSheet(final DailyThoughtDTO thought, int type) {

        dailyThoughtEditor = DailyThoughtEditor.newInstance(thought, type);
        dailyThoughtEditor.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                DailyThoughtDTO m = (DailyThoughtDTO) entity;
                *//*if (bag.getDailyThoughts() == null) {
                    bag.setDailyThoughts(new ArrayList<DailyThoughtDTO>());
                }
                bag.getDailyThoughts().add(0, m);*//*
                // setFragment();
                showSnackbar(m.getTitle().concat(" is being added"), getString(R
                        .string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                getDate(ResponseBag.DAILY_THOUGHTS);
            }

            @Override
            public void onError(String message) {
                showSnackbar(message, "bad", Constants.RED);
            }
        });

        dailyThoughtEditor.show(getSupportFragmentManager(), "SHEET_DAILY_THOUGHT");

    }*/



    private DatePickerDialog datePickerDialog;
    /*private void getDate(final int sheetType) {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(ctx*//*this*//*, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day);
                Date d = cal.getTime();
                *//*switch (sheetType) {
                    case ResponseBag.DAILY_THOUGHTS:
                        dailyThoughtEditor.setSelectedDate(d);
                        break;
                }*//*
            }
        }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }*/
    @Override
    public void onDeleteClicked(BaseDTO entity) {
        Log.w(TAG, "onDeleteClicked: ..............");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
    }

    @Override
    public void onDeleteUser(UserDTO user) {

    }
    private CrudPresenter presenterCrud;
    @Override
    public void onDeleteDailyThought(DailyThoughtDTO dailyThought) {
        Log.i(TAG, "onDeleteDailyThought");
        presenterCrud.deleteDailyThought(dailyThought);
    }

    @Override
    public void onDeleteWeeklyMessage(WeeklyMessageDTO weeklyMessage) {

    }

    @Override
    public void onDeleteWeeklyMasterClass(WeeklyMasterClassDTO masterClass) {

    }

    @Override
    public void onDeletePodcast(PodcastDTO podcast) {

    }

    @Override
    public void onDeleteNews(NewsDTO news) {

    }

    @Override
    public void onDeleteVideo(VideoDTO video) {

    }

    @Override
    public void onDeleteEbook(EBookDTO eBook) {

    }

    @Override
    public void onDeleteCategory(CategoryDTO category) {

    }

    @Override
    public void onDeleteSubscription(SubscriptionDTO subscription) {

    }
    boolean isTooltip;
    private DailyThoughtDTO dailyThought;

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "******* onEntityAdded: daily thought added to firebase "
                .concat(key));
        dailyThought.setDailyThoughtID(key);
        if (dailyThought.getStatus().equalsIgnoreCase(Constants.APPROVED)) {
            data = new Data();
            fcmUser = new FCMUserDTO();
            payLoad = new PayLoad();
            fcmMessage = new FCMessageDTO();
            data.setUserID(user.getUserID());
            data.setTitle(dailyThought.getSubtitle());
            data.setFromUser(user.getFullName());
            data.setMessageType(DAILY_THOUGHT);
            data.setMessage(dailyThought.getTitle() /*+ " - " + dailyThought.getSubtitle()*/);
            data.setDate(new Date().getTime());
            payLoad.setData(data);
            fcmMessage.setCompanyID(user.getCompanyID());
            fcmMessage.setDailyThoughtID(dailyThought.getDailyThoughtID());
            fcmMessage.setTitle(dailyThought.getSubtitle());
            fcmMessage.setData(data);
            if (dailyThought.getDailyThoughtDescription().equalsIgnoreCase(DailyThoughtDTO.DESC_INTERNAL_DAILY_THOUGHT)) {
                endpointPresenter.sendDailyThought(user.getCompanyID(), payLoad);
            }
            if (dailyThought.getDailyThoughtDescription().equalsIgnoreCase(DailyThoughtDTO.DESC_GLOBAL_DAILY_THOUGHT)) {
                endpointPresenter.sendDailyThought(user.getCompanyID(), payLoad);
                //    endpointPresenter.sendMessage(fcmMessage);
            }
        }
        attachmentsQuestion(dailyThought);
    }

    private void attachmentsQuestion(DailyThoughtDTO thought) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Add attachments")
                .setMessage("Would you like to add attachments to this thought?")
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
                        editTitle.setHint("Enter Thought");
                        editSubtitle.setHint("Enter thought author");
                        btn.setVisibility(GONE);
                        btnDone.setVisibility(View.VISIBLE);
                        btnNewThought.setVisibility(View.VISIBLE);

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

    private int i = 0;
    List<CategoryDTO> categoryList = new ArrayList<>();
    List<String> selectedCategory = new ArrayList<>();

    @Override
    public void onCategories(List<CategoryDTO> list) {
        Log.i(TAG, "onCategores: " + list.size());
        this.categoryList = list;
        List<String> lis = new ArrayList<String>();
        lis.add("Select Category");
        //  Collections.sort(list);
        for (CategoryDTO cat : categoryList) {
            lis.add(cat.getCategoryName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lis);
        catSpinner.setAdapter(adapter);
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (/*i*/position == 0) {
                    return;
                }
               /* if (categoryList.isEmpty()) {
                    Log.i(TAG, "category list is null");
                    return;
                }*/
                Log.d(TAG, "onItemSelected: category: " + categoryList.size() + " index: " + position);
                Log.i(TAG, "selectedItem: " + "\n" + "category: " + catSpinner.getSelectedItem().toString());

                //CategoryDTO name = categoryList.get(i/*- 1*/);
                Log.d(TAG, "onItemSelected: selectedCategory: ".concat(GSON.toJson(catSpinner.getSelectedItem().toString()/*name*/)));
                selectedCategory.add(catSpinner.getSelectedItem().toString()/*name*/);
                setAttendeeSpinner();

                /*if (categoryList.size() == 1) {
                    categoryList.clear();
                } else {
                    boolean isFound = false;
                    int j = 0;
                    CategoryDTO cat = categoryList.get(i - 1);
                    for (CategoryDTO u : categoryList) {
                        if (u.getCategoryID().equalsIgnoreCase(cat.getCategoryID())) {

                            Log.i(TAG, "onItemSelected: ".concat(GSON.toJson(cat)));
                            categoryList.add(cat);
                            category = cat;
                            isFound = true;
                            break;
                        }
                        j++;
                    }
                    if (isFound) {
                        categoryList.remove(j);
                        setCategorySpinner();
                    }
                }*/

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

    private String getActionsString() {
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

    private void setCategorySpinner() {
        List<String> list = new ArrayList<String>();
        list.add("Select Category");
        //   Collections.sort(categoryList);

        for (CategoryDTO cat : categoryList) {
            list.add(cat.getCategoryName());
            //    category = cat;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        catSpinner.setAdapter(adapter);
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (i == 0) {
                    return;
                }
                if (categoryList.isEmpty()) {
                    Log.i(TAG, "No categories");
                    return;
                }
                Log.d(TAG, "onItemSelected: categories: " + categoryList.size() + " index: " + i);
                if (categoryList.size() == 1) {
                    categoryList.clear();
                } else {
                    boolean isFound = false;
                    int j = 0;
                    CategoryDTO c = categoryList.get(i - 1);
                    // category = c;
                    for (CategoryDTO ca : categoryList) {
                        if (ca.getCategoryID().equalsIgnoreCase(c.getCategoryID())) {
                            isFound = true;
                            break;
                        }
                        j++;
                    }
                    if (isFound) {
                        categoryList.remove(j);
                        setCategorySpinner();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onCompanies(List<CompanyDTO> list) {

    }

    @Override
    public void onDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "onDailyThoughts: " + list.size());
        adapter = new MyDailyThoughtAdapter(ctx, list, new MyDailyThoughtAdapter.MyDailyThoughtAdapterlistener() {
            @Override
            public void onPhotoRequired(BaseDTO base) {

            }

            @Override
            public void onPodcastRequired(BaseDTO base) {

            }

            @Override
            public void onVideoRequired(BaseDTO base) {

            }

            @Override
            public void onLinkRequired(BaseDTO base) {

            }

            @Override
            public void onDeleteDailyThought(DailyThoughtDTO dailyThought) {

            }


        });

        recyclerView.setAdapter(adapter);
       /* bag = new ResponseBag();
        bag.setDailyThoughts(list);
        Collections.sort(bag.getDailyThoughts());
        bag.setType(ResponseBag.DAILY_THOUGHTS);
        setFragment();
        cachePresenter.cacheDailyThoughts(list);*/
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
        user = u;
        if (user.getUserType() == UserDTO.GOLD_USER) {
            globalButton.setVisibility(GONE);
        }
        if (user.getUserType() == UserDTO.COMPANY_ADMIN) {
            globalButton.setVisibility(GONE);
        }
        if (user.getUserType() == UserDTO.PLATINUM_ADMIN) {
            globalButton.setVisibility(GONE);
        }
        presenter.getCategories(user.getCompanyID());

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

    }

    @Override
    public void onCategoryUpdated(CategoryDTO category) {

    }

    @Override
    public void onLinksRequired(BaseDTO entity) {
        Log.w(TAG, "onLinksRequired: ..............");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        Intent m = null;
        switch (type) {

            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                m = new Intent(this, LinksActivity.class);
                m.putExtra("dailyThought", dailyThought);
                break;

        }
        m.putExtra("type", type);
        startActivityForResult(m, REQUEST_LINKS);
    }

    @Override
    public void onPhotoCaptureRequested(BaseDTO entity) {
        Log.w(TAG, "onPhotoCaptureRequested: .................");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        pickGalleryOrCamera(entity);
    }
    private void pickGalleryOrCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Select Images")
                .setMessage("Please select the source of the photos")
                .setPositiveButton("Use Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startCamera(base);
                    }
                }).setNegativeButton("Use Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                // startActivityForResult(intent, RESULT_LOAD_IMG);
                startPhotoGallerySelection(base);

            }
        }).show();
    }
    private void startCamera(BaseDTO entity) {

        Intent m = new Intent(this, CameraActivity.class);
        m.putExtra("type", CameraActivity.CAMERA_REQUEST);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                m.putExtra("dailyThought", dailyThought);
                Log.d(TAG, "startCamera: ".concat(GSON.toJson(dailyThought)));
                break;
        }

        startActivityForResult(m, CameraActivity.CAMERA_REQUEST);
    }

    private void startPhotoGallerySelection(BaseDTO base){
        Intent intent = new Intent(this, PhotoSelectionActivity.class);
        intent.putExtra("type", type);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                intent.putExtra("dailyThought", dailyThought);
                break;


        }
        startActivity(intent);
    }

    @Override
    public void onVideoCaptureRequested(BaseDTO entity) {
        Log.w(TAG, "onVideoCaptureRequested: .................");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        pickGalleryOrVideoCamera(entity);
    }
    private void pickGalleryOrVideoCamera(final BaseDTO base) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Select Images")
                .setMessage("Please select the source of the photos")
                .setPositiveButton("Use Video Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startVideo(base);
                    }
                })
                .setNegativeButton("Pick from Device", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startVideoSelection(base);
                    }
                }).show();
    }
    private void startVideo(BaseDTO entity) {

        Intent m = new Intent(this, CameraActivity.class);
        m.putExtra("type", CameraActivity.VIDEO_REQUEST);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) entity;
                m.putExtra("dailyThought", dailyThought);
                break;
        }

        startActivityForResult(m, CameraActivity.VIDEO_REQUEST);
    }
    private void startVideoSelection(BaseDTO base) {
        Intent m = new Intent(this, VideoSelectionActivity.class);
        m.putExtra("type", type);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                m.putExtra("dailyThought", dailyThought);
                break;
        }

        startActivity(m);
    }
    @Override
    public void onSomeActionRequired(BaseDTO entity) {

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
    private void startPodcastSelection(BaseDTO base){
        Intent intent = new Intent(this, PodcastSelectionActivity.class);
        intent.putExtra("type", type);
        switch (type) {

            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) base;
                intent.putExtra("dailyThought", dailyThought);
                break;

        }
        startActivity(intent);
    }
    @Override
    public void onMicrophoneRequired(BaseDTO entity) {
        Log.w(TAG, "onMicrophoneRequired: ,,,,,,,,,,,,,,,,,,,,");
        if (isTooltip) {
            isTooltip = false;
            return;
        }
        showSnackbar("Audio recording under construction", "OK", "cyan");
        startPodcastSelection(entity);

    }

    @Override
    public void onEntityClicked(BaseDTO entity) {
        Log.w(TAG, "onEntityClicked: .......".concat(GSON.toJson(entity)));
    }

    @Override
    public void onCalendarRequested(BaseDTO entity) {

    }

    @Override
    public void onEntityDetailRequested(BaseDTO entity, int type) {

    }

    @Override
    public void onDeleteTooltipRequired(int type) {
        isTooltip = true;
        Toasty.warning(this, "Remove this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onLinksTooltipRequired(int type) {
        isTooltip = true;
        Toasty.info(this, "Add internet links to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onPhotoCaptureTooltipRequired(int type) {
        isTooltip = true;
        Toasty.warning(this, "Add photos to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onVideoCaptureTooltipRequired(int type) {
        isTooltip = true;
        Toasty.info(this, "Add videos to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onSomeActionTooltipRequired(int type) {

    }

    @Override
    public void onMicrophoneTooltipRequired(int type) {
        isTooltip = true;
        Toasty.success(this, "Add audio recording to this record",
                Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onCalendarTooltipRequired(int type) {

    }

    @Override
    public void onNewsArticleRequested(BaseDTO entity) {

    }

    @Override
    public void onUpdateUser(UserDTO user) {

    }

    @Override
    public void onUpdateDailyThought(DailyThoughtDTO dailyThought) {
       /* Intent intent = new Intent(this, UpdateEntityActivity.class);
        intent.putExtra("dailyThought", dailyThought);
        startActivity(intent);*/
    }

    @Override
    public void onUpdateWeeklyMessage(WeeklyMessageDTO weeklyMessage) {

    }

    @Override
    public void onUpdateWeeklyMasterClass(WeeklyMasterClassDTO masterClass) {

    }

    @Override
    public void onUpdateNews(NewsDTO news) {

    }

    @Override
    public void onUpdateCategory(CategoryDTO category) {

    }

    @Override
    public void onUpdateSubscription(SubscriptionDTO subscription) {

    }

    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
    public static final String TAG = CreateDailyThoughtActivity.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
