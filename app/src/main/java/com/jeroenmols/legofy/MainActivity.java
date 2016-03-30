package com.jeroenmols.legofy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = (ImageView) findViewById(R.id.imageview_main);

        int granularity = 64;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flower);
        Bitmap brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick);
        Bitmap scaledBrick = Bitmap.createScaledBitmap(brick, granularity, granularity, false);

        int imageWidth = bitmap.getWidth() / granularity;
        int imageHeight = bitmap.getHeight() / granularity;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, false);

        Bitmap legofied = Bitmap.createBitmap(imageWidth * granularity, imageHeight * granularity, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(legofied);
        Paint paint = new Paint();

        for (int i = 0; i < scaledBitmap.getWidth(); i++) {
            for (int j = 0; j < scaledBitmap.getHeight(); j++) {
                paint.setColorFilter(new PorterDuffColorFilter(scaledBitmap.getPixel(i, j), PorterDuff.Mode.OVERLAY));
                canvas.drawBitmap(scaledBrick, i * granularity, j * granularity, paint);
            }
        }


        image.setImageBitmap(legofied);
    }
}

