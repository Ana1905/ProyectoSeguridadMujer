package com.example.proyectoseguridadmujer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    List<Sancion> mSanciones = new ArrayList<>();
    List<Integer> mSancionesActuales = new ArrayList<>();

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

                                    //Verifica si la usuaria tiene una sancion:
                                    obtenerSancionesActuales("https://seguridadmujer.com/app_movil/Route/ObtenerSancionActual.php?email="+email);

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

        obtenerSanciones("https://seguridadmujer.com/app_movil/Route/ObtenerSanciones.php?email="+email);

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

                        if(!creandoReporteAcontecimiento){
                            colocarOrigenRutaEnUbicacionActual();
                        }
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

                    mostrarZonasProhibidas();

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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                if(marker.getTag() != null){
                    if(marker.getTitle().equals("Zona Prohibida")){
                        Intent intent = new Intent(MapsActivity.this, RestrictedAreaAlert.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(MapsActivity.this, ShowRouteReportInfoAlert.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("ID", marker.getTitle());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    //Metodo para dibujar un circulo en el mapa:
    public void dibujarCirculo(LatLng latLng, int radio){
        if(circuloReporteAcontecimientoDibujado){
            mMap.clear();
            mostrarZonasProhibidas();
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
            if(categoria >= 1 && categoria <= 3){
                circulo.setFillColor(Color.parseColor("#80FFFF00"));
            }
            else{
                if(categoria >= 4 && categoria <= 8){
                    circulo.setFillColor(Color.parseColor("#80FFA500"));
                }
                else{
                    circulo.setFillColor(Color.parseColor("#80FF0000"));
                }
            }
        }
    }

    //Metodo para hacer un INSERT de un reporte de acontecimiento en la base de datos:
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
        posicionActual = true;
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
        eliminarReportesMostrados();
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
        obtenerSanciones("https://seguridadmujer.com/app_movil/Route/ObtenerSanciones.php?email="+email);

        moverCamara();
    }

    //Metodo para cambiar el modo de visualizacion a la creacion de un reporte de acontecimiento:
    public void visualizarCrearReporte(){
        creandoReporteAcontecimiento = true;
        circuloReporteAcontecimientoDibujado = false;
        posicionActual = true;
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

        obtenerReportesDeAcontecimiento("https://seguridadmujer.com/app_movil/Route/ObtenerZonasProhibidas.php");

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
                        reporteAcontecimiento.setID(jsonObject.getInt("ID_ReporteAcontecimiento"));
                        reporteAcontecimiento.setNombreUsuaria(jsonObject.getString("Nombre"));
                        reporteAcontecimiento.setApellidoPaternoUsuaria(jsonObject.getString("ApellidoPaterno"));
                        reporteAcontecimiento.setApellidoPaternoUsuaria(jsonObject.getString("ApellidoMaterno"));
                        reporteAcontecimiento.setCategoriaReporte(Integer.parseInt(jsonObject.getString("CategoriaReporte")));
                        reporteAcontecimiento.setLatitud(Double.parseDouble(jsonObject.getString("Latitud")));
                        reporteAcontecimiento.setLongitud(Double.parseDouble(jsonObject.getString("Longitud")));
                        reporteAcontecimiento.setRadio(Integer.parseInt(jsonObject.getString("Radio")));
                        reporteAcontecimiento.setDescripcion(jsonObject.getString("ComentariosRecomendaciones"));
                        reporteAcontecimiento.setFechaPublicacion(jsonObject.getString("FechaInicio"));
                        reporteAcontecimiento.setMostrandoEnMapa(false);

                        mReportesAcontecimiento.add(reporteAcontecimiento);
                        //Toast.makeText(getApplicationContext(), mReportesAcontecimiento.get(i).Descripcion, Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                if(creandoReporteAcontecimiento){
                    mostrarZonasProhibidas();
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

    //Metodo que borra del mapa la ruta actual y manda a llamar la API de Google para calcular la ruta:
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
        eliminarReportesMostrados();
        verificarColisionDestino();

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+mOrigenRuta.latitude+","+mOrigenRuta.longitude+"&destination="+mDestinoRuta.latitude+","+mDestinoRuta.longitude+indicacionPuntosIntermedios+"&mode="+medioTransporte+"&key=AIzaSyA4dRIX9BwyRP2WF_WAG4aYwDIMerFM2xc";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
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

    //Metodo para trazar la ruta en el mapa y para mostrar la estimacion de distancia y tiempo:
    public void trazarRuta(JSONObject json){
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        //Try-Catch para el trazado de la ruta en el mapa:
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

                        for(int l=0; l<list.size(); l++){
                            verificarColisionConReporte(list.get(l));
                        }
                    }
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        //Se muestran los TextView para la distancia y el tiempo:
        rutaTrazada = true;
        mTVTiempoEstimado.setVisibility(View.VISIBLE);
        mTVDistancia.setVisibility(View.VISIBLE);

        int duracionEnSegundos = 0;
        int distanciaEnMetros = 0;

        //Try-Catch para el calculo del tiempo y la distancia:
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

    //Metodo para mostrar en el mapa aquellos reportes de acontecimiento que colinden con la ruta recien creada:
    void verificarColisionConReporte(LatLng punto){

        float [] distancia = new float[1];
        //Se itera a traves de todos los reportes de acontecimiendo:
        for(int i=0; i<mReportesAcontecimiento.size(); i++){
            //Se obtiene la distancia entre el punto central del reporte de acontecimiento y el punto de la ruta:
            Location.distanceBetween(punto.latitude, punto.longitude, mReportesAcontecimiento.get(i).getLatitud(), mReportesAcontecimiento.get(i).getLongitud(), distancia);

            /*
            Si la distancia obtenida es menor o igual al radio significa que la ruta efectivamente colisiona con el reporte.
            Se valida esto al igual que se valida si el reporte se ha dibujado en el mapa ya o no (por medio de la propiedad
            MostrandoEnMapa).
             */
            if( (distancia[0] <= mReportesAcontecimiento.get(i).getRadio()) && (!mReportesAcontecimiento.get(i).isMostrandoEnMapa()) ){
                LatLng latLng = new LatLng(mReportesAcontecimiento.get(i).getLatitud(), mReportesAcontecimiento.get(i).getLongitud());

                //Se dibuja el circulo en el mapa:
                CircleOptions circleOptions = new CircleOptions()
                        .center(latLng)
                        .radius(mReportesAcontecimiento.get(i).getRadio());
                Circle circulo = mMap.addCircle(circleOptions);

                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(String.valueOf(mReportesAcontecimiento.get(i).getID()))
                        .icon(bitmapDescriptor(getApplicationContext(), R.drawable.ic_baseline_new_releases_24)));

                marker.setTag(true);


                //Se colorea el circulo:
                colorearCirculo(circulo, mReportesAcontecimiento.get(i).getCategoriaReporte());

                //Se actualiza el estado del reporte de acontecimiento para indicar que ya se ha dibujado en el mapa:
                mReportesAcontecimiento.get(i).setMostrandoEnMapa(true);
            }
        }
    }

    //Metodo para mostrar los reportes de acontecimiento creados por el sistema al cargar la visualizacion de reporte de acontecimiento:
    private void mostrarZonasProhibidas(){

        for(int i=0; i<mReportesAcontecimiento.size(); i++){
            LatLng latLng = new LatLng(mReportesAcontecimiento.get(i).getLatitud(), mReportesAcontecimiento.get(i).getLongitud());

            //Se dibuja el circulo en el mapa:
            CircleOptions circleOptions = new CircleOptions()
                    .center(latLng)
                    .radius(mReportesAcontecimiento.get(i).getRadio());
            Circle circulo = mMap.addCircle(circleOptions);

            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .title("Zona Prohibida")
                    .icon(bitmapDescriptor(getApplicationContext(), R.drawable.ic_baseline_new_releases_24)));

            marker.setTag(true);

            //Se colorea el circulo:
            colorearCirculo(circulo, mReportesAcontecimiento.get(i).getCategoriaReporte());

            //Se actualiza el estado del reporte de acontecimiento para indicar que ya se ha dibujado en el mapa:
            mReportesAcontecimiento.get(i).setMostrandoEnMapa(true);
        }
    }

    //Metodo para preparar un disenio de marcador personalizado:
    private BitmapDescriptor bitmapDescriptor(Context context, int resource){
        Drawable drawable = ContextCompat.getDrawable(context, resource);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //Metodo para establecer en false el valor de mostrado en el mapa de todos los reportes de acontecimiento:
    void eliminarReportesMostrados(){
        for(int i=0; i<mReportesAcontecimiento.size(); i++){
            mReportesAcontecimiento.get(i).setMostrandoEnMapa(false);
        }
    }

    //Metodo para verificar si el destino de la ruta se ubica en una zona de peligro:
    private void verificarColisionDestino(){

        float [] distancia = new float[1];
        //Se itera a traves de todos los reportes de acontecimiendo:
        for(int i=0; i<mReportesAcontecimiento.size(); i++){
            //Se obtiene la distancia entre el punto central del reporte de acontecimiento y el destino:
            Location.distanceBetween(mDestinoRuta.latitude, mDestinoRuta.longitude, mReportesAcontecimiento.get(i).getLatitud(), mReportesAcontecimiento.get(i).getLongitud(), distancia);

            /*
            Si la distancia obtenida es menor o igual al radio significa que el destino efectivamente colisiona con el reporte.
             */
            if( (distancia[0] <= mReportesAcontecimiento.get(i).getRadio())){

                startActivity(new Intent(MapsActivity.this, DangerRouteAlert.class));

                break;
            }
        }
    }

    //Metodo para obtener las sanciones de la base de datos:
    private void obtenerSanciones(String URL){

        //Vacia la lista:
        if(!mSanciones.isEmpty()){
            mSanciones.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Sancion sancion = new Sancion();
                        sancion.setID(jsonObject.getInt("ID_SancionBot"));
                        sancion.setID_Usuaria(jsonObject.getInt("ID_Usuaria"));
                        sancion.setDuracion(jsonObject.getInt("Duracion"));
                        sancion.setFechaInicio(jsonObject.getString("FechaInicio"));
                        sancion.setFechaFin(jsonObject.getString("FechaFin"));
                        sancion.setID_TipoSancion(jsonObject.getInt("TipoSancion"));
                        sancion.setTipoSancion(jsonObject.getString("NombreCategoria"));
                        sancion.setEstado(jsonObject.getInt("Mostrada"));
                        sancion.setMensajeSancion(jsonObject.getString("Mensaje"));

                        mSanciones.add(sancion);

                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                mostrarSanciones();
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

    //Metodo para mostrar las sanciones pendientes:
    private void mostrarSanciones(){

        for(int i=(mSanciones.size()-1); i>=0; i--){
            dialogopeticion(i);
            actualizarEstadoMostradoSancion(i);
        }
    }

    //Metodo para mostrar el AlertDialog de la sancion:
    public void dialogopeticion(int indiceSancion){
        AlertDialog.Builder dialogSancion = new AlertDialog.Builder(MapsActivity.this);

        if(mSanciones.get(indiceSancion).getID_TipoSancion() != 1){
            dialogSancion.setTitle("Ha recibido una sanción");
        }
        else{
            dialogSancion.setTitle("Advertencia");
        }

        if(mSanciones.get(indiceSancion).getID_TipoSancion() == 1 || mSanciones.get(indiceSancion).getDuracion() == 876000){
            dialogSancion.setMessage(mSanciones.get(indiceSancion).getMensajeSancion());
        }
        else{
            dialogSancion.setMessage(mSanciones.get(indiceSancion).getMensajeSancion() + mSanciones.get(indiceSancion).getFechaFin());
        }

        dialogSancion.setCancelable(false);

        dialogSancion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Toast.makeText(getApplicationContext(), "Aceptar", Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog dialogo = dialogSancion.create();
        dialogo.show();

    }

    //Metodo para hacer el update de la sancion en la base de datos para indicar que ya se ha mostrado:
    private void actualizarEstadoMostradoSancion(int indiceSancion){
        //Creating array for parameters
        String[] field = new String[1];
        field[0] = "ID_Sancion";

        //Creating array for data
        String[] data = new String[1];
        data[0] = String.valueOf(mSanciones.get(indiceSancion).getID());

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Route/ActualizarSancion.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Sancion actualizada")) {
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    //SI
                }
                else{
                    //Toast.makeText(getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                    //NO
                }
            }
        }
    }

    //Metodo para obtener los tipos de sanciones que actualmente tiene la usuaria:
    private void obtenerSancionesActuales(String URL){

        //Vacia la lista:
        if(!mSancionesActuales.isEmpty()){
            mSancionesActuales.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mSancionesActuales.add(jsonObject.getInt("TipoSancion"));
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                validarSancionActiva();
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

    private void validarSancionActiva(){
        boolean sancionActiva = false;
        String mensajeSancion = "";

        for(int i=0; i<mSancionesActuales.size(); i++){

            //Validacion para una sancion de bloqueo de creacion de reportes general:
            if(mSancionesActuales.get(i) == 2){
                sancionActiva = true;
                mensajeSancion = "La función para crear reportes de acontecimiento se encuentra bloqueada actualmente.";
                break;
            }
            if((mSancionesActuales.get(i) == 3) && (positionCategoria >= 1 && positionCategoria <= 3)){
                sancionActiva = true;
                mensajeSancion = "La función para crear reportes de acontecimiento de nivel amarillo se encuentra bloqueada actualmente.";
                break;
            }
            if((mSancionesActuales.get(i) == 4) && (positionCategoria >= 4 && positionCategoria <= 8)){
                sancionActiva = true;
                mensajeSancion = "La función para crear reportes de acontecimiento de nivel naranja se encuentra bloqueada actualmente.";
                break;
            }
            if((mSancionesActuales.get(i) == 5) && (positionCategoria >= 9 && positionCategoria <= 13)){
                sancionActiva = true;
                mensajeSancion = "La función para crear reportes de acontecimiento de nivel rojo se encuentra bloqueada actualmente.";
                break;
            }
        }

        if(!sancionActiva){
            insertReporteAcontecimiento();
        }
        else{
            Intent intent = new Intent(MapsActivity.this, SanctionAlert.class);
            Bundle bundle = new Bundle();
            bundle.putString("mensaje", mensajeSancion);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    //Metodo para obtener el email de la usuaria:
    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    //onBackPressed:
    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(MapsActivity.this);
        startActivity(intent);
        finish();
    }


}