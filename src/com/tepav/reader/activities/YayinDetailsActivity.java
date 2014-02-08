package com.tepav.reader.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import com.tepav.reader.R;
import com.tepav.reader.models.Yayin;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 21:44
 */
public class YayinDetailsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yayin_details);

        Yayin yayin = (Yayin) getIntent().getSerializableExtra("class");

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setTitle(yayin.getYtitle());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvYayinDetailTitle = (TextView) findViewById(R.id.tvYayinDetailTitle);
        tvYayinDetailTitle.setText(yayin.getYtitle());

        TextView tvYayinDetailRelatedInfo = (TextView) findViewById(R.id.tvYayinDetailRelatedInfo);
        tvYayinDetailRelatedInfo.setText(yayin.getYauthors() + " - " + yayin.getYtype());


        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadData(yayin.getYcontent(), "text/html; charset=UTF-8", null);
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