package com.tepav.reader.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import com.tepav.reader.R;
import com.tepav.reader.models.Yayin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 21:44
 */
public class YayinDetailsActivity extends Activity {

    Yayin yayin = null;

    private static String fileName = "tepav.pdf";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yayin_details);

        yayin = (Yayin) getIntent().getSerializableExtra("class");

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setTitle(yayin.getYtitle());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //if some pdf files exist for this yayin
        if (isFilesExist()) {
            new Download_PDF_Task().execute();
        } else {
            TextView textView = (TextView) findViewById(R.id.tvProgressOfPdfDownload);
            textView.setText("No PDF file found q");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private boolean isFilesExist() {
        return (yayin.getFileList().size() > 0);
    }

    private class Download_PDF_Task extends AsyncTask<String, Void, java.io.File> {

        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(YayinDetailsActivity.this, "Bekleyiniz", "Yükleniyor", false, false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            TextView tvProgress = (TextView) findViewById(R.id.tvProgressOfPdfDownload);
            tvProgress.setText("Downloading PDF ");
        }

        @Override
        protected java.io.File doInBackground(String... strings) {
            java.io.File file = null;
            java.io.File outputFile = null;
            try {
                URL url = new URL(yayin.getFileList().get(0).getUrl());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String PATH = Environment.getExternalStorageDirectory()
                        + "/tepavReader/";
                Log.d("Download_PDF_Task", "PATH: " + PATH);
                file = new java.io.File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                outputFile = new java.io.File(file, fileName);
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                Log.e("Download_PDF_Task", "Error: " + e);
            }
            return outputFile;
        }

        @Override
        protected void onPostExecute(java.io.File file) {
            Uri path = Uri.fromFile(file);
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } catch (ActivityNotFoundException e) {

            }
            progressDialog.dismiss();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try{
            File file = new File(Environment.getExternalStorageDirectory() + "/tepavReader/" + fileName);
            if (file.exists())
                file.delete();
        } catch (Exception e) {
            Log.e("YayinDetailsActivity", "FileNotFound", e);
        }


    }
}

