package com.oneconnect.leadership.library.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PldpDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.pldp.PldpContract;
import com.oneconnect.leadership.library.pldp.PldpPresenter;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreatePldpActivity extends AppCompatActivity implements PldpContract.View {

    EditText sessionTxt, notesTxt;
    Spinner actionSpinner, selectedActions;
    String hexColor;
    PldpPresenter pldpPresenter;
    DailyThoughtDTO dailyThought;
    Context ctx;
    TextView entityText, attendeeCount;
    private UserDTO user;
    Button submitBtn, btnDate;
    int entityType;
    private WeeklyMessageDTO weeklyMessage;
    private EBookDTO eBook;
    private PodcastDTO podcast;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private VideoDTO video;
    private NewsDTO news;
    FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pldp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ctx = getApplicationContext();
        firebaseAuth = FirebaseAuth.getInstance();

        pldpPresenter = new PldpPresenter(this);

        sessionTxt = (EditText) findViewById(R.id.sessionTxt);
        entityText = (TextView) findViewById(R.id.entityText);
        entityText.setVisibility(View.GONE);
        notesTxt = (EditText) findViewById(R.id.notesTxt);


        if (getIntent().getSerializableExtra("dailyThought") != null) {
            entityType = ResponseBag.DAILY_THOUGHTS;
            dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
            sessionTxt.setVisibility(View.GONE);
            entityText.setVisibility(View.VISIBLE);
            entityText.setText(dailyThought.getTitle().concat(" - " + dailyThought.getSubtitle()));
        }

        if (getIntent().getSerializableExtra("weeklyMasterClass") != null) {
            entityType = ResponseBag.WEEKLY_MASTERCLASS;
            weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");
            sessionTxt.setVisibility(View.GONE);
            entityText.setVisibility(View.VISIBLE);
            entityText.setText(weeklyMasterClass.getTitle().concat(" - " + weeklyMasterClass.getSubtitle()));
        }

        if (getIntent().getSerializableExtra("weeklyMessage") != null) {
            entityType = ResponseBag.WEEKLY_MESSAGE;
            weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
            sessionTxt.setVisibility(View.GONE);
            entityText.setVisibility(View.VISIBLE);
            entityText.setText(weeklyMessage.getTitle().concat(" - " + weeklyMessage.getSubtitle()));
        }

        if (getIntent().getSerializableExtra("eBook") != null) {
            entityType = ResponseBag.EBOOKS;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
            sessionTxt.setVisibility(View.GONE);
            entityText.setVisibility(View.VISIBLE);
            entityText.setText(eBook.getStorageName());
        }

        if (getIntent().getSerializableExtra("podcast") != null) {
            entityType = ResponseBag.PODCASTS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
            sessionTxt.setVisibility(View.GONE);
            entityText.setVisibility(View.VISIBLE);
            int i = podcast.getStorageName().lastIndexOf("/");
            entityText.setText(podcast.getStorageName().substring(i + 1));
        }

        if (getIntent().getSerializableExtra("video") != null) {
            entityType = ResponseBag.VIDEOS;
            video = (VideoDTO) getIntent().getSerializableExtra("video");
            sessionTxt.setVisibility(View.GONE);
            entityText.setVisibility(View.VISIBLE);
            int i = video.getStorageName().lastIndexOf("/");
            entityText.setText(video.getStorageName().substring(i + 1));
        }
        if (getIntent().getSerializableExtra("newsArticle") != null) {
            entityType = ResponseBag.NEWS;
            news = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
            sessionTxt.setVisibility(View.GONE);
            entityText.setVisibility(View.VISIBLE);
            entityText.setText(news.getTitle().concat(" - ").concat(news.getSubtitle()));
        }

        if (getIntent().getSerializableExtra("user") != null) {
            user = (UserDTO) getIntent().getSerializableExtra("user");
        } else {
            pldpPresenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
        }

        submitBtn = (Button) findViewById(R.id.submitBtn);


        if (getIntent().getSerializableExtra("hexColor") != null) {
            hexColor = (String) getIntent().getSerializableExtra("hexColor");
            toolbar.setBackgroundColor(Color.parseColor(hexColor));
            submitBtn.setBackgroundColor(Color.parseColor(hexColor));
        }




        actionSpinner = (Spinner) findViewById(R.id.actionSpinner);
        attendeeCount = (TextView)findViewById(R.id.attendeeCount);
        selectedActions = (Spinner) findViewById(R.id.selectedActions);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });



        setSpinner();
    }


    private void setSpinner() {
        final List<String> list = new ArrayList<>();
        list.add("Select Action");
        list.add(PldpDTO.DESC_LISTEN);
        list.add(PldpDTO.DESC_REMEMBER_STAFF_BIRTHDAYS);
        list.add(PldpDTO.DESC_ASSERTIVE);
        list.add(PldpDTO.DESC_COMPASSION);
        list.add(PldpDTO.DESC_START_SLOW);
        list.add(PldpDTO.DESC_DONT_COMPARE_YOURSELF);
        list.add(PldpDTO.DESC_REMEMBER_YOUR_SUCCESS);
        list.add(PldpDTO.DESC_DONT_FEAR_FAILURE);
        list.add(PldpDTO.DESC_CUT_DOWN_ON_TV);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ctx,/*android.*/R.layout.custom_spinner_item,list);
        actionSpinner.setAdapter(adapter);
        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    return;
                }
                /*PldpDTO u;*/ String name = list.get(i/*- 1*/);
                Log.d(TAG, "onItemSelected: action: ".concat(GSON.toJson(name)));
                actions.add(name);
                setAttendeeSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setAttendeeSpinner() {
        selectedActions.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<>();
        if (!actions.isEmpty()) {
            list.add("Tap to remove action");
        }
        for (String c : actions) {
            list.add(c);
        }
        attendeeCount.setText(String.valueOf(actions.size()));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.custom_spinner_item, list);
        selectedActions.setAdapter(adapter);
        selectedActions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                if (actions.isEmpty()) {
                    return;
                }
                Log.d(TAG, "onItemSelected: actions: " + actions.size() + " index: " + i);
                if (actions.size() == 1) {
                    actions.clear();
                } else {
                    boolean isFound = false;
                    int j = 0;
                    /*UserDTO*/String f = actions.get(i - 1);
                    for (/*UserDTO*/String u : actions) {
                        if (u/*.getUserID()*/.equalsIgnoreCase(f/*.getUserID()*/)) {
                            isFound = true;
                            break;
                        }
                        j++;
                    }
                    if (isFound) {
                        actions.remove(j);
                        setAttendeeSpinner();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


   // private List<PldpDTO>  actions = new ArrayList<>();
    private List<String>  actions = new ArrayList<>();

    int actionType;



    private String getActionsString() {
        StringBuilder sb = new StringBuilder();
        /*sb.append(SharedPrefUtil.getUser(this).getEmail());*/
        if (!actions.isEmpty()) {
           // sb.append(", ");
        }

        int index = 0;
        for (/*PldpDTO*/String u : actions) {
            sb.append(u/*.getActionName()*//*getEmail()*/);
            if (index < actions.size()) {
                sb.append(", ");
            }
            index++;
        }
        Log.d(TAG, "getActionsString: ".concat(sb.toString()));

        return sb.toString();
    }

    TimePickerFragment timePickerFragment;
    TimePickerDialog timePickerDialog;
    int hours, minute;
    private DatePickerDialog datePickerDialog;

    private Date selectedDate;
    Date d;
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


    private void send() {
        Log.i(TAG, "Sending pldp.... ");

        PldpDTO pldp = new PldpDTO();

        pldp.setActionName(getActionsString());
        if (user != null) {
            pldp.setJournalUserID(user.getUserID());
            pldp.setJournalUserName(user.getFullName());
        }
        pldp.setDateUpdated(new Date().getTime());
        if (dailyThought != null || weeklyMasterClass != null || podcast != null || video != null || eBook != null || news != null) {
            Log.i(TAG, "entity is not null");
        } else {
            if (!sessionTxt.getText().toString().isEmpty()) {
                pldp.setSessionName(sessionTxt.getText().toString());
            } else {
                showEmptyTextSnackBar("Please enter a session name", "Dismiss", "red");
                return;
            }
        }
        if (d != null) {
            pldp.setReminderDate(d.getTime());
        }

        if (!notesTxt.getText().toString().isEmpty()) {
            pldp.setNote(notesTxt.getText().toString());
        }

        switch (entityType) {
            case ResponseBag.DAILY_THOUGHTS:
                pldp.setDailyThoughtID(dailyThought.getDailyThoughtID());
                pldp.setDailyThought(dailyThought);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                pldp.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
                pldp.setWeeklyMasterClass(weeklyMasterClass);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                pldp.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                pldp.setWeeklyMessage(weeklyMessage);
                break;
            case ResponseBag.EBOOKS:
                pldp.seteBookID(eBook.geteBookID());
                pldp.seteBook(eBook);
                break;
            case ResponseBag.PODCASTS:
                pldp.setPodcastID(podcast.getPodcastID());
                pldp.setPodcast(podcast);
                break;
            case ResponseBag.VIDEOS:
                pldp.setVideoID(video.getVideoID());
                pldp.setVideo(video);
                break;
            case ResponseBag.NEWS:
                pldp.setNewsID(news.getNewsID());
                pldp.setNews(news);
        }

        pldpPresenter.addPldp(pldp);


    }

    static final String TAG = CreatePldpActivity.class.getSimpleName();

    @Override
    public void onPldpUploaded(String key) {
        Log.i(TAG, "onPldpUploaded: " + key);
        showSnackBar("Pldp added ","Dismiss", "green");


    }

    @Override
    public void onUserFound(UserDTO u) {
        user = u;
    }

    Snackbar snackbar;

    public void showEmptyTextSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(actionSpinner, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 snackbar.dismiss();
               // finish();
            }
        });
        snackbar.show();
    }

    public void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(actionSpinner, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // snackbar.dismiss();
                finish();
            }
        });
        snackbar.show();
    }

    @Override
    public void onError(String message) {
        Log.e(TAG,  message);

    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}
