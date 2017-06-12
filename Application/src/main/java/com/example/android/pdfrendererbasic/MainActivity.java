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
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";
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
                new AlertDialog.Builder(this)
                        .setMessage(R.string.open)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
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
                //Reset default position
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
        return true;
    }
    public void changeLock() {
        isLocked = !isLocked;
    }
}
