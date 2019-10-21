package com.example.ejerciciocontactoscarmelo;

import java.io.Serializable;

public class Contacto implements Serializable {

    protected String nombre;
    protected long id;
    protected String telefono;

    public String getNombre() {
        return nombre;
    }

    public long getId() {
        return id;
    }

    public String getTelefono() {
        return telefono;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Contacto() {
    }

    public Contacto(String nombre, long id, String telefono) {
        this.nombre = nombre;
        this.id = id;
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                ", telefono='" + telefono + '\'' +
                "}\n";
    }

    public String toCSV(){
        return id + "," + nombre + "," + telefono;
    }

}
