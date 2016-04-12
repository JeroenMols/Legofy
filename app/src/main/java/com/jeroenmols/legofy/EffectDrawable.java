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
public class EffectDrawable extends Drawable implements Drawable.Callback {

    private Paint paint;
    private Bitmap originalBitmap;
    private Effect effect;

    private Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            invalidateSelf();
            scheduleNextAnimationFrame();
        }
    };

    public EffectDrawable(Bitmap bitmap) {
        originalBitmap = bitmap;

        paint = new Paint();
        paint.setAntiAlias(true);
        this.setCallback(this);
    }

    public void applyEffect(Effect effect) {
        this.effect = effect;
        effect.initialize(originalBitmap);
        scheduleNextAnimationFrame();
    }

    private void scheduleNextAnimationFrame() {
        unscheduleSelf(animationRunnable);
        scheduleSelf(animationRunnable, SystemClock.uptimeMillis() + effect.getFrameDuration());
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

    @Override
    public void invalidateDrawable(Drawable who) {
        super.invalidateSelf();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        invalidateDrawable(who);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        super.unscheduleSelf(what);
    }
}
