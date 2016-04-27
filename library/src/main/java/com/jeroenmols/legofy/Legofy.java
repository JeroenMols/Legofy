package com.jeroenmols.legofy;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class Legofy {

    public static final int DEFAULT_AMOUNTOFBRICKS = 20;
    public static final int DEFAULT_MAXOUTPUTSIZE = 1080;
    public static final int DEFAULT_SCALE = 1;

    private Context context;
    private BrickDrawer brickDrawer;
    private final BitmapWrapper bitmapWrapper;

    private int bricksInWidth = DEFAULT_AMOUNTOFBRICKS;

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

    protected Legofy(Context context, BitmapWrapper bitmapWrapper, BrickDrawer brickDrawer) {
        this.context = context;
        this.bitmapWrapper = bitmapWrapper;
        this.brickDrawer = brickDrawer;
    }

    private Legofy(Context context) {
        this(context, new BitmapWrapper(), new BrickDrawer());
    }

    public Bitmap convert(Bitmap bitmap) {
        int brickSize = getBrickSize(bitmap);
        Bitmap outputBitmap = createBitmapForBrickSize(bitmap, brickSize);

        brickDrawer.setBitmap(context.getResources(), outputBitmap, brickSize);
        drawAllBricks(bitmap, brickSize, outputBitmap);

        return outputBitmap;
    }

    private int getBrickSize(Bitmap bitmap) {
        float scaleFactor = getScaleFactor(bitmap);
        return (int) (bitmap.getWidth() * scaleFactor) / bricksInWidth;
    }

    private int getAmountOfBricks(int brickSize, Bitmap processedBitmap) {
        return processedBitmap.getWidth() * processedBitmap.getHeight() / brickSize / brickSize;
    }

    private float getScaleFactor(Bitmap bitmap) {
        float scaleX = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getWidth();
        float scaleY = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getHeight();
        float scale = Math.min(scaleX, scaleY);
        return Math.min(scale, DEFAULT_SCALE);
    }

    private Bitmap createBitmapForBrickSize(Bitmap bitmap, int brickSize) {
        int width = brickSize * bricksInWidth;
        int height = ((int) (bitmap.getHeight() * getScaleFactor(bitmap)) / brickSize) * brickSize;
        return bitmapWrapper.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    }

    private void drawAllBricks(Bitmap outputBitmap, int brickSize, Bitmap processedBitmap) {
        int amountOfBricks = getAmountOfBricks(brickSize, processedBitmap);
        Bitmap scaledBitmap = bitmapWrapper.createScaledBitmap(outputBitmap, bricksInWidth, amountOfBricks / bricksInWidth, true);
        for (int i = 0; i < amountOfBricks; i++) {
            int posX = i % bricksInWidth;
            int posY = i / bricksInWidth;
            int color = scaledBitmap.getPixel(posX, posY);
            brickDrawer.drawBrick(color, posX * brickSize, posY * brickSize);
        }
    }
}
