package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Telephony;

import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Partido;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jamal on 31/03/14.
 */
public class DBPartidos{
    //nombre de la tabla
    public static final String TABLE_NAME = "Partidos";

    //como se llamaran las columnas
    public static final String CN_ID = "_id";  //necesario sino los cursores no funcionaran debe llamarse "_id"
    public static final String CN_FECHA = "fecha";
    public static final String CN_CANCHA = "cancha";
    public static final String CN_PUNTOS_E1 = "puntos_e1";
    public static final String CN_PUNTOS_E2 = "puntos_e2";
    public static final String CN_EQUIPO1_ID = "equipo1_id";
    public static final String CN_EQUIPO2_NOM = "equipo2_id";
    public static final String CN_PUNTOS_Q1_E1 = "puntos_q1_e1";
    public static final String CN_PUNTOS_Q2_E1 = "puntos_q2_e1";
    public static final String CN_PUNTOS_Q3_E1 = "puntos_q3_e1";
    public static final String CN_PUNTOS_Q4_E1 = "puntos_q4_e1";
    public static final String CN_PUNTOS_Q1_E2 = "puntos_q1_e2";
    public static final String CN_PUNTOS_Q2_E2 = "puntos_q2_e2";
    public static final String CN_PUNTOS_Q3_E2 = "puntos_q3_e2";
    public static final String CN_PUNTOS_Q4_E2 = "puntos_q4_e2";
    public static final String CN_PUNTOS_EXT_E1 = "ext_e1";
    public static final String CN_PUNTOS_EXT_E2 = "ext_e2";


    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"(" +
                                                    CN_ID+" integer primary key not null," +
                                                    CN_FECHA+" date not null," +
                                                    CN_CANCHA+" text," +
                                                    CN_PUNTOS_E1+" integer not null," +
                                                    CN_PUNTOS_E2+" integer not null," +
                                                    CN_EQUIPO1_ID+" integer not null," +
                                                    CN_EQUIPO2_NOM+" text not null,"+
                                                    CN_PUNTOS_Q1_E1+" integer,"+
                                                    CN_PUNTOS_Q2_E1+" integer,"+
                                                    CN_PUNTOS_Q3_E1+" integer,"+
                                                    CN_PUNTOS_Q4_E1+" integer,"+
                                                    CN_PUNTOS_Q1_E2+" integer,"+
                                                    CN_PUNTOS_Q2_E2+" integer,"+
                                                    CN_PUNTOS_Q3_E2+" integer,"+
                                                    CN_PUNTOS_Q4_E2+" integer,"+
                                                    CN_PUNTOS_EXT_E1+" integer,"+
                                                    CN_PUNTOS_EXT_E2+" integer);";
        return query;
    }

    private String[] columnas = new String[]{CN_ID,CN_FECHA,CN_CANCHA,CN_PUNTOS_E1,CN_PUNTOS_E2,CN_EQUIPO1_ID,CN_EQUIPO2_NOM,CN_PUNTOS_Q1_E1,CN_PUNTOS_Q2_E1,
            CN_PUNTOS_Q3_E1,CN_PUNTOS_Q4_E1,CN_PUNTOS_Q1_E2,CN_PUNTOS_Q2_E2,CN_PUNTOS_Q3_E2,CN_PUNTOS_Q4_E2,CN_PUNTOS_EXT_E1,CN_PUNTOS_EXT_E2};


    private DBHelper helper;
    private SQLiteDatabase db;

    public DBPartidos(Context context){
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


    private ContentValues Contenedorvalores(String fecha,String cancha,int equipo1_id,String equipo2_nom,int puntos_e1,int puntos_e2,
                                            int punt_q1_e1,int punt_q2_e1,int punt_q3_e1,int punt_q4_e1,int punt_q1_e2,int punt_q2_e2,int punt_q3_e2,int punt_q4_e2,int punt_ext_e1,int punt_ext_e2){
        ContentValues cont = new ContentValues();
        cont.put(CN_FECHA,fecha); //debe ser en esta nomenclatura: "1992-01-23"
        cont.put(CN_CANCHA,cancha);
        cont.put(CN_EQUIPO1_ID,equipo1_id);
        cont.put(CN_EQUIPO2_NOM,equipo2_nom);
        cont.put(CN_PUNTOS_E1,puntos_e1);
        cont.put(CN_PUNTOS_E2,puntos_e2);
        cont.put(CN_PUNTOS_Q1_E1,punt_q1_e1);
        cont.put(CN_PUNTOS_Q2_E1,punt_q2_e1);
        cont.put(CN_PUNTOS_Q3_E1,punt_q3_e1);
        cont.put(CN_PUNTOS_Q4_E1,punt_q4_e1);
        cont.put(CN_PUNTOS_Q1_E2,punt_q1_e2);
        cont.put(CN_PUNTOS_Q2_E2,punt_q2_e2);
        cont.put(CN_PUNTOS_Q3_E2,punt_q3_e2);
        cont.put(CN_PUNTOS_Q4_E2,punt_q4_e2);
        cont.put(CN_PUNTOS_EXT_E1,punt_ext_e1);
        cont.put(CN_PUNTOS_EXT_E2,punt_ext_e2);
        return cont;
    }


    //Inserto nuevo equipo mediante el metodo de SQLiteDatabase
    //Si se le manda un STRING como fecha el sqlite lo interpreta perfectamente siempre y cuando
    //se lo envie de la siguiente forma "1992-01-03" [anio,mes,dia] por ende es posible usar el db.insert si el String fecha esta
    //escrito de la forma correcta
    public void insertar(String fecha,String cancha,int equipo1_id,String equipo2_nom,int puntos_e1,int puntos_e2,
                          int punt_q1_e1,int punt_q2_e1,int punt_q3_e1,int punt_q4_e1,int punt_q1_e2,int punt_q2_e2,int punt_q3_e2,int punt_q4_e2,
                          int punt_ext_e1,int punt_ext_e2){

        db.insert(TABLE_NAME,null, Contenedorvalores(fecha,cancha,equipo1_id,equipo2_nom,puntos_e1,puntos_e2,punt_q1_e1,punt_q2_e1,punt_q3_e1,punt_q4_e1,
                                                        punt_q1_e2,punt_q2_e2,punt_q3_e2,punt_q4_e2,punt_ext_e1,punt_ext_e2));
    }


    //Eliminacion propia (id a eliminar, columna a basarse)
    // Ej:  dbequipos.eliminar(3,dbequipos.CN_USER_ID): borrara el q tenga como user_id = 3
    public void eliminar(int id, String columnabase){
        if(columnabase == CN_ID || columnabase == CN_EQUIPO1_ID) {
            String query = "DELETE FROM " + TABLE_NAME + " WHERE " + columnabase + " = " + id;
            db.execSQL(query);
        }
    }

    //Carga de cursor
    public Cursor Cargarcursorpartidos(){
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public Cursor Cargarcursorpartidos(int id_equip){
        return db.query(TABLE_NAME,columnas,CN_EQUIPO1_ID+" = "+id_equip,null,null,null,null);
    }

    public Partido DamePartido(int idpartido){

        Cursor c = db.query(TABLE_NAME,columnas,CN_ID+" = "+idpartido,null,null,null,null);

        try{
            if(c.moveToFirst()){

                    int id = c.getColumnIndex(CN_ID);
                    int fecha = c.getColumnIndex(CN_FECHA);
                    int cancha = c.getColumnIndex(CN_CANCHA);
                    int puntos_e1 = c.getColumnIndex(CN_PUNTOS_E1);
                    int puntos_e2 = c.getColumnIndex(CN_PUNTOS_E2);
                    int equipo1_id = c.getColumnIndex(CN_EQUIPO1_ID);
                    int equipo2_nom = c.getColumnIndex(CN_EQUIPO2_NOM);
                    int punt_q1_e1 = c.getColumnIndex(CN_PUNTOS_Q1_E1);
                    int punt_q2_e1 = c.getColumnIndex(CN_PUNTOS_Q2_E1);
                    int punt_q3_e1 = c.getColumnIndex(CN_PUNTOS_Q3_E1);
                    int punt_q4_e1 = c.getColumnIndex(CN_PUNTOS_Q4_E1);
                    int punt_q1_e2 = c.getColumnIndex(CN_PUNTOS_Q1_E2);
                    int punt_q2_e2 = c.getColumnIndex(CN_PUNTOS_Q2_E2);
                    int punt_q3_e2 = c.getColumnIndex(CN_PUNTOS_Q3_E2);
                    int punt_q4_e2 = c.getColumnIndex(CN_PUNTOS_Q4_E2);
                    int punt_ext_e1 = c.getColumnIndex(CN_PUNTOS_EXT_E1);
                    int punt_ext_e2 = c.getColumnIndex(CN_PUNTOS_EXT_E2);

                    Partido part = new Partido(c.getInt(id),c.getString(fecha),c.getString(cancha),c.getInt(puntos_e1),c.getInt(puntos_e2),c.getInt(equipo1_id),c.getString(equipo2_nom),
                            c.getInt(punt_q1_e1),c.getInt(punt_q2_e1),c.getInt(punt_q3_e1),c.getInt(punt_q4_e1),c.getInt(punt_q1_e2),c.getInt(punt_q2_e2),c.getInt(punt_q3_e2),
                            c.getInt(punt_q4_e2),c.getInt(punt_ext_e1),c.getInt(punt_ext_e2));

                    return part;
            }
            else
                return null;
        }finally {
            c.close();
        }
    }

}
