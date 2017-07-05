package com.oneconnect.leadership.library.fcm;

import com.ocg.backend.endpointAPI.model.EmailRecordDTO;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.ocg.backend.endpointAPI.model.FCMessageDTO;
import com.ocg.backend.endpointAPI.model.PayLoad;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class EndpointPresenter implements EndpointContract.Presenter {
    EndpointContract.View view;

    public EndpointPresenter(EndpointContract.View view) {
        this.view = view;
    }

    @Override
    public void saveUser(FCMUserDTO user) {
        EndpointUtil.saveUser(user, new EndpointUtil.FCMListener() {
            @Override
            public void onResponse(FCMResponseDTO response) {
                view.onFCMUserSaved(response);
            }

            @Override
            public void onError(String message) {
                 view.onError(message);
            }
        });
    }

    @Override
    public void sendMessage(FCMessageDTO message) {
         EndpointUtil.sendMessage(message, new EndpointUtil.FCMListener() {
             @Override
             public void onResponse(FCMResponseDTO response) {
                 view.onMessageSent(response);
             }

             @Override
             public void onError(String message) {
                 view.onError(message);
             }
         });
    }

    @Override
    public void sendEmail(EmailRecordDTO emailRecord) {
         EndpointUtil.sendEmail(emailRecord, new EndpointUtil.EmailListener() {
             @Override
             public void onResponse(EmailResponseDTO response) {
                 view.onEmailSent(response);
             }

             @Override
             public void onError(String message) {

             }
         });
    }

    @Override
    public void sendLeadersMessage(String companyID, PayLoad payLoad) {
        EndpointUtil.sendLeadersMessage(companyID, payLoad, new EndpointUtil.FCMListener() {
            @Override
            public void onResponse(FCMResponseDTO response) {
                view.onMessageSent(response);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void sendWeeklyMasterclassMessage(String companyID, PayLoad payLoad) {
        EndpointUtil.sendWeeklyMasterclassMessage(companyID, payLoad, new EndpointUtil.FCMListener() {
            @Override
            public void onResponse(FCMResponseDTO response) {
                view.onMessageSent(response);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void sendDailyThought(String companyID, PayLoad payLoad) {
        EndpointUtil.sendDailyThought(companyID, payLoad, new EndpointUtil.FCMListener() {
            @Override
            public void onResponse(FCMResponseDTO response) {
                view.onMessageSent(response);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void sendSubscriberMessage(String companyID, PayLoad payLoad) {
        EndpointUtil.sendSubscriberMessage(companyID, payLoad, new EndpointUtil.FCMListener() {
            @Override
            public void onResponse(FCMResponseDTO response) {
                view.onMessageSent(response);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void sendCompanyMessage(String companyID, PayLoad payLoad) {
        EndpointUtil.sendCompanyMessage(companyID, payLoad, new EndpointUtil.FCMListener() {
            @Override
            public void onResponse(FCMResponseDTO response) {
                view.onMessageSent(response);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }
}
