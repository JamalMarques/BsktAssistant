package com.skynet.basketassistant.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.tech.NfcBarcode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skynet.basketassistant.Datos.DBCiudades;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.JugadoresAct;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.PartidosAct;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.SelecTeamAct;

import java.lang.reflect.Array;

/**
 * Created by jamal on 22/04/14.
 */
public class Frag_expequip extends Fragment implements View.OnClickListener{

    private Equipo equipo;
    //private Bundle bun_usr;
    private TextView tvequip,tvciudad,tv_games,tv_wins,tv_loses;
    private int num_games,num_wins,num_loses;
    private Button b_eliminar,b_partidos,b_jugadores;

    /*public Frag_expequip(Equipo equip){
        equipo = equip;
    }*/
    public Frag_expequip(){}

    public static Frag_expequip getInstance(int equipo_id){
        Frag_expequip fexp = new Frag_expequip();
        Bundle bun = new Bundle();
        bun.putInt("equipo_id",equipo_id);
        fexp.setArguments(bun);
        return fexp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.frag_expequip,container,false);

        //bun_usr = getArguments();
        DBEquipos dbe = new DBEquipos(getActivity());
        dbe.Modolectura();
        equipo = dbe.DameEquipo(getArguments().getInt("equipo_id"));
        dbe.Cerrar();

        num_games = 0;
        num_wins = 0;
        num_loses = 0;

        b_eliminar = (Button)view.findViewById(R.id.b_eliminar);
        b_eliminar.setOnClickListener(this);
        tvequip = (TextView)view.findViewById(R.id.tvnomequip);
        tvciudad = (TextView)view.findViewById(R.id.tvciuteam);
        tv_games = (TextView)view.findViewById(R.id.tv_totalgames);
        tv_wins = (TextView)view.findViewById(R.id.tv_wins);
        tv_loses = (TextView)view.findViewById(R.id.tv_loses);
        b_partidos = (Button)view.findViewById(R.id.bpartidos);
        b_jugadores = (Button)view.findViewById(R.id.bjugadores);
        b_partidos.setOnClickListener(this);
        b_jugadores.setOnClickListener(this);



        DBCiudades dbc = new DBCiudades(getActivity());
        dbc.Modoescritura();

        tvequip.setText(equipo.getNombre());
        tvciudad.setText((dbc.Dameciudad(equipo.getCiudad_id())).getCiudad());

        DBPartidos dbp = new DBPartidos(getActivity());
        dbp.Modolectura();
        Cursor c = dbp.Cargarcursorpartidos(equipo.getId());

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

               num_games++;
               if( part.getEquipo1_id() == equipo.getId() ){
                    if( part.getPuntos_E1() > part.getPuntos_E2() )
                        num_wins++;
                    else
                        num_loses++;
                }
            }while(c.moveToNext());
        }
        c.close();

        tv_games.setText(String.valueOf(num_games));
        tv_wins.setText(String.valueOf(num_wins));
        tv_loses.setText(String.valueOf(num_loses));

        return view;
    }


    @Override
    public void onClick(View view) {
        //thake care, you need eliminate rows in others tables!!

        if( view.getId() == b_partidos.getId() ){  //presiona el boton de "PARTIDOS"

            Intent intent = new Intent(getActivity(), PartidosAct.class);
            Bundle bun_equip = new Bundle();
            bun_equip.putString("Nom_Equip",equipo.getNombre());
            intent.putExtras(bun_equip);
            startActivity(intent);
        }else
            if( view.getId() == b_jugadores.getId() ){ //presiona el boton "JUGADORES"

                Intent intent = new Intent(getActivity(), JugadoresAct.class);
                Bundle bun_equip = new Bundle();
                bun_equip.putString("Nom_Equip",equipo.getNombre());
                intent.putExtras(bun_equip);
                startActivity(intent);
            }
    }
}
