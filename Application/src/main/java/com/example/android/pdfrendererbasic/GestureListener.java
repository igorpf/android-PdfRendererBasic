package com.example.android.pdfrendererbasic;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.github.barteksc.pdfviewer.PDFView;

import static android.content.ContentValues.TAG;

/**
 * Created by igor on 27/05/17.
 */

public class GestureListener implements GestureDetector.OnGestureListener{

    private PDFView mPdfView;
    private float scale = 1, step = 0.05f;
    public GestureListener(PDFView mPdfView) {
        this.mPdfView = mPdfView;
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
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}
