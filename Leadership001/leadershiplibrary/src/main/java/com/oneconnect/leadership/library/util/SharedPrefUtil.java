package com.oneconnect.leadership.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.Random;

import static com.google.android.gms.common.Scopes.PROFILE;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class SharedPrefUtil {
    public static final String TAG = SharedPrefUtil.class.getSimpleName();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static final String THEME = "theme";

    public static void saveCloudMsgToken(String token, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("token", token);
        ed.commit();
        Log.d(TAG, "saveCloudMsgToken " + token);
    }
    public static void clearProfile(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        sp.edit().remove(PROFILE).commit();
    }

    public static String getCloudMsgToken(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String token = sp.getString("token", null);
        return token;
    }

    public static int getThemeSelection(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        int j = sp.getInt(THEME, -1);
        Log.i(TAG, "#### theme retrieved: " + j);
        return j;
    }

    public static void setThemeSelection(Context ctx, int theme) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(THEME, theme);
        ed.commit();

        Log.w(TAG, "#### theme saved: " + theme);

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

    public static void saveNewsArticle(NewsDTO dt, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("newsArticle", gson.toJson(dt));
        ed.commit();
        Log.d(TAG, "saveNewsArticle: " + dt.getTitle());
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
    public static void setCityImages(Context ctx, CityImages images) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);

        SharedPreferences.Editor ed = sp.edit();
        String json = gson.toJson(images);
        ed.putString(CITY_IMAGES, json);
        ed.commit();

        Log.w(LOG, "#### cityImages saved: " + json);

    }
    static Random random = new Random(System.currentTimeMillis());

    public static CityImages getCityImages(Context ctx) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String json = sp.getString(CITY_IMAGES, null);
        if (json == null) {
            Log.e(LOG, "#### cityImages NOT retrieved");
            return null;
        }
        CityImages cityImages = gson.fromJson(json, CityImages.class);
//        Log.i(LOG, "#### cityImages retrieved: " + cityImages.getImageResourceIDs().length);
        return cityImages;
    }

    static int lastIndex;
    public static Bitmap getRandomCityImage(Context ctx) {
        CityImages cityImages = getCityImages(ctx);
        int index = random.nextInt(cityImages.getImageResourceIDs().length - 1);
//        Log.w(LOG,"getRandomCityImage index: " + index + " lastIndex: " + lastIndex);
        if (lastIndex != 0) {
            while (index != lastIndex) {
                index = random.nextInt(cityImages.getImageResourceIDs().length - 1);
            }
        }
        int id = cityImages.getImageResourceIDs() [index];
        Drawable drawable = ContextCompat.getDrawable(ctx, id);
        BitmapDrawable bd = (BitmapDrawable)drawable;
        Bitmap k = getResizedBitmap(bd.getBitmap(), 560, 380);
        return k;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

// recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    static final String  CITY_IMAGES = "cityImages";
    static final String LOG = SharedPrefUtil.class.getSimpleName();
}
