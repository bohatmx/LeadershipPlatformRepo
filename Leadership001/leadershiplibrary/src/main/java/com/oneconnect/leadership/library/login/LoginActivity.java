package com.oneconnect.leadership.library.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.Arrays;

public abstract class LoginActivity extends AppCompatActivity
        implements LoginContract.View {

    FirebaseAuth firebaseAuth;
    public Toolbar toolbar;
    LoginPresenter presenter;
    public int type;
    ProgressDialog progressDialog;

    private static final int RC_SIGN_IN = 1123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new LoginPresenter(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            onLoginSucceeded();
            return;
        }
        startLogin();

    }

    private void startLogin() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)   //todo remove on release
                        .setTheme(R.style.RedTheme)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
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
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == ResultCodes.OK) {
                email = response.getEmail();
                getAppUser(response.getEmail());
                return;
            } else {
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

    Snackbar snackbar;

    private void showSnackbar(String title, String action, String color) {
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
        progressDialog.dismiss();
        SharedPrefUtil.saveUser(user, this);
        onLoginSucceeded();
    }

    @Override
    public void onUserAdded(UserDTO user) {
        progressDialog.dismiss();
        SharedPrefUtil.saveUser(user, this);
        onLoginSucceeded();
    }

    @Override
    public void onError(String message) {
        progressDialog.dismiss();
        if (type == UserDTO.SUBSCRIBER) {
            UserDTO u = new UserDTO();
            u.setEmail(email);
            u.setUserType(type);
            u.setUserDescription(UserDTO.DESC_SUBSCRIBER);
            presenter.addUser(u);
        } else {
            onLoginFailed();
        }

    }
}
