package com.example.proyectoseguridadmujer;

import android.os.Parcel;
import android.os.Parcelable;

public class Institutions implements Parcelable {

    int ID_Institucion;
    String Nombre;
    String Area;
    String RutaImagenPresentacion;
    String Descripcion;
    String Telefono;
    String PaginaWeb;
    double Latitud;
    double Longitud;
    int CategoriaInstitucion;
    String NombreCategoria;

    public Institutions(){

    }

    protected Institutions(Parcel in) {
        ID_Institucion = in.readInt();
        Nombre = in.readString();
        Area = in.readString();
        RutaImagenPresentacion = in.readString();
        Descripcion = in.readString();
        Telefono = in.readString();
        PaginaWeb = in.readString();
        Latitud = in.readDouble();
        Longitud = in.readDouble();
        CategoriaInstitucion = in.readInt();
        NombreCategoria = in.readString();
    }

    public static final Creator<Institutions> CREATOR = new Creator<Institutions>() {
        @Override
        public Institutions createFromParcel(Parcel in) {
            return new Institutions(in);
        }

        @Override
        public Institutions[] newArray(int size) {
            return new Institutions[size];
        }
    };

    public int getID_Institucion() {
        return ID_Institucion;
    }

    public void setID_Institucion(int ID_Institucion) {
        this.ID_Institucion = ID_Institucion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getRutaImagenPresentacion() {
        return RutaImagenPresentacion;
    }

    public void setRutaImagenPresentacion(String rutaImagenPresentacion) {
        RutaImagenPresentacion = rutaImagenPresentacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getPaginaWeb() {
        return PaginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        PaginaWeb = paginaWeb;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public int getCategoriaInstitucion() {
        return CategoriaInstitucion;
    }

    public void setCategoriaInstitucion(int categoriaInstitucion) {
        CategoriaInstitucion = categoriaInstitucion;
    }

    public String getNombreCategoria() {
        return NombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        NombreCategoria = nombreCategoria;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID_Institucion);
        dest.writeString(Nombre);
        dest.writeString(Area);
        dest.writeString(RutaImagenPresentacion);
        dest.writeString(Descripcion);
        dest.writeString(Telefono);
        dest.writeString(PaginaWeb);
        dest.writeDouble(Latitud);
        dest.writeDouble(Longitud);
        dest.writeInt(CategoriaInstitucion);
        dest.writeString(NombreCategoria);
    }
}