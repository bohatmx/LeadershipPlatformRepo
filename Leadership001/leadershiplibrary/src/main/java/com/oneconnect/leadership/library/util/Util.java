package com.oneconnect.leadership.library.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.firebase.crash.FirebaseCrash;
import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.oneconnect.leadership.library.data.UserDTO;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class Util {

    public static Date getDateAtMidnite(Date selectedDate) {

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(selectedDate);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  cal.getTime();

    }

    public static void getFileFromBitmap(Bitmap bitmap, File file) {

        OutputStream os;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("Util", "Error writing bitmap", e);
            FirebaseCrash.report(new Exception("Bitmap to file failed: "));
        }
    }

    public static FCMUserDTO createFCMUser(UserDTO user,
                                           String token) {
        FCMUserDTO u = new FCMUserDTO();
        u.setFcmID(user.getUserID().concat("@").concat(Build.SERIAL)
        .concat("*") + user.getUserType());
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

    public static Bitmap rotateBitmap(Bitmap bm) throws IOException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        // Return result
        return rotatedBitmap;
    }

    public static void writeLocationToExif(String filePath, double latitude, double longitude) {
        try {
            ExifInterface ef = new ExifInterface(filePath);
            ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE, decimalToDMS(latitude));
            ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, decimalToDMS(longitude));
            if (latitude > 0)
                ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            else
                ef.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            if (longitude > 0)
                ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            else
                ef.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            ef.saveAttributes();
//            Log.e(LOG, "### Exif attributes written to " + filePath);
        } catch (IOException e) {
            Log.e("Util", "could not write exif data in image", e);
        }
    }
    private static String decimalToDMS(double coord) {
        coord = coord > 0 ? coord : -coord;  // -105.9876543 -> 105.9876543
        String sOut = Integer.toString((int) coord) + "/1,";   // 105/1,
        coord = (coord % 1) * 60;         // .987654321 * 60 = 59.259258
        sOut = sOut + Integer.toString((int) coord) + "/1,";   // 105/1,59/1,
        coord = (coord % 1) * 60000;             // .259258 * 60000 = 15555
        sOut = sOut + Integer.toString((int) coord) + "/1000";   // 105/1,59/1,15555/1000
        // Log.i(LOG, "decimalToDMS coord: " + coord + " converted to: " + sOut);
        return sOut;
    }

    private static List<File> getFilesOnSD(String extension) {
        File extDir = Environment.getExternalStorageDirectory();
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return new ArrayList<File>();
        }
        if (!state.equalsIgnoreCase(Environment.MEDIA_UNKNOWN)) {
            return new ArrayList<File>();
        }

        List<File> fileList = new ArrayList<File>();

        @SuppressWarnings("unchecked")
        Iterator<File> iter = FileUtils.iterateFiles(extDir, new String[]{
                extension}, true);

        while (iter.hasNext()) {
            File file = iter.next();
            if (file.getName().startsWith("._")) {
                continue;
            }
            fileList.add(0, file);
            System.out.println("### Import File: " + file.getAbsolutePath());
        }

        Log.i(TAG, "### SD Import Files in list : " + fileList.size());
        return fileList;
    }

    public static List<File> getFiles(String extension) {
        System.out.println(".............getImportFiles: .............");
        File extDir = Environment.getRootDirectory();
        String state = Environment.getExternalStorageState();

        // TODO check thru all state possibilities
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return new ArrayList<File>();
        }

        List<File> fileList = getFilesOnSD(extension);
        @SuppressWarnings("unchecked")
        Iterator<File> iter = FileUtils.iterateFiles(extDir, new String[]{
                extension}, true);

        while (iter.hasNext()) {
            File file = iter.next();
            if (file.getName().startsWith("._")) {
                continue;
            }
            fileList.add(file);
            System.out.println("### disk Import File: " + file.getAbsolutePath());
        }

        Log.i(TAG, "### Import Files in list : " + fileList.size());
        return fileList;
    }
    private static final String TAG = Util.class.getSimpleName();

    //
    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public interface UtilAnimationListener {
        public void onAnimationEnded();
    }


    public static void flashOnce(View view, long duration, final UtilAnimationListener listener) {
        try {
            ObjectAnimator an = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
            an.setRepeatMode(ObjectAnimator.REVERSE);
            an.setDuration(duration);
            an.setInterpolator(new AccelerateDecelerateInterpolator());
            an.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (listener != null)
                        listener.onAnimationEnded();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            an.start();
        } catch (Exception e) {
            if (listener != null) {
                listener.onAnimationEnded();
            }
        }

    }

    static public long getMiliseconds(String date){
        long milliseconds = (long) 0.00;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMMM yyyy");
            Date date1 = dateFormat.parse(date);
            milliseconds = date1.getTime();
        }catch (ParseException pe){
            Log.d(String.valueOf(pe),"checkkkkkkkkk");
        }

        return milliseconds;
    }

    static public String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd-MM-yyyy HH:mm";

        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd-MM-yyyy HH:mm", smsTime).toString();
        }
    }

}
