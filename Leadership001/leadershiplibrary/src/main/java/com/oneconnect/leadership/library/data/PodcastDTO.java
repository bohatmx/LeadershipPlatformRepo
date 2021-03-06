package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class PodcastDTO  extends BaseDTO implements  Serializable, Comparable<PodcastDTO>{

    public static final int
            RECORDING = 1,
            PODCAST = 2;

    public static final String
            DESC_VOICE_RECORDING = "Recording",
            DESC_PODCAST = "Podcast";

    private String podcastID, transcript, stringDate, url, filePath, storageName, caption, podcastDescription;
    private Long date;
    private Double length;
    private String  stringDateUpdated, html, thumbnailUrl;
    private boolean active;
    private Long dateUpdated, podcastSize;
    private String weeklyMasterClassID, weeklyMessageID, dailyThoughtID,
            eBookID, subjectTitle;
    private int podcastType;
    private HashMap<String,String> urlLinks;
    private HashMap<String, PhotoDTO> photos;
    private HashMap<String, VideoDTO> videos;
    private HashMap<String,EBookDTO> ebooks;
    private HashMap<String,UrlDTO> urls;
    //
    //
    private EBookDTO eBook;
    private VideoDTO video;
    private UrlDTO urlDTO;
    private UserDTO user;
    private DailyThoughtDTO dailyThought;

    public String getPodcastDescription() {
        return podcastDescription;
    }

    public void setPodcastDescription(String podcastDescription) {
        this.podcastDescription = podcastDescription;
    }

    public int getPodcastType() {
        return podcastType;
    }

    public void setPodcastType(int podcastType) {
        this.podcastType = podcastType;
    }

    private HashMap<String,CalendarEventDTO> calendarEvents;
    private HashMap<String,RatingDTO> ratings;

    public HashMap<String, RatingDTO> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, RatingDTO> ratings) {
        this.ratings = ratings;
    }


    public HashMap<String, CalendarEventDTO> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(HashMap<String, CalendarEventDTO> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public HashMap<String, UrlDTO> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, UrlDTO> urls) {
        this.urls = urls;
    }

    public HashMap<String, EBookDTO> getEbooks() {
        return ebooks;
    }

    public void setEbooks(HashMap<String, EBookDTO> ebooks) {
        this.ebooks = ebooks;
    }


    public HashMap<String, PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(HashMap<String, PhotoDTO> photos) {
        this.photos = photos;
    }

    public HashMap<String, VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(HashMap<String, VideoDTO> videos) {
        this.videos = videos;
    }


    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
    public HashMap<String, String> getUrlLinks() {
        return urlLinks;
    }

    public void setUrlLinks(HashMap<String, String> urlLinks) {
        this.urlLinks = urlLinks;
    }

    public String getWeeklyMasterClassID() {
        return weeklyMasterClassID;
    }

    public void setWeeklyMasterClassID(String weeklyMasterClassID) {
        this.weeklyMasterClassID = weeklyMasterClassID;
    }

    public String getWeeklyMessageID() {
        return weeklyMessageID;
    }

    public void setWeeklyMessageID(String weeklyMessageID) {
        this.weeklyMessageID = weeklyMessageID;
    }

    public String getDailyThoughtID() {
        return dailyThoughtID;
    }

    public void setDailyThoughtID(String dailyThoughtID) {
        this.dailyThoughtID = dailyThoughtID;
    }

    public String geteBookID() {
        return eBookID;
    }

    public void seteBookID(String eBookID) {
        this.eBookID = eBookID;
    }

    public String getStringDateUpdated() {
        return stringDateUpdated;
    }

    public void setStringDateUpdated(String stringDateUpdated) {
        this.stringDateUpdated = stringDateUpdated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getPodcastID() {
        return podcastID;
    }

    public void setPodcastID(String podcastID) {
        this.podcastID = podcastID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    @Exclude
    public int compareTo(@NonNull PodcastDTO d) {
        if (date > d.date) {
            return -1;
        }
        if (date < d.date) {
            return 1;
        }
        return 0;

    }

    @Override
    public void setJournalUserID(String userID) {

    }

    @Override
    public void setJournalUserName(String userName) {

    }

    public String getStringDateScheduled() {
        return stringDateScheduled;
    }

    public void setStringDateScheduled(String stringDateScheduled) {
        this.stringDateScheduled = stringDateScheduled;
    }

    public Long getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(Long dateScheduled) {
        stringDateScheduled = sdf.format(new Date(dateScheduled));
        this.dateScheduled = dateScheduled;
    }

    public String getStringDateRegistered() {
        return stringDateRegistered;
    }

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public Long getPodcastSize() {
        return podcastSize;
    }

    public void setPodcastSize(Long podcastSize) {
        this.podcastSize = podcastSize;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public EBookDTO geteBook() {
        return eBook;
    }

    public void seteBook(EBookDTO eBook) {
        this.eBook = eBook;
    }

    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }

    public UrlDTO getUrlDTO() {
        return urlDTO;
    }

    public void setUrlDTO(UrlDTO urlDTO) {
        this.urlDTO = urlDTO;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public DailyThoughtDTO getDailyThought() {
        return dailyThought;
    }

    public void setDailyThought(DailyThoughtDTO dailyThought) {
        this.dailyThought = dailyThought;
    }
}
