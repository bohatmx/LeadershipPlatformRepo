package com.oneconnect.leadership.library.util;

import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

/**
 * Created by Kurisani on 2017/08/30.
 */

public class UpdateContract {

    public interface Presenter {
        void updateUser(UserDTO user);
        void updateDailyThought(DailyThoughtDTO dailyThought);
        void updateWeeklyMasterClass(WeeklyMasterClassDTO masterClass);
        void updateWeeklyMessage(WeeklyMessageDTO weeklyMessage);
        void updateNews(NewsDTO news);
        void updateCategory(CategoryDTO category);
    }

    public interface View {
        void onDailyThoughtUpdated(DailyThoughtDTO dailyThought);
        void onWeeklyMasterClassUpdated(WeeklyMasterClassDTO masterClass);
        void onWeeklyMessageUpdated(WeeklyMessageDTO weeklyMessage);
        void onNewsUpdated(NewsDTO news);
        void onUserUpdated(UserDTO user);
        void onCategoryUpdated(CategoryDTO category);
        void onError(String message);
    }
}
