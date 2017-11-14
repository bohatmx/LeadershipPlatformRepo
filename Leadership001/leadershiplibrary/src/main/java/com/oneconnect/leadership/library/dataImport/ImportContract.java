package com.oneconnect.leadership.library.dataImport;

import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.UserDTO;

import java.io.File;
import java.util.List;

/**
 * Created by nkululekochrisskosana on 2017/11/13.
 */

public class ImportContract {

    public interface Presenter {

        void getImportFiles();
        void parseDailyThoughtsFile(File file);
        void parseUsersFile(File file);
        //
        void addDailyThoughts(List<DailyThoughtDTO> dailyThoughts);
        void addUsers(List<UserDTO> users);
    }

    public interface View {
        void onImportFiles(List<File> files);
        void onParseDailyThoughtsFile(List<DailyThoughtDTO> dailyThoughts);
        void onParseUsersFile(List<UserDTO> users);

        void onUsersAdded(List<UserDTO> users);
        void onDailyThoughtsAdded(List<DailyThoughtDTO> dailyThoughts);

        void onError(String message);
    }
}
