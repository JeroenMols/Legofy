package com.jeroenmols.legofy;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class Legofy {

    public static final int MINIMUM_BRICKSIZE = 20;
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
        Bitmap outputBitmap = createOutputBitmap(bitmap);
        int brickSize = Math.max(getBrickSize(bitmap), MINIMUM_BRICKSIZE);

        brickDrawer.setBitmap(context.getResources(), outputBitmap, brickSize);
        drawAllBricks(bitmap, outputBitmap, brickSize);

        return outputBitmap;
    }

    private Bitmap createOutputBitmap(Bitmap bitmap) {
        int brickSize = getBrickSize(bitmap);
        int actualBrickSize = Math.max(brickSize, MINIMUM_BRICKSIZE);

        int bricksInWidth;
        if (brickSize < MINIMUM_BRICKSIZE) {
            float upscaleFactor = ((float) MINIMUM_BRICKSIZE) / brickSize;
            bricksInWidth = (int) (upscaleFactor * bitmap.getWidth() / actualBrickSize);
        } else {
            float downScaleFactor = getDownScaleFactor(bitmap);
            bricksInWidth = (int) (downScaleFactor * bitmap.getWidth() / actualBrickSize);
        }

        int bricksInHeight;
        if (brickSize < MINIMUM_BRICKSIZE) {
            float upscaleFactor = ((float) MINIMUM_BRICKSIZE) / brickSize;
            bricksInHeight = (int) (upscaleFactor * bitmap.getHeight() / actualBrickSize);
        } else {
            float downScaleFactor = getDownScaleFactor(bitmap);
            bricksInHeight = (int) (downScaleFactor * bitmap.getHeight() / actualBrickSize);
        }

        int width = bricksInWidth * actualBrickSize;
        int height = bricksInHeight * actualBrickSize;

        return bitmapWrapper.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    }

    private int getBrickSize(Bitmap bitmap) {
        float scaleFactor = getDownScaleFactor(bitmap);
        return (int) (bitmap.getWidth() * scaleFactor) / getBricksInWidth();
    }

    private int getAmountOfBricks(int brickSize, Bitmap processedBitmap) {
        return processedBitmap.getWidth() * processedBitmap.getHeight() / brickSize / brickSize;
    }

    private float getDownScaleFactor(Bitmap bitmap) {
        float scaleX = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getWidth();
        float scaleY = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getHeight();
        float scale = Math.min(scaleX, scaleY);
        return Math.min(scale, DEFAULT_SCALE);
    }

    public int getBricksInWidth() {
        return Math.min(bricksInWidth, DEFAULT_MAXOUTPUTSIZE / MINIMUM_BRICKSIZE);
    }

    private void drawAllBricks(Bitmap inputBitmap, Bitmap outputBitmap, int brickSize) {
        int bricksInWidth = getBricksInWidth();
        int amountOfBricks = getAmountOfBricks(brickSize, outputBitmap);
        Bitmap scaledBitmap = bitmapWrapper.createScaledBitmap(inputBitmap, bricksInWidth, amountOfBricks / bricksInWidth, true);
        for (int i = 0; i < amountOfBricks; i++) {
            int posX = i % bricksInWidth;
            int posY = i / bricksInWidth;
            int color = scaledBitmap.getPixel(posX, posY);
            brickDrawer.drawBrick(color, posX * brickSize, posY * brickSize);
        }
    }
}
