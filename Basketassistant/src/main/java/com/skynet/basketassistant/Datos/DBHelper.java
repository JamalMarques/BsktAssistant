package com.skynet.basketassistant.Datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jamal on 27/03/14.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "DBbasket.sqlite";
    private static final int DB_SCHEME_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_SCHEME_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.Creartablas(db);
    }

    //se ejecuta cada vez que se realiza una modificacion al esquema de la base de datos.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    private void Creartablas(SQLiteDatabase db){

        db.execSQL(DBAsistencias.crear_tabla());
        db.execSQL(DBCiudades.crear_tabla());
        db.execSQL(DBEquipos.crear_tabla());
        db.execSQL(DBFaltas.crear_tabla());
        db.execSQL(DBJugadores.crear_tabla());
        db.execSQL(DBLanzamientos.crear_tabla());
        db.execSQL(DBPartidos.crear_tabla());
        db.execSQL(DBRebotes.crear_tabla());
        db.execSQL(DBRobos.crear_tabla());
        db.execSQL(DBTapones.crear_tabla());
        db.execSQL(DBUsuarios.crear_tabla());
        db.execSQL(DBJugadores_Partidos.crear_tabla());
    }
}
