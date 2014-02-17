package com.tepav.reader.services;

import com.tepav.reader.delegates.EmailListRegisterServiceDelegate;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 17/02/14
 * Time: 12:37
 */
public class EmailListRegisterService {


    private final String PASSWORD_K = "android_newsletter";

    public static final int STATUS_OK       = 1;
    public static final int STATUS_EXISTS   = 2;
    public static final int STATUS_FAIL     = 3;


    private EmailListRegisterServiceDelegate emailListRegisterServiceDelegate;

    public EmailListRegisterService(EmailListRegisterServiceDelegate emailListRegisterServiceDelegate) {
        this.emailListRegisterServiceDelegate = emailListRegisterServiceDelegate;
    }


    public void registerForEmailList(String email) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();

            // Set url
            String url = BaseRequestService.DOMAIN;

            // Create HttpEntity
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            NameValuePair opNameValuePair = new BasicNameValuePair("op" , BaseRequestService.OP_REGISTER_EMAIL);
            nameValuePairList.add(opNameValuePair);

            NameValuePair emailNameValuePair = new BasicNameValuePair("email" , email);
            nameValuePairList.add(emailNameValuePair);

            NameValuePair passNameValuePair = new BasicNameValuePair("password_k" , PASSWORD_K);
            nameValuePairList.add(passNameValuePair);

            HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList);

            baseRequestService.sendAsyncRequest(url , BaseRequestService.POST , httpEntity , BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "registerForEmailListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void registerForEmailListRequestDidFinish(String responseString) {
         try {
             JSONObject jsonObject = new JSONObject(responseString);
             if (jsonObject.has("result") && jsonObject.getString("result").equals("ok"))
                 this.emailListRegisterServiceDelegate.registerForEmailListRequestDidFinish(STATUS_OK);
             else if(jsonObject.has("result") && jsonObject.getString("error").equals("error"))
                 this.emailListRegisterServiceDelegate.registerForEmailListRequestDidFinish(STATUS_EXISTS);
             else
                 this.emailListRegisterServiceDelegate.registerForEmailListRequestDidFinish(STATUS_FAIL);
         }
         catch (Exception e) {
             e.printStackTrace();
             this.emailListRegisterServiceDelegate.registerForEmailListRequestDidFinish(STATUS_FAIL);
         }
    }

}
