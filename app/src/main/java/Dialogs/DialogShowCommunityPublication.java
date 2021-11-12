package Dialogs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.proyectoseguridadmujer.Comment;
import com.example.proyectoseguridadmujer.CommentListAdapter;
import com.example.proyectoseguridadmujer.ListElement;
import com.example.proyectoseguridadmujer.PublicationExtraImageAdapter;
import com.example.proyectoseguridadmujer.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import httpurlconnection.PutData;

public class DialogShowCommunityPublication extends DialogFragment {

    ListElement mPublication = new ListElement();
    ArrayList<Comment> mListaComentarios = new ArrayList<Comment>();
    CommentListAdapter mAdapter;
    PublicationExtraImageAdapter ImageAdapter;

    Button BackButton, EditButton;
    ImageView CommentImage;
    TextView User, Category, ImageLabel, CommentLabel;
    EditText Content;
    RecyclerView CommentRecycler, ImageRecycler;
    ArrayList<String> mListaImagenes = new ArrayList<String>();

    String email;

    RequestQueue requestQueue;

    public DialogShowCommunityPublication () {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPublication.setID_Publicacion(getArguments().getInt("ID"));
        mPublication.setID_Usuaria(getArguments().getInt("ID_Usuaria"));
        mPublication.setCategoria(getArguments().getInt("ID_Cateegoria"));
        mPublication.setContenido(getArguments().getString("Contenido"));
        mPublication.setFechaPublicacion(getArguments().getString("Fecha"));
        mPublication.setNombre(getArguments().getString("Nombre"));
        mPublication.setNombreCategoria(getArguments().getString("NombreCategoria"));
        mPublication.setRutaImagen(getArguments().getString("RutaImagen"));
        mPublication.setCorreo(getArguments().getString("Correo"));
        mPublication.setApellidoPaterno(getArguments().getString("ApellidoPaterno"));
        mPublication.setApellidoMaterno(getArguments().getString("ApellidoMaterno"));
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.dialog_publication_comment, container, false);

        getCredentialData();

        //WiringUp
        BackButton = root.findViewById(R.id.button_dialog_back);
        ImageLabel = root.findViewById(R.id.PublicationImageLabel);
        CommentLabel = root.findViewById(R.id.PublicationCommentLabel);

        CommentRecycler = root.findViewById(R.id.CommentRecycler);
        //CommentRecycler.setAdapter(commentListAdapter);
        CommentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageRecycler = root.findViewById(R.id.ImageRecycler);
        ImageRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        CommentImage = root.findViewById(R.id.CommentRowImage);
        User = root.findViewById(R.id.CommentRowUser);
        Category = root.findViewById(R.id.PostCategory);
        Content = root.findViewById(R.id.CommentRowContent);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        EditButton = root.findViewById(R.id.EditCommentButton);
        if(mPublication.getCorreo().equals(email)){
            EditButton.setVisibility(View.VISIBLE);
        }
        else{
            EditButton.setVisibility(View.GONE);
        }


        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Content.isEnabled())
                {
                    Content.setEnabled(false);
                    updateCommentContent(mPublication.getID_Publicacion(), Content.getText().toString());
                }
                else
                {
                    Content.setEnabled(true);
                }
            }
        });

        setPublicationInfo();

        getCommentList("https://seguridadmujer.com/app_movil/Community/getCommentList.php?ID_Publicacion="+mPublication.getID_Publicacion());

        return root;
    }

    public void updateCommentContent(int ID, String newComment)
    {
        String[] field = new String[2];
        field[0] = "ID_Publicacion";
        field[1] = "Comentario";

        String[] data = new String[2];
        data[0] = String.valueOf(ID);
        data[1] = newComment;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/EditarPublicacion.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    Toast.makeText(getContext().getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext().getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void getCommentList(String URL){
        //Vacia la lista:
        if(!mListaComentarios.isEmpty()){
            mListaComentarios.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Comment comment = new Comment();

                        comment.setComentario(jsonObject.getString("Comentario"));
                        comment.setFecha(jsonObject.getString("Fecha"));

                        //Imagen
                        if(jsonObject.getString("RutaImagen") != null && !jsonObject.getString("RutaImagen").isEmpty()){
                            comment.setRutaImagen(jsonObject.getString("RutaImagen"));
                        }
                        else{
                            comment.setRutaImagen("");
                        }

                        //Nombre
                        if(jsonObject.getString("Nombre") != null && !jsonObject.getString("Nombre").isEmpty()){
                            comment.setNombre(jsonObject.getString("Nombre") + " " + jsonObject.getString("ApellidoPaterno") + " " + jsonObject.getString("ApellidoMaterno"));
                        }
                        else{
                            comment.setNombre("");
                        }

                        mListaComentarios.add(comment);
                        //Toast.makeText(getContext(), comment.getComentario() + ", " + comment.getFecha() + ", " + comment.getNombre() + ", " + comment.getRutaImagen(), Toast.LENGTH_SHORT).show();
                    }
                    catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                setAdapter();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

        obtenerImagenes("https://seguridadmujer.com/app_movil/Community/obtenerImagenesAdicionales.php?ID_Publicacion="+mPublication.getID_Publicacion());
    }

    private void setAdapter(){
        mAdapter = new CommentListAdapter(getContext(), mListaComentarios);
        CommentRecycler.setAdapter(mAdapter);
        if (mListaComentarios.isEmpty())
        {
            CommentLabel.setVisibility(View.GONE);
            CommentRecycler.setVisibility(View.GONE);
        }
        else
        {
            CommentLabel.setVisibility(View.VISIBLE);
            CommentRecycler.setVisibility(View.VISIBLE);
        }
    }

    public void obtenerImagenes(String URL){
        //Vacia la lista:
        if(!mListaImagenes.isEmpty()){
            mListaImagenes.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        mListaImagenes.add(jsonObject.getString("RutaImagen"));
                        //Toast.makeText(getApplicationContext(), jsonObject.getString("RutaImagen"), Toast.LENGTH_SHORT).show();
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

    private void mostrarImagenesAdicionales(){
        if(!mListaImagenes.isEmpty()){
            ImageLabel.setVisibility(View.VISIBLE);
            ImageAdapter = new PublicationExtraImageAdapter(getContext(), mListaImagenes);
            ImageRecycler.setAdapter(ImageAdapter);
            ImageRecycler.setVisibility(View.VISIBLE);
        }
        else{
            ImageLabel.setVisibility(View.GONE);
            ImageRecycler.setVisibility(View.GONE);
        }
    }

    private void setPublicationInfo(){
        User.setText(mPublication.getNombre() + " " + mPublication.getApellidoPaterno() + " " + mPublication.getApellidoMaterno());
        Category.setText(mPublication.getNombreCategoria());
        Content.setText(mPublication.getContenido());

        if(mPublication.getRutaImagen() != null && !mPublication.getRutaImagen().isEmpty()){
            Glide.with(getContext()).load(mPublication.getRutaImagen()).into(CommentImage);
        }
        else{
            Glide.with(getContext()).load("http://seguridadmujer.com/web/Resources/avatar_woman_people_girl_glasses_icon_159125.png").into(CommentImage);
        }
    }

    public void getCredentialData()
    {
        SharedPreferences preferences = getContext().getSharedPreferences("Credencials",getContext().MODE_PRIVATE);
        email = preferences.getString("email", "");
    }
}