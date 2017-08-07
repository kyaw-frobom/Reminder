package com.frobom.reminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

/**
 * Created by ソーイエーリン on 01-Aug-17.
 */

public class AddLocationActivity extends AppCompatActivity{

    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        addListenerOnSpinnerItemSelection();
    }


    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
}
