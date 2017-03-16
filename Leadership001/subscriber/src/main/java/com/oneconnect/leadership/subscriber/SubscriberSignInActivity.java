package com.oneconnect.leadership.subscriber;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.login.LoginActivity;

public class SubscriberSignInActivity extends LoginActivity {
    public static final String TAG = SubscriberSignInActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_sign_in);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        type = UserDTO.SUBSCRIBER;

    }

    @Override
    public void onLoginSucceeded() {
        Log.i(TAG, "onLoginSucceeded: ");
    }

    @Override
    public void onLoginFailed() {
        Log.e(TAG, "onLoginFailed: ");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subscriber_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
