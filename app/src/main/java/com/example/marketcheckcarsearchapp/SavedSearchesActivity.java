/*************************************************************************/
/*  File Name: SavedSearchesActivity.java                                */
/*  Purpose: Activity called when opening the saved searches menu.       */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/

package com.example.marketcheckcarsearchapp;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SavedSearchesActivity extends AppCompatActivity
{
    ListView results;
    ArrayList<ArrayList> mySavedSearches;
    ArrayList<CarListing> http_results;
    ArrayList<String> urls;
    Button clear_all;
    SavedSearchAdapter ssa;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_searches);

        mySavedSearches = new ArrayList<>();
        http_results = new ArrayList<>();
        urls = new ArrayList<>();
        initClearButton();
        //this.clear_all = (Button) findViewById(R.id.clear_all_btn);
        readFileAsString("saved.txt");
        this.results = (ListView) findViewById(R.id.saved_list);
        ssa = new SavedSearchAdapter(this, 0, mySavedSearches);
        results.setAdapter(ssa);
            results.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    String url = urls.get(position);
                    MarketcheckHttpGet new_get = new MarketcheckHttpGet(SavedSearchesActivity.this, url);
                    new_get.execute();
                }
            });
        //System.out.println(str);
    }

    public void readFileAsString(String fileName)
    {
        FileInputStream fis;
        DataInputStream dataInStream;
        try
        {
            fis = openFileInput("saved.txt");
            dataInStream = new DataInputStream(fis);
            String strLine = null;
            while ((strLine = dataInStream.readLine()) != null)
            {
                this.urls.add(strLine);
                this.mySavedSearches.add(parseURL(strLine));
            }
            dataInStream.close();
            fis.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("FileNotFoundException");
        }
        catch (IOException e)
        {
            System.out.println("IOException");
        }
    }

    public ArrayList<String> parseURL(String url)
    {
        ArrayList<String> data = new ArrayList<>();
        String search_params = url.substring(45);
        String[] search_split = search_params.split("&");
        String[] temp;
        String ymmt = "", location = "", tcm = "", etd = "";

        for(int i=0;i<search_split.length; i++)
        {
            if (search_split[i].contains("year"))
            {
                temp = search_split[i].split("=");
                ymmt += temp[1] + " ";
                continue;
            }
            else if (search_split[i].contains("make"))
            {
                temp = search_split[i].split("=");
                ymmt += temp[1] + " ";
                continue;
            }
            else if (search_split[i].contains("model"))
            {
                temp = search_split[i].split("=");
                ymmt += temp[1] + " ";
                continue;
            }
            else if (search_split[i].contains("trim"))
            {
                temp = search_split[i].split("=");
                ymmt += temp[1] + " ";
                continue;
            }
            // end label 0
        }
        for(int i=0;i<search_split.length; i++)
        {
            if (search_split[i].contains("radius"))
            {
                temp = search_split[i].split("=");
                location += temp[1] + " miles from ";
                continue;
            }

            else if (search_split[i].contains("latitude"))
            {
                location += "saved GPS location";
                continue;
            }

            else if (search_split[i].contains("zip"))
            {
                temp = search_split[i].split("=");
                location += temp[1];
                continue;
            }
        }
        for(int i=0;i<search_split.length; i++)
        {
            // end label 1
            if (search_split[i].contains("seller_type"))
            {
                temp = search_split[i].split("=");
                tcm += "Type:" + temp[1] + " ";
                continue;
            }
            else if (search_split[i].contains("color"))
            {
                temp = search_split[i].split("=");
                tcm += "Color: " + temp[1] + " ";
                continue;
            }
            else if (search_split[i].contains("miles_range"))
            {
                temp = search_split[i].split("-");
                tcm += "Max Miles: " + temp[1] + " ";
            }
        }
        if(tcm.contains("Max Miles") == false) tcm += "Max Miles: N/A ";
        if(tcm.contains("Color") == false) tcm += "Color: N/A ";
        if(tcm.contains("Type") == false) tcm += "Type: N/A ";
        for(int i=0;i<search_split.length; i++)
        {
            // end label 3
            if(search_split[i].contains("cylinders"))
            {
                temp = search_split[i].split("=");
                etd += "Engine: " + temp[1] + " ";
                continue;
            }

            else if(search_split[i].contains("transmission"))
            {
                temp = search_split[i].split("=");
                etd += "Transmission: " + temp[1] + " ";
                continue;
            }

            else if(search_split[i].contains("drivetrain"))
            {
                temp = search_split[i].split("=");
                etd += "Drivetrain: " + temp[1] + " ";
                continue;
            }
        }
        if(etd.contains("Engine") == false) etd += "Engine: N/A ";
        if(etd.contains("Transmission") == false) etd += "Transmission: N/A ";
        if(etd.contains("Drivetrain") == false) etd += "Drivetrain: N/A ";
        data.add(ymmt);
        data.add(location);
        data.add(tcm);
        data.add(etd);
        return data;
    }

    public void getListingsArray(ArrayList<CarListing> from_mc)
    {
        http_results = from_mc;
        Intent next_intent = new Intent(SavedSearchesActivity.this, ResultsWindow.class);
        next_intent.putParcelableArrayListExtra("stuff", http_results);
        SavedSearchesActivity.this.startActivity(next_intent);
    }

    public void initClearButton()
    {
        this.clear_all = (Button) findViewById(R.id.clear_all_btn);
        this.clear_all.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("saved.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write("");
                    outputStreamWriter.close();
                    mySavedSearches = new ArrayList<>();
                    ssa.notifyDataSetChanged();
                    finish();
                }
                catch (IOException e)
                {
                    System.out.println("File write failed: " + e.toString());
                }
            }
        });
    }
}
