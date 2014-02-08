package com.tepav.reader.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import com.tepav.reader.R;
import com.tepav.reader.models.Haber;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 08.02.2014
 * Time: 16:42
 */
public class HaberDetailsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_details);

        Haber haber = (Haber) getIntent().getSerializableExtra("class");

        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#352354")));
        getActionBar().setTitle(haber.getHtitle());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvHaberDetailTitle = (TextView) findViewById(R.id.tvHaberDetailTitle);
        tvHaberDetailTitle.setText(haber.getHtitle());

        TextView tvHaberDetailRelatedInfo = (TextView) findViewById(R.id.tvHaberDetailRelatedInfo);
        tvHaberDetailRelatedInfo.setText(haber.getHdate() + " - " + haber.getDname());


        WebView mWebView = (WebView) findViewById(R.id.webview);
        mWebView.loadData(haber.getHcontent(), "text/html; charset=UTF-8", null);
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