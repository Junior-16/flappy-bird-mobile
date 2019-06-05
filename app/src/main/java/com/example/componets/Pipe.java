package com.example.componets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.flappybird.R;

public class Pipe {

    private Bitmap pipeUp;
    private Bitmap pipeDown;
    private int opening;
    private int x;
    private int y;

    public Pipe(int x, int y, int opening) {
        this.x = x;
        this.y = y;
        this.opening = opening;
        this.pipeUp = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.pipe_up);
        this.pipeDown = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.pipe_down);
    }

    private int getX() {
        return this.x;
    }
}
