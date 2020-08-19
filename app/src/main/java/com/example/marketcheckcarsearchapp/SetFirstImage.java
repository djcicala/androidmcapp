package com.example.marketcheckcarsearchapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.transform.Result;

public class SetFirstImage extends AsyncTask<Void, Void, String>
{
    ArrayList<CarListing> listings;
    ResultsWindow in;
    ListViewArrayAdapter adapter;

    public SetFirstImage(ArrayList<CarListing> listings, ResultsWindow in, ListViewArrayAdapter adapter)
    {
        this.listings = listings;
        this.in = in;
        this.adapter = adapter;
    }


    @Override
    protected String doInBackground(Void...voids)
    {
        String result = "error";
        for(int i=0;i<this.listings.size();i++)
        {
            InputStream is = null;
            Bitmap bmp = null;
            try
            {
                if(this.listings.get(i).images.size() > 0) is = (InputStream) new URL(this.listings.get(i).images.get(0)).getContent();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                //return result;
            }
            bmp = BitmapFactory.decodeStream(is);
            this.listings.get(i).first_image = bmp;
            //adapter.notifyDataSetChanged();
            //System.out.println("test");
        }
        result = "success";
        return result;
    }

    protected void onPostExecute(String result)
    {
        if (result.equals("success"))
        {
            in.setFirstImage(this.listings);
            in.refreshView();
        }
    }
}
