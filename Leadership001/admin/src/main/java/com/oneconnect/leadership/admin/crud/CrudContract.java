package com.oneconnect.leadership.admin.crud;

import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
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

import java.util.List;

/**
 * Created by aubreymalabie on 3/17/17.
 */

public class CrudContract {
    public interface Presenter {
        void addEntity(BaseDTO entity);
        void updateEntity(BaseDTO entity);
        void createUser(UserDTO user);

        void getCategories(String companyID);
        void getCompanies(String companyID);
        void getDailyThoughts(String companyID);
        void getEbooks(String companyID);
        void getPayments(String companyID);
        void getPodcasts(String companyID);
        void getPhotos(String companyID);
        void getPrices(String companyID);
        void getUsers(String companyID);
        void getNews(String companyID);
        void getSubscriptions(String companyID);
        void getVideos(String companyID);
        void getWeeklyMasterclasses(String companyID);
        void getWeeklyMessages(String companyID);
        void getDevices(String companyID);
    }

    public interface View {
        void onEntityAdded(String key);
        void onEntityUpdated();
        void onUserCreated(UserDTO user);

        void onCategories(List<CategoryDTO> list);
        void onCompanies(List<CompanyDTO> list);
        void onDailyThoughts(List<DailyThoughtDTO> list);
        void onEbooks(List<EBookDTO> list);
        void onPayments(List<PaymentDTO> list);
        void onPodcasts(List<PodcastDTO> list);
        void onPhotos(List<PhotoDTO> list);
        void onPrices(List<PriceDTO> list);
        void onUsers(List<UserDTO> list);
        void onNews(List<NewsDTO> list);
        void onSubscriptions(List<SubscriptionDTO> list);
        void onVideos(List<VideoDTO> list);
        void onWeeklyMasterclasses(List<WeeklyMasterClassDTO> list);
        void onWeeklyMessages(List<WeeklyMessageDTO> list);
        void onDevices(List<DeviceDTO> companyID);
        void onError(String message);
    }
}
