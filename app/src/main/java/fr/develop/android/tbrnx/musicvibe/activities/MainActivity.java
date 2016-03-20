package fr.develop.android.tbrnx.musicvibe.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.develop.android.tbrnx.musicvibe.R;
import fr.develop.android.tbrnx.musicvibe.utils.Utils;
import fr.develop.android.tbrnx.musicvibe.utils.customui.ArtView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GridLayout gridView;
    private ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gridView = (GridLayout) findViewById(R.id.gridLayout);
        scrollview = (ScrollView) findViewById(R.id.mScrollview);

        scrollview.setSmoothScrollingEnabled(true);
        listMusicFiles();

    }

    private void listMusicFiles() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Log.e("selection", selection);

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };


        Cursor cursor = this.managedQuery(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.ALBUM);


        List<String> songs = new ArrayList<String>();
        while(cursor.moveToNext()) {
            String id = cursor.getString(0);
            String artist = cursor.getString(1);
            String title = cursor.getString(2);
            String data = cursor.getString(3);
            String dislay_name = cursor.getString(4);
            String duration = cursor.getString(5);
            songs.add(id + "||"
                    + artist + "||"
                    + title + "||"
                    + data + "||"
                    + dislay_name + "||"
                    + duration);
            GetArtPicTask task = new GetArtPicTask(id, artist, title, data, dislay_name, duration);
            task.execute();

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.media_route_menu_item) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class GetArtPicTask extends AsyncTask<Void, Void, JSONObject> {

        private final String id;
        private final String dislay_name;
        private final String duration;
        private String artist;
        private String title;
        private String data;


        public GetArtPicTask(String id, String artist, String title, String data, String dislay_name, String duration) {
            this.id = id;
            this.data = data;
            this.dislay_name = dislay_name;
            this.duration = duration;
            this.artist = artist;
            this.title = title;
        }

        private String replaceSpaces(String string) {
            return Utils.flattenToAscii(string.replace(" ", "+"));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection connection = null;
            try {

                String urlPathJSON = Uri.parse(String.format("https://itunes.apple.com/search?term=%s+%s&limit=1", replaceSpaces(artist), replaceSpaces(title))).toString();
                URL url;

                url = new URL(urlPathJSON);
                Log.d("url asked", urlPathJSON);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                    String line;
                    StringBuilder sb = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    br.close();
                    Log.d("campaign", sb.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        if(jsonObject.has("results")){
                            JSONArray results = jsonObject.getJSONArray("results");
                            if( results != null && results.length() > 0){
                                JSONObject first = results.getJSONObject(0);
                                return first;
//                                if(first != null && first.has("artworkUrl100")){
//                                    return first.getString("artworkUrl100").replace("100x100","300x300");
//                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("response code", String.valueOf(connection.getResponseCode()));
                    return null;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException ignored) {

                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            gridView.addView(new ArtView(getApplicationContext())
                    .setTrack(MainActivity.this, id, artist, title, data, dislay_name, duration, json));
        }
    }
}
