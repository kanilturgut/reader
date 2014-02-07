package com.tepav.reader.services;

import android.util.Log;
import com.tepav.reader.delegates.GunlukServiceDelegate;
import com.tepav.reader.models.Gunluk;
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
 * Time: 16:32
 */
public class GunlukService {

    private GunlukServiceDelegate gunlukServiceDelegate;

    public GunlukService(GunlukServiceDelegate gunlukServiceDelegate) {
        this.gunlukServiceDelegate = gunlukServiceDelegate;
    }

    public void getGunlukList() {

        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();

            // Set url
            String url = BaseRequestService.DOMAIN;

            // Create HttpEntity
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            NameValuePair nameValuePair = new BasicNameValuePair("op" , BaseRequestService.OP_GUNLUK);
            nameValuePairList.add(nameValuePair);
            HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList);

            baseRequestService.sendAsyncRequest(url , BaseRequestService.POST , httpEntity , BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "gunlukListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void getNextGunlukList(String url) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();
            baseRequestService.sendAsyncRequest(url , BaseRequestService.GET , null, BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "nextGunlukListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void getPrevGunlukList(String url) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();
            baseRequestService.sendAsyncRequest(url , BaseRequestService.GET , null, BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "prevGunlukListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void gunlukListRequestDidFinish(String responseString) {
        Map responseMap = this.parseGunlukListResponse(responseString);
        gunlukServiceDelegate.gunlukListRequestDidFinish(responseMap);
    }

    public void nextGunlukListRequestDidFinish(String responseString)  {
        Map responseMap = this.parseGunlukListResponse(responseString);
        gunlukServiceDelegate.nextGunlukListRequestDidFinish(responseMap);
    }

    public void prevGunlukListRequestDidFinish(String responseString)  {
        Map responseMap = this.parseGunlukListResponse(responseString);
        gunlukServiceDelegate.prevGunlukListRequestDidFinish(responseMap);
    }

    public Map parseGunlukListResponse(String responseString) {

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray dataList    = jsonObject.getJSONArray("data");
            JSONObject nav        = jsonObject.getJSONObject("nav");

            Map responseMap = new HashMap();
            List<Gunluk> gunlukList = new ArrayList<Gunluk>();

            for (int i = 0 ; i < dataList.length() ; i++) {
                JSONObject gunlukJson = dataList.getJSONObject(i);
                Gunluk gunluk = new Gunluk();

                Iterator<String> keyIterator = gunlukJson.keys();
                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    String setterName = Util.getSetterMethodName(key);

                    if ( !gunlukJson.isNull(key) ) {
                        Object value = gunlukJson.get(key);
                        gunluk.getClass().getMethod(setterName , value.getClass()).invoke(gunluk , value);
                    }

                }

                gunlukList.add(gunluk);
            }

            responseMap.put("gunlukList" , gunlukList);
            responseMap.put("next"      , nav.getString("next"));
            responseMap.put("prev"      , nav.getString("prev"));

            return responseMap;
        }
        catch (Exception e) {
            Log.d("ERROR", "GUNLUK  LIST JSON PARSE FAILED.");
        }

        return null;
    }

}
