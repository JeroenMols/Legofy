package com.jeroenmols.legofy;

import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class Legofy {

    private final int bricksInWidth;

    public Legofy(BitmapWrapper mock, int bricksInWidth) {
        this.bricksInWidth = bricksInWidth;
    }

    public Bitmap processBitmap(Bitmap bitmap) {
        return bitmap;
    }
}
