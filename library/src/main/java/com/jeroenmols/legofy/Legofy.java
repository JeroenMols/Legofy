package com.jeroenmols.legofy;

import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class Legofy {

    private final int bricksInWidth;
    private final BitmapWrapper bitmapWrapper;

    public Legofy(int bricksInWidth) {
        this(new BitmapWrapper(), new DrawingWrapper(), bricksInWidth);
    }

    protected Legofy(BitmapWrapper bitmapWrapper, DrawingWrapper drawingWrapper, int bricksInWidth) {
        this.bitmapWrapper = bitmapWrapper;
        this.bricksInWidth = bricksInWidth;
    }

    public Bitmap processBitmap(Bitmap bitmap) {
        int width = (bitmap.getWidth() / bricksInWidth) * bricksInWidth;
        int height = (bitmap.getHeight() / bricksInWidth) * bricksInWidth;
        Bitmap processedBitmap = bitmapWrapper.createBitmap(width, height, null);
        return processedBitmap;
    }
}
