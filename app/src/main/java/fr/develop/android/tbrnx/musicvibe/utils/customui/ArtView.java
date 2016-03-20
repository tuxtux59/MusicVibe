package fr.develop.android.tbrnx.musicvibe.utils.customui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import fr.develop.android.tbrnx.musicvibe.R;
import fr.develop.android.tbrnx.musicvibe.activities.TrackActivity;
import fr.develop.android.tbrnx.musicvibe.utils.Utils;

/**
 * Created by tbrnx on 18/03/16.
 */
public class ArtView extends LinearLayout {
    private Context context;
    private UbuntuTextView trackName;
    private UbuntuTextView trackDetails;
    private ImageView trackArt;

    public ArtView(Context context) {
        super(context);
        init(context);
    }

    public ArtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.art_layout, this, true);
        trackName = (UbuntuTextView) findViewById(R.id.track_name);
        trackDetails = (UbuntuTextView) findViewById(R.id.track_details);
        trackArt = (ImageView) findViewById(R.id.track_art);
    }


    public ArtView setTrack(final Activity activity, String id, String artist, String title,
                            final String data,
                            String dislay_name, String duration, JSONObject first) {

        Log.d("title", title);
        trackName.setText(Utils.flattenToAscii(title));
        String art = null;
        if(first != null && first.has("artworkUrl100")){
            try {
                art = first.getString("artworkUrl100").replace("100x100", "300x300");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(Integer.parseInt(duration) == 0 && first.has("trackTimeMillis")){
            try {
                duration = String.valueOf(first.getLong("trackTimeMillis"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        trackDetails.setText(String.format("%s - %s", artist, Utils.formatToDuration(Long.parseLong(duration))));
        Glide.with(activity).load(art)
                .error(R.drawable.default_cover_art)
                .centerCrop()
                .into(trackArt);

        final String finalArt = art;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cursor", data);
                Intent intent = new Intent(context, TrackActivity.class);
                intent.putExtra(Utils.TRACK,String.format("file://%s", data) );
                intent.putExtra(Utils.ART_URL, finalArt);
                activity.startActivity(intent);
//                Intent intent = new Intent();
//                intent.setAction(android.content.Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(String.format("file://%s", data)),
//                        "audio/*");
//                final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
//                if (componentName != null) {
//                    try {
//                        activity.startActivityForResult(intent, 10);
//                    } catch (Exception ex) {
//                        // Notify the user?
//                    }
//                }

            }
        });

        return this;
    }
}
