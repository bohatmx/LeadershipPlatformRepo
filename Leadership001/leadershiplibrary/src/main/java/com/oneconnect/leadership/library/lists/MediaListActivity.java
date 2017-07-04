package com.oneconnect.leadership.library.lists;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.audio.AudioPlayerActivity;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.DepthPageTransformer;
import com.oneconnect.leadership.library.video.LeExoPlayerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MediaListActivity extends AppCompatActivity implements
        VideoListFragment.VideoListener,
        EBookListFragment.EBookListener,
        PhotoListFragment.PhotoListener,
        LinksListFragment.UrlListener,
        PodcastListFragment.PodcastListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private FloatingActionButton fab;
    private VideoListFragment videoListFragment;
    private PhotoListFragment photoListFragment;
    private PodcastListFragment podcastListFragment;
    private LinksListFragment linksListFragment;
    private EBookListFragment eBookListFragment;
    private int type;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private PodcastDTO podcast;
    private EBookDTO eBook;
    private HashMap<String, VideoDTO> videos;
    private HashMap<String, PodcastDTO> podcasts;
    private HashMap<String, PhotoDTO> photos;
    public static final String TAG = MediaListActivity.class.getSimpleName();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Media Package");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) getIntent()
                        .getSerializableExtra("dailyThought");
                getSupportActionBar().setSubtitle(dailyThought.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(dailyThought)));
                break;
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) getIntent()
                        .getSerializableExtra("podcast");
                getSupportActionBar().setSubtitle(podcast.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(podcast)));
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) getIntent()
                        .getSerializableExtra("weeklyMasterClass");
                getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(weeklyMasterClass)));
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) getIntent()
                        .getSerializableExtra("weeklyMessage");
                getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(weeklyMessage)));
                break;
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) getIntent()
                        .getSerializableExtra("eBook");
                getSupportActionBar().setSubtitle(eBook.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(eBook)));
                break;
        }
        setup();
        buildTabs();
    }

    private void setup() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void buildTabs() {
        pageFragments = new ArrayList<>();
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                setDailyThoughtTabs();
                break;
            case ResponseBag.PODCASTS:
                setPodcastTabs();
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                setWeeklyMasterClassTabs();
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                setWeeklyMessageTabs();
                break;
            case ResponseBag.EBOOKS:
                setEbookTabs();
                break;
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    private void setDailyThoughtTabs() {
        if (dailyThought.getVideos() != null && !dailyThought.getVideos().isEmpty()) {
            videoListFragment = VideoListFragment.newInstance(dailyThought.getVideos());
            videos = dailyThought.getVideos();
            pageFragments.add(videoListFragment);
        }
        if (dailyThought.getPhotos() != null && !dailyThought.getPhotos().isEmpty()) {
            photoListFragment = PhotoListFragment.newInstance(dailyThought.getPhotos());
            photos = dailyThought.getPhotos();
            pageFragments.add(photoListFragment);
        }
        if (dailyThought.getUrls() != null && !dailyThought.getUrls().isEmpty()) {
            linksListFragment = LinksListFragment.newInstance(dailyThought.getUrls());
            pageFragments.add(linksListFragment);
        }
        if (dailyThought.getPodcasts() != null && !dailyThought.getPodcasts().isEmpty()) {
            podcastListFragment = PodcastListFragment.newInstance(dailyThought.getPodcasts());
            pageFragments.add(podcastListFragment);
        }
        if (dailyThought.getEbooks() != null && !dailyThought.getEbooks().isEmpty()) {
            eBookListFragment = EBookListFragment.newInstance(dailyThought.getEbooks());
            pageFragments.add(eBookListFragment);
        }
        Log.i(TAG, "setDailyThoughtTabs: tabs set up: " + pageFragments.size());
    }

    private void setWeeklyMasterClassTabs() {
        if (weeklyMasterClass.getVideos() != null && !weeklyMasterClass.getVideos().isEmpty()) {
            videoListFragment = VideoListFragment.newInstance(weeklyMasterClass.getVideos());
            videos = weeklyMasterClass.getVideos();
            pageFragments.add(videoListFragment);
        }
        if (weeklyMasterClass.getPhotos() != null && !weeklyMasterClass.getPhotos().isEmpty()) {
            photoListFragment = PhotoListFragment.newInstance(weeklyMasterClass.getPhotos());
            photos = weeklyMasterClass.getPhotos();
            pageFragments.add(photoListFragment);
        }
        if (weeklyMasterClass.getUrls() != null && !weeklyMasterClass.getUrls().isEmpty()) {
            linksListFragment = LinksListFragment.newInstance(weeklyMasterClass.getUrls());
            pageFragments.add(linksListFragment);
        }
        if (weeklyMasterClass.getPodcasts() != null && !weeklyMasterClass.getPodcasts().isEmpty()) {
            podcastListFragment = PodcastListFragment.newInstance(weeklyMasterClass.getPodcasts());
            pageFragments.add(podcastListFragment);
        }
        if (weeklyMasterClass.getEbooks() != null && !weeklyMasterClass.getEbooks().isEmpty()) {
            eBookListFragment = EBookListFragment.newInstance(weeklyMasterClass.getEbooks());
            pageFragments.add(eBookListFragment);
        }
        Log.i(TAG, "setWeeklyMasterClassTabs: " + pageFragments.size());
    }

    private void setWeeklyMessageTabs() {
        if (weeklyMessage.getVideos() != null && !weeklyMessage.getVideos().isEmpty()) {
            videoListFragment = VideoListFragment.newInstance(weeklyMessage.getVideos());
            videos = weeklyMessage.getVideos();
            pageFragments.add(videoListFragment);
        }
        if (weeklyMessage.getPhotos() != null && !weeklyMessage.getPhotos().isEmpty()) {
            photoListFragment = PhotoListFragment.newInstance(weeklyMessage.getPhotos());
            photos = weeklyMessage.getPhotos();
            pageFragments.add(photoListFragment);
        }
        if (weeklyMessage.getUrls() != null && !weeklyMessage.getUrls().isEmpty()) {
            linksListFragment = LinksListFragment.newInstance(weeklyMessage.getUrls());
            pageFragments.add(linksListFragment);
        }
        if (weeklyMessage.getPodcasts() != null && !weeklyMessage.getPodcasts().isEmpty()) {
            podcastListFragment = PodcastListFragment.newInstance(weeklyMessage.getPodcasts());
            pageFragments.add(podcastListFragment);
        }
        if (weeklyMessage.getEbooks() != null && !weeklyMessage.getEbooks().isEmpty()) {
            eBookListFragment = EBookListFragment.newInstance(weeklyMessage.getEbooks());
            pageFragments.add(eBookListFragment);
        }
        Log.i(TAG, "setWeeklyMessageTabs: " + pageFragments.size());
    }

    private void setPodcastTabs() {
        if (podcast.getVideos() != null && !podcast.getVideos().isEmpty()) {
            videoListFragment = VideoListFragment.newInstance(podcast.getVideos());
            videos = podcast.getVideos();
            pageFragments.add(videoListFragment);
        }
        if (podcast.getPhotos() != null && !podcast.getPhotos().isEmpty()) {
            photoListFragment = PhotoListFragment.newInstance(podcast.getPhotos());
            photos = podcast.getPhotos();
            pageFragments.add(photoListFragment);
        }
        if (podcast.getUrls() != null && !podcast.getUrls().isEmpty()) {
            linksListFragment = LinksListFragment.newInstance(podcast.getUrls());
            pageFragments.add(linksListFragment);
        }

        if (podcast.getEbooks() != null && !podcast.getEbooks().isEmpty()) {
            eBookListFragment = EBookListFragment.newInstance(podcast.getEbooks());
            pageFragments.add(eBookListFragment);
        }
        Log.i(TAG, "setPodcastTabs: " + pageFragments.size());
    }

    private void setEbookTabs() {
        if (eBook.getVideos() != null && !eBook.getVideos().isEmpty()) {
            videoListFragment = VideoListFragment.newInstance(eBook.getVideos());
            videos = eBook.getVideos();
            pageFragments.add(videoListFragment);
        }
        if (eBook.getPhotos() != null && !eBook.getPhotos().isEmpty()) {
            photoListFragment = PhotoListFragment.newInstance(eBook.getPhotos());
            pageFragments.add(photoListFragment);
        }
        if (eBook.getUrls() != null && !eBook.getUrls().isEmpty()) {
            linksListFragment = LinksListFragment.newInstance(eBook.getUrls());
            pageFragments.add(linksListFragment);
        }
        Log.i(TAG, "setEbookTabs: " + pageFragments.size());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_media_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_video) {
            if (!videos.isEmpty()) {
                startExoPlayer();
            }
            return true;
        }
        if (id == R.id.action_photo) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startExoPlayer() {

        if (!videos.isEmpty()) {
            ResponseBag bag = new ResponseBag();
            bag.setVideos(new ArrayList<VideoDTO>());
            for (VideoDTO v: videos.values()) {
                 bag.getVideos().add(v);
            }
            Intent m = new Intent(this,LeExoPlayerActivity.class);
            m.putExtra("bag",bag);
            startActivity(m);
        }
    }
    @Override
    public void onPhotoTapped(PhotoDTO photo) {

    }

    @Override
    public void onPodcastTapped(PodcastDTO podcast) {
        if (!podcasts.isEmpty()) {
            ResponseBag bag = new ResponseBag();
            bag.setPodcasts(new ArrayList<PodcastDTO>());
            for (PodcastDTO p : podcasts.values()) {
                bag.getPodcasts().add(p);
            }
            Intent intent = new Intent(this, AudioPlayerActivity.class);
            intent.putExtra("bag", bag);
            startActivity(intent);
        }

    }

    @Override
    public void onEBookTapped(EBookDTO eBook) {

    }

    @Override
    public void onVideoTapped(VideoDTO video) {
        Log.i(TAG, "onVideoTapped: play video: " + video.getTitle());
        Intent m = new Intent(this, LeExoPlayerActivity.class);
        ResponseBag bag = new ResponseBag();
        bag.setVideos(new ArrayList<VideoDTO>());
        bag.getVideos().add(video);
        m.putExtra("bag", bag);
        startActivity(m);

    }

    @Override
    public void onUrlTapped(UrlDTO url) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_media_list, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) pageFragments.get(position);
        }

        @Override
        public int getCount() {
            return pageFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return pageFragments.get(position).getTitle();
        }
    }

    private List<PageFragment> pageFragments = new ArrayList<>();
}
