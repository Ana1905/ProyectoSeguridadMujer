package com.example.proyectoseguridadmujer.ui.profile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectoseguridadmujer.AdapterContacts;
import com.example.proyectoseguridadmujer.AdapterWebUser;
import com.example.proyectoseguridadmujer.LoginActivity;
import com.example.proyectoseguridadmujer.MainActivity;
import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.ReporteAcontecimiento;
import com.example.proyectoseguridadmujer.Sancion;
import com.example.proyectoseguridadmujer.SanctionAlert;
import com.example.proyectoseguridadmujer.WebUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import httpurlconnection.PutData;

public class ProfileFragment extends Fragment {

    ImageView mImagenPerfil;
    TextView mTVNombre, mTVCorreo;
    EditText mETDescripcion, mETNombre, mETApellidoPaterno, mETApellidoMaterno, mETNewPassword, mETPassword;
    Button mBotonConfirmar, mBotonNotificaciones;
    RecyclerView mRecyclerViewUsuariosWeb;

    private String email = "";
    private String estadoNotificaciones = "";

    private String mImagenString = "";
    private String mTipoImagen = "";
    boolean mImagenModificada = false;

    ArrayList<WebUser> mListaUsuarios = new ArrayList<>();
    ArrayList<Sancion> mListaSanciones = new ArrayList<Sancion>();

    private int mID;
    private String mNombre, mApellidoPaterno, mApellidoMaterno, mDescripcion, mRutaImagen;
    RequestQueue requestQueue;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        //Wiring Up:
        mImagenPerfil = root.findViewById(R.id.imagen_perfil_usuaria);
        mTVNombre = root.findViewById(R.id.text_view_nombre_perfil_usuaria);
        mTVCorreo = root.findViewById(R.id.text_view_correo_perfil_usuaria);
        mETDescripcion = root.findViewById(R.id.edit_text_descripcion_perfil_usuaria);
        mETNombre = root.findViewById(R.id.edit_text_nombre_perfil_usuaria);
        mETApellidoPaterno = root.findViewById(R.id.edit_text_apellido_paterno_perfil_usuaria);
        mETApellidoMaterno = root.findViewById(R.id.edit_text_apellido_materno_perfil_usuaria);
        mETNewPassword = root.findViewById(R.id.edit_text_new_password_perfil_usuaria);
        mETPassword = root.findViewById(R.id.edit_text_password_perfil_usuaria);
        mBotonConfirmar = root.findViewById(R.id.boton_guardar_cambios_perfil_usuaria);
        mBotonNotificaciones = root.findViewById(R.id.boton_notificaciones);
        mRecyclerViewUsuariosWeb = root.findViewById(R.id.recyclerViewUsuariosConfianza);

        //Se obtienen las credenciales de la usuaria:
        getCredentialData();

        cambiarColorBoton(estadoNotificaciones);

        //Cambia el texto del boton de las notificaciones:
        definirTextoBoton();

        //Verifica si hay un baneo activo;
        obtenerSanciones("https://seguridadmujer.com/app_movil/LoginRegister/ObtenerSanciones.php?email="+email);

        //Verifica si hay peticiones de usuario web:
        obtenerPeticiones("https://seguridadmujer.com/app_movil/PeticionesRecibidas/obtener_solicitudes.php?email=" + email);
        //Verifica si hay advertencias de los administradores:
        obtenerAdvertencias("https://seguridadmujer.com/app_movil/PeticionesRecibidas/obtener_advertencias.php?email=" + email);

        //Obtiene la informacion de la usuaria:
        obtenerInformacionUsuaria("https://seguridadmujer.com/app_movil/Profile/obtenerInfoUsuaria.php?email="+email);

        //onClick de la imagen del perfil:
        mImagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se abre la galeria para seleccionar una imagen:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"), 1);
            }
        });

        //onClick del boton para guardar cambios:
        mBotonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mETPassword.getText().toString().isEmpty()){
                    if(!mETNombre.getText().toString().isEmpty()){
                        if(!mETApellidoPaterno.getText().toString().isEmpty()){
                            if(!mETApellidoMaterno.getText().toString().isEmpty()){
                                guardarCambios();
                            }
                            else{
                                Toast.makeText(getActivity(), "Introduzca su apellido materno", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getActivity(), "Introduzca su apellido paterno", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "Introduzca su nombre", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Introduzca su contraseña para poder guardar los cambios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //onClick del boton para administrar las notificaciones:
        mBotonNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!estadoNotificaciones.isEmpty()){
                    switch (estadoNotificaciones){
                        case "si":
                            administrarNotificaciones("no");
                            break;
                        case "no":
                            administrarNotificaciones("si");
                            break;
                    }
                    getCredentialData();
                    definirTextoBoton();
                }
            }
        });

        return root;
    }

    //Metodo para cambiar el texto del boton de las notificaciones:
    private void definirTextoBoton(){
        if(!estadoNotificaciones.isEmpty()){
            switch (estadoNotificaciones){
                case "si":
                    mBotonNotificaciones.setText("Desactivar notificaciones");
                    break;
                case "no":
                    mBotonNotificaciones.setText("Activar notificaciones");
                    break;
            }
        }
    }

    //Metodo para obtener el email de la usuaria y el estado de las notificaciones:
    public void getCredentialData(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");

        SharedPreferences notificaciones = this.getActivity().getSharedPreferences("Notificaciones", Context.MODE_PRIVATE);
        estadoNotificaciones = notificaciones.getString("estado", "no");

        if(email.isEmpty() || email == null){
            CloseSession();
        }
        //Toast.makeText(getContext(), estadoNotificaciones, Toast.LENGTH_SHORT).show();
    }

    //Metodo para activar o desactivar las notificaciones de la aplicacion:
    public void administrarNotificaciones(String estado){
        cambiarColorBoton(estado);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Notificaciones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("estado", estado);
        editor.commit();
    }

    //Metodo para obtener la informacion de la usuaria desde la base de datos:
    private void obtenerInformacionUsuaria(String URL){
        //Vacia los campos:
        mID = 0;
        mNombre = "";
        mApellidoPaterno = "";
        mApellidoMaterno = "";
        mDescripcion = "";
        mRutaImagen = "";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mID = jsonObject.getInt("ID_Usuaria");
                        mNombre = jsonObject.getString("Nombre");
                        mApellidoPaterno = jsonObject.getString("ApellidoPaterno");
                        mApellidoMaterno = jsonObject.getString("ApellidoMaterno");
                        mDescripcion = jsonObject.getString("Descripcion");
                        mRutaImagen = jsonObject.getString("RutaImagen");
                    }
                    catch (JSONException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                obtenerUsuariosWeb("https://seguridadmujer.com/app_movil/Profile/obtenerUsuariosWeb.php?email="+email);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    private void cambiarColorBoton(String estado){
        if(estado.equals("si")){

            mBotonNotificaciones.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.delete_button));
        }
        else if(estado.equals("no")){

            mBotonNotificaciones.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.confirm_button));
        }
    }

    //Metodo para mostrar en la interfaz la informacion obtenida desde la base de datos:
    private void cargarInformacion(){

        //Imagen:
        if(!mRutaImagen.isEmpty()){
            Glide.with(getActivity()).load(mRutaImagen).into(mImagenPerfil);
        }
        else{
            Glide.with(getActivity()).load("https://seguridadmujer.com/administracion/includes/imagenes/add_image_icon.png").into(mImagenPerfil);
        }

        mTVNombre.setText("¡Hola " + mNombre + "!");
        mTVCorreo.setText("Correo: " + email);
        mETDescripcion.setText(mDescripcion);
        mETNombre.setText(mNombre);
        mETApellidoPaterno.setText(mApellidoPaterno);
        mETApellidoMaterno.setText(mApellidoMaterno);
        mETNewPassword.setText("");
        mETPassword.setText("");
        mImagenString = "";
        mTipoImagen = "";

        //Configuracion de la RecyclerView:
        mRecyclerViewUsuariosWeb.setLayoutManager(new LinearLayoutManager(getActivity()));
        AdapterWebUser adapterWebUser = new AdapterWebUser(mListaUsuarios);
        mRecyclerViewUsuariosWeb.setAdapter(adapterWebUser);
    }

    //onActivityResult para procesar la imagen seleccionada desde la galeria:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            //Guarda la foto en un URI y la coonvierte a Bitmap
            Bitmap bitmap = null;

            if(data != null){
                Uri uri = data.getData();

                mImagenPerfil.setImageDrawable(null);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    //Se obtiene la extension de la imagen:
                    ByteArrayOutputStream array = new ByteArrayOutputStream();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                    mTipoImagen = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
                    //Toast.makeText(getActivity(), mTipoImagen, Toast.LENGTH_SHORT).show();

                    if(mTipoImagen.equals("jpg")){
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
                        mImagenPerfil.setImageURI(uri);
                    }
                    else if(mTipoImagen.equals("png")){
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, array);
                        mImagenPerfil.setImageURI(uri);
                    }
                    else{
                        Toast.makeText(getActivity(), "Por favor introduce un archivo valido", Toast.LENGTH_SHORT).show();
                    }

                    //Se convierte a string la imagen:
                    byte [] imageByte = array.toByteArray();
                    mImagenString = Base64.encodeToString(imageByte, Base64.DEFAULT);

                    mImagenModificada = true;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Metodo para guardar la informacion en la base de datos:
    private void guardarCambios(){
        String[] field = new String[9];
        field[0] = "id";
        field[1] = "imagen";
        field[2] = "tipoImagen";
        field[3] = "descripcion";
        field[4] = "nombre";
        field[5] = "apellidoPaterno";
        field[6] = "apellidoMaterno";
        field[7] = "newPassword";
        field[8] = "password";

        //Creating array for data
        String[] data = new String[9];
        data[0] = String.valueOf(mID);
        data[1] = mImagenString;
        data[2] = mTipoImagen;
        data[3] = mETDescripcion.getText().toString();
        data[4] = mETNombre.getText().toString();
        data[5] = mETApellidoPaterno.getText().toString();
        data[6] = mETApellidoMaterno.getText().toString();
        data[7] = mETNewPassword.getText().toString();
        data[8] = mETPassword.getText().toString();

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Profile/actualizarUsuaria.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(getActivity(), "Se ha actualizado la informacion", Toast.LENGTH_SHORT).show();
                    obtenerInformacionUsuaria("https://seguridadmujer.com/app_movil/Profile/obtenerInfoUsuaria.php?email="+email);
                } else {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Metodo para obtener los usuarios web:
    private void obtenerUsuariosWeb(String URL){
        //Vacia la lista:
        mListaUsuarios.clear();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        WebUser webUser = new WebUser();
                        webUser.setID(jsonObject.getInt("ID_UsuarioWeb"));
                        webUser.setNombre(jsonObject.getString("Nombre"));
                        webUser.setCorreo(jsonObject.getString("Correo"));
                        if(jsonObject.getString("RutaImagen") == null || jsonObject.getString("RutaImagen").isEmpty()){
                            webUser.setRutaImagen("");
                        }
                        else{
                            webUser.setRutaImagen(jsonObject.getString("RutaImagen"));
                        }

                        mListaUsuarios.add(webUser);
                    }
                    catch (JSONException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                cargarInformacion();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
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
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                if(!mListaSanciones.isEmpty()){
                    getActivity().getFragmentManager().popBackStack();
                    CloseSession();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }

    //Metodo para cerrar la sesion:
    public boolean CloseSession(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", "");
        editor.putString("password", "");
        editor.commit();

        Intent intent = new Intent(getContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        return true;
    }

    //Metodo para obtener las peticiones de usuario web desde la base de datos:
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
                        //Toast.makeText(getApplicationContext(), NombreUsuarioWeb, Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), CorreoUsuarioWeb, Toast.LENGTH_LONG).show();
                        int count=0;
                        dialogopeticion(NombreUsuarioWeb,CorreoUsuarioWeb,IDUsuarioWeb);

                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    //Metodo para mostrar el AlertDialog de peticion de usuario web:
    public void dialogopeticion(String nombre, String correo,String IDUsuarioWeb){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
        dialogo1.setTitle("Tiene nuevas peticiones de enlace");
        //dialogo1.setMessage("El usuario web llamado");
        dialogo1.setMessage("El usuario web llamado " + nombre + " con el correo " + correo +" desea vincularse a su cuenta. Acepte o rechace esta petición: ");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Toast.makeText(getApplicationContext(), "Aceptar", Toast.LENGTH_LONG).show();
                updatepetitionstatus(IDUsuarioWeb,"1");

            }
        });
        dialogo1.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Toast.makeText(getApplicationContext(), "Declinar", Toast.LENGTH_LONG).show();
                updatepetitionstatus(IDUsuarioWeb,"0");
            }
        });
        AlertDialog dialogo = dialogo1.create();
        dialogo.show();
    }

    //Metodo para actualizar la peticion del usuario web en la base de datos:
    public void updatepetitionstatus(String IDUsuarioWeb, String status){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
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
                            Toast.makeText(getContext(), "Le notificaremos al usuario web tu decisión ¡Gracias!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    //Metodo para obtener las peticiones de usuario web desde la base de datos:
    public void obtenerAdvertencias(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        String Contenido= jsonObject.getString("Contenido");
                        String Fecha = jsonObject.getString("Fecha");
                        String ID = jsonObject.getString("ID_Advertencia");

                        dialogoAdvertencia(Contenido, Fecha, ID);

                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    //Metodo para mostrar el AlertDialog de peticion de usuario web:
    public void dialogoAdvertencia(String Contenido, String Fecha, String ID){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
        dialogo1.setTitle("Ha recibido una advertencia");
        //dialogo1.setMessage("El usuario web llamado");
        dialogo1.setMessage("Una usuaria ha reportado una de sus pusblicaciones del módulo de comunidad el día "+Fecha+", por el momento no se le ha realizado ningún castigo o sancion, sin embargo le recomendamos: "+Contenido);
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Toast.makeText(getApplicationContext(), "Aceptar", Toast.LENGTH_LONG).show();
                updateadvertencia(ID);
            }
        });
        AlertDialog dialogo = dialogo1.create();
        dialogo.show();
    }

    //Metodo para actualizar la peticion del usuario web en la base de datos:
    public void updateadvertencia(String ID_Advertencia){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[1];
                field[0] = "ID_Advertencia";

                //Creating array for data
                String[] data = new String[1];
                data[0] = ID_Advertencia;

                PutData putData = new PutData("https://seguridadmujer.com/app_movil/PeticionesRecibidas/updateAdvertencias.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Success")) {
                            Toast.makeText(getContext(), "Notificación atendida", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

}