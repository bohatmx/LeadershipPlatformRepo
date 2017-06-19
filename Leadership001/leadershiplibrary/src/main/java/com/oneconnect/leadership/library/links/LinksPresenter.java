package com.oneconnect.leadership.library.links;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.data.UrlDTO;

/**
 * Created by aubreymalabie on 3/26/17.
 */

public class LinksPresenter implements LinksContract.Presenter {
    LinksContract.View view;
    DataAPI api;

    public LinksPresenter(LinksContract.View view) {
        this.view = view;
        api = new DataAPI();
    }

    @Override
    public void addLink(UrlDTO url, String entityID, int entityType) {
        api.addUrl(url, entityID, entityType, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onLinkAdded(key);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }
}
