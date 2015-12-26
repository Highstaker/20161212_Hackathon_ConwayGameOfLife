package com.cyberkinetiks.highstaker.hackaton20161212application;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    /////////////////
    ////////PARAMETERS
    /////////////////
    private final int MIN_GRID_SIZE = 6;//minimum grid size you can set
    private final int MAX_GRID_SIZE = 100;//maximum grid size you can set
    private int[] AVAILABLE_SPEEDS = {1, 2, 4, 5, 10, 20, 40, 50, 100};//a list containing possible speeds of simulation

    ///////////
    ///GLOBALS////
    //////////
    private int update_speed = 1;

    private TextView helloText;
    private Universe universe;
    private UniversityView universityView;
    private boolean running = false;
    private Button playButton, stopButton, resetButton, setSizeButton;
    private LineChart mChart;

    ArrayList<Entry> yVals;
    ArrayList<String> xVals;

    private void initializeButtons() {
        playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CKdebug", "playButton pressed");
                if (!running) {
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
                Log.d("CKdebug", "resetButton pressed");
                running = false;
                universe.initializeTiles();
                updateText(true);
                universityView.invalidate();
            }
        }); //объект, выполняющий действие при нажатии кнопки

        setSizeButton = (Button) findViewById(R.id.setSizeButton);
        setSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CKdebug", "setSizeButton pressed");
                running = false;
                openSetSizeDialog();
            }
        }); //объект, выполняющий действие при нажатии кнопки
    }//initializeButtons()

    private void initializeSpeedBar() {
        SeekBar speedSlider = (SeekBar) findViewById(R.id.speedSeekBar);
        speedSlider.incrementProgressBy(1);
        speedSlider.setMax(AVAILABLE_SPEEDS.length - 1);
        speedSlider.setProgress(0);
        final TextView speedValueText = (TextView) findViewById(R.id.speedText);
        speedValueText.setText("Speed " + Integer.toString(update_speed));

        speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                update_speed = AVAILABLE_SPEEDS[progress];
                Log.d("CKdebug", "onProgressChanged() Progress = " + progress + "; Speed = " + Integer.toString(update_speed));
                speedValueText.setText("Speed " + Integer.toString(update_speed));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initializeChart() {
        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setDescription("CHART!");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        XAxis xAxis = mChart.getXAxis();
        YAxis leftAxis = mChart.getAxisLeft();

        //Entry holds a value and position on X-axis
        yVals = new ArrayList<Entry>();
        yVals.add(new Entry(1, 0));
        yVals.add(new Entry(10, 1));
        yVals.add(new Entry(5, 2));
        //Creating a dataset
        LineDataSet set1 = new LineDataSet(yVals, "set1");

        //X-axis labels
        xVals = new ArrayList<String>();
        for (int i = 0; i < yVals.size(); i++) {
            xVals.add((i) + "");
        }

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1);

        LineData data = new LineData(xVals, dataSets);
        mChart.setData(data);

        mChart.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//ссылка на XML с Layout'ом. Класс R генерится системой сборки.
        Log.d("CKdebug", "onCreate() invoked");

        helloText = (TextView) findViewById(R.id.helloText);

        initializeButtons();
        initializeSpeedBar();
        initializeChart();

        //Create universe
        if (savedInstanceState != null) {//save data exists, reload it
            int size = savedInstanceState.getInt("GRID_SIZE");
            universe = new Universe(array_int_1D_to_Square2D(savedInstanceState.getIntArray("CELLS_STATE"), size)
                    , array_int_1D_to_Square2D(savedInstanceState.getIntArray("CELLS_AGES"), size)
                    , size);
        } else {// app started, create random grid
            universe = new Universe();
        }

        universityView = (UniversityView) findViewById(R.id.universityView);
        universityView.setUniverse(universe);

        updateText(true);

    }//onCreate()

    private void openSetSizeDialog() {//opens a size change dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set grid size (" + MIN_GRID_SIZE + " - " + MAX_GRID_SIZE + ")");

        // Set up the input
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("CKdebug", "OK pressed. setting size to " + input.getText().toString());
                int size = Integer.parseInt(input.getText().toString());

                //set limits of size
                if (size > MAX_GRID_SIZE) {
                    universe = new Universe(MAX_GRID_SIZE);
                } else if (size < MIN_GRID_SIZE) {
                    universe = new Universe(MIN_GRID_SIZE);
                } else {
                    universe = new Universe(size);
                }

                //update everything
                universityView.setUniverse(universe);//is this needed?
                universityView.invalidate();
                updateText(true);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CKdebug", "onDestroy() invoked");

    }//onDestroy()

    @Override
    protected void onPause() {
        Log.d("CKdebug", "onPause()");

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("CKdebug", "onStop()");

        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.d("CKdebug", "onResume()");

        super.onResume();
    }

    private void updateText(boolean start) {
        //updates the text showing time and number of living cells.
        if (start)
            helloText.setText(getText(R.string.press_start_to_begin) + "\n" + getText(R.string.cells_alive) + " " + universe.aliveCount);
        else
            helloText.setText(getText(R.string.Time_colon) + " " + universe.time + "\n" + getText(R.string.cells_alive) + " " + universe.aliveCount);
    }//updateText(boolean start)

    private void updateText() {
        //overloader to provide argument-less functionality. I'm simply trying to simulate Python-like default parameters.
        updateText(false);
    }//updateText()

    private void nextStep() {
        //Log.d("CKdebug","nextStep(). running = " + running);
        if (!universe.systemStable) {
            universe.doStep();
            updateGraph();
            universityView.invalidate();
            updateText();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (running)
                        nextStep();
                }
            }
                    , (int) (1000 / update_speed));
        }
    }//nextStep()

    private void updateGraph() {
        int index = xVals.size();
        xVals.add(index + "");
        yVals.add(new Entry(universe.aliveCount,index));

        LineDataSet set1 = new LineDataSet(yVals, "set1");
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1);
        LineData data = new LineData(xVals, dataSets);
        mChart.setData(data);

        mChart.invalidate();//refresh chart

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("CKdebug", "onSaveInstanceState() invoked");
        //put the parameters to be saved into Bundle here

        //saving the running state, so it would resume on screen rotation
        savedInstanceState.putBoolean("RUNNING", running);
        //saving the displayable time of simulation
        savedInstanceState.putInt("GAME_TIME", universe.time);
        //SIZE is no longer static, it has to be saved
        savedInstanceState.putInt("GRID_SIZE", universe.SIZE);
        //Saving the locations of cells
        savedInstanceState.putIntArray("CELLS_STATE", Array_int_Square2D_to_1D(universe.getUniverse(), universe.SIZE));
        //Saving ages of cells
        savedInstanceState.putIntArray("CELLS_AGES", Array_int_Square2D_to_1D(universe.getCellAges(), universe.SIZE));

        //let's stop the process when it is saved. When the data is restored, it will relaunch anyway because `running` is saved.
        running = false;

        super.onSaveInstanceState(savedInstanceState);
    }//onSaveInstanceState

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d("CKdebug", "onRestoreInstanceState() invoked");
        //called after onStart(). Restore the variables here.
        running = savedInstanceState.getBoolean("RUNNING");
        universe.time = savedInstanceState.getInt("GAME_TIME");


        //if the game was running on the moment of destroy, resume it
        if (running) nextStep();
        updateText();

        super.onRestoreInstanceState(savedInstanceState);
    }//onRestoreInstanceState

    public int[] Array_int_Square2D_to_1D(int[][] a, int size) {
        int[] result = new int[size * size];
        for (int i = 0; i < size * size; i++) {
            result[i] = a[i / size][i % size];
        }

        return result;
    }//Array_int_Square2D_to_1D

    private int[][] array_int_1D_to_Square2D(int[] a, int size) {
        int[][] result = new int[size][size];
        for (int i = 0; i < size * size; i++) {
            result[i / size][i % size] = a[i];
        }

        return result;
    }//array_int_1D_to_Square2D

}
