package com.example.jesus.veterinaria;

/**
 * Created by jesus on 9/12/17.
 */

public class MascotaModelo {

    private String name;
    private String id;
    private String rfc;

    public MascotaModelo() {
        super();
    }

    public MascotaModelo(String id, String name, String rfc) {
        super();
        this.name = name;
        this.id = id;
        this.rfc=rfc;
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
    public String getRfc() {
        return rfc;
    }
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
}

