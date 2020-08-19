/*************************************************************************/
/*  File Name: StartActivity.java                                        */
/*  Purpose: Starts a new activity.                                      */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/

package com.example.marketcheckcarsearchapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity
{
    Button new_search;
    Button saved_search;
    final String TABLE_NAME = "searches";
    final String COLUMN_URL = "Base URL";
    final String COLUMN_YEAR = "Year";
    final String COLUMN_MAKE = "Make";
    final String COLUMN_MODEL = "Model";
    final String COLUMN_LOCATION = "Location";
    final String COLUMN_TRIM = "TRIM";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        this.new_search = (Button) findViewById(R.id.ns_button);
        this.saved_search = (Button) findViewById(R.id.ss_button);

        this.new_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent next_intent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(next_intent);
            }
        });

        this.saved_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent next_intent = new Intent(StartActivity.this, SavedSearchesActivity.class);
                StartActivity.this.startActivity(next_intent);
            }
        });
    }

    /*public void createDB()
    {
        final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MAKE + " TEXT, " +
                COLUMN_MODEL + " TEXT, " +
                COLUMN_LOCATION+ " TEXT," +
                COLUMN_TRIM + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_YEAR + " TEXT," +

                + ")";
    }*/
}
