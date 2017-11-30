package com.oneconnect.leadership.library.pldp;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.data.PldpDTO;
import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by nkululekochrisskosana on 2017/11/24.
 */

public class PldpPresenter implements PldpContract.Presenter {
    PldpContract.View view;
    DataAPI api;
    public PldpPresenter(PldpContract.View view) {
        this.view = view;
        api = new DataAPI();
    }

    @Override
    public void addPldp(PldpDTO pldp) {
        api.addPldp(pldp, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onPldpUploaded(key);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCurrentUser(String email) {
        api.getUserByEmail(email, new DataAPI.EmailQueryListener() {
            @Override
            public void onUserFoundByEmail(UserDTO user) {
                view.onUserFound(user);
            }

            @Override
            public void onUserNotFoundByEmail() {
                view.onError("User Not Found");
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }
}
