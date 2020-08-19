/*************************************************************************/
/*  File Name: GetAllImages.java                                         */
/*  Purpose: Activity to get all images for a single car listing.        */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/
package com.example.marketcheckcarsearchapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class GetAllImages extends AsyncTask<Void, Void, String>
{
    CarListing selected;
    SingleListingWindow in;
    public GetAllImages(CarListing selected, SingleListingWindow in)
    {
        this.selected = selected;
        this.selected.image_bmps = new ArrayList<>();
        this.in = in;
    }

    @Override
    protected String doInBackground(Void...voids)
    {
        String result = "error";
        for(int i=0;i<this.selected.images.size();i++)
        {
            InputStream is = null;
            Bitmap bmp = null;
            try
            {
                is = (InputStream) new URL(this.selected.images.get(i)).getContent();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return result;
            }
            bmp = BitmapFactory.decodeStream(is);
            this.selected.image_bmps.add(bmp);
        }
        result = "success";
        return result;
    }

    protected void onPostExecute(String result)
    {
        if(result.equals("success")) in.setImageBitmaps(selected);
    }
}
