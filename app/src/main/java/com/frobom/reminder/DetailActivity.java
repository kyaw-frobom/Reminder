package com.frobom.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.btn_edit:
                return true;

            case R.id.btn_delete:
                return true;
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


}
