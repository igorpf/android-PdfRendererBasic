package com.example.android.pdfrendererbasic;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;

import static android.content.ContentValues.TAG;

/**
 * Created by igor on 25/05/17.
 */

public class GyroscopeEventListener implements SensorEventListener {

    private PDFView mPdfView;

    private double scroll = 0.0f;

    public GyroscopeEventListener(PDFView mPdfView){
        this.mPdfView = mPdfView;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.w(TAG, "onSensorChanged: "+event.values[0]+" scroll: "+ scroll);
        if(event.values[0] > 0.5f) { // anticlockwise
            scroll+=0.1f;
        } else if(event.values[0] < -0.5f) { // clockwise
            scroll-=0.1f;
        }
        mPdfView.scrollBy(0,(int)Math.floor(scroll));
        mPdfView.fitToWidth();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
