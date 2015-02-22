package com.skynet.basketassistant.Activities;

import com.skynet.basketassistant.Datos.DBEquipos;
import com.skynet.basketassistant.Datos.DBJugadores;
import com.skynet.basketassistant.Fragments.FragDialog_YesNo;
import com.skynet.basketassistant.Fragments.Frag_expjugador;
import com.skynet.basketassistant.Fragments.Frag_jugadores;
import com.skynet.basketassistant.Fragments.Frag_new_player;
import com.skynet.basketassistant.Modelo.Equipo;
import com.skynet.basketassistant.Otros.Constants;
import com.skynet.basketassistant.R;
import com.skynet.basketassistant.util.SystemUiHider;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class JugadoresAct extends BaseActivity implements View.OnClickListener,Frag_jugadores.Callback,FragDialog_YesNo.OnCompleteYesNoDialogListener,
                                                            Frag_new_player.onAddPlayerListener{

    private Bundle bun_equip;
    private Equipo equipo;

    private ImageButton b_back,b_add;
    private Frag_jugadores fragPlayers = null;



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
        fragPlayers = frag_jugadores;
    }

    @Override
    public void onClick(View view) {

        if( view == b_back){
            finish();
        }else
            if( view == b_add){
                showAddPlayer();
            }
    }

    /*public void CleanFragment(Fragment frag){
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }*/

    @Override
    public void onSeleccionItemJugador(int id_jug) {

        Frag_expjugador frag = Frag_expjugador.getInstance(id_jug);
        CambiarFragment2(frag);
    }

    private void showAddPlayer(){
        Frag_new_player frag = Frag_new_player.getInstance(equipo.getId());
        CambiarFragment2(frag);
    }

    @Override
    public void onShowAddPlayer() {
        showAddPlayer();
    }

    public void CambiarFragment1(Fragment frag){

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_gridplayers,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        ft.commit();

        fragPlayers = null; //Every time that I change the fragment 1 y will refresh the fragPlayers
    }

    public void CambiarFragment2(Fragment frag){

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_expplayer,frag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onCompleteYesNoDialog(int response, int whocall) {
        if(response == Constants.YES){
            deletePlayer(whocall);
        }
    }

    private void deletePlayer(int playerID){
        DBJugadores dbj = new DBJugadores(this);
        dbj.eliminar(playerID,DBJugadores.CN_ID);
        refreshPlayerList();
        selectPlayerOnScreen(fragPlayers.getIdOfFirstPlayerOnList());
    }

    private void refreshPlayerList(){
        if(fragPlayers!=null){
            fragPlayers.refreshList();
        }
    }

    @Override
    public void onAddPlayer() {
        refreshPlayerList();
        selectPlayerOnScreen(fragPlayers.getIdOfLastPlayerOnList());
    }

    private void selectPlayerOnScreen(int id_player){
        if( id_player != (-1) ){
            onSeleccionItemJugador(id_player);
        }else{ //Doesn't have players
            showAddPlayer();
        }

    }

}
