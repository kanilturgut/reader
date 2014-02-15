package com.tepav.reader.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 15/02/14
 * Time: 14:50
 */

public class SocialShareService {

    private static final String  POCKET_REQUEST_TOKEN_URL = "https://getpocket.com/v3/oauth/request";
    private static final String  POCKET_CONSUMER_KEY      = "23880-3afa51fd46ba3e23532418d9";
    private static final String  POCKET_REDIRECT_URI      = "pocketapp23880:authorizationFinished";
    private static final String  POCKET_AUTHORIZE_URL     = "https://getpocket.com/v3/oauth/authorize";
    private static String        POCKET_REQUEST_TOKEN;

    private static SocialShareService instance;
    private Activity activity;

    private SocialShareService() {

    }

    public static SocialShareService getInstance(Activity activity) {
        if (instance == null)
            instance = new SocialShareService();

        instance.activity = activity;

        return instance;
    }

    public void obtainPocketRequestToken() {

        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("consumer_key" , POCKET_CONSUMER_KEY);
            jsonObject.put("redirect_uri" , POCKET_REDIRECT_URI);

            HttpEntity entity = new StringEntity(jsonObject.toString());

            baseRequestService.sendAsyncRequest(POCKET_REQUEST_TOKEN_URL , BaseRequestService.POST , entity,
                                                BaseRequestService.CONTENT_TYPE_JSON , "pocketRequestTokenArrived" , this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pocketRequestTokenArrived(String responseString) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            this.POCKET_REQUEST_TOKEN = jsonObject.getString("code");
            redirectPocketAuth();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void redirectPocketAuth() {
        String url = String.format("https://getpocket.com/auth/authorize?request_token=%s&redirect_uri=%s" , POCKET_REQUEST_TOKEN , POCKET_REDIRECT_URI);
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(url));
        activity.startActivityForResult(intent ,  0);
    }

}
