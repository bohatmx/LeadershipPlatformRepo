package com.oneconnect.leadership.admin.crud;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.CountryDTO;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

/**
 * Created by aubreymalabie on 3/19/17.
 */

public class SheetPresenter implements SheetContract.Presenter {
    SheetContract.View view;
    DataAPI api;

    public SheetPresenter(SheetContract.View view) {
        this.view = view;
        api = new DataAPI();
    }
    @Override
    public void addEntity(BaseDTO entity) {
        if (entity instanceof CategoryDTO) {
            CategoryDTO c = (CategoryDTO)entity;
            api.addCategory(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof CompanyDTO) {
            CompanyDTO c = (CompanyDTO)entity;
            api.addCompany(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof CountryDTO) {
            CountryDTO c = (CountryDTO)entity;
            api.addCountry(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof DeviceDTO) {
            DeviceDTO c = (DeviceDTO) entity;
            api.addDevice(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof DailyThoughtDTO) {
            final DailyThoughtDTO c = (DailyThoughtDTO)entity;
            api.addDailyThoughts(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    c.setDailyThoughtID(key);
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof EBookDTO) {
            EBookDTO c = (EBookDTO)entity;
            api.addEBooks(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof NewsDTO) {
            NewsDTO c = (NewsDTO)entity;
            api.addNews(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof PaymentDTO) {
            PaymentDTO c = (PaymentDTO) entity;
            api.addPayment(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof PhotoDTO) {
            PhotoDTO c = (PhotoDTO) entity;
            api.addPhoto(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof PodcastDTO) {
            PodcastDTO c = (PodcastDTO) entity;
            api.addPodcast(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof PriceDTO) {
            PriceDTO c = (PriceDTO) entity;
            api.addPrice(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof SubscriptionDTO) {
            SubscriptionDTO c = (SubscriptionDTO) entity;
            api.addSubscription(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof UserDTO) {
            UserDTO c = (UserDTO) entity;
            api.addUser(c, new DataAPI.AddUserListener() {
                @Override
                public void onUserAdded(UserDTO user) {
                    view.onEntityAdded(user.getUserID());
                }

                @Override
                public void onUserAlreadyExists(UserDTO user) {
                    view.onEntityAdded(user.getUserID());
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof VideoDTO) {
            VideoDTO c = (VideoDTO) entity;
            api.addVideo(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof WeeklyMasterClassDTO) {
            WeeklyMasterClassDTO c = (WeeklyMasterClassDTO) entity;
            api.addWeeklyMasterClass(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
        if (entity instanceof WeeklyMessageDTO) {
            WeeklyMessageDTO c = (WeeklyMessageDTO) entity;
            api.addWeeklyMessage(c, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    view.onEntityAdded(key);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
    }

    @Override
    public void updateEntity(BaseDTO entity) {
        if (entity instanceof UserDTO) {
            UserDTO u = (UserDTO)entity;
            api.updateUser(u, new DataAPI.UpdateListener() {
                @Override
                public void onSuccess() {
                    view.onEntityUpdated();
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
        }
    }

    @Override
    public void deleteEntity(BaseDTO entity) {

    }
}
