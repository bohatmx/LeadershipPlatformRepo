package com.oneconnect.leadership.library.cache;

import com.oneconnect.leadership.library.data.UserDTO;

import java.util.List;

/**
 * Created by aubreymalabie on 3/19/17.
 */

public class UserCacheContract {

    public interface Presenter {
        void getUsers();

    }

    public interface View {
        void onCachedUsers(List<UserDTO> users);

        void onError(String message);

    }
}
