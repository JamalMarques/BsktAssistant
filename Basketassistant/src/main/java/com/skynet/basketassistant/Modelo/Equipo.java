package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 06/04/14.
 */
public class Equipo {

    private int Id;
    private String Nombre;
    private int Ciudad_id;
    private int User_id;
    private String Categoria;

    public Equipo(int id,String nom,int ciu_id,int us_id,String cate){
        Id = id;
        Nombre = nom;
        Ciudad_id = ciu_id;
        User_id = us_id;
        setCategoria(cate);
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

    public int getCiudad_id() {
        return Ciudad_id;
    }

    public void setCiudad_id(int ciudad_id) {
        Ciudad_id = ciudad_id;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public String getCategoria() { return Categoria; }

    public void setCategoria(String categoria) { Categoria = categoria; }
}
