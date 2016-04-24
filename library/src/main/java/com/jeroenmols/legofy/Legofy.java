package com.jeroenmols.legofy;

import android.content.res.Resources;
import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class Legofy {

    private final int bricksInWidth;
    private BrickDrawer brickDrawer;
    private final BitmapWrapper bitmapWrapper;

    public Legofy(int bricksInWidth) {
        this(new BitmapWrapper(), new BrickDrawer(), bricksInWidth);
    }

    protected Legofy(BitmapWrapper bitmapWrapper, BrickDrawer brickDrawer, int bricksInWidth) {
        this.bitmapWrapper = bitmapWrapper;
        this.brickDrawer = brickDrawer;
        this.bricksInWidth = bricksInWidth;
    }

    public Bitmap processBitmap(Resources resources, Bitmap bitmap) {
        int brickSize = bitmap.getWidth() / bricksInWidth;

        Bitmap processedBitmap = createBitmapForBrickSize(bitmap, brickSize);
        brickDrawer.setBitmap(resources, processedBitmap, brickSize);

        int amountOfBricks = processedBitmap.getWidth() * processedBitmap.getHeight() / brickSize / brickSize;
        for (int i = 0; i < amountOfBricks; i++) {
            int posX = i * brickSize % processedBitmap.getWidth();

            brickDrawer.drawBrick(0, posX, 0, brickSize);
        }

        return processedBitmap;
    }

    private Bitmap createBitmapForBrickSize(Bitmap bitmap, int brickSize) {
        int width = brickSize * bricksInWidth;
        int height = (bitmap.getHeight() / brickSize) * brickSize;
        return bitmapWrapper.createBitmap(width, height, null);
    }
}
