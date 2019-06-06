package com.example.componets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.flappybird.R;

import java.util.concurrent.ThreadLocalRandom;

public class Pipe {

    private Bitmap pipeUp;
    private Bitmap pipeDown;

    private int opening;
    private int x;
    private int y = 1265;
    private int resetPos;

    public Pipe(Resources res, int x, int type, int screenWidth) {

        this.x = x;
        this.resetPos = screenWidth;
        this.opening = this.generatePipeOpening();
        this.pipeUp = BitmapFactory.decodeResource(res, R.drawable.pipe_up);

        if (type == 1) {

            this.pipeDown = BitmapFactory.decodeResource(res, R.drawable.pipe_down1);

        } else if (type == 2) {

            this.pipeDown = BitmapFactory.decodeResource(res, R.drawable.pipe_down2);

        } else {

            this.pipeDown = BitmapFactory.decodeResource(res, R.drawable.pipe_down3);

        }

        /* The following operating was needed 'cause the pipe position is referenced in the bitmap
         * top left corner.
         * */

        this.y -= this.pipeDown.getHeight();

    }

    public int getX() {

        return this.x;

    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(this.pipeDown, this.x, this.y, null);
        canvas.drawBitmap(this.pipeUp, this.x, this.y - this.opening - this.pipeUp.getHeight(), null);

    }

    public void move() {

        if (this.x <= -280){

            this.x = this.resetPos;
            this.opening = generatePipeOpening();

        }

        this.x -= 8;

    }

    /*
     * Random pipes openings generator given an interval.
     * */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int generatePipeOpening() {

        return ThreadLocalRandom.current().nextInt(250, 450);

    }

}
