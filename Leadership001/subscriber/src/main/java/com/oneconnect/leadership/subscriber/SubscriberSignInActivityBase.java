package com.oneconnect.leadership.subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.login.BaseLoginActivity;
//import com.twitter.sdk.android.core.Twitter;

import es.dmoral.toasty.Toasty;

public class SubscriberSignInActivityBase extends BaseLoginActivity {
    public static final String TAG = SubscriberSignInActivityBase.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: subscriber ######################");
        setContentView(R.layout.activity_subscriber_sign_in);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        type = UserDTO.SUBSCRIBER;

        if (firebaseAuth.getCurrentUser() == null) {
            startSubscriberLogin();
        } else {
            Log.i(TAG, "onCreate: user already signed in. no need for toast");
            startMain();
        }
    }


    @Override
    public void onLoginSucceeded() {
        Log.i(TAG, "+++++++++++++ onLoginSucceeded: ");
        startMain();
    }

    boolean isAlreadySignedIn = true;

    private void startMain() {
        Toasty.success(this, getString(R.string.success),
                Toast.LENGTH_LONG, true).show();
        Intent m = new Intent(SubscriberSignInActivityBase.this,SubscriberMainActivity.class);
        startActivity(m);
        finish();


       /* if (isAlreadySignedIn) {
            Intent intent = new Intent(this, SubscriberMainActivity.class);
            startActivity(intent);
            finish();
            //return;
        } else {
            Toasty.success(this, getString(R.string.success),
                    Toast.LENGTH_LONG, true).show();
            Intent m = new Intent(this, SubscriberMainActivity.class);
            startActivity(m);
            finish();
        }*/
    }

    @Override
    public void onLoginFailed() {
        Log.e(TAG, "onLoginFailed: ");
        Toasty.error(this,getString(R.string.signin_failed), Toast.LENGTH_LONG,true).show();
        showSnackbar(getString(R.string.signin_failed),"OK","red");

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
