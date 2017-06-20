/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.pdfrendererbasic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

public class PdfRendererFragment extends Fragment  {

    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";
    private static final String FILENAME = "sample.pdf";
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mPageIndex;
    private PDFView mPdfView;
    private AccelerometerEventListener accelerometerEventListener;
    private boolean firstTimeStart = true;
    private Uri currentFile;
    private GestureListener gestureListener;
    public PdfRendererFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pdf_renderer_basic, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Retain view references.
        mPdfView = (PDFView) view.findViewById(R.id.pdfView);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerEventListener = new AccelerometerEventListener(mPdfView);
        mSensorManager.registerListener(accelerometerEventListener, mSensor, 20*1000);//20ms
        mPageIndex = 0;
        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mPdfView.useBestQuality(true);
            PDFView.Configurator c = (firstTimeStart || currentFile == null? mPdfView.fromAsset(FILENAME):mPdfView.fromUri(currentFile));
                c
                    .enableSwipe(false) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(false)
                    .defaultPage(0)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    .load();
            gestureListener = new GestureListener(mPdfView, this);
            final GestureDetector gestureDetector = new GestureDetector(getActivity(), gestureListener);
            mPdfView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        mPdfView.recycle();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_PAGE_INDEX, mPdfView.getCurrentPage());

    }

    public void setLocked(boolean locked) {
        accelerometerEventListener.setLocked(locked);
    }

    public void openFile(Uri uri) {
        mPdfView.recycle();
        currentFile = uri;
        gestureListener.restartScale();
        accelerometerEventListener.restartPosition();
        firstTimeStart = false;
    }

}
