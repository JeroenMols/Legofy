package com.jeroenmols.legofy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class Legofy {

    public static final int DEFAULT_AMOUNTOFBRICKS = 20;

    private int bricksInWidth = DEFAULT_AMOUNTOFBRICKS;
    private BrickDrawer brickDrawer;
    private final BitmapWrapper bitmapWrapper;

    public static Legofy with(Context context) {
        if (context == null) {
            throw new RuntimeException("Context must not be null");
        }
        return new Legofy(context.getApplicationContext());
    }

    public Legofy amountOfBricks(int minimumBricks) {
        bricksInWidth = minimumBricks;
        return this;
    }

    protected Legofy(BitmapWrapper bitmapWrapper, BrickDrawer brickDrawer) {
        this.bitmapWrapper = bitmapWrapper;
        this.brickDrawer = brickDrawer;
    }

    private Legofy(Context context) {
        this(new BitmapWrapper(), new BrickDrawer());
    }

    public Bitmap processBitmap(Resources resources, Bitmap bitmap) {
        int brickSize = bitmap.getWidth() / bricksInWidth;

        Bitmap processedBitmap = createBitmapForBrickSize(bitmap, brickSize);
        brickDrawer.setBitmap(resources, processedBitmap, brickSize);

        int amountOfBricks = processedBitmap.getWidth() * processedBitmap.getHeight() / brickSize / brickSize;
        Bitmap scaledBitmap = bitmapWrapper.createScaledBitmap(bitmap, bricksInWidth, amountOfBricks / bricksInWidth, true);
        for (int i = 0; i < amountOfBricks; i++) {
            int posX = i % bricksInWidth;
            int posY = i / bricksInWidth;

            brickDrawer.drawBrick(scaledBitmap.getPixel(posX, posY), posX * brickSize, posY * brickSize);
        }

        return processedBitmap;
    }

    private Bitmap createBitmapForBrickSize(Bitmap bitmap, int brickSize) {
        int width = brickSize * bricksInWidth;
        int height = (bitmap.getHeight() / brickSize) * brickSize;
        return bitmapWrapper.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    }
}
