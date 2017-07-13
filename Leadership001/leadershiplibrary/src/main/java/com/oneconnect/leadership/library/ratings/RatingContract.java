package com.oneconnect.leadership.library.ratings;

import com.oneconnect.leadership.library.data.RatingDTO;

/**
 * Created by Kurisani on 2017/07/12.
 */

public class RatingContract {
    public interface Presenter {
        void uploadRating(RatingDTO rating);
    }
    public interface View {
        void onRatingUploaded(String key);
        void onError(String message);
    }
}
