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

        seekBarIn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarInProgress.setText("" + (progress + 3));
                piModIn = 0.6 + 0.19*(10-progress);
                DrawView.adjustInGraph(piModIn);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarOut.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarOutProgress.setText("" + (progress + 3));
                piModOut = 0.6 + 0.19*(10-progress);
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

    private void initVar() {
        seekBarIn = (SeekBar) findViewById(R.id.seekBarIn);
        seekBarOut = (SeekBar) findViewById(R.id.seekBarOut);
        seekBarInProgress = (TextView) findViewById(R.id.maxIn);
        seekBarOutProgress = (TextView) findViewById(R.id.maxOut);

        piModIn = 0.6;
        piModOut = 0.6;
    }
}
