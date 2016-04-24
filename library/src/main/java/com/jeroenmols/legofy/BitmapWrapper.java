package com.jeroenmols.legofy;

import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class BitmapWrapper {

    public Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        return Bitmap.createBitmap(width, height, config);
    }

    public Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter);
    }
}
