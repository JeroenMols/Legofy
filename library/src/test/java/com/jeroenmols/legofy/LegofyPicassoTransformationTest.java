package com.jeroenmols.legofy;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jeroen Mols on 27/04/16.
 */
@SmallTest
public class LegofyPicassoTransformationTest {

    @Mock
    private Context mockContext;

    @Test
    public void canInstantiateWithContext() throws Exception {
        new LegofyPicassoTransformation(mockContext);
    }

    @Test
    public void shouldBePicassoTransformation() throws Exception {
        LegofyPicassoTransformation legofyPicassoTransformation = new LegofyPicassoTransformation(mockContext);

        assertThat(legofyPicassoTransformation).isInstanceOf(com.squareup.picasso.Transformation.class);
    }
}