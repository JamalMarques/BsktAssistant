package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.skynet.basketassistant.Adapters.ItemAdapterPartidos;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jamal on 29/05/14.
 */
public class Frag_listapartidos extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvpartidos;
    private Equipo equipo;
    private ItemAdapterPartidos adapterpart;

    private Callback mcallback = callbackvacio;


    public Frag_listapartidos(){/*Empty constructor*/}

    public static Frag_listapartidos getInstance(String nom_equip){
        Frag_listapartidos flp = new Frag_listapartidos();
        Bundle bun = new Bundle();
        bun.putString(Constants.TEAM_NAME,nom_equip);
        flp.setArguments(bun);
        return flp;
    }

    public interface Callback{   //Creo la interfaz que me va a servir para enlazarlo cn el metodo "onSelecciondeItemPartido" del Activity que lo contiene.
        public void onSeleccionItemPartido(Partido part);
    }

    public static Callback callbackvacio = new Callback() {
        @Override
        public void onSeleccionItemPartido(Partido part) {

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.frag_listapartidos,container,false);

        //Bundle
        DBEquipos dbe = new DBEquipos(getActivity());
        dbe.Modolectura();
        equipo = dbe.DameEquipo(getArguments().getString(Constants.TEAM_NAME)); //instancia el equipo
        dbe.Cerrar();

        lvpartidos = (ListView)view.findViewById(R.id.lvpartidos);
        lvpartidos.setOnItemClickListener(this);

        DBPartidos dbp = new DBPartidos(getActivity());
        dbp.Modolectura();
        //Cursor c = dbp.giveMeGamesOf(equipo.getId());
        List<Partido> listapart = dbp.giveMeGamesOf(equipo.getId());
        dbp.Cerrar();

        adapterpart = new ItemAdapterPartidos(getActivity().getApplicationContext(),listapart);
        lvpartidos.setAdapter(adapterpart);

        return view;
    }



    @Override
    public void onAttach(Activity activity) {  //Called once the fragment is associated with its activity.
        super.onAttach(activity);               //Ejecuto el metodo original normalmente

        if (!(activity instanceof Callback)){    //Pregunto si esta actividad implementa la interfaz "Callback" aqui creada. ("Frag_listapartidos.Callback")
            throw new IllegalStateException("Error: La actividad debe implementar el callback del fragmento!!");
        }

        mcallback = (Callback)activity; //Realizo el enlace del metodo "onSelecciondeItemPartido" de "mcallback" de esta clase con el de la "Activity" que contiene el fragment.
    }


    @Override
    public void onDetach() {    //Destruyo el enlace para que al ejecutar el metodo de de "mcallbacks" se ejecute el vacio del estatico de esta clase (callbacksvacios).
        super.onDetach();       //called immediately prior to the fragment no longer being associated with its activity.
        mcallback = callbackvacio;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int idpart = Integer.parseInt(((TextView) view.findViewById(R.id.tv_idpartido)).getText().toString());

        DBPartidos dbp = new DBPartidos(getActivity());
        dbp.Modolectura();
        Partido partido = dbp.DamePartido(idpart);

        mcallback.onSeleccionItemPartido(partido);

    }
}
