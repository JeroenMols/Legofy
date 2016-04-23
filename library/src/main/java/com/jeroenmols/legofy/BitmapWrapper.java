package com.jeroenmols.legofy;

import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class BitmapWrapper {

    public Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        return Bitmap.createBitmap(width, height, config);
    }
}
