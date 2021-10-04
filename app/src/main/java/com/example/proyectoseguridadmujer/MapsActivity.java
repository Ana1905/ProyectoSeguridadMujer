package com.example.proyectoseguridadmujer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.android.PolyUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import httpurlconnection.PutData;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Button mBotonCambiarVista, mBotonCrearReporte, mBotonCancelarReporte, mBotonRutaOrigen, mBotonRutaDestino, mBotonEliminarPuntosIntermedios;
    EditText mETLatitud, mETLongitud, mETDescripcion;
    Spinner mSpinnerCategoria, mSpinnerRadio, mSpinnerTransporte;
    TextView mTVCategoria, mTVRadio, mTVTransporte, mTVTiempoEstimado, mTVDistancia;

    boolean creandoReporteAcontecimiento = false;
    boolean circuloReporteAcontecimientoDibujado = false;
    boolean origenRutaSeleccionado = false;
    boolean destinoRutaSeleccionado = false;
    boolean posicionActual = true;
    boolean rutaTrazada = false;
    String email;
    String categoria = "";
    String medioTransporte = "driving";
    String indicacionPuntosIntermedios = "";
    int radioInt = 0;
    int positionCategoria = 0;

    RequestQueue requestQueue;

    Circle circuloReporteAcontecimiento;
    LatLng mMiUbicacion;
    LatLng mOrigenRuta;
    LatLng mDestinoRuta;

    List<ReporteAcontecimiento> mReportesAcontecimiento = new ArrayList<>();
    List<LatLng> mPuntosIntermedios = new ArrayList<>();

    private static final int FROM_REQUEST_CODE = 1;
    private static final int TO_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Wiring Up:
        mBotonCambiarVista = findViewById(R.id.boton_cambiar_vista_reporte);
        mBotonCrearReporte = findViewById(R.id.boton_crear_reporte);
        mBotonCancelarReporte = findViewById(R.id.boton_cancelar_reporte);
        mBotonRutaOrigen = findViewById(R.id.boton_origen);
        mBotonRutaDestino = findViewById(R.id.boton_destino);
        mBotonEliminarPuntosIntermedios = findViewById(R.id.boton_eliminar_waypoints);
        mETLatitud = findViewById(R.id.edit_text_latitud);
        mETLongitud = findViewById(R.id.edit_text_longitud);
        mETDescripcion = findViewById(R.id.edit_text_descripcion);
        mSpinnerCategoria = findViewById(R.id.spinner_categoria);
        mSpinnerRadio = findViewById(R.id.spinner_area_reporte);
        mSpinnerTransporte = findViewById(R.id.spinner_transporte);
        mTVCategoria = findViewById(R.id.categoriaLabel);
        mTVRadio = findViewById(R.id.radioLabel);
        mTVTransporte = findViewById(R.id.transporteLabel);
        mTVTiempoEstimado = findViewById(R.id.tiempoEstimadoLabel);
        mTVDistancia = findViewById(R.id.distanciaLabel);

        //Spinner de la categoria:
        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this,
                R.array.array_categorias_reporte_acontecimiento, android.R.layout.simple_spinner_item);
        mSpinnerCategoria.setAdapter(adapterCategoria);
        mSpinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    //Toast.makeText(getApplicationContext(), categoria, Toast.LENGTH_LONG).show();
                    categoria = parent.getItemAtPosition(position).toString();
                }
                positionCategoria = position;

                if (circuloReporteAcontecimientoDibujado) {
                    colorearCirculo(circuloReporteAcontecimiento, positionCategoria);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner del radio:
        ArrayAdapter<CharSequence> adapterRadio = ArrayAdapter.createFromResource(this,
                R.array.array_area_reporte, android.R.layout.simple_spinner_item);
        mSpinnerRadio.setAdapter(adapterRadio);
        mSpinnerRadio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        radioInt = 0;
                        break;
                    case 1:
                        radioInt = 50;
                        break;
                    case 2:
                        radioInt = 65;
                        break;
                    case 3:
                        radioInt = 80;
                        break;
                    case 4:
                        radioInt = 95;
                        break;
                    case 5:
                        radioInt = 110;
                        break;
                    case 6:
                        radioInt = 125;
                        break;
                    case 7:
                        radioInt = 140;
                        break;
                    case 8:
                        radioInt = 155;
                        break;
                    case 9:
                        radioInt = 170;
                        break;
                    case 10:
                        radioInt = 185;
                        break;
                    case 11:
                        radioInt = 200;
                        break;
                }

                if (!mETLatitud.getText().toString().isEmpty() && !mETLongitud.getText().toString().isEmpty()) {
                    LatLng latLng = new LatLng(Double.parseDouble(mETLatitud.getText().toString()), Double.parseDouble(mETLongitud.getText().toString()));
                    dibujarCirculo(latLng, radioInt);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner del medio de transporte:
        ArrayAdapter<CharSequence> adapterTransporte = ArrayAdapter.createFromResource(this,
                R.array.array_medios_transporte, android.R.layout.simple_spinner_item);
        mSpinnerTransporte.setAdapter(adapterTransporte);
        mSpinnerTransporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        medioTransporte = "driving";
                        break;
                    case 1:
                        medioTransporte = "walking";
                        break;
                    case 2:
                        medioTransporte = "bicycling";
                        break;
                    case 3:
                        medioTransporte = "transit";
                        break;
                }

                if(origenRutaSeleccionado && destinoRutaSeleccionado){
                    definirRuta();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //onClick del boton para crear reporte:
        mBotonCrearReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Valida el correo de la usuaria:
                if (!email.isEmpty()) {
                    //Valida las coordenadas:
                    if (!mETLatitud.getText().toString().isEmpty() && !mETLongitud.getText().toString().isEmpty()) {
                        //Valida el radio:
                        if (radioInt != 0) {
                            //Valida la categoria:
                            if (positionCategoria != 0) {
                                //Valida los comentarios y recomendaciones:
                                if (!mETDescripcion.getText().toString().isEmpty()) {
                                    insertReporteAcontecimiento();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Favor de redactar una descripción o comentario acerca del reporte.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Favor de seleccionar una categoría.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Favor de definir la longitud del radio del area del reporte.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Favor de definir el punto central del area del reporte.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No se ha podido recuperar el correo de la usuaria.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //onClick del boton para cancelar la creacion de un reporte:
        mBotonCancelarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Mantenga presionado sobre el mapa para agregar un punto intermedio en la ruta", Toast.LENGTH_LONG).show();
                visualizarCrearRuta();
            }
        });

        //onClick del boton que habilita la vista para la creacion de un reporte de acontecimiento:
        mBotonCambiarVista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Mantenga presionado sobre el mapa para indicar el centro del area del reporte", Toast.LENGTH_LONG).show();
                visualizarCrearReporte();
            }
        });

        //onClick del boton para seleccionar el origen de la ruta:
        mBotonRutaOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarUbicacion(FROM_REQUEST_CODE);
            }
        });

        //onClick del boton para seleccionar el destino de la ruta:
        mBotonRutaDestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarUbicacion(TO_REQUEST_CODE);
            }
        });

        //onClick del boton para eliminar puntos intermedios:
        mBotonEliminarPuntosIntermedios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPuntosIntermedios.clear();
                indicacionPuntosIntermedios = "";

                mBotonEliminarPuntosIntermedios.setVisibility(View.GONE);

                if(origenRutaSeleccionado && destinoRutaSeleccionado){
                    definirRuta();
                }
                else{
                    mMap.clear();
                    if(origenRutaSeleccionado && mOrigenRuta != null){
                        mMap.addMarker(new MarkerOptions().position(mOrigenRuta).title("Origen de la ruta"));
                    }
                    if(destinoRutaSeleccionado && mDestinoRuta != null){
                        mMap.addMarker(new MarkerOptions().position(mDestinoRuta).title("Destino de la ruta"));
                    }
                }
            }
        });

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Se esconde el formulario para el reporte de acontecimiento:
        mETLatitud.setVisibility(View.GONE);
        mETLongitud.setVisibility(View.GONE);
        mBotonCancelarReporte.setVisibility(View.GONE);
        mTVRadio.setVisibility(View.GONE);
        mSpinnerRadio.setVisibility(View.GONE);
        mETDescripcion.setVisibility(View.GONE);
        mTVCategoria.setVisibility(View.GONE);
        mSpinnerCategoria.setVisibility(View.GONE);
        mBotonCrearReporte.setVisibility(View.GONE);
        mTVTiempoEstimado.setVisibility(View.GONE);
        mTVDistancia.setVisibility(View.GONE);
        mBotonEliminarPuntosIntermedios.setVisibility(View.GONE);

        //Se obtiene el correo de la usuaria:
        getCredentialData();

        //Se obtienen los reportes de acontecimiento en su tiempo de vida:
        obtenerReportesDeAcontecimiento("https://seguridadmujer.com/app_movil/Route/ObtenerReportes.php");

        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyCqzgEMgg81wGjRnOJ7WzJjj79T3LUUVrA");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        Toast.makeText(getApplicationContext(), "Mantenga presionado sobre el mapa para agregar un punto intermedio en la ruta", Toast.LENGTH_LONG).show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Establece el zoom minimo y maximo del mapa:
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);

        //Obtiene localizacion de la usuaria:
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //Inicializa la camara en la ubicacion actual/centro de guadalajara
            LatLng GuadalajaraCentro = new LatLng(20.6769375,-103.3479686);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(GuadalajaraCentro));
        }
        else{
            mMap.setMyLocationEnabled(true);
            /*
            En caso de obtener la ubicacion, utilia setOnMyLocationChangeListener una unica vez para obtener
            la latitud y longitud de la ubicacion actual y mover la camara a dicha ubicacion.
             */
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(@NonNull @NotNull Location location) {

                    if(posicionActual){
                        mMiUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                        posicionActual = false;
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mMiUbicacion));

                        colocarOrigenRutaEnUbicacionActual();
                    }
                }
            });
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(creandoReporteAcontecimiento){
                    //Se elimina el marcador previamente creado:
                    mMap.clear();
                    circuloReporteAcontecimientoDibujado = false;

                    //Se guarda el valor de la longitud y de la latitud en EditText para almacenarlo en la bd posteriormente:
                    mETLatitud.setText(String.valueOf(latLng.latitude));
                    mETLongitud.setText(String.valueOf(latLng.longitude));

                    //Se muestra el marcador al usuario en el mapa:
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Centro del area para el reporte de acontecimiento"));

                    //Se muestra el spinner del radio:
                    mTVRadio.setVisibility(View.VISIBLE);
                    mSpinnerRadio.setVisibility(View.VISIBLE);

                    dibujarCirculo(latLng, radioInt);
                }else{
                    mPuntosIntermedios.add(latLng);

                    //Se muestra el marcador del punto intermedio:
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Punto intermedio de la ruta "+ mPuntosIntermedios.size()));

                    if(mPuntosIntermedios.size() == 1){
                        indicacionPuntosIntermedios = "&waypoints="+latLng.latitude+","+latLng.longitude;
                    }
                    else if(mPuntosIntermedios.size() > 1){
                        indicacionPuntosIntermedios = indicacionPuntosIntermedios + "|"+latLng.latitude+","+latLng.longitude;
                    }

                    mBotonEliminarPuntosIntermedios.setVisibility(View.VISIBLE);

                    if(origenRutaSeleccionado && destinoRutaSeleccionado){
                        definirRuta();
                    }
                }
            }
        });
    }

    //Metodo para obtener el email de la usuaria:
    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    //Metodo para dibujar un circulo en el mapa:
    public void dibujarCirculo(LatLng latLng, int radio){
        if(circuloReporteAcontecimientoDibujado){
            mMap.clear();
            circuloReporteAcontecimientoDibujado = false;
            mMap.addMarker(new MarkerOptions().position(latLng).title("Centro del area para el reporte de acontecimiento"));
        }

        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(radio);

        circuloReporteAcontecimiento = mMap.addCircle(circleOptions);
        colorearCirculo(circuloReporteAcontecimiento, positionCategoria);
        circuloReporteAcontecimientoDibujado = true;
    }

    //Metodo para colorear un circulo segun su categoria:
    public void colorearCirculo(Circle circulo, int categoria){
        if(categoria == 0){
            circulo.setFillColor(Color.TRANSPARENT);
        }
        else{
            if(categoria >= 1 && categoria <= 4){
                circulo.setFillColor(Color.parseColor("#80FFFF00"));
            }
            else{
                if(categoria >= 5 && categoria <= 8){
                    circulo.setFillColor(Color.parseColor("#80FFA500"));
                }
                else{
                    circulo.setFillColor(Color.parseColor("#80FF0000"));
                }
            }
        }
    }

    //Metodo para hacer un INSERT de un reporte de acontecimiento en la base de datos:;
    public void insertReporteAcontecimiento(){
        //Creating array for parameters
        String[] field = new String[6];
        field[0] = "email";
        field[1] = "latitud";
        field[2] = "longitud";
        field[3] = "radio";
        field[4] = "categoria";
        field[5] = "descripcion";

        //Creating array for data
        String[] data = new String[6];
        data[0] = email;
        data[1] = mETLatitud.getText().toString();
        data[2] = mETLongitud.getText().toString();
        data[3] = String.valueOf(radioInt);
        data[4] = String.valueOf(positionCategoria);
        data[5] = mETDescripcion.getText().toString();

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Route/GuardarReporteAcontecimiento.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Reporte de acontecimiento creado")) {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    visualizarCrearRuta();
                }
                else{
                    Toast.makeText(getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Metodo para cambiar el modo de visualizacion a la creacion de rutas:
    public void visualizarCrearRuta(){
        creandoReporteAcontecimiento = false;
        rutaTrazada = false;
        mMap.clear();

        mBotonRutaOrigen.setText(R.string.origen_de_la_ruta);
        mBotonRutaDestino.setText(R.string.destino_de_la_ruta);

        mDestinoRuta = null;
        destinoRutaSeleccionado = false;

        if(mMiUbicacion != null){
            colocarOrigenRutaEnUbicacionActual();
        }
        else{
            mOrigenRuta = null;
            origenRutaSeleccionado = false;
        }

        mPuntosIntermedios.clear();
        indicacionPuntosIntermedios = "";

        mBotonCambiarVista.setVisibility(View.VISIBLE);
        mBotonRutaOrigen.setVisibility(View.VISIBLE);
        mBotonRutaDestino.setVisibility(View.VISIBLE);
        mTVTransporte.setVisibility(View.VISIBLE);
        mSpinnerTransporte.setVisibility(View.VISIBLE);
        mSpinnerTransporte.setSelection(0);
        mTVTiempoEstimado.setVisibility(View.GONE);
        mTVDistancia.setVisibility(View.GONE);
        mBotonEliminarPuntosIntermedios.setVisibility(View.GONE);

        mBotonCancelarReporte.setVisibility(View.GONE);
        mTVRadio.setVisibility(View.GONE);
        mSpinnerRadio.setVisibility(View.GONE);
        mETDescripcion.setVisibility(View.GONE);
        mTVCategoria.setVisibility(View.GONE);
        mSpinnerCategoria.setVisibility(View.GONE);
        mBotonCrearReporte.setVisibility(View.GONE);

        obtenerReportesDeAcontecimiento("https://seguridadmujer.com/app_movil/Route/ObtenerReportes.php");
        moverCamara();
    }

    //Metodo para cambiar el modo de visualizacion a la creacion de un reporte de acontecimiento:
    public void visualizarCrearReporte(){
        creandoReporteAcontecimiento = true;
        circuloReporteAcontecimientoDibujado = false;
        mMap.clear();

        mETLatitud.setText("");
        mETLongitud.setText("");
        radioInt = 0;
        mSpinnerRadio.setSelection(radioInt);
        positionCategoria = 0;
        mSpinnerCategoria.setSelection(positionCategoria);
        mETDescripcion.setText("");
        mTVTiempoEstimado.setVisibility(View.GONE);
        mTVDistancia.setVisibility(View.GONE);
        mBotonEliminarPuntosIntermedios.setVisibility(View.GONE);

        mBotonCambiarVista.setVisibility(View.GONE);
        mBotonRutaOrigen.setVisibility(View.GONE);
        mBotonRutaDestino.setVisibility(View.GONE);
        mTVTransporte.setVisibility(View.GONE);
        mSpinnerTransporte.setVisibility(View.GONE);

        mBotonCancelarReporte.setVisibility(View.VISIBLE);
        mTVRadio.setVisibility(View.GONE);
        mSpinnerRadio.setVisibility(View.GONE);
        mETDescripcion.setVisibility(View.VISIBLE);
        mTVCategoria.setVisibility(View.VISIBLE);
        mSpinnerCategoria.setVisibility(View.VISIBLE);
        mBotonCrearReporte.setVisibility(View.VISIBLE);

        moverCamara();
    }

    //Metodo para mover la camara a la ubicacion de la usuaria o al centro de Guadalajara en su defecto:
    public void moverCamara(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //Inicializa la camara en la ubicacion actual/centro de guadalajara
            LatLng GuadalajaraCentro = new LatLng(20.6769375,-103.3479686);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(GuadalajaraCentro));
        }
        else{
            /*
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            LatLng MiUbicacion = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(MiUbicacion));
            */

            if(mMiUbicacion != null){
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mMiUbicacion));
            }
            else{
                //Inicializa la camara en la ubicacion actual/centro de guadalajara
                LatLng GuadalajaraCentro = new LatLng(20.6769375,-103.3479686);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(GuadalajaraCentro));
            }

        }
    }

    //Metodo para obtener los reportes de acontecimiento de la base de datos aun en su tiempo de vida:
    public void obtenerReportesDeAcontecimiento(String URL){
        //Vacia la lista:
        if(!mReportesAcontecimiento.isEmpty()){
            mReportesAcontecimiento.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        ReporteAcontecimiento reporteAcontecimiento = new ReporteAcontecimiento();
                        reporteAcontecimiento.setNombreUsuaria(jsonObject.getString("Nombre"));
                        reporteAcontecimiento.setApellidoPaternoUsuaria(jsonObject.getString("ApellidoPaterno"));
                        reporteAcontecimiento.setApellidoPaternoUsuaria(jsonObject.getString("ApellidoMaterno"));
                        reporteAcontecimiento.setCategoriaReporte(Integer.parseInt(jsonObject.getString("CategoriaReporte")));
                        reporteAcontecimiento.setLatitud(Double.parseDouble(jsonObject.getString("Latitud")));
                        reporteAcontecimiento.setLongitud(Double.parseDouble(jsonObject.getString("Longitud")));
                        reporteAcontecimiento.setRadio(Integer.parseInt(jsonObject.getString("Radio")));
                        reporteAcontecimiento.setDescripcion(jsonObject.getString("ComentariosRecomendaciones"));
                        reporteAcontecimiento.setFechaPublicacion(jsonObject.getString("FechaInicio"));

                        mReportesAcontecimiento.add(reporteAcontecimiento);
                        //Toast.makeText(getApplicationContext(), mReportesAcontecimiento.get(i).Descripcion, Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //onBackPressed:
    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(MapsActivity.this);
        startActivity(intent);
        finish();
    }

    //Dibula los reportes de acontecimiento aun en su tiempo de vida;
    public void dibujarReportesObtenidos(){

        //Toast.makeText(getApplicationContext(), String.valueOf(mReportesAcontecimiento.size()), Toast.LENGTH_LONG).show();

        for(int i=0; i<mReportesAcontecimiento.size(); i++){

            LatLng latLng = new LatLng(mReportesAcontecimiento.get(i).Latitud, mReportesAcontecimiento.get(i).Longitud);

            mMap.addMarker(new MarkerOptions().position(latLng).title("Centro del area para el reporte de acontecimiento"));

            CircleOptions circleOptions = new CircleOptions()
                    .center(latLng)
                    .radius(mReportesAcontecimiento.get(i).Radio);

            mMap.addCircle(circleOptions);

            //Toast.makeText(getApplicationContext(), "Reporte dibujado", Toast.LENGTH_LONG).show();
        }
    }

    //Metodo que muestra la lista para seleccionar una ubicacion proporcionada por la Places SDK de Google:
    private void seleccionarUbicacion(int requestCode){
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);

        startActivityForResult(intent, requestCode);
    }

    //onActivityResult que procesa el resultado de seleccionar una ubicacion de la lista proporcionada por Places SDK:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FROM_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = Autocomplete.getPlaceFromIntent(data);
                definirPuntoRuta(FROM_REQUEST_CODE, place);
                //Toast.makeText(getApplicationContext(), "Latitud: " + place.getLatLng().latitude + ", Longitud: " + place.getLatLng().longitude, Toast.LENGTH_LONG).show();
            }
            else if(resultCode == AutocompleteActivity.RESULT_ERROR){
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getApplicationContext(), "No se pudo obtener el punto de origen", Toast.LENGTH_LONG).show();
            }
            return;
        }
        if(requestCode == TO_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = Autocomplete.getPlaceFromIntent(data);
                definirPuntoRuta(TO_REQUEST_CODE, place);
            }
            else if(resultCode == AutocompleteActivity.RESULT_ERROR){
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(getApplicationContext(), "No se pudo obtener el punto de destino", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }

    //Metodo que define el punto de origen o de destino de la ruta y dibuja se correspondiente marcador:
    void definirPuntoRuta(int requestCode, Place place){
        //Se guarda la informacion del origen:
        if(requestCode == FROM_REQUEST_CODE){
            mBotonRutaOrigen.setText("Origen: " + place.getName());
            origenRutaSeleccionado = true;
            mOrigenRuta = place.getLatLng();
        }
        //Se guarda la direccion del destino:
        if(requestCode == TO_REQUEST_CODE){
            mBotonRutaDestino.setText("Destino: " + place.getName());
            destinoRutaSeleccionado = true;
            mDestinoRuta = place.getLatLng();
        }

        //Se mueve la camara:
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

        //Se dibujan los marcadores:
        mMap.clear();
        if(origenRutaSeleccionado && mOrigenRuta != null){
            mMap.addMarker(new MarkerOptions().position(mOrigenRuta).title("Origen de la ruta"));
        }
        if(destinoRutaSeleccionado && mDestinoRuta != null){
            mMap.addMarker(new MarkerOptions().position(mDestinoRuta).title("Destino de la ruta"));
        }

        if(origenRutaSeleccionado && destinoRutaSeleccionado){
            definirRuta();
        }
    }

    //Metodo para definir el origen de la ruta y colocar el marcador al obtener la ubicacion de la usuaria:
    void colocarOrigenRutaEnUbicacionActual(){
        mOrigenRuta = mMiUbicacion;
        origenRutaSeleccionado = true;
        mBotonRutaOrigen.setText(R.string.origen_de_la_ruta_mi_ubicacion);

        //Se dibujan los marcadores:
        mMap.clear();
        if(origenRutaSeleccionado && mOrigenRuta != null){
            mMap.addMarker(new MarkerOptions().position(mOrigenRuta).title("Origen de la ruta"));
        }
        if(destinoRutaSeleccionado && mDestinoRuta != null){
            mMap.addMarker(new MarkerOptions().position(mDestinoRuta).title("Destino de la ruta"));
        }
    }

    void definirRuta(){
        //Se borran rutas actuales:
        mMap.clear();
        if(origenRutaSeleccionado && mOrigenRuta != null){
            mMap.addMarker(new MarkerOptions().position(mOrigenRuta).title("Origen de la ruta"));
        }
        if(destinoRutaSeleccionado && mDestinoRuta != null){
            mMap.addMarker(new MarkerOptions().position(mDestinoRuta).title("Destino de la ruta"));
        }
        if(mPuntosIntermedios != null){
            for(int i=0; i<mPuntosIntermedios.size(); i++){
                mMap.addMarker(new MarkerOptions().position(mPuntosIntermedios.get(i)).title("Punto intermedio de la ruta "+ (i+1)));
            }
        }

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+mOrigenRuta.latitude+","+mOrigenRuta.longitude+"&destination="+mDestinoRuta.latitude+","+mDestinoRuta.longitude+indicacionPuntosIntermedios+"&mode="+medioTransporte+"&key=AIzaSyA4dRIX9BwyRP2WF_WAG4aYwDIMerFM2xc";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    //Toast.makeText(getApplicationContext(), String.valueOf(json), Toast.LENGTH_LONG).show();
                    trazarRuta(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al generar la ruta", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    public void trazarRuta(JSONObject json){
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = json.getJSONArray("routes");
            for (int i=0; i<jRoutes.length();i++){

                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");

                for (int j=0; j<jLegs.length();j++){

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k<jSteps.length();k++){

                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        //Log.i("end",""+polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.GRAY).width(5));
                    }
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        rutaTrazada = true;
        mTVTiempoEstimado.setVisibility(View.VISIBLE);
        mTVDistancia.setVisibility(View.VISIBLE);

        int duracionEnSegundos = 0;
        int distanciaEnMetros = 0;

        try {
            jRoutes = json.getJSONArray("routes");

            jLegs = ((JSONObject)(jRoutes.get(0))).getJSONArray("legs");
            for(int i=0; i<jLegs.length();i++){
                String duracion = ""+((JSONObject)((JSONObject)jLegs.get(i)).get("duration")).get("value");
                duracionEnSegundos += Integer.parseInt(duracion);

                String distancia = ""+((JSONObject)((JSONObject)jLegs.get(i)).get("distance")).get("value");
                distanciaEnMetros += Integer.parseInt(distancia);
            }

            double duracionaEnMinutos = 0;
            duracionaEnMinutos = duracionEnSegundos/60d;

            double distanciaEnKm = 0;
            distanciaEnKm = distanciaEnMetros/1000d;
            /*
            String duracion = "Tiempo estimado: "+((JSONObject)((JSONObject)jLegs.get(0)).get("duration")).get("text");
            String distancia = "Distancia: "+((JSONObject)((JSONObject)jLegs.get(0)).get("distance")).get("text");
            */
            String duracion = "Tiempo estimado: " + Math.round(duracionaEnMinutos) + " minutos";
            String distancia = "Distancia: " + String.format("%.1f", distanciaEnKm) + " km";

            mTVTiempoEstimado.setText(duracion);
            mTVDistancia.setText(distancia);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}