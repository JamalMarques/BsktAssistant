package com.skynet.basketassistant.Activities;

import com.google.gson.Gson;
import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.Fragments.Frag_exppart;
import com.skynet.basketassistant.Fragments.Frag_listapartidos;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Partido;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

/**
 *  Code by Jamal
 */

public class PartidosAct extends BaseActivity implements View.OnClickListener,Frag_listapartidos.Callback {

    private Equipo teamSelected;

    private ImageButton b_back;

    private Partido Last_part_pressed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_partidos);

        //bun_equip = getIntent().getExtras();   //solo lo uso para bun_usr.getString("Nom_Equip") ---> nombre equipo seleccionado

        String teamSelectedJSON = getIntent().getExtras().getString(Constants.TEAM_JSON);
        Gson gson = new Gson();
        teamSelected = gson.fromJson(teamSelectedJSON,Equipo.class);

        LoadListFragment(teamSelected.getNombre());

        b_back = (ImageButton)findViewById(R.id.bback);
        b_back.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if(view.getId() == b_back.getId()){
            finish();
        }
    }


    @Override
    public void onSeleccionItemPartido(Partido part) {
        Last_part_pressed = part;
        Frag_exppart frag = Frag_exppart.getInstance(part.getId(),teamSelected.getId());
        CambiarFragmentLayout2(frag);
    }

    public void CambiarFragmentLayout1(Fragment frag){  //Method used for change the "framelayout1" according to our will
        //I will use it for example, where ir need to refresh te listview with the news games.
    }

    public void CambiarFragmentLayout2(Fragment frag){  //Metodo usado como generico para cambiar el "framelayout2" segun nuestra voluntad.

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_exppartidos,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  //Transition effect.
        ft.commit();
    }

    public void LoadListFragment(String nom_equip){
        //cargo el fragment de la lista de partidos
        Frag_listapartidos frag_l_partidos = Frag_listapartidos.getInstance(nom_equip);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_listpartidos,frag_l_partidos);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.commit();
    }
}
