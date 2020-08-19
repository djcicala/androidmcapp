package com.example.marketcheckcarsearchapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SavedSearchAdapter extends ArrayAdapter<ArrayList>
{
    private Context context;
    private ArrayList<ArrayList> searches;

    public SavedSearchAdapter(Context context, int resource, ArrayList<ArrayList> objects)
    {
        super(context, resource, objects);

        this.context = context;
        this.searches = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        //get the car we are displaying
        ArrayList<String> search = this.searches.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.saved_search_layout, null);

        TextView ymmt = (TextView) view.findViewById(R.id.ymmt_lbl);
        TextView location = (TextView) view.findViewById(R.id.location_lbl);
        TextView tcm = (TextView) view.findViewById(R.id.tcm_lbl);
        TextView etd = (TextView) view.findViewById(R.id.tcm_lbl2);

        //set  attributes
        ymmt.setText(search.get(0));
        location.setText(search.get(1));
        tcm.setText(search.get(2));
        etd.setText(search.get(3));

        ymmt.setTextSize(12);
        location.setTextSize(10);
        tcm.setTextSize(10);
        etd.setTextSize(10);

        return view;
    }
}
