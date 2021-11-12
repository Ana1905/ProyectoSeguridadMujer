package com.example.proyectoseguridadmujer;

public class Tip {

    private int ID_Tip;
    private String Titulo;
    private String Contenido;
    private String RutaImagen1;
    private String RutaImagen2;
    private int Tipo;

    public Tip(){

    }

    public int getID_Tip() {
        return ID_Tip;
    }

    public void setID_Tip(int ID_Tip) {
        this.ID_Tip = ID_Tip;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public String getRutaImagen1() {
        return RutaImagen1;
    }

    public void setRutaImagen1(String rutaImagen1) {
        RutaImagen1 = rutaImagen1;
    }

    public String getRutaImagen2() {
        return RutaImagen2;
    }

    public void setRutaImagen2(String rutaImagen2) {
        RutaImagen2 = rutaImagen2;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }
}

