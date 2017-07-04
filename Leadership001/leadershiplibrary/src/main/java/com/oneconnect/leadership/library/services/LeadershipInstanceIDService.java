package com.oneconnect.leadership.library.services;

import android.os.Build;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.Date;

/**
 * Created by aubreymalabie on 10/8/16.
 */

public class LeadershipInstanceIDService extends FirebaseInstanceIdService {

    public static final String TAG = LeadershipInstanceIDService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.w(TAG, "onTokenRefresh - Refreshed FCM token: " + refreshedToken);

        saveToken(refreshedToken);
    }

    private void saveToken(String token) {
        Log.w(TAG, "saveToken: " + token );

        String oldToken = SharedPrefUtil.getCloudMsgToken(getApplicationContext());
        SharedPrefUtil.saveCloudMsgToken(token,getApplicationContext());

        if (oldToken != null) {
            UserDTO user = SharedPrefUtil.getUser(getApplicationContext());
            FCMUserDTO u = new FCMUserDTO();
            u.setDate(new Date().getTime());
            u.setToken(token);
            u.setAndroidVersion(Build.VERSION.RELEASE);
            u.setUserID(user.getUserID());
            u.setDeviceModel(Build.MODEL);
            u.setManufacturer(Build.MANUFACTURER);
            u.setName(user.getFullName());
            u.setSerialNumber(Build.SERIAL);
            u.setFcmID(u.getUserID() + "@" + u.getSerialNumber());

            //todo send user to GAE for use with FCM
        }




    }

    public static final String MESSAGING = "main";

}
