package com.jeroenmols.brickeffect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jeroenmols.legofy.Legofy;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

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

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });
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
        Bitmap convert = Legofy.with(this).amountOfBricks(40).convert(bitmap);
        this.image.setImageBitmap(convert);
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

    private void shareImage() {
        File savedImage = saveBitmapToCache();
        Uri contentUri = FileProvider.getUriForFile(this, "com.jeroenmols.brickeffect.fileprovider", savedImage);

        if (contentUri != null) {
            grantUriPermission(getPackageName(), contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent shareIntent = new Intent();
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    private File saveBitmapToCache() {
        File cachePath = new File(getCacheDir(), "images");
        cachePath.mkdirs();
        File imageFile = new File(cachePath, String.format("%s.jpg", UUID.randomUUID().toString()));

        try {
            FileOutputStream stream = new FileOutputStream(imageFile);
            Bitmap effectBitmap = ((EffectDrawable) image.getDrawable()).getEffectBitmap();
            effectBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}

