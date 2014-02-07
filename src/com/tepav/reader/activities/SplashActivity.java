package com.tepav.reader.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.tepav.reader.R;
import com.tepav.reader.delegates.HaberServiceDelegate;
import com.tepav.reader.services.BaseRequestService;
import com.tepav.reader.services.HaberService;

import java.util.Map;

public class SplashActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainScreenActivity.class));
                finish();
            }
        }, 1500);
    }

}
