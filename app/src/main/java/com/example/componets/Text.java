package com.example.componets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

import com.example.flappybird.R;

public class Text {

    private String text;
    private Typeface font;
    private Paint paint;
    private int x, y;

    public Text(Context context, int x, int y, String text) {

        this.x = x;
        this.y = y;
        this.text = text;
        this.font = ResourcesCompat.getFont(context, R.font.brabunr);
        this.paint = new Paint();
        this.paint.setTypeface(this.font);
        this.paint.setARGB(255,255, 0, 0);
        this.paint.setTextSize(130);

    }

    public void draw(Canvas canvas) {

        canvas.drawText(this.text, this.x, this.y, this.paint);

    }

    public void setText(String text) {
        this.text = text;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
