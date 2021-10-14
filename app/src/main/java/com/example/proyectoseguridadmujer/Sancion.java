package com.example.proyectoseguridadmujer;

public class Sancion {
    private int ID;
    private int ID_Usuaria;
    private int Duracion;
    private String FechaInicio;
    private String FechaFin;
    private int ID_TipoSancion;
    private String TipoSancion;
    private int Estado;
    private String MensajeSancion;

    //Constructor vacio:
    public Sancion(){
    }

    //Getters y Setters:
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_Usuaria() {
        return ID_Usuaria;
    }

    public void setID_Usuaria(int ID_Usuaria) {
        this.ID_Usuaria = ID_Usuaria;
    }

    public int getDuracion() {
        return Duracion;
    }

    public void setDuracion(int duracion) {
        Duracion = duracion;
    }

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(String fechaFin) {
        FechaFin = fechaFin;
    }

    public int getID_TipoSancion() {
        return ID_TipoSancion;
    }

    public void setID_TipoSancion(int ID_TipoSancion) {
        this.ID_TipoSancion = ID_TipoSancion;
    }

    public String getTipoSancion() {
        return TipoSancion;
    }

    public void setTipoSancion(String tipoSancion) {
        TipoSancion = tipoSancion;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public String getMensajeSancion() {
        return MensajeSancion;
    }

    public void setMensajeSancion(String mensajeSancion) {
        MensajeSancion = mensajeSancion;
    }
}
