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
import com.example.componets.Score;
import com.example.componets.Text;

/*
 *
 * Grateful to this site: https://www.spriters-resource.com/mobile/flappybird/sheet/59894/
 * I get the sprites from there.
 * */

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread thread;
    private Context context;

    private Pipe[] pipeList;
    private Bird bird;
    private Bar bar;
    private Text text0;
    private Text text1;
    private Text text2;
    private Bitmap background;
    private Queue<Integer> pipeIndexQueue;
    private Score score;

    private boolean start = false;
    private boolean tap = false;
    private boolean end = false;
    private boolean initComponents = false;
    private int gravity;
    private int bestScore;

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
        boolean retry = true;
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

                if (this.collides() || this.bird.getY() >= 1240) {
                    this.end = true;
                    this.text0.setX(getWidth() / 4);
                    this.text0.setY(getHeight() / 3);
                    this.text0.setText("Game Over");
                    this.bird.changeStatus();
                    if (this.score.getScore() > this.bestScore) {
                        this.bestScore = this.score.getScore();
                    }
                    this.text2.setText("Best: "+this.bestScore);
                }

            }

            if (!this.start) {

                this.bird.fly();

            } else if (!this.tap && !this.end) {

                this.bird.fall(this.gravity);
                this.gravity += 2;

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
        this.score.draw(canvas);

        if (this.end) {
            this.text0.draw(canvas);
            this.text1.draw(canvas);
            this.text2.draw(canvas);

            if (this.bird.getY() < 1240) {
                this.bird.fall(this.gravity);
                this.gravity += 5;
            }
        }
        //canvas.drawCircle(0, this.bird.getY(), 5, this.paint);
        //canvas.drawCircle(150, this.bird.getY(), 5, this.paint);

    }

    private void updatePipeQueue() {

        int frontPipe = this.pipeIndexQueue.peek();

        /*if (this.pipeList[frontPipe].getX() <= 55) {
            this.score.increase();
        }*/

        if (this.pipeList[frontPipe].getX() <= -10) {
            frontPipe = this.pipeIndexQueue.remove();
            this.pipeIndexQueue.add(frontPipe);
            this.score.increase();
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


    public boolean getInitComponents(){

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
        }
        catch (Exception e) {
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

        this.score = new Score(getContext(), getWidth()/2 - 30, getWidth()/2 + 30, getHeight() / 5);
        this.bird = new Bird(getResources(), 55, (getHeight() / 2) - 150);
        this.bar = new Bar(this.context, 0, 1265, getWidth());
        this.text0 = new Text(context,getWidth() / 5, getHeight() / 2, "Tap to start");
        this.text1 = new Text(context,getWidth() / 5, (getHeight() / 3)+150, "Tap to Restart");
        this.text2 = new Text(context,getWidth() / 5 + 140, getHeight() / 2 + 10, "Best: ");
        this.initComponents = true;

    }

    /*
     * Listener method called when a "tap" event occur
     * */

    @Override
    public boolean onTouchEvent(MotionEvent tap) {

        if (!this.end && tap.getActionMasked() == tap.ACTION_DOWN){
            this.tap = true;
            this.gravity = 0;
        }

        if (!this.start) {
            this.start = true;
        }

        if (this.end && this.bird.getY() >= 1240) {

            this.pipeIndexQueue.clear();
            this.start = true;
            this.end = false;
            this.gravity = 0;
            this.bird.changeStatus();
            this.bird.setY((getHeight() / 2) - 150);
            int pos = getWidth();
            this.score.reset();

            for (int i = 0; i < this.pipeList.length; i++) {
                this.pipeList[i].setX(pos);
                this.pipeList[i].setOpening();
                this.pipeIndexQueue.add(i);
                pos += 320;
            }

        }

        return true;
    }

}
