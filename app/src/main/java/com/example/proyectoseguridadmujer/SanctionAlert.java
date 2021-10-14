package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SanctionAlert extends AppCompatActivity {

    String mMensaje;

    TextView mTVMensajeSancion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanction_alert);

        //Define el tamanio de la pantalla
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int ancho = displayMetrics.widthPixels;
        int alto = displayMetrics.heightPixels;

        getWindow().setLayout((int) (ancho * 0.9), (int) (alto * 0.45));

        //Wiring Up:
        mTVMensajeSancion = findViewById(R.id.text_view_mensaje_sancion);

        //Se obtiene el mensaje:
        getBundle();

        //Se muestra el mensaje:
        mostrarInfoSancion();
    }

    //Metodo para mostrar el mensaje en el TextView:
    private void mostrarInfoSancion(){
        mTVMensajeSancion.setText(mMensaje);
    }

    //Metodo para obtener la informacion del bundle enviado desde MapsActivity.
    private void getBundle(){
        mMensaje = getIntent().getExtras().getString("mensaje");
    }
}