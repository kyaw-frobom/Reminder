package com.frobom.reminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabWidget;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("TODAY");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("TOMORROW");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("UPCOMING");

        tab1.setIndicator("TODAY");
        tab1.setContent(R.id.tab1);

        tab2.setIndicator("TOMORROW");
        tab2.setContent(R.id.tab2);

        tab3.setIndicator("UPCOMING");
        tab3.setContent(R.id.tab3);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
    }
}
