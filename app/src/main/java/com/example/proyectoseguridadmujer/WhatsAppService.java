package com.example.proyectoseguridadmujer;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.net.URLEncoder;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WhatsAppService extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_WHATSAPP = "com.example.proyectoseguridadmujer.action.WHATSAPP";

    // TODO: Rename parameters
    private static final String MESSAGE = "com.example.proyectoseguridadmujer.extra.PARAM1";
    private static final String COUNT = "com.example.proyectoseguridadmujer.extra.PARAM2";
    private static final String MOBILE_NUMBER = "com.example.proyectoseguridadmujer.extra.PARAM3";

    public WhatsAppService() {
        super("WhatsAppService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionWhatsApp(Context context, String message, String count, String mobile_number) {
        Intent intent = new Intent(context, WhatsAppService.class);
        intent.setAction(ACTION_WHATSAPP);
        intent.putExtra(MESSAGE, message);
        intent.putExtra(COUNT, count);
        intent.putExtra(MOBILE_NUMBER, mobile_number);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_WHATSAPP.equals(action)) {
                final String message = intent.getStringExtra(MESSAGE);
                final String count = intent.getStringExtra(COUNT);
                final String mobile_number = intent.getStringExtra(MOBILE_NUMBER);
                handleActionWhatsApp(message, count, mobile_number);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    public void handleActionWhatsApp(String message, String count, String mobile_number){
        try{
            PackageManager packageManager = getApplicationContext().getPackageManager();
            for(int j=0; j<Integer.parseInt(count); j++){
              /*  Toast.makeText(getApplicationContext(), "Entro al for del servicio, iteracion" + j, Toast.LENGTH_SHORT).show();
                String url = "https://api.whatsapp.com?phone="+mobile_number+"&text=" + URLEncoder.encode(message, "UTF-8");
                //String url = "https://api.whatsapp.com?phone=523323495386&text=Hola pauchis";
                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);*/


                Intent whatsappIntent = new Intent();
                whatsappIntent.setAction(Intent.ACTION_VIEW);
                whatsappIntent.setAction(Intent.ACTION_VIEW);
               // String url = "whatsapp://send?phone=523323495386&text=hola Pau";
                String url = "whatsapp://send?phone=59165868685&text=Este es el mensaje a cambiar";


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