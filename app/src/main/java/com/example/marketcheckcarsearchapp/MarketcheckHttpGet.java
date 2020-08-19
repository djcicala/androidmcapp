/*************************************************************************/
/*  File Name: MarketcheckHttpGet.java                                   */
/*  Purpose: Performs an HTTPGet request with the MC API.                */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/

package com.example.marketcheckcarsearchapp;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MarketcheckHttpGet extends AsyncTask<Void, Void, String>
{
    public String url_string;
    MainActivity in;
    MoreOptionsActivity moin;
    SavedSearchesActivity ssain;
    ArrayList<CarListing> car_list;
    ArrayList<HashMap<String, String>> hash_list;

    public MarketcheckHttpGet(MainActivity in, String url_str)
    {
        this.in = in;
        this.moin = null;
        this.ssain = null;
        this.url_string = url_str;
        car_list = new ArrayList<>();
    }

    public MarketcheckHttpGet(MoreOptionsActivity in, String url_str)
    {
        this.moin = in;
        this.ssain = null;
        this.in = null;
        this.url_string = url_str;
        car_list = new ArrayList<>();
    }

    public MarketcheckHttpGet(SavedSearchesActivity in, String url_str)
    {
        this.ssain = in;
        this.moin = null;
        this.in = null;
        this.url_string = url_str;
        car_list = new ArrayList<>();
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        URL mc_url;
        HttpURLConnection mc_conn;
        BufferedReader read_from_mc;
        String result = "";
        try
        {
            mc_url = new URL(this.url_string);
            mc_conn = (HttpURLConnection) mc_url.openConnection();
            mc_conn.setRequestMethod("GET");
            read_from_mc = new BufferedReader(new InputStreamReader(mc_conn.getInputStream()));
            String json_str = "", read_line = "";
            while ((read_line = read_from_mc.readLine()) != null)
            {
                json_str += read_line;
            }
            JSONObject json_blob = new JSONObject(json_str);
            int num_responses = json_blob.getInt("num_found");
            JSONArray results_array = json_blob.getJSONArray("listings");

            if(num_responses > 0)
            {
                for (int i = 0; i < num_responses; i++)
                {
                    CarListing current_listing = new CarListing(results_array.getJSONObject(i), i);
                    this.car_list.add(current_listing);
                    current_listing.makeHashMap();
                    //this.hash_list.add(current_listing.listview_hashmap);
                }
            }
            result = "success";
        }
        catch (IOException ex)
        {

        }
        catch (NullPointerException ex)
        {

        }
        catch (JSONException ex)
        {

        }
        return result;
    }

    protected void onPostExecute(String result)
    {
        if(result.equals("success"))
        {
            if(this.in == null && this.ssain == null) moin.getListingsArray(car_list);
            else if(this.in == null && this.moin == null) ssain.getListingsArray(car_list);
            else in.getListingsArray(car_list);
        }
    }
}
