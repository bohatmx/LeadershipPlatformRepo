package com.oneconnect.leadership.library.audio;

import com.oneconnect.leadership.library.data.PodcastDTO;

/**
 * Created by Nkululeko on 2017/04/11.
 */

public class PodcastUploadContract {
    public interface Presenter {
        void uploadPodcast(PodcastDTO podcast);
    }
    public interface View {
        void onPodcastUploaded(String key);
        void onProgress(long transferred, long size);
        void onError(String message);
    }
}
