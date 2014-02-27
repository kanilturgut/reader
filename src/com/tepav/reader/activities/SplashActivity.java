package com.tepav.reader.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.tepav.reader.R;
import com.tepav.reader.repositories.BaseDao;
import com.tepav.reader.services.LoginRegisterService;
import com.tepav.reader.utils.Util;

public class SplashActivity extends Activity {

    public static SharedPreferences languagePreferences;
    private final String sharedFileName = "firstTimeController";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        languagePreferences = getSharedPreferences(sharedFileName, MODE_PRIVATE);
        if (languagePreferences.getBoolean("isThisFirstTime", true)) {
            //it means, this is the first launch of application
            try {
                //force system language to Turkish
                Util.changeLocale(SplashActivity.this, "tr");

                SharedPreferences.Editor editor = languagePreferences.edit();
                editor.putBoolean("isThisFirstTime", false);
                editor.putString("language", "TR");
                editor.commit();
            } catch (Exception e) {
                Log.e("SplashActivity", "System language couldn't change to Turkish", e);
            }
        }

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
