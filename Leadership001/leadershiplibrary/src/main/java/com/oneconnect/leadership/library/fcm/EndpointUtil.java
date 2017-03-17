package com.oneconnect.leadership.library.fcm;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.ocg.backend.endpointAPI.EndpointAPI;
import com.ocg.backend.endpointAPI.model.EmailRecordDTO;
import com.ocg.backend.endpointAPI.model.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMResponseDTO;
import com.ocg.backend.endpointAPI.model.FCMUserDTO;
import com.ocg.backend.endpointAPI.model.FCMessageDTO;
import com.ocg.backend.endpointAPI.model.PayLoad;
import com.oneconnect.leadership.library.util.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

import static com.oneconnect.leadership.library.util.Constants.APPLICATION_NAME;
import static com.oneconnect.leadership.library.util.Constants.APP_ENGINE_ROOT_URL;


/**
 * Created by aubreymalabie on 11/9/16.
 */

public class EndpointUtil {
    public static final String TAG = EndpointUtil.class.getSimpleName();

    private static EndpointAPI endpointAPI = null;

    public static final int COMPANY = 1, SUBSCRIBERS = 2, LEADERS = 3, ADMINS = 4,
            DAILY_THOUGHT = 5, WEEKLY_MASTERCLASS = 6;

    public interface FCMListener {
        void onResponse(FCMResponseDTO response);

        void onError(String message);
    }
    public interface EmailListener {
        void onResponse(EmailResponseDTO response);

        void onError(String message);
    }

    public static void sendMessage(FCMessageDTO message, FCMListener fcmListener) {
        new SendMessageTask(message, fcmListener).execute();
    }

    public static void sendEmail(EmailRecordDTO emailRecord, EmailListener emailListener) {
        new SendEmailTask(emailRecord, emailListener).execute();
    }

    public static void saveUser(FCMUserDTO user, FCMListener fcmListener) {
        new SaveUserTask(user, fcmListener).execute();
    }

    public static void sendCompanyMessage(String companyID, PayLoad payLoad, FCMListener listener) {
        TopicMessageTask t = new TopicMessageTask(
                companyID, COMPANY, payLoad, listener);
        t.execute();
    }
    public static void sendSubscriberMessage(String companyID, PayLoad payLoad, FCMListener listener) {
        TopicMessageTask t = new TopicMessageTask(
                companyID, SUBSCRIBERS, payLoad, listener);
        t.execute();
    }
    public static void sendDailyThought(String companyID, PayLoad payLoad, FCMListener listener) {
        TopicMessageTask t = new TopicMessageTask(
                companyID, DAILY_THOUGHT, payLoad, listener);
        t.execute();
    }
    public static void sendWeeklyMasterclassMessage(String companyID, PayLoad payLoad, FCMListener listener) {
        TopicMessageTask t = new TopicMessageTask(
                companyID, WEEKLY_MASTERCLASS, payLoad, listener);
        t.execute();
    }
    public static void sendLeadersMessage(String companyID, PayLoad payLoad, FCMListener listener) {
        TopicMessageTask t = new TopicMessageTask(
                companyID, LEADERS, payLoad, listener);
        t.execute();
    }
    static class SaveUserTask extends AsyncTask<Void, Void, FCMResponseDTO> {

        FCMUserDTO user;
        FCMListener listener;

        public SaveUserTask(FCMUserDTO user, FCMListener listener) {
            this.user = user;
            this.listener = listener;
        }

        @Override
        protected FCMResponseDTO doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: ########### : ".concat(APP_ENGINE_ROOT_URL));
            if (endpointAPI == null) {  // Only do this once
                EndpointAPI.Builder builder = new EndpointAPI.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setApplicationName(APPLICATION_NAME)
                        .setRootUrl(APP_ENGINE_ROOT_URL)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                endpointAPI = builder.build();
            }

            try {
                FCMResponseDTO fcm = endpointAPI.saveUser(user).execute();
                Log.e(TAG, "doInBackground: api returns from GoogleAppEngine: " + fcm.getMessage());
                return fcm;
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: failed to save user in gae", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(FCMResponseDTO result) {
            if (result == null) {
                listener.onError("Unable to save user for FCM messaging");
                return;
            }
            listener.onResponse(result);
        }
    }

    static class SendMessageTask extends AsyncTask<Void, Void, FCMResponseDTO> {

        FCMessageDTO fcMessage;
        FCMListener listener;
        int count = 0;

        public SendMessageTask(FCMessageDTO fcMessage, FCMListener listener) {
            this.fcMessage = fcMessage;
            this.listener = listener;
        }

        @Override
        protected FCMResponseDTO doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: ###################################################");
            if (endpointAPI == null) {  // Only do this once
                EndpointAPI.Builder builder = new EndpointAPI.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setApplicationName(APPLICATION_NAME)
                        .setRootUrl(APP_ENGINE_ROOT_URL)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                endpointAPI = builder.build();
            }

            count = 1;
            return callAPI();
        }

        @Nullable
        private FCMResponseDTO callAPI() {
            try {
                FCMResponseDTO fcm = endpointAPI.sendMessage(fcMessage).execute();
                Log.e(TAG, "doInBackground: api returns from GoogleAppEngine: " + fcm.getMessage());
                if (fcm.getStatusCode() > 0) {
                    return null;
                }
                return fcm;
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: failed to send FCM message in gae", e);

                if (e instanceof SocketTimeoutException) {
                    if (count < 3) {
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                count++;
                            }
                        }, 1000 * count);
                    }
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(FCMResponseDTO result) {
            if (result == null) {
                listener.onError("Unable to send FCM message");
                return;
            }
            listener.onResponse(result);
        }
    }

    static Timer timer;


    static class TopicMessageTask extends AsyncTask<Void, Void, FCMResponseDTO> {

        String id;
        PayLoad payLoad;
        int type;
        FCMListener listener;

        public TopicMessageTask(String id, int type, PayLoad payLoad, FCMListener listener) {
            this.id = id;
            this.payLoad = payLoad;
            this.type = type;
            this.listener = listener;

        }

        @Override
        protected FCMResponseDTO doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: ############################");

            if (endpointAPI == null) {  // Only do this once
                EndpointAPI.Builder builder = new EndpointAPI.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setApplicationName(Constants.APPLICATION_NAME)
                        .setRootUrl(Constants.APP_ENGINE_ROOT_URL)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                endpointAPI = builder.build();
            }

            try {
                FCMResponseDTO fcm = new FCMResponseDTO();
                String topicKey = null;
                switch (type) {
                    case COMPANY:
                        topicKey = TOPIC_COMPANY.concat(id);
                        fcm = endpointAPI.sendTopicMessage(
                                topicKey, payLoad).execute();
                        break;
                    case WEEKLY_MASTERCLASS:
                        topicKey = TOPIC_MASTERCLASS.concat(id);
                        fcm = endpointAPI.sendTopicMessage(
                                topicKey, payLoad).execute();
                        break;
                    case ADMINS:
                        topicKey = TOPIC_COMPANY_STAFF.concat(id);
                        fcm = endpointAPI.sendTopicMessage(
                                topicKey, payLoad).execute();
                        break;
                    case DAILY_THOUGHT:
                        topicKey = TOPIC_DAILY_THOUGHT.concat(id);
                        fcm = endpointAPI.sendTopicMessage(
                                topicKey, payLoad).execute();
                        break;

                    case SUBSCRIBERS:
                        topicKey = TOPIC_SUBSCRIBER.concat(id);
                        fcm = endpointAPI.sendTopicMessage(
                                topicKey, payLoad).execute();
                        break;
                    case LEADERS:
                        topicKey = TOPIC_LEADER.concat(id);
                        fcm = endpointAPI.sendTopicMessage(
                                topicKey, payLoad).execute();
                        break;
                    default:
                        fcm.setStatusCode(99);
                        Log.e(TAG, "doInBackground: invalid message" );
                        break;

                }

                Log.e(TAG, "doInBackground: api returns from GoogleAppEngine: "
                        + fcm.getMessage());
                if (fcm.getStatusCode() > 0) {
                    return null;
                }
                return fcm;
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: failed to send FCM message in gae", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(FCMResponseDTO result) {
            if (result == null) {
                listener.onError("Unable to send FCM topic message");
                return;
            }
            listener.onResponse(result);
        }
    }
    public static final String
            TOPIC_COMPANY_STAFF = "company_staff",
            TOPIC_DAILY_THOUGHT = "daily_thought",
            TOPIC_SUBSCRIBER = "subscriber",
            TOPIC_MASTERCLASS = "master_class",
            TOPIC_COMPANY = "company",
            TOPIC_LEADER = "leader",
            TOPIC_GENERAL = "general";

    static class SendEmailTask extends AsyncTask<Void, Void, EmailResponseDTO> {

        EmailRecordDTO emailRecord;
        EmailListener listener;

        public SendEmailTask(EmailRecordDTO emailRecord, EmailListener listener) {
            this.emailRecord = emailRecord;
            this.listener = listener;
        }

        @Override
        protected EmailResponseDTO doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: ###################################################");
            if (endpointAPI == null) {  // Only do this once
                EndpointAPI.Builder builder = new EndpointAPI.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setApplicationName(APPLICATION_NAME)
                        .setRootUrl(APP_ENGINE_ROOT_URL)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                endpointAPI = builder.build();
            }

            try {

                EmailResponseDTO fcm = endpointAPI.sendEmail(emailRecord).execute();
                Log.e(TAG, "doInBackground: api returns from GAE: " + fcm.getMessage());
                if (fcm.getStatusCode() > 0) {
                    return null;
                }
                return fcm;
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: failed to send FCM message in gae", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(EmailResponseDTO result) {
            if (result == null) {
                listener.onError("Unable to send email message");
                return;
            }
            if (result.getStatusCode() > 0) {
                listener.onError("Unable to send email message");
                return;
            }
            result.setMessage("Email message sent via SendGrid");
            listener.onResponse(result);
        }
    }

}
