package com.oneconnect.leadership.library.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Date;

import static com.oneconnect.leadership.library.lists.BasicEntityAdapter.sdf;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class PhotoDTO extends BaseDTO implements  Serializable, Comparable<PhotoDTO>/*, Parcelable*/ {

    @Exclude
    private String filePath;
    private String photoID, caption, url;
    private int height, width;
    private long bytes;
    private long date, imageSize, dateUploaded;
    private String userID, userName, stringDateUploaded, stringDate;
    //
    private PodcastDTO podcast;
    private UrlDTO urlDTO;
    private VideoDTO video;
    private NewsDTO news;

    private String weeklyMasterClassID, weeklyMessageID,
            dailyThoughtID, eBookID, podcastID, newsID;

    public PhotoDTO() {
        date = new Date().getTime();
        stringDate = sdf.format(new Date());
    }

    protected PhotoDTO(Parcel in) {
        filePath = in.readString();
        photoID = in.readString();
        caption = in.readString();
        url = in.readString();
        height = in.readInt();
        width = in.readInt();
        bytes = in.readLong();
        date = in.readLong();
        imageSize = in.readLong();
        dateUploaded = in.readLong();
        userID = in.readString();
        userName = in.readString();
        stringDateUploaded = in.readString();
        stringDate = in.readString();
        weeklyMasterClassID = in.readString();
        weeklyMessageID = in.readString();
        dailyThoughtID = in.readString();
        eBookID = in.readString();
        podcastID = in.readString();
    }

   /* public static final Creator<PhotoDTO> CREATOR = new Creator<PhotoDTO>() {
        @Override
        public PhotoDTO createFromParcel(Parcel in) {
            return new PhotoDTO(in);
        }

        @Override
        public PhotoDTO[] newArray(int size) {
            return new PhotoDTO[size];
        }
    };*/

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(long dateUploaded) {
        stringDateUploaded = sdf.format(new Date(dateUploaded));
        this.dateUploaded = dateUploaded;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getPodcastID() {
        return podcastID;
    }

    public void setPodcastID(String podcastID) {
        this.podcastID = podcastID;
    }

    public String getStringDateUploaded() {
        return stringDateUploaded;
    }

    public void setStringDateUploaded(String stringDateUploaded) {
        this.stringDateUploaded = stringDateUploaded;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    @Exclude
    public String getFilePath() {
        return filePath;
    }

    @Exclude
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getCaption() {
        return caption;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Exclude
    public int compareTo(@NonNull PhotoDTO d) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    ////
    /*@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
    }
*/
    public PodcastDTO getPodcast() {
        return podcast;
    }

    public void setPodcast(PodcastDTO podcast) {
        this.podcast = podcast;
    }

    public UrlDTO getUrlDTO() {
        return urlDTO;
    }

    public void setUrlDTO(UrlDTO urlDTO) {
        this.urlDTO = urlDTO;
    }

    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }

    public NewsDTO getNews() {
        return news;
    }

    public void setNews(NewsDTO news) {
        this.news = news;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }
}
