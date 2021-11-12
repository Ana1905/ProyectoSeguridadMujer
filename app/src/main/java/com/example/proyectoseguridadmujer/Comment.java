package com.example.proyectoseguridadmujer;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    int ID_Comentario;
    int ID_Publicacion;
    String Comentario;
    int ID_Usuaria;
    int ID_Especialista;
    String Fecha;
    String Nombre;
    String RutaImagen;

    public Comment () {

    }

    public Comment(Parcel in) {
        ID_Comentario = in.readInt();
        ID_Publicacion = in.readInt();
        Comentario = in.readString();
        ID_Usuaria = in.readInt();
        ID_Especialista = in.readInt();
        Fecha = in.readString();
        Nombre = in.readString();
        RutaImagen = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };


    public int getID_Comentario() {
        return ID_Comentario;
    }

    public void setID_Comentario(int ID_Comentario) {
        this.ID_Comentario = ID_Comentario;
    }

    public int getID_Publicacion() {
        return ID_Publicacion;
    }

    public void setID_Publicacion(int ID_Publicacion) {
        this.ID_Publicacion = ID_Publicacion;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public int getID_Usuaria() {
        return ID_Usuaria;
    }

    public void setID_Usuaria(int ID_Usuaria) {
        this.ID_Usuaria = ID_Usuaria;
    }

    public int getID_Especialista() {
        return ID_Especialista;
    }

    public void setID_Especialista(int ID_Especialista) {
        this.ID_Especialista = ID_Especialista;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getRutaImagen() {
        return RutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        RutaImagen = rutaImagen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID_Comentario);
        dest.writeInt(ID_Publicacion);
        dest.writeString(Comentario);
        dest.writeInt(ID_Usuaria);
        dest.writeInt(ID_Especialista);
        dest.writeString(Fecha);
        dest.writeString(Nombre);
        dest.writeString(RutaImagen);
    }
}
