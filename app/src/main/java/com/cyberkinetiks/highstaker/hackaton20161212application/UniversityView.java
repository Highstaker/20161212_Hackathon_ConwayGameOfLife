package com.cyberkinetiks.highstaker.hackaton20161212application;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by highstaker on 12.12.15.
 */
public class UniversityView extends View {

    private final Paint paintBlack, paintWhite;

    private Universe universe;

    public UniversityView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBlack = new Paint();
        paintBlack.setColor(Color.BLACK);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
    }

    public void setUniverse(Universe u)
    {
        universe = u;
    }

    @Override
    protected void onDraw(Canvas canvas) {//отрисовка
        //Можно оверрайдить чтобы сделать свой вариант элемента вместо стандартного.
        //https://developer.android.com/intl/ru/training/custom-views/custom-drawing.html

        canvas.drawColor(Color.LTGRAY);

        float cellSize = Math.min(canvas.getWidth(),canvas.getHeight()) / Universe.SIZE;
        float cellSizeHalf = cellSize / 2f;
        float cellR = cellSizeHalf * 0.9f;

        for(int x = 0; x < Universe.SIZE ; x++)
            for(int y = 0; y < Universe.SIZE ; y++)
            {
                canvas.drawCircle(x*cellSize+cellSizeHalf
                        ,y*cellSize+cellSizeHalf
                        ,cellR
                        ,universe.isAlive(x,y) ?  paintWhite : paintBlack);
            }

        super.onDraw(canvas);
    }
}
