package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.appcompat.R;

import com.skynet.basketassistant.Modelo.Ciudad;
import com.skynet.basketassistant.Modelo.Equipo;

/**
 * Created by jamal on 30/03/14.
 */
public class DBCiudades{
    //Nombre de la tabla
    public static final String TABLE_NAME = "Ciudades";

    //como se llamaran las columnas
    public static final String CN_ID = "_id";
    public static final String CN_CIUDAD = "ciudad";
    public static final String CN_PROVINCIA = "provincia";

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"(" +
                                                    CN_ID+" integer primary key not null," +
                                                    CN_CIUDAD+" text not null," +
                                                    CN_PROVINCIA+" text not null);";
        return query;
    }

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBCiudades(Context context) {
        helper = new DBHelper(context);
    }

    //Abrir la tabla en modo escritura
    public void Modoescritura(){ db = helper.getWritableDatabase(); }

    //Abrir la tabla en modo lectura
    public void Modolectura(){ db = helper.getReadableDatabase(); }

    //Cierro la base de datos
    public void Cerrar(){
        if(db != null)
            db.close();
    }


    private ContentValues Contenedorvalores(String ciudad,String provincia){
        ContentValues cont = new ContentValues();
        cont.put(CN_CIUDAD,ciudad);
        cont.put(CN_PROVINCIA,provincia);
        return cont;
    }

    //Inserto nuevo equipo mediante el metodo de SQLiteDatabase
    public void insertar(String ciudad,String provincia){

        db.insert(TABLE_NAME,null, Contenedorvalores(ciudad,provincia));
    }

    //Eliminar comun, debido a que no tiene ninguna FK
    public void eliminar(int id){
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+CN_ID+" = "+id;
        db.execSQL(query);
    }

    //Carga de cursor
    public Cursor Cargarcursorciudades(){
        String[] columnas = new String[]{CN_ID,CN_CIUDAD,CN_PROVINCIA};
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }


    //dame ciudad especifica por id
    public Ciudad Dameciudad(int id_ciud){
        String[] columnas = new String[]{CN_ID,CN_CIUDAD,CN_PROVINCIA};
        String query = "SELECT "+CN_ID+","+CN_CIUDAD+","+CN_PROVINCIA+" FROM "+TABLE_NAME+" WHERE "+CN_ID+" = "+id_ciud;
        Cursor c = db.rawQuery(query,null);

        try{
            if(c.moveToFirst()){
                int id = c.getColumnIndex(CN_ID);
                int ciu = c.getColumnIndex(CN_CIUDAD);
                int prov = c.getColumnIndex(CN_PROVINCIA);
                Ciudad ciudad = new Ciudad(c.getInt(id),c.getString(ciu),c.getString(prov));
                return ciudad;
            }
            else
                return null;
        }finally {
            c.close();
        }
    }

    //dame ciudad especifica por nombre   COMILLASSSS!!!
    public Ciudad Dameciudad(String ciudad){
        String[] columnas = new String[]{CN_ID,CN_CIUDAD,CN_PROVINCIA};
        String query = "SELECT "+CN_ID+","+CN_CIUDAD+","+CN_PROVINCIA+" FROM "+TABLE_NAME+" WHERE "+CN_CIUDAD+" = '"+ciudad+"' ";
        Cursor c = db.rawQuery(query,null);

        try{
            if(c.moveToFirst()){
                int id = c.getColumnIndex(CN_ID);
                int ciu = c.getColumnIndex(CN_CIUDAD);
                int prov = c.getColumnIndex(CN_PROVINCIA);
                Ciudad ci = new Ciudad(c.getInt(id),c.getString(ciu),c.getString(prov));
                return ci;
            }
            else
                return null;
        }finally {
            c.close();
        }
    }

    public boolean Existe(String ciudad){

        Ciudad ciu = Dameciudad(ciudad);
        if(ciu == null)
            return false;
        else
            return true;
    }

}

