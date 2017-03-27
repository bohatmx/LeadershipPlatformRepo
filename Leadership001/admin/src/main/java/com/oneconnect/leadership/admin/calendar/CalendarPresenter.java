package com.oneconnect.leadership.admin.calendar;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.data.CalendarEventDTO;

/**
 * Created by aubreymalabie on 3/25/17.
 */

public class CalendarPresenter implements CalendarContract.Presenter {
    CalendarContract.View view;
    DataAPI api;

    public CalendarPresenter(CalendarContract.View view) {
        this.view = view;
        api = new DataAPI();
    }

    @Override
    public void addCalendarEvent(final CalendarEventDTO event) {
        api.addCalendarEvent(event, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                event.setCalendarEventID(key);
                addEventToEntity(event);
            }

            @Override
            public void onError(String message) {
               view.onError(message);
            }
        });
    }

    private void addEventToEntity(CalendarEventDTO event) {
        api.addCalendarEventToEntity(event, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
               view.onCalendarEventAdded(key);
            }

            @Override
            public void onError(String message) {
               view.onError(message);
            }
        });
    }
}
