package com.skynet.basketassistant.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.skynet.basketassistant.Activities.GameActivity;
import com.skynet.basketassistant.Datos.DBCiudades;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.Activities.JugadoresAct;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Activities.PartidosAct;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;

import java.util.List;

/**
 * Created by jamal on 22/04/14.
 */
public class Frag_expequip extends Fragment implements View.OnClickListener{

    public static final String NAME = "FRAGEXP_TEAM";
    private Equipo equipo;
    private TextView tvequip,tvciudad,tv_games,tv_wins,tv_loses;
    private int num_games,num_wins,num_loses;
    private Button b_eliminar,b_partidos,b_jugadores,b_play;

    public Frag_expequip(){}

    public static Frag_expequip getInstance(int equipo_id){
        Frag_expequip fexp = new Frag_expequip();
        Bundle bun = new Bundle();
        bun.putInt(Constants.TEAM_ID,equipo_id);
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
        equipo = dbe.DameEquipo(getArguments().getInt(Constants.TEAM_ID));
        dbe.Cerrar();

        num_games = 0;
        num_wins = 0;
        num_loses = 0;

/*        b_eliminar = (Button)view.findViewById(R.id.b_eliminar);
        b_eliminar.setOnClickListener(this);*/
        tvequip = (TextView)view.findViewById(R.id.tvnomequip);
        tvciudad = (TextView)view.findViewById(R.id.tvciuteam);
        tv_games = (TextView)view.findViewById(R.id.tv_totalgames);
        tv_wins = (TextView)view.findViewById(R.id.tv_wins);
        tv_loses = (TextView)view.findViewById(R.id.tv_loses);
        b_partidos = (Button)view.findViewById(R.id.bpartidos);
        b_jugadores = (Button)view.findViewById(R.id.bjugadores);
        b_play = (Button)view.findViewById(R.id.bPlay);
        b_partidos.setOnClickListener(this);
        b_jugadores.setOnClickListener(this);
        b_play.setOnClickListener(this);



        DBCiudades dbc = new DBCiudades(getActivity());
        dbc.Modoescritura();

        tvequip.setText(equipo.getNombre());
        tvciudad.setText((dbc.Dameciudad(equipo.getCiudad_id())).getCiudad());

        DBPartidos dbp = new DBPartidos(getActivity());
        dbp.Modolectura();

        List<Partido> gamesList = dbp.giveMeGamesOf(equipo.getId());

        dbp.Cerrar();

        for (int i=0; i < gamesList.size() ; i++) {
            num_games++;
            if (gamesList.get(i).getEquipo1_id() == equipo.getId()) {
                if (gamesList.get(i).getPuntos_E1() > gamesList.get(i).getPuntos_E2())
                    num_wins++;
                else
                    num_loses++;
            }
        }

        tv_games.setText(String.valueOf(num_games));
        tv_wins.setText(String.valueOf(num_wins));
        tv_loses.setText(String.valueOf(num_loses));

        return view;
    }


    @Override
    public void onClick(View view) {

        if( view == b_partidos ){  //presiona el boton de "PARTIDOS"

            Intent intent = new Intent(getActivity(), PartidosAct.class);
            Bundle bun_equip = new Bundle();
            bun_equip.putString(Constants.TEAM_NAME, equipo.getNombre());
            intent.putExtras(bun_equip);
            startActivity(intent);
        }else
            if( view == b_jugadores){ //presiona el boton "JUGADORES"

                Intent intent = new Intent(getActivity(), JugadoresAct.class);
                Bundle bun_equip = new Bundle();
                bun_equip.putString(Constants.TEAM_NAME,equipo.getNombre());
                intent.putExtras(bun_equip);
                startActivity(intent);
            }else
                 if( view == b_play){
                     Intent intent = new Intent(getActivity(), GameActivity.class);
                     Bundle bun_equip = new Bundle();
                     bun_equip.putString(Constants.TEAM_NAME,equipo.getNombre());
                     intent.putExtras(bun_equip);
                     startActivity(intent);
                 }else{
                     if( view == b_eliminar){
                         FragDialog_YesNo fgd = FragDialog_YesNo.getInstance(getActivity().getString(R.string.DeleteTeam),equipo.getId()); //I use the whoCall to send the id to delete
                         fgd.show(getActivity().getSupportFragmentManager(), NAME);
                     }
                 }
    }

}
