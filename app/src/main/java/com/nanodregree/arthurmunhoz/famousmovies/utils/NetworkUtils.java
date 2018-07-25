package com.nanodregree.arthurmunhoz.famousmovies.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.nanodregree.arthurmunhoz.famousmovies.R;
import com.nanodregree.arthurmunhoz.famousmovies.model.Movie;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Arthur on 30/03/2018.
 */

@SuppressLint("Registered")
public class NetworkUtils extends AppCompatActivity
{
    /*************************************
     *
     * IS CONNECTION AVAILABLE
     *
     * @param context
     * @return
     */
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo i = null;
        if (conMgr != null)
        {
            i = conMgr.getActiveNetworkInfo();
        }

        return i != null && i.isConnected() && i.isAvailable();
    }

    /**************************************
     *
     * MAKE HTTP REQUEST
     *
     * @param context
     * @param url
     * @return
     */
    public static JSONArray makeHttpRequest(Context context, String url, View view)
    {
        /* PREPARING THE HTTP REQUEST */
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = null;

        OkHttpClient client = new OkHttpClient();
        JSONArray jsonResultsArray = null;
        JSONObject jsonObject = null;

        /* MAKING THE HTTP REQUEST */
        try
        {
            response = client.newCall(request).execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (response != null)
        {
            if (response.isSuccessful())
            {
                String jsonData = null;
                try
                {
                    jsonData = response.body().string();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                try
                {
                    jsonObject = new JSONObject(jsonData);
                    jsonResultsArray = jsonObject.getJSONArray("results");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                showConnectionErrorMsg(context, view);
            }
        }
        else
        {
            showConnectionErrorMsg(context, view);
        }

        return jsonResultsArray;
    }

    public static void showConnectionErrorMsg(Context context, View view)
    {
        //Show snackbar connection message

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.makeText(context, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        //toast.setDuration(Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.BOTTOM, 0, 0);
        //toast.setText(context.getString(R.string.error_message));
        //toast.show();

        //Toast.makeText(context, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        //Snackbar.make(view, context.getString(R.string.error_message), Snackbar.LENGTH_LONG).show();
    }

}
