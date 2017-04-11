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

import android.graphics.Bitmap;
import android.graphics.Color;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class TestBitmapWrapper extends BitmapWrapper {

    public static final int FIRST_COLOR = Color.RED;
    public static final int SECOND_HORIZONTAL_COLOR = Color.BLUE;

    @Override
    public Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(width).when(mockBitmap).getWidth();
        doReturn(height).when(mockBitmap).getHeight();
        doReturn(config).when(mockBitmap).getConfig();
        return mockBitmap;
    }

    @Override
    public Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(FIRST_COLOR).when(mockBitmap).getPixel(0, 0);
        doReturn(SECOND_HORIZONTAL_COLOR).when(mockBitmap).getPixel(1, 0);
        return mockBitmap;
    }
}
