package com.ocg.leadership.company.activities;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ocg.leadership.company.R;
import com.ocg.leadership.company.adapters.HarmonyThoughtAdapter;
import com.oneconnect.leadership.library.activities.SubscriberPresenter;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.lists.HarmonyListFragment;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.List;

public class HarmonyThoughtsActivity extends AppCompatActivity implements HarmonyThoughtAdapter.HarmonyThoughtAdapterlistener{

    SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private Context ctx;
    ResponseBag bag;
    private UserDTO user;
    private int type;
    EntityListFragment entityListFragment;
    HarmonyListFragment harmonyListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harmony_thoughts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();
        harmonyListFragment = (HarmonyListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        user = SharedPrefUtil.getUser(this);
        setFragment();
    }

    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment");
        SharedPrefUtil.saveFragmentType(bag.getType(), this);
        type = bag.getType();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(com.oneconnect.leadership.library.R.anim.slide_in_left,R.anim.slide_out_right);
        entityListFragment = EntityListFragment.newInstance(bag);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }



    @Override
    public void onPldpRequested(DailyThoughtDTO dailyThought) {

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
    public void onUrlRequired(UrlDTO url) {

    }

    @Override
    public void onPhotosRequired(List<PhotoDTO> list) {

    }
    static final String TAG = HarmonyThoughtsActivity.class.getSimpleName();
}
