package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
public class Frag_jugadores extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Equipo equipo;
    private GridView gv_jugadores;
    private List<Jugador> lista_jugadores;
    private ProgressBar prog_bar;
    private LinearLayout addPlayerLayout;

    private ImageButton addPlayerButton;

    private ItemAdapterJugadores adapterjug;

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
        public void onShowAddPlayer();
    }


    public static Callback callbackvacio = new Callback() { //callback vacio para sobreescribir
        @Override
        public void onSeleccionItemJugador(int id_jug) {
            //empty!
        }

        @Override
        public void onShowAddPlayer() {
            //empty
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.frag_jugadores,container,false);

        prog_bar = (ProgressBar)view.findViewById(R.id.prog_bar);
        addPlayerLayout = (LinearLayout)view.findViewById(R.id.addPlayerLayout);
        addPlayerButton = (ImageButton)view.findViewById(R.id.addPlayerButton2);
        addPlayerButton.setOnClickListener(this);

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

        //new SetearAdaptador().execute();
        adapterjug = new ItemAdapterJugadores(getActivity().getApplicationContext(),lista_jugadores,true);
        prog_bar.setVisibility(View.GONE);
        gv_jugadores.setAdapter(adapterjug);
        if(adapterjug.getCount() == 0) {
            gv_jugadores.setVisibility(View.GONE);
            addPlayerLayout.setVisibility(View.VISIBLE);
        }
        else {
            addPlayerLayout.setVisibility(View.GONE);
            gv_jugadores.setVisibility(View.VISIBLE);
        }

        adapterjug.notifyDataSetChanged();

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

    /*private class SetearAdaptador extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           adapterjug = new ItemAdapterJugadores(getActivity().getApplicationContext(),lista_jugadores,true);
           return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            prog_bar.setVisibility(View.GONE);
            gv_jugadores.setAdapter(adapterjug);
            if(adapterjug.getCount() == 0) {
                gv_jugadores.setVisibility(View.GONE);
                addPlayerLayout.setVisibility(View.VISIBLE);
            }
            else {
                addPlayerLayout.setVisibility(View.GONE);
                gv_jugadores.setVisibility(View.VISIBLE);
            }

            adapterjug.notifyDataSetChanged();
        }
    }*/

    public void refreshList(){
        DBJugadores dbj = new DBJugadores(getActivity());
        dbj.Modolectura();
        lista_jugadores = dbj.DameListaJugadoresEquipo(equipo.getId());
        adapterjug = new ItemAdapterJugadores(getActivity().getApplicationContext(),lista_jugadores,true);
        gv_jugadores.setAdapter(adapterjug);
        dbj.Cerrar();
        showHideAddPlayer(adapterjug.getCount() == 0);
    }

    private void showHideAddPlayer(boolean show){
        int visibility = (show)? View.VISIBLE : View.GONE;
        addPlayerLayout.setVisibility(visibility);
    }

    @Override
    public void onClick(View view) {
        if( view == addPlayerButton){
            m_callback.onShowAddPlayer();
        }
    }

    public int getIdOfFirstPlayerOnList(){
        return getIdPlayerPosition(0);
    }

    public int getIdOfLastPlayerOnList(){
        return getIdPlayerPosition(lista_jugadores.size()-1);
    }

    private int getIdPlayerPosition(int position){
        if(lista_jugadores.size() != 0)
            return lista_jugadores.get(position).getId();
        else
            return -1;
    }



}
