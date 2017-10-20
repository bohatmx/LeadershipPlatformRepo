package com.oneconnect.leadership.library.fcm;


import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.ocg.backend.endpointAPI.model.EmailRecordDTO;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMessageDTO;
import com.ocg.backend.endpointAPI.model.PayLoad;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class EndpointContract {
    public interface Presenter {
    void saveUser(FCMUserDTO user);
        void sendMessage(FCMessageDTO message);
        void sendEmail(EmailRecordDTO emailRecord);
        void sendLeadersMessage(String companyID, PayLoad payLoad);
        void sendWeeklyMasterclassMessage(String companyID, PayLoad payLoad);
        void sendDailyThought(String companyID, PayLoad payLoad);
        void sendSubscriberMessage(String companyID, PayLoad payLoad);
        void sendCompanyMessage(String companyID, PayLoad payLoad);
    }
    public interface View {
        void onFCMUserSaved(FCMResponseDTO response);
        void onMessageSent(FCMResponseDTO response);
        void onEmailSent(EmailResponseDTO response);
        void onError(String message);
    }
}
