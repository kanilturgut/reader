package com.tepav.reader.activities;

import android.app.Activity;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.tepav.reader.R;
import com.tepav.reader.models.Yayin;
import com.tepav.reader.services.ReadingListService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 21:44
 */
public class YayinDetailsActivity extends Activity implements View.OnClickListener {

    Button buttonAddToFavList, buttonRemoveFromFavList, buttonAddToReadList, buttonRemoveFromReadList, buttonShare;
    Button buttonOpenPDF;
    ReadingListService readingListService = null;
    Yayin yayin = null;
    File downloadedPDF = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yayin_details);

        yayin = (Yayin) getIntent().getSerializableExtra("class");
        readingListService = ReadingListService.getInstance(this);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setTitle(yayin.getYtitle());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        buttonOpenPDF = (Button) findViewById(R.id.buttonOpenPDF);
        buttonOpenPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (downloadedPDF != null) {
                    try {
                        Uri path = Uri.fromFile(downloadedPDF);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            }
        });

        //if some pdf files exist for this yayin
        if (isFilesExist()) {
            chechIfAlreadySaved(yayin.getFileList().get(0).getName());
        } else {
            buttonOpenPDF.setText(getResources().getString(R.string.no_doc));
        }

        TextView tvHaberDetailTitle = (TextView) findViewById(R.id.tvYayinDetailTitle);
        tvHaberDetailTitle.setText(yayin.getYtitle());

        TextView tvHaberDetailRelatedInfo = (TextView) findViewById(R.id.tvYayinDetailRelatedInfo);
        tvHaberDetailRelatedInfo.setText(yayin.getYauthors());

        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadData(yayin.getYcontent(), "text/html; charset=UTF-8", null);

        initializeButtons();
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

    private void initializeButtons() {

        buttonAddToFavList = (Button) findViewById(R.id.buttonAddFavList);
        buttonAddToFavList.setOnClickListener(this);

        buttonRemoveFromFavList = (Button) findViewById(R.id.buttonRemoveFavList);
        buttonRemoveFromFavList.setOnClickListener(this);

        buttonAddToReadList = (Button) findViewById(R.id.buttonAddReadList);
        buttonAddToReadList.setOnClickListener(this);

        buttonRemoveFromReadList = (Button) findViewById(R.id.buttonRemoveReadList);
        buttonRemoveFromReadList.setOnClickListener(this);

        buttonShare = (Button) findViewById(R.id.bShare);
        buttonShare.setOnClickListener(this);

        checkLists();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddFavList:
                readingListService.save(yayin, ReadingListService.PERSISTANCE_TYPE_FAVORITES);
                disableAndEnableButtons(buttonAddToFavList, buttonRemoveFromFavList);
                break;
            case R.id.buttonRemoveFavList:
                readingListService.delete(yayin, ReadingListService.PERSISTANCE_TYPE_FAVORITES);
                disableAndEnableButtons(buttonRemoveFromFavList, buttonAddToFavList);
                break;
            case R.id.buttonAddReadList:
                readingListService.save(yayin, ReadingListService.PERSISTANCE_TYPE_READ_LIST);
                disableAndEnableButtons(buttonAddToReadList, buttonRemoveFromReadList);
                break;
            case R.id.buttonRemoveReadList:
                readingListService.delete(yayin, ReadingListService.PERSISTANCE_TYPE_READ_LIST);
                disableAndEnableButtons(buttonRemoveFromReadList, buttonAddToReadList);
                break;
            case R.id.bShare:
                String url = "http://www.tepav.org.tr/tr/yayin/s/" + yayin.getYayin_id();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, yayin.getYtitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_with)));
                break;
        }
    }

    private class Download_PDF_Task extends AsyncTask<String, Integer, java.io.File> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            buttonOpenPDF.setText(getResources().getString(R.string.doc_downloading) + values[0]);

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
                file = new java.io.File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                outputFile = new java.io.File(file, yayin.getFileList().get(0).getName());
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
                cancel(true);
            }
            return outputFile;
        }

        @Override
        protected void onPostExecute(java.io.File file) {
            downloadedPDF = file;
            buttonOpenPDF.setText(getResources().getString(R.string.open_doc));
        }

        @Override
        protected void onCancelled() {
            buttonOpenPDF.setText(getResources().getString(R.string.not_found_doc));
        }
    }

    private void disableAndEnableButtons(Button disableThisButton, Button enableThisButton) {
        disableThisButton.setVisibility(Button.GONE);
        enableThisButton.setVisibility(Button.VISIBLE);
    }

    private void checkLists(){
        checkIfInFavoriteList();
        checkIfInReadList();
    }

    private void checkIfInFavoriteList() {
        List<Object> favoriteList = readingListService.getFavoritesList();
        for (Object object: favoriteList) {
            if (object instanceof Yayin) {
                Yayin favoritedYayin = (Yayin) object;
                if (favoritedYayin.getYayin_id().equals(yayin.getYayin_id())) {
                    //it is already in favorite list
                    disableAndEnableButtons(buttonAddToFavList, buttonRemoveFromFavList);
                }
            }
        }
    }

    private void checkIfInReadList() {
        List<Object> readList = readingListService.getReadingList();
        for (Object object: readList) {
            if (object instanceof Yayin) {
                Yayin readedYayin = (Yayin) object;
                if (readedYayin.getYayin_id().equals(yayin.getYayin_id())) {
                    //it is already in favorite list
                    disableAndEnableButtons(buttonAddToReadList, buttonRemoveFromReadList);
                }
            }
        }
    }

    private void chechIfAlreadySaved(String filename) {
        File newFile = new File(Environment.getExternalStorageDirectory() + "/tepavReader/" + filename);

        if (!newFile.exists()) {
            new Download_PDF_Task().execute();
        } else {
            downloadedPDF = newFile;
            buttonOpenPDF.setText(getResources().getString(R.string.open_doc));
        }
    }
}