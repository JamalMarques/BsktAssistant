package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 06/04/14.
 */
public class Tapon {

    private int Id;
    private int Jugador_id;
    private int Partido_id;

    public Tapon(int id,int jug_id,int par_id){
        Id = id;
        Jugador_id = jug_id;
        Partido_id = par_id;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
