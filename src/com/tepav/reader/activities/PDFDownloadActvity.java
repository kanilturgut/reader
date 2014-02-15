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
import android.widget.Button;
import com.tepav.reader.R;
import com.tepav.reader.models.Gunluk;
import com.tepav.reader.models.Haber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 14.02.2014
 * Time: 21:27
 */
public class PDFDownloadActvity extends Activity {

    private File downloadedPDF = null;
    private Button buttonOpenDownloadedPDF;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_download);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Haber haber = (Haber) getIntent().getSerializableExtra("class");
        getActionBar().setTitle(haber.getHtitle());


        buttonOpenDownloadedPDF = (Button) findViewById(R.id.buttonOpenDownloadedPDF);
        buttonOpenDownloadedPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downloadedPDF != null) {
                    try {
                        Uri path = Uri.fromFile(downloadedPDF);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path, "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } catch (ActivityNotFoundException e) {

                    }
                }
            }
        });

        chechIfAlreadySaved(getIntent().getStringExtra("file_name"));
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

    public class PDF_Task extends AsyncTask<String, Integer, File> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            buttonOpenDownloadedPDF.setText(getResources().getString(R.string.doc_downloading) + values[0]);
        }

        @Override
        protected File doInBackground(String... strings) {
            File file = null;
            File outputFile = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                int fileLength = c.getContentLength();
                String PATH = Environment.getExternalStorageDirectory()
                        + "/tepavReader/";
                file = new File(PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }

                outputFile = new File(file, strings[1]);
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

                return outputFile;

            } catch (IOException e) {
                cancel(true);
                Log.e("Download_PDF_Task", "Error: " + e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            downloadedPDF = file;
            buttonOpenDownloadedPDF.setText(getResources().getString(R.string.open_doc));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            buttonOpenDownloadedPDF.setText(getResources().getString(R.string.not_found_doc));
        }
    }

    private void chechIfAlreadySaved(String filename) {
        File newFile = new File(Environment.getExternalStorageDirectory() + "/tepavReader/" + filename);

        if (!newFile.exists()) {
            new PDF_Task().execute(getIntent().getStringExtra("file_url"), filename);
        } else {
            downloadedPDF = newFile;
            buttonOpenDownloadedPDF.setText(getResources().getString(R.string.open_doc));
        }
    }
}