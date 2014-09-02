package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 06/04/14.
 */
public class Partido {

    private int Id;
    private String Fecha;
    private String Cancha;
    private int Puntos_E1;
    private int Puntos_E2;
    private int Equipo1_id;
    private String Equipo2_nom;
    private int Punt_q1_e1;
    private int Punt_q2_e1;
    private int Punt_q3_e1;
    private int Punt_q4_e1;
    private int Punt_q1_e2;
    private int Punt_q2_e2;
    private int Punt_q3_e2;
    private int Punt_q4_e2;
    private int Punt_ext_e1;
    private int Punt_ext_e2;


    public Partido(int id,String fech,String can,int p_e1,int p_e2,int e1_id,String e2_nom,
                   int pun_q1_e1,int pun_q2_e1,int pun_q3_e1,int pun_q4_e1,int pun_q1_e2,int pun_q2_e2,int pun_q3_e2,int pun_q4_e2,int pun_ext_e1,int pun_ext_e2){
        Id = id;
        Fecha = fech;
        Cancha = can;
        Puntos_E1 = p_e1;
        Puntos_E2 = p_e2;
        Equipo1_id = e1_id;
        Equipo2_nom = e2_nom;
        Punt_q1_e1 = pun_q1_e1;
        Punt_q2_e1 = pun_q2_e1;
        Punt_q3_e1 = pun_q3_e1;
        Punt_q4_e1 = pun_q4_e1;
        Punt_q1_e2 = pun_q1_e2;
        Punt_q2_e2 = pun_q2_e2;
        Punt_q3_e2 = pun_q3_e2;
        Punt_q4_e2 = pun_q4_e2;
        Punt_ext_e1 = pun_ext_e1;
        Punt_ext_e2 = pun_ext_e2;
    }


    public boolean Victoria(){

        if(Puntos_E1 > Puntos_E2)
            return  true;
        else
            return false;
    }


    public int getId() {
        return Id;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getCancha() {
        return Cancha;
    }

    public void setCancha(String cancha) {
        Cancha = cancha;
    }

    public int getPuntos_E1() {
        return Puntos_E1;
    }

    public void setPuntos_E1(int puntos_E1) {
        Puntos_E1 = puntos_E1;
    }

    public int getPuntos_E2() {
        return Puntos_E2;
    }

    public void setPuntos_E2(int puntos_E2) {
        Puntos_E2 = puntos_E2;
    }

    public int getEquipo1_id() {
        return Equipo1_id;
    }

    public void setEquipo1_id(int equipo1_id) {
        Equipo1_id = equipo1_id;
    }


    public int getPunt_q4_e1() {
        return Punt_q4_e1;
    }

    public void setPunt_q4_e1(int punt_q4_e1) {
        Punt_q4_e1 = punt_q4_e1;
    }

    public String getEquipo2_nom() {
        return Equipo2_nom;
    }

    public void setEquipo2_nom(String equipo2_nom) {
        Equipo2_nom = equipo2_nom;
    }

    public int getPunt_q1_e1() {
        return Punt_q1_e1;
    }

    public void setPunt_q1_e1(int punt_q1_e1) {
        Punt_q1_e1 = punt_q1_e1;
    }

    public int getPunt_q2_e1() {
        return Punt_q2_e1;
    }

    public void setPunt_q2_e1(int punt_q2_e1) {
        Punt_q2_e1 = punt_q2_e1;
    }

    public int getPunt_q3_e1() {
        return Punt_q3_e1;
    }

    public void setPunt_q3_e1(int punt_q3_e1) {
        Punt_q3_e1 = punt_q3_e1;
    }

    public int getPunt_q1_e2() {
        return Punt_q1_e2;
    }

    public void setPunt_q1_e2(int punt_q1_e2) {
        Punt_q1_e2 = punt_q1_e2;
    }

    public int getPunt_q2_e2() {
        return Punt_q2_e2;
    }

    public void setPunt_q2_e2(int punt_q2_e2) {
        Punt_q2_e2 = punt_q2_e2;
    }

    public int getPunt_q3_e2() {
        return Punt_q3_e2;
    }

    public void setPunt_q3_e2(int punt_q3_e2) {
        Punt_q3_e2 = punt_q3_e2;
    }

    public int getPunt_q4_e2() {
        return Punt_q4_e2;
    }

    public void setPunt_q4_e2(int punt_q4_e2) {
        Punt_q4_e2 = punt_q4_e2;
    }

    public int getPunt_ext_e1() {
        return Punt_ext_e1;
    }

    public void setPunt_ext_e1(int punt_ext_e1) {
        Punt_ext_e1 = punt_ext_e1;
    }

    public int getPunt_ext_e2() {
        return Punt_ext_e2;
    }

    public void setPunt_ext_e2(int punt_ext_e2) {
        Punt_ext_e2 = punt_ext_e2;
    }
}
