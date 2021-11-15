package com.example.proyectoseguridadmujer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

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
        AlertDialog .Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogeTheme);
        builder.setTitle("Términos y condiciones").setMessage(getString(R.string.terminos_y_condiciones));
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //si quieres que al picar aceptar haga algo, ponlo aqui. Si no pones nada solo lo va a cerrar
            }
        });

        return builder.create();
    }
}
