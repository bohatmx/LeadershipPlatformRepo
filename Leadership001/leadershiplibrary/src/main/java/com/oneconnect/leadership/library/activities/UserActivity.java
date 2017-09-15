package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.UserListAdapter;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.lists.UserListFragment;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

public class UserActivity extends AppCompatActivity implements UserListAdapter.IconListener{

    UserListFragment userListFragment;
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
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();
        userListFragment = (UserListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);


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
        //   entityListFragment.setListener(this);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }
    @Override
    public void onTakePicture(UserDTO user) {

    }

    @Override
    public void onGallery(UserDTO user) {

    }

    @Override
    public void onLocation(UserDTO user) {

    }

    @Override
    public void onPhone(UserDTO user) {

    }

    @Override
    public void onDriverLogs(UserDTO driver) {

    }


    static final String TAG = UserActivity.class.getSimpleName();
}
