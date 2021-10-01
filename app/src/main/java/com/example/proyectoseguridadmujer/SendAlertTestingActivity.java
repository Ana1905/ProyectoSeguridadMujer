package com.example.proyectoseguridadmujer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SendAlertTestingActivity extends AppCompatActivity {
    Button mButtonWhatssap;
    Button mButtonSMS;
    Button  mButtonCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_alert_testing);

        //WIRING UP
        mButtonWhatssap= findViewById(R.id.WhatssapButton);
        mButtonSMS = findViewById(R.id.SMSButton);
        mButtonCall= findViewById(R.id.CallButton);

        if(ActivityCompat.checkSelfPermission(SendAlertTestingActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SendAlertTestingActivity.this, new String[]{Manifest.permission.SEND_SMS},1);
        }

        if(ActivityCompat.checkSelfPermission(SendAlertTestingActivity.this,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SendAlertTestingActivity.this, new String[]{Manifest.permission.CALL_PHONE},2);
        }
        /*
        if(isAcc){

        }
        */
        mButtonSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("3323495386",null,"Hola", null ,null );
                Toast.makeText(SendAlertTestingActivity.this, "SMS enviado",Toast.LENGTH_SHORT).show();
            }
        });

        mButtonWhatssap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_VIEW);
                String uri = "whatsapp://send?phone=523323495386&text=hola Pau";
                sendIntent.setData(Uri.parse(uri));
                startActivity(sendIntent);
                */

                /*
                WhatsAppService.startActionWhatsApp(getApplicationContext(), "Hola pauchis", "1", "3323495386");
                Toast.makeText(getApplicationContext(), "Se mando a llamar el servicio desde la activity", Toast.LENGTH_SHORT).show();
                */

                /*
                handleActionWhatsApp("Hola pauchis", "2", "3323495386");
                 */

                String id = "wDgTARsoR0SMummcd9a3OXBhdWxpbml0YXgzX2F0X2dtYWlsX2RvdF9jb20=";
                String celular = "523323495386";
                String mensaje = "Hola pauchis";

                HttpURLConnection conexion = null;

                try{
                    URL enlace = new URL("https://NiceApi.net/API");
                    conexion = (HttpURLConnection) enlace.openConnection();
                    conexion.setRequestMethod("POST");
                    conexion.setRequestProperty("X-APIId", id);
                    conexion.setRequestProperty("X-APIMobile", celular);
                    conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conexion.setUseCaches(false);
                    conexion.setDoOutput(true);


                    DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
                    salida.writeBytes(mensaje);
                    salida.close();

                    /*
                    InputStream entrada = conexion.getInputStream();
                    BufferedReader lectura = new BufferedReader(new InputStreamReader(entrada));

                    lectura.close();
                    */
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if(conexion != null){
                        conexion.disconnect();
                    }
                }
            }

        });

        mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:3323495386"));
                startActivity(i);
            }
        });
        /*
        IntentFilter intent = new IntentFilter("my.own.broadcast");
        LocalBroadcastManager.getInstance(this).registerReceiver(myL);
         */
    }

    public void handleActionWhatsApp(String message, String count, String mobile_number){
        try{
            Log.i("whatsapp", "Entro al handle");
            PackageManager packageManager = getApplicationContext().getPackageManager();
            for(int j=0; j<Integer.parseInt(count); j++){
                Log.i("whatsapp", "Entro al bucle");
                String url = "https://api.whatsapp.com?phone="+mobile_number+"&text=" + URLEncoder.encode(message, "UTF-8");
                //String url = "https://api.whatsapp.com?phone=523323495386&text=Hola pauchis";
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);

                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.setData(Uri.parse(url));
                whatsappIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if(whatsappIntent.resolveActivity(packageManager) != null){
                    Toast.makeText(getApplicationContext(), "!=  null", Toast.LENGTH_SHORT).show();
                    getApplicationContext().startActivity(whatsappIntent);
                    Thread.sleep(10000);
                    sendBroadcastMessage("Result: " + mobile_number);
                }
                else{
                    Toast.makeText(getApplicationContext(), "nulp", Toast.LENGTH_SHORT).show();
                    sendBroadcastMessage("WhatsApp no se encuentra instalado");
                }
            }
        }
        catch(Exception e){
            sendBroadcastMessage("Result: " + e.toString());
        }
    }

    public void sendBroadcastMessage(String message){
        Intent localIntent = new Intent("my.own.broadcast");
        localIntent.putExtra("result", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

}