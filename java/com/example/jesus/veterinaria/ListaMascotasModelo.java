package com.example.jesus.veterinaria;

/**
 * Created by jesus on 11/12/17.
 */

public class ListaMascotasModelo {
    String Nombre;
    String Raza;
    String Color;
    String Fecha;
    String Pk;
    public ListaMascotasModelo(){super();}
    public ListaMascotasModelo(String nombre, String raza, String color, String fecha,String pk) {
        super();
        Nombre = nombre;
        Raza = raza;
        Color = color;
        Fecha = fecha;
        Pk = pk;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getRaza() {
        return Raza;
    }

    public void setRaza(String raza) {
        Raza = raza;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getPk() {
        return Pk;
    }

    public void setPk(String pk) {
        Pk = pk;
    }
}
