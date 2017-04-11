/*
 *  Copyright 2016 Jeroen Mols
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

    private int requestedBricksInWidth = DEFAULT_AMOUNTOFBRICKS;

    public static Legofy with(Context context) {
        if (context == null) {
            throw new RuntimeException("Context must not be null");
        }
        return new Legofy(context.getApplicationContext());
    }

    public Legofy amountOfBricks(int minimumBricks) {
        requestedBricksInWidth = minimumBricks;
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
        int bricksInWidth = getBricksInWidth(bitmap, brickSize);
        int bricksInHeight = getBricksInHeight(bitmap, brickSize);

        return createLegofiedBitmap(bitmap, brickSize, bricksInWidth, bricksInHeight);
    }

    private Bitmap createLegofiedBitmap(Bitmap bitmap, int brickSize, int bricksInWidth, int bricksInHeight) {
        Bitmap outputBitmap = createOutputBitmap(bricksInWidth, bricksInHeight, brickSize);
        brickDrawer.setBitmap(context.getResources(), outputBitmap, brickSize);
        drawAllBricks(bitmap, bricksInWidth, bricksInHeight, brickSize);
        return outputBitmap;
    }

    private Bitmap createOutputBitmap(int bricksInWidth, int bricksInHeight, int actualBrickSize) {
        int width = bricksInWidth * actualBrickSize;
        int height = bricksInHeight * actualBrickSize;

        return bitmapWrapper.createBitmap(width, height, Bitmap.Config.ARGB_4444);
    }

    private int getActualBrickSize(Bitmap bitmap) {
        return Math.max(getRequestedBrickSize(bitmap), MINIMUM_BRICKSIZE);
    }

    private int getRequestedBrickSize(Bitmap bitmap) {
        float scaleFactor = getDownScaleFactorToLimitOutputSize(bitmap);
        return (int) (bitmap.getWidth() * scaleFactor) / getBricksInWidth();
    }

    private int getBricksInWidth(Bitmap bitmap, int actualBrickSize) {
        float upscaleFactor = getUpScaleFactorToFitRequestedBricks(bitmap);
        float downScaleFactor = getDownScaleFactorToLimitOutputSize(bitmap);
        return (int) (downScaleFactor * upscaleFactor * bitmap.getWidth() / actualBrickSize);
    }

    private int getBricksInHeight(Bitmap bitmap, int actualBrickSize) {
        float upscaleFactor = getUpScaleFactorToFitRequestedBricks(bitmap);
        float downScaleFactor = getDownScaleFactorToLimitOutputSize(bitmap);
        return (int) (downScaleFactor * upscaleFactor * bitmap.getHeight() / actualBrickSize);
    }
    private float getUpScaleFactorToFitRequestedBricks(Bitmap bitmap) {
        float scaleToFitAllBricks = ((float) MINIMUM_BRICKSIZE) / getRequestedBrickSize(bitmap);
        float maxScaleX = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getWidth();
        float maxScaleY = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getHeight();
        float scale = Math.min(scaleToFitAllBricks, Math.min(maxScaleX, maxScaleY));
        return Math.max(DEFAULT_SCALE, scale);
    }

    private float getDownScaleFactorToLimitOutputSize(Bitmap bitmap) {
        float scaleX = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getWidth();
        float scaleY = ((float) DEFAULT_MAXOUTPUTSIZE) / bitmap.getHeight();
        float scale = Math.min(scaleX, scaleY);
        return Math.min(scale, DEFAULT_SCALE);
    }

    public int getBricksInWidth() {
        int maxBricks = DEFAULT_MAXOUTPUTSIZE / MINIMUM_BRICKSIZE;
        return Math.min(requestedBricksInWidth, maxBricks);
    }

    private void drawAllBricks(Bitmap inputBitmap, int bricksInWidth, int bricksInHeight, int brickSize) {
        int amountOfBricks = bricksInWidth * bricksInHeight;
        Bitmap scaledBitmap = bitmapWrapper.createScaledBitmap(inputBitmap, bricksInWidth, bricksInHeight, true);
        for (int i = 0; i < amountOfBricks; i++) {
            int posX = i % bricksInWidth;
            int posY = i / bricksInWidth;
            int color = scaledBitmap.getPixel(posX, posY);
            brickDrawer.drawBrick(color, posX * brickSize, posY * brickSize);
        }
    }
}
