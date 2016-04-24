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

    @Override
    public Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(width).when(mockBitmap).getWidth();
        doReturn(height).when(mockBitmap).getHeight();
        return mockBitmap;
    }

    @Override
    public Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(FIRST_COLOR).when(mockBitmap).getPixel(0, 0);
        return super.createScaledBitmap(src, dstWidth, dstHeight, filter);
    }
}
