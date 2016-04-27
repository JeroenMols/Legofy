package com.jeroenmols.legofy;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * @author Jeroen Mols on 27/04/16.
 */
public class LegofyPicassoTransformation implements Transformation {

    public LegofyPicassoTransformation(Context context) {
        context.getApplicationContext();
    }

    @Override
    public Bitmap transform(Bitmap source) {
        return null;
    }

    @Override
    public String key() {
        return null;
    }
}
