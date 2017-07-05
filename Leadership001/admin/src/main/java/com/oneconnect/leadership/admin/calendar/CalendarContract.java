package com.oneconnect.leadership.admin.calendar;

import com.oneconnect.leadership.library.data.CalendarEventDTO;

/**
 * Created by aubreymalabie on 3/25/17.
 */

public class CalendarContract {
    public interface Presenter {
        void addCalendarEvent(CalendarEventDTO event);
    }
    public interface View {
        void onCalendarEventAdded(String key);
        void onError(String message);
    }
}
