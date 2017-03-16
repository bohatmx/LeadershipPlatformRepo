package com.oneconnect.leadership.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class SharedPrefUtil {
    public static final String TAG = SharedPrefUtil.class.getSimpleName();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveCloudMsgToken(String token, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("token", token);
        ed.commit();
        Log.d(TAG, "saveCloudMsgToken " + token);
    }

    public static String getCloudMsgToken(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String token = sp.getString("token", null);
        return token;
    }


    public static void saveUser(UserDTO user, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("user", gson.toJson(user));
        ed.commit();
        Log.d(TAG, "saveUser: " + user.getEmail());
    }

    public static UserDTO getUser(Context ctx) {

        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("user", null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, UserDTO.class);
    }

}
