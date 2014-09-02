package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 06/04/14.
 */
public class Lanzamiento {

    private int Id;
    private int Efectivo;
    private int Posx;
    private int Posy;
    private String TipoLanzamiento;
    private int Valor;
    private int partido_id;
    private int jugador_id;

    public Lanzamiento(int id,int efe,int px,int py,String tiplanz,int val,int part_id,int jug_id){
        Id = id;
        Efectivo = efe;
        Posx = px;
        Posy = py;
        TipoLanzamiento = tiplanz;
        Valor = val;
        partido_id = part_id;
        jugador_id = jug_id;
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

    public int getPosx() {
        return Posx;
    }

    public void setPosx(int posx) {
        Posx = posx;
    }

    public int getPosy() {
        return Posy;
    }

    public void setPosy(int posy) {
        Posy = posy;
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
}
