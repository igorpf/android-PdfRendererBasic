package com.example.android.pdfrendererbasic;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;

import static android.content.ContentValues.TAG;

/**
 * Created by igor on 25/05/17.
 */

public class AccelerometerEventListener implements SensorEventListener {

    private PDFView mPdfView;

    private double offset = 0.0f, scroll=0.0f;
    private final double step = 0.003f;
    private final float threshold =6.2f;
    private final int X=0, Y=1, Z=2;
    private boolean locked;

    public AccelerometerEventListener(PDFView mPdfView){
        this.mPdfView = mPdfView;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(locked)
            return;
        Log.w(TAG, "on: "+event.values[Y]+" offset: "+ offset+" scroll: "+ scroll);
        if(event.values[Y] > threshold) { // anticlockwise
            offset +=step;
        } else if(event.values[Y] > -threshold && event.values[Y] < 3.0) { // clockwise
            offset -=step;
        }
        offset = offset >1? 1: offset <0? 0 : offset;
        mPdfView.setPositionOffset((float) offset);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

}
