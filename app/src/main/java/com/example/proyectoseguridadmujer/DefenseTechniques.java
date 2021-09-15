package com.example.proyectoseguridadmujer;

public class DefenseTechniques {

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
}
