package com.nanodregree.arthurmunhoz.famousmovies.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nanodregree.arthurmunhoz.famousmovies.R;
import com.nanodregree.arthurmunhoz.famousmovies.activities.MainActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarMainActivity();
            }
        }, 2500);

    }

    private void mostrarMainActivity()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
