package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 06/04/14.
 */
public class Falta {

    private int Id;
    private int Partido_id;
    private int Jugador_id;
    private String tipo;

    public Falta(int id,int par_id,int jug_id,String tip){
        Id = id;
        Partido_id = par_id;
        Jugador_id = jug_id;
        tipo = tip;
    }


    public int getId() {
        return Id;
    }

    public int getPartido_id() {
        return Partido_id;
    }

    public void setPartido_id(int partido_id) {
        Partido_id = partido_id;
    }

    public int getJugador_id() {
        return Jugador_id;
    }

    public void setJugador_id(int jugador_id) {
        Jugador_id = jugador_id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
