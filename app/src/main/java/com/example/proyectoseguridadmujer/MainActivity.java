package com.example.proyectoseguridadmujer;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoseguridadmujer.ui.alert.AlertFragment;
import com.example.proyectoseguridadmujer.ui.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import httpurlconnection.PutData;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    MenuItem item;
    //String email = getIntent().getStringExtra("email");
    RequestQueue requestQueue;
    String email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getCredentialData();
        //dialogopeticion();



        checkPetitions();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_self_defense, R.id.nav_informative, R.id.nav_route, R.id.nav_alert,R.id.nav_comunity, R.id.nav_helping_network)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                CloseSession(email);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkPetitions() {

        obtenerPeticiones("https://seguridadmujer.com/app_movil/PeticionesRecibidas/obtener_solicitudes.php?email=" + email);




       /* */
    }

    public void obtenerPeticiones(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        String NombreUsuarioWeb= jsonObject.getString("Nombre");
                        String CorreoUsuarioWeb= jsonObject.getString("Correo");
                        String IDUsuarioWeb= jsonObject.getString("ID_UsuarioWeb");
                        Toast.makeText(getApplicationContext(), NombreUsuarioWeb, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), CorreoUsuarioWeb, Toast.LENGTH_LONG).show();
                        int count=0;
                        dialogopeticion(NombreUsuarioWeb,CorreoUsuarioWeb,IDUsuarioWeb);

                    } catch (JSONException e) {
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

    public void updatepetitionstatus(String IDUsuarioWeb, String status){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Starting Write and Read data with URL
                //Creating array for parameters
                String[] field = new String[2];
                field[0] = "ID_UsuarioWeb";
                field[1] = "estatus";



                //Creating array for data
                String[] data = new String[2];
                data[0] = IDUsuarioWeb;
                data[1] = status;


                PutData putData = new PutData("https://seguridadmujer.com/app_movil/PeticionesRecibidas/updatePetitions.php", "POST", field, data);

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();

                        if (result.equals("Update Success")) {
                            Toast.makeText(getApplicationContext(), "Le notificaremos al usuario web tu decisión ¡Gracias!", Toast.LENGTH_SHORT).show();


                        }


                    }
                }
                //End Write and Read data with URL
            }
        });
    }

    public void dialogopeticion(String nombre, String correo,String IDUsuarioWeb){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MainActivity.this);
        dialogo1.setTitle("Tiene nuevas peticiones de enlace");
        dialogo1.setMessage("El usuario web llamado");
        dialogo1.setMessage("El usuario web llamado " + nombre + " con el correo " + correo +" desea vincularse a su cuenta. Acepte o rechace esta petición: ");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast.makeText(getApplicationContext(), "Aceptar", Toast.LENGTH_LONG).show();
                updatepetitionstatus(IDUsuarioWeb,"1");
            }
        });
        dialogo1.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                Toast.makeText(getApplicationContext(), "Declinar", Toast.LENGTH_LONG).show();
                updatepetitionstatus(IDUsuarioWeb,"0");
            }
        });
        AlertDialog dialogo = dialogo1.create();
        dialogo.show();

    }

    public void getCredentialData(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        email = preferences.getString("email", "");
    }


    public boolean CloseSession(String email){

            SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", "");
            editor.putString("password", "");
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();

        return true;
    }

}