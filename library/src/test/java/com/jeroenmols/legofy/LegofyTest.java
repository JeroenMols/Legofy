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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@SmallTest
public class LegofyTest {

    public static final int BRICKS_INWIDTH = 10;
    public static final int BRICK_SIZE = 30;

    @Mock
    private Context mockContext;

    @Mock
    private BrickDrawer mockDrawer;

    private Legofy legofy;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        legofy = new Legofy(mockContext, new TestBitmapWrapper(), mockDrawer).amountOfBricks(BRICKS_INWIDTH);
    }

    @Test
    public void createViaFactory() throws Exception {
        Legofy legofy = Legofy.with(mockContext);

        assertThat(legofy).isNotNull();
    }

    @Test(expected = RuntimeException.class)
    public void throwExceptionWhenContextNull() throws Exception {
        Legofy.with(null);
    }

    @Test
    public void takeApplicationContext() throws Exception {
        Legofy.with(mockContext);

        verify(mockContext).getApplicationContext();
    }

    @Test
    public void setNumberOfBricks() throws Exception {
        Legofy legofy = Legofy.with(mock(Context.class));

        Legofy returnLegofy = legofy.amountOfBricks(10);

        assertThat(returnLegofy).isSameAs(legofy);
    }

    @Test
    public void createWithBitmapWrapper() throws Exception {
        new Legofy(null, mock(BitmapWrapper.class), null);
    }

    @Test
    public void createWithBrickDrawer() throws Exception {
        new Legofy(null, null, mock(BrickDrawer.class));
    }

    @Test
    public void convertBitmap() throws Exception {
        Bitmap mockBitmap = createMockBitmap(234, 257);

        Bitmap convertedBitmap = legofy.convert(mockBitmap);

        assertThat(convertedBitmap).isNotNull();
    }

    @Test
    public void widthMultipleOfBricks() throws Exception {
        Bitmap mockBitmap = createMockBitmap(234, 257);

        Bitmap convertedBitmap = legofy.convert(mockBitmap);

        assertThat(convertedBitmap.getWidth()).isEqualTo(230);
    }

    @Test
    public void heightMultipleOfBricks() throws Exception {
        Bitmap mockBitmap = createMockBitmap(234, 257);

        Bitmap convertedBitmap = legofy.convert(mockBitmap);

        assertThat(convertedBitmap.getHeight()).isEqualTo(253);
    }

    @Test
    public void convertedBitmapIsArgb4444() throws Exception {
        Bitmap mockBitmap = createMockBitmap(3 * BRICK_SIZE, 2 * BRICK_SIZE);

        Bitmap convertedBitmap = legofy.amountOfBricks(3).convert(mockBitmap);

        assertThat(convertedBitmap.getConfig()).isEqualTo(Bitmap.Config.ARGB_4444);
    }

    @Test
    public void useResourcesFromContext() throws Exception {
        Resources mockResources = mock(Resources.class);
        doReturn(mockResources).when(mockContext).getResources();
        Bitmap mockBitmap = createMockBitmap(BRICK_SIZE, BRICK_SIZE);

        legofy.convert(mockBitmap);

        verify(mockDrawer).setBitmap(eq(mockResources), any(Bitmap.class), anyInt());
    }

    @Test
    public void setconvertedBitmapToBrickDrawer() throws Exception {
        Bitmap mockBitmap = createMockBitmap(BRICK_SIZE, BRICK_SIZE);

        Bitmap convertedBitmap = legofy.convert(mockBitmap);

        verify(mockDrawer).setBitmap(any(Resources.class), eq(convertedBitmap), anyInt());
    }

    @Test
    public void setBrickSizeBrickDrawer() throws Exception {
        Bitmap mockBitmap = createMockBitmap(234, 257);

        legofy.convert(mockBitmap);

        verify(mockDrawer).setBitmap(any(Resources.class), any(Bitmap.class), eq(23));
    }

    @Test
    public void drawFirstBrick() throws Exception {
        Bitmap mockBitmap = createMockBitmap(BRICK_SIZE, BRICK_SIZE);

        legofy.amountOfBricks(1).convert(mockBitmap);

        verify(mockDrawer).drawBrick(anyInt(), eq(0), eq(0));
    }

    @Test
    public void drawSecondHorizontalBrick() throws Exception {
        Bitmap mockBitmap = createMockBitmap(2 * BRICK_SIZE, BRICK_SIZE);

        legofy.amountOfBricks(2).convert(mockBitmap);

        verify(mockDrawer).drawBrick(anyInt(), eq(BRICK_SIZE), eq(0));
    }

    @Test
    public void drawSecondVerticalBrick() throws Exception {
        Bitmap mockBitmap = createMockBitmap(BRICK_SIZE, 2 * BRICK_SIZE);

        legofy.amountOfBricks(1).convert(mockBitmap);

        verify(mockDrawer).drawBrick(anyInt(), eq(0), eq(BRICK_SIZE));
    }

    @Test
    public void scaleImageOnlyOnceForColorExtraction() throws Exception {
        Bitmap mockBitmap = createMockBitmap(3 * BRICK_SIZE, 2 * BRICK_SIZE);
        BitmapWrapper mockWrapper = createMockBitmapWrapper(mockBitmap);

        new Legofy(mockContext, mockWrapper, mockDrawer).amountOfBricks(3).convert(mockBitmap);

        verify(mockWrapper).createScaledBitmap(mockBitmap, 3, 2, true);
    }

    @Test
    public void drawFirstBrickWithDownScaledColor() throws Exception {
        Bitmap mockBitmap = createMockBitmap(BRICK_SIZE, BRICK_SIZE);

        legofy.amountOfBricks(1).convert(mockBitmap);

        verify(mockDrawer).drawBrick(eq(TestBitmapWrapper.FIRST_COLOR), eq(0), eq(0));
    }

    @Test
    public void drawSecondBrickWithDownScaledColor() throws Exception {
        Bitmap mockBitmap = createMockBitmap(2 * BRICK_SIZE, BRICK_SIZE);

        legofy.amountOfBricks(2).convert(mockBitmap);

        verify(mockDrawer).drawBrick(eq(TestBitmapWrapper.SECOND_HORIZONTAL_COLOR), eq(BRICK_SIZE), eq(0));
    }

    @Test
    public void outputWidthBitmapMax1080p() throws Exception {
        Bitmap mockBitmap = createMockBitmap(2000, 1000);

        Bitmap processedBitmap = legofy.amountOfBricks(BRICKS_INWIDTH).convert(mockBitmap);

        assertThat(processedBitmap.getWidth()).isEqualTo(1080);
        assertThat(processedBitmap.getHeight()).isEqualTo(540);
    }

    @Test
    public void outputHeightBitmapMax1080p() throws Exception {
        Bitmap mockBitmap = createMockBitmap(1000, 2000);

        Bitmap processedBitmap = legofy.amountOfBricks(BRICKS_INWIDTH).convert(mockBitmap);

        assertThat(processedBitmap.getWidth()).isEqualTo(540);
        assertThat(processedBitmap.getHeight()).isEqualTo(1080);
    }

    @Test
    public void outputHeightBitmapMax1080p_2() throws Exception {
        Bitmap mockBitmap = createMockBitmap(2000, 3000);

        Bitmap processedBitmap = legofy.amountOfBricks(BRICKS_INWIDTH).convert(mockBitmap);

        assertThat(processedBitmap.getWidth()).isEqualTo(720);
        assertThat(processedBitmap.getHeight()).isEqualTo(1080);
    }

    @Test
    public void upscaleImageIfTooSmall() throws Exception {
        Bitmap mockBitmap = createMockBitmap(10, 10);

        Bitmap processedBitmap = legofy.amountOfBricks(BRICKS_INWIDTH).convert(mockBitmap);

        assertThat(processedBitmap.getWidth()).isEqualTo(200);
        assertThat(processedBitmap.getHeight()).isEqualTo(200);
    }

    @Test
    public void limitAmountOfBricksIfTooManyRequested() throws Exception {
        Bitmap mockBitmap = createMockBitmap(1080, 1080);

        new Legofy(mockContext, new TestBitmapWrapper(), mockDrawer).amountOfBricks(1080).convert(mockBitmap);

        verify(mockDrawer, times(2916)).drawBrick(anyInt(), anyInt(), anyInt());
    }

    @Test
    public void dontUpscaleWidthIfNoRoomForHeight() throws Exception {
        Bitmap mockBitmap = createMockBitmap(100, 1080);

        Bitmap processedBitmap = legofy.amountOfBricks(1080).convert(mockBitmap);

        assertThat(processedBitmap.getWidth()).isEqualTo(100);
    }

    @Test
    public void dontUpscaleWidthIfNoRoomForWidth() throws Exception {
        Bitmap mockBitmap = createMockBitmap(100, 50);

        Bitmap processedBitmap = legofy.amountOfBricks(1080).convert(mockBitmap);

        assertThat(processedBitmap.getWidth()).isEqualTo(1080);
    }

    @Test
    public void useCorrectDimensionsAfterScaling() throws Exception {
        Bitmap mockBitmap = createMockBitmap(159, 183);

        legofy.amountOfBricks(40).convert(mockBitmap);

        verify(mockDrawer, atLeastOnce()).drawBrick(anyInt(), eq(45 * 20), anyInt());
    }

    private Bitmap createMockBitmap(int width, int height) {
        Bitmap mockBitmap = mock(Bitmap.class);
        doReturn(width).when(mockBitmap).getWidth();
        doReturn(height).when(mockBitmap).getHeight();
        return mockBitmap;
    }

    private BitmapWrapper createMockBitmapWrapper(Bitmap returnBitmap) {
        BitmapWrapper mockWrapper = mock(BitmapWrapper.class);
        doReturn(returnBitmap).when(mockWrapper).createBitmap(anyInt(), anyInt(), any(Bitmap.Config.class));
        doReturn(returnBitmap).when(mockWrapper).createScaledBitmap(any(Bitmap.class), anyInt(), anyInt(), anyBoolean());
        return mockWrapper;
    }
}