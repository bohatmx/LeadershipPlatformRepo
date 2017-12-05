package com.oneconnect.leadership.library.lists;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.firebase.ui.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.CreatePldpActivity;
import com.oneconnect.leadership.library.activities.FullArticleActivity;
import com.oneconnect.leadership.library.activities.PodcastPlayerActivity;
import com.oneconnect.leadership.library.activities.SubscriberContract;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.adapters.PldpAdapter;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PldpDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.SimpleDividerItemDecoration;
import com.oneconnect.leadership.library.util.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PldpListFragment.PldpListFragmentListener} interface
 * to handle interaction events.
 * Use the {@link PldpListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PldpListFragment extends Fragment implements PageFragment, SubscriberContract.View, CacheContract.View, com.oneconnect.leadership.library.calendar.CalendarContract.View{

    private PldpListFragmentListener mListener;
    SubscriberPresenter presenter;
    CachePresenter cachePresenter;
    ResponseBag bag;
    PldpDTO pldp;
    int type;
    Context ctx;
    View view;
    FirebaseAuth firebaseAuth;
    PldpAdapter pldpAdapter;
    FloatingActionButton fabIcon;

    public PldpListFragment() {
        // Required empty public constructor
    }


    public static PldpListFragment newInstance() {
        PldpListFragment fragment = new PldpListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bag = (ResponseBag) getArguments().getSerializable("bag");
            pldp = (PldpDTO) getArguments().getSerializable("pldp");
            type = getArguments().getInt("type", 0);

            presenter = new SubscriberPresenter(this);
            cachePresenter = new CachePresenter(this, ctx);

            type = SharedPrefUtil.getFragmentType(ctx);
        }
    }

    RecyclerView recyclerView;
    SearchView search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_pldp_list, container, false);
//        search = view.findViewById(R.id.search);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        presenter = new SubscriberPresenter(this);
        ctx = getActivity();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setHasFixedSize(true);

        fabIcon = (FloatingActionButton) view.findViewById(R.id.fabIcon);
        fabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CreatePldpActivity.class);
                if (user != null) {
                    intent.putExtra("user", user);
                }
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                }
                startActivity(intent);

            }
        });

        //getCachedDailyThoughts();
        getPldp();


        return view;
    }

    private void getPldp() {
        Log.d(LOG, "************** getPldps: " );
        presenter.getCurrentUser(firebaseAuth.getCurrentUser().getEmail());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PldpListFragmentListener) {
            mListener = (PldpListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PldpListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    static final String LOG = PldpListFragment.class.getSimpleName();

    @Override
    public void setThemeColors(int var1, int var2) {

    }

    String pageTitle;

    @Override
    public String getTitle() {
        return pageTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    public void onDataCached() {

    }

    @Override
    public void onCacheCategories(List<CategoryDTO> list) {

    }

    @Override
    public void onCacheDailyThoughts(List<DailyThoughtDTO> list) {

    }

    @Override
    public void onCacheEbooks(List<EBookDTO> list) {

    }

    @Override
    public void onCacheNews(List<NewsDTO> list) {

    }

    @Override
    public void onCachePodcasts(List<PodcastDTO> list) {

    }

    @Override
    public void onCacheVideos(List<VideoDTO> list) {

    }

    @Override
    public void onCachePrices(List<PriceDTO> list) {

    }

    @Override
    public void onCacheSubscriptions(List<SubscriptionDTO> list) {

    }

    @Override
    public void onCacheUsers(List<UserDTO> list) {

    }

    @Override
    public void onCacheWeeklyMasterclasses(List<WeeklyMasterClassDTO> list) {

    }

    @Override
    public void onCacheWeeklyMessages(List<WeeklyMessageDTO> list) {

    }

    @Override
    public void onCachePhotos(List<PhotoDTO> list) {

    }

    @Override
    public void onCacheCalendarEvents(List<CalendarEventDTO> list) {

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
    UserDTO user;

    @Override
    public void onUserFound(UserDTO u) {
        Log.i(LOG, "onUserFound: " + u.getFullName());
        user = u;
        presenter.getPldps(u.getUserID());
        presenter.getCompanyProfile(u.getCompanyID());

    }

    String hexColor;

    @Override
    public void onCompanyFound(CompanyDTO company) {
        if (company.getPrimaryColor() != 0) {
            Log.i(LOG, "*** converting primary color to a hex color ***");
            hexColor = String.format("#%06X", (0xFFFFFF & company.getPrimaryColor()));
        }
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
    public void onDailythoughtsByUser(List<DailyThoughtDTO> list) {

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

    android.widget.SearchView.OnQueryTextListener listener = new android.widget.SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<PldpDTO> filteredList = new ArrayList<>();

            for (int i = 0; i < pldpList.size(); i++) {

                final String text = pldpList.get(i).getJournalUserName().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(pldpList.get(i));
                }

            }
     //       setRecyclerView(filteredList);
            return true;

        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    private List<PldpDTO> pldpList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private void setRecyclerView(List<DailyThoughtDTO> dailyThoughtList) {

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
      //  search.setOnQueryTextListener(listener);


        recyclerView.setAdapter(pldpAdapter);

    }

    @Override
    public void onPldps(List<PldpDTO> list) {
        Log.i(LOG, "onPldps: " + list.size());
        this.pldpList = list;
        /*if (category != null) {
            list = getCategoryList(list, category.getCategoryName());
        }*/
        Collections.sort(list);
     //   setRecyclerView(list);
        pldpAdapter = new PldpAdapter(ctx, pldpList, new PldpAdapter.PldpAdapterListener() {
            @Override
            public void onDailyThoughtSelected(DailyThoughtDTO dailyThought) {

            }

            @Override
            public void onWeeklyMasterClassSelected(WeeklyMasterClassDTO masterClass) {

            }

            @Override
            public void onPodcastSelected(PodcastDTO podcast) {
                Intent intent = new Intent(ctx, PodcastPlayerActivity.class);
                if (hexColor != null) {
                    intent.putExtra("hexColor", hexColor);
                    Log.d(LOG, "color found: " + hexColor);
                }
                intent.putExtra("podcast", podcast);
                startActivity(intent);
            }

            @Override
            public void onVideoSelected(VideoDTO video) {
                Intent intent = new Intent(ctx, PodcastPlayerActivity.class);
                intent.putExtra("video", video);
                startActivity(intent);
               // ctx.startActivity(intent);

            }

            @Override
            public void onEbookSelected(EBookDTO ebook) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(ebook.getUrl()), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent newIntent = Intent.createChooser(intent, "Open File");
                try {
                    ctx.startActivity(newIntent);
                } catch(ActivityNotFoundException e) {
                    Log.e(LOG, "Failed to open pdf");
                }
            }

            @Override
            public void onArticleSelected(NewsDTO news) {
                if (news.getBody() != null) {
                    Intent intent = new Intent(ctx, FullArticleActivity.class);
                    intent.putExtra("newsArticle", news);
                    startActivity(intent);
                  //  ctx.startActivity(intent);
                }
            }

            @Override
            public void onReminderRequired(PldpDTO pldp) {

               // getDate();
                /*timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getActivity().getFragmentManager(), "DIALOG_TIME");*/

              //  pldp.setReminderDate(timePickerFragment.getSetTime());
            }
        });
        recyclerView.setAdapter(pldpAdapter);
    }


    /*public class AnnoyingBeep {
        Toolkit toolkit;
        Timer timer;

        public AnnoyingBeep() {
            toolkit = Toolkit.getDefaultToolkit();
            timer = new Timer();
            timer.schedule(new RemindTask(),
                    0,        //initial delay
                    1*1000);  //subsequent rate
        }

        class RemindTask extends TimerTask {
            int numWarningBeeps = 3;
            public void run() {
                if (numWarningBeeps > 0) {
                    toolkit.beep();
                    System.out.println("Beep!");
                    numWarningBeeps--;
                } else {
                    toolkit.beep();
                    System.out.println("Time's up!");
                    //timer.cancel(); // Not necessary because
                    // we call System.exit
                    System.exit(0);   // Stops the AWT thread
                    // (and everything else)
                }
            }
        }
    }*/

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
    public void onCalendarEventAdded(String key) {

    }

    @Override
    public void onError(String message) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface PldpListFragmentListener {
    }
}
