package com.example.android.pdfrendererbasic;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import com.github.barteksc.pdfviewer.PDFView;
import static android.content.ContentValues.TAG;
public class AccelerometerEventListener implements SensorEventListener {

    private PDFView mPdfView;

    private double offset = 0.0f;
    private final double step = 0.002f;
    private final float verticalScrollThreshold =6.2f,horizontalScrollThreshold =1.6f;
    private final int X=0, Y=1, Z=2;
    private boolean locked;
    private static double[] current_position = {0,0};
    private static double[] default_position = {0,0};
    private final double[] scroll_weight = {0, 1, 1.2, 1.4, 1.6, 2, 2.5, 3, 5, 7, 9, 15, 17, 20, 25, 30};

    public AccelerometerEventListener(PDFView mPdfView){
        this.mPdfView = mPdfView;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(locked)
            return;
        //double aux = event.values[Y]-default_position[1];
        //Log.w(TAG, "Real Y: "+event.values[Y]+"\tY Default: "+default_position[1]+"\tAdjusted Y: "+aux+"\toffset: "+ offset+"\tscroll: "+ scroll);

        if (event.values[Y] > 0 && (int)(event.values[Y]-default_position[1]) >= 0)
            offset += step * scroll_weight[(int) (event.values[Y] - default_position[1])] / GestureListener.getScale();
        else
            offset -= step * scroll_weight[-(int) (event.values[Y] - default_position[1])] / GestureListener.getScale();

        int scroll = 5,limit=150*(int)(GestureListener.getScale()/5);
        if(event.values[X] - default_position[0] > horizontalScrollThreshold) {
            int s = -scroll+mPdfView.getScrollX();
            mPdfView.setScrollX(s<-limit?-limit:s);
        } else if(event.values[X] < -horizontalScrollThreshold) {
            int s = scroll + mPdfView.getScrollX();
            mPdfView.setScrollX(s > limit? limit:s);
        }

        offset = offset >1? 1: offset <0? 0 : offset;
        mPdfView.setPositionOffset((float) offset);

        current_position[0] = event.values[X];
        current_position[1] = event.values[Y];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public static void setDefaulPositioning(){
        default_position[0] = current_position[0];
        default_position[1] = current_position[1];
    }
    public void restartPosition(){
        offset = 0;
        mPdfView.scrollTo(0,0);
    }
}
