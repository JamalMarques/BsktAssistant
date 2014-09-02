package com.skynet.basketassistant.Datos;

import android.content.Context;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skynet.basketassistant.Modelo.Falta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamal on 02/04/14.
 */
public class DBFaltas{

    //Nombre de la tabla
    private static final String TABLE_NAME = "Faltas";

    //Como se llamaran las columnas
    public static final String CN_ID = "_id"; //necesario para los cursores
    public static final String CN_JUGADOR_ID = "jugador_id";
    public static final String CN_PARTIDO_ID = "partido_id";
    public static final String CN_TIPO = "tipo";  //tipo de falta : normal/antideportiva

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"("+
                CN_ID+" integer primary key not null,"+
                CN_PARTIDO_ID+" integer not null,"+
                CN_JUGADOR_ID+" integer not null,"+
                CN_TIPO+" text not null);";
        return query;
    }

    private String[] columnas = new String[]{CN_ID,CN_JUGADOR_ID,CN_PARTIDO_ID,CN_TIPO};

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBFaltas(Context context){
        helper = new DBHelper(context);
    }

    //Abrir la tabla en modo escritura
    public void Modoescritura(){ db = helper.getWritableDatabase(); }

    //Abrir tabla en modo lectura
    public void Modolectura(){ db = helper.getReadableDatabase(); }

    //Cierro la base de datos
    public void Cerrar(){
        if(db != null)
            db.close();
    }


    private ContentValues Contenedorvalores(int partido_id,int jugador_id,String tip){
        ContentValues cont = new ContentValues();
        cont.put(CN_PARTIDO_ID,partido_id);
        cont.put(CN_JUGADOR_ID,jugador_id);
        cont.put(CN_TIPO,tip);
        return cont;
    }

    //insercion mediante metodo android
    public void insertar(int partido_id,int jugador_id,String tip){
        db.insert(TABLE_NAME,null, Contenedorvalores(partido_id,jugador_id,tip));
    }

    //eliminacion propia (id a eliminar, columna en cual basarse)
    public void eliminar(int id, String columnabase){
        if(columnabase == CN_ID || columnabase == CN_JUGADOR_ID || columnabase == CN_PARTIDO_ID){
            String query = "delete from "+TABLE_NAME+" where "+columnabase+" = "+id;
            db.execSQL(query);
        }
    }

    public List<Falta> ListaFaltasJugador(int jug_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id,null,null,null,null);
        return CrearLista(c);
    }

    public List<Falta> ListaFaltasPartido(int part_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_PARTIDO_ID+" = "+part_id,null,null,null,null);
        return CrearLista(c);
    }

    public List<Falta> CrearLista(Cursor c){ //esto elimina el cursor tambien
        List<Falta> lista_faltas = new ArrayList<Falta>();
        if(c.moveToFirst()){
            do {
                int id = c.getColumnIndex(CN_ID);
                int p_id = c.getColumnIndex(CN_PARTIDO_ID);
                int j_id = c.getColumnIndex(CN_JUGADOR_ID);
                int tip = c.getColumnIndex(CN_TIPO);
                Falta fal = new Falta(c.getInt(id),c.getInt(p_id),c.getInt(j_id),c.getString(tip));
                lista_faltas.add(fal);
            }while(c.moveToNext());
        }
        c.close();
        return lista_faltas;
    }

    public List<Falta> ListaFaltas_jugador_partido(int jug_id,int part_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_PARTIDO_ID+" = "+part_id+" and "+CN_JUGADOR_ID+" = "+jug_id,null,null,null,null);
        return CrearLista(c);
    }

}
