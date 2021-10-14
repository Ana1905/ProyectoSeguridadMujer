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

public class ShowRouteReportInfoAlert extends AppCompatActivity {

    TextView mTVNombreUsuaria, mTVFecha, mTVCategoria, mTVComentarios;

    String mIDReporte, mNombreCategoria;

    ReporteAcontecimiento mReporteAcontecimiento = new ReporteAcontecimiento();

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route_report_info_alert);

        //Define el tamanio de la pantalla
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int ancho = displayMetrics.widthPixels;
        int alto = displayMetrics.heightPixels;

        getWindow().setLayout((int) (ancho * 0.9), (int) (alto * 0.8));

        //Wiring Up:
        mTVNombreUsuaria = findViewById(R.id.text_view_nombre_usuaria);
        mTVFecha = findViewById(R.id.text_view_fecha_reporte);
        mTVCategoria = findViewById(R.id.text_view_categoria_reporte);
        mTVComentarios = findViewById(R.id.text_view_comentarios);

        //Se obtiene el ID del reporte seleccionado:
        getBundle();

        //Se obtiene la informacion del reporte desde la base de datos:
        obtenerReporteDeAcontecimiento("https://seguridadmujer.com/app_movil/Route/ObtenerReporte.php?ID_Reporte="+mIDReporte);

    }

    //Metodo para obtener la informacion del reporte de acontecimiento desde la base de datos:
    private void obtenerReporteDeAcontecimiento(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mReporteAcontecimiento.setID(Integer.valueOf(mIDReporte));
                        mReporteAcontecimiento.setNombreUsuaria(jsonObject.getString("Nombre"));
                        mReporteAcontecimiento.setApellidoPaternoUsuaria(jsonObject.getString("ApellidoPaterno"));
                        mReporteAcontecimiento.setApellidoMaternoUsuaria(jsonObject.getString("ApellidoMaterno"));
                        mReporteAcontecimiento.setFechaPublicacion(jsonObject.getString("FechaInicio"));
                        mReporteAcontecimiento.setDescripcion(jsonObject.getString("ComentariosRecomendaciones"));
                        mNombreCategoria = jsonObject.getString("CategoriaReporte");

                        mostrarInfoReporte();
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

    //Metodo para mostrar la informacion del reporte de acontecimiento en sus correspondientes TextView:
    private void mostrarInfoReporte(){

        mTVNombreUsuaria.setText(mReporteAcontecimiento.getNombreUsuaria() + " " + mReporteAcontecimiento.getApellidoPaternoUsuaria() + " " + mReporteAcontecimiento.getApellidoMaternoUsuaria());
        mTVFecha.setText(mReporteAcontecimiento.getFechaPublicacion());
        mTVCategoria.setText(mNombreCategoria);
        mTVComentarios.setText(mReporteAcontecimiento.getDescripcion());
    }

    //Metodo para obtener la informacion del bundle enviado desde MapsActivity.
    private void getBundle(){
        mIDReporte = getIntent().getExtras().getString("ID");
    }
}