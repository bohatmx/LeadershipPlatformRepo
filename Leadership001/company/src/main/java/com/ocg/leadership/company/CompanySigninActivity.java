package com.ocg.leadership.company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.login.BaseLoginActivity;

import es.dmoral.toasty.Toasty;

public class CompanySigninActivity extends BaseLoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_company_signin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        type = UserDTO.PLATINUM_USER;

        if (firebaseAuth.getCurrentUser() == null) {
            startCorporateLogin();
        } else {
            Log.i(TAG, "onCreate: user already signed in. no need for toast");
            startMain();
        }

    }

    private void startMain() {
        Toasty.success(this, getString(R.string.success), Toast.LENGTH_LONG, true).show();
        Intent m = new Intent(CompanySigninActivity.this, CompanyMainActivity.class);
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
        showSnackbar(getString(R.string.signin_failed),"Dismiss","red");
    }
}
