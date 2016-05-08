package com.jeroenmols.brickeffect.effect;

import android.graphics.Bitmap;

/**
 * @author Jeroen Mols on 17/04/16.
 */
public class NoEffect implements Effect {
    private Bitmap bitmap;

    @Override
    public void initialize(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public boolean hasNextFrame() {
        return false;
    }

    @Override
    public int getFrameDuration() {
        return 100000000;
    }

    @Override
    public Bitmap nextFrame() {
        return bitmap;
    }
}
