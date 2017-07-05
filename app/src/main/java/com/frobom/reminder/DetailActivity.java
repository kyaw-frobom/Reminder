package com.frobom.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;

import static com.frobom.reminder.R.drawable.b;

/**
 * Created by KyawMinHtwe on 7/4/2017.
 */

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();

        ((TextView) findViewById(R.id.txtTitle)).setText(i.getStringExtra("title"));
        ((TextView) findViewById(R.id.txtDescription)).setText( i.getStringExtra("description"));
        ((TextView) findViewById(R.id.txtDate)).setText( i.getStringExtra("date"));
        ((TextView) findViewById(R.id.txtTime)).setText( i.getStringExtra("time"));
        ((TextView) findViewById(R.id.txtPath)).setText( i.getStringExtra("path"));


//        ((TextView) findViewById(R.id.txtTitle)).setTextContent( i.getStringExtra("title"));
//        ((TextView) findViewById(R.id.txtDescription)).setTextContent( i.getStringExtra("description"));
//        ((TextView) findViewById(R.id.txtDate)).setTextContent( i.getStringExtra("date"));
//        ((TextView) findViewById(R.id.txtTime)).setTextContent( i.getStringExtra("time"));
//        ((TextView) findViewById(R.id.txtPath)).setTextContent( i.getStringExtra("path"));

    }

}
