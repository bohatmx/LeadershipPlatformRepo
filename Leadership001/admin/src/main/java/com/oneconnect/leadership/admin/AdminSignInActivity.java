package com.oneconnect.leadership.admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
        check();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
    private void proceed() {
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
        Toasty.success(this, getString(R.string.success),
                Toast.LENGTH_LONG, true).show();
        Intent m = new Intent(this, CrudActivity.class);
        startActivity(m);
        finish();
    }

    @Override
    public void onLoginFailed() {
        Log.e(TAG, "..................onLoginFailed: ");
        showSnackbar(getString(R.string.login_failed), getString(R.string.not_ok), "red");

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

    public static final int PERMISSIONS_REQUEST = 113;

    private void check() {
        Log.w(TAG, "check: PERMISSIONS_REQUEST" );
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST);
            return;

        }
        proceed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: .................");
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "onRequestPermissionsResult: ".concat(permissions.toString())
                            .concat("\n").concat(grantResults.toString()));
                    Log.e(TAG, "onRequestPermissionsResult: PERMISSION_GRANTED");
                    proceed();

                } else {
                    Log.e(TAG, "onRequestPermissionsResult: PERMISSION_DENIED" );
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
