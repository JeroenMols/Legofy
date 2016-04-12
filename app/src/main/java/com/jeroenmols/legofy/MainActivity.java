package com.jeroenmols.legofy;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int GRANULARITY = 64;
    public static final int ANIM_DURATION_MS = 300;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageview_main);

        addImageClickListener(R.id.imageview_profile, R.drawable.profile_picture);
        addImageClickListener(R.id.imageview_monalise, R.drawable.mona_lisa);
        addImageClickListener(R.id.imageview_magritte, R.drawable.magritte);
        addImageClickListener(R.id.imageview_android, R.drawable.android);

        setImageResource(R.drawable.profile_picture);
    }

    private void addImageClickListener(@IdRes int viewId, @DrawableRes final int drawable) {
        findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageResource(drawable);
            }
        });
    }

    private void setImageResource(@DrawableRes int image) {
        EffectDrawable customDrawable = new EffectDrawable(BitmapFactory.decodeResource(getResources(), image));
        this.image.setImageDrawable(customDrawable);
        customDrawable.applyEffect(new LegofyEffect(getResources(), GRANULARITY, ANIM_DURATION_MS));
    }
}

