package com.example.componets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/*
 * Score class contains the 0 up to 9 image numbers
 */


public class Score {

    private Bitmap[] numbers;
    private int score, x1, x2, y;

    public Score(Context context, int x1, int x2, int y) {
        this.numbers = new Bitmap[10];
        this.score = 0;
        this.x1 = x1;
        this.x2 = x2;
        this.y = y;

        for (int i = 0; i < 10; i++) {
            try {
                this.numbers[i] = BitmapFactory.decodeStream(context.getAssets().open(i + ".png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Canvas canvas) {

        if (this.score >= 10) {

            String scoreString = Integer.toString(this.score);
            int i0 = Character.getNumericValue(scoreString.charAt(0));
            int i1 = Character.getNumericValue(scoreString.charAt(1));
            canvas.drawBitmap(this.numbers[i0], this.x1, this.y, null);
            canvas.drawBitmap(this.numbers[i1], this.x2, this.y, null);

        } else {

            canvas.drawBitmap(this.numbers[this.score], this.x1, this.y, null);

        }

    }

    public void reset() {

        this.score = 0;

    }

    public void increase() {

        this.score++;

    }


}
