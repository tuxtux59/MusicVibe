package fr.develop.android.tbrnx.musicvibe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import fr.develop.android.tbrnx.musicvibe.R;

public class SplashScreen extends Activity {

    private static final long MYDELAYTIME = 2000;
    private Runnable mLaunchTask = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler mHandler = new Handler();


        mHandler.postDelayed(mLaunchTask, MYDELAYTIME);
    }
}
