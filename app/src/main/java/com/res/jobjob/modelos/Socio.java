package com.res.jobjob.modelos;

public class Socio {
    String id;
    String nombre;
    String correo;
    String oficio;
    String tel;

    public Socio(String id, String nombre, String correo, String oficio, String tel) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.oficio = oficio;
        this.tel = tel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
