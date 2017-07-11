package com.frobom.reminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by Lenovo on 6/30/2017.
 */

public class CustomListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Item> items; //data source of the list adapter

    TextView titleName;
    TextView titleDescription;

    //public constructor
    public CustomListAdapter(Context context, ArrayList<Item>  items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_item, parent, false);
        }

        // get current item to be displayed
        Item currentItem = (Item) getItem(position);

        // get the TextView for item name and item description
         titleName = (TextView) convertView.findViewById(R.id.txtTitle);
         titleDescription = (TextView) convertView.findViewById(R.id.txtData);

        //sets the text for item name and item description from the current item object
        titleName.setText(currentItem.getTitle());
        titleDescription.setText(currentItem.getDescription());

        // returns the view for the current row
        return convertView;
    }

}