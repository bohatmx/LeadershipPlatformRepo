package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.MasterAdapter;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.lists.MasterListFragment;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.List;

/**
 * Created by Kurisani on 2017/05/02.
 */

public class MasterActivity  extends AppCompatActivity implements MasterAdapter.MasterAdapterListener{

    MasterListFragment masterListFragment;
    SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private Context ctx;
    ResponseBag bag;
    private UserDTO user;
    private int type;
    EntityListFragment entityListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_master_classes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ctx = getApplicationContext();
        masterListFragment = (MasterListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment();

            }
        });
        user = SharedPrefUtil.getUser(this);
    }

    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment");
        SharedPrefUtil.saveFragmentType(bag.getType(), this);
        type = bag.getType();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        entityListFragment = EntityListFragment.newInstance(bag);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }

    @Override
    public void onPldpRequested(WeeklyMasterClassDTO weeklyMasterClass) {

    }

    @Override
    public void onPhotoRequired(PhotoDTO photo) {

    }

    @Override
    public void onVideoRequired(VideoDTO video) {

    }

    @Override
    public void onPodcastRequired(PodcastDTO podcast) {

    }

    @Override
    public void onMasterClassRating(WeeklyMasterClassDTO masterClass) {

    }

    @Override
    public void onUrlRequired(UrlDTO url) {

    }

    @Override
    public void onPhotosRequired(List<PhotoDTO> list) {

    }

    @Override
    public void onMasterClicked(int position) {

    }

    static final String TAG = MasterActivity.class.getSimpleName();
}
