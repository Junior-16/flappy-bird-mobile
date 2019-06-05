package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.componets.Bird;
import com.example.componets.Bar;
import com.example.componets.Text;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Context context;
    private boolean initComponents = false;

    private Bird bird;
    private Bar bar;
    private Text text;
    private Bitmap background;

    private boolean start = false;
    private boolean tap = false;

    public Game(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = false;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    /*
     * Updates components positions
     * */

    public void update() {

        if (!this.start){
            this.bird.fly();
        } else if (!this.tap){
            this.bird.fall();
        } else {
            this.bird.climb();
            if (this.bird.getClimbing() == 0) {
                this.tap = false;
            }
        }

        this.bar.move();

    }

    /*
     * Draws game components in the canvas
     * */

    public void drawCanvas(Canvas canvas) {

        canvas.drawBitmap(this.background,0,0, null);
        this.bird.draw(canvas);
        this.bar.draw(canvas);
        if (!this.start){
            this.text.draw(canvas);
        }


    }

    public boolean getInitComponets(){

        return this.initComponents;

    }

    public void initComponents() {
        try {
            Bitmap bgBuffer = BitmapFactory.decodeStream(this.context.getAssets().open("surface.jpg"));
            float bgScale = (float) bgBuffer.getHeight() / (float) getHeight();
            int newWidth = Math.round(bgBuffer.getWidth() / bgScale);
            int newHeight = Math.round(bgBuffer.getHeight() / bgScale);
            this.background = Bitmap.createScaledBitmap(bgBuffer, newWidth + 15, newHeight, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.bird = new Bird(getResources(), 55, (getHeight() / 2) - 150);
        this.bar = new Bar(this.context, 0, 1265, getWidth());
        this.text = new Text(getWidth() / 5, getHeight() / 2, context);
        this.initComponents = true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent tap) {
        if (tap.getActionMasked() == tap.ACTION_DOWN){
            this.tap = true;
            this.start = true;
        }
        return true;
    }

}
