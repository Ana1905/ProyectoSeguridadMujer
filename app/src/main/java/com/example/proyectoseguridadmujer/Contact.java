package com.example.proyectoseguridadmujer;

public class Contact {
    int ID_Contacto;
    String Nombre;
    String Numero;

    public Contact(){

    }

    public int getID_Contacto() {
        return ID_Contacto;
    }

    public void setID_Contacto(int ID_Contacto) {
        this.ID_Contacto = ID_Contacto;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public Contact(String nombre, String numero) {
        Nombre = nombre;
        Numero = numero;
    }
}
