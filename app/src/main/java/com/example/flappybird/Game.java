package com.example.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.LinkedList;
import java.util.Queue;

import com.example.componets.Bird;
import com.example.componets.Bar;
import com.example.componets.Pipe;
import com.example.componets.Text;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Context context;

    private boolean initComponents = false;
    private Pipe[] pipeList;
    private Bird bird;
    private Bar bar;
    private Text text0;
    private Text text1;
    private Bitmap background;
    private Queue<Integer> pipeIndexQueue;

    private boolean start = false;
    private boolean tap = false;
    private boolean end = false;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void update() {

        if (!this.end) {

            if (this.start) {

                for (Pipe p : this.pipeList) {
                    p.move();
                }

                this.updatePipeQueue();

            }

            if (!this.start) {

                this.bird.fly();

            } else if (!this.tap) {

                this.bird.fall(10);

                if (this.collides()) {
                    this.end = true;
                    this.text0.setX(getWidth() / 4);
                    this.text0.setY(getHeight() / 3);
                    this.text0.setText("Game Over");
                    this.text1.setText("Tap to restart");
                    this.text1.setX(getWidth()/5);
                    this.bird.killRevive();
                }

            } else {
                this.bird.climb();

                if (this.bird.getClimbing() == 0) {

                    this.tap = false;

                }
            }

            this.bar.move();
        }

    }

    /*
     * Draws game components in the canvas
     * */

    public void drawCanvas(Canvas canvas) {

        canvas.drawBitmap(this.background,0,0, null);
        this.bar.draw(canvas);

        if (!this.start){
            this.text0.draw(canvas);
        }

        else {
            for (Pipe p : this.pipeList) {
                p.draw(canvas);
            }
        }

        this.bird.draw(canvas);

        if (this.end) {
            this.text0.draw(canvas);
            this.text1.draw(canvas);
            if (this.bird.getY() < 1260) {
                this.bird.fall(25);
            }
        }

    }

    private void updatePipeQueue() {

        int frontPipe = this.pipeIndexQueue.peek();

        if (this.pipeList[frontPipe].getX() <= -32) {
            frontPipe = this.pipeIndexQueue.remove();
            this.pipeIndexQueue.add(frontPipe);
        }

    }

    /*
     * Method that checks if the first pipe of the queue collides with the bird.
     * The strategy used to do so was based in the pipe and bird positions.
     * */

    private boolean collides() {

        int frontPipe = this.pipeIndexQueue.peek();
        Pipe pipe = this.pipeList[frontPipe];
        frontPipe = pipe.getX();

        if (frontPipe >= 0 && frontPipe <= 150) {
            if (this.bird.getY()+this.bird.getHeight() >= pipe.getPipeDownY()) {
                return true;
            }
            else if (this.bird.getY() <= pipe.getPipeUpperY()) {
                return true;
            }
            return false;

        }
        return false;

    }


    public boolean getInitComponets(){

        return this.initComponents;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        this.pipeIndexQueue = new LinkedList<Integer>();
        this.pipeList = new Pipe[4];
        int x = getWidth();

        for (int i = 0; i < this.pipeList.length; i++) {
            this.pipeIndexQueue.add(i);
            this.pipeList[i] = new Pipe(getResources(), x, getWidth());
            x += 320;
        }


        this.bird = new Bird(getResources(), 55, (getHeight() / 2) - 150);
        this.bar = new Bar(this.context, 0, 1265, getWidth());
        this.text0 = new Text(getWidth() / 5, getHeight() / 2, context);
        this.text1 = new Text(getWidth() / 5, (getHeight() / 3)+150, context);

        this.initComponents = true;

    }

    /*
     * Listener method called when a "tap" event occur
     * */

    @Override
    public boolean onTouchEvent(MotionEvent tap) {

        if (tap.getActionMasked() == tap.ACTION_DOWN){
            this.tap = true;
        }

        if (!this.start) {
            this.start = true;
        }

        return true;
    }

}
