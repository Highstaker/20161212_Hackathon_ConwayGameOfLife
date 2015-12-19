package com.cyberkinetiks.highstaker.hackaton20161212application;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private TextView helloText;
    private Universe universe;
    private UniversityView universityView;
    private boolean running = false;
    private Button playButton, stopButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//ссылка на XML с Layout'ом. Класс R генерится системой сборки.
        Log.d("CKdebug","onCreate() invoked");

        helloText = (TextView) findViewById(R.id.helloText);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CKdebug", "playButton pressed");
                if(!running) {
                    running = true;
                    nextStep();//start playing
                }
            }
        }); //объект, выполняющий действие при нажатии кнопки

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CKdebug", "stopButton pressed");
                running = false;
//                Log.d("CKdebug", "stopButton pressed. End function. running = " + running);

            }
        }); //объект, выполняющий действие при нажатии кнопки

        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CKdebug", "playButton pressed");
                running = false;
                universe.initializeTiles();
                updateText(true);
                universityView.invalidate();

            }
        }); //объект, выполняющий действие при нажатии кнопки

        if(savedInstanceState != null)
        {//save data exists, reload it
            universe = new Universe( array_int_1D_to_Square2D(savedInstanceState.getIntArray("CELLS_STATE"),Universe.SIZE ) );
        }
        else
        {// app started, create random grid
            universe = new Universe();
        }
        
        universityView = (UniversityView)findViewById(R.id.universityView);
        universityView.setUniverse(universe);

        updateText(true);

    }//onCreate()

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CKdebug","onDestroy() invoked");

    }//onDestroy()

    @Override
    protected void onPause() {
        Log.d("CKdebug","onPause()");

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("CKdebug","onStop()");

        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.d("CKdebug","onResume()");

        super.onResume();
    }

    private void updateText(boolean start){
        //updates the text showing time and number of living cells.
        if(start)helloText.setText(getText(R.string.press_start_to_begin) + "\n" + getText(R.string.cells_alive) + " " + universe.aliveCount);
        else helloText.setText(getText(R.string.Time_colon) + " " + universe.time + "\n" + getText(R.string.cells_alive) + " " + universe.aliveCount);
    }//updateText(boolean start)

    private void updateText(){
        //overloader to provide argument-less functionality. I'm simply trying to simulate Python-like default parameters.
        updateText(false);
    }//updateText()

    private void nextStep(){
        Log.d("CKdebug","nextStep(). running = " + running);
        if(!universe.systemStable) {
            universe.doStep();
            universityView.invalidate();
            updateText();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (running)
                        nextStep();
                }
            }
                    , 200);
        }
    }//nextStep()


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("CKdebug","onSaveInstanceState() invoked");
        //put the parameters to be saved into Bundle here
        savedInstanceState.putBoolean("RUNNING",running);
        savedInstanceState.putInt("GAME_TIME",universe.time);
        savedInstanceState.putIntArray("CELLS_STATE",Array_int_Square2D_to_1D(universe.getUniverse(), Universe.SIZE));

        //let's stop the process when it is saved. When the data is restored, it will relaunch anyway because `running` is saved.
        running = false;

        super.onSaveInstanceState(savedInstanceState);
    }//onSaveInstanceState

    public int[] Array_int_Square2D_to_1D(int[][] a,int size) {
        int[] result = new int[size*size];
        for (int i = 0; i < size*size; i++)
        {
            result[i] = a[i/size][i%size];
        }

        return result;
    }//Array_int_Square2D_to_1D

    private int[][] array_int_1D_to_Square2D(int[] a, int size) {
        int[][] result = new int[size][size];
        for(int i = 0;i < size*size ; i++)
        {
            result[i/size][i%size] = a[i];
        }

        return result;
    }//array_int_1D_to_Square2D

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d("CKdebug","onRestoreInstanceState() invoked");
        //called after onStart(). Restore the variables here.
        running = savedInstanceState.getBoolean("RUNNING");
        universe.time = savedInstanceState.getInt("GAME_TIME");


        //if the game was running on the moment of destroy, resume it
        if(running)nextStep();
        updateText();

        super.onRestoreInstanceState(savedInstanceState);
    }//onRestoreInstanceState
}