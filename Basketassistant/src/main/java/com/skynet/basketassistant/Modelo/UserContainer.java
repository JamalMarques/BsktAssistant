package com.skynet.basketassistant.Modelo;

/**
 * Created by Jamal on 31/05/14.
 */
public class UserContainer {

    private static Usuario user = null;

    public static void AsignarUser(Usuario usr){
        UserContainer.user = usr;
    }

    public static void DesasignarUser(){
        UserContainer.user = null;
    }

    public static Usuario DameUser(){
        return UserContainer.user;
    }
}
