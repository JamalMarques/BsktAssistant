package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Jugador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamal on 30/03/14.
 */
public class DBJugadores{
    //Nombre de la tabla
    public static final String TABLE_NAME = "Jugadores";

    //como se llamaran las columnas
    public static final String CN_ID = "_id";
    public static final String CN_APELLIDO = "apellido";
    public static final String CN_NOMBRE = "nombre";
    public static final String CN_ALTURA = "altura";
    public static final String CN_PESO = "peso";
    public static final String CN_NUMERO = "numero";
    public static final String CN_ROL = "rol";
    public static final String CN_EQUIPO_ID = "equipo_id";
    public static final String CN_IMAGEN_URL = "imagen_url";

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"(" +
                                                    CN_ID+" integer primary key not null," +
                                                    CN_APELLIDO+" text not null," +
                                                    CN_NOMBRE+" text not null," +
                                                    CN_ALTURA+" integer not null," +
                                                    CN_PESO+" integer not null," +
                                                    CN_NUMERO+" integer not null," +
                                                    CN_ROL+" text not null," +
                                                    CN_EQUIPO_ID+" int not null," +
                                                    CN_IMAGEN_URL+" text ," +
                " FOREIGN KEY ("+CN_EQUIPO_ID+") REFERENCES "+DBEquipos.TABLE_NAME+" ("+DBEquipos.CN_ID+") ON DELETE CASCADE );";
        return query;
    }

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBJugadores(Context context) {
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


    private ContentValues Contenedorvalores(String apellido,String nombre,double altura,
                                            double peso,int numero,String rol,int equipo_id,String img_url){
        ContentValues cont = new ContentValues();
        cont.put(CN_APELLIDO,apellido);
        cont.put(CN_NOMBRE,nombre);
        cont.put(CN_ALTURA,altura);
        cont.put(CN_PESO,peso);
        cont.put(CN_NUMERO,numero);
        cont.put(CN_ROL,rol);
        cont.put(CN_EQUIPO_ID,equipo_id);
        cont.put(CN_IMAGEN_URL,img_url);
        return cont;
    }

    //Inserto nuevo equipo mediante el metodo de SQLiteDatabase
    public void insertar(String apellido,String nombre,double altura,
                         double peso,int numero,String rol,int equipo_id,String img_url){

        db.insert(TABLE_NAME,null, Contenedorvalores(apellido,nombre,altura,peso,numero,rol,equipo_id,img_url));
    }

    //Eliminacion propia (id a eliminar, columna a basarse)
    //Ejemplo en DBEquipos
    public void eliminar(int id, String columnabase){
        if(columnabase == CN_ID || columnabase == CN_EQUIPO_ID) {
            String query = "DELETE FROM " + TABLE_NAME + " WHERE " + columnabase + " = " + id;
            db.execSQL(query);
        }
    }

    public void CambiarNomImagen_Jugador(int jug_id,String imagenom){
        db.execSQL("UPDATE "+TABLE_NAME+" SET "+CN_IMAGEN_URL+" = '"+imagenom+"' WHERE "+CN_ID+" = "+jug_id);
    }

    //Carga de cursor
    private Cursor Cargarcursorjugadores(){
        String[] columnas = new String[]{CN_ID,CN_APELLIDO,CN_NOMBRE,CN_ALTURA,CN_PESO,CN_NUMERO,CN_ROL,CN_EQUIPO_ID,CN_IMAGEN_URL};
        return db.query(TABLE_NAME,columnas,null,null,null,null,null);
    }

    //Cursor con busqueda especifica por equipo
    private Cursor Cargarcursorjugadores(int equipo_id){
        String[] columnas = new String[]{CN_ID,CN_APELLIDO,CN_NOMBRE,CN_ALTURA,CN_PESO,CN_NUMERO,CN_ROL,CN_EQUIPO_ID,CN_IMAGEN_URL};
        return db.query(TABLE_NAME,columnas,CN_EQUIPO_ID+" = "+equipo_id,null,null,null,null);
    }

    public Jugador DameJugador(int idjug){
        String[] columnas = new String[]{CN_ID,CN_APELLIDO,CN_NOMBRE,CN_ALTURA,CN_PESO,CN_NUMERO,CN_ROL,CN_EQUIPO_ID,CN_IMAGEN_URL};
        Cursor c = db.query(TABLE_NAME,columnas,CN_ID+" = "+idjug,null,null,null,null);

        try{
            if(c.moveToFirst()){
                int id = c.getColumnIndex(CN_ID);
                int ape = c.getColumnIndex(CN_APELLIDO);
                int nom = c.getColumnIndex(CN_NOMBRE);
                int alt = c.getColumnIndex(CN_ALTURA);
                int pes = c.getColumnIndex(CN_PESO);
                int num = c.getColumnIndex(CN_NUMERO);
                int rol = c.getColumnIndex(CN_ROL);
                int equip_id = c.getColumnIndex(CN_EQUIPO_ID);
                int img_url = c.getColumnIndex(CN_IMAGEN_URL);
                Jugador jug = new Jugador(c.getInt(id),c.getString(ape),c.getString(nom),c.getDouble(alt),c.getDouble(pes),c.getInt(num),c.getString(rol),
                                            c.getInt(equip_id),c.getString(img_url));
                return jug;

            }else
                return null; //no existe el jugador
        }finally {
            c.close();
        }
    }

    private List<Jugador> CrearLista(Cursor c){
        List<Jugador> list_jug = new ArrayList<Jugador>();
        if(c.moveToFirst()){
            do {
                int id = c.getColumnIndex(CN_ID);
                int ape = c.getColumnIndex(CN_APELLIDO);
                int nom = c.getColumnIndex(CN_NOMBRE);
                int alt = c.getColumnIndex(CN_ALTURA);
                int pes = c.getColumnIndex(CN_PESO);
                int num = c.getColumnIndex(CN_NUMERO);
                int rol = c.getColumnIndex(CN_ROL);
                int equip_id = c.getColumnIndex(CN_EQUIPO_ID);
                int img_url = c.getColumnIndex(CN_IMAGEN_URL);
                Jugador jug = new Jugador(c.getInt(id),c.getString(ape),c.getString(nom),c.getDouble(alt),c.getDouble(pes),c.getInt(num),c.getString(rol),
                        c.getInt(equip_id),c.getString(img_url));
                list_jug.add(jug);
            }while(c.moveToNext());
        }
        c.close();
        return list_jug;
    }

    public List<Jugador> DameListaJugadores(){
        return CrearLista(Cargarcursorjugadores());
    }

    public List<Jugador> DameListaJugadoresEquipo(int team_id){
        return CrearLista(Cargarcursorjugadores(team_id));
    }
}
