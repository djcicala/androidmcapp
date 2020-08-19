/*************************************************************************/
/*  File Name: GetLocationFromIP.java                                    */
/*  Purpose: Activity to get approximate location using the IP address.  */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/

package com.example.marketcheckcarsearchapp;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetLocationFromIP extends AsyncTask<Void, Void, String>
{
    String url;
    URL ip_url;
    HttpURLConnection ip_conn;
    MainActivity in;
    double latitude;
    double longitude;

    public GetLocationFromIP(MainActivity in, String url)
    {
        this.url = url;
        this.in = in;
    }
    @Override
    protected String doInBackground(Void... voids)
    {
        BufferedReader ip_reader;
        try
        {
            URL ip_url = new URL(url);
            ip_conn = (HttpURLConnection) ip_url.openConnection();
            ip_conn.setRequestMethod("GET");
            ip_reader = new BufferedReader(new InputStreamReader(ip_conn.getInputStream()));
            String json_str = "", read_line = "";
            while ((read_line = ip_reader.readLine()) != null)
            {
                json_str += read_line;
            }
            JSONObject json_blob = new JSONObject(json_str);
            latitude = json_blob.getDouble("latitude");
            longitude = json_blob.getDouble("longitude");
        }
        catch(MalformedURLException ex)
        {
            //Toast.makeText(getApplicationContext(),"Warning: Malformed URL. Please enter a valid manufacturer", Toast.LENGTH_LONG);
            // make_input.setText("");
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            //Toast.makeText(getApplicationContext(),"Warning: IOException. Please enter a valid manufacturer", Toast.LENGTH_LONG);
            //make_input.setText("");
            ex.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result)
    {
        in.setLocation(latitude, longitude);
    }
}
