package com.example.proyectoseguridadmujer;

import android.os.Parcel;
import android.os.Parcelable;

public class ListElement implements Parcelable {

    int ID_Publicacion;
    String Contenido;
    int Monitoreada;
    String NivelAlerta;
    String ComentarioMonitoreo;
    String FechaPublicacion;
    int Categoria;
    int ID_Usuaria;
    String Nombre;
    String NombreCategoria;
    String RutaImagen;
    String Correo;
    String ApellidoPaterno;
    String ApellidoMaterno;

    public ListElement(){

    }

    public ListElement(Parcel in) {
        ID_Publicacion = in.readInt();
        Contenido = in.readString();
        Monitoreada = in.readInt();
        NivelAlerta = in.readString();
        ComentarioMonitoreo = in.readString();
        FechaPublicacion = in.readString();
        Categoria = in.readInt();
        ID_Usuaria = in.readInt();
        Nombre = in.readString();
        NombreCategoria = in.readString();
        RutaImagen = in.readString();
        Correo = in.readString();
        ApellidoPaterno = in.readString();
        ApellidoMaterno = in.readString();
    }

    public static final Creator<ListElement> CREATOR = new Creator<ListElement>() {
        @Override
        public ListElement createFromParcel(Parcel in) {
            return new ListElement(in);
        }

        @Override
        public ListElement[] newArray(int size) {
            return new ListElement[size];
        }
    };

    public int getID_Publicacion() {
        return ID_Publicacion;
    }

    public void setID_Publicacion(int ID_Publicacion) {
        this.ID_Publicacion = ID_Publicacion;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public int getMonitoreada() {
        return Monitoreada;
    }

    public void setMonitoreada(int monitoreada) {
        Monitoreada = monitoreada;
    }

    public String getNivelAlerta() {
        return NivelAlerta;
    }

    public void setNivelAlerta(String nivelAlerta) {
        NivelAlerta = nivelAlerta;
    }

    public String getComentarioMonitoreo() {
        return ComentarioMonitoreo;
    }

    public void setComentarioMonitoreo(String comentarioMonitoreo) {
        ComentarioMonitoreo = comentarioMonitoreo;
    }

    public String getFechaPublicacion() {
        return FechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        FechaPublicacion = fechaPublicacion;
    }

    public int getCategoria() {
        return Categoria;
    }

    public void setCategoria(int categoria) {
        Categoria = categoria;
    }

    public int getID_Usuaria() {
        return ID_Usuaria;
    }

    public void setID_Usuaria(int ID_Usuaria) {
        this.ID_Usuaria = ID_Usuaria;
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

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID_Publicacion);
        dest.writeString(Contenido);
        dest.writeInt(Monitoreada);
        dest.writeString(NivelAlerta);
        dest.writeString(ComentarioMonitoreo);
        dest.writeString(FechaPublicacion);
        dest.writeInt(Categoria);
        dest.writeInt(ID_Usuaria);
        dest.writeString(Nombre);
        dest.writeString(NombreCategoria);
        dest.writeString(RutaImagen);
        dest.writeString(Correo);
        dest.writeString(ApellidoPaterno);
        dest.writeString(ApellidoMaterno);
    }
}