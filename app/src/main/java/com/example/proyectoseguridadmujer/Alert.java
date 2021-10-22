package com.example.proyectoseguridadmujer;

public class Alert {
    private int ID_Alerta;
    private int TipoAlerta;
    private String Mensaje;
    private String RutaImagen;
    private String TipoImagen;
    private int ID_Contacto;
    private String TelefonoContacto;

    public Alert(){

    }

    public int getID_Alerta() {
        return ID_Alerta;
    }

    public void setID_Alerta(int ID_Alerta) {
        this.ID_Alerta = ID_Alerta;
    }

    public int getTipoAlerta() {
        return TipoAlerta;
    }

    public void setTipoAlerta(int tipoAlerta) {
        TipoAlerta = tipoAlerta;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getRutaImagen() {
        return RutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        RutaImagen = rutaImagen;
    }

    public String getTipoImagen() {
        return TipoImagen;
    }

    public void setTipoImagen(String tipoImagen) {
        TipoImagen = tipoImagen;
    }

    public int getID_Contacto() {
        return ID_Contacto;
    }

    public void setID_Contacto(int ID_Contacto) {
        this.ID_Contacto = ID_Contacto;
    }

    public String getTelefonoContacto() {
        return TelefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        TelefonoContacto = telefonoContacto;
    }
}
