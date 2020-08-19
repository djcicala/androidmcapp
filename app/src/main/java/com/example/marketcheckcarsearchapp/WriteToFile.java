/*************************************************************************/
/*  File Name: WriteToFile.java                                          */
/*  Purpose: Writes saved searches to a file.                            */
/*  Created by: Darren Cicala on 3/1/19.                                 */
/*  Copyright © 2019 Darren Cicala. All rights reserved.                 */
/*************************************************************************/

package com.example.marketcheckcarsearchapp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static android.content.Context.MODE_PRIVATE;

public class WriteToFile extends AsyncTask
{
    String url;
    Context context;

    public WriteToFile(String url, Context in)
    {
        this.url = url;
        this.context = in;
    }

    @Override
    protected Object doInBackground(Object[] objects)
    {
        //FileOutputStream fos;
        try
        {
            File fout = new File(context.getFilesDir(), "saved.txt");
            FileOutputStream fos = new FileOutputStream(fout, true);
            PrintStream printstream = new PrintStream(fos);
            printstream.print(url+"\n");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
