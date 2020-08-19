package com.example.marketcheckcarsearchapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    /* layout objects */
    public Spinner make_dropdown;
    public Spinner model_dropdown;
    public Spinner year_dropdown;
    public Spinner distance_dropdown;
    public Spinner max_year_dropdown;
    public EditText trim_textbox;
    public EditText zip_textbox;
    public Button more_options;
    public Button search_now;
    public String url;

    /* other variables */
    public String[] possible_makes_array;
    public String[] distance_array;
    public Resources stored_resources;
    public String url_base = "https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/";
    public String url_end = "?format=xml";
    public ArrayList<String> years_list;
    public int max_num_searches = 50;

    public double latitude;
    public double longitude;

    public LocationManager locationmanager;
    public LocationListener locationlistener;
    public double gps_lat;
    public double gps_lon;

    public String mc_api_key = "00Kbr7oiG7WwYHpbcletfMZ5TbTkjQaA";
    public String mc_base_url = "https://marketcheck-prod.apigee.net/v1/search?";

    public String ip_api_key = "2ba35a2c76636662fddee0bea01ca348";
    public String ip_base_url = "http://api.ipstack.com/";

    ArrayList<CarListing> results;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        this.model_dropdown = (Spinner) findViewById(R.id.model_dropdown);
        this.trim_textbox = (EditText) findViewById(R.id.trim_textbox);
        this.zip_textbox = (EditText) findViewById(R.id.zip_textbox);

        initMakesArray();
        initYearsArrays();
        initDistanceArray();
        initMoreOptionsButton();
        initSearchButton();
        getLocationFromIP();
    }

    protected void initMakesArray()
    {
        this.stored_resources = getResources();
        this.possible_makes_array = stored_resources.getStringArray(R.array.new_makes_array);
        this.make_dropdown = (Spinner) findViewById(R.id.make_dropdown);
        ArrayAdapter<String> possible_makes_adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, possible_makes_array);
        this.make_dropdown.setAdapter(possible_makes_adapter);
        this.make_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {

                String spinner_val = make_dropdown.getItemAtPosition(position).toString();
                String url_val = spinner_val;
                if(spinner_val.contains(" "))
                {
                    url_val = spinner_val.replace(" ", "%20");
                }
                String nhtsa_url_str = url_base + url_val + url_end;
                NhtsaHttpRequest new_req = new NhtsaHttpRequest(MainActivity.this, nhtsa_url_str, spinner_val);
                new_req.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //do nothing
            }
        });

    }

    protected void initYearsArrays()
    {
        String i_string = "";
        this.years_list = new ArrayList<>();
        this.year_dropdown = (Spinner) findViewById(R.id.year_dropdown);
        this.max_year_dropdown = (Spinner) findViewById(R.id.max_year_dropdown);
        this.years_list.add("");
        for(int i=1980;i<2021;i++)
        {
            i_string = String.valueOf(i);
            this.years_list.add(i_string);
        }

        ArrayAdapter<String> year_adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item, years_list);
        this.year_dropdown.setAdapter(year_adapter);
        this.max_year_dropdown.setAdapter(year_adapter);
        //this.year_min_dropdown.setAdapter(year_adapter);
    }

    protected void initDistanceArray()
    {
        this.distance_dropdown = (Spinner) findViewById(R.id.distance_dropdown);
        this.distance_array = stored_resources.getStringArray(R.array.distance_array);
        ArrayAdapter<String> distance_adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item, distance_array);
        this.distance_dropdown.setAdapter(distance_adapter);
    }

    protected void initMoreOptionsButton()
    {
        this.more_options = (Button) findViewById(R.id.more_params_button);
        this.more_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent next_intent = new Intent(MainActivity.this, MoreOptionsActivity.class);
                String url = buildURL();
                if(url.equals("") == false)
                {
                    next_intent.putExtra("url", url);
                    MainActivity.this.startActivity(next_intent);
                }
            }
        });
    }

    protected void getLocationFromIP()
    {
        String url = ip_base_url + "check?access_key=" + ip_api_key;
        System.out.println(url);
        GetLocationFromIP gl = new GetLocationFromIP(this, url);
        gl.execute();
    }

    public void setModelDropdown(ArrayList<String> returned_from_nhtsa)
    {
        ArrayAdapter<String> returned_models_adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item, returned_from_nhtsa);
        model_dropdown.setAdapter(returned_models_adapter);
    }

    protected void initSearchButton()
    {
        this.search_now = (Button) findViewById(R.id.search_button);
        this.search_now.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buildURL();
                MarketcheckHttpGet new_get = new MarketcheckHttpGet(MainActivity.this, url);
                new_get.execute();
            }
        });
    }

    public String buildURL()
    {
        try
        {
            String make =  make_dropdown.getSelectedItem().toString();
            String model = model_dropdown.getSelectedItem().toString();
            String trim = trim_textbox.getText().toString();
            String year = year_dropdown.getSelectedItem().toString();
            String zip = zip_textbox.getText().toString();
            String radius = distance_dropdown.getSelectedItem().toString();
            String max_year = max_year_dropdown.getSelectedItem().toString();
            String location = "";

            int min_year_int = 0;
            int max_year_int = 0;
            String condition = "";
            String url_make = make;
            String url_model = model;
            String url_trim = trim;
            min_year_int = Integer.parseInt(year);
            if(max_year.equals("") == false) max_year_int = Integer.parseInt(max_year);

            if(min_year_int > 2018) condition = "new";
            else condition = "used";

            if(max_year.equals("") == false)
            {
                year = "";
                for(int i= min_year_int; i<max_year_int+1;i++)
                {
                    year += Integer.toString(i) + ",";
                }
                year = year.substring(0, year.length()-1);
            }

            if(make.contains(" ")) url_make = make.replace(" ", "%20");
            if(model.contains(" ")) url_model = model.replace(" ", "%20");
            if(trim.contains(" ")) url_trim = trim.replace(" ", "%20");

            if(zip.equals(""))
            {
                System.out.println("lat: " + latitude + "long: " + longitude);
                location = "&latitude=" + Double.toString(latitude) + "&longitude=" + Double.toString(longitude);
            }
            else location = "&zip=" + zip;

            if(trim.equals(""))
            {
                String url = mc_base_url + "&api_key=" + mc_api_key + "&year=" + year + "&make=" + url_make + "&model=" + url_model +
                        "&car_type=" + condition + "&start=0&rows=" + max_num_searches + "&country=US" + "&radius=" + radius + location;
                System.out.println(url);
                this.url = url;
                return url;
            }
            else
            {
                 String url = mc_base_url + "&api_key=" + mc_api_key + "&year=" + year + "&make=" + url_make + "&model=" + url_model +
                            "&trim=" + url_trim + "&car_type=" + condition + "&start=0&rows=" + max_num_searches + "&country=US" + "&radius=" + radius + location;
                 System.out.println(url);
                 this.url = url;
                 return url;
            }
        }
        catch(NullPointerException ex)
        {
            Toast.makeText(MainActivity.this, "Please fill in all of the required fields", Toast.LENGTH_LONG).show();
            return "";
        }
        catch(NumberFormatException ex)
        {
            Toast.makeText(MainActivity.this, "Please fill in all of the required fields", Toast.LENGTH_LONG).show();
            return "";
        }
    }

    public void getListingsArray(ArrayList<CarListing> from_mc)
    {
        results = from_mc;
        Intent next_intent = new Intent(MainActivity.this, ResultsWindow.class);
        next_intent.putParcelableArrayListExtra("stuff", results);
        next_intent.putExtra("url", this.url);
        //System.out.println(url);
        MainActivity.this.startActivity(next_intent);
    }

    public void setLocation(double lat, double lon)
    {
        this.latitude = lat;
        this.longitude = lon;
    }

}
