package com.oneconnect.leadership.library.activities;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.crud.CrudContract;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by Nkululeko on 2017/09/14.
 */

public class CompanyPresenter implements CompanyContract.Presenter {
    private CompanyContract.View view;
    private DataAPI dataAPI;
    private ListAPI listAPI;

    public CompanyPresenter(CompanyContract.View view) {
        this.view = view;
        dataAPI = new DataAPI();
        listAPI = new ListAPI();
    }

    @Override
    public void createCompany(CompanyDTO company) {
        dataAPI.addCompany(company, new DataAPI.CreateCompanyListener() {
            @Override
            public void onCompanyCreated(CompanyDTO company) {
                view.onCompanyCreated(company);
            }

            @Override
            public void onCompanyAlreadyExists(CompanyDTO company) {
                view.onCompanyCreated(company);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCompany(String companyID) {
        dataAPI.getCompany(companyID, new DataAPI.CompanyQueryListener() {
            @Override
            public void onCompanyFound(CompanyDTO company) {
                view.onCompanyFound(company);
            }

            @Override
            public void onCompanyNotFound() {
                view.onCompanyNotFound();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void updateCompany(final CompanyDTO company) {
        dataAPI.updateCompany(company, new DataAPI.UpdateListener() {
            @Override
            public void onSuccess() {
                view.onCompanyUpdated(company);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getUser(String email) {
        dataAPI.getUserByEmail(email, new DataAPI.EmailQueryListener() {
            @Override
            public void onUserFoundByEmail(UserDTO user) {
                view.onUserFound(user);
            }

            @Override
            public void onUserNotFoundByEmail() {

            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }
}
