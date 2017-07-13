package com.oneconnect.leadership.library.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.oneconnect.leadership.library.R;


public class DeepLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData data) {
                        if (data == null) {
                            Log.d(LOG, "getInvitation: no data");
                            return;
                        }

                        // Get the deep link
                        Uri deepLink = data.getLink();

                        // Extract invite
                        FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                        if (invite != null) {
                            String invitationId = invite.getInvitationId();
                        }

                        // Handle the deep link
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG, "getDynamicLink:onFailure", e);
                    }
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (AppInviteReferral.hasReferral(intent)) {
            processReferralIntent(intent);
        }
    }

    private void processReferralIntent(Intent intent) {
        // Extract referral information from the intent
        String invitationId = AppInviteReferral.getInvitationId(intent);
        String deepLink = AppInviteReferral.getDeepLink(intent);

        // Display referral information
        // [START_EXCLUDE]
        Log.d(LOG, "Found Referral: " + invitationId + ":" + deepLink);
        ((TextView) findViewById(R.id.deep_link_text)).setText(getString(R.string.deep_link_fmt, deepLink));
        ((TextView) findViewById(R.id.invitation_id_text)).setText(getString(R.string.invitation_id_fmt, invitationId));
        // [END_EXCLUDE]
    }

    public class DeepLinkManager implements GoogleApiClient.OnConnectionFailedListener,
            ResultCallback, AppInviteInvitationResult{

        private final String TAG = DeepLinkManager.class.getSimpleName();
        private final GoogleApiClient mGoogleApiClient;
        private final FragmentActivity context;

        private DeepLinkListener deepLinkListener;

        @Override
        public Status getStatus() {
            return null;
        }

        @Override
        public Intent getInvitationIntent() {
            return null;
        }

        @Override
        public void onResult(@NonNull Result result) {

        }



    public DeepLinkManager(FragmentActivity activity, DeepLinkListener linkListener) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addApi(AppInvite.API)
                    .enableAutoManage(activity, this)
                    .build();

            this.context = activity;
            this.deepLinkListener = linkListener;
        }


        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.d(TAG, "onConnectionFailed:" + connectionResult);
            deepLinkListener.onConnectionError("Google Play Services error!");
        }

    public void checkForInvites(boolean autoLaunchDeepLink)
    {
        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.

        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, context, autoLaunchDeepLink)
                .setResultCallback(this);
    }

    //@Override
    public void onResult(@NonNull AppInviteInvitationResult result) {
        Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
        if (result.getStatus().isSuccess()) {
            // Extract information from the intent
            Intent intent = result.getInvitationIntent();
            String deepLink = AppInviteReferral.getDeepLink(intent);
            String invitationId = AppInviteReferral.getInvitationId(intent);

            // Because autoLaunchDeepLink = true we don't have to do anything
            // here, but we could set that to false and manually choose
            // an Activity to launch to handle the deep link here.
            // ...
        }
    }
}

    public interface DeepLinkListener{
        void onConnectionError(String errorMessage);
    }

        static final String LOG = DeepLinkActivity.class.getSimpleName();
    }

