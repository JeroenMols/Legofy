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

    @Test(expected = RuntimeException.class)
    public void shouldLegofyBitmap() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);

        legofyPicassoTransformation.transform(mockBitmap);
    }

    @Test
    public void shouldReturnKey() throws Exception {
        String key = legofyPicassoTransformation.key();

        assertThat(key).isEqualTo(LegofyPicassoTransformation.LEGOFY);
    }
}