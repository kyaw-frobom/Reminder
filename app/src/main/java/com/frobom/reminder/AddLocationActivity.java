package com.frobom.reminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ソーイエーリン on 01-Aug-17.
 */

public class AddLocationActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
