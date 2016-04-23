package com.jeroenmols.legofy;

import android.graphics.Bitmap;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@SmallTest
public class LegofyTest {

    @Test
    public void createWithNumberOfWidthBricks() throws Exception {
        new Legofy(10);
    }

    @Test
    public void createWithBitmapWrapper() throws Exception {
        new Legofy(mock(BitmapWrapper.class), null, 0);
    }

    @Test
    public void createWithBrickDrawer() throws Exception {
        new Legofy(null, mock(DrawingWrapper.class), 0);
    }

    @Test
    public void processBitmap() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(234).when(mockBitmap).getWidth();

        Bitmap processedBitmap = new Legofy(new TestBitmapWrapper(), mock(DrawingWrapper.class), 10).processBitmap(mockBitmap);

        assertThat(processedBitmap).isNotNull();
    }

    @Test
    public void widthMultipleOfBricks() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(234).when(mockBitmap).getWidth();

        Bitmap processedBitmap = new Legofy(new TestBitmapWrapper(), mock(DrawingWrapper.class), 10).processBitmap(mockBitmap);

        assertThat(processedBitmap.getWidth()).isEqualTo(230);
    }

    @Test
    public void heightMultipleOfBricks() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(257).when(mockBitmap).getHeight();

        Bitmap processedBitmap = new Legofy(new TestBitmapWrapper(), mock(DrawingWrapper.class), 10).processBitmap(mockBitmap);

        assertThat(processedBitmap.getHeight()).isEqualTo(250);
    }
}