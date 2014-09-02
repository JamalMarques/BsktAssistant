package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Adapters.ItemAdapterPartidos;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mdpprogram0 on 29/05/14.
 */
public class Frag_listapartidos extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lvpartidos;
    private Equipo equipo;
    private ItemAdapterPartidos adapterpart;

    private Callback mcallback = callbackvacio;


    /*public Frag_listapartidos(Equipo eq){
        equipo = eq;
    }*/

    public Frag_listapartidos(){/*Empty constructor*/}

    public static Frag_listapartidos getInstance(String nom_equip){
        Frag_listapartidos flp = new Frag_listapartidos();
        Bundle bun = new Bundle();
        bun.putString("nom_equip",nom_equip);
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
        equipo = dbe.DameEquipo(getArguments().getString("nom_equip")); //instancia el equipo
        dbe.Cerrar();

        lvpartidos = (ListView)view.findViewById(R.id.lvpartidos);
        lvpartidos.setOnItemClickListener(this);

        DBPartidos dbp = new DBPartidos(getActivity());
        dbp.Modolectura();
        Cursor c = dbp.Cargarcursorpartidos(equipo.getId());

        List<Partido> listapart = new ArrayList<Partido>();

        if(c.moveToFirst()){
            do {
                int id = c.getColumnIndex(dbp.CN_ID);
                int fecha = c.getColumnIndex(dbp.CN_FECHA);
                int cancha = c.getColumnIndex(dbp.CN_CANCHA);
                int puntos_e1 = c.getColumnIndex(dbp.CN_PUNTOS_E1);
                int puntos_e2 = c.getColumnIndex(dbp.CN_PUNTOS_E2);
                int equipo1_id = c.getColumnIndex(dbp.CN_EQUIPO1_ID);
                int equipo2_nom = c.getColumnIndex(dbp.CN_EQUIPO2_NOM);
                int punt_q1_e1 = c.getColumnIndex(dbp.CN_PUNTOS_Q1_E1);
                int punt_q2_e1 = c.getColumnIndex(dbp.CN_PUNTOS_Q2_E1);
                int punt_q3_e1 = c.getColumnIndex(dbp.CN_PUNTOS_Q3_E1);
                int punt_q4_e1 = c.getColumnIndex(dbp.CN_PUNTOS_Q4_E1);
                int punt_q1_e2 = c.getColumnIndex(dbp.CN_PUNTOS_Q1_E2);
                int punt_q2_e2 = c.getColumnIndex(dbp.CN_PUNTOS_Q2_E2);
                int punt_q3_e2 = c.getColumnIndex(dbp.CN_PUNTOS_Q3_E2);
                int punt_q4_e2 = c.getColumnIndex(dbp.CN_PUNTOS_Q4_E2);
                int punt_ext_e1 = c.getColumnIndex(dbp.CN_PUNTOS_EXT_E1);
                int punt_ext_e2 = c.getColumnIndex(dbp.CN_PUNTOS_EXT_E2);

                Partido part = new Partido(c.getInt(id),c.getString(fecha),c.getString(cancha),c.getInt(puntos_e1),c.getInt(puntos_e2),c.getInt(equipo1_id),c.getString(equipo2_nom),
                        c.getInt(punt_q1_e1),c.getInt(punt_q2_e1),c.getInt(punt_q3_e1),c.getInt(punt_q4_e1),c.getInt(punt_q1_e2),c.getInt(punt_q2_e2),c.getInt(punt_q3_e2),
                        c.getInt(punt_q4_e2),c.getInt(punt_ext_e1),c.getInt(punt_ext_e2));

                listapart.add(part);

            }while (c.moveToNext());
        }
        c.close();

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
