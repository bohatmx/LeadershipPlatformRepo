package com.oneconnect.leadership.library.util;

import android.os.Build;

import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class Util {


    public static FCMUserDTO createFCMUser(UserDTO user, String token) {
        FCMUserDTO u = new FCMUserDTO();
        u.setFcmID(user.getUserID()+"@"+ Build.SERIAL);
        u.setDate(user.getDateRegistered());
        u.setUserID(user.getUserID());
        u.setName(user.getFullName());
        u.setToken(token);
        u.setAndroidVersion(Build.VERSION.RELEASE);
        u.setManufacturer(Build.MANUFACTURER);
        u.setDeviceModel(Build.MODEL);
        u.setSerialNumber(Build.SERIAL);
        u.setCompanyID(user.getCompanyID());
        u.setCompanyName(user.getCompanyName());
        return u;
    }

}
