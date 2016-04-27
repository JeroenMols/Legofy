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
        int brickSize = getActualBrickSize(bitmap);
        Bitmap outputBitmap = createOutputBitmap(bitmap, brickSize);

        brickDrawer.setBitmap(context.getResources(), outputBitmap, brickSize);
        drawAllBricks(bitmap, outputBitmap, brickSize);

        return outputBitmap;
    }

    private Bitmap createOutputBitmap(Bitmap bitmap, int actualBrickSize) {
        int bricksInWidth = getBricksInWidth(bitmap, actualBrickSize);
        int bricksInHeight = getBricksInHeight(bitmap, actualBrickSize);

        int width = bricksInWidth * actualBrickSize;
        int height = bricksInHeight * actualBrickSize;

        return bitmapWrapper.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    }

    private int getActualBrickSize(Bitmap bitmap) {
        return Math.max(getRequestedBrickSize(bitmap), MINIMUM_BRICKSIZE);
    }

    private int getBricksInWidth(Bitmap bitmap, int actualBrickSize) {
        float upscaleFactor = getUpScaleFactor(bitmap);
        float downScaleFactor = getDownScaleFactor(bitmap);
        return (int) (downScaleFactor * upscaleFactor * bitmap.getWidth() / actualBrickSize);
    }

    private int getBricksInHeight(Bitmap bitmap, int actualBrickSize) {
        float upscaleFactor = getUpScaleFactor(bitmap);
        float downScaleFactor = getDownScaleFactor(bitmap);
        return (int) (downScaleFactor * upscaleFactor * bitmap.getHeight() / actualBrickSize);
    }

    private int getRequestedBrickSize(Bitmap bitmap) {
        float scaleFactor = getDownScaleFactor(bitmap);
        return (int) (bitmap.getWidth() * scaleFactor) / getBricksInWidth();
    }

    private int getAmountOfBricks(int brickSize, Bitmap processedBitmap) {
        return processedBitmap.getWidth() * processedBitmap.getHeight() / brickSize / brickSize;
    }

    // Upscaling needed to make bricks fit on image
    private float getUpScaleFactor(Bitmap bitmap) {
        float scaleToFitAllBricks = ((float) MINIMUM_BRICKSIZE) / getRequestedBrickSize(bitmap);
        float maxScaleY = DEFAULT_MAXOUTPUTSIZE / bitmap.getHeight();
        float scale = Math.min(scaleToFitAllBricks, maxScaleY);
        return Math.max(DEFAULT_SCALE, scale);
    }

    // Downscaling needed to avoid out of memory exceptions
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
