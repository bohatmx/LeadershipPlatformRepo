package com.oneconnect.leadership.library.activities;

import com.oneconnect.leadership.library.data.BaseDTO;

/**
 * Created by aubreymalabie on 3/17/17.
 */

public class SheetContract {
    public interface Presenter {
        void addEntity(BaseDTO entity);
        void updateEntity(BaseDTO entity);
        void deleteEntity(BaseDTO entity);

    }

    public interface View {
        void onEntityAdded(String key);
        void onEntityUpdated();
        void onEntityDeleted();

        void onError(String message);
    }
}
