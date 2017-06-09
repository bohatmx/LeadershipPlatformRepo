package com.oneconnect.leadership.subscriber;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.fcm.EndpointContract;
import com.oneconnect.leadership.library.fcm.EndpointPresenter;
import com.oneconnect.leadership.library.login.LoginContract;
import com.oneconnect.leadership.library.login.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, EndpointContract.View{

    EditText input_email, input_password;
    Button btn_login;
    private LoginPresenter presenter;
    public FirebaseAuth firebaseAuth;
    public Toolbar toolbar;
    EndpointPresenter fcmPresenter;
    public int type;
    ProgressDialog progressDialog;

    private static final int REQUEST_SIGN_IN= 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        presenter = new LoginPresenter(this);
        fcmPresenter = new EndpointPresenter(this);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){

        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();
        if (email != null || password != null) {
            presenter.getUserByEmail(email);
        }
    }

    @Override
    public void onUserFoundByEmail(UserDTO user) {

    }

    @Override
    public void onUserNotFoundByEmail() {

    }

    @Override
    public void onUserAddedToAppDatabase(UserDTO user) {

    }

    @Override
    public void onUserAlreadyExists(UserDTO user) {

    }

    @Override
    public void onFCMUserSaved(FCMResponseDTO response) {

    }

    @Override
    public void onMessageSent(FCMResponseDTO response) {

    }

    @Override
    public void onEmailSent(EmailResponseDTO response) {

    }

    @Override
    public void onError(String message) {

    }
}
