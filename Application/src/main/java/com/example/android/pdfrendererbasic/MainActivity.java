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

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;

public class MainActivity extends AppCompatActivity {

    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";
    private static final int OPEN_FILE = 0;
    private boolean isLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_real);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PdfRendererFragment(),
                            FRAGMENT_PDF_RENDERER_BASIC)
                    .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                //Search and open file
                Intent i2 = new Intent(getApplicationContext(), FileChooser.class);
                i2.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
                startActivityForResult(i2,OPEN_FILE);
                return true;
            case R.id.action_info:
                //Show help
                Dialog dialog = new Dialog(this, R.style.AppTheme);
                dialog.setContentView(R.layout.help_dialog);
                dialog.show();
            case R.id.action_lock:
                isLocked = true;
                supportInvalidateOptionsMenu();
                return true;
            case R.id.action_unlock:
                isLocked = false;
                supportInvalidateOptionsMenu();
                return true;
            case R.id.action_SetPosition:
                AccelerometerEventListener.setDefaulPositioning();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem lock = menu.findItem(R.id.action_lock);
        MenuItem unlock = menu.findItem(R.id.action_unlock);

        lock.setVisible(!isLocked);
        unlock.setVisible(isLocked);
        ((PdfRendererFragment) getSupportFragmentManager().findFragmentById(R.id.container)).setLocked(isLocked);
        AccelerometerEventListener.setDefaulPositioning();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_FILE && data!=null) {
            if (resultCode == RESULT_OK) {
                Uri file = data.getData();
                if(isValidFile(file.toString())){
                    ((PdfRendererFragment) getSupportFragmentManager().findFragmentById(R.id.container)).openFile(file);
                } else {
                    Toast.makeText(this, "Arquivo inválido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private boolean isValidFile(String name){return name != null && name.endsWith(".pdf");}
    public void changeLock() {
        isLocked = !isLocked;
    }
}
