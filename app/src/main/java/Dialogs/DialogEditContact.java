package Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.ui.alert.AlertFragment;

import org.jetbrains.annotations.NotNull;

import httpurlconnection.PutData;

public class DialogEditContact extends DialogFragment {

    EditText mEditTextNombre, mEditTextNumero;
    Button mBotonEliminar, mBotonActualizar, mBotonRegresar;

    String mID, mNombre, mTelefono;
    String email;

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void returnData(int result);
    }

    public DialogEditContact(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se obtiene la informacion del contacto enviado desde AddTrustedFriendsActivity:
        getBundle();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //WiringUp
        View root = inflater.inflate(R.layout.activity_edit_contact, container, false);

        //wiring up
        mEditTextNombre= root.findViewById(R.id.nombre);
        mEditTextNumero = root.findViewById(R.id.telefono);
        mBotonEliminar = root.findViewById(R.id.boton_eliminar_contacto);
        mBotonActualizar = root.findViewById(R.id.boton_editar_contacto);
        mBotonRegresar = root.findViewById(R.id.ButtonEditContactBack);

        //Se obtiene el email de la usuaria:
        getCredentialData();

        //Se ponen los datos en los EditText:
        mEditTextNombre.setText(mNombre);
        mEditTextNumero.setText(mTelefono);

        //onClick del boton para eliminar el contacto:
        mBotonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        //onClick del boton para acutalizar el contacto:
        mBotonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditTextNumero.getText().toString().startsWith("+52")){
                    if(mEditTextNumero.getText().toString().length()<13){
                        Toast.makeText(getActivity(), "El numero introducido es demasiado corto", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        updateContact();
                    }
                }
                else{
                    if(mEditTextNumero.getText().toString().length()<10){
                        Toast.makeText(getActivity(), "El numero introducido es demasiado corto", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(mEditTextNumero.getText().toString().startsWith("+521")){
                            //Toast.makeText(getApplicationContext(), "simon", Toast.LENGTH_SHORT).show();
                            mEditTextNumero.setText(mEditTextNumero.getText().toString().substring(0, 3) + mEditTextNumero.getText().toString().substring(4));
                        }
                        else{
                            if(!mEditTextNumero.getText().toString().startsWith("+52")){
                                mEditTextNumero.setText("+52"+mEditTextNumero.getText().toString());
                            }
                        }

                        if(mEditTextNumero.getText().toString().length()>13){
                            mEditTextNumero.setText(mEditTextNumero.getText().toString().substring(0, 13));
                        }
                        updateContact();
                    }
                }
            }
        });

        //OnClick del boton para regresar:
        mBotonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.returnData(1);
                dismiss();
            }
        });

        return root;
    }

    //Metodo para hacer el delete:
    void deleteContact(){

        String[] field = new String[1];
        field[0] = "ID_Contacto";

        //Creating array for data
        String[] data = new String[1];
        data[0] = mID;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/deleteContact.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(getActivity(), "Se ha eliminado el contacto de confianza", Toast.LENGTH_SHORT).show();
                    mListener.returnData(1);
                    dismiss();
                }
                else{
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Metodo para hacer el update del contacto:
    void updateContact(){
        String[] field = new String[6];
        field[0] = "email";
        field[1] = "newnombre";
        field[2] = "newtelefono";
        field[3] = "nombre";
        field[4] = "telefono";
        field[5] = "id";

        //Creating array for data
        String[] data = new String[6];
        data[0] = email;
        data[1] = mEditTextNombre.getText().toString();
        data[2] = mEditTextNumero.getText().toString();
        data[3] = mNombre;
        data[4] = mTelefono;
        data[5] = mID;

        //Toast.makeText(getApplicationContext(), mNombre, Toast.LENGTH_SHORT).show();

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Alert/updateContact.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(getActivity(), "Se ha actualizado el contacto de confianza", Toast.LENGTH_SHORT).show();
                    mListener.returnData(1);
                    dismiss();
                }
                else{
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*
    @Override
    public void onAttach(@NonNull @NotNull Activity activity) {
        super.onAttach(activity);
        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }
        catch (ClassCastException e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
     */

    //Metodo para obtener la informacion del bundle enviado desde AddTrustedFriendsActivity.
    void getBundle(){
        mID = String.valueOf(getArguments().getInt("ID"));
        mNombre = getArguments().getString("Nombre");
        mTelefono = getArguments().getString("Numero");
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void getCredentialData() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Credencials", Context.MODE_PRIVATE);
        email = preferences.getString("email", "");
    }
}
