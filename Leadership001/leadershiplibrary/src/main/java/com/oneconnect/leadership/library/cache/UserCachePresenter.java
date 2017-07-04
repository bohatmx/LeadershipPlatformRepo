package com.oneconnect.leadership.library.cache;

import android.content.Context;

import com.oneconnect.leadership.library.data.UserDTO;

import java.util.List;

/**
 * Created by aubreymalabie on 3/19/17.
 */

public class UserCachePresenter implements UserCacheContract.Presenter {
    private UserCacheContract.View view;
    private Context ctx;

    public UserCachePresenter(UserCacheContract.View view, Context ctx) {
        this.ctx = ctx;
        this.view = view;
    }

    @Override
    public void getUsers() {
         UserCache.getUsers(ctx, new UserCache.ReadListener() {
             @Override
             public void onDataRead(List<UserDTO> users) {
                 view.onCachedUsers(users);
             }

             @Override
             public void onError(String message) {
                  view.onError(message);
             }
         });
    }
}
