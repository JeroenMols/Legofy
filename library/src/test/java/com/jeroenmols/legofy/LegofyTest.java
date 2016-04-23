package com.jeroenmols.legofy;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@SmallTest
public class LegofyTest {

    @Test
    public void canInstantiate() throws Exception {
        new Legofy();
    }
}