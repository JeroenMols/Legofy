package com.jeroenmols.legofy;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Jeroen Mols on 27/04/16.
 */
@SmallTest
public class LegofyPicassoTransformationTest {

    @Mock
    private Context mockContext;
    private LegofyPicassoTransformation legofyPicassoTransformation;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        legofyPicassoTransformation = new LegofyPicassoTransformation(mockContext);
    }

    @Test
    public void canInstantiateWithContext() throws Exception {
        new LegofyPicassoTransformation(mockContext);
    }

    @Test
    public void shouldBePicassoTransformation() throws Exception {
        assertThat(legofyPicassoTransformation).isInstanceOf(com.squareup.picasso.Transformation.class);
    }

    @Test
    public void shouldTakeApplicationContext() throws Exception {
        verify(mockContext).getApplicationContext();
    }

    @Test
    public void shouldRecycleBitmap() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);

        legofyPicassoTransformation.transform(mockBitmap);

        verify(mockBitmap).recycle();
    }
}