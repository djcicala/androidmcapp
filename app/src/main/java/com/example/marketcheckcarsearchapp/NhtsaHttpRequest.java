/*************************************************************************/
/*  File Name: NhtsaHttpRequest.java                                     */
/*  Purpose: Activity for HTTP request to NHTSA API.                     */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/

package com.example.marketcheckcarsearchapp;

import android.os.AsyncTask;
import android.widget.EditText;

import com.example.marketcheckcarsearchapp.MainActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class NhtsaHttpRequest extends AsyncTask<Void, Void, String>
{
    String nhtsa_url_str;
    String make_input;
    MainActivity in;

    protected NhtsaHttpRequest(MainActivity in, String nhtsa_url_str, String make_input)
    {
        this.in = in;
        this.nhtsa_url_str = nhtsa_url_str;
        this.make_input = make_input;
    }

    @Override
    public String doInBackground(Void... voids)
    {
        /* parameters to set when the button is clicked */
        URL nhtsa_url;
        HttpURLConnection nhtsa_conn;
        BufferedReader read_from_nhtsa;
        StringBuffer buffer;
        String xml_str = "";
        try
        {
            nhtsa_url = new URL(nhtsa_url_str);
            nhtsa_conn = (HttpURLConnection) nhtsa_url.openConnection();
            nhtsa_conn.setRequestMethod("GET");
            read_from_nhtsa = new BufferedReader(new InputStreamReader(nhtsa_conn.getInputStream()));
            buffer = new StringBuffer("");
            String line = "";
            while ((line = (String) read_from_nhtsa.readLine()) != null)
            {
                buffer.append(line);
            }
            read_from_nhtsa.close();
            xml_str = buffer.toString();
            nhtsa_conn.disconnect();
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
        return xml_str;
    }
    protected void onPostExecute(String xml_str)
    {
        try
        {
            String tag = "";
            String contents = "";
            String make = "";
            String model = "";
            ArrayList<String> output_list = new ArrayList();

            XmlPullParserFactory parser_factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parser_factory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(new StringReader(xml_str));

            int event = parser.getEventType();
            while (event!= XmlPullParser.END_DOCUMENT)
            {
                tag = parser.getName();
                switch (event)
                {
                    case XmlPullParser.START_TAG:
                        if(tag.equals("ModelsForMake"))
                        {
                            model = new String();
                            make = new String();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        contents = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        switch (tag)
                        {
                            case "Make_Name":
                                make = contents;
                                make = make.trim(); // specifically for the Hummer make
                                break;
                            case "Model_Name":
                                model = contents;
                                break;
                            case "ModelsForMake":
                                if(make.equalsIgnoreCase(make_input))
                                    output_list.add(model);
                                break;
                        }
                        break;
                }
                event = parser.next();
            }
            Collections.sort(output_list);
            in.setModelDropdown(output_list);
        }
        catch(XmlPullParserException ex)
        {
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
