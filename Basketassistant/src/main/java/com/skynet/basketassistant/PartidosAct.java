package com.skynet.basketassistant;

import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBPartidos;
import com.skynet.basketassistant.Fragments.Frag_exppart;
import com.skynet.basketassistant.Fragments.Frag_listapartidos;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Partido;
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

/**
 *  Code by Jamal
 */

public class PartidosAct extends Activity implements View.OnClickListener,Frag_listapartidos.Callback {

    private Bundle bun_equip;   //bun_usr.getString("User") ---> nombre del usuario! //bun_usr.getString("Nom_Equip") ---> nombre equipo seleccionado
    private Equipo equipo;

    private Button b_back,b_eliminar;

    private Partido Last_part_pressed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_partidos);

        bun_equip = getIntent().getExtras();   //solo lo uso para bun_usr.getString("Nom_Equip") ---> nombre equipo seleccionado

        DBEquipos dbe = new DBEquipos(this);
        dbe.Modolectura();
        equipo = dbe.DameEquipo(bun_equip.getString("Nom_Equip"));

        //cargo el fragment de la lista de partidos
        Frag_listapartidos frag_l_partidos = new Frag_listapartidos(equipo);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_listpartidos,frag_l_partidos);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.commit();

        b_back = (Button)findViewById(R.id.bback);
        b_eliminar = (Button)findViewById(R.id.b_eliminar);
        b_back.setOnClickListener(this);
        b_eliminar.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        if(view.getId() == b_back.getId()){
            finish();
        }else
            if( view.getId() == b_eliminar.getId() ){
                DBPartidos dbp = new DBPartidos(this);
                dbp.Modoescritura();
                //ACA REALIZO LA ELIMINACION EN CASCADA.
                dbp.Cerrar();
            }
    }


    @Override
    public void onSeleccionItemPartido(Partido part) {
        Last_part_pressed = part;
        Frag_exppart frag = new Frag_exppart(part);
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
}
