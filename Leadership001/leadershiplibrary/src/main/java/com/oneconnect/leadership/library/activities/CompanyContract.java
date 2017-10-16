package com.oneconnect.leadership.library.activities;

import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by Nkululeko on 2017/09/14.
 */

public class CompanyContract {

    public interface Presenter {
        void createCompany(CompanyDTO company);
        void getCompany(String companyID);
        void updateCompany(CompanyDTO company);
        void getUser(String email);

    }

    public interface View {
        void onCompanyFound(CompanyDTO company);
        void onError(String message);
        void onCompanyNotFound();
        void onCompanyUpdated(CompanyDTO company);
        void onUserFound(UserDTO user);
        void onCompanyCreated(CompanyDTO company);
    }
}
