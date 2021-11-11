package com.example.proyectoseguridadmujer;

import android.os.Parcel;
import android.os.Parcelable;

public class DefenseTechniques implements Parcelable {

    int ID_PublicacionDefensa;
    String Titulo;
    String Seccion;
    String Contenido;
    String RutaImagenPresentacion;
    String RutaVideo;

    public DefenseTechniques(int idTecnica, String titulo, String seccion, String comentario, String imagen, String video) {
        ID_PublicacionDefensa = idTecnica;
        Titulo = titulo;
        Seccion = seccion;
        Contenido = comentario;
        RutaImagenPresentacion = imagen;
        RutaVideo = video;
    }

    protected DefenseTechniques(Parcel in) {
        ID_PublicacionDefensa = in.readInt();
        Titulo = in.readString();
        Seccion = in.readString();
        Contenido = in.readString();
        RutaImagenPresentacion = in.readString();
        RutaVideo = in.readString();
    }

    public static final Creator<DefenseTechniques> CREATOR = new Creator<DefenseTechniques>() {
        @Override
        public DefenseTechniques createFromParcel(Parcel in) {
            return new DefenseTechniques(in);
        }

        @Override
        public DefenseTechniques[] newArray(int size) {
            return new DefenseTechniques[size];
        }
    };

    public int getID_PublicacionDefensa() {
        return ID_PublicacionDefensa;
    }

    public void setID_PublicacionDefensa(int ID_PublicacionDefensa) {
        this.ID_PublicacionDefensa = ID_PublicacionDefensa;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getSeccion() {
        return Seccion;
    }

    public void setSeccion(String seccion) {
        Seccion = seccion;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public String getRutaImagenPresentacion() {
        return RutaImagenPresentacion;
    }

    public void setRutaImagenPresentacion(String rutaImagenPresentacion) {
        RutaImagenPresentacion = rutaImagenPresentacion;
    }

    public String getRutaVideo() {
        return RutaVideo;
    }

    public void setRutaVideo(String rutaVideo) {
        RutaVideo = rutaVideo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID_PublicacionDefensa);
        dest.writeString(Titulo);
        dest.writeString(Seccion);
        dest.writeString(Contenido);
        dest.writeString(RutaImagenPresentacion);
        dest.writeString(RutaVideo);
    }
}