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
    private Bitmap birdDead;
    private boolean birdRendered;
    private int x;
    private int y;
    private boolean climb;
    private int climbing; // how many times the bird will climb
    private boolean dead;

    public Bird(Resources res, int x, int y) {
        this.birdWingsDown = BitmapFactory.decodeResource(res, R.drawable.wing_down);
        this.birdWingsUp = BitmapFactory.decodeResource(res, R.drawable.wing_up);
        this.birdDead = BitmapFactory.decodeResource(res, R.drawable.wing_up);
        this.x = x;
        this.y = y;
        this.dead = false;
        this.climb = false;
        this.climbing = 0;
        this.birdRendered = true;
    }

    public void draw(Canvas canvas) {
        if (!this.dead) {
            if (this.birdRendered) {

                canvas.drawBitmap(this.birdWingsUp, this.x, this.y, null);

            } else {

                canvas.drawBitmap(this.birdWingsDown, this.x, this.y, null);

            }
            this.birdRendered = !this.birdRendered;
        } else{
            canvas.drawBitmap(this.birdWingsDown, this.x, this.y, null);
        }

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

    public int getHeight() {

        return this.birdWingsDown.getHeight();

    }

    public void killRevive() {
        this.dead = !this.dead;
    }

}
