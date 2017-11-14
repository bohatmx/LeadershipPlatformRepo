package com.oneconnect.leadership.library.dataImport;

import android.content.Context;
import android.util.Log;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.ImportAPI;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.UserDTO;

import java.io.File;
import java.util.List;

/**
 * Created by nkululekochrisskosana on 2017/11/13.
 */

public class ImportPresenter implements ImportContract.Presenter {
    ImportContract.View view;
    ImportAPI importAPI;
    DataAPI dataAPI;
    ListAPI listAPI;
    Context ctx;
    public static final String TAG = ImportPresenter.class.getSimpleName();

    public ImportPresenter(ImportContract.View view, Context ctx) {
        this.view = view;
        this.ctx = ctx;
        importAPI = new ImportAPI();
        dataAPI = new DataAPI();
        listAPI = new ListAPI();
    }

    @Override
    public void getImportFiles() {
        List<File> files = importAPI.getImportFiles();
        view.onImportFiles(files);
    }

    @Override
    public void parseDailyThoughtsFile(File file) {
        try {
            view.onParseDailyThoughtsFile(importAPI.parseDailyThoughtsFile(file));
        } catch (Exception e) {
            Log.e(TAG, "parseDailyThoughtsFile: ", e);
            view.onError("Unable to parse daily thoughts");
        }
    }

    @Override
    public void parseUsersFile(File file) {
       /* try {
            view.onParseUsersFile(importAPI.parseUsersFile(file));
        } catch (Exception e) {
            Log.e(TAG, "parseUsersFile: ", e);
            view.onError("Unable to parse users");
        }*/
    }

    @Override
    public void addDailyThoughts(List<DailyThoughtDTO> dailyThoughts) {

    }

    @Override
    public void addUsers(List<UserDTO> users) {

    }
}
