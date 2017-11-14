package com.oneconnect.leadership.library.api;

import android.os.Environment;
import android.util.Log;

import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.util.Constants;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by nkululekochrisskosana on 2017/11/13.
 */

public class ImportAPI {

    private List<File> getImportFilesOnSD() {
        System.out.println(".....................getImportFiles On SD: .........");
        File extDir = Environment.getExternalStorageDirectory();
        String state = Environment.getExternalStorageState();
        // TODO check thru all state possibilities
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return new ArrayList<File>();
        }

        List<File> fileList = new ArrayList<File>();

        @SuppressWarnings("unchecked")
        Iterator<File> iter = FileUtils.iterateFiles(extDir, new String[]{
                "csv"}, true);

        while (iter.hasNext()) {
            File file = iter.next();
            if (file.getName().startsWith("._")) {
                continue;
            }
            fileList.add(0, file);
            Log.d(TAG, "getImportFilesOnSD: ### disk Import File: " + file.getAbsolutePath());
        }

        Log.i(TAG, "### SD Import Files in list : " + fileList.size());
        return fileList;
    }

    public List<File> getImportFiles() {
        System.out.println(".............getImportFiles: .............");
        File extDir = Environment.getRootDirectory();
        String state = Environment.getExternalStorageState();

        // TODO check thru all state possibilities
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return new ArrayList<File>();
        }

        List<File> fileList = getImportFilesOnSD();
        @SuppressWarnings("unchecked")
        Iterator<File> iter = FileUtils.iterateFiles(extDir, new String[]{
                "csv"}, true);

        while (iter.hasNext()) {
            File file = iter.next();
            if (file.getName().startsWith("._")) {
                continue;
            }
            fileList.add(file);
            Log.d(TAG, "getImportFiles: ### disk Import File: " + file.getAbsolutePath());
        }

        Log.i(TAG, "### Import Files in list : " + fileList.size());
        return fileList;
    }

    private boolean isEmptyLine(String line) {
        Pattern patt = Pattern.compile(";");
        boolean OK = false;
        String[] result = patt.split(line);
        try {
            if (result.length == 0) {
                OK = true;
            }

        } catch (Exception e) {
            System.out.println("---- ERROR parse failed");
        }
        return OK;
    }

    /**
     * Parse csv file containing DailyThoughts
     * @param file
     * @return List<DailyThought>
     * @throws Exception
     */
    public List<DailyThoughtDTO> parseDailyThoughtsFile(File file) throws Exception {
        System.out.println("parseDailyThoughtsFile: ............" + file.getName());
        List<DailyThoughtDTO> list = new ArrayList<>();

        BufferedReader brReadMe = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF-8"));
        String strLine = brReadMe.readLine();
        while (strLine != null) {
            if (isEmptyLine(strLine)) {
                strLine = brReadMe.readLine();
                continue;
            }
            if (strLine.contains("REGISTRATION") || strLine.contains("YEAR")) {
                strLine = brReadMe.readLine();
                continue;
            }

            DailyThoughtDTO dailyThought = parseDailyThought(strLine);
            if (dailyThought != null) {
                list.add(dailyThought);
            }
            strLine = brReadMe.readLine();
        }

        brReadMe.close();
        Log.i(TAG, "####### vehicle list has been imported into app, found: "
                + list.size());
        return list;
    }

    /**
     * Create DailyThoughtDTO from registration and optional year
     * @param line
     * @return
     * @throws Exception
     */
    private DailyThoughtDTO parseDailyThought(String line) throws Exception {
        System.out.println("###### parseDailyThought: " + line );
        String[] result = line.split(",");
        DailyThoughtDTO dto = new DailyThoughtDTO();
        try {
            dto.setTitle(result[0]);
            dto.setSubtitle(result[1]);
           // dto.setVehicleReg(result[0]);
            dto.setStatus(Constants.APPROVED);
            /*if (result.length == 2) {
                String sYear = result[1];
                try {
                    Integer.parseInt(sYear);
                    dto.setYear(sYear);
                } catch (Exception e) {
                    dto.setYear("0");
                }
            } else {
                dto.setYear("0");
            }*/


        } catch (Exception e) {
            System.out.println("---- ERROR parseDailyThought  failed: " + line);
            return null;
        }

        Log.d(TAG,
                "Found thought: " + dto.getTitle() + " to import");
        return dto;
    }

    static final String TAG = ImportAPI.class.getSimpleName();

}
