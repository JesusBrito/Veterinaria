package com.example.jesus.veterinaria;



public class ClienteModelo {

    private String name;
    private String id;
    //private Drawable image;

    public ClienteModelo() {
        super();
    }

    public ClienteModelo(String id, String name) {
        super();
        this.name = name;
        //this.image = image;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
