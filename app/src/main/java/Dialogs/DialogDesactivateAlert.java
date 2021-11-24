package Dialogs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoseguridadmujer.AddTrustedFriendsActivity;
import com.example.proyectoseguridadmujer.Contact;
import com.example.proyectoseguridadmujer.DesactivateAlertActivity;
import com.example.proyectoseguridadmujer.MainActivity;
import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import httpurlconnection.PutData;

public class DialogDesactivateAlert extends DialogFragment {

    Button mBotonDesactivarAlerta;

    int mTipoAlerta;
    String email, mensaje = "";
    boolean botonPresionado = false;

    ArrayList<Contact> mListaContactos = new ArrayList<Contact>();

    RequestQueue requestQueue;

    public DialogDesactivateAlert(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se obtiene el tipo de alerta:
        getBundle();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //WiringUp
        View root = inflater.inflate(R.layout.activity_desactivate_alert, container, false);

        //Wiring up:
        mBotonDesactivarAlerta = root.findViewById(R.id.boton_desactivar_alerta);

        //Obtiene el email de la usuaria:
        getCredentialData();

        //onClick del boton para desactivar alerta:
        mBotonDesactivarAlerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarAlerta();
            }
        });

        return root;
    }

    private void eliminarAlerta(){
        String[] field = new String[1];
        field[0] = "email";

        String[] data = new String[1];
        data[0] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/eliminarAlerta.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {

                    mensaje = "He notificado a la aplicación de seguridad para la mujer que me encuentro fuera de la situación de peligro";
                    switch (mTipoAlerta){
                        case 1:
                            enviarWhatsApp();
                            break;
                        case 2:
                            obtenerContactos("https://seguridadmujer.com/app_movil/Alert/ObtenerContactosConfianza.php?email="+email);
                            break;
                    }
                }
                else{
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void enviarWhatsApp(){
        String[] field = new String[2];
        field[0] = "email";
        field[1] = "mensaje";

        String[] data = new String[2];
        data[0] = email;
        data[1] = mensaje;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/alertaConfirmacion.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(getActivity(), "Se ha enviado el mensaje de WhatsApp", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    //dismiss();
                }
                else{
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Metodo para obtener los contactos de confianza:
    private void obtenerContactos(String URL){

        if(!mListaContactos.isEmpty()){
            mListaContactos.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Contact contact = new Contact();
                        contact.setID_Contacto(jsonObject.getInt("ID_Contacto"));
                        contact.setNombre(jsonObject.getString("NombreContacto"));
                        contact.setNumero(jsonObject.getString("Telefono"));

                        mListaContactos.add(contact);
                    }
                    catch (JSONException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                enviarSMS();
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

    //Metodo para enviar el SMS:
    private void enviarSMS(){

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},1);
        }

        for(int i=0; i<mListaContactos.size(); i++){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mListaContactos.get(i).getNumero(),null, mensaje, null ,null );
            Toast.makeText(getActivity(), "SMS no. "+ (i) + " enviado",Toast.LENGTH_SHORT).show();
        }

        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        //dismiss();
    }

    //Metodo para obtener la informacion del bundle enviado desde AddTrustedFriendsActivity.
    void getBundle(){
        mTipoAlerta = getArguments().getInt("Tipo");
    }


    public void getCredentialData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");
    }
}
