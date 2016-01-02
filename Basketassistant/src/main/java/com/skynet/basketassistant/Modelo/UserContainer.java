package com.skynet.basketassistant.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import com.skynet.basketassistant.Datos.DBUsuarios;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

/**
 * Created by Jamal on 31/05/14.
 */
public class UserContainer {

    private static Usuario user = null;

    public static boolean isUserRegistered(Context context){
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        String username = sp.getString(Constants.SP_USER, "");

        DBUsuarios dbus = new DBUsuarios(context);
        dbus.Modolectura();

        if((username.length() > 0) && (dbus.BuscarUsuario(username) != null)){
            UserContainer.user = dbus.BuscarUsuario(username);
            return true;
        }else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Constants.SP_USER, "");
            editor.commit();
            return false;
        }
    }

    public static void UserFirstTimeRegister(Context context, String username, String email){
        DBUsuarios dbus = new DBUsuarios(context);
        dbus.Modolectura();
        Usuario user = dbus.BuscarUsuario(username);
        if(user == null){
            dbus.Modoescritura();
            user = dbus.insertar(username,"",email);
            dbus.Cerrar();
        }
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SP_USER, user.getNombre());
        editor.commit();
        UserContainer.user = user;
    }

    public static Usuario DameUser(){
        return UserContainer.user;
    }


    public static void DesloguearUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        UserContainer.user = null;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SP_USER, "");
        editor.commit();
    }
}
