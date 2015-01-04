package com.skynet.basketassistant.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBAsistencias;
import com.skynet.basketassistant.Datos.DBFaltas;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Datos.DBJugadores_Partidos;
import com.skynet.basketassistant.Datos.DBLanzamientos;
import com.skynet.basketassistant.Datos.DBRebotes;
import com.skynet.basketassistant.Datos.DBRobos;
import com.skynet.basketassistant.Datos.DBTapones;
import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Modelo.Lanzamiento;
import com.skynet.basketassistant.Modelo.Rebote;
import com.skynet.basketassistant.Modelo.Tapon;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.Otros.Manejo_Imagenes;
import com.skynet.basketassistant.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Jamal on 13/06/14.
 */


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Frag_expjugador extends Fragment implements View.OnClickListener {

    private Jugador jugador;
    private TextView tv_apellido,tv_nombre,tv_altura,tv_peso,tv_rol,tv_numero,tv_juegos,tv_anotaciones,
                    tv_disparos,tv_triples,tv_dobles,tv_simples,tv_efectividad,tv_robos,tv_tapas,tv_asistencias,
                    tv_rebotes,tv_reb_def,tv_reb_ofen;
    private ImageView iv_fotoplayer;

    private Button deleteButton;

    private ProgressBar load_circle;

    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private String Url = Environment.getExternalStorageDirectory()+"/BasketAssistant/Imagenes/";  //Url donde almaceno imagenes del proyecto
    private Intent intent;
    private int codigo;

    private DBLanzamientos dbl;
    private DBRebotes dbr;

    public Frag_expjugador(){/*Empty constructor*/}

    public static Frag_expjugador getInstance(int id_jug){
        Frag_expjugador fexpj = new Frag_expjugador();
        Bundle bun = new Bundle();
        bun.putInt(Constants.PLAYER_ID,id_jug);
        fexpj.setArguments(bun);
        return fexpj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.frag_expjugadores,container,false);

        dbl = new DBLanzamientos(getActivity());
        dbr = new DBRebotes(getActivity());
        dbl.Modolectura();
        dbr.Modolectura();

        //Bundle
        DBJugadores dbj = new DBJugadores(getActivity());
        dbj.Modolectura();
        jugador = dbj.DameJugador(getArguments().getInt(Constants.PLAYER_ID));
        dbj.Cerrar();

        iv_fotoplayer = (ImageView)view.findViewById(R.id.iv_fotoplayer);
        iv_fotoplayer.setOnClickListener(this);
        load_circle = (ProgressBar)view.findViewById(R.id.load_circle);

        new CargarFoto().execute();  //Cargo foto en el iv_fotoplayer

        tv_apellido = (TextView)view.findViewById(R.id.tv_apellido);
        tv_nombre = (TextView)view.findViewById(R.id.tv_nombre);
        tv_altura = (TextView)view.findViewById(R.id.tv_altura);
        tv_peso = (TextView)view.findViewById(R.id.tv_peso);
        tv_rol = (TextView)view.findViewById(R.id.tv_rol);
        tv_numero = (TextView)view.findViewById(R.id.tv_numero);
        tv_juegos = (TextView)view.findViewById(R.id.tv_juegos);
        tv_anotaciones = (TextView)view.findViewById(R.id.tv_anotaciones);
        tv_disparos = (TextView)view.findViewById(R.id.tv_disparos);
        tv_triples = (TextView)view.findViewById(R.id.tv_triples);
        tv_dobles = (TextView)view.findViewById(R.id.tv_dobles);
        tv_simples = (TextView)view.findViewById(R.id.tv_simples);
        tv_efectividad = (TextView)view.findViewById(R.id.tv_efectividad);
        tv_robos = (TextView)view.findViewById(R.id.tv_robos);
        tv_tapas = (TextView)view.findViewById(R.id.tv_tapas);
        tv_asistencias = (TextView)view.findViewById(R.id.tv_asistencias);
        tv_rebotes = (TextView)view.findViewById(R.id.tv_rebotes);
        tv_reb_def = (TextView)view.findViewById(R.id.tv_def);
        tv_reb_ofen = (TextView)view.findViewById(R.id.tv_ofen);
        iv_fotoplayer = (ImageView)view.findViewById(R.id.iv_fotoplayer);

        //Delete button
        deleteButton = (Button)view.findViewById(R.id.button);
        deleteButton.setOnClickListener(this);

        //SIMPLE DATA
        tv_apellido.setText(jugador.getApellido());
        tv_nombre.setText(jugador.getNombre());
        tv_altura.setText(String.valueOf(jugador.getAltura())+"m");
        tv_peso.setText(String.valueOf(jugador.getPeso())+"kg");
        tv_rol.setText(jugador.getRol());
        tv_numero.setText(String.valueOf(jugador.getNumero()));

        //CALCULOS
        tv_tapas.setText(String.valueOf(CantdeTapas()));
        tv_robos.setText(String.valueOf(CantdeRobos()));
        tv_juegos.setText(String.valueOf(CantPartidosJugados()));
        tv_asistencias.setText(String.valueOf(CantAsistencias()));
        tv_disparos.setText(String.valueOf(CantLanzamientos()));
        tv_triples.setText(String.valueOf(CantTriples()));
        tv_dobles.setText(String.valueOf(CantDobles()));
        tv_simples.setText(String.valueOf(CantSimples()));
        tv_anotaciones.setText(String.valueOf(CantAcertadosTotal()));

        //String asd = "hola"; //asd.substring(0,2) = ho
        tv_efectividad.setText((String.valueOf(PorcentajedeTotal())).substring(0,2)+"%");

        tv_rebotes.setText(String.valueOf(CantRebotes()));
        tv_reb_ofen.setText(String.valueOf(CantRebOfen()));
        tv_reb_def.setText(String.valueOf(CantRebDef()));

        dbl.Cerrar();
        dbr.Cerrar();


        return view;
    }


    public int CantdeTapas(){
        DBTapones dbt = new DBTapones(getActivity());
        dbt.Modolectura();
        List<Tapon> lista_tapas = dbt.CrearListaTapones(jugador.getId());
        dbt.Cerrar();
        return  lista_tapas.size();
    }

    public int CantdeRobos(){
        DBRobos dbr = new DBRobos(getActivity());
        dbr.Modolectura();
        int cant = dbr.CrearListaRobos(jugador.getId()).size();
        dbr.Cerrar();
        return cant;
    }

    public int CantPartidosJugados(){
        DBJugadores_Partidos db_jp = new DBJugadores_Partidos(getActivity());
        db_jp.Modolectura();
        int cant = db_jp.Lista_PJ_Jugador(jugador.getId()).size();
        db_jp.Cerrar();
        return cant;
    }

    public int CantAsistencias(){
        DBAsistencias dba = new DBAsistencias(getActivity());
        dba.Modolectura();
        int cant = dba.Lista_Asist_Jugador(jugador.getId()).size();
        dba.Cerrar();
        return cant;
    }

    public int CantFaltas(){
        DBFaltas dbf = new DBFaltas(getActivity());
        dbf.Modolectura();
        int cant = dbf.ListaFaltasJugador(jugador.getId()).size();
        dbf.Cerrar();
        return cant;
    }

    //----------- < LANZAMIENTOS > -------------------

    public int CantLanzamientos(){
        int cant = dbl.ListaLanzamientosJugador(jugador.getId()).size();
        return cant;
    }
    public int CantTriples(){
        int cant = dbl.Lanzamientos_triple_jugador(jugador.getId()).size();
        return cant;
    }
    public int CantDobles(){
        return CantAcertados(dbl.Lanzamientos_doble_jugador(jugador.getId()));
    }
    public int CantSimples(){
        return CantAcertados(dbl.Lanzamientos_simple_jugador(jugador.getId()));
    }

    public int CantAcertados(List<Lanzamiento> list_lanz){  //Calculo los acertados de una lista de lanzamientos
        int cont = 0;
        for( int i = 0 ; i < list_lanz.size() ; i++ ){
            if( (list_lanz.get(i)).getEfectivo() == 1 ){ cont++; }
        }
        return cont;
    }

    public int CantAcertadosTotal(){
        return CantTriples()+CantDobles()+CantSimples();
    }

    public float PorcentajedeTotal(){  //sacar porcentaje en base a "CantAcertadosTotal" y "CantAcertadosTotal"  verificar los de el ((flotante))
        if(CantLanzamientos() == 0)
            return 0;
        else{
            float porc = ((CantAcertadosTotal()*100)/CantLanzamientos()); //porcentaje
            return porc;
        }
    }
    //----------- </ Lanzamientos > ------------------


    //--------- < REBOTES > ------------
    public int CantRebotes(){
        return (dbr.RetobesJugador(jugador.getId())).size();
    }

    public int CantRebOfen(){
        int cont = 0;
        List<Rebote> list_reb = dbr.RetobesJugador(jugador.getId());
        for( int i = 0 ; i < list_reb.size() ; i++ ){
            if( (list_reb.get(i)).getTiporeb().equalsIgnoreCase("Ofensivo") ){ cont++; }
        }
        return cont;
    }

    public int CantRebDef(){
        int cont = 0;
        List<Rebote> list_reb = dbr.RetobesJugador(jugador.getId());
        for( int i = 0 ; i < list_reb.size() ; i++ ){
            if( (list_reb.get(i)).getTiporeb().equalsIgnoreCase("Defensivo") ){ cont++; }
        }
        return cont;
    }
    //-------- </ REBOTES > -----------

    @Override
    public void onClick(View view) {

        if( view.getId() == iv_fotoplayer.getId()){  //Toca la foto del player!

            CharSequence opciones[] = new CharSequence[]{"Ver Imagen","Seleccionar de Galeria","Sacar Foto"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Seleccione Opcion");
            builder.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { // el user clickea en opciones[cual]

                    if( which == 0){ //Selecciona Ver Imagen

                        Toast.makeText(getActivity(), "Ver imageeeen", Toast.LENGTH_SHORT).show();

                    }else{
                        if( which == 1 ){  //Seleccionar de la Galeria
                            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            codigo = SELECT_PICTURE;

                        }else{  //Sacar foto
                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            codigo = TAKE_PICTURE;
                            Uri output = Uri.fromFile(new File(Manejo_Imagenes.Url+jugador.getApellido()+String.valueOf(jugador.getId())+".jpg"));  //ej: Marques27.jpg
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,output);
                        }

                        startActivityForResult(intent, codigo);
                    }
                }
            });
            builder.show();
        }else{
            if(view == deleteButton){
                FragDialog_YesNo fdYN = FragDialog_YesNo.getInstance(getActivity().getString(R.string.DeletePlayer),jugador.getId()); //whoCall is used to send the player id
                fdYN.show(getActivity().getSupportFragmentManager(),"FragDialogYesNo");
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  //Aca vuelve una vez saca la foto o toma de la galeria
        super.onActivityResult(requestCode,resultCode,data);

        if( requestCode == TAKE_PICTURE ){
            DBJugadores dbj = new DBJugadores(getActivity());
            dbj.Modoescritura();
            dbj.CambiarNomImagen_Jugador(jugador.getId(),jugador.getApellido()+String.valueOf(jugador.getId())+".jpg");
            jugador.setImagen_url(jugador.getApellido()+String.valueOf(jugador.getId())+".jpg");  //Ya almaceno el nombre de la imagen en el objeto ya creado
            dbj.Cerrar();
            try{
                Bitmap bm = Manejo_Imagenes.Cubo_Rotar_Rotacion(jugador.getImagen_url());
                if(bm == null)
                    Toast.makeText(getActivity(),getString(R.string.NoFoto),Toast.LENGTH_SHORT).show();
                else{
                    iv_fotoplayer.setImageBitmap(Bitmap.createScaledBitmap(bm,350,350,true));
                }
                //iv_fotoplayer.setImageBitmap(Bitmap.createScaledBitmap(Manejo_Imagenes.Cubo_Rotar_Rotacion(jugador.getImagen_url()),350,350,true)); //Luego probar con el metodo CargarFoto!
            }catch (Exception e){
                System.out.println(e.getMessage().toString());
            }
        }
        else
            if( requestCode == SELECT_PICTURE ){  //Tengo que rotar la imagen desde la URL de la galeria original
                try {
                    Uri selectedimage = data.getData();
                    //selectedimage.getEncodedPath(); sera la url de la imagen?
                    //Toast.makeText(getActivity(),selectedimage.getEncodedPath(),Toast.LENGTH_SHORT).show();
                    InputStream is;
                    is = getActivity().getContentResolver().openInputStream(selectedimage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap bitmap = BitmapFactory.decodeStream(bis);
                    Bitmap postbitmap = Manejo_Imagenes.Hacer_cubo_imagen(bitmap);
                    iv_fotoplayer.setImageBitmap(postbitmap);

                    String imagename = jugador.getApellido()+String.valueOf(jugador.getId()+".jpg");
                    jugador.setImagen_url(imagename);
                    Manejo_Imagenes.Grabar_Imagen(bitmap,imagename);    //Grabo la imagen en la memoria del dispositivo

                    DBJugadores dbj = new DBJugadores(getActivity());       //Guardo en la base de datos el nombre de la imagen al jugador.
                    dbj.Modoescritura();
                    dbj.CambiarNomImagen_Jugador(jugador.getId(),imagename);
                    dbj.Cerrar();

                } catch(Exception e){/*not founded*/
                    Log.e("Error ",e.getMessage().toString());
                }
            }
    }

    private class CargarFoto extends AsyncTask<Void,Void,Void>{

        private Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(Url + jugador.getImagen_url());
            if( file.exists()){
            try {
                //bitmap = Bitmap.createScaledBitmap(Manejo_Imagenes.Cubo_Rotar_Rotacion(jugador.getImagen_url()),350,350,true);
                bitmap = Manejo_Imagenes.Cubo_Rotar_Rotacion2(jugador.getImagen_url(),250,250);
            }catch (Exception e){
                System.out.println(e.getMessage().toString());
            }
            }else{ bitmap = Manejo_Imagenes.ImageNoPlayer; }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            load_circle.setVisibility(View.GONE);
            iv_fotoplayer.setImageBitmap(bitmap);
        }

    }

}
