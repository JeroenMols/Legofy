package com.jeroenmols.legofy;

import android.graphics.Bitmap;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@SmallTest
public class LegofyTest {

    @Test
    public void canInstantiate() throws Exception {
        new Legofy();
    }

    @Test
    public void processBitmap() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);

        Bitmap processedBitmap = new Legofy().processBitmap(mockBitmap);

        assertThat(processedBitmap).isNotNull();
        assertNotNull(processedBitmap);
    }
}