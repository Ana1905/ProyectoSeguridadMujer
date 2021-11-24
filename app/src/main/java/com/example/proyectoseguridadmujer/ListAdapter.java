package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Dialogs.DialogNewPostFragment;
import Dialogs.DialogReportPublication;
import Dialogs.DialogShowCommunityPublication;
import Dialogs.DialogUserProfile;
import httpurlconnection.PutData;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.PublicationViewHolder> implements Filterable
{
    private Context context;
    private List<ListElement> list = new ArrayList<>();
    private List<ListElement> FullList = new ArrayList<>();
    private String email;
    private boolean filtrando = false;
    DialogReportPublication.ListenerReportPublication listenerReportPublication;
    DialogShowCommunityPublication.ListenerShowCommunityPublication listenerShowCommunityPublication;
    DialogUserProfile.ListenerUserProfile listenerUserProfile;

    ArrayList<Sancion> mListaSanciones = new ArrayList<Sancion>();
    RequestQueue requestQueue;


    public ListAdapter(Context context, List<ListElement> itemList, DialogReportPublication.ListenerReportPublication listenerReportPublication, DialogShowCommunityPublication.ListenerShowCommunityPublication listenerShowCommunityPublication, DialogUserProfile.ListenerUserProfile listenerUserProfile)
    {
        this.context = context;
        this.list = itemList;
        this.listenerReportPublication = listenerReportPublication;
        this.listenerShowCommunityPublication = listenerShowCommunityPublication;
        this.listenerUserProfile = listenerUserProfile;
        FullList = list;
        filtrando = false;
    }


    @Override
    public PublicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_post,null);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PublicationViewHolder holder, final int position) {
        getCredentialData();

        holder.Username.setText(list.get(position).getNombre() + " " + list.get(position).getApellidoPaterno() + " " + list.get(position).getApellidoMaterno());
        holder.Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                DialogUserProfile dialogUserProfile = new DialogUserProfile();

                Bundle arguments = new Bundle();
                arguments.putString("Usuario", list.get(position).getNombre() + " " + list.get(position).getApellidoPaterno() + " " + list.get(position).getApellidoMaterno());
                arguments.putInt("ID_Usuaria",list.get(position).getID_Usuaria());
                arguments.putString("Imagen", list.get(position).getRutaImagen());

                dialogUserProfile.setArguments(arguments);
                dialogUserProfile.setListener(listenerUserProfile);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, dialogUserProfile).addToBackStack(null).commit();
            }
        });

        holder.Category.setText(list.get(position).getNombreCategoria());
        holder.Content.setText(list.get(position).getContenido());

        if(list.get(position).getRutaImagen() != null && !list.get(position).getRutaImagen().isEmpty() && !list.get(position).getRutaImagen().equals("")){
            Glide.with(context).load(list.get(position).getRutaImagen()).into(holder.Profile);
        }
        else{
            Glide.with(context).load("http://seguridadmujer.com/web/Resources/avatar_woman_people_girl_glasses_icon_159125.png").into(holder.Profile);
        }
        holder.Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
                DialogUserProfile dialogUserProfile = new DialogUserProfile();

                Bundle arguments = new Bundle();
                arguments.putString("Usuario", list.get(position).getNombre() + " " + list.get(position).getApellidoPaterno() + " " + list.get(position).getApellidoMaterno());
                arguments.putInt("ID_Usuaria", list.get(position).getID_Usuaria());
                arguments.putString("Imagen", list.get(position).getRutaImagen());

                dialogUserProfile.setArguments(arguments);
                dialogUserProfile.setListener(listenerUserProfile);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, dialogUserProfile).addToBackStack(null).commit();
            }
        });

        holder.bind(list.get(position));

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context.getApplicationContext(), "Borrando", Toast.LENGTH_SHORT).show();
                dialogo(position);
            }
        });

        //Toast.makeText(context, email, Toast.LENGTH_SHORT).show();
        if(list.get(position).getCorreo().equals(email)){
            holder.Delete.setVisibility(View.VISIBLE);
        }
        else{
            holder.Delete.setVisibility(View.GONE);
        }

        if(list.get(position).getCorreo().equals(email)){
            holder.ReportButton.setVisibility(View.GONE);
            holder.SaveButton.setVisibility(View.GONE);
        }
        else{
            holder.ReportButton.setVisibility(View.VISIBLE);
            holder.SaveButton.setVisibility(View.VISIBLE);
        }

        holder.ReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                DialogReportPublication dialogReportPublication = new DialogReportPublication();

                Bundle arguments = new Bundle();
                arguments.putString("Usuario", list.get(position).getNombre());
                arguments.putString("Categoria", list.get(position).getNombreCategoria());
                arguments.putString("Contenido", list.get(position).getContenido());
                arguments.putInt("ID", list.get(position).getID_Publicacion());
                arguments.putInt("ID_Usuaria",list.get(position).getID_Usuaria());

                dialogReportPublication.setArguments(arguments);
                dialogReportPublication.setListener(listenerReportPublication);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, dialogReportPublication).addToBackStack(null).commit();

                /*
                Toast.makeText(context.getApplicationContext(), "Reportar publicaciÃ³n", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context.getApplicationContext(), ReportPublicationActivity.class);
                intent.putExtra("Usuario", list.get(position).getNombre());
                intent.putExtra("Categoria", list.get(position).getNombreCategoria());
                intent.putExtra("Contenido", list.get(position).getContenido());
                intent.putExtra("ID", list.get(position).getID_Publicacion());
                intent.putExtra("ID_Usuaria", list.get(position).getID_Usuaria());
                context.startActivity(intent);
                */
            }
        });

        holder.SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context.getApplicationContext(), "Guardando", Toast.LENGTH_SHORT).show();
                SavePublication(list.get(position).getID_Publicacion(), list.get(position).getID_Usuaria());
            }
        });

        holder.Comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String CommentInput = holder.Comment.getText().toString();
                if (CommentInput.isEmpty())
                {
                    holder.SendComment.setVisibility(View.INVISIBLE);
                    //Toast.makeText(context.getApplicationContext(), String.valueOf(position) + " " + list.size(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    holder.SendComment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.SendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCredentialData();
                //Toast.makeText(context.getApplicationContext(), "Comentando", Toast.LENGTH_SHORT).show();
                //Toast.makeText(context.getApplicationContext(), String.valueOf(position) + " " + list.size(), Toast.LENGTH_SHORT).show();
                //holder.Category.setText(holder.Comment.getText().toString());
                obtenerSanciones("https://seguridadmujer.com/app_movil/Community/ObtenerSancionesComentario.php?email="+email, holder, position);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context.getApplicationContext(), String.valueOf(position) + " " + list.size(), Toast.LENGTH_SHORT).show();

                //Toast.makeText(context.getApplicationContext(), "Mostrar comentarios", Toast.LENGTH_SHORT).show();

                /*
                    Intent intent = new Intent(context.getApplicationContext(), CommentListActivity.class);
                    intent.putExtra("Usuario", list.get(position).getNombre());
                    intent.putExtra("Categoria", list.get(position).getNombreCategoria());
                    intent.putExtra("Contenido", list.get(position).getContenido());
                    intent.putExtra("ID", list.get(position).getID_Publicacion());
                    //intent.putExtra("ID_Usuaria", list.get(position).getID_Usuaria());
                    context.startActivity(intent);

                    //ShowCommentDialog();
                */

                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                DialogShowCommunityPublication dialogShowCommunityPublication = new DialogShowCommunityPublication();

                Bundle arguments = new Bundle();
                arguments.putInt("ID", list.get(position).getID_Publicacion());
                arguments.putInt("ID_Usuaria",list.get(position).getID_Usuaria());
                arguments.putInt("ID_Cateegoria",list.get(position).getCategoria());
                arguments.putString("Contenido", list.get(position).getContenido());
                arguments.putString("Fecha", list.get(position).getFechaPublicacion());
                arguments.putString("Nombre", list.get(position).getNombre());
                arguments.putString("NombreCategoria", list.get(position).getNombreCategoria());
                arguments.putString("RutaImagen", list.get(position).getRutaImagen());
                arguments.putString("Correo", list.get(position).getCorreo());
                arguments.putString("ApellidoPaterno", list.get(position).getApellidoPaterno());
                arguments.putString("ApellidoMaterno", list.get(position).getApellidoMaterno());

                dialogShowCommunityPublication.setArguments(arguments);
                dialogShowCommunityPublication.setListener(listenerShowCommunityPublication);
                //dialogNewHelpPostFragment.show(((MainActivity)context).getSupportFragmentManager(), "tag");

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, dialogShowCommunityPublication).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void dialogo(int position){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
        dialogo1.setTitle(R.string.delete_dialog_title);
        dialogo1.setMessage(R.string.delete_dialog_body);
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Toast.makeText(getApplicationContext(), "Aceptar", Toast.LENGTH_LONG).show();
                //Accepted = true;
                DeletePublication(list.get(position).getID_Publicacion(), position);
            }
        });
        dialogo1.setNegativeButton(R.string.dialog_no_accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Toast.makeText(getApplicationContext(), "Declinar", Toast.LENGTH_LONG).show();
                //Accepted = false;
            }
        });
        AlertDialog dialogo = dialogo1.create();
        dialogo.show();
    }

    @Override
    public Filter getFilter ()
    {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ListElement> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(FullList);
                filtrando = false;
            }
            else
            {
                filtrando = true;
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ListElement publication : FullList)
                {
                    String fullname = publication.getNombre() + " " + publication.getApellidoPaterno() + " " + publication.getApellidoMaterno();
                    if (String.valueOf(publication.getCategoria()).toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(publication);
                    }else if (publication.getContenido().toLowerCase().contains(filterPattern)){
                        if(!filterPattern.equals("1") && !filterPattern.equals("2") && !filterPattern.equals("3") && !filterPattern.equals("4") && !filterPattern.equals("5") && !filterPattern.equals("6")){
                            filteredList.add(publication);
                        }
                    }else if (fullname.toLowerCase().contains(filterPattern)){
                        if(!filterPattern.equals("1") && !filterPattern.equals("2") && !filterPattern.equals("3") && !filterPattern.equals("4") && !filterPattern.equals("5") && !filterPattern.equals("6")){
                            filteredList.add(publication);
                        }
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            list = new ArrayList<ListElement>();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    void DeletePublication (int PublicationID, int position)
    {
        String[] field = new String[1];
        field[0] = "ID_Publicacion";

        String[] data = new String[1];
        data[0] = String.valueOf(PublicationID);

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/EliminarPublicacion.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    //Toast.makeText(context.getApplicationContext(), String.valueOf(filtrando), Toast.LENGTH_LONG).show();
                    if(filtrando){
                        for(int i =0; i<FullList.size(); i++){
                            if(FullList.get(i).getID_Publicacion() == list.get(position).getID_Publicacion()){
                                FullList.remove(i);
                                list.remove(position);
                                break;
                            }
                        }
                    }
                    else{
                        list.remove(position);
                        FullList = list;
                    }
                    notifyDataSetChanged();
                }
                else{
                    Toast.makeText(context.getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void SendComment (String Comment, int PublicationID, int UserID)
    {
        getCredentialData();
        String[] field = new String[3];
        field[0] = "Contenido";
        field[1] = "ID_Publicacion";
        field[2] = "ID_Usuaria";

        String[] data = new String[3];
        data[0] = Comment;
        data[1] = String.valueOf(PublicationID);
        data[2] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/GuardarComentarioPublicacion.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context.getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void SavePublication (int PublicationID, int UserID)
    {
        getCredentialData();
        String[] field = new String[2];
        field[0] = "ID_Publicacion";
        field[1] = "ID_Usuaria";

        String[] data = new String[2];
        data[0] = String.valueOf(PublicationID);
        data[1] = email;

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Community/GuardarPublicacionGuardada.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context.getApplicationContext(), result + " Favor de intentarlo nuevamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void ShowCommentDialog ()
    {
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //(AppCompatActivity (context)).getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();

        //DialogFragment dialog = (DialogFragment) DialogFragment.instantiate(context.getApplicationContext(), "Comment");
        //dialog.show(DialogNewPostFragment.getFragmentManager(), "Comentario");

        //DialogNewPostFragment dialogNewPostFragment = new DialogNewPostFragment();
        //dialogNewPostFragment.show(dialogNewPostFragment.getFragmentManager(), "Comment");


    }

    public void getCredentialData()
    {
        SharedPreferences preferences = context.getSharedPreferences("Credencials",context.MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public class PublicationViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username;
        TextView Category;
        Button Delete;
        ImageView Profile;
        TextView Content;
        Button ReportButton;
        Button SaveButton;
        EditText Comment;
        Button SendComment;
        ListElement Data;

        public PublicationViewHolder(View itemView)
        {
            super(itemView);
            Username = itemView.findViewById(R.id.user_name_comunity);
            Category = itemView.findViewById(R.id.post_category_label_comunity);
            Delete = itemView.findViewById(R.id.Delete_Button_Community);
            Profile = itemView.findViewById(R.id.profilepic_publication);
            Content = itemView.findViewById(R.id.post_content_label_comunity);
            ReportButton = itemView.findViewById(R.id.button_report_post_comunity);
            SaveButton = itemView.findViewById(R.id.button_save_post_comunity);
            Comment = itemView.findViewById(R.id.Add_Comment_Edit_Text);
            SendComment = itemView.findViewById(R.id.Add_Comment_Send_Button);
        }

        public void bind (ListElement communityPublication)
        {
            Data = communityPublication;
        }
    }

    //Metodo que valida si hay un baneo de cuenta:
    private void obtenerSanciones(String URL, PublicationViewHolder holder, int position){

        //Vacia la lista:
        if(!mListaSanciones.isEmpty()){
            mListaSanciones.clear();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);

                        Sancion sancion = new Sancion();
                        sancion.setID(jsonObject.getInt("ID_Baneo"));
                        sancion.setID_Usuaria(jsonObject.getInt("ID_Usuaria"));
                        sancion.setDuracion(jsonObject.getInt("Duracion"));
                        sancion.setFechaInicio(jsonObject.getString("FechaInicio"));
                        sancion.setFechaFin(jsonObject.getString("FechaFin"));
                        sancion.setTipoSancion(jsonObject.getString("Tipo"));

                        mListaSanciones.add(sancion);
                    }
                    catch (JSONException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                if(mListaSanciones.isEmpty()){
                    SendComment(holder.Comment.getText().toString(), list.get(position).getID_Publicacion(), list.get(position).getID_Usuaria());
                    holder.Comment.setText("");
                }
                else{
                    //Toast.makeText(getApplicationContext(), "Mamaste", Toast.LENGTH_SHORT).show();

                    String mensajeSancion = "";
                    ArrayList<Integer> listaDuraciones = new ArrayList<Integer>();

                    for(int i=0; i<mListaSanciones.size(); i++){
                        listaDuraciones.add(mListaSanciones.get(i).getDuracion());
                    }

                    for(int i=0; i<mListaSanciones.size(); i++){
                        if(mListaSanciones.get(i).getDuracion() == Collections.max(listaDuraciones)){
                            if(mListaSanciones.get(i).getTipoSancion().equals("Bloquear interaccion completa en modulo de Comunidad")){
                                if(mListaSanciones.get(i).getDuracion() == 876000){
                                    mensajeSancion = "Lo sentimos, la interaccion con el modulo de comunidad se le ha sido bloqueada de forma permanente.";
                                }
                                else{
                                    mensajeSancion = "Lo sentimos, la interaccion con el modulo de comunidad se le ha sido bloqueada, podra volver a acceder el dia "+mListaSanciones.get(i).getFechaFin();
                                }
                            }
                            else if(mListaSanciones.get(i).getTipoSancion().equals("Bloquear funcion de comentario")){
                                if(mListaSanciones.get(i).getDuracion() == 876000){
                                    mensajeSancion = "Lo sentimos, la funcion para realizar comentarios se le ha sido bloqueada de forma permanente.";
                                }
                                else{
                                    mensajeSancion = "Lo sentimos, la funcion para realizar comentarios se le ha sido bloqueada, podra volver a acceder el dia "+mListaSanciones.get(i).getFechaFin();
                                }
                            }

                        }
                    }

                    Intent intent = new Intent(context, SanctionAlert.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mensaje", mensajeSancion);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        );
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }


}