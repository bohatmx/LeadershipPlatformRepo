package com.oneconnect.leadership.library.data;

import android.os.Parcel;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aubreymalabie on 3/19/17.
 */

public abstract class BaseDTO /*extends ExpandableGroup<PhotoDTO>*/ implements Serializable{
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
    public String stringDateRegistered, stringDateScheduled,
            journalUserID, journalUserName, title,subtitle;
    public Long dateRegistered, dateScheduled;
    public String companyID, companyName;


    public BaseDTO() {
        dateRegistered = new Date().getTime();
        stringDateRegistered = sdf.format(new Date());
    }


    public abstract void setJournalUserID(String userID);
    public abstract void setJournalUserName(String userName);

}
