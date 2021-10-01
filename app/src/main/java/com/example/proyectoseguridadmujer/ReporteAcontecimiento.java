package com.example.proyectoseguridadmujer;

public class ReporteAcontecimiento {
    public String NombreUsuaria;
    public String ApellidoPaternoUsuaria;
    public String ApellidoMaternoUsuaria;
    public int CategoriaReporte;
    public double Latitud;
    public double Longitud;
    public int Radio;
    public String Descripcion;
    public String FechaPublicacion;

    //Constructor vacio:
    public ReporteAcontecimiento(){
    }

    //Getters y Setters:
    public String getNombreUsuaria() {
        return NombreUsuaria;
    }

    public void setNombreUsuaria(String nombreUsuaria) {
        NombreUsuaria = nombreUsuaria;
    }

    public String getApellidoPaternoUsuaria() {
        return ApellidoPaternoUsuaria;
    }

    public void setApellidoPaternoUsuaria(String apellidoPaternoUsuaria) {
        ApellidoPaternoUsuaria = apellidoPaternoUsuaria;
    }

    public String getApellidoMaternoUsuaria() {
        return ApellidoMaternoUsuaria;
    }

    public void setApellidoMaternoUsuaria(String apellidoMaternoUsuaria) {
        ApellidoMaternoUsuaria = apellidoMaternoUsuaria;
    }

    public int getCategoriaReporte() {
        return CategoriaReporte;
    }

    public void setCategoriaReporte(int categoriaReporte) {
        CategoriaReporte = categoriaReporte;
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

    public int getRadio() {
        return Radio;
    }

    public void setRadio(int radio) {
        Radio = radio;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFechaPublicacion() {
        return FechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        FechaPublicacion = fechaPublicacion;
    }
}
