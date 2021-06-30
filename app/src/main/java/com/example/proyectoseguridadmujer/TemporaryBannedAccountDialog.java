package com.example.proyectoseguridadmujer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

public class TemporaryBannedAccountDialog extends AppCompatDialogFragment {
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cuenta temporalmente bloqueada").setMessage("La cuenta ingresada está suspendida por incumplimiento en las normativas y no se puede acceder a ella, gracias.").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //si quieres que al picar aceptar haga algo, ponlo aqui. Si no pones nada solo lo va a cerrar
            }
        });
        return builder.create();
    }
}