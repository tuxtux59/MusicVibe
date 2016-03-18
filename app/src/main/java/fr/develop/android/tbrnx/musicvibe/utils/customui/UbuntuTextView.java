package fr.develop.android.tbrnx.musicvibe.utils.customui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by tbrnx on 18/03/16.
 */
public class UbuntuTextView extends TextView {
    public UbuntuTextView(Context context) {
        super(context);
        init();
    }

    public UbuntuTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UbuntuTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface face= Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu.ttf");
        setTypeface(face);
    }
}
