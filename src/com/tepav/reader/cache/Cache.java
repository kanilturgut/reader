package com.tepav.reader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import com.tepav.reader.R;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 07.02.2014
 * Time: 23:44
 */
public class Cache {

    private static Cache instance = null;
    private static final int cacheSize = 4 * 1024 * 1024; //4MiB
    private LruCache<String, Bitmap> cache = null;
    private final static String TAG = "Cache";

    private Cache() {
        cache = new LruCache<String, Bitmap>(cacheSize);
    }

    public static Cache getInstance() {
        if (instance == null)
            instance = new Cache();

        return instance;
    }

    public void getImageFromCache(Context context, String url, ImageView iv) {
        Log.d(TAG, "Cache URL: " + url);
        Bitmap bmp;
        synchronized (cache) {
            bmp = cache.get(url);
        }
        if (bmp != null) {
            Log.d(TAG, "Cache HIT: " + url);
            iv.setImageBitmap(bmp);
        } else {
            Log.d(TAG, "Cache MISS: " + url);
            DownloadImageTask dt = new DownloadImageTask(context, url, iv);
            dt.execute();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private final String url;
        private final ImageView iv;
        private final Context context;

        public DownloadImageTask(Context context, String url, ImageView iv) {
            this.iv = iv;
            this.url = url;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            InputStream is = context.getResources().openRawResource(R.drawable.no_image);
            iv.setImageBitmap(BitmapFactory.decodeStream(is));
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(this.url).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                cancel(true);
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                synchronized (cache) {
                    cache.put(this.url, result);
                }
                iv.setImageBitmap(result);
            }
            else {
                InputStream is = context.getResources().openRawResource(R.drawable.no_image);
                iv.setImageBitmap(BitmapFactory.decodeStream(is));
            }
        }

        @Override
        protected void onCancelled() {
            InputStream is = context.getResources().openRawResource(R.drawable.no_image);
            iv.setImageBitmap(BitmapFactory.decodeStream(is));
        }
    }
}

