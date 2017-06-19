package com.oneconnect.leadership.library.links;

import com.oneconnect.leadership.library.data.UrlDTO;

/**
 * Created by aubreymalabie on 3/26/17.
 */

public class LinksContract {
    public interface Presenter {
        void addLink(UrlDTO url, String entityID, int entityType);
    }
    public interface View {
        void onLinkAdded(String key);
        void onError(String message);
    }
}
