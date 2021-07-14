package Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;

public class DialogOtherCategory extends DialogFragment {

    EditText mEditTextCategory;
    Button mButtonAccept;
    String category;

    public DialogOtherCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dialog_other_category, container, false);
        mEditTextCategory =  root.findViewById(R.id.EditTextCategory);
        mButtonAccept =  root.findViewById(R.id.ButtonAccept);

        mButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                category = String.valueOf(mEditTextCategory.getText()).trim();
                if(category.equals("")){
                    Toast.makeText(getContext(), "Por favor escriba su categoria", Toast.LENGTH_SHORT).show();
                }
                else {
                    dismiss();
                }
            }
        });

        return root;
    }


    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }




}
