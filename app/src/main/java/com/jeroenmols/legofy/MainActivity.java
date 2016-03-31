package com.jeroenmols.legofy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
            Bitmap legoFiedBitmap = createLegoFiedBitmap(progress, true);
            image.setImageBitmap(legoFiedBitmap);
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
                Bitmap legoFiedBitmap = createLegoFiedBitmap(seekbar.getProgress(), false);
                image.setImageBitmap(legoFiedBitmap);
            }
        });

        Bitmap legofied = createLegoFiedBitmap(seekbar.getProgress(), true);
        image.setImageBitmap(legofied);
    }

    @NonNull
    private Bitmap createLegoFiedBitmap(int granularity, boolean fastmode) {
        if (granularity <= 0) {
            granularity = 1;
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_picture);
        Bitmap brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        Bitmap scaledBrick = Bitmap.createScaledBitmap(brick, granularity, granularity, false);

        int imageWidth = bitmap.getWidth() / granularity;
        int imageHeight = bitmap.getHeight() / granularity;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, true);

        Bitmap legofied = Bitmap.createBitmap(imageWidth * granularity, imageHeight * granularity, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(legofied);
        Paint paint = new Paint();

        for (int i = 0; i < scaledBitmap.getWidth(); i++) {
            for (int j = 0; j < scaledBitmap.getHeight(); j++) {
                if (fastmode) {
                    paint.setColorFilter(new PorterDuffColorFilter(scaledBitmap.getPixel(i, j), PorterDuff.Mode.OVERLAY));
                } else {
                    paint.setColorFilter(new PorterDuffColorFilter(getColor(bitmap, i, j, granularity), PorterDuff.Mode.OVERLAY));
                }
                canvas.drawBitmap(scaledBrick, i * granularity, j * granularity, paint);
            }
        }
        return legofied;
    }

    public int getColor(Bitmap source, int x, int y, int granularity) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int i = 0; i < granularity; i++) {
            for (int j = 0; j < granularity; j++) {
                int color = source.getPixel(granularity * x + i, granularity * y + j);
                red += Color.red(color);
                green += Color.green(color);
                blue += Color.blue(color);
            }
        }
        return Color.rgb(red / (granularity * granularity), green / (granularity * granularity), blue / (granularity * granularity));
    }
}

