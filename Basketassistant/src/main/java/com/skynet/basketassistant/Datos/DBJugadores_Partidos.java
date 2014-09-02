package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Modelo.Partido_Jugador;
import com.skynet.basketassistant.Modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PROGRAMACION on 17/06/14.
 */
public class DBJugadores_Partidos {

    //Nombre de la tabla
    public static final String TABLE_NAME = "Jugadores_Partidos";

    //como se llamaran las columnas
    public static final String CN_ID = "_id";
    public static final String CN_PARTIDO_ID = "partido_id";
    public static final String CN_JUGADOR_ID = "jugador_id";
                                                            //crear una variable con la cantidad de minutos jugados?

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"(" +
                                                    CN_ID+" integer primary key not null," +
                                                    CN_PARTIDO_ID+" integer not null," +
                                                    CN_JUGADOR_ID+" integer not null);";
        return query;
    }

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBJugadores_Partidos(Context context) {
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

    private ContentValues Contenedorvalores(int part_id,int jug_id){
        ContentValues cont = new ContentValues();
        cont.put(CN_PARTIDO_ID,part_id);
        cont.put(CN_JUGADOR_ID,jug_id);
        return cont;
    }

    //Inserto nuevo equipo mediante el metodo de SQLiteDatabase
    public void insertar(int part_id,int jug_id){

        db.insert(TABLE_NAME,null, Contenedorvalores(part_id,jug_id));
    }

    //Eliminar comun, debido a que no tiene ninguna FK
    public void eliminar(int id){
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+CN_ID+" = "+id;
        db.execSQL(query);
    }

    //Carga de cursor
    public Cursor CargarcursorJug_Part(){
        String[] columnas = new String[]{CN_ID,CN_PARTIDO_ID,CN_JUGADOR_ID};
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public List<Partido_Jugador> Lista_PJ_Jugador(int jug_id){
        String[] columnas = new String[]{CN_ID,CN_PARTIDO_ID,CN_JUGADOR_ID};
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id,null,null,null,null);
        return CrearListaporCursor(c);
    }

    public List<Partido_Jugador> Lista_PJ_Partido(int part_id){
        String[] columnas = new String[]{CN_ID,CN_PARTIDO_ID,CN_JUGADOR_ID};
        Cursor c = db.query(TABLE_NAME,columnas,CN_PARTIDO_ID+" = "+part_id,null,null,null,null);
        return CrearListaporCursor(c);
    }

    public List<Partido_Jugador> CrearListaporCursor(Cursor c){

        List<Partido_Jugador> lista_pj = new ArrayList<Partido_Jugador>();
        if(c.moveToFirst()){
            do{
                int id = c.getColumnIndex(CN_ID);
                int j_id = c.getColumnIndex(CN_JUGADOR_ID);
                int p_id = c.getColumnIndex(CN_PARTIDO_ID);
                Partido_Jugador p_j = new Partido_Jugador(c.getInt(id),c.getInt(p_id),c.getInt(j_id));
                lista_pj.add(p_j);
            }while(c.moveToNext());
        }
        c.close();
        return lista_pj;
    }

}
