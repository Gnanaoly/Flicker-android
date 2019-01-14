package com.gnanaoly.flickr.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gnanaoly.flickr.R;
import com.gnanaoly.flickr.util.AppLog;

class BaseActivity extends AppCompatActivity{

    private static final String TAG = "BaseActivity";

    static final String FLICKR_QUERY = "FLICKR_QUERY";

    static final String PHOTO_TRANSFER ="PHOTO_TRANSFER";

    static final String PHOTO_TRANSFER_IS_PUBLIC ="PHOTO_TRANSFER_IS_PUBLIC";

    void activateToolbar(boolean enableHome){

        AppLog.logDebug(TAG, "activateToolbar: starts");

        ActionBar actionBar = getSupportActionBar();

        if( actionBar == null){

            Toolbar toolbar = findViewById(R.id.toolbar);

            if(toolbar != null){
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if( actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }

    }
}
