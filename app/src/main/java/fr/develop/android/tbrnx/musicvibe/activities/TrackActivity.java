package fr.develop.android.tbrnx.musicvibe.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import fr.develop.android.tbrnx.musicvibe.R;
import fr.develop.android.tbrnx.musicvibe.utils.Utils;

public class TrackActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private ProgressBar track_progress;
    ImageView art_cover;

    private Handler mHandler = new Handler();
    boolean playing = false;
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if(playing){
                track_progress.setProgress(mediaPlayer.getCurrentPosition());
                mHandler.postDelayed(this, 100);
            }else {

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        track_progress = (ProgressBar) findViewById(R.id.track_progress);
        art_cover = (ImageView) findViewById(R.id.art_cover);

        Intent income = getIntent();
        if (income.hasExtra(Utils.TRACK)) {
            String track = income.getStringExtra(Utils.TRACK);
            Log.d("track", track);
            initPlayer(track);
            initUi(income);
        }else {
            finish();
        }
    }

    private void initUi(Intent income) {
        if(income.hasExtra(Utils.ART_URL)){
            String art_url = income.getStringExtra(Utils.ART_URL);
            Glide.with(this).load(art_url).error(R.drawable.default_cover_art).into(art_cover);
        }
    }

    private void initPlayer(String track) {
        Uri uri = Uri.parse(track);
        Log.d("uri", uri.toString());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e("prepared", "prepared!");

                    track_progress.setMax(mp.getDuration());
                    mediaPlayer.start();
                    playing = true;
                    runOnUiThread(mUpdateTimeTask);
                    mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                            Log.d("progress", String.valueOf(percent));
                            track_progress.setProgress(percent);
                        }
                    });
                }
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //temporary code
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mHandler.removeCallbacks(mUpdateTimeTask);
        }
    }
}
