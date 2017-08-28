package com.oneconnect.leadership.library.login;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View view;
    DataAPI api;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        api = new DataAPI();
    }

    @Override
    public void getUserByEmail(String email) {
        api.getUserByEmail(email, new DataAPI.EmailQueryListener() {
            @Override
            public void onUserFoundByEmail(UserDTO user) {

                view.onUserFoundByEmail(user);
            }

            @Override
            public void onUserNotFoundByEmail() {
                view.onUserNotFoundByEmail();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void addUser(final UserDTO user) {

        api.addUser(user, new DataAPI.AddUserListener() {
            @Override
            public void onUserAdded(UserDTO user) {
                view.onUserAddedToAppDatabase(user);
            }

            @Override
            public void onUserAlreadyExists(UserDTO user) {
                view.onUserAlreadyExists(user);
            }

            @Override
            public void onError(String message) {
                 view.onError(message);
            }
        });

    }
}
