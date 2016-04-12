package com.jeroenmols.legofy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

/**
 * @author Jeroen Mols on 11/04/16.
 */
public class EffectDrawable extends Drawable implements Drawable.Callback, Runnable {

    private Paint paint;
    private Bitmap originalBitmap;
    private Effect effect;

    public EffectDrawable(Bitmap bitmap) {
        originalBitmap = bitmap;

        paint = new Paint();
        paint.setAntiAlias(true);
        this.setCallback(this);
    }

    public void applyEffect(Effect effect) {
        this.effect = effect;
        effect.initialize(originalBitmap);
        run();
    }

    public void invalidateDrawable(Drawable drawable) {
        super.invalidateSelf(); //This was done for my specific example. I wouldn't use it otherwise
    }


    public void scheduleDrawable(Drawable drawable, Runnable runnable, long l) {
        invalidateDrawable(drawable);
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        super.unscheduleSelf(runnable);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect srcRect = new Rect(0, 0, originalBitmap.getHeight(), originalBitmap.getWidth());
        Rect destRect = createDestinationRectangle(canvas, originalBitmap);

        canvas.drawBitmap(originalBitmap, srcRect, destRect, paint);

        if (effect != null) {
            Bitmap bitmap = effect.nextFrame();
            srcRect = new Rect(0, 0, bitmap.getHeight(), bitmap.getWidth());
            canvas.drawBitmap(bitmap, srcRect, destRect, paint);
        }
    }

    @NonNull
    private Rect createDestinationRectangle(Canvas canvas, Bitmap bitmap) {
        float scaleX = ((float) canvas.getWidth()) / bitmap.getWidth();
        float scaleY = ((float) canvas.getHeight()) / bitmap.getHeight();

        Rect destRect;
        if (shouldCenterHorizontally(scaleX, scaleY)) {
            destRect = createHorizontalCenteredRectangle(canvas, bitmap, scaleY);
        } else {
            destRect = createVerticallyCenteredRectangle(canvas, bitmap, scaleX);
        }
        return destRect;
    }

    private boolean shouldCenterHorizontally(float scaleX, float scaleY) {
        return scaleX > scaleY;
    }

    @NonNull
    private Rect createVerticallyCenteredRectangle(Canvas canvas, Bitmap bitmap, float scaleX) {
        int scaledWidth = (int) (scaleX * bitmap.getWidth());
        int scaledHeight = (int) (scaleX * bitmap.getHeight());
        int translationY = (int) (canvas.getHeight() - (scaleX * bitmap.getHeight())) / 2;
        return new Rect(0, translationY, scaledWidth, scaledHeight + translationY);
    }

    @NonNull
    private Rect createHorizontalCenteredRectangle(Canvas canvas, Bitmap bitmap, float scaleY) {
        int translationX = (int) (canvas.getWidth() - (scaleY * bitmap.getWidth())) / 2;
        int scaledWidth = (int) (scaleY * bitmap.getWidth());
        int scaledHeight = (int) (scaleY * bitmap.getHeight());
        return new Rect(translationX, 0, scaledWidth + translationX, scaledHeight);
    }

    public void nextFrame() {
        unscheduleSelf(this);
        scheduleSelf(this, SystemClock.uptimeMillis() + effect.getFrameDuration());
    }

    public void run() {
        invalidateSelf();
        nextFrame();
    }

    @Override
    public void setAlpha(int alpha) {
        // Has no effect
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // Has no effect
    }

    @Override
    public int getOpacity() {
        // Not Implemented
        return 0;
    }

}
