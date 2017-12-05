package com.oneconnect.leadership.library.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ocg.backend.endpointAPI.model.Data;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.ocg.backend.endpointAPI.model.FCMessageDTO;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.fcm.EndpointContract;
import com.oneconnect.leadership.library.fcm.EndpointPresenter;
import com.oneconnect.leadership.library.fcm.EndpointUtil;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public abstract class BaseLoginActivity extends AppCompatActivity
        implements LoginContract.View, EndpointContract.View {

    public FirebaseAuth firebaseAuth;
    public Toolbar toolbar;
    LoginPresenter presenter;
    EndpointPresenter fcmPresenter;
    public int type;
    ProgressDialog progressDialog;
    UserDTO user;
    Context ctx;

    private static final int REQUEST_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: +++++++++++++++++++++++++++");
        firebaseAuth = FirebaseAuth.getInstance();
        presenter = new LoginPresenter(this);
        fcmPresenter = new EndpointPresenter(this);
    }
    public void startSubscriberLogin() {
        Log.d(TAG, "startLogin: +++++++++++++++++++++++++++");
        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .setPermissions(Arrays.asList(Scopes.EMAIL, Scopes.PROFILE))
                .build();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)   //todo remove on release
                        .setTheme(R.style.RedTheme)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build() ))/*,
                                googleIdp,
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()*/
                        .build(),
                REQUEST_SIGN_IN);
    }

    public void startAdminLogin() {
        Log.d(TAG, "startLogin: +++++++++++++++++++++++++++");
        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .setPermissions(Arrays.asList(Scopes.EMAIL, Scopes.PROFILE))
                .build();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)   //todo remove on release
                        .setTheme(R.style.RedTheme)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()/*,
                                googleIdp,
                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()*/))
                        .build(),
                REQUEST_SIGN_IN);
    }

    public void startCorporateLogin() {
        Log.d(TAG, "startLogin: +++++++++++++++++++++++++++");
        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .setPermissions(Arrays.asList(Scopes.EMAIL, Scopes.PROFILE))
                .build();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)   //todo remove on release
                        .setTheme(R.style.RedTheme)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()/*,
                                googleIdp,
                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()*/))
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
    IdpResponse idpResponse;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: ......... resultCode " + resultCode);
        if (requestCode == REQUEST_SIGN_IN) {
            idpResponse = IdpResponse.fromResultIntent(data);

            Log.d(TAG, "onActivityResult: IdpResponse: ".concat(GSON.toJson(idpResponse)));
            if (resultCode == ResultCodes.OK) {
                Log.i(TAG, "onActivityResult: resultCode OK");
                email = idpResponse.getEmail();
                getAppUser(idpResponse.getEmail());
                return;
            } else {
                Log.e(TAG, "onActivityResult: login not ok");
                if (idpResponse == null) {
                    Toasty.error(getApplicationContext(), "sign in cancelled", Toast.LENGTH_LONG, true).show();
                   // showSnackbar(getString(R.string.sign_in_cancelled), "Bad", "red");
                    onLoginFailed();
                    return;
                }

                if (idpResponse.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(getString(R.string.no_internet_connection), "Bad", "red");
                    onLoginFailed();
                    return;
                }

                if (idpResponse.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
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
        snackbar.show();
    }

    @Override
    public void onUserFoundByEmail(UserDTO user) {
        Log.i(TAG, "++++++++++++++++++ onUserFound by email: ".concat(user.getEmail()));
        progressDialog.dismiss();
        SharedPrefUtil.saveUser(user, this);
        addFCMUser(user);

    }

    @Override
    public void onUserNotFoundByEmail() {
        progressDialog.dismiss();
        UserDTO u = new UserDTO();
        switch (type) {
            case UserDTO.SUBSCRIBER:
                u.setEmail(email);
                u.setUserType(type);
                Log.d(TAG, "onUserNotFoundByEmail: this is a subscriber, adding to app database ...");
                presenter.addUser(u);
                break;
            case UserDTO.LEADER:
                u.setEmail(email);
                u.setUserType(type);
                Log.d(TAG, "onUserNotFoundByEmail: this is a leader, adding to app database ...");
                presenter.addUser(u);
                break;
            case UserDTO.PLATINUM_USER:
                u.setEmail(email);
                u.setUserType(type);
                Log.d(TAG, "onUserNotFoundByEmail: this is a GOLD user, adding to app database ...");
                presenter.addUser(u);
                break;
            case UserDTO.GOLD_USER:
                u.setEmail(email);
                u.setUserType(type);
                Log.d(TAG, "onUserNotFoundByEmail: this is a PLATINUM user, adding to app database ...");
                presenter.addUser(u);
                break;
            case UserDTO.COMPANY_STAFF:
                Log.d(TAG, "onUserNotFoundByEmail: staff member not found, failing");
                onLoginFailed();
                break;
        }

    }

    @Override
    public void onUserAddedToAppDatabase(UserDTO user) {
        Log.i(TAG, "############### onUserAdded to app's database, now adding to fcm: ".concat(user.getEmail()));
        progressDialog.dismiss();
        SharedPrefUtil.saveUser(user, this);
        addFCMUser(user);

        onLoginSucceeded();
    }
    @Override
    public void onUserAlreadyExists(UserDTO user) {
        SharedPrefUtil.saveUser(user,this);
        addFCMUser(user);
    }
    private void addFCMUser(UserDTO u) {
        FCMUserDTO m = Util.createFCMUser(u,
                SharedPrefUtil.getCloudMsgToken(this));
        Log.d(TAG, "addFCMUser: adding fcmuser to gae ".concat(GSON.toJson(m)));
        fcmPresenter.saveUser(m);
    }

    @Override
    public void onFCMUserSaved(FCMResponseDTO response) {
        Log.i(TAG, "onFCMUserSaved: ".concat(response.getMessage()));
        if (response.getStatusCode() == 0) {
            UserDTO u = SharedPrefUtil.getUser(this);
            Data d = new Data();
            d.setDate(new Date().getTime());
            d.setFromUser("Leadership Platform");
            FCMessageDTO m = new FCMessageDTO();
            m.setCompanyID(u.getCompanyID());
            m.setDate(new Date().getTime());
            m.setData(d);
            m.setUserIDs(new ArrayList<String>());
            m.getUserIDs().add(u.getUserID());
            switch (u.getUserType()) {
                case UserDTO.SUBSCRIBER:
                    d.setMessage(getString(R.string.welcome_platform));
                    d.setTitle(getString(R.string.platform_welcome));
                    break;

                case UserDTO.COMPANY_STAFF:
                    d.setMessage(getString(R.string.staff_welcome));
                    d.setTitle(u.getCompanyName());
                    break;

                case UserDTO.LEADER:
                    d.setMessage(getString(R.string.leader_welcome));
                    if (u.getCompanyName() != null) {
                        d.setTitle(u.getCompanyName());
                    }
                    break;

                case UserDTO.PLATINUM_USER:
                    d.setMessage(getString(R.string.welcome_platform));
                    d.setTitle(getString(R.string.platform_welcome));
                    break;

                case UserDTO.GOLD_USER:
                    d.setMessage(getString(R.string.welcome_platform));
                    d.setTitle(getString(R.string.platform_welcome));
                    break;

                case UserDTO.COMPANY_ADMIN:
                    d.setMessage(getString(R.string.staff_welcome));
                    d.setTitle(u.getCompanyName());
                    break;

                case UserDTO.PLATINUM_ADMIN:
                    d.setMessage(getString(R.string.staff_welcome));
                    d.setTitle(u.getCompanyName());
                    break;

                case UserDTO.STANDARD_USER:
                    d.setMessage(getString(R.string.welcome_platform));
                    d.setTitle(getString(R.string.platform_welcome));
                    break;



            }
            Log.d(TAG, "onFCMUserSaved: about to send welcome message");
            fcmPresenter.sendMessage(m);
            subscribe(u);
            onLoginSucceeded();
        } else {
            FirebaseCrash.report(new Exception("Unable to save FCM user "));
            Log.e(TAG, "onFCMUserSaved: unable to save FCM user");
            onLoginFailed();
        }
    }

    @Override
    public void onMessageSent(FCMResponseDTO response) {
        if (response.getStatusCode() == 0) {
            Log.i(TAG, "onMessageSent: welcome message sent");
        } else {
            Log.e(TAG, "onMessageSent: welcome message failed");
            FirebaseCrash.report(new Exception("Welcome message failed"));
        }
    }

    private void subscribe(UserDTO user) {

        FirebaseMessaging.getInstance().subscribeToTopic(EndpointUtil.TOPIC_GENERAL);
        Log.w(TAG, "################# ==> Subscribed to topic general");

        switch (user.getUserType()) {
            case UserDTO.SUBSCRIBER:
                FirebaseMessaging.getInstance().subscribeToTopic(
                        EndpointUtil.TOPIC_SUBSCRIBER);
                Log.w(TAG, "################## ==> Subscribed to topic: subscriber");
                break;
            case UserDTO.COMPANY_STAFF:
                if (user.getCompanyID() != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(
                            EndpointUtil.TOPIC_COMPANY_STAFF + user.getCompanyID());
                    Log.w(TAG, "################## ==> Subscribed to topic: company_staff"
                            .concat(user.getCompanyID()));
                }
                break;
            case UserDTO.LEADER:
                if (user.getCompanyID() != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(
                            EndpointUtil.TOPIC_LEADER + user.getCompanyID());
                    Log.w(TAG, "################## ==> Subscribed to topic: leader"
                            .concat(user.getCompanyID()));
                }
                break;

            case UserDTO.PLATINUM_USER:
                if (user.getCompanyID() != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(
                            EndpointUtil.TOPIC_DAILY_THOUGHT/*TOPIC_GOLD*/ + user.getCompanyID());
                    Log.w(TAG, "################## ==> Subscribed to topic: Gold"
                            .concat(user.getCompanyID()));
                }
                break;

            case UserDTO.GOLD_USER:
                if (user.getCompanyID() != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(
                            EndpointUtil.TOPIC_DAILY_THOUGHT + user.getCompanyID());
                    Log.w(TAG, "################## ==> Subscribed to topic: Platinum"
                            .concat(user.getCompanyID()));
                }
                break;
            case UserDTO.STANDARD_USER:
                if (user.getCompanyID() != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(
                            EndpointUtil.TOPIC_DAILY_THOUGHT + user.getCompanyID());
                    Log.w(TAG, "################## ==> Subscribed to topic: DailyThought"
                            .concat(user.getCompanyID()));
                }
                break;
            case UserDTO.COMPANY_ADMIN:
                if (user.getCompanyID() != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(
                            EndpointUtil.TOPIC_DAILY_THOUGHT + user.getCompanyID());
                    Log.w(TAG, "################## ==> Subscribed to topic: DailyThought"
                            .concat(user.getCompanyID()));
                }
                break;
            case UserDTO.PLATINUM_ADMIN:
                if (user.getCompanyID() != null) {
                    FirebaseMessaging.getInstance().subscribeToTopic(
                            EndpointUtil.TOPIC_DAILY_THOUGHT + user.getCompanyID());
                    Log.w(TAG, "################## ==> Subscribed to topic: DailyThought"
                            .concat(user.getCompanyID()));
                }
                break;
        }

    }

    public static final String TAG = BaseLoginActivity.class.getSimpleName();

    @Override
    public void onEmailSent(EmailResponseDTO response) {
        Log.i(TAG, "onEmailSent: ".concat(response.getMessage()));
    }

    @Override
    public void onError(String message) {
        Log.e(TAG, "............onError: ".concat(message));
        progressDialog.dismiss();
        onLoginFailed();

    }
}
