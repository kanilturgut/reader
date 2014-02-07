package com.tepav.reader.services;

import android.util.Log;
import com.tepav.reader.delegates.HaberServiceDelegate;
import com.tepav.reader.models.File;
import com.tepav.reader.models.Haber;
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
 * Date: 06/02/14
 * Time: 21:10
 */
public class HaberService {

    private HaberServiceDelegate haberServiceDelegate;

    public HaberService(HaberServiceDelegate haberServiceDelegate) {
        this.haberServiceDelegate = haberServiceDelegate;
    }

    public void getHaberList() {

        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();

            // Set url
            String url = BaseRequestService.DOMAIN;

            // Create HttpEntity
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            NameValuePair nameValuePair = new BasicNameValuePair("op" , BaseRequestService.OP_HABER);
            nameValuePairList.add(nameValuePair);
            HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList);

            baseRequestService.sendAsyncRequest(url , BaseRequestService.POST , httpEntity , BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "haberListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void getNextHaberList(String url) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();
            baseRequestService.sendAsyncRequest(url , BaseRequestService.GET , null, BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "nextHaberListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void getPrevHaberList(String url) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();
            baseRequestService.sendAsyncRequest(url , BaseRequestService.GET , null, BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "prevHaberListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void haberListRequestDidFinish(String responseString) {
        Map responseMap = this.parseHaberListResponse(responseString);
        haberServiceDelegate.haberListRequestDidFinish(responseMap);
    }

    public void nextHaberListRequestDidFinish(String responseString)  {
        Map responseMap = this.parseHaberListResponse(responseString);
        haberServiceDelegate.nextHaberListRequestDidFinish(responseMap);
    }

    public void prevHaberListRequestDidFinish(String responseString)  {
        Map responseMap = this.parseHaberListResponse(responseString);
        haberServiceDelegate.prevHaberListRequestDidFinish(responseMap);
    }

    public Map parseHaberListResponse(String responseString) {

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray  dataList   = jsonObject.getJSONArray("data");
            JSONObject nav        = jsonObject.getJSONObject("nav");

            Map responseMap = new HashMap();
            List<Haber> haberList = new ArrayList<Haber>();

            for (int i = 0 ; i < dataList.length() ; i++) {
                JSONObject haberJson = dataList.getJSONObject(i);
                Haber haber = new Haber();

                Iterator<String> keyIterator = haberJson.keys();
                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();

                    if (key.equals("files")) {
                        List<File> fileList = BaseRequestService.parseFileListJson(haberJson.getJSONArray(key));
                        haber.setFiles(fileList);
                        continue;
                    }

                    String setterName = Util.getSetterMethodName(key);

                    if ( !haberJson.isNull(key) ) {
                        Object value = haberJson.get(key);
                        haber.getClass().getMethod(setterName , value.getClass()).invoke(haber , value);
                    }

                }

                haberList.add(haber);
            }

            responseMap.put("haberList" , haberList);
            responseMap.put("next"      , nav.getString("next"));
            responseMap.put("prev"      , nav.getString("prev"));

            return responseMap;
        }
        catch (Exception e) {
            Log.d("ERROR" , "HABER LIST JSON PARSE FAILED.");
        }

        return null;
    }

}
