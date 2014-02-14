package com.tepav.reader.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tepav.reader.R;
import com.tepav.reader.models.Haber;
import com.tepav.reader.services.ReadingListService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 16:42
 */
public class HaberDetailsActivity extends Activity implements View.OnClickListener {

    Button buttonAddToFavList, buttonRemoveFromFavList, buttonAddToReadList, buttonRemoveFromReadList, buttonShare;
    ReadingListService readingListService = null;
    private Haber haber;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_details);

        haber = (Haber) getIntent().getSerializableExtra("class");
        readingListService = ReadingListService.getInstance(this);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setTitle(haber.getHtitle());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvHaberDetailTitle = (TextView) findViewById(R.id.tvHaberDetailTitle);
        tvHaberDetailTitle.setText(haber.getHtitle());

        TextView tvHaberDetailRelatedInfo = (TextView) findViewById(R.id.tvHaberDetailRelatedInfo);
        tvHaberDetailRelatedInfo.setText(haber.getHdate() + " - " + haber.getDname());

        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadData(haber.getHcontent(), "text/html; charset=UTF-8", null);

        LinearLayout filesLayout = (LinearLayout) findViewById(R.id.filesLayout);
        for (com.tepav.reader.models.File file : haber.getFileList()) {
            filesLayout.addView(createTextView(file));
        }

        initializeButtons();
        checkLists();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonAddFavList:
                readingListService.save(haber, ReadingListService.PERSISTANCE_TYPE_FAVORITES);
                disableAndEnableButtons(buttonAddToFavList, buttonRemoveFromFavList);
                break;
            case R.id.buttonRemoveFavList:
                readingListService.delete(haber, ReadingListService.PERSISTANCE_TYPE_FAVORITES);
                disableAndEnableButtons(buttonRemoveFromFavList, buttonAddToFavList);
                break;
            case R.id.buttonAddReadList:
                readingListService.save(haber, ReadingListService.PERSISTANCE_TYPE_READ_LIST);
                disableAndEnableButtons(buttonAddToReadList, buttonRemoveFromReadList);
                break;
            case R.id.buttonRemoveReadList:
                readingListService.delete(haber, ReadingListService.PERSISTANCE_TYPE_READ_LIST);
                disableAndEnableButtons(buttonRemoveFromReadList, buttonAddToReadList);
                break;
            case R.id.bShare:
                Toast.makeText(this, "Social Share", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void disableAndEnableButtons(Button disableThisButton, Button enableThisButton) {
        disableThisButton.setVisibility(Button.GONE);
        enableThisButton.setVisibility(Button.VISIBLE);
    }

    private void checkLists() {
        checkIfInFavoriteList();
        checkIfInReadList();
    }

    private void checkIfInFavoriteList() {
        List<Object> favoriteList = readingListService.getFavoritesList();
        for (Object object : favoriteList) {
            if (object instanceof Haber) {
                Haber favoritedHaber = (Haber) object;
                if (favoritedHaber.getHaber_id().equals(haber.getHaber_id())) {
                    //it is already in favorite list
                    disableAndEnableButtons(buttonAddToFavList, buttonRemoveFromFavList);
                }
            }
        }
    }

    private void checkIfInReadList() {
        List<Object> readList = readingListService.getReadingList();
        for (Object object : readList) {
            if (object instanceof Haber) {
                Haber readedHaber = (Haber) object;
                if (readedHaber.getHaber_id().equals(haber.getHaber_id())) {
                    //it is already in favorite list
                    disableAndEnableButtons(buttonAddToReadList, buttonRemoveFromReadList);
                }
            }
        }
    }

    public TextView createTextView(final com.tepav.reader.models.File file) {
        TextView textView = new TextView(this);
        textView.setId(R.id.fileLink);
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(20);
        textView.setText(file.getName());
        textView.setTag(file);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openPDFIntent = new Intent(HaberDetailsActivity.this, PDFDownloadActvity.class);
                openPDFIntent.putExtra("file_name", file.getName());
                openPDFIntent.putExtra("file_url", file.getUrl());
                openPDFIntent.putExtra("class", haber);
                startActivity(openPDFIntent);
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 5, 0, 5);
        textView.setLayoutParams(lp);

        return textView;
    }
}