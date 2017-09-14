package com.oneconnect.leadership.library.util;

import android.app.Activity;
import android.util.Log;

import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.LeadershipApplication;

public class ThemeChooser {


    /**
     * Set the theme of the activity, according to the configuration.
     */
    public static void setTheme(Activity activity) {

        int theme = SharedPrefUtil.getThemeSelection(activity);

        switch (theme) {

            case App.THEME_BLUE:
              activity.setTheme(R.style.BlueThemeOne);
                break;
            case App.THEME_INDIGO:
                activity.setTheme(R.style.IndigoTheme);
                break;
            case App.THEME_RED:
                activity.setTheme(R.style.RedTheme);
                break;
            case App.THEME_TEAL:
                activity.setTheme(R.style.TealTheme);
                break;
            case App.THEME_BLUE_GRAY:
                activity.setTheme(R.style.BlueGrayTheme);
                break;
            case App.THEME_ORANGE:
                activity.setTheme(R.style.OrangeTheme);
                break;
            case App.THEME_PINK:
                activity.setTheme(R.style.PinkTheme);
                break;
            case App.THEME_CYAN:
                activity.setTheme(R.style.CyanTheme);
                break;
            case App.THEME_GREEN:
                activity.setTheme(R.style.GreenTheme);
                break;
            case App.THEME_GREY:
                activity.setTheme(R.style.GreyTheme);
                break;
            case App.THEME_LIGHT_GREEN:
                activity.setTheme(R.style.LightGreenTheme);
                break;
            case App.THEME_LIME:
                activity.setTheme(R.style.LimeTheme);
                break;
            case App.THEME_PURPLE:
                activity.setTheme(R.style.PurpleTheme);
                break;
            case App.THEME_AMBER:
                activity.setTheme(R.style.AmberTheme);
                break;
            case App.THEME_BROWN:
                activity.setTheme(R.style.BrownTheme);
                break;
            default:
                Log.d("ThemeChooser", "### no theme selected, none set");
                break;
        }
    }
}