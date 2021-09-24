package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BandVinculationActivity extends AppCompatActivity {

    private static final String TAG = "BandVinculationActivity";
    ListView IdLista;
    BluetoothSocket socket;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    public static String EXTRA_DEVICE_ADRESS = "device_adress";

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter mPairedDevicesAdapter;
    Handler bluetoothIn;
    final int handlerState = 0;
    ConnectedThread connectedThread ;

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);         //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_vinculation);
        bluetoothIn = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VerificarEstadoBt();
        mPairedDevicesAdapter = new ArrayAdapter(this, R.layout.founded_bands);
        IdLista = (ListView) findViewById(R.id.list_bluetooth);
        IdLista.setAdapter(mPairedDevicesAdapter);
        IdLista.setOnItemClickListener(mDeviceClickListener);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesAdapter.add(device.getName() + "/n" + device.getAddress());

            }
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            finishAffinity();
            conectBand(address);
        }


    };


    private void conectBand(String device) {
        BluetoothDevice mDevice = mBtAdapter.getRemoteDevice(device);
        try {
            socket = createBluetoothSocket(mDevice);
        }
        catch (IOException e){
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }

        try {
           socket.connect();
        }
        catch (IOException e){

              try{
                  socket.close();
              }
              catch(IOException e2){
                  Toast.makeText(getBaseContext(), "FATAL ERROR", Toast.LENGTH_LONG).show();
              }
        }

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        connectedThread.write("1");

}

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    private void VerificarEstadoBt(){

        mBtAdapter= BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter==null){
            Toast.makeText(this, "El dispositivo no soporta luetooth", Toast.LENGTH_SHORT).show();;
        } else{
            if(mBtAdapter.isEnabled()){
                Log.d(TAG, "...Bluetooth activado");
            } else{
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,1);
            }
        }
    }

}