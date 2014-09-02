package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 02/04/14.
 */
public class Robo {

    private int Id;
    private int Jugador_id;
    private int Partido_id;

    //private DBRobos dbrobos;

    public Robo(int id,int jugid,int partid){
        Id = id;
        Jugador_id = jugid;
        Partido_id = partid;
    }

    public int getId() {
        return Id;
    }

    public int getJugador_id() {
        return Jugador_id;
    }

    public void setJugador_id(int jugador_id) {
        Jugador_id = jugador_id;
    }

    public int getPartido_id() {
        return Partido_id;
    }

    public void setPartido_id(int partido_id) {
        Partido_id = partido_id;
    }
}
