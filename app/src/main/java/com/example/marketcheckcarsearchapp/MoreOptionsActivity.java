package com.example.marketcheckcarsearchapp;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MoreOptionsActivity extends AppCompatActivity
{
    public Spinner dealer_type_dropdown;
    public Spinner color_dropdown;
    public Spinner mileage_dropdown;
    public Spinner transmission_type_dropdown;
    public Spinner engine_dropdown;
    public Spinner drive_type_dropdown;
    public Resources stored_resources;
    public Button search_button;
    public String url_from_main;

    ArrayList<CarListing> results;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);

        Intent this_intent = getIntent();
        this.url_from_main = this_intent.getStringExtra("url");

        initSellerTypeSpinner();
        initColorSpinner();
        initMileageSpinner();
        initTransmissionSpinner();
        initEngineSpinner();
        initDrivetypeSpinner();
        initSearchButton();
    }

    protected void initSearchButton()
    {
        this.search_button = (Button) findViewById(R.id.search_mo_button);
        this.search_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buildURL();
                MarketcheckHttpGet new_get = new MarketcheckHttpGet(MoreOptionsActivity.this, url_from_main);
                new_get.execute();
            }
        });
    }

    protected void initSellerTypeSpinner()
    {
        this.dealer_type_dropdown = (Spinner) findViewById(R.id.seller_dropdown);
        this.stored_resources = getResources();
        String[] seller_types = stored_resources.getStringArray(R.array.seller_array);
        ArrayAdapter<String> seller_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, seller_types);
        this.dealer_type_dropdown.setAdapter(seller_adapter);
    }

    protected void initColorSpinner()
    {
        this.color_dropdown = (Spinner) findViewById(R.id.color_dropdown);
        String[] color_types = stored_resources.getStringArray(R.array.color_array);
        ArrayAdapter<String> color_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, color_types);
        this.color_dropdown.setAdapter(color_adapter);
    }

    protected void initMileageSpinner()
    {
        this.mileage_dropdown = (Spinner) findViewById(R.id.mileage_dropdown);
        String[] mileage_types = stored_resources.getStringArray(R.array.mileage_array);
        ArrayAdapter<String> mileage_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, mileage_types);
        this.mileage_dropdown.setAdapter(mileage_adapter);
    }

    protected void initTransmissionSpinner()
    {
        this.transmission_type_dropdown = (Spinner) findViewById(R.id.transmission_dropdown);
        String[] transmission_types = stored_resources.getStringArray(R.array.transmission_array);
        ArrayAdapter<String> transmission_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, transmission_types);
        this.transmission_type_dropdown.setAdapter(transmission_adapter);
    }

    protected void initEngineSpinner()
    {
        this.engine_dropdown = (Spinner) findViewById(R.id.engine_dropdown);
        String[] engine_types = stored_resources.getStringArray(R.array.engine_array);
        ArrayAdapter<String> engine_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, engine_types);
        this.engine_dropdown.setAdapter(engine_adapter);
    }

    protected void initDrivetypeSpinner()
    {
        this.drive_type_dropdown = (Spinner) findViewById(R.id.drivetype_dropdown);
        String[] drive_types = stored_resources.getStringArray(R.array.drivetrain_array);
        ArrayAdapter<String> drivetype_adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, drive_types);
        this.drive_type_dropdown.setAdapter(drivetype_adapter);
    }

    protected void buildURL()
    {
        String engine = this.engine_dropdown.getSelectedItem().toString();
        String drivetype = this.drive_type_dropdown.getSelectedItem().toString();
        String transmission = this.transmission_type_dropdown.getSelectedItem().toString();
        String mileage = this.mileage_dropdown.getSelectedItem().toString();
        String color = this.color_dropdown.getSelectedItem().toString();
        String seller = this.dealer_type_dropdown.getSelectedItem().toString();

        if(engine.equals("") == false)
        {
            this.url_from_main += ("&cylinders=" + engine);
        }
        if(drivetype.equals("") == false)
        {
            this.url_from_main += ("&drivetrain=" + drivetype);
        }
        if(transmission.equals("") == false)
        {
            this.url_from_main += ("&transmission=" + transmission);
        }
        if(mileage.equals("") == false)
        {
            this.url_from_main += ("&miles_range=0-" + mileage);
        }
        if(color.equals("") == false)
        {
            this.url_from_main += ("&color=" + color);
        }
        if(seller.equals("") == false)
        {
            this.url_from_main += ("&seller_type=" + seller);
        }
    }

    public void getListingsArray(ArrayList<CarListing> from_mc)
    {
        results = from_mc;
        Intent next_intent = new Intent(MoreOptionsActivity.this, ResultsWindow.class);
        next_intent.putParcelableArrayListExtra("stuff", results);
        next_intent.putExtra("url", this.url_from_main);
        MoreOptionsActivity.this.startActivity(next_intent);
    }

}
