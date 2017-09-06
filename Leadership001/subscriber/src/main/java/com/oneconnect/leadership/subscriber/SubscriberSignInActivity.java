package com.oneconnect.leadership.subscriber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.login.BaseLoginActivity;

import es.dmoral.toasty.Toasty;

public class SubscriberSignInActivity extends BaseLoginActivity {


    public static final String TAG = SubscriberSignInActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber_sign_in);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        type = UserDTO.SUBSCRIBER;

        if (firebaseAuth.getCurrentUser() == null) {
            startSubscriberLogin();
        } else {
            Log.i(TAG, "onCreate: user already signed in. no need for toast");
            startMain();
        }
    }

    private void startMain() {
        Toasty.success(this, getString(R.string.success),
                Toast.LENGTH_LONG, true).show();
        Intent m = new Intent(SubscriberSignInActivity.this,CategoryActivity.class);
        startActivity(m);
        finish();
    }

    @Override
    public void onLoginSucceeded() {
        Log.i(TAG, "+++++++++++++ onLoginSucceeded: ");
        startMain();
    }

    @Override
    public void onLoginFailed() {
        Log.e(TAG, "onLoginFailed: ");
        Toasty.error(this,getString(R.string.signin_failed), Toast.LENGTH_LONG,true).show();
        showSnackbar(getString(R.string.signin_failed),"OK","red");
    }
}
