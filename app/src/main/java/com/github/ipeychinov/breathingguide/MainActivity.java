package com.github.ipeychinov.breathingguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private SeekBar seekBarIn;
    private SeekBar seekBarOut;

    private TextView seekBarInProgress;
    private TextView seekBarOutProgress;

    private double piModIn;
    private double piModOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVar();

        //setup seekBar listeners
        //breath In bar
        seekBarIn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarInProgress.setText(String.format("%.1f", ((double)progress/10.0 + 3)));
                piModIn = 0.6 + 0.19*(100.0-(double)progress)/10.0;
                //change In graph
                DrawView.adjustInGraph(piModIn);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //breath Out bar
        seekBarOut.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarOutProgress.setText(String.format("%.1f", ((double)progress/10.0 + 3)));
                piModOut = 0.6 + 0.19*(100.0-(double)progress)/10.0;
                //change Out graph
                DrawView.adjustOutGraph(piModOut);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    //Initializes the class members
    private void initVar() {
        seekBarIn = (SeekBar) findViewById(R.id.seekBarIn);
        seekBarOut = (SeekBar) findViewById(R.id.seekBarOut);
        seekBarInProgress = (TextView) findViewById(R.id.maxIn);
        seekBarOutProgress = (TextView) findViewById(R.id.maxOut);

        piModIn = 0.6;
        piModOut = 0.6;
    }
}
