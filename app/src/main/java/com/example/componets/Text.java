package com.example.componets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

import com.example.flappybird.R;

public class Text {

    private String initText = "Tap to start";
    private Typeface font;
    private Paint paint;
    private int x, y;

    public Text(int x, int y, Context context) {

        this.x = x;
        this.y = y;
        this.font = ResourcesCompat.getFont(context, R.font.brabunr);
        this.paint = new Paint();
        this.paint.setTypeface(this.font);
        this.paint.setARGB(255,175,238,238);
        this.paint.setTextSize(120);

    }

    public void draw(Canvas canvas) {

        canvas.drawText(this.initText, this.x, this.y, this.paint);

    }
}
