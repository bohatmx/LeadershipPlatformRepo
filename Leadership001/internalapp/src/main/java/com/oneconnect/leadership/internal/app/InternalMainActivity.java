package com.oneconnect.leadership.internal.app;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.UserDTO;

public class InternalMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DataAPI api = new DataAPI();
    Toolbar toolbar;
    Snackbar snackbar;
    DrawerLayout drawer;
    TextInputEditText txtCompany, txtEmail, txtAdminEmail, txtPassword;
    FloatingActionButton btn;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: %%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        setContentView(R.layout.activity_admin_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leadership Platform Internal");
        getSupportActionBar().setSubtitle("Company Data Management");

        setup();
        if (auth.getCurrentUser() == null) {
            signIn();
            return;
        } else {
            Log.i(TAG, "onCreate: user already logged in");
        }

    }

    private void setup() {
        Log.d(TAG, "setup: &&&&&&&&&&&&&&&&&&&&&&");
        txtCompany = (TextInputEditText) findViewById(R.id.company);
        txtEmail = (TextInputEditText) findViewById(R.id.email);
        txtAdminEmail = (TextInputEditText) findViewById(R.id.adminEmail);
        txtPassword = (TextInputEditText) findViewById(R.id.adminPassword);
        btn = (FloatingActionButton) findViewById(R.id.fab);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(TAG, "onClick: +++++++++++++++++++" );
                addCompany();

            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void signIn() {
        Log.w(TAG, "signIn: ....................." );
        api.signIn("aubreym@oneconnectgroup.com", "kktiger3", new DataAPI.OnSignedIn() {
            @Override
            public void onSuccess(FirebaseUser user) {
                showSnackbar("Signed in: " + user.getEmail(),"OK","green");
                 addCompany();
            }

            @Override
            public void onError() {
                 showSnackbar("Failed sign in","bad", "red");
            }
        }
    );
    }
    private void addCompany() {
        if (TextUtils.isEmpty(txtCompany.getText())) {
            txtCompany.setError("Missing company name");
            return;
        }
        if (TextUtils.isEmpty(txtEmail.getText())) {
            txtEmail.setError("Missing email address");
            return;
        }
        if (TextUtils.isEmpty(txtAdminEmail.getText())) {
            txtAdminEmail.setError("Missing admin email address");
            return;
        }
        if (TextUtils.isEmpty(txtPassword.getText())) {
            txtPassword.setError("Missing admin password");
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading data ...");
        progressDialog.show();
        Log.d(TAG, "addCompany: ............");
        final CompanyDTO c = new CompanyDTO();
        c.setCompanyName(txtCompany.getText().toString());
        c.setEmail(txtEmail.getText().toString());

        api.addCompany(c, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                showSnackbar("Company added: " + c.getCompanyName(),"OK","green");
                addAdministrator(key, c.getCompanyName());
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                showSnackbar(message,"bad", "red");
            }
        });
    }

    private void addAdministrator(String companyID, String companyName) {
        Log.w(TAG, "addAdministrator: ............" );
        UserDTO u = new UserDTO();
        u.setCompanyID(companyID);
        u.setCompanyName(companyName);
        u.setEmail(txtAdminEmail.getText().toString());
        u.setFirstName("Administrator");
        u.setLastName(companyName);
        u.setUserType(UserDTO.COMPANY_STAFF);
        u.setPassword(txtPassword.getText().toString());

        api.createUser(u, new DataAPI.CreateUserListener() {
            @Override
            public void onUserCreated(UserDTO user) {
                Log.i(TAG, "addAdministrator: createUser onResponse: ".concat(GSON.toJson(user)));
                progressDialog.dismiss();
                btn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_done_all_white));
                showSnackbar("Admin added in: " + user.getEmail(),"OK","green");
            }

            @Override
            public void onUserAlreadyExists(UserDTO user) {
                progressDialog.dismiss();
                btn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_cancel_white));
                showSnackbar("User already exists","Not OK", "yellow");
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                btn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_cancel_white));
                showSnackbar(message,"Not OK", "red");
            }
        });
    }
    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final String TAG = InternalMainActivity.class.getSimpleName();

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
