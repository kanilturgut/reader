package com.tepav.reader.services;

import android.os.AsyncTask;
import android.util.Log;
import com.tepav.reader.models.File;
import com.tepav.reader.utils.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 06/02/14
 * Time: 17:27
 */
public class BaseRequestService {

    private static BaseRequestService instance;
    private static DefaultHttpClient  defaultHttpClient;

    public static final String DOMAIN      = "http://test.vklab.net/tepav/server/";
    public static final String OP_HABER    = "/tepav/haber";
    public static final String OP_GUNLUK   = "/tepav/gunluk";
    public static final String OP_YAYIN    = "/tepav/yayin";

    public static final int GET  = 0;
    public static final int POST = 1;


    public static final String CONTENT_TYPE_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON             = "application/json";

    private BaseRequestService() {
        defaultHttpClient = new DefaultHttpClient();
    }

    public static BaseRequestService getInstance() {

        if (instance == null) {
            instance = new BaseRequestService();
        }

        return  instance;

    }

    public void sendAsyncRequest(String url , int httpMethod , HttpEntity entity , String contentType, String callbackMethodName , Object caller) {
        RequestTask requestTask = new RequestTask(url , httpMethod , entity , contentType , callbackMethodName , caller);
        requestTask.execute();
    }

    private class RequestTask extends AsyncTask<Void , Void , String> {

        private String url;
        private int httpMethod;
        private HttpEntity entity;
        private Object caller;
        private String callbackMethodName;
        private String contentType;

        public RequestTask(String url , int httpMethod , HttpEntity entity , String contentType, String callbackMethodName , Object caller) {
            this.url                = url;
            this.httpMethod         = httpMethod;
            this.entity             = entity;
            this.callbackMethodName = callbackMethodName;
            this.caller             = caller;
            this.contentType        = contentType;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                HttpResponse response = null;

                if (httpMethod == GET) {
                    HttpGet httpGet = new HttpGet(url);
                    response = defaultHttpClient.execute(httpGet);
                }
                else if (httpMethod == POST){
                    HttpPost post = new HttpPost(url);

                    if (entity != null)
                        post.setEntity(entity);

                    post.setHeader("Content-Type" , contentType);

                    response = defaultHttpClient.execute(post);
                }

                if (response.getStatusLine().getStatusCode() != 200) {
                    return null;
                }
                else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    out.close();

                    return out.toString();
                }
            }
            catch (Exception e) {
                Log.d("ERROR" , "REQUEST FAILED.");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String responseString) {
            try {
                caller.getClass().getMethod(callbackMethodName , String.class).invoke(caller,responseString);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public static List<File> parseFileListJson(JSONArray fileListJsonArray) {
        List<File> fileList = new ArrayList<File>();

        try {
            for (int i = 0 ; i < fileListJsonArray.length() ; i++) {
                JSONObject fileJson = fileListJsonArray.getJSONObject(i);
                File file = new File();

                Iterator<String> keyIterator = fileJson.keys();
                while(keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    String setterName = Util.getSetterMethodName(key);

                    if (!fileJson.isNull(key))  {
                        Object value = fileJson.get(key);
                        file.getClass().getMethod(setterName , value.getClass()).invoke(file , value);
                    }

                }

                fileList.add(file);
            }

            return fileList;
        }
        catch (Exception e) {
            Log.d("ERROR" , "FILE JSON PARSE FAILED");
        }

        return fileList;
    }

}
