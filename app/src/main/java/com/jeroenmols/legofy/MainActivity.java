package com.jeroenmols.legofy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private static final int GRANULARITY = 64;
    private ImageView image;
    private SeekBar seekbar;
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture);
            LegofyDrawable legofyDrawable = new LegofyDrawable(getResources(), bitmap);
            image.setImageDrawable(legofyDrawable);
            legofyDrawable.createLegoFiedBitmap(getResources(), seekbar.getProgress(), true);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //NOP
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //NOP
        }
    };
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageview_main);
        seekbar = (SeekBar) findViewById(R.id.seekbar_main_bricksize);
        seekbar.setProgress(GRANULARITY);
        seekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        Button button = (Button) findViewById(R.id.button_main_highres);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture);
                LegofyDrawable legofyDrawable = new LegofyDrawable(getResources(), bitmap);
                image.setImageDrawable(legofyDrawable);
                legofyDrawable.createLegoFiedBitmap(getResources(), seekbar.getProgress(), false);
            }
        });

//        image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture));

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
        customDrawable.applyEffect(new LegofyEffect(getResources(), 64, 300));
    }
}

