package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by aubreymalabie on 2/12/17.
 */

public class CountryDTO implements DTOEntity, Serializable, Comparable<CountryDTO>{
    private String countryID, countryName;

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public int compareTo(@NonNull CountryDTO c) {
        return this.countryName.compareTo(c.countryName);
    }

    @Override
    public String getTitleForAdapter() {
        return countryName;
    }

    @Override
    public String getTopText() {
        return null;
    }

    @Override
    public String getBottomTitle() {
        return null;
    }

    @Override
    public String getBottomText() {
        return null;
    }
}
