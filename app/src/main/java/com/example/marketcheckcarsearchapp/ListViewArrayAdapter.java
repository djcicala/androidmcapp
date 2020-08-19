/*************************************************************************/
/*  File Name: ListViewArrayAdapter.java                                 */
/*  Purpose: Adapter to render a car listing in a list view.             */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/

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

public class ListViewArrayAdapter extends ArrayAdapter<CarListing>
{
    private Context context;
    private List<CarListing> cars;

    public ListViewArrayAdapter(Context context, int resource, ArrayList<CarListing> objects)
    {
        super(context, resource, objects);

        this.context = context;
        this.cars = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        //get the car we are displaying
        CarListing car = this.cars.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listing_layout, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView information = (TextView) view.findViewById(R.id.information);
        TextView dealer = (TextView) view.findViewById(R.id.dealer);
        TextView distance = (TextView) view.findViewById(R.id.distance);
        ImageView first_image = (ImageView) view.findViewById(R.id.first_image);

        //set  attributes
        title.setText(car.listview_hashmap.get("title"));
        information.setText(car.listview_hashmap.get("information"));
        dealer.setText(car.listview_hashmap.get("dealer"));
        distance.setText(car.listview_hashmap.get("distance"));
        first_image.setImageBitmap(car.first_image);//drawables.get(0));
        first_image.postInvalidate();

        if(title.getText().toString().length() > 40) title.setTextSize(12);
        if(information.getText().toString().length() > 30) information.setTextSize(10);
        if(dealer.getText().toString().length() > 30) dealer.setTextSize(10);
        if(distance.getText().toString().length() > 30) distance.setTextSize(10);

        return view;
    }
}
