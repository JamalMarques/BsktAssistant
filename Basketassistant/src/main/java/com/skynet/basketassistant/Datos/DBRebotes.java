package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skynet.basketassistant.Modelo.Rebote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamal on 02/04/14.
 */
public class DBRebotes{

    //Nombre de la tabla
    private static final String TABLE_NAME = "Rebotes";

    //Como se llamaran las columnas
    public static final String CN_ID = "_id"; //necesario para los cursores
    public static final String CN_JUGADOR_ID = "jugador_id";
    public static final String CN_PARTIDO_ID = "partido_id";
    public static final String CN_TIPOREB = "tiporeb";

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"("+
                CN_ID+" integer primary key not null,"+
                CN_JUGADOR_ID+" integer not null,"+
                CN_PARTIDO_ID+" integer not null,"+
                CN_TIPOREB+" text not null," +  //Ofensivo,Defensivo
                " FOREIGN KEY ("+CN_PARTIDO_ID+") REFERENCES "+DBPartidos.TABLE_NAME+" ("+DBPartidos.CN_ID+") ON DELETE CASCADE," +
                " FOREIGN KEY ("+CN_JUGADOR_ID+") REFERENCES "+DBJugadores.TABLE_NAME+" ("+DBJugadores.CN_ID+") ON DELETE CASCADE);";
        return query;
    }

    private String[] columnas = new String[]{CN_ID,CN_JUGADOR_ID,CN_PARTIDO_ID,CN_TIPOREB};

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBRebotes(Context context){
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


    private ContentValues Contenedorvalores(int jugador_id,int partido_id,String tiporeb){
        ContentValues cont = new ContentValues();
        cont.put(CN_JUGADOR_ID,jugador_id);
        cont.put(CN_PARTIDO_ID,partido_id);
        cont.put(CN_TIPOREB,tiporeb);
        return cont;
    }

    //insercion mediante metodo android
    public void insertar(int partido_id,int jugador_id,String tiporeb){
        db.insert(TABLE_NAME,null, Contenedorvalores(jugador_id,partido_id,tiporeb));
    }

    //eliminacion propia (id a eliminar, columna en cual basarse)
    public void eliminar(int id, String columnabase){
        if(columnabase == CN_ID || columnabase == CN_JUGADOR_ID || columnabase == CN_PARTIDO_ID){
            String query = "delete from "+TABLE_NAME+" where "+columnabase+" = "+id;
            db.execSQL(query);
        }
    }

    //Carga de cursor
    public Cursor Cargarcursorrebotes(){
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    public List<Rebote> RetobesJugador(int jug_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id,null,null,null,null);
        return CrearLista(c);
    }

    public List<Rebote> RebotesPartido(int part_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_PARTIDO_ID+" = "+part_id,null,null,null,null);
        return CrearLista(c);
    }

    public List<Rebote> CrearLista(Cursor c){
        List<Rebote> list_reb = new ArrayList<Rebote>();
        if(c.moveToFirst()){
            do{
                int id = c.getColumnIndex(CN_ID);
                int jug_id = c.getColumnIndex(CN_JUGADOR_ID);
                int part_id = c.getColumnIndex(CN_PARTIDO_ID);
                int tip_reb = c.getColumnIndex(CN_TIPOREB);
                Rebote reb = new Rebote(c.getInt(id),c.getInt(jug_id),c.getInt(part_id),c.getString(tip_reb));
                list_reb.add(reb);
            }while(c.moveToNext());
        }
        c.close();
        return list_reb;
    }

    public List<Rebote> ListaReb_jugador_partido(int jug_id,int part_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id+" and "+CN_PARTIDO_ID+" = "+part_id,null,null,null,null);
        return CrearLista(c);
    }

    public boolean SaveOnDatabase(List<Rebote> reboundList){
        this.Modoescritura();
        for (int i=0; i < reboundList.size(); i++){
            insertar(reboundList.get(i).getPartido_id(),reboundList.get(i).getJugador_id(),reboundList.get(i).getTiporeb());
        }
        this.Cerrar();
        return true;
    }
}
