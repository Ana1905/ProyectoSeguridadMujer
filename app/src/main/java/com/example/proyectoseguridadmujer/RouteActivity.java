package com.example.proyectoseguridadmujer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;

public class RouteActivity extends AppCompatActivity {

    private static final String TAG = RouteActivity.class.getSimpleName();
    MapView mMapView;           //Declaracion de la instancia del mapa de Here Maps.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        // Get a MapView instance from the layout.
        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        mMapView.setOnReadyListener(new MapView.OnReadyListener() {
            @Override
            public void onMapViewReady() {
                // This will be called each time after this activity is resumed.
                // It will not be called before the first map scene was loaded.
                // Any code that requires map data may not work as expected beforehand.
                Log.d(TAG, "HERE Rendering Engine attached.");
            }
        });
        loadMapScene();

    }

    //Metodo para cargar el mapa por primera vez:
    private void loadMapScene() {
        // Load a scene from the HERE SDK to render the map with a map scheme.
        mMapView.getMapScene().loadScene(MapScheme.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if (mapError == null) {
                    double distanceInMeters = 1000 * 10;
                    mMapView.getCamera().lookAt(
                            new GeoCoordinates(20.67697, -103.34749), distanceInMeters);
                } else {
                    Log.d(TAG, "Loading map failed: mapError: " + mapError.name());
                }
            }
        });
    }

    //onBackPressed:
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(RouteActivity.this);
        startActivity(intent);
        finish();
    }
}
