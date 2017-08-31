package com.oneconnect.leadership.library.util;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

/**
 * Created by Kurisani on 2017/08/30.
 */

public class UpdatePresenter implements UpdateContract.Presenter {
    private UpdateContract.View view;
    private DataAPI dataAPI;
    private ListAPI listAPI;

    public UpdatePresenter(UpdateContract.View view) {
        this.view = view;
        dataAPI = new DataAPI();
        listAPI = new ListAPI();
    }

    @Override
    public void updateUser(final UserDTO user) {
        dataAPI.updateUser(user, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onUserUpdated(user);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void updateDailyThought(DailyThoughtDTO dailyThought) {

    }

    @Override
    public void updateWeeklyMasterClass(WeeklyMasterClassDTO masterClass) {

    }

    @Override
    public void updateWeeklyMessage(WeeklyMessageDTO weeklyMessage) {

    }

    @Override
    public void updateNews(NewsDTO news) {

    }

    @Override
    public void updateCategory(final CategoryDTO category) {
        dataAPI.updateCategory(category, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onCategoryUpdated(category);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }
}
