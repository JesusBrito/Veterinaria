package com.example.jesus.veterinaria;

/**
 * Created by jesus on 9/12/17.
 */

public class ServicioModelo {
    private String name;
    private String id;
    private String precio;

    public ServicioModelo() {
        super();
    }

    public ServicioModelo(String id, String name, String precio) {
        this.name = name;
        this.id = id;
        this.precio = precio;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPrecio() {
        return precio;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
