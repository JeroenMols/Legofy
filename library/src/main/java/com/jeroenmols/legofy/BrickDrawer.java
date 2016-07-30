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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class BrickDrawer {

    private final Paint paint;
    private final Canvas canvas;
    private Bitmap brickBitmap;

    public BrickDrawer() {
        paint = new Paint();
        canvas = new Canvas();
    }

    public void setBitmap(Resources resources, Bitmap targetBitmap, int brickSize) {
        canvas.setBitmap(targetBitmap);
        brickBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.brick), brickSize, brickSize, false);
    }

    public void drawBrick(int color, int xPos, int yPos) {
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.OVERLAY));
        canvas.drawBitmap(brickBitmap, xPos, yPos, paint);
    }
}
