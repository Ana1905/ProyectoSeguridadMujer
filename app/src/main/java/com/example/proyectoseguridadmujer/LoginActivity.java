package com.example.proyectoseguridadmujer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Collections;

import httpurlconnection.PutData;

public class LoginActivity extends AppCompatActivity {
    EditText mEditTextSignInEmail, mEditTextSignInPassword;
    Button mButtonSignIn,mButtonCreateAccount;
    TextView mTextViewForgotPassword;

    String email, contraseña;
    String mPhoneNumber;
    int REQUEST_CODE= 200;

    RequestQueue requestQueue;

    ArrayList<Sancion> mListaSanciones = new ArrayList<Sancion>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //PERMISOS

        verificarPermisos();

        //PHONE
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mPhoneNumber = tMgr.getLine1Number();

        if(mPhoneNumber == null){
            mPhoneNumber = "0123456789";
        }

        //Toast.makeText(getApplicationContext(), mPhoneNumber, Toast.LENGTH_LONG).show();

        //Wiring up
        mEditTextSignInEmail = findViewById(R.id.sign_in_email_input);
        mEditTextSignInPassword = findViewById(R.id.sign_in_password_input);
        mButtonSignIn = findViewById(R.id.sign_in_enter_button);
        mButtonCreateAccount = findViewById(R.id.sign_in_create_account_button);
        mTextViewForgotPassword = findViewById(R.id.sign_in_forgot_password_label);

        cargarSesion();

        //OnClicks
        mButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mTextViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //Variables to catch data
                //Toast.makeText(getApplicationContext(), "IMPORTANTE: Recuerda que si no aceptaste el permiso de teléfono para aplicacion seguridad mujer no tendrás acceso a la aplicacion)", Toast.LENGTH_LONG).show();

                email = String.valueOf(mEditTextSignInEmail.getText());
                contraseña = String.valueOf(mEditTextSignInPassword.getText());


                if (!email.equals("") && !contraseña.equals("")) {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "email";
                            field[1] = "contraseña";
                            field[2] = "telefono";


                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = email;
                            data[1] = contraseña;
                            data[2] = mPhoneNumber;

                            PutData putData = new PutData("https://seguridadmujer.com/app_movil/LoginRegister/login.php", "POST", field, data);

                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();

                                    if (result.equals("Login Success")) {

                                        obtenerSanciones("https://seguridadmujer.com/app_movil/LoginRegister/ObtenerSanciones.php?email="+email);

                                    } else {

                                        if (result.equals("Missing email verification email or Password wrong")) {
                                            Toast.makeText(getApplicationContext(), "La cuenta aún no ha sido creada pues no se ha verificado la dirección de correo", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            if (result.equals("phones not match email or Password wrong")) {
                                                Toast.makeText(getApplicationContext(), "Estás intentando acceder desde un dispositivo que no es el que tenemos registrado, por tu seguridad no te daremos acceso", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Correo o contraseña erróneos", Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void verificarPermisos(){

        int permiso= ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
        if(permiso==PackageManager.PERMISSION_GRANTED){

        }
        else{

            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_CODE);
        }

    }

    public void guardarSesion(){
        SharedPreferences preferences = getSharedPreferences("Credencials",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.putString("password", contraseña);
        editor.commit();

    }


    public void cargarSesion() {
        SharedPreferences preferences = getSharedPreferences("Credencials", MODE_PRIVATE);
        email = preferences.getString("email", "");
        contraseña = preferences.getString("password", "");

        if (!email.isEmpty() && !contraseña.isEmpty()) {
            String[] field = new String[3];
            field[0] = "email";
            field[1] = "contraseña";
            field[2] = "telefono";

            //Creating array for data
            String[] data = new String[3];
            data[0] = email;
            data[1] = contraseña;
            data[2] = mPhoneNumber;
            //Change ip and port of your computer and xampp
            PutData putData = new PutData("https://seguridadmujer.com/app_movil/LoginRegister/login.php", "POST", field, data);

            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();

                    if (result.equals("Login Success")) {
                        // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        //guardarSession();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }
        }
    }

    //Metodo que valida si hay un baneo de cuenta:
    private void obtenerSanciones(String URL){

        //Vacia la lista:
        if(!mListaSanciones.isEmpty()){
            mListaSanciones.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Sancion sancion = new Sancion();
                        sancion.setID(jsonObject.getInt("ID_Baneo"));
                        sancion.setID_Usuaria(jsonObject.getInt("ID_Usuaria"));
                        sancion.setDuracion(jsonObject.getInt("Duracion"));
                        sancion.setFechaInicio(jsonObject.getString("FechaInicio"));
                        sancion.setFechaFin(jsonObject.getString("FechaFin"));
                        sancion.setTipoSancion(jsonObject.getString("Tipo"));

                        mListaSanciones.add(sancion);
                    }
                    catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                if(mListaSanciones.isEmpty()){
                    login();
                }
                else{

                    String mensajeSancion = "";
                    ArrayList<Integer> listaDuraciones = new ArrayList<Integer>();

                    for(int i=0; i<mListaSanciones.size(); i++){
                        listaDuraciones.add(mListaSanciones.get(i).getDuracion());
                    }

                    for(int i=0; i<mListaSanciones.size(); i++){
                        if(mListaSanciones.get(i).getDuracion() == Collections.max(listaDuraciones)){
                            if(mListaSanciones.get(i).getDuracion() == 876000){
                                mensajeSancion = "Lo sentimos, su cuenta ha sido bloqueada permanentemente.";
                            }
                            else{
                                mensajeSancion = "Lo sentimos, su cuenta se encuentra bloqueada actualmente, podra volver a acceder el día "+mListaSanciones.get(i).getFechaFin();
                            }
                        }
                    }

                    Intent intent = new Intent(LoginActivity.this, SanctionAlert.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mensaje", mensajeSancion);
                    intent.putExtras(bundle);
                    startActivity(intent);
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

    public void login(){
        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        guardarSesion();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }
}