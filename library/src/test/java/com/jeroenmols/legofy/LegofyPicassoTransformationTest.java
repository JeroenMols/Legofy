package com.jeroenmols.legofy;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jeroen Mols on 27/04/16.
 */
@SmallTest
public class LegofyPicassoTransformationTest {

    @Test
    public void canInstantiate() throws Exception {
        new LegofyPicassoTransformation();
    }

    @Test
    public void shouldBePicassoTransformation() throws Exception {
        LegofyPicassoTransformation legofyPicassoTransformation = new LegofyPicassoTransformation();

        assertThat(legofyPicassoTransformation).isInstanceOf(com.squareup.picasso.Transformation.class);
    }
}