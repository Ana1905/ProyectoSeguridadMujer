package com.example.proyectoseguridadmujer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BandVinculationActivity extends AppCompatActivity {

    Button mBotonMostrarDispositivos, mBotonTerminarConexion;
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

    boolean alertaRecibida = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_vinculation);

        //Wiring Up:
        mBotonMostrarDispositivos = findViewById(R.id.boton_mostrar_dispositivos);
        mListaDispositivos = findViewById(R.id.list_bluetooth);
        mTVMostrarDispositivos = findViewById(R.id.text_view_mostrar_dispositivos);
        mBotonTerminarConexion = findViewById(R.id.boton_cerrar_conexion);

        //Se esconde el boton para terminar la conexion con la pulsera y la lista:
        mListaDispositivos.setVisibility(View.GONE);
        mBotonTerminarConexion.setVisibility(View.GONE);

        //Se crea el adaptador Bluetooth:
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //En caso de que el dispositivo no soporte Bluetooth, se direccionara a la MainActivity.
            Toast.makeText(getApplicationContext(), "Su dispositivo no soporta Bluetooth, lamentamos que no será capaz de utilizar la función de alerta", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
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
                /*
                String string = "Hola pulsera";
                mSendReceive.write(string.getBytes());
                */
                mClientClass.cancel();
            }
        });
    }

    //Handler que monitorea los estados de conexion del Bluetooth:
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what){
                case STATE_LISTENING:
                    Toast.makeText(getApplicationContext(), "Escuchando...", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTING:
                    Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(), "Se ha conectado la pulsera con el dispositivo exitosamente.", Toast.LENGTH_LONG).show();
                    //Se actualiza la interfaz:
                    mTVMostrarDispositivos.setVisibility(View.GONE);
                    mBotonMostrarDispositivos.setVisibility(View.GONE);
                    mListaDispositivos.setVisibility(View.GONE);
                    mBotonTerminarConexion.setVisibility(View.VISIBLE);
                    break;
                case STATE_CONNECTION_FAILED:
                    Toast.makeText(getApplicationContext(), "Conexion fallida.", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;     //Se obtiene y castea el mensaje obtenido.
                    String mensajeTemporal = new String(readBuff, 0, msg.arg1);
                    Toast.makeText(getApplicationContext(), mensajeTemporal, Toast.LENGTH_LONG).show();
                    if(mensajeTemporal.equals("ALERTA") && !alertaRecibida){
                        alertaRecibida = true;
                        Toast.makeText(getApplicationContext(), "Modo de alerta activado", Toast.LENGTH_LONG).show();
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
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, stringsDispositivos);
            mListaDispositivos.setAdapter(arrayAdapter);
            mListaDispositivos.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(getApplicationContext(), "No tiene dispositivos vinculados.", Toast.LENGTH_SHORT).show();
        }

        //onClick de los elementos de la lista:
        mListaDispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(stringsDispositivos[position].equals("SEGURIDAD MUJER CETI")){
                    Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_SHORT).show();

                    mClientClass = new ClientClass(mArrayDispositivos[position]);
                    mClientClass.start();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Este dispositivo no es una pulsera, favor de seleccionar una pulsera identificada bajo el nombre de 'SEGURIDAD MUJER CETI'", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class ServerClass extends Thread{
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run(){
            BluetoothSocket socket=null;

            while(socket == null){
                try {
                    Message mensaje = Message.obtain();
                    mensaje.what = STATE_CONNECTING;
                    handler.sendMessage(mensaje);

                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();

                    Message mensaje = Message.obtain();
                    mensaje.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(mensaje);
                }

                if(socket != null){
                    Message mensaje = Message.obtain();
                    mensaje.what = STATE_CONNECTED;
                    handler.sendMessage(mensaje);

                    //Codigo para enviar/recibir informacion
                    mSendReceive = new SendReceive(socket);
                    mSendReceive.start();
                    break;
                }
            }
        }
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
                Toast.makeText(getApplicationContext(), "Se ha concluido la conexion Bluetooth con la pulsera.", Toast.LENGTH_LONG).show();
                //Se actualiza la interfaz:
                mTVMostrarDispositivos.setVisibility(View.VISIBLE);
                mBotonMostrarDispositivos.setVisibility(View.VISIBLE);
                mListaDispositivos.setVisibility(View.GONE);
                mListaDispositivos.setAdapter(null);
                mBotonTerminarConexion.setVisibility(View.GONE);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "No se pudo terminar la conexion Bluetooth con la pulsera.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Mensaje enviado.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No se pudo enviar el mensaje.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(BandVinculationActivity.this);
        startActivity(intent);
        finish();
    }
}