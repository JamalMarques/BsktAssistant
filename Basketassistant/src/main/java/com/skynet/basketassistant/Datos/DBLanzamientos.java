package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skynet.basketassistant.Modelo.Lanzamiento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamal on 02/04/14.
 */
public class DBLanzamientos{

    //efectivo bool (Sqlite se puede declarar como ´boolean´ pero los maneja como 0 y 1)

    //Nombre de la tabla
    private static final String TABLE_NAME = "Lanzamientos";

    //Como se llamaran las columnas
    public static final String CN_ID = "_id"; //necesario para los cursores
    public static final String CN_EFECTIVO = "efectivo";
    //public static final String CN_POSX = "posx";
   // public static final String CN_POSY = "posy";
    public static final String CN_QUARTER = "quarter";
    public static final String CN_TIPOLANZ = "tipolanz";
    public static final String CN_VALOR = "valor";
    public static final String CN_PARTIDO_ID = "partido_id";
    public static final String CN_JUGADOR_ID = "jugador_id";

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"("+
                                                    CN_ID+" integer primary key not null,"+
                                                    CN_EFECTIVO+" integer not null,"+
                                                   // CN_POSX+" integer not null,"+
                                                  //  CN_POSY+" integer not null,"+
                                                    CN_QUARTER+" integer not null,"+
                                                    CN_TIPOLANZ+" text not null,"+  //simple,doble o triple.
                                                    CN_VALOR+" integer not null,"+
                                                    CN_PARTIDO_ID+" integer not null,"+
                                                    CN_JUGADOR_ID+" integer not null," +
                            " FOREIGN KEY ("+CN_PARTIDO_ID+") REFERENCES "+DBPartidos.TABLE_NAME+" ("+DBPartidos.CN_ID+") ON DELETE CASCADE ," +
                            " FOREIGN KEY ("+CN_JUGADOR_ID+") REFERENCES "+DBJugadores.TABLE_NAME+" ("+DBJugadores.CN_ID+") ON DELETE CASCADE);";
        return query;
    }

    private String[] columnas = new String[]{CN_ID,CN_EFECTIVO,CN_QUARTER,CN_TIPOLANZ,CN_VALOR,CN_PARTIDO_ID,CN_JUGADOR_ID};

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBLanzamientos(Context context){
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

    private ContentValues Contenedorvalores(int efectivo,String tipolanz,int quarter,int valor,int part_id,int jug_id){
        ContentValues cont = new ContentValues();
        cont.put(CN_EFECTIVO,efectivo);
        //cont.put(CN_POSX,posx);
        //cont.put(CN_POSY,posy);
        cont.put(CN_QUARTER,quarter);
        cont.put(CN_TIPOLANZ,tipolanz);
        cont.put(CN_VALOR,valor);
        cont.put(CN_PARTIDO_ID,part_id);
        cont.put(CN_JUGADOR_ID,jug_id);
        return cont;
    }

    //insercion mediante metodo android
    public void insertar(int efectivo,String tipolanz,int quarter,int valor,int part_id,int jug_id){
        db.insert(TABLE_NAME,null, Contenedorvalores(efectivo,tipolanz,quarter,valor,part_id,jug_id));
    }
    public void insertar(Lanzamiento lanz){
        this.insertar(lanz.getEfectivo(),lanz.getTipoLanzamiento(),lanz.getQuarter_number(),lanz.getValor(),lanz.getPartido_id(),lanz.getJugador_id());
    }

    //eliminacion normal
    public void eliminar(int id){
        String query = "delete from "+TABLE_NAME+" where "+CN_ID+" = "+id;
        db.execSQL(query);
    }

    /* ACTIVAR AL AGREGAR EL ATRIBUTO CN_JUGADOR_ID
    public void eliminarporjugador(int id_jug){
        String query = "delete from "+TABLE_NAME+" where "+CN_JUGADOR_ID+" = "+id_jug;
        db.execSQL(query);
    }*/

    //Carga de cursor
    public Cursor Cargarcursorlanzamientos(){
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public List<Lanzamiento> ListaLanzamientosJugador(int jug_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id,null,null,null,null);
        return CrearLista(c);
    }

    public List<Lanzamiento> ListaLanzamientosPartido(int part_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_PARTIDO_ID+" = "+part_id,null,null,null,null);
        return CrearLista(c);
    }

    public List<Lanzamiento> CrearLista(Cursor c){
        List<Lanzamiento> lista_lanz = new ArrayList<Lanzamiento>();
        if(c.moveToFirst()){
            do {
                int id = c.getColumnIndex(CN_ID);
                int efect = c.getColumnIndex(CN_EFECTIVO);
                int tiplanz = c.getColumnIndex(CN_TIPOLANZ);
                int quarter = c.getColumnIndex(CN_QUARTER);
                int val = c.getColumnIndex(CN_VALOR);
                int part_id = c.getColumnIndex(CN_PARTIDO_ID);
                int jug_id = c.getColumnIndex(CN_JUGADOR_ID);
                Lanzamiento lanz = new Lanzamiento(c.getInt(id),c.getInt(efect),c.getString(tiplanz),c.getInt(val),
                                                    c.getInt(part_id),c.getInt(jug_id),c.getInt(quarter));
                lista_lanz.add(lanz);
            }while (c.moveToNext());
        }
        c.close();
        return lista_lanz;
    }

    public List<Lanzamiento> Lanzamientos_triple_jugador(int jug_id){  //FUNCIONAAA!
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id+" and "+CN_VALOR+" = 3",null,null,null,null);
        return CrearLista(c);
    }
    public List<Lanzamiento> Lanzamientos_doble_jugador(int jug_id){  //FUNCIONAAA!
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id+" and "+CN_VALOR+" = 2",null,null,null,null);
        return CrearLista(c);
    }
    public List<Lanzamiento> Lanzamientos_simple_jugador(int jug_id){  //FUNCIONAAA!
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id+" and "+CN_VALOR+" = 1",null,null,null,null);
        return CrearLista(c);
    }

    public List<Lanzamiento> Lanzamientos_jugador_en_partido(int jug_id,int part_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id+" and "+CN_PARTIDO_ID+" = "+part_id,null,null,null,null);
        return CrearLista(c);
    }

    public boolean SaveOnDatabase(List<Lanzamiento> shootList){
        this.Modoescritura();
        for (int i=0; i < shootList.size(); i++){
            insertar(shootList.get(i).getEfectivo(),shootList.get(i).getTipoLanzamiento(),shootList.get(i).getQuarter_number(),shootList.get(i).getValor(),shootList.get(i).getPartido_id(),
                    shootList.get(i).getJugador_id());
        }
        this.Cerrar();
        return true;
    }

}
