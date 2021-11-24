package Dialogs;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoseguridadmujer.AddTrustedFriendsActivity;
import com.example.proyectoseguridadmujer.Alert;
import com.example.proyectoseguridadmujer.BandVinculationActivity;
import com.example.proyectoseguridadmujer.Contact;
import com.example.proyectoseguridadmujer.DesactivateAlertActivity;
import com.example.proyectoseguridadmujer.MainActivity;
import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import httpurlconnection.PutData;

public class DialogBandVinculation extends DialogFragment {

    Button mBotonMostrarDispositivos, mBotonTerminarConexion, mBackButton;
    ListView mListaDispositivos;
    TextView mTVMostrarDispositivos;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice[] mArrayDispositivos;

    ClientClass mClientClass;
    SendReceive mSendReceive;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    int REQUEST_ENABLE_BLUETOOTH = 1;

    private static final String APP_NAME = "ProyectoSeguridadMujer";

    //private static final UUID MY_UUID = UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66");
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final static String CHANNEL_ID = "NOTIFICACION";
    private int NOTIFICACION_ID = 0;

    boolean pulseraConectada = false;
    boolean alertaActiva = false;

    int contadorIntentos = 0;

    String email, mensaje = "";
    String mTipoAlerta = "";
    String estadoNotificaciones = "no";
    ArrayList<Contact> mListaContactos = new ArrayList<Contact>();

    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se obtiene la informacion del contacto enviado desde AddTrustedFriendsActivity:
        //getBundle();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //WiringUp
        View root = inflater.inflate(R.layout.activity_band_vinculation, container, false);

        //Wiring Up:
        mBotonMostrarDispositivos = root.findViewById(R.id.boton_mostrar_dispositivos);
        mListaDispositivos = root.findViewById(R.id.list_bluetooth);
        mTVMostrarDispositivos = root.findViewById(R.id.text_view_mostrar_dispositivos);
        mBotonTerminarConexion = root.findViewById(R.id.boton_cerrar_conexion);
        mBackButton = root.findViewById(R.id.ButtonBandVinculationBack);

        //Se esconde el boton para terminar la conexion con la pulsera y la lista:
        mListaDispositivos.setVisibility(View.GONE);
        mBotonTerminarConexion.setVisibility(View.VISIBLE);
        mBotonTerminarConexion.setVisibility(View.GONE);

        //Se obtiene el email de la usuaria:
        getCredentialData();

        /*
        if(ActivityCompat.checkSelfPermission(BandVinculationActivity.this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BandVinculationActivity.this, new String[]{Manifest.permission.CALL_PHONE},2);
        }
         */

        //Se crea el adaptador Bluetooth:
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //En caso de que el dispositivo no soporte Bluetooth, se direccionara a la MainActivity.
            Toast.makeText(getActivity(), "Su dispositivo no soporta Bluetooth, lamentamos que no será capaz de utilizar la función de alerta", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(getActivity(), MainActivity.class);
            //startActivity(intent);
            //getActivity().finish();
            dismiss();
        }
        if(!mBluetoothAdapter.isEnabled()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        //onClick del boton para mostrar la lista de dispositivos:
        mBotonMostrarDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarListaDispositivos();
            }
        });

        //onClick boton para terminar la conexion con la pulsera:
        mBotonTerminarConexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pulseraConectada){
                    mClientClass.cancel();
                }
                else{
                    Toast.makeText(getActivity(), "No tiene ninguna pulsera conectada", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //onClick del boton para regresar:
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return root;
    }

    //Handler que monitorea los estados de conexion del Bluetooth:
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case STATE_LISTENING:
                    Toast.makeText(getActivity(), "Escuchando...", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTING:
                    Toast.makeText(getActivity(), "Conectando...", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTED:
                    Toast.makeText(getActivity(), "Se ha conectado la pulsera con el dispositivo exitosamente.", Toast.LENGTH_LONG).show();
                    //Se actualiza la interfaz:
                    mTVMostrarDispositivos.setVisibility(View.GONE);
                    mBotonMostrarDispositivos.setVisibility(View.GONE);
                    mListaDispositivos.setVisibility(View.GONE);
                    mBotonTerminarConexion.setVisibility(View.VISIBLE);

                    pulseraConectada = true;
                    break;
                case STATE_CONNECTION_FAILED:
                    Toast.makeText(getActivity(), "Conexion fallida.", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;     //Se obtiene y castea el mensaje obtenido.
                    String mensajeTemporal = new String(readBuff, 0, msg.arg1);
                    Toast.makeText(getActivity(), mensajeTemporal, Toast.LENGTH_LONG).show();

                    if(mensajeTemporal.equals("ALERTA")){

                        //Verifica por medio de la base de datos si no hay una alerta actualmente:
                        crearAlerta();

                        ejecutarAlerta();
                    }
                    break;
            }
            return true;
        }
    });

    //Metodo para mostrar la lista de dispositivos vinculados:
    private void mostrarListaDispositivos(){
        //Se obtienen los dispositivos vinculados:
        Set<BluetoothDevice> listaDispositivos = mBluetoothAdapter.getBondedDevices();

        String[] stringsDispositivos = new String[listaDispositivos.size()];
        mArrayDispositivos = new BluetoothDevice[listaDispositivos.size()];
        int contador = 0;

        if(listaDispositivos.size()>0){
            //Se crea una lista con los nombres de los dispositivos vinculados:
            for(BluetoothDevice dispositivo:listaDispositivos){
                mArrayDispositivos[contador] = dispositivo;
                stringsDispositivos[contador] = dispositivo.getName();
                contador++;
            }

            //Se carga la lista de los dispositivos en el ListView por medio de un ArrayAdapter:
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stringsDispositivos);
            mListaDispositivos.setAdapter(arrayAdapter);
            mListaDispositivos.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(getActivity(), "No tiene dispositivos vinculados.", Toast.LENGTH_SHORT).show();
        }

        //onClick de los elementos de la lista:
        mListaDispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(stringsDispositivos[position].equals("SEGURIDAD MUJER CETI")){
                    Toast.makeText(getActivity(), "Conectando...", Toast.LENGTH_SHORT).show();

                    mClientClass = new ClientClass(mArrayDispositivos[position]);
                    mClientClass.start();
                }
                else{
                    Toast.makeText(getActivity(), "Este dispositivo no es una pulsera, favor de seleccionar una pulsera identificada bajo el nombre de 'SEGURIDAD MUJER CETI'", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Hilo que realiza la conexion con la pulsera
    private class ClientClass extends Thread{
        private BluetoothDevice dispositivo;
        private BluetoothSocket socket;

        //El constructor crea un socket para efectuar la conexion obteniendo la informacion del dispositivo correspondiente a la pulsera:
        public ClientClass(BluetoothDevice dispositivoVinculado){
            dispositivo = dispositivoVinculado;

            try {
                socket = dispositivo.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //En el metodo run se intenta realizar la conexion por medio de socket.connect().
        public void run(){
            try {
                socket.connect();
                Message mensaje = Message.obtain();
                mensaje.what = STATE_CONNECTED;
                handler.sendMessage(mensaje);

                //Codigo para enviar/recibir informacion
                mSendReceive = new SendReceive(socket);
                mSendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message mensaje = Message.obtain();
                mensaje.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(mensaje);
            }
        }

        //Metodo para cerrar el socket y terminar la conexion Bluetooth:
        public void cancel() {
            try {
                socket.close();
                pulseraConectada = false;
                Toast.makeText(getActivity(), "Se ha concluido la conexion Bluetooth con la pulsera.", Toast.LENGTH_LONG).show();
                //Se actualiza la interfaz:
                mTVMostrarDispositivos.setVisibility(View.VISIBLE);
                mBotonMostrarDispositivos.setVisibility(View.VISIBLE);
                mListaDispositivos.setVisibility(View.GONE);
                mListaDispositivos.setAdapter(null);
                mBotonTerminarConexion.setVisibility(View.GONE);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "No se pudo terminar la conexion Bluetooth con la pulsera.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Hilo para enviar y recibir informacion por medio del socket Bluetooth:
    private class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        //En el constructor se proporciona el socket que contiene la conexion con la pulsera:
        public SendReceive(BluetoothSocket socket){
            bluetoothSocket = socket;
            InputStream inputTemporal = null;
            OutputStream outputTemporal = null;

            try {
                inputTemporal = bluetoothSocket.getInputStream();
                outputTemporal = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = inputTemporal;
            outputStream = outputTemporal;
        }

        //Lectura de informacion entrante:
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while(true){
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Metodo para enviar informacion:
        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
                Toast.makeText(getActivity(), "Mensaje enviado.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "No se pudo enviar el mensaje.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearAlerta(){
        String[] field = new String[1];
        field[0] = "email";

        String[] data = new String[1];
        data[0] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/crearAlerta.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                if (result.equals("Success")) {
                    alertaActiva = true;
                }
                else{
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    alertaActiva = false;
                    reintentarEnvio();
                }
            }
        }
    }

    private void ejecutarAlerta(){
        if(alertaActiva){
            //Se ejecuta el hilo para comprobar si ya pasaron las 2 horas:
            cerrarActivity();

            verificarTipoAlerta();

            if(!mTipoAlerta.isEmpty()){

                switch (mTipoAlerta){
                    case "1":
                        //WhatsApp
                        enviarWhatsApp();
                        break;
                    case "2":
                        obtenerContactos("https://seguridadmujer.com/app_movil/Alert/ObtenerContactosConfianza.php?email="+email);
                        break;
                    case "3":
                        //SMS o llamada
                        obtenerAlerta("https://seguridadmujer.com/app_movil/Alert/ObtenerAlerta.php?email="+email);
                        break;
                }
            }
        }
    }

    //Obtiene el tipo de alerta:
    private void verificarTipoAlerta(){
        String[] field = new String[1];
        field[0] = "email";

        String[] data = new String[1];
        data[0] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/verificarTipoAlerta.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();

                if (result.equals("1") || result.equals("2")  || result.equals("3") ) {
                    mTipoAlerta = result;
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void enviarWhatsApp(){
        String[] field = new String[1];
        field[0] = "email";

        String[] data = new String[1];
        data[0] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/alerta.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(getActivity(), "Se ha enviado el mensaje de WhatsApp", Toast.LENGTH_SHORT).show();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    DialogDesactivateAlert dialogDesactivateAlert = new DialogDesactivateAlert();

                    Bundle bundle = new Bundle();
                    bundle.putInt("Tipo", 1);

                    dialogDesactivateAlert.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.add(android.R.id.content, dialogDesactivateAlert).addToBackStack(null).commit();
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

                obtenerAlerta("https://seguridadmujer.com/app_movil/Alert/ObtenerAlerta.php?email="+email);
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

    private void obtenerAlerta(String URL){
        Alert mAlert = new Alert();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mAlert.setID_Alerta(jsonObject.getInt("ID_Alerta"));
                        mAlert.setTipoAlerta(jsonObject.getInt("TipoAlerta"));

                        if(mAlert.getTipoAlerta() != 3){
                            mAlert.setMensaje(jsonObject.getString("Mensaje"));

                            if(!jsonObject.getString("RutaImagen").isEmpty() && !jsonObject.getString("TipoImagen").isEmpty()){
                                mAlert.setRutaImagen(jsonObject.getString("RutaImagen"));
                                mAlert.setTipoImagen(jsonObject.getString("TipoImagen"));
                            }
                            else{
                                mAlert.setRutaImagen("");
                                mAlert.setTipoImagen("");
                            }
                        }
                        else{
                            mAlert.setID_Contacto(jsonObject.getInt("ID_Contacto"));
                            mAlert.setTelefonoContacto(jsonObject.getString("Telefono"));
                        }

                        //Toast.makeText(getApplicationContext(), mListaContactos.get(i).getNombre() + " " + mListaContactos.get(i).getNumero(), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                switch (mTipoAlerta){
                    case "1":
                        //WhatsApp
                        break;
                    case "2":
                        //SMS
                        enviarSMS(mAlert);
                        break;
                    case "3":
                        //Llamada
                        llamadaTelefonica(mAlert);
                        break;
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

    //Metodo para enviar el SMS:
    private void enviarSMS(Alert mAlert){

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},1);
        }

        for(int i=0; i<mListaContactos.size(); i++){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mListaContactos.get(i).getNumero(),null, mAlert.getMensaje(), null ,null );
            //Toast.makeText(BandVinculationActivity.this, "SMS no. "+ (i) + " enviado",Toast.LENGTH_SHORT).show();
        }


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogDesactivateAlert dialogDesactivateAlert = new DialogDesactivateAlert();

        Bundle bundle = new Bundle();
        bundle.putInt("Tipo", 2);

        dialogDesactivateAlert.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.add(android.R.id.content, dialogDesactivateAlert).addToBackStack(null).commit();
    }

    //Metodo para realizar la llamada telefonica:
    private void llamadaTelefonica(Alert mAlert){

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},2);
        }

        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mAlert.getTelefonoContacto()));
        startActivity(i);

        eliminarAlerta();
    }

    //Este metodo crea un hilo para redirigir a Login Activity pasada la media hora de espera para la confirmacion de la cuenta:
    public void cerrarActivity(){
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {

                //Creating array for parameters
                String[] field = new String[1];
                field[0] = "email";

                //Creating array for data
                String[] data = new String[1];
                data[0] = email;

                PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/verificarAlertaConfirmada.php", "POST", field, data);
                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
                        if(result.equals("Success")) {
                            mensaje = "Me encuentro en peligro, han transcurrido 2 horas desde la activación de mi señal de alerta y no he notificado a la aplicación de seguridad para la mujer que me encuentro a salvo";
                            switch (mTipoAlerta){
                                case "1":
                                    enviarWhatsAppThread();
                                    break;
                                case "2":
                                    obtenerContactosThread("https://seguridadmujer.com/app_movil/Alert/ObtenerContactosConfianza.php?email="+email);
                                    break;
                                case "3":
                                    //Intent intent = new Intent(getActivity(), AddTrustedFriendsActivity.class);
                                    //startActivity(intent);
                                    dismiss();
                                    break;
                            }
                            getActivity().finish();
                        }
                        else{
                            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                            if(result.equals("Stop")){
                                getActivity().finish();
                            }
                            else{
                                handler.postDelayed(this, 15000);
                            }
                        }
                    }
                }
            }
        }, 15000);
    }

    private void enviarWhatsAppThread(){
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

                    //Intent i = new Intent(getActivity(), AddTrustedFriendsActivity.class);
                    //startActivity(i);
                    dismiss();
                }
                else{
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Metodo para obtener los contactos de confianza:
    private void obtenerContactosThread(String URL){

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

                enviarSMSthread();
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
    private void enviarSMSthread(){

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},1);
        }

        for(int i=0; i<mListaContactos.size(); i++){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mListaContactos.get(i).getNumero(),null, mensaje, null ,null );
            //Toast.makeText(BandVinculationActivity.this, "SMS no. "+ (i) + " enviado",Toast.LENGTH_SHORT).show();
        }

        //Intent i = new Intent(getActivity(), AddTrustedFriendsActivity.class);
        //startActivity(i);
        dismiss();
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
                if(!result.equals("Success")){
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void reintentarEnvio(){
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {

                Toast.makeText(getActivity(), String.valueOf(contadorIntentos), Toast.LENGTH_SHORT).show();
                /*
                if(contadorIntentos == 0 && estadoNotificaciones.equals("si")){
                    crearNotificacion();
                }
                 */

                String[] field = new String[1];
                field[0] = "email";

                String[] data = new String[1];
                data[0] = email;

                PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/crearAlerta.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();

                        if (result.equals("Success")) {
                            contadorIntentos = 0;
                            alertaActiva = true;
                            createNotificationChannel();
                            createNotification();
                            ejecutarAlerta();
                            getActivity().finish();
                        }
                        else{
                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                            alertaActiva = false;

                            if (contadorIntentos < 10){
                                if(estadoNotificaciones.equals("si")){
                                    createNotificationChannel();
                                    createNotification();
                                }
                                handler.postDelayed(this, 10000);
                                contadorIntentos++;
                            }
                            else{
                                contadorIntentos = 0;
                                getActivity().finish();
                            }

                        }
                    }
                }

            }
        }, 10000);
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Toast.makeText(getApplicationContext(), "Version mayor a O", Toast.LENGTH_SHORT).show();
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("No se pudo enviar la alerta");
        if(contadorIntentos == 9){
            builder.setContentText("Se han agotado los intentos para volver a enviar su alerta.");
        }
        else{
            builder.setContentText("No hay conexión a internet, intentando nuevamente...");
        }
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        Intent intent = new Intent(getActivity(), AddTrustedFriendsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());

        NOTIFICACION_ID++;
    }

    public void getCredentialData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");

        SharedPreferences notificaciones = getActivity().getSharedPreferences("Notificaciones", Context.MODE_PRIVATE);
        estadoNotificaciones = notificaciones.getString("estado", "");
        Toast.makeText(getActivity(), "Notificaciones: " + estadoNotificaciones, Toast.LENGTH_SHORT).show();
    }
}
