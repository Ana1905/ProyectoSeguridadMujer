package com.example.proyectoseguridadmujer;

public class BodyDefenseTechniques {

    int ID_PublicacionDefensa;
    String Titulo;
    String Seccion;
    String Contenido;
    int Imagen;
    String Video;

    public BodyDefenseTechniques(int idTecnica, String titulo, String seccion, String comentario, int imagen, String video) {
        ID_PublicacionDefensa = idTecnica;
        Titulo = titulo;
        Seccion = seccion;
        Contenido = comentario;
        Imagen = imagen;
        Video = video;
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

    public int getImagen() {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }
}
