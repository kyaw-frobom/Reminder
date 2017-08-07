package com.frobom.reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class LocationDetailActivity extends AppCompatActivity {
    public Attributes attToDB;
    private ListView itemsListView;
    public LocationAttributes attLoc;
    private String PathHolder;
    public DatabaseAccessAdapter4Loc datasource;

    private ArrayList<Item> itemList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle data = getIntent().getExtras();
        attLoc = (LocationAttributes) data.getParcelable("attributeObjectLoc");

        //Bundle bundle = getIntent().getExtras();
        //attLoc = (LocationAttributes) bundle.getParcelable("locUpdate");
        datasource = new DatabaseAccessAdapter4Loc(this);
        datasource.open();

        PathHolder = attLoc.getAlarmPath();
        Log.e("PathHolder Loc ", attLoc.getAlarmPath());
        String[] filePath = PathHolder.split("/");
        File file=new File(PathHolder);
        String fileNameFrom=file.getName();
        Log.e("Location ", attLoc.getAlarmLocation());

        itemList.add(new Item("Title", attLoc.getTitle()));
        itemList.add(new Item("Description", attLoc.getDescription()));
        itemList.add(new Item("Radius", String.valueOf(attLoc.getRadius())));
        itemList.add(new Item("Location", attLoc.getAlarmLocation()));
        itemList.add(new Item("Alarm", fileNameFrom));

        // instantiate the custom list adapter
        final CustomListAdapter adapter = new CustomListAdapter(this, itemList);

        // get the ListView and attach the adapter
        itemsListView = (ListView) findViewById(R.id.listview4Loc);
        itemsListView.setAdapter(adapter);
        itemsListView.setFocusable(false);
        itemsListView.setEnabled(false);
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
            case R.id.btn_edit: {
                Intent i = new Intent(LocationDetailActivity.this, LocationEditActivity.class );
                i.putExtra("locationObject", attLoc);
                startActivity(i);
                break;
            }

            case R.id.btn_delete:
                AlertDialog dialog = AskOption();
                dialog.show();
                return true;

        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
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
                        datasource.deleteAttributes(attLoc);
                        dialog.dismiss();
                        Toast.makeText( LocationDetailActivity.this, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LocationDetailActivity.this, MainActivity.class );
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
}
