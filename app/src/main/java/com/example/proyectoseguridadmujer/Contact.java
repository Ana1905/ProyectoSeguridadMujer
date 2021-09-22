package com.example.proyectoseguridadmujer;

public class Contact {
    String Nombre;
    String Numero;

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
