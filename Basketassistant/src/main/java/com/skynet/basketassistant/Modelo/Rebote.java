package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 02/04/14.
 */
public class Rebote {

    private int Id;
    private int Jugador_id;
    private int Partido_id;
    private String Tiporeb;

    public Rebote(int id,int jugid,int partd,String tipore){
        Id = id;
        Jugador_id = jugid;
        Partido_id = partd;
        Tiporeb = tipore;
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

    public String getTiporeb() {
        return Tiporeb;
    }

    public void setTiporeb(String tiporeb) {
        Tiporeb = tiporeb;
    }
}
