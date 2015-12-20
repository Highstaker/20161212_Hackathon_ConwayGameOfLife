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
    private final Paint[] paintColors;

    private Universe universe;

    private Paint[] generatePaintColors()
    {
        final int N_COLORS = 300;//number of colors
        Paint[] colors = new Paint[N_COLORS/2];
        for(int n=0,i = 0; i<N_COLORS;i+=2)
        {
            colors[n] = new Paint();
            colors[n].setColor( Color.HSVToColor( new float[]{i,0.8f,1} ) );
            n++;
        }

        return colors;
    }

    public UniversityView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBlack = new Paint();
        paintBlack.setColor(Color.BLACK);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);

        paintColors = generatePaintColors();
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

        float cellSize = Math.min(canvas.getWidth(),canvas.getHeight()) / universe.SIZE;
        float cellSizeHalf = cellSize / 2f;
        float cellR = cellSizeHalf * 0.9f;

        for(int x = 0; x < universe.SIZE ; x++)
            for(int y = 0; y < universe.SIZE ; y++)
            {
                canvas.drawCircle(x*cellSize+cellSizeHalf
                        ,y*cellSize+cellSizeHalf
                        ,cellR
                        ,universe.isAlive(x,y) ?  ( universe.getCellAge(x,y) < paintColors.length ? paintColors[universe.getCellAge(x,y)] : paintColors[paintColors.length-1]) : paintBlack
                        );
            }

        super.onDraw(canvas);
    }
}
