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
        float scaleFactor = createScaleFactor(bitmap);
        int brickSize = (int) (bitmap.getWidth() * scaleFactor) / bricksInWidth;

        Bitmap processedBitmap = createBitmapForBrickSize(bitmap, brickSize);
        brickDrawer.setBitmap(context.getResources(), processedBitmap, brickSize);

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
        int height = ((int) (bitmap.getHeight() * createScaleFactor(bitmap)) / brickSize) * brickSize;
        return bitmapWrapper.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    }

    private float createScaleFactor(Bitmap bitmap) {
        float scaleX = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getWidth();
        float scaleY = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getHeight();
        float scale = Math.min(scaleX, scaleY);
        return Math.min(scale, DEFAULT_SCALE);
    }
}
