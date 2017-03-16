package com.oneconnect.leadership.library.data;

import java.util.List;

/**
 * Created by aubreymalabie on 2/12/17.
 */

public class ResponseBag {

    private int statusCode;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private List<CategoryDTO> categories;
    private List<DailyThoughtDTO> dailyThoughts;
    private List<EBookDTO> eBooks;
    private List<PaymentDTO> payments;
    private List<PhotoDTO> photos;
    private List<PodcastDTO> podcasts;
    private List<PriceDTO> prices;
    private List<UserDTO> subscribers;
    private List<VideoDTO> videos;
    private List<WeeklyMasterClassDTO> weeklyMasterClasses;
    private List<WeeklyMessageDTO> weeklyMessages;
    private List<UserDTO> users;


    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }

    public List<DailyThoughtDTO> getDailyThoughts() {
        return dailyThoughts;
    }

    public void setDailyThoughts(List<DailyThoughtDTO> dailyThoughts) {
        this.dailyThoughts = dailyThoughts;
    }

    public List<EBookDTO> geteBooks() {
        return eBooks;
    }

    public void seteBooks(List<EBookDTO> eBooks) {
        this.eBooks = eBooks;
    }

    public List<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
    }

    public List<PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoDTO> photos) {
        this.photos = photos;
    }

    public List<PodcastDTO> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<PodcastDTO> podcasts) {
        this.podcasts = podcasts;
    }

    public List<PriceDTO> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceDTO> prices) {
        this.prices = prices;
    }

    public List<UserDTO> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<UserDTO> subscribers) {
        this.subscribers = subscribers;
    }

    public List<VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDTO> videos) {
        this.videos = videos;
    }

    public List<WeeklyMasterClassDTO> getWeeklyMasterClasses() {
        return weeklyMasterClasses;
    }

    public void setWeeklyMasterClasses(List<WeeklyMasterClassDTO> weeklyMasterClasses) {
        this.weeklyMasterClasses = weeklyMasterClasses;
    }

    public List<WeeklyMessageDTO> getWeeklyMessages() {
        return weeklyMessages;
    }

    public void setWeeklyMessages(List<WeeklyMessageDTO> weeklyMessages) {
        this.weeklyMessages = weeklyMessages;
    }
}
