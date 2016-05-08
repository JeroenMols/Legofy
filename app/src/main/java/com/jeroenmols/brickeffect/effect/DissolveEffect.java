package com.jeroenmols.brickeffect.effect;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jeroen Mols on 11/04/16.
 */
public class DissolveEffect implements Effect {

    private final int duration;
    private final Resources resources;
    private int bricksPerFrame;
    private int bricksPerWidth = 20;

    private Bitmap baseBitmap;
    private Bitmap legofiedBitmap;
    private int amountOfBricks;
    private Canvas canvas;
    private Paint paint;

    private List<Integer> positions;
    private int currentPosition;
    private int brickSize;

    public DissolveEffect(Resources resources, int bricksPerWidth, int duration) {
        if (bricksPerWidth > 0) {
            this.bricksPerWidth = bricksPerWidth;
        }
        this.resources = resources;
        this.duration = duration;
    }

    @Override
    public void initialize(Bitmap bitmap) {
        brickSize = bitmap.getWidth() / bricksPerWidth;

        int imageWidth = bricksPerWidth;
        int imageHeight = (int) (((float) bitmap.getHeight()) / bitmap.getWidth() * bricksPerWidth);

        baseBitmap = Bitmap.createScaledBitmap(bitmap, imageWidth * brickSize, imageHeight * brickSize, true);
        legofiedBitmap = Bitmap.createBitmap(imageWidth * brickSize, imageHeight * brickSize, Bitmap.Config.ARGB_4444);
        amountOfBricks = imageWidth * imageHeight;

        canvas = new Canvas(legofiedBitmap);
        paint = new Paint();

        initializePositions();
        currentPosition = 0;

        bricksPerFrame = (int) Math.ceil(amountOfBricks / (((float) duration) / getFrameDuration()));

    }

    private void initializePositions() {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < amountOfBricks; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);
        this.positions = positions;
    }

    @Override
    public boolean hasNextFrame() {
        return currentPosition < positions.size() - 1;
    }

    @Override
    public int getFrameDuration() {
        return 1000 / 60;
    }

    @Override
    public Bitmap nextFrame() {

        for (int i = 0; i < bricksPerFrame; i++) {
            if (currentPosition < positions.size()) {
                int shuffledPosition = positions.get(currentPosition);
                drawRectangle(shuffledPosition);
                currentPosition++;
            }
        }

        return legofiedBitmap;
    }

    private void drawRectangle(int shuffledPosition) {
        int xPos = shuffledPosition % bricksPerWidth;
        int yPos = shuffledPosition / bricksPerWidth;

        Rect rect = new Rect(xPos * brickSize, yPos * brickSize, xPos * brickSize + brickSize, yPos * brickSize + brickSize);
        canvas.drawBitmap(baseBitmap, rect, rect, paint);
    }
}
