package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;

import java.util.SplittableRandom;
import java.util.UUID;

public class BluetoothTestingActivity extends AppCompatActivity {


    Handler edtTextOut;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    //private ConnectedThread MyConexionBt;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34");
    private static String address= null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_testing);

       // bluetoothIn = new Handler();
        //public void handleMessage(android.os.Message msg){
          //  if(msg.what == handlerState){

          //  }
       // }

    }
}