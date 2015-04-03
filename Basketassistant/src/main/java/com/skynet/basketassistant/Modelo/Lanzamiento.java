package com.skynet.basketassistant.Modelo;

import com.skynet.basketassistant.Otros.Constants;

/**
 * Created by jamal on 06/04/14.
 */
public class Lanzamiento {

    private int Id;
    private int Efectivo;
    private String TipoLanzamiento;
    private int Valor;
    private int partido_id;
    private int jugador_id;
    private int quarter_number;

    public Lanzamiento(int id,int efe,String tiplanz,int val,int part_id,int jug_id, int quarter){
        Id = id;
        Efectivo = efe;
        TipoLanzamiento = tiplanz;
        Valor = val;
        partido_id = part_id;
        jugador_id = jug_id;
        setQuarter_number(quarter);
    }

    public Lanzamiento(int efe,String tiplanz,int val,int part_id,int jug_id, int quarter){
        Id = 0;
        Efectivo = efe;
        TipoLanzamiento = tiplanz;
        Valor = val;
        partido_id = part_id;
        jugador_id = jug_id;
        setQuarter_number(quarter);
    }


    public int getId() {
        return Id;
    }

    public int getEfectivo() {
        return Efectivo;
    }

    public void setEfectivo(int efectivo) {
        Efectivo = efectivo;
    }

    public String getTipoLanzamiento() {
        return TipoLanzamiento;
    }

    public void setTipoLanzamiento(String tipoLanzamiento) {
        TipoLanzamiento = tipoLanzamiento;
    }

    public int getValor() {
        return Valor;
    }

    public void setValor(int valor) {
        Valor = valor;
    }

    public int getPartido_id() {
        return partido_id;
    }

    public void setPartido_id(int partido_id) {
        this.partido_id = partido_id;
    }

    public int getJugador_id() {
        return jugador_id;
    }

    public void setJugador_id(int jugador_id) {
        this.jugador_id = jugador_id;
    }

    public int getQuarter_number() {
        return quarter_number;
    }

    public void setQuarter_number(int quarter_number) {
        if (quarter_number >= Constants.MAX_NUMBER_OF_QUARTERS)
            this.quarter_number = Constants.MAX_NUMBER_OF_QUARTERS;
        else
            this.quarter_number = quarter_number;
    }
}
