package com.jeroenmols.legofy;

import android.graphics.Bitmap;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertNotNull;

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
        Bitmap mockBitmap = Mockito.mock(Bitmap.class);

        Bitmap processedBitmap = new Legofy().processBitmap(mockBitmap);

        assertNotNull(processedBitmap);
    }
}