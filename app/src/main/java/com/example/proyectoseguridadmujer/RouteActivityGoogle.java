package com.example.proyectoseguridadmujer;

import android.os.Bundle;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;


import androidx.appcompat.app.AppCompatActivity;


public class RouteActivityGoogle extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the SDK
        //Places.initialize(getApplicationContext(), apiKey);

        // Create a new PlacesClient instance+
        PlacesClient placesClient = Places.createClient(this);
    }

}