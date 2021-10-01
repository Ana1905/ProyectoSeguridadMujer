package com.example.proyectoseguridadmujer;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class WhatsAppAccessibilityService extends AccessibilityService {


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if(getRootInActiveWindow() == null){
            return;
        }

        //Getting root mode
        AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());

        //Get edit text if message from WhatsApp
        List<AccessibilityNodeInfoCompat> messageNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");

        //IF en caso de que no tenga instalada la aplicacion:
        if(messageNodeList == null || messageNodeList.isEmpty()){
            return;
        }

        //Verifica que el EditText tenga texto y termine con el sufijo indicado:
        AccessibilityNodeInfoCompat messageField = messageNodeList.get(0);
        if(messageField == null || messageField.getText().length() == 0 || messageField.getText().toString().endsWith("   ")){
            return;
        }

        //Obtiene el node list del boton de envio de mensaje de WhatsApp:
        List<AccessibilityNodeInfoCompat> sendMessageNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
        if(sendMessageNodeList == null || sendMessageNodeList.isEmpty()){
            return;
        }

        AccessibilityNodeInfoCompat sendMessage = sendMessageNodeList.get(0);
        //Verifica que el boton sea visible para el usuario:
        if(!sendMessage.isVisibleToUser()){
            return;
        }
        //Hace click en el boton de WhatsApp:
        sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK);

        //Volver a nuestra aplicacion:
        try{
            Thread.sleep(2000);
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(2000);
        }
        catch(InterruptedException ignored){
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }

    @Override
    public void onInterrupt() {

    }
}
