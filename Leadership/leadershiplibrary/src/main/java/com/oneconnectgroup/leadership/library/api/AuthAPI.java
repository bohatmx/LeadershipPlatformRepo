package com.oneconnectgroup.leadership.library.api;

/**
 * Created by aubreymalabie on 3/16/17.
 */


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class AuthAPI {

    private FirebaseAuth auth;
    public static final String TAG = AuthAPI.class.getSimpleName();

    public AuthAPI() {
        auth = FirebaseAuth.getInstance();
    }

    public interface LoginListener {
        void onLoggedSucceeded(FirebaseUser user);
        void onLoginFailed(String message);
    }

    public FirebaseUser getLoggedInUser() {
        boolean isLoggedIn = false;
        if (auth.getCurrentUser() == null) {
            return auth.getCurrentUser();
        } else {
            return null;
        }
    }

    public void login(String email, String password, final LoginListener listener) {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.i(TAG, "$$$$$$ onAuthStateChanged: login succeeded. welcome!");
                    listener.onLoggedSucceeded(firebaseAuth.getCurrentUser());
                } else {
                    Log.e(TAG, "onAuthStateChanged: login failed");
                    listener.onLoginFailed("Firebase auth failed");
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.i(TAG, "onComplete: AuthResult is successful: ".concat(task.getResult().getUser().getUid()));
                } else {
                    Log.e(TAG, "onComplete: AuthResult is BAD");
                }
                Log.d(TAG, "onComplete: signing in, waiting for auth state change");
            }
        });
    }

    public void loginAnonymously(final LoginListener listener) {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.i(TAG, "##### onAuthStateChanged: login succeeded. welcome!");
                    listener.onLoggedSucceeded(firebaseAuth.getCurrentUser());
                } else {
                    Log.e(TAG, "onAuthStateChanged: login failed");
                    listener.onLoginFailed("Firebase auth failed");
                }
            }
        };
        auth.addAuthStateListener(authStateListener);
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "onComplete: AuthResult is successful: ".concat(task.getResult().getUser().getUid()));
                } else {
                    Log.e(TAG, "onComplete: AuthResult is BAD");
                }
                Log.d(TAG, "onComplete: signing in Anonymously, waiting for auth state change");
            }
        });
    }
}

