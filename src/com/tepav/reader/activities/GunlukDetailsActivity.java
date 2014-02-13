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

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 19:26
 */
public class GunlukDetailsActivity extends Activity implements View.OnClickListener {

    Button buttonAddToFavList, buttonAddToReadList, buttonShare;
    ReadingListService readingListService = null;
    private Gunluk gunluk;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gunluk_details);

        gunluk = (Gunluk) getIntent().getSerializableExtra("class");

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setTitle(gunluk.getBtitle());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvGunlukDetailTitle = (TextView) findViewById(R.id.tvGunlukDetailTitle);
        tvGunlukDetailTitle.setText(gunluk.getBtitle());

        TextView tvGunlukDetailRelatedInfo = (TextView) findViewById(R.id.tvGunlukDetailRelatedInfo);
        tvGunlukDetailRelatedInfo.setText(gunluk.getBdate() + " - " + gunluk.getPfullname());

        WebView mWebView = (WebView) findViewById(R.id.webview);

        mWebView.loadData(gunluk.getBcontent(), "text/html; charset=UTF-8", null);
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
            case R.id.bFavList:
                readingListService.save(gunluk, ReadingListService.PERSISTANCE_TYPE_FAVORITES);
                break;
            case  R.id.bReadList:
                readingListService.save(gunluk, ReadingListService.PERSISTANCE_TYPE_READ_LIST);
                break;
            case R.id.bShare:
                Toast.makeText(this, "Social Share", Toast.LENGTH_LONG).show();
                break;
        }
    }
}