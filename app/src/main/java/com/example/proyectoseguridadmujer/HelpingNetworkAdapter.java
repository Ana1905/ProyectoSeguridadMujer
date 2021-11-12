package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Dialogs.DialogShowHelpPublication;
import httpurlconnection.PutData;

public class HelpingNetworkAdapter extends RecyclerView.Adapter<HelpingNetworkAdapter.HelpPublicationViewHolder> implements Filterable {

    private Context context;
    private ArrayList<HelpingNetworkPublication> list = new ArrayList<>();
    private ArrayList<HelpingNetworkPublication> FullList = new ArrayList<>();
    private String email;
    private boolean filtrando = false, Accepted = false;


    public HelpingNetworkAdapter(Context context, ArrayList<HelpingNetworkPublication> itemList)
    {
        this.context = context;
        this.list = itemList;
        FullList = list;
        filtrando = false;
    }

    @NonNull
    @NotNull
    @Override
    public HelpPublicationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.help_post_row, null);
        return new HelpPublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HelpPublicationViewHolder holder, int position) {
        holder.Username.setText(list.get(position).getNombre());
        holder.Category.setText(list.get(position).getNombreCategoria());
        holder.Content.setText(list.get(position).getContenido());
        if(list.get(position).getRutaImagen() != null && !list.get(position).getRutaImagen().isEmpty()){
            Glide.with(context).load(list.get(position).getRutaImagen()).into(holder.Profile);
        }
        else{
            Glide.with(context).load("http://seguridadmujer.com/web/Resources/avatar_woman_people_girl_glasses_icon_159125.png").into(holder.Profile);
        }

        holder.bind(list.get(position));

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context.getApplicationContext(), "Borrando", Toast.LENGTH_SHORT).show();

                /*
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                ConfirmEreaseDialog confirmEreaseDialog = new ConfirmEreaseDialog();

                Bundle arguments = new Bundle();
                arguments.putInt("ID", list.get(position).getID_Publicacion());
                arguments.putInt("Position", position);
                arguments.putString("Link", "https://seguridadmujer.com/app_movil/HelpingNetwork/EliminarPublicacion.php");

                confirmEreaseDialog.setArguments(arguments);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, confirmEreaseDialog).addToBackStack(null).commit();

                //DeletePublication(list.get(position).getID_Publicacion(), position);
                */
                //openDialog();
                dialogo(position);

                /*
                if (Accepted)
                {
                    DeletePublication(list.get(position).getID_Publicacion(), position);
                }

                 */
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
                //Toast.makeText(context.getApplicationContext(), "Comentando", Toast.LENGTH_SHORT).show();
                //Toast.makeText(context.getApplicationContext(), String.valueOf(position) + " " + list.size(), Toast.LENGTH_SHORT).show();
                //holder.Category.setText(holder.Comment.getText().toString());
                SendComment(holder.Comment.getText().toString(), list.get(position).getID_Publicacion(), list.get(position).getID_Usuaria());
                holder.Comment.setText("");
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
                DialogShowHelpPublication dialogShowHelpPublication = new DialogShowHelpPublication();

                Bundle arguments = new Bundle();
                arguments.putInt("ID", list.get(position).getID_Publicacion());
                arguments.putInt("ID_Usuaria",list.get(position).getID_Usuaria());
                arguments.putInt("ID_Cateegoria",list.get(position).getID_Categoria());
                arguments.putString("Contenido", list.get(position).getContenido());
                arguments.putString("Fecha", list.get(position).getFecha());
                arguments.putInt("Respondida",list.get(position).getRespondida());
                arguments.putString("Nombre", list.get(position).getNombre());
                arguments.putString("NombreCategoria", list.get(position).getNombreCategoria());
                arguments.putString("RutaImagen", list.get(position).getRutaImagen());

                dialogShowHelpPublication.setArguments(arguments);
                //dialogNewHelpPostFragment.show(((MainActivity)context).getSupportFragmentManager(), "tag");

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(android.R.id.content, dialogShowHelpPublication).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void dialogo(int position){
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
        dialogo1.setTitle("Confirmar borrado");
        dialogo1.setMessage("¿Seguro que quieres borrar esta publicación?");
        dialogo1.setCancelable(false);

        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                //Toast.makeText(getApplicationContext(), "Aceptar", Toast.LENGTH_LONG).show();
                //Accepted = true;
                DeletePublication(list.get(position).getID_Publicacion(), position);
            }
        });
        dialogo1.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
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
            List<HelpingNetworkPublication> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(FullList);
                filtrando = false;
            }
            else
            {
                filtrando = true;
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HelpingNetworkPublication publication : FullList)
                {
                    if (String.valueOf(publication.getID_Categoria()).toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(publication);
                    }else if (publication.getContenido().toLowerCase().contains(filterPattern)){
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
            list = new ArrayList<HelpingNetworkPublication>();
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

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/HelpingNetwork/EliminarPublicacion.php", "POST", field, data);
        if(putData.startPut()){
            if(putData.onComplete()){
                String result = putData.getResult();
                //INSERT exitoso:
                if(result.equals("Success")) {
                    //Toast.makeText(context.getApplicationContext(), result, Toast.LENGTH_LONG).show();
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

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/HelpingNetwork/GuardarComentarioRedDeApoyo.php", "POST", field, data);
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

    public void getCredentialData()
    {
        SharedPreferences preferences = context.getSharedPreferences("Credencials",context.MODE_PRIVATE);
        email = preferences.getString("email", "");
    }

    public class HelpPublicationViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username;
        TextView Category;
        Button Delete;
        ImageView Profile;
        TextView Content;
        EditText Comment;
        Button SendComment;
        HelpingNetworkPublication Data;

        public HelpPublicationViewHolder (View itemView)
        {
            super(itemView);
            Username = itemView.findViewById(R.id.user_name_helping_network);
            Category = itemView.findViewById(R.id.post_category_label_helping_network);
            Delete = itemView.findViewById(R.id.Delete_Button_HN);
            Profile = itemView.findViewById(R.id.user_profilepic_helping_network);
            Content = itemView.findViewById(R.id.post_content_label_helping_network);
            Comment = itemView.findViewById(R.id.Add_Comment_Edit_Text_helping_network);
            SendComment = itemView.findViewById(R.id.Add_Comment_Send_Button_helping_network);
        }

        public void bind (HelpingNetworkPublication listElement)
        {
            Data = listElement;
        }
    }
}