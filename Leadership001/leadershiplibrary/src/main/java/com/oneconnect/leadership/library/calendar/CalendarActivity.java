package com.oneconnect.leadership.library.calendar;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.cache.UserCacheContract;
import com.oneconnect.leadership.library.cache.UserCachePresenter;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.core.Data;

public class CalendarActivity extends AppCompatActivity implements UserCacheContract.View, com.oneconnect.leadership.library.calendar.CalendarContract.View {

    private Spinner userSpinner, attendeeSpinner;
    private List<Calendar> calendars;
    private List<UserDTO> users, attendees = new ArrayList<>();
    private Calendar calendar;
    private TextView txtTitle, txtDate, txtStartDate, txtEndDate, txtStartTime, txtEndTime, txtAttendeeCount;


    private View startDateLayout, endDateLayout, startTimeLayout, endTimeLayout;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private Button btnSend, btnAll;
    private int type;
    private UserCachePresenter cachePresenter;
    DailyThoughtDTO dailyThought;
    PodcastDTO podcast;
    WeeklyMasterClassDTO weeklyMasterClass;
    WeeklyMessageDTO weeklyMessage;
    CalendarPresenter presenter;

    private Date startDate, endDate;
    private Time startTime, endTime;
    private DatePickerDialog startDatePicker, endDatePicker;
    private TimePickerDialog startTimePicker, endTimePicker;
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    public static final String TAG = CalendarActivity.class.getSimpleName();
    private String mTitle, mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leadership");
        getSupportActionBar().setSubtitle("Calendar Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        presenter = new CalendarPresenter(this);
        setup();

        type = getIntent().getIntExtra("type", 0);

        //todo check the passing of these objects from caller - strange shit going on!
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("base");
                Log.e(TAG, "...onCreate: ".concat(GSON.toJson(dailyThought)));
                dailyThought = SharedPrefUtil.getDailyThought(this);
                Log.w(TAG, "onCreate: from shared prefs:".concat(GSON.toJson(dailyThought)));
                txtTitle.setText(dailyThought.getTitle());
                mTitle = dailyThought.getTitle();
                txtDate.setText(sdf1.format(dailyThought.getDateScheduled()));
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("base");
                Log.e(TAG, "...onCreate: ".concat(GSON.toJson(weeklyMessage)));
                weeklyMessage = SharedPrefUtil.getWeeklyMessage(this);
                Log.w(TAG, "onCreate: from shared prefs:".concat(GSON.toJson(weeklyMessage)));
                txtTitle.setText(weeklyMessage.getTitle());
                mTitle = weeklyMessage.getTitle();
                txtDate.setText(sdf1.format(weeklyMessage.getDateScheduled()));
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("base");
                Log.e(TAG, "...onCreate: ".concat(GSON.toJson(weeklyMasterClass)));
                weeklyMasterClass = SharedPrefUtil.getWeeklyMasterclass(this);
                Log.w(TAG, "onCreate: from shared prefs:".concat(GSON.toJson(weeklyMasterClass)));
                txtTitle.setText(weeklyMasterClass.getTitle());
                txtDate.setText(sdf1.format(weeklyMasterClass.getDateScheduled()));
                break;
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) getIntent().getSerializableExtra("base");
                Log.e(TAG, "...onCreate: ".concat(GSON.toJson(podcast)));
                podcast = SharedPrefUtil.getPodcast(this);
                Log.w(TAG, "onCreate: from shared prefs:".concat(GSON.toJson(podcast)));
                txtTitle.setText(podcast.getTitle());
                txtDate.setText(sdf1.format(podcast.getDateScheduled()));
                break;
        }

        cachePresenter = new UserCachePresenter(this, this);
        cachePresenter.getUsers();
        getCalendars();
    }

    private void setup() {
        txtStartDate = (TextView) findViewById(R.id.startDate);
        txtEndDate = (TextView) findViewById(R.id.endDate);
        txtStartTime = (TextView) findViewById(R.id.startTime);
        txtEndTime = (TextView) findViewById(R.id.endTime);
        txtAttendeeCount = (TextView) findViewById(R.id.attendeeCount);

        txtStartDate.setText("");
        txtEndDate.setText("");
        txtStartTime.setText("");
        txtEndTime.setText("");
        //
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDate = (TextView) findViewById(R.id.txtDate);
        userSpinner = (Spinner) findViewById(R.id.userSpinner);
        attendeeSpinner = (Spinner) findViewById(R.id.attendeeSpinner);
        attendeeSpinner.setVisibility(View.GONE);
        startDateLayout = findViewById(R.id.t1);
        endDateLayout = findViewById(R.id.t2);
        startTimeLayout = findViewById(R.id.t3);
        endTimeLayout = findViewById(R.id.t4);
        btnSend = (Button) findViewById(R.id.btn);
        btnAll = (Button) findViewById(R.id.btnAll);
        startDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStartDate();
            }
        });
        endDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEndDate();
            }
        });
        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStartTime();
            }
        });
        endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEndTime();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 setAllUsersAsAttendees();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (type) {
                    case ResponseBag.DAILY_THOUGHTS:
                        saveEvent(dailyThought.getTitle(), dailyThought.getSubtitle()
                                .concat(" ").concat(dailyThought.getStringDateScheduled()));
                        break;
                    case ResponseBag.WEEKLY_MESSAGE:
                        saveEvent(weeklyMessage.getTitle(), weeklyMessage.getSubtitle()
                                .concat(" ").concat(weeklyMessage.getStringDateScheduled()));
                        break;
                    case ResponseBag.WEEKLY_MASTERCLASS:
                        saveEvent(weeklyMasterClass.getTitle(), weeklyMasterClass.getSubtitle()
                                .concat(" ").concat(weeklyMasterClass.getStringDateScheduled()));
                        break;
                    case ResponseBag.PODCASTS:
                        saveEvent(podcast.getTitle(), podcast.getSubtitle()
                                .concat(" ").concat(podcast.getStringDateScheduled()));
                        break;
                }
            }
        });


//        calendarView = (CalendarView) findViewById(R.id.calendar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    long sDate, eDate;

    private void saveEvent(String title, String description) {
        if (startDate == null) {
            Toasty.warning(this, "Select Start Date", Toast.LENGTH_SHORT, true).show();
            getStartDate();
            return;
        }
        if (endDate == null) {
            Toasty.warning(this, "Select End Date", Toast.LENGTH_SHORT, true).show();
            getEndDate();
            return;
        }
        if (startTime == null) {
            Toasty.warning(this, "Select Start Time", Toast.LENGTH_SHORT, true).show();
            getStartTime();
            return;
        }
        if (endTime == null) {
            Toasty.warning(this, "Select End Time", Toast.LENGTH_SHORT, true).show();
            getEndTime();
            return;
        }
        setNewEventId();
        java.util.Calendar cal1 = getStartCalendar(startDate, startTime.hour, startTime.minute);
        sDate = cal1.getTimeInMillis();

        java.util.Calendar cal2 = getEndCalendar(endDate, endTime.hour, endTime.minute);
        eDate = cal2.getTimeInMillis();


        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal1.getTime().getTime())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal2.getTime().getTime())

                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "To Be Determined")
                .putExtra(CalendarContract.Events._ID, eventID)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, getAttendeeString());

        Log.d(TAG, "saveEvent: ######## startActivity for calendar app on device");
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: checking eventID; see if calendar event saved");
        if (eventID == 0) {
            return;
        }
        setLastEventId();
        if (eventID == lastEventID) {
            Log.i(TAG, "onResume: eventID's match - event was saved in the calendar");
            addEventToFirebase();
        } else {
            Log.i(TAG, "onResume: eventID's DO NOT match - event was NOT saved in the calendar");
        }

    }

    private long eventID, lastEventID;

    public void setLastEventId() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        ContentResolver cr = this.getContentResolver();
        Cursor cursor = cr.query(CalendarContract.Events.CONTENT_URI, new String[]{"MAX(_id) as max_id"}, null, null, "_id");
        cursor.moveToFirst();
        lastEventID = cursor.getLong(cursor.getColumnIndex("max_id"));
        Log.d(TAG, "setLastEventId: " + lastEventID);
    }
    public void setNewEventId() {
        ContentResolver cr = this.getContentResolver();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor cursor = cr.query(CalendarContract.Events.CONTENT_URI, new String[]{"MAX(_id) as max_id"}, null, null, "_id");
        cursor.moveToFirst();
        long max_val = cursor.getLong(cursor.getColumnIndex("max_id"));
        eventID = max_val+1;
        Log.d(TAG, ".........setNewEventId: " + eventID);
    }
    @NonNull
    private java.util.Calendar getEndCalendar(Date endDate, int hour2, int minute2) {
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(endDate);
        cal2.set(java.util.Calendar.HOUR, hour2);
        cal2.set(java.util.Calendar.MINUTE, minute2);
        return cal2;
    }

    @NonNull
    private java.util.Calendar getStartCalendar(Date startDate, int hour2, int minute2) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(startDate);
        cal1.set(java.util.Calendar.HOUR, hour2);
        cal1.set(java.util.Calendar.MINUTE, minute2);
        return cal1;
    }

    private void addEventToFirebase() {
        Log.d(TAG, "addEventToFirebase: ....................");
        CalendarEventDTO e = new CalendarEventDTO();
        e.setArrangedBy(SharedPrefUtil.getUser(this).getFullName());
        e.setAttendees(getAttendeeString());
        e.setStartDate(sDate);
        e.setEndDate(eDate);

        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                e.setDailyThoughtID(dailyThought.getDailyThoughtID());
                e.setDescription(dailyThought.getTitle().concat(" ")
                        .concat(dailyThought.getSubtitle()));
                e.setTitle(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                e.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                e.setDescription(weeklyMessage.getTitle().concat(" ")
                        .concat(weeklyMessage.getSubtitle()));
                e.setTitle(weeklyMessage.getTitle());
                break;
            case ResponseBag.PODCASTS:
                e.setPodcastID(podcast.getPodcastID());
                e.setDescription(podcast.getTitle().concat(" ")
                        .concat(podcast.getSubtitle()));
                e.setTitle(podcast.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                e.setWeeklyMasterclassID(weeklyMasterClass.getWeeklyMasterClassID());
                e.setDescription(weeklyMasterClass.getTitle().concat(" ")
                        .concat(weeklyMasterClass.getSubtitle()));
                e.setTitle(weeklyMasterClass.getTitle());
                break;
        }
        showSnackbar("Adding calendar event to database","OK","yellow");
        presenter.addCalendarEvent(e);

    }
    private String getAttendeeString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SharedPrefUtil.getUser(this).getEmail());
        if (!attendees.isEmpty()) {
            sb.append(", ");
        }

        int index = 0;
        for (UserDTO u : attendees) {
            sb.append(u.getEmail());
            if (index < attendees.size()) {
                sb.append(", ");
            }
            index++;
        }
        Log.d(TAG, "getAttendeeString: ".concat(sb.toString()));

        return sb.toString();
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private void getStartDate() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        startDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day, 0, 0, 0);
                Date d = cal.getTime();
                startDate = d;
                txtStartDate.setText(sdf.format(startDate));
                if (endDate == null) {
                    txtEndDate.setText(sdf.format(startDate));
                    endDate = startDate;
                }
            }
        }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));

        startDatePicker.show();
    }

    private void getEndDate() {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        endDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day, 0, 0, 0);
                Date d = cal.getTime();
                endDate = d;
                txtEndDate.setText(sdf.format(endDate));
            }
        }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));

        endDatePicker.show();
    }

    private void getStartTime() {
        final TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                startTime = new Time(i, i1);
                StringBuilder sb = new StringBuilder();
                sb.append(i).append(":");
                if (i1 < 10) {
                    sb.append("0").append(i1);
                } else {
                    sb.append(i1);
                }
                txtStartTime.setText(sb.toString());
                if (endTime == null) {
                    endTime = new Time(i + 1, i1);
                    sb = new StringBuilder();
                    sb.append(endTime.hour).append(":");
                    if (endTime.minute < 10) {
                        sb.append("0").append(endTime.minute);
                    } else {
                        sb.append(endTime.minute);
                    }
                    txtEndTime.setText(sb.toString());
                }
            }
        };

        startTimePicker = new TimePickerDialog(this, listener, 9, 0, true);
        startTimePicker.show();
    }

    private void getEndTime() {
        final TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                endTime = new Time(i, i1);
                StringBuilder sb = new StringBuilder();
                sb.append(i).append(":");
                if (i1 < 10) {
                    sb.append("0").append(i1);
                } else {
                    sb.append(i1);
                }
                txtEndTime.setText(sb.toString());
            }
        };

        endTimePicker = new TimePickerDialog(this, listener, 10, 0, true);
        endTimePicker.show();
    }

    private void getCalendars() {
        CalendarProvider calendarProvider = new CalendarProvider(this);
        Data<Calendar> data = calendarProvider.getCalendars();
        calendars = data.getList();
        List<String> list = new ArrayList<>();
        for (Calendar c : calendars) {
            list.add(c.displayName);
//            Log.w(TAG, "testLink: calendar: ".concat(GSON.toJson(c)));
        }

    }

    @Override
    public void onCachedUsers(List<UserDTO> cachedUsers) {
        this.users = cachedUsers;
        List<String> list = new ArrayList<>();
        list.add("Tap to add Attendee");
        for (UserDTO c : users) {
            list.add(c.getFullName());
        }

        ArrayAdapter<String> a = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        userSpinner.setAdapter(a);
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                UserDTO u = users.get(i - 1);
                Log.d(TAG, "onItemSelected: user: ".concat(GSON.toJson(u)));
                attendees.add(u);
                setAttendeeSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setAllUsersAsAttendees() {
        attendees = users;
        setAttendeeSpinner();
    }
    public void setAttendeeSpinner() {
        attendeeSpinner.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<>();
        if (!attendees.isEmpty()) {
            list.add("Tap to remove Attendee");
        }
        for (UserDTO c : attendees) {
            list.add(c.getFullName());
        }
        txtAttendeeCount.setText(String.valueOf(attendees.size()));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        attendeeSpinner.setAdapter(adapter);
        attendeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    return;
                }
                if (attendees.isEmpty()) {
                    return;
                }
                Log.d(TAG, "onItemSelected: attendees: " + attendees.size() + " index: " + i);
                if (attendees.size() == 1) {
                    attendees.clear();
                } else {
                    boolean isFound = false;
                    int j = 0;
                    UserDTO f = attendees.get(i - 1);
                    for (UserDTO u : attendees) {
                        if (u.getUserID().equalsIgnoreCase(f.getUserID())) {
                            isFound = true;
                            break;
                        }
                        j++;
                    }
                    if (isFound) {
                        attendees.remove(j);
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
    public void onCalendarEventAdded(String key) {
        Log.i(TAG, "onCalendarEventAdded: event added to firebase");
        Toasty.success(this,"Event added to database",Toast.LENGTH_LONG,true).show();
        showSnackbar("Event added to database","OK","green");

    }

    @Override
    public void onError(String message) {

    }

    class Time {
        int hour, minute;

        public Time(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }
    }

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
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
