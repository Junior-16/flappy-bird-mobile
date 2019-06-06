package com.example.componets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Bar {

    private Bitmap bar1;
    private Bitmap bar2;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int outOfScreen;

    public Bar(Context context, int x, int y, int width) {
        Bitmap bar = null;

        try {

            bar = BitmapFactory.decodeStream(context.getAssets().open("bar.png"));

        } catch (Exception e) {

            e.printStackTrace();

        }
        this.bar1 = Bitmap.createScaledBitmap(bar, width,70, true);
        this.bar2 = Bitmap.createScaledBitmap(bar, width+6,70, true);
        this.x1 = x;
        this.y1 = y;
        this.x2 = width-12;
        this.y2 = y;
        this.outOfScreen = width;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.bar1, this.x1,this.y1, null);
        canvas.drawBitmap(this.bar2, this.x2,this.y2, null);
    }

    public void move(){
        if (this.x2 <= 0 && this.x1 <= -133) {
            this.x1 = this.outOfScreen-12;
        }
        if (this.x1 <= 0 && this.x2 <= -133) {
            this.x2 = this.outOfScreen-12;
        }
        this.x1 -= 8;
        this.x2 -= 8;
    }
}
