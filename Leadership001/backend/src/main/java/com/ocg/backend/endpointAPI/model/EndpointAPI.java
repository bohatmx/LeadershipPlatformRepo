/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.ocg.backend.endpointAPI.model;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.gson.Gson;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.cmd.Query;

import com.ocg.backend.endpointAPI.model.data.EmailRecordDTO;
import com.ocg.backend.endpointAPI.model.data.EmailResponseDTO;
import com.ocg.backend.endpointAPI.model.data.FCMResponseDTO;
import com.ocg.backend.endpointAPI.model.data.FCMRoot;
import com.ocg.backend.endpointAPI.model.data.FCMessageDTO;
import com.ocg.backend.endpointAPI.model.data.PayLoad;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "endpointAPI",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.ocg.com",
                ownerName = "backend.ocg.com",
                packagePath = ""

        )
)
public class EndpointAPI {
    private static final Logger log = Logger.getLogger(EndpointAPI.class.getName());
    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send",
            API_KEY = "AAAAZgwTzHM:APA91bE3GGJDu3JikXmXjWlX9dh0teidOTnzIG97HZsfEGu4YEIrj7Gs4c3WqO-8PMjI259pGoyFtzQ-6VXgKsEX2kBebGl0mNXVp5hxlyCBrf8WPT1pNiJNvO70Vn3c8xghVyR2M7nI";
    public static final Gson gson = new Gson();
    public static final String CONTENT_TYPE = "application/json; charset=UTF-8";

    @ApiMethod(name = "sendMessage")
    public FCMResponseDTO sendMessage(final FCMessageDTO message) {
        log.warning("sendMessage running inside GAE endpoint...we have happiness!!!");
        final StringBuilder sb = new StringBuilder();
        final FCMResponseDTO fcm = new FCMResponseDTO();
        final List<Integer> statusCodes = new ArrayList<>();
        ObjectifyService.run(new VoidWork() {
            public void vrun() {
                try {
                    for (String id : message.getUserIDs()) {
                        Query<FCMUserDTO> userQuery = ObjectifyService.ofy()
                                .load()
                                .type(FCMUserDTO.class)
                                .filter("userID", id);
                        for (FCMUserDTO u : userQuery) {
                            if (u == null) {
                                log.log(Level.WARNING, "FCMUser is null, not found in datastore, id searched:  "
                                        + id + " title: " + message.getData().getTitle());
                                fcm.setStatusCode(555);
                                fcm.setMessage("User not registered for FCM messaging: " + id);
                                log.log(Level.WARNING, fcm.getMessage());
                                return;
                            }
                            if (u.getToken() == null) {
                                log.log(Level.WARNING, "FCMUser TOKEN is null, userID:  "
                                        + id + " title: " + message.getData().getTitle());
                                fcm.setStatusCode(555);
                                fcm.setMessage("User not registered for FCM messaging: " + id);
                                log.log(Level.WARNING, fcm.getMessage());
                                return;
                            }
                            log.log(Level.WARNING, "FCM User retrieved...Yay! "
                                    + u.getName() + " - sending FCM message: " + u.getToken());
                            //send message ........
                            PayLoad payLoad = new PayLoad(u.getToken(), message.getData());
                            FCMRoot root = send(payLoad);
                            if (root.getFailure() == 0 && root.getCanonicalIds() == 0) {
                                log.log(Level.WARNING, "Everything's hunky dory; message in FCM's safe hands!, userID: " + id);
                                statusCodes.add(0);
                                sb.append("######### GoogleAppEngine successfully sent FCM message to userID: " + id);
                                sb.append("\n");
                            } else {
                                statusCodes.add(777);
                                sb.append("???????? Unable to send FCM message to userID ").append(id).append("\n");
                                log.log(Level.SEVERE, "Unable to send FCM message, userID: " + id + " msg: "
                                        + message.getData().getTitle());
                            }
                        }
                    }
                    int errors = 0, good = 0;
                    for (Integer status : statusCodes) {
                        if (status > 0) {
                            errors++;
                        } else {
                            good++;
                        }
                    }
                    log.log(Level.WARNING, "sent FCM messages, good: " + good + " errors: " + errors);
                    if (good > 0) {
                        fcm.setStatusCode(0);
                    } else {
                        fcm.setStatusCode(errors);
                    }
                    fcm.setMessage(sb.toString());
                    saveMessage(message);
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Error sending fcm message: " + e.getLocalizedMessage());
                    fcm.setStatusCode(8);
                    fcm.setMessage("Unable to send message via FCM: \n" + e.getMessage());
                }
            }
        });


        return fcm;
    }

    private FCMRoot send(PayLoad payLoad) {
        String json = gson.toJson(payLoad);
        log.log(Level.WARNING, json);
        FCMRoot root = null;
        try {
            URLFetchService url_service = URLFetchServiceFactory.getURLFetchService();
            HTTPRequest request = new HTTPRequest(new URL(FCM_URL), HTTPMethod.POST);
            request.setHeader(new HTTPHeader("Content-Type", CONTENT_TYPE));
            request.setHeader(new HTTPHeader("Authorization", "key=" + API_KEY));
            request.setPayload(json.getBytes("UTF-8"));
            HTTPResponse response = url_service.fetch(request);
            if (response.getResponseCode() != 200) {
                log.log(Level.SEVERE, "ERROR: HTTP response code from FCM is: " + response.getResponseCode()
                        + " " + response.getContent().toString());
                root = new FCMRoot();
                root.setSuccess(0);
                root.setFailure(1);
                return root;
            }
            String content = new String(response.getContent());
            log.log(Level.WARNING, ".........response from FCM, content:  " + content);
            root = gson.fromJson(content, FCMRoot.class);
            processResponse(root);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "*** FCM server problem ...: ", ex);
            root = new FCMRoot();
            root.setSuccess(0);
            root.setFailure(1);
        }
        return root;
    }

    private void processResponse(FCMRoot root) {
        log.log(Level.ALL, "about to process response from fcm in case there are canonical ids to update");
    }

    @ApiMethod(name = "saveUser")
    public FCMResponseDTO saveUser(final FCMUserDTO user) {
        log.warning("about to save user in datastore ...." + user.getName());
        final FCMResponseDTO fcm = new FCMResponseDTO();
        if (user.getToken() == null) {
            fcm.setStatusCode(8);
            fcm.setMessage("FCM token is null. Check on FCM user save");
            return fcm;
        }
        ObjectifyService.run(new VoidWork() {
            public void vrun() {

                try {
                    ofy().save().entity(user).now();
                    fcm.setMessage("Things are just fine and dandy! User saved, token: " + user.getToken());
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Error saving user in datastore: " + user.getUserID(), e);
                    fcm.setStatusCode(8);
                    fcm.setMessage("Unable to save user in datastore");
                }
            }
        });

        return fcm;
    }

    private FCMResponseDTO saveMessage(final FCMessageDTO messaage) {
        log.warning("about to save message in datastore ....");
        final FCMResponseDTO fcm = new FCMResponseDTO();
        ObjectifyService.run(new VoidWork() {
            public void vrun() {

                try {
                    ofy().save().entity(messaage).now();
                    log.log(Level.WARNING, "Looks com.oneconnect.leadership.library.like the message has been saved...Yay!");
                    fcm.setMessage("Things are just fine and dandy! message saved");
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Error saving message: ", e);
                    fcm.setStatusCode(888);
                    fcm.setMessage("Unable to save message in datastore");
                }
            }
        });

        return fcm;
    }

    //############### TOPIC MESSAGES  #####################################
    @ApiMethod(name = "sendTopicMessage")
    public FCMResponseDTO sendTopicMessage (
            @Named("topicKey") String topicKey,
            PayLoad payLoad) throws IOException {
        FCMResponseDTO resp = new FCMResponseDTO();
        try {
            payLoad.setTo("/topics/".concat(topicKey));
            String json = gson.toJson(payLoad);
            log.log(Level.WARNING, json);
            doSend(topicKey, payLoad, json);
            resp.setMessage("Daily Thought topic message sent");
        } catch (Exception e) {
            resp.setStatusCode(666);
            resp.setMessage("Failed to send to daily thought message");
            log.log(Level.SEVERE, "Failed to send message in topic: " + e.getMessage());
        }
        return resp;
    }

    private void doSend(String topicKey, PayLoad payLoad, String json) throws IOException {
        try {
            URLFetchService url_service = URLFetchServiceFactory.getURLFetchService();
            HTTPRequest request = new HTTPRequest(new URL(FCM_URL), HTTPMethod.POST);
            request.setHeader(new HTTPHeader("Content-Type", CONTENT_TYPE));
            request.setHeader(new HTTPHeader("Authorization", "key=" + API_KEY));
            request.setPayload(json.getBytes("utf8"));
            HTTPResponse response = url_service.fetch(request);
            if (response.getResponseCode() != 200) {
                throw new IOException(new String(response.getContent()));
            }
            String content = new String(response.getContent());
            log.log(Level.WARNING, "response from fcm:  " + content);
            saveTopicMessage(topicKey, payLoad);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to send topic message: " + json, e);
            throw new IOException();
        }
    }

    private FCMResponseDTO saveTopicMessage(String topicKey, final PayLoad p) {
        log.warning("about to save message in datastore ....");
        final FCMessageDTO message = new FCMessageDTO();
        int i = topicKey.lastIndexOf("@");
        if (i < 0) {
            return new FCMResponseDTO();
        }
        message.setCompanyID(topicKey.substring(i + 1));
        message.setData(p.getData());
        message.setDate(new Date().getTime());
        final FCMResponseDTO fcm = new FCMResponseDTO();
        ObjectifyService.run(new VoidWork() {
            public void vrun() {

                try {
                    ofy().save().entity(message).now();
                    log.log(Level.WARNING, "Looks com.oneconnect.leadership.library.like the message has been saved OK...Yay!");
                    fcm.setMessage("Things are just fine and dandy! message saved");
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Error saving message: ", e);
                    fcm.setStatusCode(888);
                    fcm.setMessage("Unable to save message in datastore");
                }
            }
        });

        return fcm;
    }

    @ApiMethod(name = "sendEmail")
    public EmailResponseDTO sendEmail(EmailRecordDTO email) {
        EmailResponseDTO resp = new EmailResponseDTO();
        try {
            int result = sendMail(email);
            if (result == 0) {
                resp.setMessage("Email successfully sent");
            } else {
                resp.setStatusCode(9);
                resp.setMessage("Unable to send email message");
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "SendGrid email send failed", e);
            resp.setStatusCode(9);
            resp.setMessage("Unable to send email message");
        }


        return resp;
    }

    public static final String SENDGRID_API =
            "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";

    private int sendMail(EmailRecordDTO r) throws IOException {
        Email from = new Email(r.getSentFrom());
        Email to = new Email(r.getSentTo());
        Content content = new Content("text/html", r.getText());
        Mail mail = new Mail(from, r.getSubject(), to, content);

        SendGrid sg = new SendGrid(SENDGRID_API);
        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sg.api(request);
            log.log(Level.INFO, "################### Email Sent: status code: {0} body: {1} \nheaders: {2}",
                    new Object[]{response.statusCode, response.body, response.headers});
            return 0;
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Failed to send SendGrid email message", ex);
            throw ex;
        }
    }

}
