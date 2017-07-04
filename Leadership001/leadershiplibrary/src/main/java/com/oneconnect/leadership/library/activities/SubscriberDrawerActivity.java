package com.oneconnect.leadership.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.lists.BasicEntityAdapter;
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.EBookListFragment;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.lists.PageFragment;
import com.oneconnect.leadership.library.lists.PodcastListFragment;
import com.oneconnect.leadership.library.lists.VideoListFragment;

import java.util.ArrayList;
import java.util.List;

public class SubscriberDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EBookListFragment.EBookListener, BasicEntityAdapter.EntityListener,
        PodcastListFragment.PodcastListener, VideoListFragment.VideoListener{


    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private PodcastDTO podcast;
    private EntityListFragment entityListFragment;
    private EBookListFragment eBookListFragment;
    private PodcastListFragment podcastListFragment;
    private VideoListFragment videoListFragment;
    private DailyThoughtListFragment dailyThoughtListFragment;

    String page;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        page = getIntent().getStringExtra("page");

        mPager = (ViewPager) findViewById(R.id.viewpager);
        PagerTitleStrip strip = (PagerTitleStrip) mPager.findViewById(R.id.pager_title_strip);
        strip.setVisibility(View.VISIBLE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpViewPager();
    }

    static List<PageFragment> pageFragmentList;
    ResponseBag bag;

    private void setUpViewPager() {

        //setMenuDestination();

        pageFragmentList = new ArrayList<>();
        dailyThoughtListFragment = DailyThoughtListFragment.newInstance();
        //podcastListFragment = PodcastListFragment.newInstance();
        //videoListFragment = VideoListFragment.newInstance();
        //eBookListFragment = EBookListFragment.newInstance();

        pageFragmentList.add(dailyThoughtListFragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subscriber_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_daily_thought) {
            // Handle the camera action
            Intent intent = new Intent(SubscriberDrawerActivity.this, EntityListFragment.class);
            startActivity(intent);
        } else if (id == R.id.nav_podcast) {

        } else if (id == R.id.nav_video) {

        } else if (id == R.id.nav_eBooks) {

        }
        else if (id == R.id.nav_master) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAddEntity() {

    }

    @Override
    public void onDeleteClicked(BaseDTO entity) {

    }

    @Override
    public void onLinksRequired(BaseDTO entity) {

    }

    @Override
    public void onPhotoCaptureRequested(BaseDTO entity) {

    }

    @Override
    public void onVideoCaptureRequested(BaseDTO entity) {

    }

    @Override
    public void onSomeActionRequired(BaseDTO entity) {

    }

    @Override
    public void onMicrophoneRequired(BaseDTO entity) {

    }

    @Override
    public void onEntityClicked(BaseDTO entity) {

    }

    @Override
    public void onCalendarRequested(BaseDTO entity) {

    }

    @Override
    public void onEntityDetailRequested(BaseDTO entity, int type) {

    }

    @Override
    public void onDeleteTooltipRequired(int type) {

    }

    @Override
    public void onLinksTooltipRequired(int type) {

    }

    @Override
    public void onPhotoCaptureTooltipRequired(int type) {

    }

    @Override
    public void onVideoCaptureTooltipRequired(int type) {

    }

    @Override
    public void onSomeActionTooltipRequired(int type) {

    }

    @Override
    public void onMicrophoneTooltipRequired(int type) {

    }

    @Override
    public void onCalendarTooltipRequired(int type) {

    }

    @Override
    public void onNewsArticleRequested(BaseDTO entity) {

    }

    @Override
    public void onPodcastTapped(PodcastDTO podcast) {

    }

    @Override
    public void onEBookTapped(EBookDTO eBook) {

    }

    @Override
    public void onVideoTapped(VideoDTO video) {

    }
}
