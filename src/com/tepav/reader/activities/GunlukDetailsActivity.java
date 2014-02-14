package com.tepav.reader.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.tepav.reader.R;
import com.tepav.reader.models.Gunluk;
import com.tepav.reader.services.ReadingListService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 19:26
 */
public class GunlukDetailsActivity extends Activity implements View.OnClickListener {

    Button buttonAddToFavList, buttonRemoveFromFavList, buttonAddToReadList,buttonRemoveFromReadList, buttonShare;
    ReadingListService readingListService = null;
    private Gunluk gunluk;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gunluk_details);

        gunluk = (Gunluk) getIntent().getSerializableExtra("class");
        readingListService = ReadingListService.getInstance(this);

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setTitle(gunluk.getBtitle());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvGunlukDetailTitle = (TextView) findViewById(R.id.tvGunlukDetailTitle);
        tvGunlukDetailTitle.setText(gunluk.getBtitle());

        TextView tvGunlukDetailRelatedInfo = (TextView) findViewById(R.id.tvGunlukDetailRelatedInfo);
        tvGunlukDetailRelatedInfo.setText(gunluk.getBdate() + " - " + gunluk.getPfullname());

        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadData(gunluk.getBcontent(), "text/html; charset=UTF-8", null);

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
        switch (item.getItemId())
        {
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
                readingListService.save(gunluk, ReadingListService.PERSISTANCE_TYPE_FAVORITES);
                disableAndEnableButtons(buttonAddToFavList, buttonRemoveFromFavList);
                break;
            case R.id.buttonRemoveFavList:
                readingListService.delete(gunluk, ReadingListService.PERSISTANCE_TYPE_FAVORITES);
                disableAndEnableButtons(buttonRemoveFromFavList, buttonAddToFavList);
                break;
            case  R.id.buttonAddReadList:
                readingListService.save(gunluk, ReadingListService.PERSISTANCE_TYPE_READ_LIST);
                disableAndEnableButtons(buttonAddToReadList, buttonRemoveFromReadList);
                break;
            case R.id.buttonRemoveReadList:
                readingListService.delete(gunluk, ReadingListService.PERSISTANCE_TYPE_READ_LIST);
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

    private void checkLists(){
        checkIfInFavoriteList();
        checkIfInReadList();
    }

    private void checkIfInFavoriteList() {
        List<Object> favoriteList = readingListService.getFavoritesList();
        for (Object object: favoriteList) {
            if (object instanceof Gunluk) {
                Gunluk favoritedGunluk = (Gunluk) object;
                if (favoritedGunluk.getGunluk_id().equals(gunluk.getGunluk_id())) {
                    //it is already in favorite list
                    disableAndEnableButtons(buttonAddToFavList, buttonRemoveFromFavList);
                }
            }
        }
    }

    private void checkIfInReadList() {
        List<Object> readList = readingListService.getReadingList();
        for (Object object: readList) {
            if (object instanceof Gunluk) {
                Gunluk readedGunluk = (Gunluk) object;
                if (readedGunluk.getGunluk_id().equals(gunluk.getGunluk_id())) {
                    //it is already in favorite list
                    disableAndEnableButtons(buttonAddToReadList, buttonRemoveFromReadList);
                }
            }
        }
    }
}