package com.skynet.basketassistant.Modelo;

/**
 * Created by jamal on 06/04/14.
 */
public class Ciudad {

    private int Id;
    private String Ciudad;
    private String Provincia;

    public Ciudad(int id,String ci,String prov){
        Id = id;
        Ciudad = ci;
        Provincia = prov;
    }


    public int getId() {
        return Id;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }
}
