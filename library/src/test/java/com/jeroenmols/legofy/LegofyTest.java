package com.jeroenmols.legofy;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@SmallTest
public class LegofyTest {

    public static final int BRICKS_SIZE = 10;

    @Mock
    private BrickDrawer mockDrawer;
    private Legofy legofy;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        legofy = new Legofy(new TestBitmapWrapper(), mockDrawer, BRICKS_SIZE);
    }

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
        new Legofy(null, mock(BrickDrawer.class), 0);
    }

    @Test
    public void processBitmap() throws Exception {
        Bitmap mockBitmap = createMockBitmap(234, 257);

        Bitmap processedBitmap = legofy.processBitmap(null, mockBitmap);

        assertThat(processedBitmap).isNotNull();
    }

    @Test
    public void widthMultipleOfBricks() throws Exception {
        Bitmap mockBitmap = createMockBitmap(234, 257);

        Bitmap processedBitmap = legofy.processBitmap(null, mockBitmap);

        assertThat(processedBitmap.getWidth()).isEqualTo(230);
    }

    @Test
    public void heightMultipleOfBricks() throws Exception {
        Bitmap mockBitmap = createMockBitmap(234, 257);

        Bitmap processedBitmap = legofy.processBitmap(null, mockBitmap);

        assertThat(processedBitmap.getHeight()).isEqualTo(250);
    }

    @Test
    public void setProcessedBitmapToBrickDrawer() throws Exception {
        Bitmap mockBitmap = createMockBitmap(15, 15);

        Bitmap processedBitmap = legofy.processBitmap(null, mockBitmap);

        verify(mockDrawer).setBitmap(any(Resources.class), eq(processedBitmap), anyInt());
    }

    private Bitmap createMockBitmap(int width, int height) {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(width).when(mockBitmap).getWidth();
        doReturn(height).when(mockBitmap).getHeight();
        return mockBitmap;
    }
}