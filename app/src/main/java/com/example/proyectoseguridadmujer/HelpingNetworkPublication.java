package com.example.proyectoseguridadmujer;

import android.os.Parcel;
import android.os.Parcelable;

public class HelpingNetworkPublication implements Parcelable {

    int ID_Publicacion;
    int ID_Usuaria;
    int ID_Categoria;
    String 	Contenido;
    String Fecha;
    int Respondida;
    String Nombre;
    String NombreCategoria;
    String RutaImagen;

    public HelpingNetworkPublication(){

    }
    public HelpingNetworkPublication(Parcel in) {
        ID_Publicacion = in.readInt();
        ID_Usuaria = in.readInt();
        ID_Categoria = in.readInt();
        Contenido = in.readString();
        Fecha = in.readString();
        Respondida = in.readInt();
        Nombre = in.readString();
        NombreCategoria = in.readString();
        RutaImagen = in.readString();
    }

    public static final Creator<HelpingNetworkPublication> CREATOR = new Creator<HelpingNetworkPublication>() {
        @Override
        public HelpingNetworkPublication createFromParcel(Parcel in) {
            return new HelpingNetworkPublication(in);
        }

        @Override
        public HelpingNetworkPublication[] newArray(int size) {
            return new HelpingNetworkPublication[size];
        }
    };

    public int getID_Publicacion() {
        return ID_Publicacion;
    }

    public void setID_Publicacion(int ID_Publicacion) {
        this.ID_Publicacion = ID_Publicacion;
    }

    public int getID_Usuaria() {
        return ID_Usuaria;
    }

    public void setID_Usuaria(int ID_Usuaria) {
        this.ID_Usuaria = ID_Usuaria;
    }

    public int getID_Categoria() {
        return ID_Categoria;
    }

    public void setID_Categoria(int ID_Categoria) {
        this.ID_Categoria = ID_Categoria;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public int getRespondida() {
        return Respondida;
    }

    public void setRespondida(int respondida) {
        Respondida = respondida;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }

    public String getRutaImagen() {
        return RutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        RutaImagen = rutaImagen;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID_Publicacion);
        dest.writeInt(ID_Usuaria);
        dest.writeInt(ID_Categoria);
        dest.writeString(Contenido);
        dest.writeString(Fecha);
        dest.writeInt(Respondida);
        dest.writeString(Nombre);
        dest.writeString(NombreCategoria);
        dest.writeString(RutaImagen);
    }
}