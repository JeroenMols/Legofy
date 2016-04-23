package com.jeroenmols.legofy;

import android.graphics.Bitmap;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * @author Jeroen Mols on 23/04/16.
 */
public class TestBitmapWrapper extends BitmapWrapper {

    @Override
    public Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(width).when(mockBitmap).getWidth();
        return mockBitmap;
    }
}
