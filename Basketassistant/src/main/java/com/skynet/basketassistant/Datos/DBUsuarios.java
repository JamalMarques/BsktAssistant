package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skynet.basketassistant.Modelo.Usuario;

/**
 * Created by jamal on 30/03/14.
 */
public class DBUsuarios{
    //Nombre de la tabla
    public static final String TABLE_NAME = "Usuarios";

    //como se llamaran las columnas
    public static final String CN_ID = "_id";
    public static final String CN_NOMBRE = "nombre";
    public static final String CN_PASS = "pass";
    public static final String CN_CORREO = "correo";

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"(" +
                                                    CN_ID+" integer primary key not null," +
                                                    CN_NOMBRE+" text not null," +
                                                    CN_PASS+" text not null," +
                                                    CN_CORREO+" text not null);";
        return query;
    }

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBUsuarios(Context context) {
        helper = new DBHelper(context);
    }

    //Abrir la tabla en modo escritura
    public void Modoescritura(){ db = helper.getWritableDatabase(); }

    //Abrir la tabla en modo lectura
    public void Modolectura(){ db = helper.getReadableDatabase(); }

    //Cierro la base de datos
    public void Cerrar(){
        db.close();
    }


    private ContentValues Contenedorvalores(String nombre,String pass,String correo){
        ContentValues cont = new ContentValues();
        cont.put(CN_NOMBRE,nombre);
        cont.put(CN_PASS,pass);
        cont.put(CN_CORREO, correo);
        return cont;
    }

    //Inserto nuevo equipo mediante el metodo de SQLiteDatabase
    public Usuario insertar(String nombre,String pass,String correo){
        db.insert(TABLE_NAME, null, Contenedorvalores(nombre, pass, correo));
        return BuscarUsuario(nombre);
    }

    //Eliminar comun, debido a que no tiene ninguna FK
    public void eliminar(int id){
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+CN_ID+" = "+id;
        db.execSQL(query);
    }

    //Carga de cursor
    public Cursor Cargarcursorusuarios(){
        String[] columnas = new String[]{CN_ID,CN_NOMBRE,CN_PASS,CN_CORREO};
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public Usuario BuscarUsuario(String nomb){
        String[] columnas = new String[]{CN_ID,CN_NOMBRE,CN_PASS,CN_CORREO};
        Cursor c = db.query(TABLE_NAME,columnas,CN_NOMBRE+"=?",new String[]{nomb},null,null,null);
        try{
            if(c.moveToFirst()){
                int id = c.getColumnIndex(CN_ID);
                int nom = c.getColumnIndex(CN_NOMBRE);
                int pass = c.getColumnIndex(CN_PASS);
                int corr = c.getColumnIndex(CN_CORREO);
                Usuario usr = new Usuario(c.getInt(id),c.getString(nom),c.getString(pass),c.getString(corr));
                return usr;
            }
            else
            return null;
        }finally {
            c.close();
        }
    }

    public boolean Existe(String nom){
        Usuario us = BuscarUsuario(nom);
        if(us == null){
            return false;
        }else
            return true;
    }

}
