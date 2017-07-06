package com.frobom.reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by KyawMinHtwe on 7/4/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private DatabaseAccessAdapter adapter;
    public Attributes att;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        adapter = new DatabaseAccessAdapter(this);
        adapter.open();

        Bundle data = getIntent().getExtras();
        att = (Attributes) data.getParcelable("attributeObject");

      /* ((TextView) findViewById(R.id.txtTitle)).setText( att.getTitle());
        ((TextView) findViewById(R.id.txtDescription)).setText( att.getDescription());
        ((TextView) findViewById(R.id.txtDate)).setText( att.getAlarmDate());
        ((TextView) findViewById(R.id.txtTime)).setText( att.getAlarmTime());
        ((TextView) findViewById(R.id.txtPath)).setText( att.getAlarmPath());*/

        //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Warning")
                .setMessage("Are you sure to delete?")
                .setIcon(R.drawable.ic_action_warning)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        adapter.deleteAttributes(att);
                        dialog.dismiss();
                        Toast.makeText( DetailActivity.this, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(DetailActivity.this, MainActivity.class );
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
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
                AlertDialog dialog = AskOption();
                dialog.show();
                return true;
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


}
