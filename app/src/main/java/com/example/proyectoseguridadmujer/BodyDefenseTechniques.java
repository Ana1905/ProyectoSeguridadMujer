package com.example.proyectoseguridadmujer;

public class BodyDefenseTechniques {

    String IdTecnica;
    String Titulo;
    String Seccion;
    String Comentario;
    int Imagen;
    String Video;

    public BodyDefenseTechniques(String idTecnica, String titulo, String seccion, String comentario, int imagen, String video) {
        IdTecnica = idTecnica;
        Titulo = titulo;
        Seccion = seccion;
        Comentario = comentario;
        Imagen = imagen;
        Video = video;
    }

    public String getIdTecnica() {
        return IdTecnica;
    }

    public void setIdTecnica(String idTecnica) {
        IdTecnica = idTecnica;
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

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
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
