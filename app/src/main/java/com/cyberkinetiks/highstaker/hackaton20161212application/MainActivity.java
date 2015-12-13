package com.cyberkinetiks.highstaker.hackaton20161212application;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
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

        helloText = (TextView) findViewById(R.id.helloText);

        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("happy!", "button clicked!");
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
                running = false;

            }
        }); //объект, выполняющий действие при нажатии кнопки

        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                universe.initializeTiles();
                updateText(true);
                universityView.invalidate();

            }
        }); //объект, выполняющий действие при нажатии кнопки

        universe = new Universe();
        universityView = (UniversityView)findViewById(R.id.universityView);
        universityView.setUniverse(universe);

        updateText(true);

    }//onCreate()

    private void updateText(boolean start){
        //updates the text showing time and number of living cells.
        if(start)helloText.setText(getText(R.string.press_start_to_begin) + "\n" + getText(R.string.cells_alive) + " " + universe.aliveCount);
        else helloText.setText(getText(R.string.Time_colon) + " " + universe.time + "\n" + getText(R.string.cells_alive) + " " + universe.aliveCount);
    }

    private void updateText(){
        //overloader to provide argument-less functionality. I'm simply trying to simulate Python-like default parameters.
        updateText(false);
    }

    private void nextStep(){
        universe.doStep();
        universityView.invalidate();
        updateText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(running)
                nextStep();
            }
        }
                ,200);
    }//nextStep()


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }//onSaveInstanceState

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }//onRestoreInstanceState
}