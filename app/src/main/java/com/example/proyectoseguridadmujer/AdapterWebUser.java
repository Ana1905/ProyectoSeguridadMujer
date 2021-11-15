package com.example.proyectoseguridadmujer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import httpurlconnection.PutData;

public class AdapterWebUser extends RecyclerView.Adapter<AdapterWebUser.ViewHolderWebUser> {

    ArrayList<WebUser> mListaUsuarios;

    Context context;

    public AdapterWebUser(ArrayList<WebUser> lista) {
        this.mListaUsuarios = lista;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderWebUser onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.web_user_list,parent,false);

        context = parent.getContext();

        return new ViewHolderWebUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderWebUser holder, int position) {
        holder.Nombre.setText(mListaUsuarios.get(position).getNombre());
        holder.Correo.setText(mListaUsuarios.get(position).getCorreo());

        //Imagen:
        if(!mListaUsuarios.get(position).getRutaImagen().isEmpty()){
            Glide.with(context).load(mListaUsuarios.get(position).getRutaImagen()).into(holder.Imagen);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), String.valueOf(mListaUsuarios.get(position).getID()), Toast.LENGTH_SHORT).show();

                /*
                Intent intent = new Intent(v.getContext(), EditContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ID", ListContacts.get(position).ID_Contacto);
                bundle.putString("Nombre", ListContacts.get(position).Nombre);
                bundle.putString("Numero", ListContacts.get(position).Numero);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                 */
                eliminarUsuarioWeb(mListaUsuarios.get(position).getID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListaUsuarios.size();
    }

    public class ViewHolderWebUser extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView Imagen;
        TextView Nombre;
        TextView Correo;

        public ViewHolderWebUser(@NonNull @NotNull View itemView) {
            super(itemView);

            //Wiring Up:
            Imagen = (ImageView) itemView.findViewById(R.id.imagen_usuario_web);
            Nombre = (TextView) itemView.findViewById(R.id.nombre_usuario_web);
            Correo = (TextView)  itemView.findViewById(R.id.correo_usuario_web);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private void eliminarUsuarioWeb(int id) {
        String[] field = new String[1];
        field[0] = "id";

        //Creating array for data
        String[] data = new String[1];
        data[0] = String.valueOf(id);

        PutData putData = new PutData("https://seguridadmujer.com/app_movil/Profile/desconectarUsuarioWeb.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                if (result.equals("Success")) {
                    Toast.makeText(context, "Se ha desvinculado al usuario web", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
