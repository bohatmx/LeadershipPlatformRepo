package com.oneconnect.leadership.library.activities;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.CountryDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

/**
 * Created by Nkululeko on 2017/04/18.
 */

public class SubscriberPresenter implements SubscriberContract.Presenter {
    private SubscriberContract.View view;
    private DataAPI dataAPI;
    private ListAPI listAPI;

    public SubscriberPresenter(SubscriberContract.View view) {
        this.view = view;
        dataAPI = new DataAPI();
        listAPI = new ListAPI();
    }

    @Override
    public void getCategories(String companyID) {
        listAPI.getCategories(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onCategories(bag.getCategories());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getCompanies(String companyID) {
        listAPI.getCompanies(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onCompanies(bag.getCompanies());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getDailyThoughts(String companyID) {
        listAPI.getDailyThoughts(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onDailyThoughts(bag.getDailyThoughts());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getDailyThoughtsByUser(String userID) {
        listAPI.getDailyThoughtsByUser(userID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onDailythoughtsByUser(bag.getDailyThoughts());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }
    @Override
    public void getAllCompanyDailyThoughts(String companyID) {
        listAPI.getAllCompanyDailyThoughts(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllCompanyDailyThoughts(bag.getDailyThoughts());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getDailyThoughtsRating(String dailyThoughtID) {
        listAPI.getDailyThoughtsRating(dailyThoughtID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onDailyThoughtRatings(bag.getRatings());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getWeeklyMessageRating(final String weeklyMessageID) {
        listAPI.getWeeklyMessageRating(weeklyMessageID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onWeeklyMessageRatings(bag.getRatings());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getWeeklyMasterClassRating(String weeklyMasterClassID) {
        listAPI.getWeeklyMasterClassRating(weeklyMasterClassID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onWeeklyMasterClassRatings(bag.getRatings());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getCategorisedWeeklyMessages(String categoryID) {

            listAPI.getCategorisedWeeklyMessages(categoryID, new ListAPI.DataListener() {
                @Override
                public void onResponse(ResponseBag bag) {
                    view.onWeeklyMessages(bag.getWeeklyMessages());
                }

                @Override
                public void onError(String messsage) {
                    view.onError(messsage);
                }
            });

    }

    @Override
    public void getCategorisedWeeklyMasterClasses(String categoryID) {
        listAPI.getCategorisedWeeklyMasterClasses(categoryID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onWeeklyMasterclasses(bag.getWeeklyMasterClasses());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getCurrentUser(String email) {
        dataAPI.getUserByEmail(email, new DataAPI.EmailQueryListener() {
            @Override
            public void onUserFoundByEmail(UserDTO user) {
                view.onUserFound(user);
            }

            @Override
            public void onUserNotFoundByEmail() {

            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCompanyProfile(String companyID) {
        dataAPI.getCompany(companyID, new DataAPI.CompanyQueryListener() {
            @Override
            public void onCompanyFound(CompanyDTO company) {
                view.onCompanyFound(company);
            }

            @Override
            public void onCompanyNotFound() {
                view.onError("Company not found");
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCategorisedCalenderEvents() {
        /*listAPI.getCategorisedCalenderEvents(categoryID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onCalenderEvents(bag.getCalenderEvents());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });*/
    }

    @Override
    public void getDailyThoughtsByUserType(String userType) {
        listAPI.getDailyThoughtsByUserType(userType, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllDailyThoughts(bag.getDailyThoughts());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllStaff() {
        listAPI.getAllStaff(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onUsers(bag.getUsers());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllLeaders() {
       /* listAPI.getAllLeaders(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllLeaders(bag.getLeaders());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });*/
    }

    @Override
    public void getAllSubscribers() {

    }

    @Override
    public void getAllSubscriptions() {
        listAPI.getAllSubscriptions(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllSubscriptions(bag.getSubscriptions());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllDailyThoughts() {
        listAPI.getAllDailyThoughts(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllDailyThoughts(bag.getDailyThoughts());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllCategories() {
        listAPI.getAllCategories(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllCategories(bag.getCategories());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllNewsArticle() {
        listAPI.getAllNewsArticle(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllNewsArticle(bag.getNews());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }
    @Override
    public void getAllWeeklyMessages() {
        listAPI.getAllWeeklyMessages(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllWeeklyMessages(bag.getWeeklyMessages());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllWeeklyMasterClasses() {
        listAPI.getAllWeeklyMasterClasses(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onWeeklyMasterclasses(bag.getWeeklyMasterClasses());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllVideos() {
        listAPI.getAllVideos(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onVideos(bag.getVideos());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllPodcasts() {
        listAPI.getAllPodcasts(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllPodcasts(bag.getPodcasts());
              //  view.onPodcasts(bag.getPodcasts());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllEBooks() {
        listAPI.getAllEBooks(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllEBooks(bag.geteBooks());
              //  view.onEbooks(bag.geteBooks());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllPhotos() {
        listAPI.getAllPhotos(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllPhotos(bag.getPhotos());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllCalendarEvents() {
        listAPI.getAllCalendarEvents(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllCalendarEvents(bag.getCalendarEvents());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getAllRatings() {
        listAPI.getAllRatings(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllRatings(bag.getRatings());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }


    @Override
    public void getEbooks(String companyID) {
        listAPI.getEBooks(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onEbooks(bag.geteBooks());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getPayments(String companyID) {
        listAPI.getPayments(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onPayments(bag.getPayments());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getPodcasts(String companyID) {
        listAPI.getPodcasts(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onPodcasts(bag.getPodcasts());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getPhotos(String companyID) {
        listAPI.getPhotos(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onPhotos(bag.getPhotos());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getPrices(String companyID) {
        listAPI.getPrices(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onPrices(bag.getPrices());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getUsers(String companyID) {
        listAPI.getCompanyStaff(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onUsers(bag.getUsers());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getNews(String companyID) {
        listAPI.getNews(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onNews(bag.getNews());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getSubscriptions(String companyID) {
        listAPI.getSubscriptions(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onSubscriptions(bag.getSubscriptions());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getVideos(String companyID) {
        listAPI.getCompanyVideos(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onVideos(bag.getVideos());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getWeeklyMasterclasses(String companyID) {
        listAPI.getWeeklyMasterclasses(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onWeeklyMasterclasses(bag.getWeeklyMasterClasses());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getWeeklyMessages(String companyID) {
        listAPI.getWeeklyMessages(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onWeeklyMessages(bag.getWeeklyMessages());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }

    @Override
    public void getDevices(String companyID) {
        listAPI.getDevices(companyID, new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onDevices(bag.getDevices());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }
}
