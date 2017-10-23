package com.oneconnect.leadership.library.login;

import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
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
        void onUserFoundByEmail(UserDTO user);
        void onUserNotFoundByEmail();
        void onUserAddedToAppDatabase(UserDTO user);
        void onUserAlreadyExists(UserDTO user);

        void onFCMUserSaved(FCMResponseDTO response);

        void onMessageSent(FCMResponseDTO response);

        void onError(String message);
    }
}
