package com.tepav.reader.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.tepav.reader.R;
import com.tepav.reader.repositories.BaseDao;
import com.tepav.reader.services.LoginRegisterService;

public class SplashActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        BaseDao.saveApplicationContext(this);
        LoginRegisterService loginRegisterService = LoginRegisterService.getInstance();
        loginRegisterService.login("test@test.com" , "test");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainScreenActivity.class));
                finish();
            }
        }, 500);
    }

}
