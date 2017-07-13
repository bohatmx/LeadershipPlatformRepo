package com.oneconnect.leadership.library.ratings;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.photo.PhotoUploadContract;

/**
 * Created by Kurisani on 2017/07/12.
 */

public class RatingPresenter implements RatingContract.Presenter {

    RatingContract.View view;
    DataAPI api;

    public RatingPresenter(RatingContract.View view) {
        this.view = view;
        api = new DataAPI();
    }

    @Override
    public void uploadRating(RatingDTO rating) {
       api.addRating(rating, new DataAPI.DataListener() {
           @Override
           public void onResponse(String key) {
               view.onRatingUploaded(key);
           }

           @Override
           public void onError(String message) {
               view.onError(message);
           }
       });
        /* api.addRatingToEntity(rating, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onRatingUploaded(key);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        }); */
    }
}