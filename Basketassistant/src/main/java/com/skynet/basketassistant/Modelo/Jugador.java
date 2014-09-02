package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 06/04/14.
 */
public class Jugador {

    private int Id;
    private String Apellido;
    private String Nombre;
    private double Altura;
    private double Peso;
    private int Numero;
    private String Rol;
    private int Equipo_id;
    private String Imagen_url;

    public Jugador(int id,String ape,String nom,double alt,double pes,int num,String rol,int eq_id,String img_url){
        Id = id;
        Apellido = ape;
        Nombre = nom;
        Altura = alt;
        Peso = pes;
        Numero = num;
        Rol = rol;
        Equipo_id = eq_id;
        Imagen_url = img_url;
    }


    public int getId() {
        return Id;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public double getAltura() {
        return Altura;
    }

    public void setAltura(double altura) {
        Altura = altura;
    }

    public double getPeso() {
        return Peso;
    }

    public void setPeso(double peso) {
        Peso = peso;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int numero) {
        Numero = numero;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String rol) {
        Rol = rol;
    }

    public int getEquipo_id() {
        return Equipo_id;
    }

    public void setEquipo_id(int equipo_id) {
        Equipo_id = equipo_id;
    }

    public String getImagen_url() {
        return Imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        Imagen_url = imagen_url;
    }
}
