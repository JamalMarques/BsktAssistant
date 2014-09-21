package com.skynet.basketassistant.Fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skynet.basketassistant.Datos.DBAsistencias;
import com.skynet.basketassistant.Datos.DBFaltas;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Datos.DBLanzamientos;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.Datos.DBRebotes;
import com.skynet.basketassistant.Datos.DBRobos;
import com.skynet.basketassistant.Datos.DBTapones;
import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Modelo.Lanzamiento;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Modelo.Rebote;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.Otros.Manejo_Imagenes;
import com.skynet.basketassistant.Activities.PartidosAct;
import com.skynet.basketassistant.R;

import java.util.List;

/**
 * Created by Jamal on 20/06/14.
 */
public class Frag_exp_jug_part extends Fragment implements View.OnClickListener{

    private String ACERTADO = "acertado";
    private String FALLIDO = "fallido";
    private String OFENSIVO = "Ofensivo";
    private String DEFENSIVO = "Defensivo";

    private Jugador jugador;
    private Partido partido;

    private ImageView iv_photoplayer;
    private ProgressBar load_circle;

    private TextView tv_fecha,tv_puntos,tv_foules,tv_asist,tv_robos,tv_tapas,tv_lanz,tv_acert,tv_fallidos,
                    tv_rebotes,tv_reb_def,tv_reb_ofen;

    private ImageButton b_back;

    /*public Frag_exp_jug_part(Jugador jug,Partido part){
        jugador = jug;
        partido = part;
    }*/

    public Frag_exp_jug_part(){/*Empty constructor*/}

    public static Frag_exp_jug_part getInstance(int id_jug,int id_part){
        Frag_exp_jug_part fejp = new Frag_exp_jug_part();
        Bundle bun = new Bundle();
        bun.putInt(Constants.PLAYER_ID,id_jug);
        bun.putInt(Constants.GAME_ID,id_part);
        fejp.setArguments(bun);
        return fejp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.frag_exp_jug_part,container,false);

        b_back = (ImageButton)view.findViewById(R.id.b_back);
        b_back.setOnClickListener(this);
        load_circle = (ProgressBar)view.findViewById(R.id.load_circle);
        iv_photoplayer = (ImageView)view.findViewById(R.id.iv_photoplayer);

        //Bundle
        DBJugadores dbj = new DBJugadores(getActivity());
        dbj.Modolectura();
        jugador = dbj.DameJugador(getArguments().getInt(Constants.PLAYER_ID));
        DBPartidos dbp = new DBPartidos(getActivity());
        dbp.Modolectura();
        partido = dbp.DamePartido(getArguments().getInt(Constants.GAME_ID));
        dbj.Cerrar();
        dbp.Cerrar();

        new CargaImagen_on_BG().execute();

        tv_fecha = (TextView)view.findViewById(R.id.tv_fecha);
        tv_puntos = (TextView)view.findViewById(R.id.tv_puntos);
        tv_foules = (TextView)view.findViewById(R.id.tv_foules);
        tv_asist = (TextView)view.findViewById(R.id.tv_asist);
        tv_robos = (TextView)view.findViewById(R.id.tv_robos);
        tv_tapas = (TextView)view.findViewById(R.id.tv_tapas);
        tv_lanz = (TextView)view.findViewById(R.id.tv_lanz);
        tv_acert = (TextView)view.findViewById(R.id.tv_acert);
        tv_fallidos = (TextView)view.findViewById(R.id.tv_fallidos);
        tv_rebotes = (TextView)view.findViewById(R.id.tv_rebotes);
        tv_reb_def = (TextView)view.findViewById(R.id.tv_reb_def);
        tv_reb_ofen = (TextView)view.findViewById(R.id.tv_reb_ofen);

        tv_fecha.setText(partido.getFecha().toString());
        tv_puntos.setText(String.valueOf(CantPuntos(jugador.getId(),partido.getId())));
        tv_foules.setText(String.valueOf(CantFoules(jugador.getId(),partido.getId())));
        tv_asist.setText(String.valueOf(CantAsist(jugador.getId(),partido.getId())));
        tv_robos.setText(String.valueOf(CantRob(jugador.getId(),partido.getId())));
        tv_tapas.setText(String.valueOf(CantTap(jugador.getId(),partido.getId())));
        tv_lanz.setText(String.valueOf(CantLanz(jugador.getId(),partido.getId())));
        tv_acert.setText(String.valueOf(CantLanz_acert_fall(jugador.getId(),partido.getId(),ACERTADO)));
        tv_fallidos.setText(String.valueOf(CantLanz_acert_fall(jugador.getId(),partido.getId(),FALLIDO)));
        tv_rebotes.setText(String.valueOf(CantReb(jugador.getId(),partido.getId())));
        tv_reb_ofen.setText(String.valueOf(CantReb_Ofen_Defen(jugador.getId(),partido.getId(),OFENSIVO)));
        tv_reb_def.setText(String.valueOf(CantReb_Ofen_Defen(jugador.getId(),partido.getId(),DEFENSIVO)));

        return view;
    }

    //PODRIA PROBAR CON C.GETCOUNT() PARA MEJORAR EL RENDIMIENTO!

    // ------ Points ------------

        public DBLanzamientos DBLanz(){
            DBLanzamientos dbl = new DBLanzamientos(getActivity());
            dbl.Modolectura();
            return dbl;
        }

        public int CantPuntos(int jug_id,int part_id){
            int cont = 0;
            List<Lanzamiento> list_lanz = DBLanz().Lanzamientos_jugador_en_partido(jug_id,part_id);
            for (int i = 0 ; i < list_lanz.size() ; i++){
                Lanzamiento lanz = list_lanz.get(i);
                cont = cont + lanz.getValor();
            }
            return cont;
        }

        public int CantLanz(int jug_id,int part_id){
            return (DBLanz().Lanzamientos_jugador_en_partido(jug_id,part_id)).size();
        }

        public int CantLanz_acert_fall(int jug_id, int part_id,String tipo){  //tipo= "acertado"/"fallido"
            int acert=0,fall=0;
            List<Lanzamiento> list_lanz = DBLanz().Lanzamientos_jugador_en_partido(jug_id,part_id);
            for (int i = 0 ; i < list_lanz.size() ; i++){
                Lanzamiento lanz = list_lanz.get(i);
                if( lanz.getEfectivo() == 1 )
                    acert++;
                else
                    fall++;
            }
            if(tipo.equals(ACERTADO))
                return acert;
            else
                return fall;
        }
    // ------ /Points -----------

    // ------ Foules ------------
        public int CantFoules(int jug_id,int part_id){
            DBFaltas dbf = new DBFaltas(getActivity());
            dbf.Modolectura();
            return (dbf.ListaFaltas_jugador_partido(jug_id,part_id).size()); //devuelvo cantidad de faltas del jugador en ese partido
        }
    // ------ /Foules -----------


    // ------ Asistencias --------
        public int CantAsist(int jug_id,int part_id){
            DBAsistencias dba = new DBAsistencias(getActivity());
            dba.Modolectura();
            return (dba.ListAsist_jugador_partido(jug_id,part_id)).size();
        }
    // ------ /Asistencias -------

    // ------ Robos -------------
        public int CantRob(int jug_id,int part_id){
            DBRobos dbr = new DBRobos(getActivity());
            dbr.Modolectura();
            return (dbr.ListaRob_jugador_partido(jug_id,part_id)).size();
        }
    // ------- /Robos ------------

    // ----- Tapas --------
        public int CantTap(int jug_id,int part_id){
            DBTapones dbt = new DBTapones(getActivity());
            dbt.Modolectura();
            return (dbt.ListaTap_jugador_partido(jug_id,part_id)).size();
        }
    // ----- /Tapas -------

    //----- Rebotes -----------
        public DBRebotes DBReb(){
            DBRebotes dbr = new DBRebotes(getActivity());
            dbr.Modolectura();
            return dbr;
        }

        public int CantReb(int jug_id,int part_id){
            return (DBReb().ListaReb_jugador_partido(jug_id,part_id)).size();
        }

        public int CantReb_Ofen_Defen(int jug_id,int part_id,String tipo){ //tipo= "Ofensivo"/"Defensivo"
            int cont=0;
            List<Rebote> list_reb = DBReb().ListaReb_jugador_partido(jug_id,part_id);
            for (int i=0 ; i<list_reb.size() ; i++){
                if(list_reb.get(i).getTiporeb().equals(tipo))
                    cont++;
            }
            return cont;
        }

    // ------- /Rebotes --------


    private class CargaImagen_on_BG extends AsyncTask<Void,Void,Void>{

        private Bitmap bit;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                bit = Manejo_Imagenes.Cubo_Rotar_Rotacion(jugador.getImagen_url());
            }catch (Exception e){ bit = Manejo_Imagenes.ImageNoPlayer; }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            iv_photoplayer.setImageBitmap(bit);
            load_circle.setVisibility(View.GONE);
            iv_photoplayer.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View view) {
        if( view.getId() == b_back.getId()){  //if i press back!
            //Frag_exppart frag = new Frag_exppart(partido);
            Frag_exppart frag = Frag_exppart.getInstance(partido.getId());
            ((PartidosAct)getActivity()).CambiarFragmentLayout2(frag);
        }
    }

}
