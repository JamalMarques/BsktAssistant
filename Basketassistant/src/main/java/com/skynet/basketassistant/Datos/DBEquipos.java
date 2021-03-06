package com.skynet.basketassistant.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skynet.basketassistant.Modelo.Equipo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamal on 27/03/14.
 */
public class DBEquipos{
    //nombre de la tabla
    public static final String TABLE_NAME = "Equipos";

    //como se llamaran las columnas
    public static final String CN_ID = "_id";  //necesario sino los cursores no funcionaran debe llamarse "_id"
    public static final String CN_NOMBRE = "nombre";
    public static final String CN_CIUDAD_ID = "ciudad_id";
    public static final String CN_USER_ID = "user_id";
    public static final String CN_CATEGORIA = "categoria";

    public static String crear_tabla(){
        String query = "create table "+TABLE_NAME+"(" +
                                                    CN_ID+" integer primary key not null," +
                                                    CN_NOMBRE+" text not null," +
                                                    CN_CIUDAD_ID+" integer," +
                                                    CN_USER_ID+" integer," +
                                                    CN_CATEGORIA+" text not null," +
                " FOREIGN KEY ("+CN_USER_ID+") REFERENCES "+DBUsuarios.TABLE_NAME+" ("+DBUsuarios.CN_ID+") ON DELETE CASCADE );";
        return query;
    }

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBEquipos(Context context){
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


    private ContentValues Contenedorvalores(String nombre,int ciudad_id,int user_id,String cate){
        ContentValues cont = new ContentValues();
        cont.put(CN_NOMBRE,nombre);
        cont.put(CN_CIUDAD_ID,ciudad_id);
        cont.put(CN_USER_ID,user_id);
        cont.put(CN_CATEGORIA,cate);
        return cont;
    }

    //Inserto nuevo equipo mediante el metodo de SQLiteDatabase
    public void insertar(String nombre,int ciudad_id,int user_id,String cate){
        Modolectura();
        db.insert(TABLE_NAME,null, Contenedorvalores(nombre,ciudad_id,user_id,cate));
        Cerrar();
    }

    //Eliminacion propia (id a eliminar, columna a basarse)
    // Ej:  dbequipos.eliminar(3,dbequipos.CN_USER_ID): borrara el q tenga como user_id = 3
    public void eliminar(int id, String columnabase){
        Modoescritura();
        if(columnabase == CN_ID || columnabase == CN_CIUDAD_ID || columnabase == CN_USER_ID) {
            String query = "DELETE FROM " + TABLE_NAME + " WHERE " + columnabase + " = " + id;
            db.execSQL(query);
        }
        Cerrar();
    }

    // ELEMENTOS CONCRETOS
    public List<Equipo> EquiposdeUsuario(int us_id){  //hacer con consulta SQL mucho mas facil y seguro...
        Modolectura();
        Cursor c = db.rawQuery("Select * from "+TABLE_NAME+" where "+CN_USER_ID+" = "+us_id,null);
        return CrearLista(c);
    }

    public Equipo DameEquipo(String nomequip){
        Modolectura();
        Cursor c = db.rawQuery("Select * from "+TABLE_NAME+" where "+CN_NOMBRE+" like '"+nomequip+"'",null);
        return GenerarEquipo(c);
    }

    public Equipo DameEquipo(int id_equip){
        Modolectura();
        Cursor c = db.rawQuery("Select * from "+TABLE_NAME+" where "+CN_ID+" = "+id_equip,null);
        return GenerarEquipo(c);
    }

    public Equipo GenerarEquipo(Cursor c){

        try{
            if(c.moveToFirst()){
                int id = c.getColumnIndex(CN_ID);
                int nom = c.getColumnIndex(CN_NOMBRE);
                int ciu_id = c.getColumnIndex(CN_CIUDAD_ID);
                int us_id = c.getColumnIndex(CN_USER_ID);
                int cat = c.getColumnIndex(CN_CATEGORIA);
                Equipo equip = new Equipo(c.getInt(id),c.getString(nom),c.getInt(ciu_id),c.getInt(us_id),c.getString(cat));
                return equip;
            }else
                return null;  //No se encontro ninguno...
        }finally {
            c.close();
            Cerrar();
        }
    }


    public boolean Existe(String nomequip){
        Equipo equip = DameEquipo(nomequip);
        if(equip == null){
            return false;
        }
        else
            return true;
    }



    private List<Equipo> CrearLista(Cursor c){
        List<Equipo> team_list = new ArrayList<Equipo>();
        if(c.moveToFirst()){
            do {
                int id = c.getColumnIndex(CN_ID);
                int nom = c.getColumnIndex(CN_NOMBRE);
                int ciu_id = c.getColumnIndex(CN_CIUDAD_ID);
                int us_id = c.getColumnIndex(CN_USER_ID);
                int cat = c.getColumnIndex(CN_CATEGORIA);
                Equipo equip = new Equipo(c.getInt(id),c.getString(nom),c.getInt(ciu_id),c.getInt(us_id),c.getString(cat));
                team_list.add(equip);
            }while(c.moveToNext());
        }
        c.close();
        Cerrar();
        return team_list;
    }


}
