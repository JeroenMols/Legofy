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

package com.jeroenmols.brickeffect.effect;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.jeroenmols.brickeffect.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jeroen Mols on 11/04/16.
 */
public class LegofyEffect implements Effect {

    private final int duration;
    private final Resources resources;
    private int bricksPerFrame;
    private int bricksPerWidth = 20;

    private Bitmap baseBitmap;
    private Bitmap legofiedBitmap;
    private Bitmap brickBitmap;
    private int amountOfBricks;
    private Canvas canvas;
    private Paint paint;

    private List<Integer> positions;
    private int currentPosition;

    public LegofyEffect(Resources resources, int bricksPerWidth, int duration) {
        if (bricksPerWidth > 0) {
            this.bricksPerWidth = bricksPerWidth;
        }
        this.resources = resources;
        this.duration = duration;
    }

    @Override
    public void initialize(Bitmap bitmap) {
        int brickSize = bitmap.getWidth() / bricksPerWidth;

        int imageWidth = bricksPerWidth;
        int imageHeight = (int) (((float) bitmap.getHeight()) / bitmap.getWidth() * bricksPerWidth);

        brickBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.brick), brickSize, brickSize, false);


        baseBitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, true);
        legofiedBitmap = Bitmap.createBitmap(imageWidth * brickSize, imageHeight * brickSize, Bitmap.Config.ARGB_4444);
        amountOfBricks = imageWidth * imageHeight;

        canvas = new Canvas(legofiedBitmap);
        paint = new Paint();

        initializePositions();
        currentPosition = 0;

        bricksPerFrame = amountOfBricks / (duration / getFrameDuration());

    }

    private void initializePositions() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < amountOfBricks; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);
        this.positions = positions;
    }

    @Override
    public boolean hasNextFrame() {
        return currentPosition < positions.size() - 1;
    }

    @Override
    public int getFrameDuration() {
        return 1000 / 60;
    }

    @Override
    public Bitmap nextFrame() {

        for (int i = 0; i < bricksPerFrame; i++) {
            if (currentPosition < positions.size()) {
                int shuffledPosition = positions.get(currentPosition);
                drawBrick(shuffledPosition);
                currentPosition++;
            }
        }

        return legofiedBitmap;
    }

    private void drawBrick(int shuffledPosition) {
        int xPos = shuffledPosition % baseBitmap.getWidth();
        int yPos = shuffledPosition / baseBitmap.getWidth();
        paint.setColorFilter(new PorterDuffColorFilter(baseBitmap.getPixel(xPos, yPos), PorterDuff.Mode.OVERLAY));
        canvas.drawBitmap(brickBitmap, xPos * brickBitmap.getWidth(), yPos * brickBitmap.getHeight(), paint);
    }
}
