package fr.develop.android.tbrnx.musicvibe.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import fr.develop.android.tbrnx.musicvibe.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

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
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        Handler mHandler = new Handler();


        mHandler.postDelayed(mLaunchTask, MYDELAYTIME);
    }
}
