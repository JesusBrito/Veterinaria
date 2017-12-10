package com.example.jesus.veterinaria;



public class PersonaModelo {

    private String name;
    private String id;

    public PersonaModelo() {
        super();
    }

    public PersonaModelo(String id, String name) {
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
