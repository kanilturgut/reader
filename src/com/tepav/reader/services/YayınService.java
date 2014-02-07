package com.tepav.reader.services;

import android.util.Log;
import com.tepav.reader.delegates.YayınServiceDelegate;
import com.tepav.reader.models.File;
import com.tepav.reader.models.Yayın;
import com.tepav.reader.utils.Util;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 07/02/14
 * Time: 19:15
 */
public class YayınService {

    private YayınServiceDelegate yayınServiceDelegate;

    public YayınService(YayınServiceDelegate yayınServiceDelegate) {
        this.yayınServiceDelegate = yayınServiceDelegate;
    }

    public void getYayınList() {

        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();

            // Set url
            String url = BaseRequestService.DOMAIN;

            // Create HttpEntity
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            NameValuePair nameValuePair = new BasicNameValuePair("op" , BaseRequestService.OP_YAYIN);
            nameValuePairList.add(nameValuePair);
            HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList);

            baseRequestService.sendAsyncRequest(url , BaseRequestService.POST , httpEntity , BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "yayınListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void getNextYayınList(String url) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();
            baseRequestService.sendAsyncRequest(url , BaseRequestService.GET , null, BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "nextYayınListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void getPrevYayınList(String url) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();
            baseRequestService.sendAsyncRequest(url , BaseRequestService.GET , null, BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "prevYayınListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void yayınListRequestDidFinish(String responseString) {
        Map responseMap = this.parseYayınListResponse(responseString);
        yayınServiceDelegate.yayınListRequestDidFinish(responseMap);
    }

    public void nextYayınListRequestDidFinish(String responseString)  {
        Map responseMap = this.parseYayınListResponse(responseString);
        yayınServiceDelegate.nextYayınListRequestDidFinish(responseMap);
    }

    public void prevYayınListRequestDidFinish(String responseString)  {
        Map responseMap = this.parseYayınListResponse(responseString);
        yayınServiceDelegate.prevYayınListRequestDidFinish(responseMap);
    }

    public Map parseYayınListResponse(String responseString) {

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray dataList    = jsonObject.getJSONArray("data");
            JSONObject nav        = jsonObject.getJSONObject("nav");

            Map responseMap = new HashMap();
            List<Yayın> yayınList = new ArrayList<Yayın>();

            for (int i = 0 ; i < dataList.length() ; i++) {
                JSONObject yayınJson = dataList.getJSONObject(i);
                Yayın yayın = new Yayın();

                Iterator<String> keyIterator = yayınJson.keys();
                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();

                    if (key.equals("files")) {
                        List<File> fileList = BaseRequestService.parseFileListJson(yayınJson.getJSONArray(key));
                        yayın.setFiles(fileList);
                        continue;
                    }

                    String setterName = Util.getSetterMethodName(key);

                    if ( !yayınJson.isNull(key) ) {
                        Object value = yayınJson.get(key);
                        yayın.getClass().getMethod(setterName , value.getClass()).invoke(yayın , value);
                    }

                }

                yayınList.add(yayın);
            }

            responseMap.put("yayınList" , yayınList);
            responseMap.put("next"      , nav.getString("next"));
            responseMap.put("prev"      , nav.getString("prev"));

            return responseMap;
        }
        catch (Exception e) {
            Log.d("ERROR", "YAYIN  LIST JSON PARSE FAILED.");
        }

        return null;
    }


}
