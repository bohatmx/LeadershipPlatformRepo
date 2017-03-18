package com.oneconnect.leadership.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.oneconnect.leadership.admin.crud.CrudActivity;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.login.BaseLoginActivity;

import es.dmoral.toasty.Toasty;

public class AdminSignInActivity extends BaseLoginActivity {
    public static final String TAG = AdminSignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: @@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        setContentView(R.layout.activity_admin_sign_in);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.platform_signin);
        type = UserDTO.COMPANY_STAFF;
        if (firebaseAuth.getCurrentUser() == null) {
            startLogin();
        } else {
            startMain();
        }

    }

    @Override
    public void onLoginSucceeded() {
        Log.i(TAG, "+++++++++++++ onLoginSucceeded: ");
        startMain();
    }

    private void startMain() {
        Toasty.success(this,getString(R.string.success),
                Toast.LENGTH_LONG,true).show();
        Intent m = new Intent(this,CrudActivity.class);
        startActivity(m);
    }

    @Override
    public void onLoginFailed() {
        Log.e(TAG, "..................onLoginFailed: ");
        showSnackbar(getString(R.string.login_failed),getString(R.string.not_ok),"red");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

        if (id == R.id.action_logout) {
            logoff();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
