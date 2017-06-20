package com.example.android.pdfrendererbasic;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.github.barteksc.pdfviewer.PDFView;

import static android.content.ContentValues.TAG;

public class GestureListener extends GestureDetector.SimpleOnGestureListener implements GestureDetector.OnGestureListener {

    private PDFView mPdfView;
    private static float scale = 1, step = 0.03f;
    private Fragment fragment;
    public GestureListener(PDFView mPdfView, Fragment fragment) {
        this.mPdfView = mPdfView;
        this.fragment = fragment;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(distanceY>120)
            Log.w(TAG, "onscroll: x: "+distanceX+"y: "+distanceY);
        if(distanceY>0) {
            scale+=step*Math.sqrt(Math.abs(distanceY));
        }
        else {
            scale-=step*Math.sqrt(Math.abs(distanceY));
        }
        if(scale<1)
            scale=1;
        else if(scale>4)
            scale=4;
        mPdfView.setScaleX(scale);
        mPdfView.setScaleY(scale);

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        AccelerometerEventListener.setDefaulPositioning();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        MainActivity activity = (MainActivity)((PdfRendererFragment) fragment).getActivity();
        activity.changeLock();
        activity.supportInvalidateOptionsMenu();
        return true;
    }

    public static float getScale(){return scale*5;}

    public void restartScale(){
        scale=1;
        mPdfView.setScaleX(scale);
        mPdfView.setScaleY(scale);
    }

}
