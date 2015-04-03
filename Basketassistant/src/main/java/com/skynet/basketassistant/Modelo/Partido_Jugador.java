package com.skynet.basketassistant.Modelo;

/**
 * Created by PROGRAMACION on 17/06/14.
 */
public class Partido_Jugador {

    private int Id;
    private int Partido_id;
    private int Jugador_id;

    public Partido_Jugador(int id,int p_id,int j_id){
        Id = id;
        Partido_id = p_id;
        Jugador_id = j_id;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
}
