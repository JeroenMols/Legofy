package com.jeroenmols.brickeffect.effect;

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

    private final Bitmap previousBitmap;
    private Paint paint;
    private Bitmap nextBitmap;
    private Bitmap resizedBitmap;
    private Effect effect;

    private Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            invalidateSelf();
            scheduleNextAnimationFrame();
        }
    };

    public EffectDrawable(Bitmap previousBitmap, Bitmap nextBitmap) {
        this.previousBitmap = previousBitmap;
        this.nextBitmap = nextBitmap;

        paint = new Paint();
        paint.setAntiAlias(true);
        this.setCallback(this);
    }

    public void applyEffect(Effect effect) {
        this.effect = effect;
        resizedBitmap = null;
        scheduleNextAnimationFrame();
    }

    public Bitmap getEffectBitmap() {
        return effect.nextFrame();
    }

    private void scheduleNextAnimationFrame() {
        unscheduleSelf(animationRunnable);
        scheduleSelf(animationRunnable, SystemClock.uptimeMillis() + effect.getFrameDuration());
    }

    @Override
    public void draw(Canvas canvas) {
        if (resizedBitmap == null) {
            resizedBitmap = resizeOriginalBitmap(canvas.getWidth(), canvas.getHeight());
            recycleOriginalBitmap();
            effect.initialize(resizedBitmap);
        }

        if (previousBitmap != null && !previousBitmap.isRecycled()) {
            Rect srcRect1 = new Rect(0, 0, previousBitmap.getWidth(), previousBitmap.getHeight());
            Rect destRect1 = createDestinationRectangle(canvas, previousBitmap);
            canvas.drawBitmap(previousBitmap, srcRect1, destRect1, paint);
        }

        if (effect != null) {
            Bitmap bitmap = effect.nextFrame();
            Rect destRect = createDestinationRectangle(canvas, resizedBitmap);
            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(bitmap, srcRect, destRect, paint);
        }
    }

    @NonNull
    private Bitmap resizeOriginalBitmap(int width, int height) {
        float scaleX = ((float) width) / nextBitmap.getWidth();
        float scaleY = ((float) height) / nextBitmap.getHeight();
        float scale = Math.min(scaleX, scaleY);

        return Bitmap.createScaledBitmap(nextBitmap, (int) (nextBitmap.getWidth() * scale), (int) (nextBitmap.getHeight() * scale), true);
    }

    private void recycleOriginalBitmap() {
        nextBitmap.recycle();
        nextBitmap = null;
    }

    @NonNull
    private Rect createDestinationRectangle(Canvas canvas, Bitmap bitmap) {
        float scaleX = ((float) canvas.getWidth()) / bitmap.getWidth();
        float scaleY = ((float) canvas.getHeight()) / bitmap.getHeight();

        Rect destRect;
        if (isWideImage(scaleX, scaleY)) {
            destRect = createHorizontalCenteredRectangle(canvas, bitmap, scaleY);
        } else {
            destRect = createVerticallyCenteredRectangle(canvas, bitmap, scaleX);
        }
        return destRect;
    }

    private boolean isWideImage(float scaleX, float scaleY) {
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
        int scaledWidth = (int) (scaleY * bitmap.getWidth());
        int scaledHeight = (int) (scaleY * bitmap.getHeight());
        int translationX = (int) (canvas.getWidth() - (scaleY * bitmap.getWidth())) / 2;
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
