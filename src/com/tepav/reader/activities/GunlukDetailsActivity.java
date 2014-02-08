package com.tepav.reader.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import com.tepav.reader.R;
import com.tepav.reader.models.Gunluk;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 19:26
 */
public class GunlukDetailsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gunluk_details);

        Gunluk gunluk = (Gunluk) getIntent().getSerializableExtra("class");

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setTitle(gunluk.getBtitle());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvGunlukDetailTitle = (TextView) findViewById(R.id.tvGunlukDetailTitle);
        tvGunlukDetailTitle.setText(gunluk.getBtitle());

        TextView tvGunlukDetailRelatedInfo = (TextView) findViewById(R.id.tvGunlukDetailRelatedInfo);
        tvGunlukDetailRelatedInfo.setText(gunluk.getBdate() + " - " + gunluk.getPfullname());

        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadData(gunluk.getBcontent(), "text/html; charset=UTF-8", null);
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
}