package com.oneconnect.leadership.library.login;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.data.ResponseBag;
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
        api.getUserByEmail(email, new DataAPI.OnDataRead() {
            @Override
            public void onResponse(ResponseBag responseBag) {
                view.onUserFound(responseBag.getUsers().get(0));
            }

            @Override
            public void onError() {
                 view.onError("User not found in database");
            }
        });
    }

    @Override
    public void addUser(final UserDTO user) {
        api.addUser(user, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onUserAdded(user);
            }

            @Override
            public void onError(String message) {
                 view.onError(message);
            }
        });
    }
}
