package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Adapters.ItemAdapterJugadores;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Jugador;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamal on 09/06/14.
 */
public class Frag_jugadores extends Fragment implements AdapterView.OnItemClickListener {

    private Equipo equipo;
    private GridView gv_jugadores;
    private List<Jugador> lista_jugadores;
    private ProgressBar prog_bar;

    ItemAdapterJugadores adapterjug;

    private Callback m_callback = callbackvacio;

    public Frag_jugadores(){/*Empty constructor*/}

    public static Frag_jugadores getInstance(int equipo_id){
        Frag_jugadores fjug = new Frag_jugadores();
        Bundle bun = new Bundle();
        bun.putInt(Constants.TEAM_ID,equipo_id);
        fjug.setArguments(bun);
        return fjug;
    }


    public interface Callback{
        public void onSeleccionItemJugador(int id_jug);
    }


    public static Callback callbackvacio = new Callback() { //callback vacio para sobreescribir
        @Override
        public void onSeleccionItemJugador(int id_jug) {
            //vacio!
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.frag_jugadores,container,false);

        prog_bar = (ProgressBar)view.findViewById(R.id.prog_bar);

        gv_jugadores = (GridView)view.findViewById(R.id.gv_jugadores);
        gv_jugadores.setOnItemClickListener(this);

        //Bundle
        DBEquipos dbe = new DBEquipos(getActivity());
        dbe.Modolectura();
        equipo = dbe.DameEquipo(getArguments().getInt(Constants.TEAM_ID));
        dbe.Cerrar();

        DBJugadores dbj = new DBJugadores(getActivity());
        dbj.Modolectura();

        lista_jugadores = new ArrayList<Jugador>();
        lista_jugadores = dbj.DameListaJugadoresEquipo(equipo.getId());

        new SetearAdaptador().execute();

        return view;
    }

    @Override
    public void onDetach() {  //desasigno el enlace al callback del activity contenedor
        super.onDetach();
        m_callback = callbackvacio;
    }

    @Override
    public void onAttach(Activity activity) {  //enlazo el metodo de "m_callback" entre la activity contenedora y el fragment.
        super.onAttach(activity);

        if (!(activity instanceof Callback)){    //Pregunto si esta actividad implementa la interfaz "Callback" aqui creada.
            throw new IllegalStateException("Error: La actividad debe implementar el callback del fragmento!!");
        }

        m_callback = (Callback)activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int id_jug = Integer.parseInt(((TextView)view.findViewById(R.id.tv_hidden_idplayer)).getText().toString());

        m_callback.onSeleccionItemJugador(id_jug);
    }

    private class SetearAdaptador extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           adapterjug = new ItemAdapterJugadores(getActivity().getApplicationContext(),lista_jugadores);
           return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            prog_bar.setVisibility(View.GONE);
            gv_jugadores.setAdapter(adapterjug);
            gv_jugadores.setVisibility(View.VISIBLE);
        }
    }

    public void refreshList(){
        DBJugadores dbj = new DBJugadores(getActivity());
        dbj.Modolectura();
        lista_jugadores = dbj.DameListaJugadoresEquipo(equipo.getId());
        new SetearAdaptador().execute();
    }

}
