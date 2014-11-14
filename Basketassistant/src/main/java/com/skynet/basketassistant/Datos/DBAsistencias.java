package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skynet.basketassistant.Modelo.Asistencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamal on 02/04/14.
 */
public class DBAsistencias{

    //Nombre de la tabla
    private static final String TABLE_NAME = "Asistencias";

    //Como se llamaran las columnas
    public static final String CN_ID = "_id"; //necesario para los cursores
    public static final String CN_JUGADOR_ID = "jugador_id";
    public static final String CN_PARTIDO_ID = "partido_id";

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"("+
                                                    CN_ID+" integer primary key not null,"+
                                                    CN_JUGADOR_ID+" integer not null,"+
                                                    CN_PARTIDO_ID+" integer not null);";
        return query;
    }

    private String[] columnas = new String[]{CN_ID,CN_JUGADOR_ID,CN_PARTIDO_ID};

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBAsistencias(Context context){
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


    private ContentValues Contenedorvalores(int jugador_id,int partido_id){
        ContentValues cont = new ContentValues();
        cont.put(CN_JUGADOR_ID,jugador_id);
        cont.put(CN_PARTIDO_ID,partido_id);
        return cont;
    }

    //insercion mediante metodo android
    public void insertar(int partido_id,int jugador_id){
        db.insert(TABLE_NAME,null, Contenedorvalores(jugador_id,partido_id));
    }

    //eliminacion propia (id a eliminar, columna en cual basarse)
    public void eliminar(int id, String columnabase){
        if(columnabase == CN_ID || columnabase == CN_JUGADOR_ID || columnabase == CN_PARTIDO_ID){
            String query = "delete from "+TABLE_NAME+" where "+columnabase+" = "+id;
            db.execSQL(query);
        }
    }


    public List<Asistencia> Lista_Asist_Jugador(int jug_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id,null,null,null,null);
        return CrearLista(c);
    }

    public List<Asistencia> List_Asist_Partido(int part_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_PARTIDO_ID+" = "+part_id,null,null,null,null);
        return CrearLista(c);
    }

    public List<Asistencia> CrearLista(Cursor c){

        List<Asistencia> lista_asist = new ArrayList<Asistencia>();
        if(c.moveToFirst()){
            do {
                int id = c.getColumnIndex(CN_ID);
                int jug_id = c.getColumnIndex(CN_JUGADOR_ID);
                int part_id = c.getColumnIndex(CN_PARTIDO_ID);
                Asistencia asist = new Asistencia(c.getInt(id),c.getInt(jug_id),c.getInt(part_id));
                lista_asist.add(asist);
            }while(c.moveToNext());
        }
        c.close();
        return lista_asist;
    }

    public List<Asistencia> ListAsist_jugador_partido(int jug_id,int part_id){
        Cursor c = db.query(TABLE_NAME,columnas,CN_JUGADOR_ID+" = "+jug_id+" and "+CN_PARTIDO_ID+" = "+part_id,null,null,null,null);
        return CrearLista(c);
    }

    public boolean SaveOnDatabase(List<Asistencia> assistList){
        this.Modoescritura();
        for (int i=0; i < assistList.size(); i++){
            insertar(assistList.get(i).getPartido_id(),assistList.get(i).getJugador_id());
        }
        this.Cerrar();
        return true;
    }
}
