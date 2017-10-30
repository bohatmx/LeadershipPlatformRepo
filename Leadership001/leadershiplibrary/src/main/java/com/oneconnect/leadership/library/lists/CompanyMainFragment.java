package com.oneconnect.leadership.library.lists;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.activities.ColorPickerActivity;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
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
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.editors.DailyThoughtEditor;
import com.oneconnect.leadership.library.editors.UserEditorBottomSheet;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.isDebugEnabled;

public class CompanyMainFragment extends Fragment  implements PageFragment, CrudContract.View{
    private CompanyFragmentListener mListener;

    String pageTitle;

    @Override
    public String getTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
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

    String hexColor;
    @Override
    public void onCompanyFound(CompanyDTO company) {
        Log.i(TAG, "*** onCompanyFound ***" + company.getCompanyName());
        if (company.getPrimaryColor() != 0) {
            this.hexColor = String.format("#%06X", (0xFFFFFF & company.getPrimaryColor()));
        }

      //  toolbar.setBackgroundColor(Color.parseColor(hexColor));
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

    @Override
    public void onCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onCompanies(List<CompanyDTO> list) {

    }

    private void startUserBottomSheet(final UserDTO user, int type) {

        final UserEditorBottomSheet myBottomSheet =
                UserEditorBottomSheet.newInstance(user, type);
        myBottomSheet.setCrudPresenter(presenter);
        myBottomSheet.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                UserDTO m = (UserDTO) entity;
                if (bag.getUsers() == null) {
                    bag.setUsers(new ArrayList<UserDTO>());
                }
                bag.getUsers().add(0, m);
                //  setFragment();
                myBottomSheet.dismiss();
                showSnackbar(m.getFullName().concat(" is being added/updated"),
                        getString(R.string.ok_label), "green");

            }

            @Override
            public void onDateRequired() {
                Log.d(TAG, "onDateRequired: date not required");
            }

            @Override
            public void onError(String message) {
                showSnackbar(message, "bad", Constants.RED);
            }
        });
        myBottomSheet.show(getFragmentManager()/*getSupportFragmentManager()*/, "SHEET_USER");

    }

    private void startDailyThoughtBottomSheet(final DailyThoughtDTO thought, int type) {

        dailyThoughtEditor = DailyThoughtEditor.newInstance(thought, type);
        dailyThoughtEditor.setBottomSheetListener(new BaseBottomSheet.BottomSheetListener() {
            @Override
            public void onWorkDone(BaseDTO entity) {
                DailyThoughtDTO m = (DailyThoughtDTO) entity;
                if (bag.getDailyThoughts() == null) {
                    bag.setDailyThoughts(new ArrayList<DailyThoughtDTO>());
                }
                bag.getDailyThoughts().add(0, m);
                // setFragment();
                showSnackbar(m.getTitle().concat(" is being added/updated"), getString(R
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

        dailyThoughtEditor.show(getFragmentManager()/*getSupportFragmentManager()*/, "SHEET_DAILY_THOUGHT");

    }

    private void getDate(final int sheetType) {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(/*this*/ctx, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                cal.set(year, month, day);
                Date d = cal.getTime();
                switch (sheetType) {
                    /*case ResponseBag.NEWS:
                        newArticleEditor.setSelectedDate(d);
                        break;*/
                    case ResponseBag.DAILY_THOUGHTS:
                        dailyThoughtEditor.setSelectedDate(d);
                        break;
                    /*case ResponseBag.WEEKLY_MESSAGE:
                        weeklyMessageEditor.setSelectedDate(d);
                        break;
                    case ResponseBag.WEEKLY_MASTERCLASS:
                        weeklyMasterclassEditor.setSelectedDate(d);
                        break;*/
                }
            }
        }, cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    Snackbar snackbar;

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(companyTitle, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }

    @Override
    public void onDailyThoughts(List<DailyThoughtDTO> list) {
        Log.i(TAG, "*** onDailyThoughts ***" + list.size());
        companyThoughts.setText("" + list.size());
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
        Log.i(TAG, "*** onPodcasts ***" + list.size());
        companyPodcasts.setText("" + list.size());
    }

    @Override
    public void onPhotos(List<PhotoDTO> list) {

    }

    @Override
    public void onPrices(List<PriceDTO> list) {

    }

    @Override
    public void onUserFound(final UserDTO user) {
        Log.i(TAG, "*** onUserFound ***" +"\n" + "FullName: " + user.getFullName());
        companyTitle.setText(user.getCompanyName());
        presenter.getCompanyProfile(user.getCompanyID());
        presenter.getDailyThoughts(user.getCompanyID());
        presenter.getPodcasts(user.getCompanyID());
        presenter.getVideos(user.getCompanyID());
        presenter.getUsers(user.getCompanyID());
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, ColorPickerActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("darkColor", THEME_REQUESTED);
                ctx.startActivity(intent);
                // startColorPicker(user);
                 /* Util.flashOnce(editFab, 300, new Util.UtilAnimationListener() {
                      @Override
                      public void onAnimationEnded() {
                          startColorPicker(user);
                      }
                  });*/
            }
        });
       /* card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(card4, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        Intent intent = new Intent(ctx, PodcastSelectionActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                });
            }
        });*/
    }

    @Override
    public void onUsers(List<UserDTO> list) {
        Log.i(TAG, "*** onUsers ***" + list.size());
        companyUsers.setText("" + list.size());
    }

    @Override
    public void onNews(List<NewsDTO> list) {

    }

    @Override
    public void onSubscriptions(List<SubscriptionDTO> list) {

    }

    @Override
    public void onVideos(List<VideoDTO> list) {
        Log.i(TAG, "*** onVideos ***" + list.size());
        companyVideos.setText("" + list.size());
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

    public interface CompanyFragmentListener {
        void onThoughtsSelected();
        void onVideosSelected();
        void onUsersSelected();
        void onPodcastsSelected();
    }
    public CompanyMainFragment() {
        // Required empty public constructor
    }


    public static CompanyMainFragment newInstance() {
        CompanyMainFragment fragment = new CompanyMainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    View view;
    Context ctx;
    CardView card1, card2, card3, card4;
    TextView companyThoughts, companyVideos, companyUsers, companyPodcasts, companyTitle;
    ImageView companyLogo, thoughtIcon, videoIcon, userIcon, podcastIcon;
    CrudPresenter presenter;
    private FirebaseAuth firebaseAuth;
    ResponseBag bag;
    private DailyThoughtEditor dailyThoughtEditor;
    private DatePickerDialog datePickerDialog;
    private UserDTO user;
    private int themeColor;
    private FloatingActionButton editFab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: #################");
        view =  inflater.inflate(R.layout.fragment_company_main, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
       // toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        ctx = getActivity();
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        presenter = new CrudPresenter(this);

        if (getActivity().getIntent().getSerializableExtra("user") != null) {
            user = (UserDTO) getActivity().getIntent().getSerializableExtra("user");
            Log.i(TAG, "user: " + user.getFullName());
        } else {
            presenter.getUser(firebaseAuth.getCurrentUser().getEmail());
        }

        presenter.getUser(firebaseAuth.getCurrentUser().getEmail());
        card1 = (CardView) view.findViewById(R.id.card1);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        mListener.onThoughtsSelected();
                        //startDailyThoughtBottomSheet(null, Constants.NEW_ENTITY);

            }
        });
        card2 = (CardView) view.findViewById(R.id.card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                      //  mListener.onVideosSelected();
                       /* Intent intent = new Intent(ctx, VideoSelectionActivity.class);
                        if (hexColor != null) {
                            intent.putExtra("hexColor", hexColor);
                        }
                        startActivity(intent);*/

            }
        });
        card3 = (CardView) view.findViewById(R.id.card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        /*mListener.onUsersSelected();*/
                        //startUserBottomSheet(null, Constants.NEW_ENTITY);

            }
        });
        card4 = (CardView) view.findViewById(R.id.card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        /*mListener.onPodcastsSelected();*/


            }
        });

        companyThoughts = (TextView) view.findViewById(R.id.companyThoughts);
        companyVideos = (TextView) view.findViewById(R.id.companyVideos);
        companyUsers = (TextView) view.findViewById(R.id.companyUsers);
        companyPodcasts = (TextView) view.findViewById(R.id.companyPodcasts);
       // companyLogo = (ImageView) view.findViewById(R.id.companyLogo);
        companyTitle = (TextView) view.findViewById(R.id.companyTitle);
        //
        editFab = (FloatingActionButton) view.findViewById(R.id.editFab);
        editFab.setVisibility(View.GONE);
        thoughtIcon = (ImageView) view.findViewById(R.id.thoughtIcon);
        videoIcon = (ImageView) view.findViewById(R.id.videoIcon);
        userIcon = (ImageView) view.findViewById(R.id.userIcon);
        podcastIcon = (ImageView) view.findViewById(R.id.podcastIcon);
        if (themeColor != 0) {
            thoughtIcon.setColorFilter(themeColor);
            videoIcon.setColorFilter(themeColor);
            userIcon.setColorFilter(themeColor);
            podcastIcon.setColorFilter(themeColor);
        }

        return view;
    }

    private void startColorPicker(UserDTO user) {
        Intent intent = new Intent(ctx, ColorPickerActivity.class);
        intent.putExtra("user", user);
        ctx.startActivity(intent);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CompanyFragmentListener) {
            mListener = (CompanyFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CompanyFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    static final int THEME_REQUESTED = 8075;

    int primaryColor, primaryDarkColor;

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.primaryDarkColor = primaryDarkColor;
    }

    static final String TAG = CompanyMainFragment.class.getSimpleName();
}
