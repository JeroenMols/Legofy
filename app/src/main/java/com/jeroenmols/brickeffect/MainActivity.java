package com.jeroenmols.brickeffect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 1;
    private static final int AMOUNT_OF_BRICKS_IN_WIDTH = 40;
    public static final int ANIM_DURATION_MS = 300;

    private ImageView image;
    private ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.imageview_main);
        thumbnail = (ImageView) findViewById(R.id.imageview_thumbnail);
        initializeThumbnailClickListeners();
        setImageResource(R.drawable.profile_picture);
    }

    private void initializeThumbnailClickListeners() {
        addThumbnailClickListener(R.id.imageview_profile, R.drawable.profile_picture);
        addThumbnailClickListener(R.id.imageview_monalise, R.drawable.mona_lisa);
        addThumbnailClickListener(R.id.imageview_magritte, R.drawable.magritte);
        addSelectImageClickListener(R.id.imageview_plusbutton);
    }

    private void addThumbnailClickListener(@IdRes int viewId, @DrawableRes final int drawable) {
        findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageResource(drawable);
            }
        });
    }

    private void addSelectImageClickListener(@IdRes int viewId) {
        findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void setImageResource(@DrawableRes int image) {
        setImageBitmap(BitmapFactory.decodeResource(getResources(), image));
    }

    private void setImageBitmap(Bitmap bitmap) {
        EffectDrawable customDrawable = new EffectDrawable(bitmap);
        this.image.setImageDrawable(customDrawable);
        customDrawable.applyEffect(new LegofyEffect(getResources(), AMOUNT_OF_BRICKS_IN_WIDTH, ANIM_DURATION_MS));
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            thumbnail.setImageURI(fullPhotoUri);

            setImageBitmap(loadScaledBitmap(fullPhotoUri));
        }
    }

    private Bitmap loadScaledBitmap(Uri uri) {
        FileDescriptor photo = getFileDescriptor(uri);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(photo, null, bmOptions);

        int scaleInt = getScalingFactor(bmOptions.outWidth, bmOptions.outHeight);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleInt;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFileDescriptor(photo, null, bmOptions);
    }

    private FileDescriptor getFileDescriptor(Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return parcelFileDescriptor.getFileDescriptor();
    }

    private int getScalingFactor(int outWidth, int outHeight) {
        float scaleFactor = Math.min(((float) outWidth) / image.getWidth(), ((float) outHeight) / image.getHeight());
        int scaleInt = (int) scaleFactor;
        if (scaleFactor > ((int) scaleFactor) + 0.7) {
            scaleInt = (int) Math.ceil(scaleFactor);
        }
        return scaleInt;
    }
}

