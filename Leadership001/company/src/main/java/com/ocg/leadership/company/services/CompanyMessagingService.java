package com.ocg.leadership.company.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.ocg.leadership.company.NotifHandlerActivity;
import com.ocg.leadership.company.R;
import com.oneconnect.leadership.library.data.FCMData;
import com.oneconnect.leadership.library.fcm.EndpointUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Kurisani on 2017/09/13.
 */

public class CompanyMessagingService extends FirebaseMessagingService {

    public CompanyMessagingService() {
    }

    public static final String TAG = CompanyMessagingService.class.getSimpleName(),
            BROADCAST_LOCATION_RESPONSE_ARRIVED = "com.oneconnect.BROADCAST_LOCATION_RESPONSE_ARRIVED",
            BROADCAST_MESSAGE_RECEIVED = "com.oneconnect.BROADCAST_MESSAGE_RECEIVED";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "=========================================\n" +
                "onMessageReceived: from: " + remoteMessage.getFrom()
                + " to:" + remoteMessage.getTo() + " collapseKey: "
                + remoteMessage.getCollapseKey());
        boolean isRunning = isMainActivityRunning("com.ocg.leadership.company");
        Log.w(TAG, "onMessageReceived: com.ocg.leadership.company isRunning: " + isRunning);
        //check for notifcations without app data structures
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            if (isRunning) {
                Intent m = new Intent(BROADCAST_MESSAGE_RECEIVED);
                FCMData f = new FCMData();
                f.setTitle(title);
                f.setMessage(body);
                m.putExtra("data", f);
                LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getApplicationContext());
                bm.sendBroadcast(m);
                Log.w(TAG, "onMessageReceived: BROADCAST_MESSAGE_RECEIVED has been broadcast to app" );
            } else {
                sendNotification(title, body);
            }
            return;
        }
        try {
            Map<String, String> map = remoteMessage.getData();
            for (String s : map.keySet()) {
                Log.d(TAG, "onMessageReceived: name: " + s + " value: " + map.get(s));
            }
            FCMData data = new FCMData();
            data.setMessage(map.get("message"));
            data.setTitle(map.get("title"));
            data.setFromUser(map.get("fromUser"));
            data.setUserID(map.get("userID"));
            data.setJson(map.get("json"));
            data.setMessageType(Integer.parseInt(map.get("messageType")));
            if (map.get("expiryDate") != null)
                data.setExpiryDate(Long.parseLong(map.get("expiryDate")));
            if (map.get("date") != null)
                data.setDate(Long.parseLong(map.get("date")));
            Log.d(TAG, "onMessageReceived: " + gson.toJson(data));

            if (data.getMessageType() == EndpointUtil.ADMINS) {
                Log.w(TAG, "onMessageReceived: responding to ADMINS ....");

                return;
            }
            if (data.getMessageType() == EndpointUtil.COMPANY) {
                Log.w(TAG, "onMessageReceived: responding to company ....");
            }
            if (isRunning) {
                Intent m = new Intent(BROADCAST_MESSAGE_RECEIVED);
                m.putExtra("data", data);
                LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getApplicationContext());
                bm.sendBroadcast(m);
                Log.w(TAG, "onMessageReceived: BROADCAST_MESSAGE_RECEIVED has been broadcast to app" );
            } else {
                sendNotification(data);
            }


        } catch (Exception e) {
            Log.e(TAG, "ERROR onMessageReceived: ", e);
            FirebaseCrash.report(new Exception("Error receiving FCM message: ".concat(e.getMessage())));
        }
    }

    private void sendNotification(FCMData message) {
        if (message.getTitle() == null) {
            message.setTitle("Leadership Platform");
        }
        StringBuilder sb = new StringBuilder();
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_clipboard)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle(message.getTitle())
                        .setContentText(message.getMessage());

        Intent resultIntent = new Intent(this, NotifHandlerActivity.class);
        resultIntent.putExtra("data", message);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 0014;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        Log.e(TAG, "sendNotification: notification has been sent");


    }

    private void sendNotification(String title, String body) {

        StringBuilder sb = new StringBuilder();
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_clipboard)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentTitle(title)
                        .setContentText(body);

        Intent resultIntent = new Intent(this, NotifHandlerActivity.class);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("body", body);


        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setTicker(body);
        int mNotificationId = 0011;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        Log.e(TAG, "sendNotification: notification has been sent");


    }

    public boolean isMainActivityRunning(String packageName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (int i = 0; i < tasksInfo.size(); i++) {
            Log.d(TAG, "This app is RUNNING: " +
                    tasksInfo.get(i).baseActivity.getPackageName().toString());
            if (tasksInfo.get(i).baseActivity.getPackageName().toString().equals(packageName))
                return true;
        }

        return false;
    }

    static final Gson gson = new Gson();
}
