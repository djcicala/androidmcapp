/*************************************************************************/
/*  File Name: ResultsWindow.java                                        */
/*  Purpose: Window that appears after performing a query.               */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/

package com.example.marketcheckcarsearchapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ResultsWindow extends AppCompatActivity
{
    ArrayList<CarListing> car_listings;
    //ListAdapter adapter;
    ArrayList<HashMap<String, String>> results_hash;
    ListView results_view;
    ListViewArrayAdapter adapter;
    TextView num_results;
    Spinner sortby;
    Resources stored_resources;
    String[] sortby_array;
    Button save_button;
    String url;

    public ResultsWindow()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_window);
        Intent myIntent = getIntent();
        car_listings = myIntent.getParcelableArrayListExtra("stuff");
        url = myIntent.getStringExtra("url");
        this.results_view = (ListView) findViewById(R.id.results_list);
        this.num_results = (TextView) findViewById(R.id.none_found);
        this.num_results.setText(Integer.toString(car_listings.size()) + " results found.");
        initSortbyArray();
        getArrayOfMaps();
        initSaveButton();
        adapter = new ListViewArrayAdapter(this, 0, this.car_listings);
        results_view.setAdapter(adapter);
        results_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CarListing selected = getCarListing(position);
                Intent intent = new Intent(ResultsWindow.this, SingleListingWindow.class);
                intent.putExtra("listing", selected);
                ResultsWindow.this.startActivity(intent);
            }
        });
        SetFirstImage sfm = new SetFirstImage(car_listings, this, adapter);
        sfm.execute();
    }

    protected void getArrayOfMaps()
    {
         this.results_hash = new ArrayList<>();
        for (int i = 0; i < car_listings.size(); i++)
        {
            HashMap<String, String> temp = this.car_listings.get(i).listview_hashmap;
            this.results_hash.add(temp);
        }
    }

    protected void setFirstImage(ArrayList<CarListing> input)
    {
        this.car_listings = input;
        /*for(int i=0;i<this.car_listings.size();i++)
        {
            InputStream is = null;
            try
            {
                is = (InputStream) new URL(this.car_listings.get(i).images.get(0)).getContent();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            Drawable d = Drawable.createFromStream(is, null);
            this.car_listings.get(i).first_image = d;
        } */
    }

    public void refreshView()
    {
        results_view.setVisibility(View.GONE);
        results_view.setVisibility(View.VISIBLE);
        results_view.invalidate();
        adapter.notifyDataSetChanged();
    }

    public CarListing getCarListing(int index)
    {
        CarListing selected = this.car_listings.get(index);
        return selected;
    }

    protected void initSortbyArray()
    {
        this.stored_resources = getResources();
        this.sortby = (Spinner) findViewById(R.id.sortby);
        this.sortby_array = stored_resources.getStringArray(R.array.sortby_params);
        ArrayAdapter<String> sortby_adapter = new ArrayAdapter<String>(ResultsWindow.this, android.R.layout.simple_spinner_dropdown_item, sortby_array);
        this.sortby.setAdapter(sortby_adapter);
        this.sortby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String spinner_val = sortby.getItemAtPosition(position).toString();
                if(spinner_val.equals("Distance (closest)"))
                {
                    Collections.sort(car_listings, CarListing.CompareByDist);
                    adapter.notifyDataSetChanged();
                }
                else if(spinner_val.equals("Mileage (low to high)"))
                {
                    Collections.sort(car_listings, CarListing.CompareByMileage);
                    adapter.notifyDataSetChanged();
                }
                else if(spinner_val.equals("Price (low to high)"))
                {
                    Collections.sort(car_listings, CarListing.CompareByPrice);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Collections.sort(car_listings, CarListing.CompareByYear);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    public void initSaveButton()
    {
        final String local_url = url;
        this.save_button = (Button) findViewById(R.id.save_button);
        this.save_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println(local_url);
                writeToFile(local_url, getApplicationContext());
                Toast.makeText(ResultsWindow.this,"Search saved!", Toast.LENGTH_SHORT).show();
                //System.out.println(url);
                //wtf.execute();
            }
        });
    }
    private void writeToFile(String data, Context context)
    {
        try
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("saved.txt", Context.MODE_APPEND));
            outputStreamWriter.append(data + "\n");
            outputStreamWriter.close();
        }
        catch (IOException e)
        {
            System.out.println("File write failed: " + e.toString());
        }
    }
}
