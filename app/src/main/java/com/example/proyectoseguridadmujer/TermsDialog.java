package com.example.proyectoseguridadmujer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

public class TermsDialog extends AppCompatDialogFragment {
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog .Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Términos y condiciones").setMessage("Aqui van los terminos y condiciones de la aplicación").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //si quieres que al picar aceptar haga algo, ponlo aqui. Si no pones nada solo lo va a cerrar
            }
        });
        return builder.create();
    }
}
