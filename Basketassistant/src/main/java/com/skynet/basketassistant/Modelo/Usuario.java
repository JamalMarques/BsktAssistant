package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 09/04/14.
 */
public class Usuario {

    private int Id;
    private String Nombre;
    private String Pass;
    private String Correo;

    public Usuario(int id,String nom,String pas,String cor){
        Id = id;
        Nombre = nom;
        Pass = pas;
        Correo = cor;
    }


    public int getId() {
        return Id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }
}
