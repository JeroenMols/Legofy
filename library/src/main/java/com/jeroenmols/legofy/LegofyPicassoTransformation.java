package com.jeroenmols.legofy;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * @author Jeroen Mols on 27/04/16.
 */
public class LegofyPicassoTransformation implements Transformation {

    private final Context applicationContext;

    public LegofyPicassoTransformation(Context context) {
        applicationContext = context.getApplicationContext();
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap legofiedBitmap = Legofy.with(applicationContext).convert(source);
        source.recycle();
        return legofiedBitmap;
    }

    @Override
    public String key() {
        return null;
    }
}
