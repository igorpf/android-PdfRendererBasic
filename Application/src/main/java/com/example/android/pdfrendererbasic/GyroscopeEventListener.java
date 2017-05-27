package com.example.android.pdfrendererbasic;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;

import static android.content.ContentValues.TAG;

/**
 * Created by igor on 25/05/17.
 */

public class GyroscopeEventListener implements SensorEventListener {

    private PDFView mPdfView;

    private double offset = 0.0f, scroll=0.0f;
    private final double step = 0.003f;
    private final float threshold = 0.2f;
    private final int X=0, Y=1, Z=2;//

    public GyroscopeEventListener(PDFView mPdfView){
        this.mPdfView = mPdfView;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
//        Log.w(TAG, "onSensorChanged: "+event.values[X]+" offset: "+ offset+" scroll: "+ scroll);
        if(event.values[X] > threshold) { // anticlockwise
            offset +=step*event.values[X];
        } else if(event.values[X] < -threshold) { // clockwise
            offset -=step*-event.values[X];
        }
        if(Math.abs(event.values[X]) > threshold) {
            scroll+=event.values[X];
            offset += scroll >= 0? step : -step;
        }
        offset = offset >1? 1: offset <0? 0 : offset;
        mPdfView.setPositionOffset((float) offset);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
