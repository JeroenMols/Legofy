package com.jeroenmols.brickeffect.effect;

import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 11/04/16.
 */
public interface Effect {
    void initialize(Bitmap bitmap);

    boolean hasNextFrame();

    int getFrameDuration();

    Bitmap nextFrame();
}
