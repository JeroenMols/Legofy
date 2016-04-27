package com.jeroenmols.legofy;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * @author Jeroen Mols on 27/04/16.
 */
@SmallTest
public class LegofyPicassoTransformationTest {

    @Mock
    private Context mockContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void canInstantiateWithContext() throws Exception {
        new LegofyPicassoTransformation(mockContext);
    }

    @Test
    public void shouldBePicassoTransformation() throws Exception {
        LegofyPicassoTransformation legofyPicassoTransformation = new LegofyPicassoTransformation(mockContext);

        assertThat(legofyPicassoTransformation).isInstanceOf(com.squareup.picasso.Transformation.class);
    }

    @Test
    public void shouldTakeApplicationContext() throws Exception {
        new LegofyPicassoTransformation(mockContext);

        verify(mockContext).getApplicationContext();
    }
}