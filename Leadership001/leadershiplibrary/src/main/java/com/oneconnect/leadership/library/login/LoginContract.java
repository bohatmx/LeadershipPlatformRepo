package com.oneconnect.leadership.library.login;

import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class LoginContract {
    public interface Presenter {
        void getUserByEmail(String email);
        void addUser(UserDTO user);
    }
    public interface View {
        void onUserFound(UserDTO user);
        void onUserAdded(UserDTO user);
        void onError(String message);
    }
}
