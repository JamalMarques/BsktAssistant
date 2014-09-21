package com.skynet.basketassistant.Activities;

import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Fragments.Frag_expjugador;
import com.skynet.basketassistant.Fragments.Frag_jugadores;
import com.skynet.basketassistant.Fragments.Frag_listapartidos;
import com.skynet.basketassistant.Fragments.Frag_new_player;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Modelo.Jugador;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class JugadoresAct extends BaseActivity implements View.OnClickListener,Frag_jugadores.Callback {

    private Bundle bun_equip;
    private Equipo equipo;

    private ImageButton b_back,b_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores);

        bun_equip = getIntent().getExtras();  //Here i recive the team´s name  ("Nom_Equip")

        b_back = (ImageButton)findViewById(R.id.b_back);
        b_add = (ImageButton)findViewById(R.id.b_add);
        b_back.setOnClickListener(this);
        b_add.setOnClickListener(this);

        DBEquipos dbe = new DBEquipos(this);
        dbe.Modolectura();
        equipo = dbe.DameEquipo(bun_equip.getString(Constants.TEAM_NAME));
        dbe.Cerrar();

        Frag_jugadores frag_jugadores = Frag_jugadores.getInstance(equipo.getId());
        CambiarFragment1(frag_jugadores);
    }

    @Override
    public void onClick(View view) {

        if( view.getId() == b_back.getId()){
            finish();
        }else
            if( view.getId() == b_add.getId()){
                Frag_new_player frag = Frag_new_player.getInstance(equipo.getId());
                CambiarFragment2(frag);
            }
    }

    public void VaciarFragment2(Fragment frag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onSeleccionItemJugador(int id_jug) {

        Frag_expjugador frag = Frag_expjugador.getInstance(id_jug);
        CambiarFragment2(frag);
    }

    public void CambiarFragment1(Fragment frag){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_gridplayers,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.commit();

    }

    public void CambiarFragment2(Fragment frag){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_expplayer,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

}
