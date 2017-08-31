package com.oneconnect.leadership.library.util;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.crud.CrudContract;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

/**
 * Created by Kurisani on 2017/08/30.
 */

public class DeletePresenter implements DeleteContract.Presenter {
    private DeleteContract.View view;
    private DataAPI dataAPI;
    private ListAPI listAPI;

    public DeletePresenter(DeleteContract.View view) {
        this.view = view;
        dataAPI = new DataAPI();
        listAPI = new ListAPI();
    }

    @Override
    public void deleteUser(final UserDTO user) {
        dataAPI.deleteUser(user, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onUserDeleted(user);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteDailyThought(final DailyThoughtDTO dailyThought) {
        dataAPI.deleteDailyThought(dailyThought, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onDailyThoughtDeleted(dailyThought);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteWeeklyMessage(final WeeklyMessageDTO weeklyMessage) {
        dataAPI.deleteWeeklyMessage(weeklyMessage, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onWeeklyMessageDeleted(weeklyMessage);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteWeeklyMasterClass(final WeeklyMasterClassDTO masterClass) {
            dataAPI.deleteWeeklyMasterClass(masterClass, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onWeeklyMasterClassDeleted(masterClass);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
    }

    @Override
    public void deleteVideo(final VideoDTO video) {
        dataAPI.deleteVideo(video, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onVideoDeleted(video);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deletePodcast(final PodcastDTO podcast) {
        dataAPI.deletePodcast(podcast, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onPodcastDeleted(podcast);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deleteNews(final NewsDTO news) {
        dataAPI.deleteNews(news, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onNewsDeleted(news);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void deletePhoto(final PhotoDTO photo) {
            dataAPI.deletePhoto(photo, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onPhotoDeleted(photo);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
    }

    @Override
    public void deleteEbook(final EBookDTO eBook) {
            dataAPI.deleteBook(eBook, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEbookDeleted(eBook);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
    }
}
