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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.tepav.reader.R;
import com.tepav.reader.models.Yayin;
import com.tepav.reader.services.ReadingListService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 21:44
 */
public class YayinDetailsActivity extends Activity implements View.OnClickListener {

    Button buttonAddToFavList, buttonAddToReadList, buttonShare;
    ReadingListService readingListService = null;
    Yayin yayin = null;

    TextView tvProgressOfPdfDownload = null;

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
            tvProgressOfPdfDownload = (TextView) findViewById(R.id.tvProgressOfPdfDownload);
            tvProgressOfPdfDownload.setText("No PDF file found");
        }

        buttonAddToFavList = (Button) findViewById(R.id.bFavList);
        buttonAddToFavList.setOnClickListener(this);

        buttonAddToReadList = (Button) findViewById(R.id.bReadList);
        buttonAddToReadList.setOnClickListener(this);

        buttonShare = (Button) findViewById(R.id.bShare);
        buttonShare.setOnClickListener(this);

        readingListService = ReadingListService.getInstance(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bFavList:
                readingListService.save(yayin, ReadingListService.PERSISTANCE_TYPE_FAVORITES);
                break;
            case  R.id.bReadList:
                readingListService.save(yayin, ReadingListService.PERSISTANCE_TYPE_READ_LIST);
                break;
            case R.id.bShare:
                Toast.makeText(this, "Social Share", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private class Download_PDF_Task extends AsyncTask<String, Integer, java.io.File> {


        @Override
        protected void onProgressUpdate(Integer... values) {

            TextView tvProgress = (TextView) findViewById(R.id.tvProgressOfPdfDownload);
            tvProgress.setText("PDF Yükleniyor %" + values[0]);
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

                int fileLength = c.getContentLength();

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

                long total = 0;

                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    publishProgress((int) (total * 100 / fileLength));
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

