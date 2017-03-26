package com.oneconnect.leadership.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

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

    public static void saveFragmentType(int type, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt("type", type);
        ed.commit();
        Log.d(TAG, "saveFragmentType " + type);
    }

    public static int getFragmentType(Context ctx) {
        if (ctx == null) return 0;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        int token = sp.getInt("type", 0);
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

    public static void saveDailyThought(DailyThoughtDTO dt, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("dailyThought", gson.toJson(dt));
        ed.commit();
        Log.d(TAG, "saveDailyThought: " + dt.getTitle());
    }

    public static DailyThoughtDTO getDailyThought(Context ctx) {

        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("dailyThought", null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, DailyThoughtDTO.class);
    }

    public static void saveWeeklyMessage(WeeklyMessageDTO message, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("weeklyMessage", gson.toJson(message));
        ed.commit();
        Log.d(TAG, "saveWeeklyMessage " + message.getTitle());
    }

    public static WeeklyMessageDTO getWeeklyMessage(Context ctx) {

        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("weeklyMessage", null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, WeeklyMessageDTO.class);
    }
    public static void saveWeeklyMasterclass(WeeklyMasterClassDTO masterclass, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("weeklyMasterclass", gson.toJson(masterclass));
        ed.commit();
        Log.d(TAG, "saveWeeklyMasterclass " + masterclass.getTitle());
    }

    public static WeeklyMasterClassDTO getWeeklyMasterclass(Context ctx) {

        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("weeklyMasterclass", null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, WeeklyMasterClassDTO.class);
    }
    public static void savePodcast(PodcastDTO podcast, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("podcast", gson.toJson(podcast));
        ed.commit();
        Log.d(TAG, "savePodcast " + podcast.getTitle());
    }

    public static PodcastDTO getPodcast(Context ctx) {

        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("podcast", null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, PodcastDTO.class);
    }

}
