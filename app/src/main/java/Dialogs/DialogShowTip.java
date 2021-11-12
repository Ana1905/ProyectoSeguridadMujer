package Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectoseguridadmujer.R;
import com.example.proyectoseguridadmujer.Tip;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DialogShowTip extends DialogFragment {

    Tip mTip = new Tip();

    Button mBotonVolver;
    TextView mTextViewTipo, mTextViewTitulo, mTextViewContenido;
    ImageView mImageView1, mImageView2;

    RequestQueue requestQueue;

    public DialogShowTip() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTip.setID_Tip(getArguments().getInt("ID_Tip"));
        mTip.setTitulo(getArguments().getString("Titulo"));
        mTip.setContenido(getArguments().getString("Contenido"));
        mTip.setTipo(getArguments().getInt("Tipo"));
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.dialog_show_tip, container, false);

        //WiringUp
        mBotonVolver = root.findViewById(R.id.button_dialog_tip_back);
        mTextViewTipo = root.findViewById(R.id.TipoTip);
        mTextViewTitulo = root.findViewById(R.id.TituloTip);
        mTextViewContenido = root.findViewById(R.id.ContenidoTip);
        mImageView1 = root.findViewById(R.id.ImagenTip1);
        mImageView2 = root.findViewById(R.id.ImagenTip2);

        mBotonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        definirInfoTip();

        if(mTip.getTipo() == 1){
            obtenerImagenesTip("https://seguridadmujer.com/app_movil/Institucion/obtenerImagenesTip.php?ID_Tip="+ mTip.getID_Tip());
        }
        else{
            obtenerImagenesTip("https://seguridadmujer.com/app_movil/Institucion/obtenerImagenesTipPrevencion.php?ID_Tip="+ mTip.getID_Tip());
        }


        return root;
    }

    public void obtenerImagenesTip(String URL){
        //Vacia la lista:
        mTip.setRutaImagen1("");
        mTip.setRutaImagen2("");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        if(i==0){
                            mTip.setRutaImagen1(jsonObject.getString("RutaImagen"));
                        }
                        else{
                            mTip.setRutaImagen2(jsonObject.getString("RutaImagen"));
                        }
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                mostrarImagenesAdicionales();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void definirInfoTip(){

        if(mTip.getTipo() == 1){
            mTextViewTipo.setText("Tip");
        }
        else{
            mTextViewTipo.setText("Tip de prevención");
        }

        mTextViewTitulo.setText(mTip.getTitulo());
        mTextViewContenido.setText(mTip.getContenido());
    }

    private void mostrarImagenesAdicionales(){
        //Imagen 1:
        if(!mTip.getRutaImagen1().isEmpty()){
            mImageView1.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mTip.getRutaImagen1()).into(mImageView1);
        }else{
            mImageView1.setVisibility(View.GONE);
        }

        //Imagen 2:
        if(!mTip.getRutaImagen2().isEmpty()){
            mImageView2.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(mTip.getRutaImagen2()).into(mImageView2);
        }else{
            mImageView2.setVisibility(View.GONE);
        }
    }

}