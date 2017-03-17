package com.oneconnect.leadership.library.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.ocg.backend.endpointAPI.model.Data;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.ocg.backend.endpointAPI.model.FCMessageDTO;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.fcm.EndpointContract;
import com.oneconnect.leadership.library.fcm.EndpointPresenter;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public abstract class BaseLoginActivity extends AppCompatActivity
        implements LoginContract.View, EndpointContract.View {

    FirebaseAuth firebaseAuth;
    public Toolbar toolbar;
    LoginPresenter presenter;
    EndpointPresenter fcmPresenter;
    public int type;
    ProgressDialog progressDialog;

    private static final int REQUEST_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: +++++++++++++++++++++++++++");
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            onLoginSucceeded();
            return;
        }
        presenter = new LoginPresenter(this);
        fcmPresenter = new EndpointPresenter(this);
    }

    public void startLogin() {
        Log.d(TAG, "startLogin: +++++++++++++++++++++++++++");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)   //todo remove on release
                        .setTheme(R.style.GreenTheme)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .build(),
                REQUEST_SIGN_IN);
    }

    public void logoff() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                         onLoggedOut();
                    }
                });
    }

    private void onLoggedOut() {
           finish();
    }

    public abstract void onLoginSucceeded();

    public abstract void onLoginFailed();

    private void getAppUser(String email) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Setting up app user...please wait");
        progressDialog.show();
        presenter.getUserByEmail(email);
    }

    String email;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ......... resultCode " + resultCode);
        if (requestCode == REQUEST_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == ResultCodes.OK) {
                Log.i(TAG, "onActivityResult: resultCode OK");
                email = response.getEmail();
                getAppUser(response.getEmail());
                return;
            } else {
                Log.e(TAG, "onActivityResult: login not ok" );
                if (response == null) {
                    showSnackbar(getString(R.string.sign_in_cancelled), "Bad", "red");
                    onLoginFailed();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(getString(R.string.no_internet_connection), "Bad", "red");
                    onLoginFailed();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(getString(R.string.unknown_error), "Bad", "red");
                    onLoginFailed();
                    return;
                }
            }

            showSnackbar(getString(R.string.unknown_sign_in_response), "Bad", "red");
            onLoginFailed();
        }
    }

    public Snackbar snackbar;

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.dismiss();

    }

    @Override
    public void onUserFound(UserDTO user) {
        Log.i(TAG, "onUserFound: ");
        progressDialog.dismiss();
        SharedPrefUtil.saveUser(user, this);
        addFCMUser(user);
        onLoginSucceeded();
    }

    @Override
    public void onUserAdded(UserDTO user) {
        Log.i(TAG, "onUserAdded: ");
        progressDialog.dismiss();
        SharedPrefUtil.saveUser(user, this);
        addFCMUser(user);
        onLoginSucceeded();
    }
    private void addFCMUser(UserDTO u) {
        FCMUserDTO m = Util.createFCMUser(u,
                SharedPrefUtil.getCloudMsgToken(this));
        fcmPresenter.saveUser(m);
    }
    @Override
    public void onFCMUserSaved(FCMResponseDTO response) {
        Log.i(TAG, "onFCMUserSaved: ");
        if (response.getStatusCode() == 0) {
            UserDTO u = SharedPrefUtil.getUser(this);
            Data d = new Data();
            d.setDate(new Date().getTime());
            d.setMessage("Welcome to the best Leadership Platform in the world!");
            d.setFromUser("Leadership Platform");
            d.setTitle("Leadership Platform Welcome");
            FCMessageDTO m = new FCMessageDTO();
            m.setCompanyID(u.getCompanyID());
            m.setDate(new Date().getTime());
            m.setData(d);
            m.setUserIDs(new ArrayList<String>());
            m.getUserIDs().add(u.getUserID());
            fcmPresenter.sendMessage(m);
        }
    }

    @Override
    public void onMessageSent(FCMResponseDTO response) {
        if (response.getStatusCode() == 0) {
            Log.i(TAG, "onMessageSent: welcome message sent");
        } else {
            Log.e(TAG, "onMessageSent: welcome message failed" );
            FirebaseCrash.report(new Exception("Welcome message failed"));
        }
    }
    public static final String TAG = BaseLoginActivity.class.getSimpleName();
    @Override
    public void onEmailSent(EmailResponseDTO response) {

    }

    @Override
    public void onError(String message) {
        Log.e(TAG, "............onError: ".concat(message) );
        progressDialog.dismiss();
        if (type == UserDTO.SUBSCRIBER) {
            UserDTO u = new UserDTO();
            u.setEmail(email);
            u.setUserType(type);
            u.setUserDescription(UserDTO.DESC_SUBSCRIBER);
            presenter.addUser(u);
        } else {
            Log.d(TAG, "onError: about to call onLoginFailed");
            onLoginFailed();
        }

    }
}
