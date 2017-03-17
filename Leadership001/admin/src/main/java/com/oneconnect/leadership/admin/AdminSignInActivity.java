package com.oneconnect.leadership.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.login.BaseLoginActivity;

public class AdminSignInActivity extends BaseLoginActivity {
    public static final String TAG = AdminSignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: @@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        setContentView(R.layout.activity_admin_sign_in);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leadership Platform Sign In");
        type = UserDTO.COMPANY_STAFF;
        startLogin();

    }

    @Override
    public void onLoginSucceeded() {
        Log.i(TAG, "..................onLoginSucceeded: ");
        Intent m = new Intent(this,AdminMainActivity.class);
        startActivity(m);
    }

    @Override
    public void onLoginFailed() {
        Log.e(TAG, "..................onLoginFailed: ");
        showSnackbar("Login failed","Not OK","red");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logoff();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
