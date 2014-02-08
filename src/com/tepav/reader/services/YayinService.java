package com.tepav.reader.services;

import android.util.Log;
import com.tepav.reader.delegates.YayinServiceDelegate;
import com.tepav.reader.models.File;
import com.tepav.reader.models.Yayin;
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
public class YayinService {

    private YayinServiceDelegate yayinServiceDelegate;

    public static final String YAYIN_TYPE_RAPOR        = "1";
    public static final String YAYIN_TYPE_NOT          = "3";
    public static final String YAYIN_TYPE_BASILI_YAYIN = "12";


    public YayinService(YayinServiceDelegate yayinServiceDelegate) {
        this.yayinServiceDelegate = yayinServiceDelegate;
    }

    public void getYayinList() {

        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();

            // Set url
            String url = BaseRequestService.DOMAIN;

            // Create HttpEntity
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            NameValuePair nameValuePair = new BasicNameValuePair("op" , BaseRequestService.OP_YAYIN);
            nameValuePairList.add(nameValuePair);
            HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList);

            baseRequestService.sendAsyncRequest(url , BaseRequestService.POST , httpEntity , BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "yayinListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void getNextYayinList(String url) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();
            baseRequestService.sendAsyncRequest(url , BaseRequestService.GET , null, BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "nextYayinListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void getPrevYayinList(String url) {
        try {
            BaseRequestService baseRequestService = BaseRequestService.getInstance();
            baseRequestService.sendAsyncRequest(url , BaseRequestService.GET , null, BaseRequestService.CONTENT_TYPE_FORM_URL_ENCODED , "prevYayinListRequestDidFinish" , this);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void yayinListRequestDidFinish(String responseString) {
        Map responseMap = this.parseYayinListResponse(responseString);
        yayinServiceDelegate.yayinListRequestDidFinish(responseMap);
    }

    public void nextYayinListRequestDidFinish(String responseString)  {
        Map responseMap = this.parseYayinListResponse(responseString);
        yayinServiceDelegate.nextYayinListRequestDidFinish(responseMap);
    }

    public void prevYayinListRequestDidFinish(String responseString)  {
        Map responseMap = this.parseYayinListResponse(responseString);
        yayinServiceDelegate.prevYayinListRequestDidFinish(responseMap);
    }

    public Map parseYayinListResponse(String responseString) {

        try {
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray dataList    = jsonObject.getJSONArray("data");
            JSONObject nav        = jsonObject.getJSONObject("nav");

            Map responseMap = new HashMap();
            List<Yayin> basılıYayinList  = new ArrayList<Yayin>();
            List<Yayin> notList          = new ArrayList<Yayin>();
            List<Yayin> raporList        = new ArrayList<Yayin>();

            for (int i = 0 ; i < dataList.length() ; i++) {
                JSONObject yayinJson = dataList.getJSONObject(i);
                Yayin yayin = new Yayin();

                Iterator<String> keyIterator = yayinJson.keys();
                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();

                    if (key.equals("files")) {
                        List<File> fileList = BaseRequestService.parseFileListJson(yayinJson.getJSONArray(key));
                        yayin.setFiles(fileList);
                        continue;
                    }

                    String setterName = Util.getSetterMethodName(key);

                    if ( !yayinJson.isNull(key) ) {
                        Object value = yayinJson.get(key);
                        yayin.getClass().getMethod(setterName , value.getClass()).invoke(yayin , value);
                    }

                }

                if (yayin.getYtype_id().equals(YAYIN_TYPE_BASILI_YAYIN))
                    basılıYayinList.add(yayin);
                else if(yayin.getYtype_id().equals(YAYIN_TYPE_NOT))
                    notList.add(yayin);
                else if (yayin.getYtype_id().equals(YAYIN_TYPE_RAPOR))
                    raporList.add(yayin);

            }

            responseMap.put("basılıYayinList" , basılıYayinList);
            responseMap.put("notList"         , notList);
            responseMap.put("raporList"       , raporList);
            responseMap.put("next"            , nav.getString("next"));
            responseMap.put("prev"            , nav.getString("prev"));

            return responseMap;
        }
        catch (Exception e) {
            Log.d("ERROR", "YAYIN  LIST JSON PARSE FAILED.");
        }

        return null;
    }


}
