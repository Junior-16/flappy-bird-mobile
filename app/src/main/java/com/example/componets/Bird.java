package com.example.componets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.flappybird.R;

public class Bird{

    private Bitmap birdWingsUp;
    private Bitmap birdWingsDown;
    private boolean birdRendered = true;
    private int x;
    private int y;
    private boolean climb = false;
    private int climbing = 0; // how many times the bird will climb

    public Bird(Resources res, int x, int y) {
        this.birdWingsDown = BitmapFactory.decodeResource(res, R.drawable.wing_down);
        this.birdWingsUp = BitmapFactory.decodeResource(res, R.drawable.wing_up);
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        if (this.birdRendered) {

            canvas.drawBitmap(this.birdWingsUp, this.x, this.y, null);

        } else {

            canvas.drawBitmap(this.birdWingsDown, this.x, this.y, null);

        }

        this.birdRendered = !this.birdRendered;
    }

    public void fly() {
        if (this.y >= 854) {
            this.climb = true;
        }
        if (this.y <= 654 && this.climb) {
            this.climb = false;
        }
        if (!this.climb) {
            this.y += 8;
        } else if (this.climb) {
            this.y -= 8;
        }
    }

    public void fall() {
        this.y += 10;
    }

    public void climb() {
        if (this.climbing == 3) {
            this.climbing = 0;
        } else {
            this.climbing += 1;
            this.y -= 20;
        }
    }

    public int getClimbing(){

        return this.climbing;

    }

    public int getY() {
        return this.y;
    }

}
