package com.oneconnect.leadership.library.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aubreymalabie on 2/12/17.
 */

public class ResponseBag implements Serializable {

    private int statusCode, type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private List<CategoryDTO> categories;
    private List<DailyThoughtDTO> dailyThoughts;
    private List<EBookDTO> eBooks;
    private List<PaymentDTO> payments;
    private List<PhotoDTO> photos;
    private List<PodcastDTO> podcasts;
    private List<PriceDTO> prices;
    private List<VideoDTO> videos;
    private List<WeeklyMasterClassDTO> weeklyMasterClasses;
    private List<WeeklyMessageDTO> weeklyMessages;
    private List<UserDTO> users;
    private List<NewsDTO> news;
    private List<SubscriptionDTO> subscriptions;
    private List<CompanyDTO> companies;
    private List<CountryDTO> countries;
    private List<DeviceDTO> devices;
    private List<UrlDTO> urls;
    private List<ThumbnailDTO> thumbnails;
    private List<CalendarEventDTO> calendarEvents;
    private List<RatingDTO> ratings;
    private List<SubscriptionTypeDTO> subscriptionTypes;

    public static final int
            CATEGORIES = 1,
            DAILY_THOUGHTS = 2,
            EBOOKS = 3,
            PAYMENTS = 4,
            PHOTOS = 5,
            PODCASTS = 6,
            VIDEOS = 7,
            WEEKLY_MASTERCLASS = 8,
            WEEKLY_MESSAGE = 9,
            USERS = 10,
            NEWS = 11,
            SUBSCRIPTIONS = 12,
            COMPANIES = 13,
            DEVICES = 14,
            COUNTRIES = 15,
            PRICES = 16,
            CALENDAR_EVENTS = 17,
            URLS = 18,
            THUMBNAILS = 19,
            RATINGS = 20,
            SUBSCRIPTION_TYPES = 21;

    public static final String
            DESC_CATEGORIES = "Categories",
            DESC_DAILY_THOUGHTS = "Daily Thoughts",
            DESC_EBOOKS = "EBooks",
            DESC_PAYMENTS = "Payments",
            DESC_PHOTOS = "Photos",
            DESC_PODCASTS = "Podcasts",
            DESC_VIDEOS = "Videos",
            DESC_WEEKLY_MASTERCLASS = "Weekly MasterClass",
            DESC_WEEKLY_MESSAGE = "Weekly Message",
            DESC_USERS = "Users & Subscribers",
            DESC_NEWS = "News Articles",
            DESC_SUBSCRIPTIONS = "Subscriptions",
            DESC_SUBSCRIPTION_TYPES = "SubscriptionTypes",
            DESC_COMPANY = "Companies",
            DESC_DEVICE = "Devices",
            DESC_COUNTRY = "Countries",
            DESC_PRICE = "Prices",
            DESC_CALENDAR_EVENTS = "calendarEvents",
            DESC_URLS = "urls",
            DESC_RATINGS = "ratings",
            DESC_THUMBNAILS = "thumbnails";

    public List<SubscriptionTypeDTO> getSubscriptionTypes() {
        return subscriptionTypes;
    }

    public void setSubscriptionTypes(List<SubscriptionTypeDTO> subscriptionTypes) {
        this.subscriptionTypes = subscriptionTypes;
    }

    public List<RatingDTO> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingDTO> ratings) {
        this.ratings = ratings;
    }

    public List<ThumbnailDTO> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(List<ThumbnailDTO> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<CalendarEventDTO> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(List<CalendarEventDTO> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public List<UrlDTO> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlDTO> urls) {
        this.urls = urls;
    }

    public List<DeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceDTO> devices) {
        this.devices = devices;
    }

    public List<CompanyDTO> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyDTO> companies) {
        this.companies = companies;
    }

    public List<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDTO> countries) {
        this.countries = countries;
    }

    public List<SubscriptionDTO> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionDTO> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<NewsDTO> getNews() {
        return news;
    }

    public void setNews(List<NewsDTO> news) {
        this.news = news;
    }

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
