package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BandVinculationActivity extends AppCompatActivity {


    private static final String TAG ="BandVinculationActivity";
    ListView IdLista;

    public static String EXTRA_DEVICE_ADRESS= "device_adress";

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter mPairedDevicesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_vinculation);
    }

    @Override
    protected void onResume() {
        super.onResume();


        VerificarEstadoBt();
        mPairedDevicesAdapter = new ArrayAdapter(this,R.layout.founded_bands);
        IdLista = (ListView) findViewById(R.id.list_bluetooth);
        IdLista.setAdapter(mPairedDevicesAdapter);
        IdLista.setOnItemClickListener(mDeviceClickListener);
        mBtAdapter= BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        if(pairedDevices.size()>0){
            for(BluetoothDevice device : pairedDevices){
                mPairedDevicesAdapter.add(device.getName() + "/n" + device.getAddress());

            }
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            String address= info.substring(info.length()-17);
            finishAffinity();


            Intent intent = new Intent(BandVinculationActivity.this, BluetoothTestingActivity.class);
            intent.putExtra(EXTRA_DEVICE_ADRESS,address);
            startActivity(intent);
        }
    };

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